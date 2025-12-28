package cs102groupproject;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

/**Controller class for the side panel which includes different menus within.
 * @author Gülşen Mercan
 * @date 24/12/2025
 */
public class SidePanelController {
    
    @FXML private StackPane contentArea;

    @FXML private Parent marketView;
    @FXML private Parent storageView;
    @FXML private Parent sessionView;

    /**The market is shown as the default view. */
    @FXML
    public void initialize() {
        showMarket(); 
    }

    @FXML
    /**Brings the storage view, which allows users to buy furniture upfront. */
    private void showMarket() {
        marketView.toFront();
    }

    @FXML
    /**Brings the market view, which allows users to place owned furniture upfront. */
    private void showStorage() {
        storageView.toFront();
    }

    @FXML
    /**Brings the session view, which allows users to create a session upfront. */
    private void showSessions() {
        sessionView.toFront();
    }

    /**Incomplete. */
    @FXML
    private void handleCreateSession() {
        OfficeController.openSessionPopupStatic();
    }

    @FXML
    /**Handles the closing of the side panel. */
    private void handleClose() {
        OfficeController.toggleSidePanelStatic();
    }
}

