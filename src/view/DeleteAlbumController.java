package view;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Popup so that user can check if really want to delete an album or not
 * @author Brian Wong, Laszlo Glant
 *
 */
public class DeleteAlbumController {
	@FXML
	public Button noButton, yesButton;

	/**
	 * user presses no button
	 * @param event
	 */
	@FXML
	private void noButtonEvent(ActionEvent event) {
		Stage stage = (Stage) noButton.getScene().getWindow();
		stage.close();
	}

	/**
	 * user presses yes button
	 * @param event
	 * @throws IOException
	 */
	@FXML 
	private void yesButtonEvent(ActionEvent event) throws IOException { 
		if (UserViewController.albums.isEmpty()) { 
			return; 
		} else { 
			UserViewController.albums.remove(UserViewController.index); // take out from observable list 
			UserViewController.numberOfImages.remove(UserViewController.index); 
			UserViewController.date_range.remove(UserViewController.index); 
			UserViewController.oldest_photo.remove(UserViewController.index); 
			AdminViewController.users.get(LogInViewController.currUser).getAlbumList().remove(UserViewController.index); //remove item from arrayList 

			// take out from array list 
			Stage stage = (Stage) yesButton.getScene().getWindow(); 
			stage.close(); 
			return; 
		} 
	}
}
