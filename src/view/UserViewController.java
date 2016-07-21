package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import application.Album;
import application.PhotoAlbum;
import application.Utility;
import control.ControlledScreen;
import control.ScreensController;

/**
 * This is the class that the user sees when logging in as a user. The user can add albums, delete albums,
 * rename albums, log out, see the number of photos, date of the oldest photo, date range for each album, etc...
 * @author Brian Wong, Laszlo Glant
 *
 */
public class UserViewController implements Initializable, ControlledScreen {

	ScreensController myController;

	@FXML
	private TextField createAlbum;
	@FXML
	private TextField renameAlbum;
	@FXML
	private ListView<String> albumList;
	@FXML
	private ListView<String> numImages;
	@FXML
	private ListView<String> oldestPhoto;
	@FXML
	private ListView<String> dateRange;
	@FXML
	public void handleMouseClick(MouseEvent arg0) {
		setFields();
	}
	public static int index = -1;


	public static ObservableList<String> albums = FXCollections.observableArrayList();
	public static ObservableList<String> numberOfImages = FXCollections.observableArrayList();
	public static ObservableList<String> oldest_photo = FXCollections.observableArrayList();
	public static ObservableList<String> date_range = FXCollections.observableArrayList();

	/**
	 * set index of currently selected album and set renameAlbum text appropriately
	 */
	public void setFields(){
		index = albumList.getSelectionModel().getSelectedIndex();
		if (albums.isEmpty() == true || index < 0) {
			return;
		}
		renameAlbum.setText(albums.get(index));
	}

	/**
	 * getter for albumList
	 * @return albumList
	 */
	public ListView<String> getAlbumList(){
		return albumList;
	}

	/**
	 * called when program starts up
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		//initialize albums here to be displayed in albumList
		albumList.setItems(albums);
		numImages.setItems(numberOfImages);
		oldestPhoto.setItems(oldest_photo);
		dateRange.setItems(date_range);
		//        setFields();
	}

	/**
	 * set screen parent
	 */
	public void setScreenParent(ScreensController screenParent){
		myController = screenParent;
	}

	/**
	 * log out button on user view
	 * @param event
	 */
	@FXML
	private void goToLogin(ActionEvent event){
		myController.setScreen(PhotoAlbum.screen1ID);
		renameAlbum.clear();
		createAlbum.clear();
	}

	/**
	 * Go to Album View button on user view
	 * @param event
	 */
	@FXML
	private void goToAlbum(ActionEvent event){
		System.out.println("index: "+ index);
		if (albums.isEmpty()){
			return;
		}
		else{
			index = -1;
			myController.setScreen(PhotoAlbum.screen4ID);
		}
	}

	/**
	 * quit button, stores all data to disk if user presses this
	 * @param event
	 */
	@FXML
	private void quit(ActionEvent event){
		Utility.output(AdminViewController.users);
		System.exit(0);
	}

	/**
	 * create album button on user view
	 * @param event
	 * @throws IOException
	 */
	@FXML
	private void createAlbum(ActionEvent event) throws IOException{
		String albumName = createAlbum.getText();

		if (albumName.equals("")) {
			// can't add a blank album
			return;
		} else if (AdminViewController.users.get(LogInViewController.currUser).getAlbumList().size() == 0) {//no albums for this user
			createAlbum.clear();
			AdminViewController.users.get(LogInViewController.currUser).getAlbumList().add(new Album(albumName));
			albums.add(albumName);
			albumList.setItems(albums);
			albumList.getSelectionModel().select(0);

			// set number of images in this album to 0 and display
			numberOfImages.add("0");        // observable list
			numImages.setItems(numberOfImages); // list view

			// set oldest photo
			oldest_photo.add("N/A");        // observable list
			oldestPhoto.setItems(oldest_photo);    // list view

			// set date range
			date_range.add("N/A");
			dateRange.setItems(date_range);
		} else {
			for (int i = 0; i < albums.size(); i++) {
				if (albums.get(i).equalsIgnoreCase(albumName)) {
					createAlbum.clear();
					ScreensController.showSameAlbumScene();
					return;
				}
			}
			AdminViewController.users.get(LogInViewController.currUser).getAlbumList().add(new Album(albumName));
			createAlbum.clear();
			albums.add(albumName);
			albumList.setItems(albums);
			albumList.getSelectionModel().select(0);

			// set number of images in this album to 0 and display
			numberOfImages.add("0");    // observable list
			numImages.setItems(numberOfImages); // list view

			// set oldest photo
			oldest_photo.add("N/A");        // observable list
			oldestPhoto.setItems(oldest_photo);    // list view

			// set date range
			date_range.add("N/A");
			dateRange.setItems(date_range);
		}

	}

	/**
	 * rename album button on user view
	 * @param event
	 * @throws IOException
	 */
	@FXML
	private void renameAlbum(ActionEvent event) throws IOException{
		String newName = renameAlbum.getText();

		index = albumList.getSelectionModel().getSelectedIndex();

		if(albums.size() == 0 || renameAlbum.getText().equals("") || index < 0 ){
			renameAlbum.clear();
			return;
		}

		albumList.requestFocus();
		albumList.getSelectionModel().select(index);

		for (int i = 0; i < albums.size(); i++){
			if (albums.get(i).equalsIgnoreCase(newName)){
				renameAlbum.clear();
				ScreensController.showSameAlbumScene();
				return;
			}
		}
		AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(index).setAlbumName(newName);//set new name in array list
		albums.set(index, newName);
		albumList.setItems(albums);//update ListView with new name
		renameAlbum.clear();
	}

	/**
	 * delete album button on user view
	 * @param event
	 * @throws IOException
	 */
	@FXML
	private void deleteAlbum(ActionEvent event) throws IOException{
		index = -1;
		index = albumList.getSelectionModel().getSelectedIndex();
		if(albums.size() == 0 || index < 0){
			return;
		}
		ScreensController.showDeleteAlbumScene();
		renameAlbum.clear();
		albumList.requestFocus();
		albumList.getSelectionModel().select(index);
		albumList.setItems(albums);//re populate ListView

		index = -1;
	}
}