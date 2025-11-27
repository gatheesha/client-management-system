package com.gatarita.games.clientmanagementsystem.controllers;



import com.gatarita.games.clientmanagementsystem.models.Client;
import com.gatarita.games.clientmanagementsystem.database.DataManager;
import com.gatarita.games.clientmanagementsystem.utils.FileExporter;
import com.gatarita.games.clientmanagementsystem.utils.ValidationUtils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import java.io.IOException;

import java.sql.SQLException;

public class ClientsController {

    @FXML
    private TableView<Client> clientTable;

    private DataManager dataManager;

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
        initializeTable();
    }

    private void initializeTable() {
        clientTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        clientTable.getColumns().clear();

        TableColumn<Client, String> nameCol = new TableColumn<>("NAME");
        nameCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        nameCol.setPrefWidth(150);

        TableColumn<Client, String> companyCol = new TableColumn<>("COMPANY");
        companyCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCompany()));
        companyCol.setPrefWidth(150);

        TableColumn<Client, String> jobCol = new TableColumn<>("JOB");
        jobCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getJobTitle()));
        jobCol.setPrefWidth(100);

        TableColumn<Client, String> emailCol = new TableColumn<>("EMAIL");
        emailCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        emailCol.setPrefWidth(200);

        TableColumn<Client, String> mobileCol = new TableColumn<>("MOBILE");
        mobileCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMobile()));
        mobileCol.setPrefWidth(120);

        TableColumn<Client, String> tagsCol = new TableColumn<>("TAGS");
        tagsCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNotes()));
        tagsCol.setPrefWidth(200);

        clientTable.getColumns().addAll(nameCol, companyCol, jobCol, emailCol, mobileCol, tagsCol);

        clientTable.setItems(dataManager.getClients());
    }

    @FXML
    private void handleNewClient() {
        Dialog<Client> dialog = new Dialog<>();
        dialog.setTitle("New Client");
        dialog.setHeaderText("Add a new client");

        javafx.scene.layout.VBox content = new javafx.scene.layout.VBox(10);
        content.setPadding(new Insets(15));

        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");

        TextField companyField = new TextField();
        companyField.setPromptText("Company");

        TextField jobField = new TextField();
        jobField.setPromptText("Job Title");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        TextField mobileField = new TextField();
        mobileField.setPromptText("Mobile Number");

        TextArea notesArea = new TextArea();
        notesArea.setPromptText("Notes/Tags");
        notesArea.setPrefRowCount(4);
        notesArea.setWrapText(true);

        content.getChildren().addAll(
                new Label("Name:"), nameField,
                new Label("Company:"), companyField,
                new Label("Job Title:"), jobField,
                new Label("Email:"), emailField,
                new Label("Mobile:"), mobileField,
                new Label("Notes:"), notesArea
        );

        ScrollPane scrollPane = new ScrollPane(content);
        dialog.getDialogPane().setContent(scrollPane);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK && !nameField.getText().isEmpty()) {
                return new Client(
                        nameField.getText(),
                        companyField.getText(),
                        jobField.getText(),
                        emailField.getText(),
                        mobileField.getText(),
                        notesArea.getText()
                );
            }
            return null;
        });

        dialog.showAndWait().ifPresent(client -> dataManager.addClient(client));
    }

    @FXML
    private void handleDeleteClient() {
        Client selected = clientTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            System.out.println("DEBUG: Attempting to delete client with ID: " + selected.getId());

            if (selected.getId() <= 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Cannot delete client");
                alert.setContentText("Client has invalid ID: " + selected.getId());
                alert.showAndWait();
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Delete Client");
            confirm.setHeaderText("Delete " + selected.getName() + "?");
            confirm.setContentText("This will also delete all projects associated with this client.");

            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    dataManager.removeClient(selected);
                    System.out.println("DEBUG: Client deleted successfully");
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("Please select a client to delete");
            alert.showAndWait();
        }
    }

    @FXML
    public void handleEditClient(ActionEvent actionEvent) {
        Client selected = clientTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            System.out.println("DEBUG: Editing client with ID: " + selected.getId());

            if (selected.getId() <= 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Cannot edit client");
                alert.setContentText("Client has invalid ID: " + selected.getId());
                alert.showAndWait();
                return;
            }

            Dialog<Client> dialog = new Dialog<>();
            dialog.setTitle("Edit Client");
            dialog.setHeaderText("Edit " + selected.getName());

            javafx.scene.layout.VBox content = new javafx.scene.layout.VBox(10);
            content.setPadding(new Insets(15));

            TextField nameField = new TextField();
            nameField.setPromptText("Full Name");
            nameField.setText(selected.getName());

            TextField companyField = new TextField();
            companyField.setPromptText("Company");
            companyField.setText(selected.getCompany());

            TextField jobField = new TextField();
            jobField.setPromptText("Job Title");
            jobField.setText(selected.getJobTitle());

            TextField emailField = new TextField();
            emailField.setPromptText("Email");
            emailField.setText(selected.getEmail());

            TextField mobileField = new TextField();
            mobileField.setPromptText("Mobile Number");
            mobileField.setText(selected.getMobile());

            TextArea notesArea = new TextArea();
            notesArea.setPromptText("Notes/Tags");
            notesArea.setPrefRowCount(4);
            notesArea.setWrapText(true);
            notesArea.setText(selected.getNotes());

            content.getChildren().addAll(
                    new Label("Name:"), nameField,
                    new Label("Company:"), companyField,
                    new Label("Job Title:"), jobField,
                    new Label("Email:"), emailField,
                    new Label("Mobile:"), mobileField,
                    new Label("Notes:"), notesArea
            );

            ScrollPane scrollPane = new ScrollPane(content);
            dialog.getDialogPane().setContent(scrollPane);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.setResultConverter(buttonType -> {
                if (buttonType == ButtonType.OK && !nameField.getText().isEmpty()) {
                    Client updatedClient = new Client(
                            nameField.getText(),
                            companyField.getText(),
                            jobField.getText(),
                            emailField.getText(),
                            mobileField.getText(),
                            notesArea.getText()
                    );
                    updatedClient.setId(selected.getId());
                    return updatedClient;
                }
                return null;
            });

            dialog.showAndWait().ifPresent(client -> {
                dataManager.updateClient(selected, client);
                System.out.println("DEBUG: Client updated successfully");
            });

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Selection");
            alert.setHeaderText("Please select a client to edit");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleFilterClient() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Search Client");
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
                    clientTable.setItems(dataManager.getClients());
                } else {
                    ObservableList<Client> filtered = dataManager.filterClient(searchText);
                    clientTable.setItems(filtered);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    @FXML
    private void handleExportClients() {
        try {
            String filename = "clients_export_" + java.time.LocalDate.now() + ".csv";
            FileExporter.exportToCSV(dataManager.getClients(), filename);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export Successful");
            alert.setHeaderText("Clients exported successfully!");
            alert.setContentText("File saved as: " + filename);
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Export Failed");
            alert.setHeaderText("Failed to export clients");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}