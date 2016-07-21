package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import control.ControlledScreen;
import control.ScreensController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Popup that gives the user a listView of the other albums this user has, can select destination album
 * @author Brian Wong, Laszlo Glant
 *
 */
public class TransferPhotoController implements Initializable, ControlledScreen {
	ScreensController myController;

	@FXML
	public Button noButton;
	@FXML
	public Button yesButton;
	@FXML 
	private ListView<String> albumList;
	public static int destIndex = -1;
	public static ObservableList<String> albums = FXCollections.observableArrayList();
	
	/**
	 * set screen parent
	 */
	public void setScreenParent(ScreensController screenParent){
		myController = screenParent;
	}
	
	@FXML 
	public void handleMouseClick(MouseEvent arg0) {
		
	}

	/**
	 * user presses yes button
	 * @param event
	 * @throws IOException
	 */
	@FXML 
	private void yesButtonEvent(ActionEvent event) throws IOException { 
		destIndex = albumList.getSelectionModel().getSelectedIndex();
		if (albums.isEmpty() || destIndex < 0){
			System.out.println("press yes, destIndex " + destIndex);
			return;
		}
		else {			
			System.out.println("else in yesButtonEvent");
			Stage stage = (Stage) yesButton.getScene().getWindow(); 
			stage.close(); 
			return; 
		}
	}

	/**
	 * program start up
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		//initialize albums here to be displayed in albumList
		albumList.setItems(albums);
	}
}
