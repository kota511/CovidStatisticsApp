import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;

/**
 * The controller for the information panel.
 * 
 */
public class InformationPanelController {

    /**
     * 
     * Closes the current window when the exit button is clicked.
     * 
     * @param event The Action event that triggers the method.
     */
    @FXML
    void exit(ActionEvent event) {
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }
}
