package cs102groupproject.Client;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

/**
 * controller for the side panel,
 *  which contains different menus.
 * @author Gülşen Mercan
 */
public class SidePanelController {
    
    @FXML private StackPane contentArea;

    @FXML private Parent marketView;
    @FXML private Parent storageView;
    @FXML private Parent sessionView;

    /**
     * The market is shown as the default view.
     */
    @FXML
    public void initialize() {
        showMarket(); // default
    }

    /**
     * Brings the storage view, 
     * which allows users to buy furniture upfront.
     */
    @FXML
    private void showMarket() {
        marketView.toFront();
    }

    /**
     * Brings the market view, 
     * which allows users to place owned furniture upfront.
     */
    @FXML
    private void showStorage() {
        storageView.toFront();
    }

    /**
     * Brings the session view, 
     * which allows users to create a session upfront.
     */
    @FXML
    private void showSessions() {
        sessionView.toFront();
    }

    @FXML
    private void handleCreateSession() {
        OfficeController.openSessionPopupStatic();
    }

    /**
     * Handles the closing of the side panel.
     */
    @FXML
    private void handleClose() {
        OfficeController.toggleSidePanelStatic();
    }
}
