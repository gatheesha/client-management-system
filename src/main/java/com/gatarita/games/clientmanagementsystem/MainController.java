package com.gatarita.games.clientmanagementsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;

import java.io.IOException;

public class MainController {
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab projectsTab;
    @FXML
    private Tab clientsTab;

    // store the scene if other controllers/dialogs want to use the window as owner
    private Scene scene;

    /**
     * Called from ClientManagementApp after loading the FXML to pass the Scene.
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     * Optional helper to get the window for dialog owners:
     * Platform code can call getSceneWindow() to initOwner(...) for dialogs.
     */
    public javafx.stage.Window getSceneWindow() {
        return scene != null ? scene.getWindow() : null;
    }

    @FXML
    public void initialize() {
        DataManager dataManager = new DataManager();

        try {
            FXMLLoader projectsLoader = new FXMLLoader(getClass().getResource("ProjectsView.fxml"));
            VBox projectsView = projectsLoader.load();
            ProjectsController projectsController = projectsLoader.getController();
            projectsController.setDataManager(dataManager);
            projectsTab.setContent(projectsView);

            FXMLLoader clientsLoader = new FXMLLoader(getClass().getResource("ClientsView.fxml"));
            VBox clientsView = clientsLoader.load();
            ClientsController clientsController = clientsLoader.getController();
            clientsController.setDataManager(dataManager);
            clientsTab.setContent(clientsView);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading FXML files: " + e.getMessage());
        }
    }
}
