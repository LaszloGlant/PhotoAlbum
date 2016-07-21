package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import control.ScreensController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * load album controller
 * @author Brian Wong, Laszlo Glant
 *
 */
public class LoadAlbumController implements Initializable{
	@FXML
	public Button noButton, yesButton;
	@FXML 
	public ListView<String> albumList;
	public static ObservableList<String> albums = FXCollections.observableArrayList();
	ScreensController myController;
	/**
	 * set screen parent
	 */
	public void setScreenParent(ScreensController screenParent){
		myController = screenParent;
	}
	/**
	 * program start up
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		//initialize albums here to be displayed in albumList
		albumList.setItems(albums);
	}
	
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
		UserViewController.index = albumList.getSelectionModel().getSelectedIndex();
		if (albums.isEmpty() || UserViewController.index < 0){
			return;
		}
		else {
			Stage stage = (Stage) yesButton.getScene().getWindow(); 
			stage.close(); 
			return; 
		}
	}
}
