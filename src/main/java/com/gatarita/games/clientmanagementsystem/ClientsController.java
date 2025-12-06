package com.gatarita.games.clientmanagementsystem;
/*
*this control the clients view in user interface(UI) 'literally'!!!!
Uses DataManager for database or data operations,
Uses JavaFX Dialogs for input forms
**/

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.SQLException;

@SuppressWarnings("unchecked")
public class ClientsController {

    @FXML
    private TableView<Client> clientTable;
    /*its like private int x;

    * clientTable → the variable name
    * TableView<Client> → the data type (table of Client objects where that can display rows of data, where each row represents a Client object.)
    * (JavaFX UI component)
    */

    /*
     * what i learned;
     * @FXML ---> connects the UI (FXML) with your controller code-----the yellow thing in  intelliJ
     * in .fxml file there is <TableView fx:id="clientTable" />
     */

    @FXML
    private TextField searchField;

    // DataManager handles database operations, the class ClientController don't directly handel data  from the database

    // dataManager is a private variable, type is DataManager which store a dataManager object
    private DataManager dataManager;

    //setter method to inject a DataManager instance(object) into this controller
    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
        initializeTable();
        setupSearchField();
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

    private void setupSearchField() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue == null || newValue.trim().isEmpty()) {
                    clientTable.setItems(dataManager.getClients());
                } else {
                    ObservableList<Client> filtered = dataManager.filterClient(newValue);
                    clientTable.setItems(filtered);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Search Error");
                alert.setHeaderText("Error searching clients");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        });
    }

    @FXML
    private void handleNewClient() {
        Dialog<Client> dialog = new Dialog<>();
        dialog.setTitle("New Client");
        dialog.setHeaderText("Add a new client");

        javafx.scene.layout.VBox content = new javafx.scene.layout.VBox(10);

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

        //Converts dialog input into a Client object.
        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK && !nameField.getText().isEmpty() && !mobileField.getText().isEmpty()) {
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

        //Table Refresh Issues
        //clientTable.refresh() refreshes table,
        // but ObservableList must be updated properly in DataManager
        // -----the client table is not properly refreshed so thetre is an error
        dialog.showAndWait().ifPresent(client -> {
            dataManager.addClient(client);
            clientTable.refresh();
        });
    }

    @FXML
    private void handleDeleteClient() {
        Client selected = clientTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
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
                    clientTable.refresh();
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
                clientTable.refresh();
            });

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Selection");
            alert.setHeaderText("Please select a client to edit");
            alert.showAndWait();
        }
    }

    public void setSearchField(TextField searchField) {
        this.searchField = searchField;
    }

    public void setClientTable(TableView<Client> clientTable) {
        this.clientTable = clientTable;
    }
}
