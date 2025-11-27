package com.gatarita.games.clientmanagementsystem.controllers;

import com.gatarita.games.clientmanagementsystem.database.DataManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;

public class MainController {
    @FXML
    private TabPane tabPane;

    @FXML
    private Tab projectsTab;

    @FXML
    private Tab clientsTab;

    @FXML
    public void initialize() {
        DataManager dataManager = new DataManager();

        try {
            FXMLLoader projectsLoader = new FXMLLoader(
                    getClass().getResource("/com/gatarita/games/clientmanagementsystem/ProjectsView.fxml")
            );
            VBox projectsView = projectsLoader.load();
            ProjectsController projectsController = projectsLoader.getController();
            projectsController.setDataManager(dataManager);
            projectsTab.setContent(projectsView);

            FXMLLoader clientsLoader = new FXMLLoader(
                    getClass().getResource("/com/gatarita/games/clientmanagementsystem/ClientsView.fxml")
            );
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
