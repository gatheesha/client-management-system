package com.gatarita.games.clientmanagementsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;

import java.sql.SQLException;
import java.time.LocalDate;

@SuppressWarnings("unchecked")
public class ProjectsController {

    @FXML private TableView<Project> pendingTable;
    @FXML private TableView<Project> ongoingTable;
    @FXML private TableView<Project> completedTable;
    @FXML private TableView<Project> cancelledTable;
    @FXML private TextField searchField;

    private DataManager dataManager;
    private ObservableList<Project> pendingProjects;
    private ObservableList<Project> ongoingProjects;
    private ObservableList<Project> completedProjects;
    private ObservableList<Project> cancelledProjects;

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
        initializeTables();
        setupSearchField();
    }

    private void initializeTables() {
        pendingProjects = dataManager.getProjectsByStatus(Project.Status.PENDING);
        ongoingProjects = dataManager.getProjectsByStatus(Project.Status.ONGOING);
        completedProjects = dataManager.getProjectsByStatus(Project.Status.COMPLETED);
        cancelledProjects = dataManager.getProjectsByStatus(Project.Status.CANCELLED);

        setupProjectTable(pendingTable);
        pendingTable.setItems(pendingProjects);

        setupProjectTable(ongoingTable);
        ongoingTable.setItems(ongoingProjects);

        setupProjectTable(completedTable);
        completedTable.setItems(completedProjects);

        setupProjectTable(cancelledTable);
        cancelledTable.setItems(cancelledProjects);
    }

    private void setupSearchField() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue == null || newValue.trim().isEmpty()) {
                    refreshTables();
                } else {
                    ObservableList<Project> filtered = dataManager.filterProject(newValue);

                    pendingProjects.clear();
                    ongoingProjects.clear();
                    completedProjects.clear();
                    cancelledProjects.clear();

                    for (Project p : filtered) {
                        switch (p.getStatus()) {
                            case PENDING:
                                pendingProjects.add(p);
                                break;
                            case ONGOING:
                                ongoingProjects.add(p);
                                break;
                            case COMPLETED:
                                completedProjects.add(p);
                                break;
                            case CANCELLED:
                                cancelledProjects.add(p);
                                break;
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Search Error");
                alert.setHeaderText("Error searching projects");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        });
    }

    private void refreshTables() {
        pendingProjects.clear();
        pendingProjects.addAll(dataManager.getProjectsByStatus(Project.Status.PENDING));

        ongoingProjects.clear();
        ongoingProjects.addAll(dataManager.getProjectsByStatus(Project.Status.ONGOING));

        completedProjects.clear();
        completedProjects.addAll(dataManager.getProjectsByStatus(Project.Status.COMPLETED));

        cancelledProjects.clear();
        cancelledProjects.addAll(dataManager.getProjectsByStatus(Project.Status.CANCELLED));
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
        statusCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus().getDisplayName()));
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
                    setText(item.toString());
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
                    setText(item.toString());
                }
            }
        });

        DatePicker dueDate = new DatePicker();
        DatePicker startDate = new DatePicker();

        ComboBox<Project.Status> statusBox = new ComboBox<>(FXCollections.observableArrayList(Project.Status.values()));
        statusBox.setValue(Project.Status.PENDING);

        TextField costField = new TextField();
        costField.setPromptText("0.00");

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
                new Label("Cost:"), costField,
                new Label("Notes:"), notesArea
        );

        ScrollPane scrollPane = new ScrollPane(content);
        dialog.getDialogPane().setContent(scrollPane);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK && !nameField.getText().isEmpty() && clientBox.getValue() != null) {
                double cost = 0.0;
                try {
                    cost = Double.parseDouble(costField.getText().isEmpty() ? "0" : costField.getText());
                } catch (NumberFormatException e) {
                    cost = 0.0;
                }

                return new Project(nameField.getText(), dueDate.getValue(), startDate.getValue(),
                        statusBox.getValue() != null ? statusBox.getValue() : Project.Status.PENDING,
                        cost, notesArea.getText(), clientBox.getValue().getId());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(project -> {
            dataManager.addProject(project);
            refreshTables();
        });
    }

    @FXML
    private void handleEditProject() {
        Project projectToEdit = getSelectedProject();

        if (projectToEdit == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("Please select a project to edit");
            alert.setContentText("Click on a project in any of the tables above.");
            alert.showAndWait();
            return;
        }

        final Project selected = projectToEdit;

        if (selected.getId() <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot edit project");
            alert.setContentText("Project has invalid ID: " + selected.getId());
            alert.showAndWait();
            return;
        }

        Dialog<Project> dialog = new Dialog<>();
        dialog.setTitle("Edit Project");
        dialog.setHeaderText("Edit " + selected.getName());

        javafx.scene.layout.VBox content = new javafx.scene.layout.VBox(10);
        content.setPadding(new Insets(15));

        TextField nameField = new TextField();
        nameField.setPromptText("Project Name");
        nameField.setText(selected.getName());

        ComboBox<Client> clientBox = new ComboBox<>();
        clientBox.setItems(dataManager.getClients());
        Client currentClient = dataManager.getClientById(selected.getClientId());
        clientBox.setValue(currentClient);
        clientBox.setCellFactory(param -> new ListCell<Client>() {
            @Override
            protected void updateItem(Client item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
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
                    setText(item.toString());
                }
            }
        });

        DatePicker dueDate = new DatePicker();
        dueDate.setValue(selected.getDueDate());

        DatePicker startDate = new DatePicker();
        startDate.setValue(selected.getStartedOn());

        ComboBox<Project.Status> statusBox = new ComboBox<>(FXCollections.observableArrayList(Project.Status.values()));
        statusBox.setValue(selected.getStatus());

        // Cancellation reason field (only shown if cancelled)
        TextArea cancellationReasonArea = new TextArea();
        cancellationReasonArea.setPromptText("Reason for cancellation");
        cancellationReasonArea.setPrefRowCount(3);
        cancellationReasonArea.setWrapText(true);
        cancellationReasonArea.setText(selected.getCancellationReason() != null ? selected.getCancellationReason() : "");
        cancellationReasonArea.setVisible(selected.getStatus() == Project.Status.CANCELLED);
        cancellationReasonArea.setManaged(selected.getStatus() == Project.Status.CANCELLED);

        Label cancellationLabel = new Label("Cancellation Reason:");
        cancellationLabel.setVisible(selected.getStatus() == Project.Status.CANCELLED);
        cancellationLabel.setManaged(selected.getStatus() == Project.Status.CANCELLED);

        statusBox.setOnAction(e -> {
            boolean isCancelled = statusBox.getValue() == Project.Status.CANCELLED;
            cancellationReasonArea.setVisible(isCancelled);
            cancellationReasonArea.setManaged(isCancelled);
            cancellationLabel.setVisible(isCancelled);
            cancellationLabel.setManaged(isCancelled);
        });

        TextField costField = new TextField();
        costField.setPromptText("0.00");
        costField.setText(String.valueOf(selected.getCost()));

        TextArea notesArea = new TextArea();
        notesArea.setPromptText("Notes");
        notesArea.setPrefRowCount(4);
        notesArea.setWrapText(true);
        notesArea.setText(selected.getNotes());

        content.getChildren().addAll(
                new Label("Name:"), nameField,
                new Label("Client:"), clientBox,
                new Label("Due Date:"), dueDate,
                new Label("Started:"), startDate,
                new Label("Status:"), statusBox,
                cancellationLabel, cancellationReasonArea,
                new Label("Cost:"), costField,
                new Label("Notes:"), notesArea
        );

        ScrollPane scrollPane = new ScrollPane(content);
        dialog.getDialogPane().setContent(scrollPane);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK && !nameField.getText().isEmpty() && clientBox.getValue() != null) {
                double cost = 0.0;
                try {
                    cost = Double.parseDouble(costField.getText().isEmpty() ? "0" : costField.getText());
                } catch (NumberFormatException e) {
                    cost = 0.0;
                }

                Project updatedProject = new Project(
                        nameField.getText(),
                        dueDate.getValue(),
                        startDate.getValue(),
                        statusBox.getValue() != null ? statusBox.getValue() : Project.Status.PENDING,
                        cost,
                        notesArea.getText(),
                        clientBox.getValue().getId()
                );
                updatedProject.setId(selected.getId());
                updatedProject.setCompletedOn(selected.getCompletedOn());
                updatedProject.setCancelledOn(selected.getCancelledOn());

                // Update cancellation info
                if (statusBox.getValue() == Project.Status.CANCELLED) {
                    updatedProject.setCancellationReason(cancellationReasonArea.getText());
                    if (selected.getStatus() != Project.Status.CANCELLED) {
                        updatedProject.setCancelledOn(LocalDate.now());
                    }
                } else if (statusBox.getValue() == Project.Status.COMPLETED) {
                    if (selected.getStatus() != Project.Status.COMPLETED) {
                        updatedProject.setCompletedOn(LocalDate.now());
                    }
                }

                return updatedProject;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(project -> {
            dataManager.updateProject(selected, project);
            refreshTables();
        });
    }

    @FXML
    private void handleDeleteProject() {
        Project projectToDelete = getSelectedProject();

        if (projectToDelete != null) {
            final Project selected = projectToDelete;

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

    private Project getSelectedProject() {
        if (pendingTable.getSelectionModel().getSelectedItem() != null) {
            return pendingTable.getSelectionModel().getSelectedItem();
        } else if (ongoingTable.getSelectionModel().getSelectedItem() != null) {
            return ongoingTable.getSelectionModel().getSelectedItem();
        } else if (completedTable.getSelectionModel().getSelectedItem() != null) {
            return completedTable.getSelectionModel().getSelectedItem();
        } else if (cancelledTable.getSelectionModel().getSelectedItem() != null) {
            return cancelledTable.getSelectionModel().getSelectedItem();
        }
        return null;
    }
}
