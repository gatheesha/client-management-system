package com.gatarita.games.clientmanagementsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;

import java.sql.SQLException;
import java.time.LocalDate;

public class ProjectsController {

    @FXML private TableView<Project> pendingTable;
    @FXML private TableView<Project> ongoingTable;
    @FXML private TableView<Project> completedTable;

    private DataManager dataManager;

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
        initializeTables();
    }

    private void initializeTables() {
        // Initialize Pending Projects Table
        setupProjectTable(pendingTable);
        pendingTable.setItems(dataManager.getProjectsByStatus(Project.Status.PENDING));

        // Initialize Ongoing Projects Table
        setupProjectTable(ongoingTable);
        ongoingTable.setItems(dataManager.getProjectsByStatus(Project.Status.ONGOING));

        // Initialize Completed Projects Table
        setupProjectTable(completedTable);
        completedTable.setItems(dataManager.getProjectsByStatus(Project.Status.COMPLETED));
    }

    private void setupProjectTable(TableView<Project> table) {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Clear existing columns
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
        Dialog<Project> dialog = new Dialog<>();
        dialog.setTitle("New Project");
        dialog.setHeaderText("Add a new project");

        javafx.scene.layout.VBox content = new javafx.scene.layout.VBox(10);
        content.setPadding(new Insets(15));

        TextField nameField = new TextField();
        nameField.setPromptText("Project Name");

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
            if (buttonType == ButtonType.OK && !nameField.getText().isEmpty()) {
                return new Project(nameField.getText(), dueDate.getValue(), startDate.getValue(),
                        statusBox.getValue() != null ? statusBox.getValue() : Project.Status.PENDING,
                        costSpinner.getValue(), notesArea.getText(), 1);
            }
            return null;
        });

        dialog.showAndWait().ifPresent(project -> dataManager.addProject(project));
    }

    @FXML
    private void handleDeleteProject() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Select a project to delete from any table above");
        alert.showAndWait();
    }
//// there is A WRONG WITH I DON'T KNOW WHAT IT IS. OUTPUT OF CREATING NEW PROJECT IS NOT VISIBLE
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
                ObservableList<Project> filtered = dataManager.filterProject(searchText);
                pendingTable.setItems(filtered);
                ongoingTable.setItems(filtered);
                completedTable.setItems(filtered);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

}