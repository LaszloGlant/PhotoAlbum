package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import application.MyImage;
import application.PhotoAlbum;
import application.PhotoSearch;
import application.Utility;
import application.Album;
import control.ControlledScreen;
import control.ScreensController;

/**
 * This class is the controller for the album view screen. This is the screen the user sees
 * when an album is open. The user can add photos, remove photos, transfer a photo to a different album,
 * do searches for fields and for images within 2 dates, set tags, create an album with the search results,
 * cycle forward and backwards through the images in this album, etc....
 * @author Brian Wong, Laszlo Glant
 *
 */
public class AlbumViewController implements Initializable, ControlledScreen {

	ScreensController myController;

	@FXML
	private TextField location;
	@FXML
	private TextField people;
	@FXML
	private TextField month;
	@FXML
	private TextField day;
	@FXML
	private TextField year;   
	@FXML
	private ImageView bigScreen;
	@FXML
	public GridPane miniImages;
	@FXML
	private TextArea captionText;
	@FXML
	private Button cycleF;
	@FXML
	private Button cycleB;
	@FXML
	private Button quit;

	@FXML
	private TextField month2;
	@FXML
	private TextField day2;
	@FXML
	private TextField year2;

	@FXML
	private TextField createAlbumName;

	public static int albumIndex;        // index of the album that is currently open (selected)
	public static int column = -1;        // column of place to add at
	public static int row = 0;            // row of place to add at
	public static int currSelect = 0;    // index of image that is currently selected

	public static ArrayList<MyImage> savedImages = new ArrayList<MyImage>();    // used to save copy of
	public static ArrayList<MyImage> searchResults = new ArrayList<MyImage>();    // results of search fields or search by date result
	public static ObservableList<String> picturePath = FXCollections.observableArrayList();
	public static int init = 0;
	ImageView imageView;
	Image image;
	File file;

	int grid_x, grid_y;

	/**
	 * Quit button in upper right of album view
	 * @param event
	 */
	@FXML
	private void quit(ActionEvent event){
		Utility.output(AdminViewController.users);
		System.exit(0);
	}

	/**
	 * mouse click on grid pane
	 * @param event
	 */
	@FXML
	public void handleMouseClick(MouseEvent event) {

		for( Node node: miniImages.getChildren()) {
			if( node instanceof ImageView) {
				System.out.println("AlbumViewController mouse clicked in for >>>>> in 1st if "+(event.getSceneX()-40)+" "+(event.getSceneY()-187)
						+" \nLocal bounds "+node.getBoundsInLocal()
						+" \nParentBounds "+node.getBoundsInParent()
						+" \nLayoutBounds "+node.getLayoutBounds());

				System.out.println("AlbumViewController mouse clicked in for >>>>> in 1st if "+GridPane.getRowIndex( node)+" "+GridPane.getRowIndex( node));
				if( node.getBoundsInParent().contains((event.getSceneX()-40),  (event.getSceneY()-187))) {
					System.out.println( "Node: " + node + " at " + GridPane.getRowIndex( node) + "/" + GridPane.getRowIndex( node));
				}
			}
		}
		//setDisplay();
	}

	/**
	 * when this screen is initialized on program start up
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}

	/**
	 * sets screen parent
	 */
	public void setScreenParent(ScreensController screenParent){
		myController = screenParent;
	}

	/**
	 * button labeled "Close Album" on album view
	 * @param event
	 */
	@FXML
	private void goToUser(ActionEvent event){
		//clears the 4 observableLists
		UserViewController.albums.clear();
		UserViewController.numberOfImages.clear();
		UserViewController.date_range.clear();
		UserViewController.oldest_photo.clear();

		for (int j = 0; j < AdminViewController.users.get(LogInViewController.currUser).getAlbumList().size(); j++) {//set album ObservableList
			// loop through all albums

			// update 4 obsLists

			// 1. update users
			UserViewController.albums.add(AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(j).getAlbumName());

			// 2. update number of images
			UserViewController.numberOfImages.add(AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(j).getPicsList().size()+"");//images in albums

			// 3. update date range
			UserViewController.date_range.add(Utility.dateRange(LogInViewController.currUser, j));

			// 4. update oldest photo
			UserViewController.oldest_photo.add(Utility.oldestPhoto(LogInViewController.currUser, j));

		}

		myController.setScreen(PhotoAlbum.screen3ID);
		miniImages.getChildren().clear();
		bigScreen.setImage(null);

		column = -1;
		row = 0;

		clearTexts();
	}


