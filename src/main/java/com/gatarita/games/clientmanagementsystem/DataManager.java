package com.gatarita.games.clientmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;

public class DataManager {
    private ObservableList<Client> Clients;
    private ObservableList<Project> projects;

    public DataManager() {
        Clients = FXCollections.observableArrayList();
        projects = FXCollections.observableArrayList();
        loadSampleData();
    }

    private void loadSampleData() {
        // Sample clients
        Client c1 = new Client("Ahmad Hassan", "Hasscom Ltd", "Manager", "ahmad@hasscom.com", "0771234567", "Main contact");
        Client c2 = new Client("Kamal Silva", "Silva Corp", "CEO", "kamal@silva.com", "0772345678", "");
        Client c3 = new Client("Nimal Perera", "Perera Industries", "Director", "nimal@perera.com", "0773456789", "Important client");

        Clients.addAll(c1, c2, c3);

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
        return Clients;
    }

    public ObservableList<Project> getProjects() {
        return projects;
    }

    public void addClient(Client client) {
        Clients.add(client);
    }

    public void removeClient(Client client) {
        Clients.remove(client);
        projects.removeIf(p -> p.getClientId() == client.getId());
    }

    public void addProject(Project project) {
        projects.add(project);
    }

    public void removeProject(Project project) {
        projects.remove(project);
    }

    public Client getClientById(int id) {
        return Clients.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    public ObservableList<Project> getProjectsByStatus(Project.Status status) {
        ObservableList<Project> filtered = FXCollections.observableArrayList();
        projects.stream().filter(p -> p.getStatus() == status).forEach(filtered::add);
        return filtered;
    }
}