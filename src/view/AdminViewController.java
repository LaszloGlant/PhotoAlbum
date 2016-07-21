package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import application.Utility;
import application.PhotoAlbum;
import application.User;
import control.ControlledScreen;
import control.ScreensController;

/**
 * Screen when logged in as admin. Can create users, delete users, and log out
 * @author Brian Wong, Laszlo Glant
 *
 */
public class AdminViewController implements Initializable, ControlledScreen {

	ScreensController myController;
	public static ObservableList<String> userObs = FXCollections.observableArrayList();
	@FXML 
	public ListView<String> usersList;
	@FXML
	private TextField newUser;

	User user;

	static int deleteIndex = -1;
	public static ArrayList<User> users = new ArrayList<User>();
	
	/**
	 * when called, program start up
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		//load data into userList(ArrayList<User>) and also into users(ObservableList<String>)
		File f = new File("users.ser");
		if (f.exists()) {
			users = Utility.input();
			for (int i = 0; i < users.size(); i++) {
				userObs.add(users.get(i).getUserName());
			}
			usersList.setItems(userObs);
		}
	}

	/**
	 * set screen parent
	 */
	public void setScreenParent(ScreensController screenParent){
		myController = screenParent;
	}

	/**
	 * log out button on admin view
	 * @param event
	 */
	@FXML
	private void goToLogout(ActionEvent event){   
		myController.setScreen(PhotoAlbum.screen1ID);
	}
	
	/**
	 * create user button, uses adjacent text field
	 * @param event
	 * @throws IOException
	 */
	@FXML
	private void createUser(ActionEvent event) throws IOException{

		String username = newUser.getText();

		if (username.equals("")){//will not allow to insert blank string
			return;
		}
		else{
			for (int i = 0; i < userObs.size(); i++){
				if (userObs.get(i).equalsIgnoreCase(username)){
					newUser.clear();
					ScreensController.showSameUserScene();
					return;
				}
			}
		}
		newUser.clear();
		userObs.add(username);
		usersList.setItems(userObs);
		usersList.getSelectionModel().select(0);
		// add to array list
		users.add(user = new User(username));
		
		
		File dirName = new File(username);
		
		if (!dirName.exists()){
			
			try{
				dirName.mkdirs();
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * delete user on admin view
	 * @param event
	 * @throws IOException
	 */
	@FXML
	private void deleteUser(ActionEvent event) throws IOException{
		
		deleteIndex = usersList.getSelectionModel().getSelectedIndex();
		 
		if(userObs.size() == 0 || deleteIndex < 0){
			return;
		}
		ScreensController.showDeleteUserScene();
		usersList.requestFocus();
		usersList.getSelectionModel().select(deleteIndex);
	}
	
	/**
	 * quit button, saves all data and then exits application
	 * @param event
	 */
	@FXML
	private void quit(ActionEvent event){
		Utility.output(users);
		System.exit(0);
	}
}
