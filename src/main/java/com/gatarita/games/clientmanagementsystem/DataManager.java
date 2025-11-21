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
//      loadSampleData();
    }

    public void getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlite:app.db");
                logger.info("Connected to database");
                createTable();
            }
        } catch (SQLException e) {
            logger.info(e.toString());
        }
    }

    private void createTable() {
        getConnection();
        String query = "create table if not exists client (id integer not null primary key autoincrement, name text not null, company text, jobTitle text, email, mobile text not null, notes text)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
            logger.info("Table created");
        } catch (SQLException e) {
            logger.info(e.toString());
        }
    }

    private void closeConnection() throws SQLException {
        if (connection != null || !connection.isClosed()) {
            connection.close();
        }
    }

    public void insertClientToDb(Client client) {
        getConnection();
        String query = "insert into client (name, company, jobTitle, email, mobile, notes) values(?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, client.getName());
            statement.setString(2, client.getCompany());
            statement.setString(3, client.getCompany());
            statement.setString(4, client.getEmail());
            statement.setString(5, client.getMobile());
            statement.setString(6, client.getNotes());
            statement.executeUpdate();
            logger.info("Client inserted");

            closeConnection();
        } catch (SQLException e) {
            logger.info(e.toString());
        }
    }

    public void readClientsFromDb() {
        getConnection();
        String query = "select * from client";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                clients.add(new Client(rs.getString("name"), rs.getString("company"), rs.getString("jobTitle"), rs.getString("email"), rs.getString("mobile"), rs.getString("notes")));
            }
            closeConnection();
        } catch (SQLException e) {
            logger.info(e.toString());
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
            logger.info(e.toString());
        }
    }

    public void updateClientsFromDb(Client client) {
        getConnection();
        String query = "update  set name = ?, company = ?, jobTitle = ?, email = ?, mobile = ?, notes = ? where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, client.getName());
            statement.setString(2, client.getCompany());
            statement.setString(3, client.getCompany());
            statement.setString(4, client.getEmail());
            statement.setString(5, client.getMobile());
            statement.setString(6, client.getNotes());
            statement.setInt(7, client.getId());
            statement.executeUpdate();
            logger.info("Client updated");

            closeConnection();
        } catch (SQLException e) {
            logger.info(e.toString());
        }
    }

    public ObservableList<Client> filterClient(String searchText) throws SQLException {
        ObservableList<Client> filtered = FXCollections.observableArrayList();
        getConnection();
        String query = "SELECT * FROM client WHERE name LIKE ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, "%" + searchText + "%");
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
                LocalDate dueDate = rs.getDate("dueDate") != null ? rs.getDate("dueDate").toLocalDate() : null;
                LocalDate startedOn = rs.getDate("startedOn") != null ? rs.getDate("startedOn").toLocalDate() : null;
                Project.Status status = Project.Status.valueOf(rs.getString("status"));
                double cost = rs.getDouble("cost");
                String notes = rs.getString("notes");
                int clientId = rs.getInt("clientId");

                Project p = new Project(name, dueDate, startedOn, status, cost, notes, clientId);
                p.setId(rs.getInt("id"));
                filtered.add(p);
            }
        }
        closeConnection();
        return filtered;
    }


    private void loadSampleData() {
        // Sample clients
        Client c1 = new Client("Ahmad Hassan", "Hasscom Ltd", "Manager", "ahmad@hasscom.com", "0771234567", "Main contact");
        Client c2 = new Client("Kamal Silva", "Silva Corp", "CEO", "kamal@silva.com", "0772345678", "");
        Client c3 = new Client("Nimal Perera", "Perera Industries", "Director", "nimal@perera.com", "0773456789", "Important client");

        clients.addAll(c1, c2, c3);

        // Sample projects
        projects.add(new Project("Hema Issara Project", LocalDate.of(2024, 12, 31), LocalDate.of(2024, 1, 15),
                Project.Status.COMPLETED, 6000, "Completed successfully", c1.getId()));

        projects.add(new Project("That Project", LocalDate.of(2024, 11, 30), null,
                Project.Status.PENDING, 1000, "", c1.getId()));

        projects.add(new Project("Kandikale Project", LocalDate.of(2024, 12, 15), LocalDate.of(2024, 2, 1),
                Project.Status.COMPLETED, 100, "Small project", c2.getId()));

        projects.add(new Project("Yet another Project", LocalDate.of(2025, 3, 30), LocalDate.of(2024, 6, 1),
                Project.Status.ONGOING, 2500, "", c3.getId()));

        projects.add(new Project("Some other Project", LocalDate.of(2024, 10, 30), null,
                Project.Status.PENDING, 999, "", c1.getId()));
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
        deleteClientsFromDb(client.getId());
        clients.remove(client);
        projects.removeIf(p -> p.getClientId() == client.getId());
    }

    public void updateClient(Client oldClient, Client newClient) {
        // sort of a workaround, alternatively maybe we can make client properties observable and update according but this one gets the job done for now
        int index = clients.indexOf(oldClient);
        if (index >= 0) {
            clients.set(index, newClient);
        }
        updateClientsFromDb(newClient);
    }



    public void addProject(Project project) {
        projects.add(project);
    }

    public void removeProject(Project project) {
        projects.remove(project);
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