	/**
	 * add photo button on album view
	 * @param event
	 */
	@FXML
	private void addPhoto(ActionEvent event){
		try {           
			if (UserViewController.index == -1) {
				ScreensController.showOpenAlbumWaningScene();
				return;
			}

			FileChooser chooser = new FileChooser();
			chooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("All Files", "*.*"),
					new FileChooser.ExtensionFilter("JPG", "*.jpg"),
					new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
					new FileChooser.ExtensionFilter("PNG", "*.png"),
					new FileChooser.ExtensionFilter("BMP", "*.bmp")
					);
			Window chooseStage = null;
			file = chooser.showOpenDialog(chooseStage);
			image = new Image(file.toURI().toString());
			int curentAlbumSize = AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(UserViewController.index).getPicsList().size();


			for (int i = 0; i < curentAlbumSize; i++){
				if (AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(UserViewController.index).getPicsList().get(i).equals(removeFile(file.toURI().toString()))){//check for duplicate images in a given album
					//make a popup to display THIS PICTURE IS ALREADY IN THIS ALBUM
					System.out.println("THIS PICTURE IS ALREADY IN THIS ALBUM");
					return;
				}
			}

			for (int i = 0; i < AdminViewController.users.get(LogInViewController.currUser).getImageList().size(); i++) {
				if (AdminViewController.users.get(LogInViewController.currUser).getImageList().get(i).getPicturePath().equals(removeFile(file.toURI().toString()))) {
					// duplicate photo within same user's image list, but not necessarily same album
					System.out.println("duplicate photo in imageList");
					imageAdderNoImageList();
					return;
				}
			}

			if (curentAlbumSize == 0){
				imageAdder();
				return;
			}
			// not a duplicate at this point
			imageAdder();
		} catch (Exception e) {
			return;
		}
	}

	/**
	 * add image into database
	 */
	public void imageAdder(){
		AdminViewController.users.get(LogInViewController.currUser).getImageList().add(new MyImage());    // create new image in array list (add 1 in case list is empty)
		AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(UserViewController.index).getPicsList().add(removeFile(file.toURI().toString()));
		AdminViewController.users.get(LogInViewController.currUser).getImageList().get(AdminViewController.users.get(LogInViewController.currUser).getImageList().size() - 1).setPicturePath(removeFile(file.toURI().toString()));     // set picture field in array list
		Utility.setDateTaken(AdminViewController.users.get(LogInViewController.currUser).getImageList().get(AdminViewController.users.get(LogInViewController.currUser).getImageList().size() - 1));
		setDisplay();
	}

	public void imageAdderNoImageList() {
		AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(UserViewController.index).getPicsList().add(removeFile(file.toURI().toString()));
		AdminViewController.users.get(LogInViewController.currUser).getImageList().get(AdminViewController.users.get(LogInViewController.currUser).getImageList().size() - 1).setPicturePath(removeFile(file.toURI().toString()));     // set picture field in array list
		Utility.setDateTaken(AdminViewController.users.get(LogInViewController.currUser).getImageList().get(AdminViewController.users.get(LogInViewController.currUser).getImageList().size() - 1));
		setDisplay();
	}

	/**
	 * display image on big screen and all fields/tags associated with that image
	 */
	public void setDisplay() {
		bigScreen.setImage(image);    // display image on big screen
		System.out.println("addPhoto file: " + file);
		imageView = createImageView(file);
		//miniImages.getChildren().addAll(imageView);
		column++;
		if (column % 2 == 0 && column != 0) {
			column = 0;
			row++;
		}
		System.out.println("adding at " + column + "," + row);
		GridPane.setRowIndex(imageView, row);
		GridPane.setColumnIndex(imageView, column);
		miniImages.getChildren().add(imageView);
		//        miniImages.add(imageView, column, row);        // add smaller version of image in grid pane

		clearTexts();    // clear text fields and caption

		currSelect = AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(UserViewController.index).getPicsList().size() - 1;    // set currSelect to the index of the current (last) image
		System.out.println("in addPhoto currSelect "+currSelect);
		setTexts(currSelect);    // display image's tags in each text field
		setCaption(currSelect);
	}

	/**
	 * create an image view, which can be added into miniImages (gridPane)
	 * @param imageFile file path passed in from add photo
	 * @return image view created
	 */
	private ImageView createImageView(final File imageFile) {
		System.out.println("in createImageView, imageFile: " + imageFile);
		ImageView imageView = null;
		try {

			final Image image = new Image(new FileInputStream(imageFile),  130, 130, true, true);

			imageView = new ImageView(image);

			//            if (imageView == null) {
			//                System.out.println("image view is null");
			//            }

			if (image.getHeight() > image.getWidth()){
				imageView.setFitHeight(130);
			}
			else {
				imageView.setFitWidth(130);
			}
		} catch (Exception e) {
			return null;
		}
		System.out.println("successufully created image view");
		return imageView;
	}

	/**
	 * remove photo button on album view
	 * @param event
	 */
	@FXML
	private void removePhoto(ActionEvent event){
		if (UserViewController.index == -1) {
			// don't do anything
			try {
				ScreensController.showOpenAlbumWaningScene();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		if (AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(UserViewController.index).getPicsList().size() == 0) {
			System.out.println("No images to remove");
			return;
		}


		System.out.println("currSelect: " + currSelect + ", going to delete");
		ObservableList<Node> childrens = miniImages.getChildren();
		miniImages.getChildren().remove(childrens.get(currSelect));

		System.out.println("pics list size " + AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(UserViewController.index).getPicsList().size());

		AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(UserViewController.index).getPicsList().remove(currSelect);   
		reloadAlbum();

		System.out.println("have deleted at " + currSelect);

		if (AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(UserViewController.index).getPicsList().size() > 0) {
			String path = AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(UserViewController.index).getPicsList().get(currSelect);
			file = new File(path);
			image = new Image(file.toURI().toString());
			bigScreen.setImage(image);    // display image on big screen

			setTexts(currSelect);
			setCaption(currSelect);
		}

		System.out.println("number of remaining albums " + AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(UserViewController.index).getPicsList().size());

		if (AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(UserViewController.index).getPicsList().size() == 0) {
			// deleted the last image, set big screen to blank
			System.out.println("just deleted last image in album");
			bigScreen.setImage(null);

			column = -1;
			row = 0;
		}
	}

	/**
	 * transfer photo button on album view
	 * @param event
	 */
	@FXML
	private void transferPhoto(ActionEvent event){
		if (UserViewController.index == -1) {
			// don't do anything
			try {
				ScreensController.showOpenAlbumWaningScene();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		if (AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(UserViewController.index).getPicsList().size() == 0) {//no pictures in current album can't transfer
			return;
		}
		if (AdminViewController.users.get(LogInViewController.currUser).getAlbumList().size() < 1) {//only one album can't transfer
			return;
		}

		// make listView of albums appear
		for (int j = 0; j < AdminViewController.users.get(LogInViewController.currUser).getAlbumList().size(); j++) {//set album ObservableList

			TransferPhotoController.albums.add(AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(j).getAlbumName());

		}

		try {
			ScreensController.showTransferPhotoScene();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String path = AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(UserViewController.index).getPicsList().get(currSelect);
		file = new File(path);
		image = new Image(file.toURI().toString());
		bigScreen.setImage(image);    // display image on big screen

		// add currently selected photo to other album
		AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(TransferPhotoController.destIndex).getPicsList().add(removeFile(file.toURI().toString()));


		// delete from old album
		ObservableList<Node> childrens = miniImages.getChildren();
		miniImages.getChildren().remove(childrens.get(currSelect));

		AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(UserViewController.index).getPicsList().remove(currSelect);   

		// reset appearance of current/old album
		reloadAlbum();


	}

	/**
	 * reload album button on album view, resets album after doing a search
	 */
	@FXML
	public void reloadAlbum(){       
		if (UserViewController.index == -1) {
			// don't do anything
			try {
				ScreensController.showOpenAlbumWaningScene();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		if (miniImages == null) {
			System.out.println("miniImages == null");
			miniImages.getChildren();
			System.out.println("children");
		}

		// take all images out of miniImages so start fresh
		miniImages.getChildren().clear();

		// clear caption and texts
		clearTexts();

		if (AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(UserViewController.index).getPicsList().size() == 0) {
			// no pictures in this album
			return;
		}

		// add all images into miniImages
		column = -1;
		row = 0;
		for (int i = 0; i < AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(UserViewController.index).getPicsList().size(); i++){
			String path = AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(UserViewController.index).getPicsList().get(i);
			file = new File(path);
			imageView = createImageView(file);
			column++;
			if (column % 2 == 0 && column != 0) {
				column = 0;
				row++;
			}
			System.out.println("adding at " + column + "," + row);
			GridPane.setRowIndex(imageView, row);
			GridPane.setColumnIndex(imageView, column);
			miniImages.getChildren().add(imageView);
		}

		// select 0, display on big screen
		currSelect = 0;

		String path = AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(UserViewController.index).getPicsList().get(currSelect);
		file = new File(path);
		image = new Image(file.toURI().toString());
		bigScreen.setImage(image);    // display image on big screen
		System.out.println("path file image \n" + path+ " \n" + file + " \n"+image );
		setTexts(currSelect);    // display image's tags in each text field
		setCaption(currSelect);
	}

	@FXML
	private void openAlbum(ActionEvent event){
		LoadAlbumController.albums.clear();
		for (int j = 0; j < AdminViewController.users.get(LogInViewController.currUser).getAlbumList().size(); j++) {//set album ObservableList
			LoadAlbumController.albums.add(AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(j).getAlbumName());
		}

		try {
			ScreensController.showLoadAlbumScene();

		} catch (IOException e) {
			e.printStackTrace();
		}
		if(UserViewController.index == -1){
			return;
		}
		reloadAlbum();
	}

	/**
	 * attach contents of caption text to image
	 * @param event
	 */
	@FXML
	private void updateCaption(ActionEvent event){
		if (UserViewController.index == -1) {
			// don't do anything
			try {
				ScreensController.showOpenAlbumWaningScene();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		if (AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(UserViewController.index).getPicsList().size() == 0) {
			return;
		}

		// convert currSelect into index in imageList
		int imageIndex = getImageIndex(currSelect);
		System.out.println("currSelect:" + currSelect + " imageIndex:" + imageIndex);
		System.out.println("getImageList imageIndex:" + AdminViewController.users.get(LogInViewController.currUser).getImageList().get(imageIndex).getPicturePath());
		AdminViewController.users.get(LogInViewController.currUser).getImageList().get(imageIndex).setCaption(captionText.getText());
	}

	/**
	 * attach fields in album view to image
	 * @param event
	 */
	@FXML
	private void updateFields(ActionEvent event){
		if (UserViewController.index == -1) {
			// don't do anything
			try {
				ScreensController.showOpenAlbumWaningScene();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		if (AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(UserViewController.index).getPicsList().size() == 0) {
			return;
		}

		int imageIndex = getImageIndex(currSelect);

		if (location.getText() == null) {
			AdminViewController.users.get(LogInViewController.currUser).getImageList().get(imageIndex).setLocation("");
		} else {
			AdminViewController.users.get(LogInViewController.currUser).getImageList().get(imageIndex).setLocation(location.getText());
		}

		System.out.println("people.getText() " + people.getText() + " " + PhotoSearch.namesNoDups(people.getText()));
		if (people.getText() == null) {
			AdminViewController.users.get(LogInViewController.currUser).getImageList().get(imageIndex).setPeople("");
		} else if (PhotoSearch.namesNoDups(people.getText())) {
			// add blank name because can't tag a photo with 2 of the same name
			System.out.println("adding duplicate names");
			AdminViewController.users.get(LogInViewController.currUser).getImageList().get(imageIndex).setPeople("");
		} else {
			AdminViewController.users.get(LogInViewController.currUser).getImageList().get(imageIndex).setPeople(people.getText());
		}

		if (!year.getText().equals("")) {
			AdminViewController.users.get(LogInViewController.currUser).getImageList().get(imageIndex).setYear(Integer.parseInt(year.getText()));
		}
		if (!month.getText().equals("")) {
			AdminViewController.users.get(LogInViewController.currUser).getImageList().get(imageIndex).setMonth(Integer.parseInt(month.getText()));
		}
		if (!day.getText().equals("")) {
			AdminViewController.users.get(LogInViewController.currUser).getImageList().get(imageIndex).setDay(Integer.parseInt(day.getText()));
		}
	}

	/**
	 * set all fields in album view to blank
	 * @param event
	 */
	@FXML
	private void clearFields(ActionEvent event) {

		clearTexts();
	}

	/**
	 * grab all images with dates within 2 dates provided
	 * @param event
	 */
	@FXML
	private void searchDateRangeButton(ActionEvent event) {
		if (UserViewController.index == -1) {
			// don't do anything
			try {
				ScreensController.showOpenAlbumWaningScene();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		// save images in savedImages so can revert back after search
		saveCopy(AdminViewController.users.get(LogInViewController.currUser).getImageList(), savedImages);

		// grab 2 dates
		int year1 = Integer.parseInt(year.getText());
		int month1 = Integer.parseInt(month.getText());
		int day1 = Integer.parseInt(day.getText());
		int year = Integer.parseInt(year2.getText());
		int month = Integer.parseInt(month2.getText());
		int day = Integer.parseInt(day2.getText());

		System.out.printf("%d %d %d - %d %d %d\n", year1, month1, day1, year, month, day);

		// clear searchResults
		searchResults.clear();

		// store photos that are within the date range in array list called dest
		ArrayList<MyImage> dest = new ArrayList<MyImage>();

		PhotoSearch.searchDateRange(dest, year1, month1, day1, year, month, day);
		System.out.println(dest.size() + " images within date range");

		// clear miniImages
		miniImages.getChildren().clear();

		// add dest into miniImages
		column = -1;
		row = 0;
		for (int i = 0; i < dest.size(); i++){
			String path = dest.get(i).getPicturePath();
			file = new File(path);
			imageView = createImageView(file);

			column++;
			if (column % 2 == 0 && column != 0) {
				column = 0;
				row++;
			}

			GridPane.setRowIndex(imageView, row);
			GridPane.setColumnIndex(imageView, column);
			miniImages.getChildren().add(imageView);
		}

		saveCopy(dest, searchResults);
	}

	/**
	 * grab all images that match at least one field provided by user in album view
	 * @param event
	 */
	@FXML
	private void searchFields(ActionEvent event){
		if (AdminViewController.users.get(LogInViewController.currUser).getImageList().size() == 0) {
			// this user has no pictures
			return;
		}

		// save images in savedImages so can revert back after search
		saveCopy(AdminViewController.users.get(LogInViewController.currUser).getImageList(), savedImages);

		// clear searchResults
		searchResults.clear();

		// store photos that match search fields called dest
		ArrayList<MyImage> dest = new ArrayList<MyImage>();

		System.out.println("before search all fields");

		PhotoSearch.searchAllFields(dest, location.getText(), year.getText(), month.getText(), day.getText(), people.getText());

		System.out.println(dest.size() + " images that match");

		// clear miniImages
		miniImages.getChildren().clear();

		// add dest into miniImages
		column = -1;
		row = 0;
		for (int i = 0; i < dest.size(); i++){
			String path = dest.get(i).getPicturePath();
			file = new File(path);
			imageView = createImageView(file);

			column++;
			if (column % 2 == 0 && column != 0) {
				column = 0;
				row++;
			}

			GridPane.setRowIndex(imageView, row);
			GridPane.setColumnIndex(imageView, column);
			miniImages.getChildren().add(imageView);
		}

		saveCopy(dest, searchResults);
	}

	/**
	 * cycle > button on album view, goes through album one picture to the right
	 * @param event
	 */
	@FXML
	private void cycleForward(ActionEvent event) {
		if (UserViewController.index == -1) {
			// don't do anything
			try {
				ScreensController.showOpenAlbumWaningScene();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		if (AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(UserViewController.index).getPicsList().isEmpty()) {
			return;
		}

		if (currSelect + 1 < AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(UserViewController.index).getPicsList().size()) {
			currSelect++;
		}

		String path = AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(UserViewController.index).getPicsList().get(currSelect);
		file = new File(path);
		image = new Image(file.toURI().toString());
		bigScreen.setImage(image);    // display image on big screen
		System.out.println("path file image \n" + path+ " \n" + file + " \n"+image );
		setTexts(currSelect);    // display image's tags in each text field
		setCaption(currSelect);
		System.out.println("currSelect: " + currSelect + " imageIndex: " + getImageIndex(currSelect));
	}

	/**
	 * cycle < button on album view, goes through album one picture to the left
	 * @param event
	 */
	@FXML
	private void cycleBack(ActionEvent event) {
		if (UserViewController.index == -1) {
			// don't do anything
			try {
				ScreensController.showOpenAlbumWaningScene();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		if (AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(UserViewController.index).getPicsList().isEmpty()) {
			return;
		}

		if (currSelect > 0) {
			currSelect--;
		}
		String path = AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(UserViewController.index).getPicsList().get(currSelect);
		file = new File(path);
		image = new Image(file.toURI().toString());
		bigScreen.setImage(image);    // display image on big screen
		System.out.println("path file image \n" + path+ " \n" + file + " \n"+image );

		setTexts(currSelect);    // display image's tags in each text field
		setCaption(currSelect);
		System.out.println("currSelect: " + currSelect + " imageIndex: " + getImageIndex(currSelect));
	}

	/**
	 * make an album with search results
	 * @param event
	 */
	@FXML
	private void createAlbum(ActionEvent event) {
		if (searchResults.isEmpty()) {
			// cannot create album out of these
			return;
		}
		String albumName = createAlbumName.getText();

		if (albumName.equals("")) {
			// can't add a blank album
			return;
		} else {
			if (miniImages.getChildren().isEmpty()) {
				// no images, cannot create a new album
				return;
			}

			for (int i = 0; i < UserViewController.albums.size(); i++) {
				if (UserViewController.albums.get(i).equalsIgnoreCase(albumName)) {
					createAlbumName.clear();
					// cannot create album that is a duplicate of another
					return;
				}
			}

			// after this point, albumName is good

			AdminViewController.users.get(LogInViewController.currUser).getAlbumList().add(new Album(albumName));    // create new album
			for (int i = 0; i < searchResults.size(); i++) {
				// add everything from searchResults to new album
				AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(AdminViewController.users.get(LogInViewController.currUser).getAlbumList().size() - 1).getPicsList().add(searchResults.get(i).getPicturePath());
			}

		}
		createAlbumName.clear();
	}

	/**
	 * clear all fields in album view
	 */
	private void clearTexts() {
		location.clear();
		people.clear();
		captionText.clear();
		year.clear();
		month.clear();
		day.clear();
		year2.clear();
		month2.clear();
		day2.clear();
		createAlbumName.clear();
	}

	/**
	 * set all 5 text fields in album view
	 * @param currSelect currently selected image
	 */
	private void setTexts(int currSelect) {
		int imageIndex = getImageIndex(currSelect);

		if (location.getText() == null) {
			location.setText("");
		} else {
			location.setText(AdminViewController.users.get(LogInViewController.currUser).getImageList().get(imageIndex).getLocation());
		}

		if (people.getText() == null) {
			people.setText("");
		} else {
			people.setText(AdminViewController.users.get(LogInViewController.currUser).getImageList().get(imageIndex).getPeople());
		}

		year.setText(AdminViewController.users.get(LogInViewController.currUser).getImageList().get(imageIndex).getYear() + "");
		month.setText(AdminViewController.users.get(LogInViewController.currUser).getImageList().get(imageIndex).getMonth() + "");
		day.setText(AdminViewController.users.get(LogInViewController.currUser).getImageList().get(imageIndex).getDay() + "");
	}

	/**
	 * set image's caption
	 * @param currSelect currently selected image
	 */
	private void setCaption(int currSelect) {
		int imageIndex = getImageIndex(currSelect);
		captionText.setText(AdminViewController.users.get(LogInViewController.currUser).getImageList().get(imageIndex).getCaption());
	}

	/**
	 * make a copy of array list a1, store in a2
	 * @param a1 source array list
	 * @param a2 destination array list
	 */
	private void saveCopy(ArrayList<MyImage> a1, ArrayList<MyImage> a2) {
		for (int i = 0; i < a1.size(); i++) {
			a2.add(a1.get(i));
		}
	}

	/**
	 * take "file:" off of beginning of f and replace %20 with space
	 * @param f file
	 * @return file without "file:" and "%20"
	 */
	private String removeFile(String f) {
		String f2 = f.replaceAll("%20", " ");
		if (f2.startsWith("file:")) {
			return f2.substring(5);
		}
		return f2;
	}

	/**
	 * convert currSelect to imageIndex to be used in getImageList()
	 * @param currSelect currently selected image
	 * @return imageIndex
	 */
	private int getImageIndex(int currSelect) {
		String targetPath = AdminViewController.users.get(LogInViewController.currUser).getAlbumList().get(UserViewController.index).getPicsList().get(currSelect);

		for (int i = 0; i < AdminViewController.users.get(LogInViewController.currUser).getImageList().size(); i++) {
			if (AdminViewController.users.get(LogInViewController.currUser).getImageList().get(i).getPicturePath().equals(targetPath)) {
				return i;
			}
		}

		// shouldn't happen
		return -1;
	}
}
