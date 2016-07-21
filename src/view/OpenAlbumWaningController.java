package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Popup when user enters same name
 * @author Brian Wong, Laszlo Glant
 *
 */
public class OpenAlbumWaningController {

	@FXML
	public Button okButton;

	/**
	 * ok button
	 * @param event
	 */
	@FXML
	private void okButtonEvent(ActionEvent event) {
		Stage stage = (Stage) okButton.getScene().getWindow();
		stage.close();
	}

}
