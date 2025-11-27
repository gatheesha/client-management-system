package com.gatarita.games.clientmanagementsystem.controllers;

import com.gatarita.games.clientmanagementsystem.models.Client;
import com.gatarita.games.clientmanagementsystem.models.Project;
import com.gatarita.games.clientmanagementsystem.database.DataManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;

import java.sql.SQLException;

public class ProjectsController {

    @FXML private TableView<Project> pendingTable;
    @FXML private TableView<Project> ongoingTable;
    @FXML private TableView<Project> completedTable;

    private DataManager dataManager;
    private ObservableList<Project> pendingProjects;
    private ObservableList<Project> ongoingProjects;
    private ObservableList<Project> completedProjects;

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
        initializeTables();
    }

    private void initializeTables() {
        pendingProjects = dataManager.getProjectsByStatus(Project.Status.PENDING);
        ongoingProjects = dataManager.getProjectsByStatus(Project.Status.ONGOING);
        completedProjects = dataManager.getProjectsByStatus(Project.Status.COMPLETED);

        setupProjectTable(pendingTable);
        pendingTable.setItems(pendingProjects);

        setupProjectTable(ongoingTable);
        ongoingTable.setItems(ongoingProjects);

        setupProjectTable(completedTable);
        completedTable.setItems(completedProjects);
    }

    private void refreshTables() {
        pendingProjects.clear();
        pendingProjects.addAll(dataManager.getProjectsByStatus(Project.Status.PENDING));

        ongoingProjects.clear();
        ongoingProjects.addAll(dataManager.getProjectsByStatus(Project.Status.ONGOING));

        completedProjects.clear();
        completedProjects.addAll(dataManager.getProjectsByStatus(Project.Status.COMPLETED));
    }

    private void setupProjectTable(TableView<Project> table) {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.getColumns().clear();

        TableColumn<Project, String> nameCol = new TableColumn<>("PROJECT");
        nameCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        nameCol.setPrefWidth(150);

        TableColumn<Project, Double> amountCol = new TableColumn<>("AMOUNT");
        amountCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getCost()));
        amountCol.setPrefWidth(100);

        TableColumn<Project, String> dueCol = new TableColumn<>("DUE ON");
        dueCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getDueDate() != null ? cellData.getValue().getDueDate().toString() : ""));
        dueCol.setPrefWidth(100);

        TableColumn<Project, String> startedCol = new TableColumn<>("STARTED");
        startedCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getStartedOn() != null ? cellData.getValue().getStartedOn().toString() : ""));
        startedCol.setPrefWidth(100);

        TableColumn<Project, String> clientCol = new TableColumn<>("CLIENTS");
        clientCol.setCellValueFactory(cellData -> {
            Client client = dataManager.getClientById(cellData.getValue().getClientId());
            return new javafx.beans.property.SimpleStringProperty(client != null ? client.getName() : "");
        });
        clientCol.setPrefWidth(150);

        TableColumn<Project, String> statusCol = new TableColumn<>("STATUS");
        statusCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus().toString()));
        statusCol.setPrefWidth(100);

        table.getColumns().addAll(nameCol, amountCol, dueCol, startedCol, clientCol, statusCol);
    }

    @FXML
    private void handleNewProject() {
        if (dataManager.getClients().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Clients");
            alert.setHeaderText("Please add a client first");
            alert.setContentText("You need to have at least one client before creating a project.");
            alert.showAndWait();
            return;
        }

        Dialog<Project> dialog = new Dialog<>();
        dialog.setTitle("New Project");
        dialog.setHeaderText("Add a new project");

        javafx.scene.layout.VBox content = new javafx.scene.layout.VBox(10);
        content.setPadding(new Insets(15));

        TextField nameField = new TextField();
        nameField.setPromptText("Project Name");

        ComboBox<Client> clientBox = new ComboBox<>();
        clientBox.setItems(dataManager.getClients());
        clientBox.setPromptText("Select Client");
        clientBox.setCellFactory(param -> new ListCell<Client>() {
            @Override
            protected void updateItem(Client item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " - " + item.getCompany());
                }
            }
        });
        clientBox.setButtonCell(new ListCell<Client>() {
            @Override
            protected void updateItem(Client item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " - " + item.getCompany());
                }
            }
        });

        DatePicker dueDate = new DatePicker();
        DatePicker startDate = new DatePicker();

        ComboBox<Project.Status> statusBox = new ComboBox<>(FXCollections.observableArrayList(Project.Status.values()));
        statusBox.setValue(Project.Status.PENDING);

        Spinner<Integer> costSpinner = new Spinner<>(0, 100000, 0, 100);

        TextArea notesArea = new TextArea();
        notesArea.setPromptText("Notes");
        notesArea.setPrefRowCount(4);
        notesArea.setWrapText(true);

        content.getChildren().addAll(
                new Label("Name:"), nameField,
                new Label("Client:"), clientBox,
                new Label("Due Date:"), dueDate,
                new Label("Started:"), startDate,
                new Label("Status:"), statusBox,
                new Label("Cost:"), costSpinner,
                new Label("Notes:"), notesArea
        );

        ScrollPane scrollPane = new ScrollPane(content);
        dialog.getDialogPane().setContent(scrollPane);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK && !nameField.getText().isEmpty() && clientBox.getValue() != null) {
                return new Project(nameField.getText(), dueDate.getValue(), startDate.getValue(),
                        statusBox.getValue() != null ? statusBox.getValue() : Project.Status.PENDING,
                        costSpinner.getValue(), notesArea.getText(), clientBox.getValue().getId());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(project -> {
            dataManager.addProject(project);
            refreshTables();
        });
    }

    @FXML
    private void handleDeleteProject() {
        Project projectToDelete = null;

        if (pendingTable.getSelectionModel().getSelectedItem() != null) {
            projectToDelete = pendingTable.getSelectionModel().getSelectedItem();
        } else if (ongoingTable.getSelectionModel().getSelectedItem() != null) {
            projectToDelete = ongoingTable.getSelectionModel().getSelectedItem();
        } else if (completedTable.getSelectionModel().getSelectedItem() != null) {
            projectToDelete = completedTable.getSelectionModel().getSelectedItem();
        }

        if (projectToDelete != null) {
            final Project selected = projectToDelete;

            System.out.println("DEBUG: Attempting to delete project with ID: " + selected.getId());

            if (selected.getId() <= 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Cannot delete project");
                alert.setContentText("Project has invalid ID: " + selected.getId());
                alert.showAndWait();
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Delete Project");
            confirm.setHeaderText("Delete " + selected.getName() + "?");
            confirm.setContentText("This action cannot be undone.");

            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    dataManager.removeProject(selected);
                    refreshTables();
                    System.out.println("DEBUG: Project deleted successfully");
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("Please select a project to delete");
            alert.setContentText("Click on a project in any of the tables above.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleFilterProject() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Search Project");
        dialog.setHeaderText("Text to search");

        TextField queryField = new TextField();
        queryField.setPromptText("Search query");

        javafx.scene.layout.VBox content = new javafx.scene.layout.VBox(10);
        content.getChildren().addAll(new Label("Name:"), queryField);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                return queryField.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(searchText -> {
            try {
                if (searchText.trim().isEmpty()) {
                    refreshTables();
                } else {
                    ObservableList<Project> filtered = dataManager.filterProject(searchText);
                    pendingTable.setItems(filtered);
                    ongoingTable.setItems(filtered);
                    completedTable.setItems(filtered);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

}