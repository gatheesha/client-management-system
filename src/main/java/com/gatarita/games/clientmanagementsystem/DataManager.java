package com.gatarita.games.clientmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Logger;

public class DataManager {
    private Connection connection;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    private ObservableList<Client> clients;
    private ObservableList<Project> projects;

    public DataManager() {
        clients = FXCollections.observableArrayList();
        projects = FXCollections.observableArrayList();
        readClientsFromDb();
        readProjectsFromDb();
    }

    public void getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlite:app.db");
                logger.info("Connected to database");
                createTable();
            }
        } catch (SQLException e) {
            logger.severe(e.toString());
        }
    }

    private void createTable() {
        getConnection();

        String query = "create table if not exists client (id integer not null primary key autoincrement, name text not null, company text, jobTitle text, email text, mobile text not null, notes text)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
            logger.info("Client table created");
        } catch (SQLException e) {
            logger.severe(e.toString());
        }

        String projectQuery = "create table if not exists project (id integer not null primary key autoincrement, name text not null, dueDate text, startedOn text, completedOn text, cancelledOn text, status text not null, cost real, notes text, cancellationReason text, clientId integer)";
        try (PreparedStatement statement = connection.prepareStatement(projectQuery)) {
            statement.executeUpdate();
            logger.info("Project table created");
        } catch (SQLException e) {
            logger.severe(e.toString());
        }
    }

    private void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public void insertClientToDb(Client client) {
        getConnection();
        String query = "insert into client (name, company, jobTitle, email, mobile, notes) values(?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, client.getName());
            statement.setString(2, client.getCompany());
            statement.setString(3, client.getJobTitle());
            statement.setString(4, client.getEmail());
            statement.setString(5, client.getMobile());
            statement.setString(6, client.getNotes());
            statement.executeUpdate();

            Statement idStatement = connection.createStatement();
            ResultSet rs = idStatement.executeQuery("SELECT last_insert_rowid()");
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                client.setId(generatedId);
                logger.info("Client inserted with ID: " + generatedId);
            }
            rs.close();
            idStatement.close();

        } catch (SQLException e) {
            logger.severe("Error inserting client: " + e.toString());
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                closeConnection();
            } catch (SQLException e) {
                logger.severe("Error closing resources: " + e.toString());
            }
        }
    }

    public void readClientsFromDb() {
        getConnection();
        String query = "select * from client";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Client c = new Client(
                        rs.getString("name"),
                        rs.getString("company"),
                        rs.getString("jobTitle"),
                        rs.getString("email"),
                        rs.getString("mobile"),
                        rs.getString("notes")
                );
                c.setId(rs.getInt("id"));
                clients.add(c);
            }
            logger.info("Loaded " + clients.size() + " clients from database");
            closeConnection();
        } catch (SQLException e) {
            logger.severe(e.toString());
        }
    }

    public void readProjectsFromDb() {
        getConnection();
        String query = "select * from project";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                LocalDate dueDate = rs.getString("dueDate") != null ? LocalDate.parse(rs.getString("dueDate")) : null;
                LocalDate startedOn = rs.getString("startedOn") != null ? LocalDate.parse(rs.getString("startedOn")) : null;
                LocalDate completedOn = rs.getString("completedOn") != null ? LocalDate.parse(rs.getString("completedOn")) : null;
                LocalDate cancelledOn = rs.getString("cancelledOn") != null ? LocalDate.parse(rs.getString("cancelledOn")) : null;
                Project.Status status = Project.Status.fromDisplayName(rs.getString("status"));

                Project project = new Project(
                        rs.getString("name"),
                        dueDate,
                        startedOn,
                        status,
                        rs.getDouble("cost"),
                        rs.getString("notes"),
                        rs.getInt("clientId")
                );
                project.setId(rs.getInt("id"));
                project.setCompletedOn(completedOn);
                project.setCancelledOn(cancelledOn);
                project.setCancellationReason(rs.getString("cancellationReason"));
                projects.add(project);
            }
            logger.info("Loaded " + projects.size() + " projects from database");
            closeConnection();
        } catch (SQLException e) {
            logger.severe(e.toString());
        }
    }

    public void deleteClientsFromDb(int id) {
        getConnection();
        String query = "delete from client where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            logger.info("Client " + id + " deleted");
            closeConnection();
        } catch (SQLException e) {
            logger.severe(e.toString());
        }
    }

    public void deleteProjectFromDb(int id) {
        getConnection();
        String query = "delete from project where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            logger.info("Project " + id + " deleted");
            closeConnection();
        } catch (SQLException e) {
            logger.severe(e.toString());
        }
    }

    public void updateClientsFromDb(Client client) {
        getConnection();
        String query = "update client set name = ?, company = ?, jobTitle = ?, email = ?, mobile = ?, notes = ? where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, client.getName());
            statement.setString(2, client.getCompany());
            statement.setString(3, client.getJobTitle());
            statement.setString(4, client.getEmail());
            statement.setString(5, client.getMobile());
            statement.setString(6, client.getNotes());
            statement.setInt(7, client.getId());
            statement.executeUpdate();
            logger.info("Client updated");
            closeConnection();
        } catch (SQLException e) {
            logger.severe(e.toString());
        }
    }

    public void insertProjectToDb(Project project) {
        getConnection();
        String query = "insert into project (name, dueDate, startedOn, completedOn, cancelledOn, status, cost, notes, cancellationReason, clientId) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, project.getName());
            statement.setString(2, project.getDueDate() != null ? project.getDueDate().toString() : null);
            statement.setString(3, project.getStartedOn() != null ? project.getStartedOn().toString() : null);
            statement.setString(4, project.getCompletedOn() != null ? project.getCompletedOn().toString() : null);
            statement.setString(5, project.getCancelledOn() != null ? project.getCancelledOn().toString() : null);
            statement.setString(6, project.getStatus().name());
            statement.setDouble(7, project.getCost());
            statement.setString(8, project.getNotes());
            statement.setString(9, project.getCancellationReason());
            statement.setInt(10, project.getClientId());
            statement.executeUpdate();

            Statement idStatement = connection.createStatement();
            ResultSet rs = idStatement.executeQuery("SELECT last_insert_rowid()");
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                project.setId(generatedId);
                logger.info("Project inserted with ID: " + generatedId);
            }
            rs.close();
            idStatement.close();

        } catch (SQLException e) {
            logger.severe("Error inserting project: " + e.toString());
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                closeConnection();
            } catch (SQLException e) {
                logger.severe("Error closing resources: " + e.toString());
            }
        }
    }

    public void updateProjectFromDb(Project project) {
        getConnection();
        String query = "update project set name = ?, dueDate = ?, startedOn = ?, completedOn = ?, cancelledOn = ?, status = ?, cost = ?, notes = ?, cancellationReason = ?, clientId = ? where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, project.getName());
            statement.setString(2, project.getDueDate() != null ? project.getDueDate().toString() : null);
            statement.setString(3, project.getStartedOn() != null ? project.getStartedOn().toString() : null);
            statement.setString(4, project.getCompletedOn() != null ? project.getCompletedOn().toString() : null);
            statement.setString(5, project.getCancelledOn() != null ? project.getCancelledOn().toString() : null);
            statement.setString(6, project.getStatus().toString());
            statement.setDouble(7, project.getCost());
            statement.setString(8, project.getNotes());
            statement.setString(9, project.getCancellationReason());
            statement.setInt(10, project.getClientId());
            statement.setInt(11, project.getId());
            statement.executeUpdate();
            logger.info("Project updated");
            closeConnection();
        } catch (SQLException e) {
            logger.severe(e.toString());
        }
    }

    public ObservableList<Client> filterClient(String searchText) throws SQLException {
        ObservableList<Client> filtered = FXCollections.observableArrayList();
        getConnection();
        String query = "SELECT * FROM client WHERE name LIKE ? OR company LIKE ? OR email LIKE ? OR mobile LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            String searchPattern = "%" + searchText + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ps.setString(4, searchPattern);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Client c = new Client(
                        rs.getString("name"),
                        rs.getString("company"),
                        rs.getString("jobTitle"),
                        rs.getString("email"),
                        rs.getString("mobile"),
                        rs.getString("notes")
                );
                c.setId(rs.getInt("id"));
                filtered.add(c);
            }
        }
        closeConnection();
        return filtered;
    }

    public ObservableList<Project> filterProject(String searchText) throws SQLException {
        ObservableList<Project> filtered = FXCollections.observableArrayList();
        getConnection();
        String query = "SELECT * FROM project WHERE name LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, "%" + searchText + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                LocalDate dueDate = rs.getString("dueDate") != null ? LocalDate.parse(rs.getString("dueDate")) : null;
                LocalDate startedOn = rs.getString("startedOn") != null ? LocalDate.parse(rs.getString("startedOn")) : null;
                LocalDate completedOn = rs.getString("completedOn") != null ? LocalDate.parse(rs.getString("completedOn")) : null;
                LocalDate cancelledOn = rs.getString("cancelledOn") != null ? LocalDate.parse(rs.getString("cancelledOn")) : null;
                Project.Status status = Project.Status.valueOf(rs.getString("status"));
                double cost = rs.getDouble("cost");
                String notes = rs.getString("notes");
                String cancellationReason = rs.getString("cancellationReason");
                int clientId = rs.getInt("clientId");

                Project p = new Project(name, dueDate, startedOn, status, cost, notes, clientId);
                p.setId(rs.getInt("id"));
                p.setCompletedOn(completedOn);
                p.setCancelledOn(cancelledOn);
                p.setCancellationReason(cancellationReason);
                filtered.add(p);
            }
        }
        closeConnection();
        return filtered;
    }

    public ObservableList<Client> getClients() {
        return clients;
    }

    public ObservableList<Project> getProjects() {
        return projects;
    }

    public void addClient(Client client) {
        insertClientToDb(client);
        clients.add(client);
    }

    public void removeClient(Client client) {
        if (client.getId() <= 0) {
            logger.warning("Cannot delete client with invalid ID: " + client.getId());
            return;
        }

        getConnection();
        String projectDeleteQuery = "delete from project where clientId = ?";
        try (PreparedStatement statement = connection.prepareStatement(projectDeleteQuery)) {
            statement.setInt(1, client.getId());
            int deletedProjects = statement.executeUpdate();
            logger.info("Deleted " + deletedProjects + " projects for client " + client.getId());
            closeConnection();
        } catch (SQLException e) {
            logger.severe(e.toString());
        }

        deleteClientsFromDb(client.getId());
        clients.remove(client);
        projects.removeIf(p -> p.getClientId() == client.getId());
    }

    public void updateClient(Client oldClient, Client newClient) {
        int index = clients.indexOf(oldClient);
        if (index >= 0) {
            clients.set(index, newClient);
        }
        updateClientsFromDb(newClient);
    }

    public void addProject(Project project) {
        insertProjectToDb(project);
        projects.add(project);
    }

    public void removeProject(Project project) {
        if (project.getId() <= 0) {
            logger.warning("Cannot delete project with invalid ID: " + project.getId());
            return;
        }
        deleteProjectFromDb(project.getId());
        projects.remove(project);
    }

    public void updateProject(Project oldProject, Project newProject) {
        int index = projects.indexOf(oldProject);
        if (index >= 0) {
            projects.set(index, newProject);
        }
        updateProjectFromDb(newProject);
    }

    public Client getClientById(int id) {
        return clients.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    public ObservableList<Project> getProjectsByStatus(Project.Status status) {
        ObservableList<Project> filtered = FXCollections.observableArrayList();
        projects.stream().filter(p -> p.getStatus() == status).forEach(filtered::add);
        return filtered;
    }
}
