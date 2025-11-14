package com.gatarita.games.clientmanagementsystem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.geometry.Insets;

public class ClientsController {

    @FXML private TableView<Client> clientTable;

    private DataManager dataManager;

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
        initializeTable();
    }

    private void initializeTable() {
        clientTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Clear existing columns
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

        // IMPORTANT: Binds the table to the observable list
        // This automatically updates the table when clients list changes
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
                return new Client(nameField.getText(), companyField.getText(), jobField.getText(),
                        emailField.getText(), mobileField.getText(), notesArea.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(client -> dataManager.addClient(client));
    }

    @FXML
    private void handleDeleteClient() {
        Client selected = clientTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Delete Client");
            confirm.setHeaderText("Delete " + selected.getName() + "?");
            confirm.setContentText("This will also delete all projects associated with this client.");

            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    dataManager.removeClient(selected);
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("Please select a client to delete");
            alert.showAndWait();
        }
    }

    public void handleEditClient(ActionEvent actionEvent) {
    }
}