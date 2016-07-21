package view;

import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Popup that allows admin to check if really want to delete user or not
 * @author Brian Wong, Laszlo Glant
 *
 */
public class DeleteUserController {
	@FXML
	public Button noButton, yesButton;

	/**
	 * press no button
	 * @param event
	 */
	@FXML
	private void noButtonEvent(ActionEvent event) {
		Stage stage = (Stage) noButton.getScene().getWindow();
		stage.close();
	}

	/**
	 * press yes button
	 * @param event
	 * @throws IOException
	 */
	@FXML
	private void yesButtonEvent(ActionEvent event) throws IOException {

		if (AdminViewController.userObs.isEmpty()) {
			return;
		} else {

			System.out.println("userName to be deleted "+ AdminViewController.users.get(AdminViewController.deleteIndex).getUserName());
			File dirName = new File (AdminViewController.users.get(AdminViewController.deleteIndex).getUserName());
			if (dirName.exists()){

				try{
					dirName.delete();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			AdminViewController.userObs.remove(AdminViewController.deleteIndex);	// take out from observable list
			AdminViewController.users.remove(AdminViewController.deleteIndex);						// take out from array list
			Stage stage = (Stage) yesButton.getScene().getWindow();
			stage.close();
			return;

		}
	}
}
