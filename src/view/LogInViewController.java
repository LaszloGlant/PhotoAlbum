package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import application.PhotoAlbum;
import application.User;
import application.Utility;
import control.ControlledScreen;
import control.ScreensController;
import view.UserViewController;

/**
 * Openning screen that pops up when program starts up
 * @author Brian Wong, Laszlo Glant
 *
 */
public class LogInViewController implements Initializable, ControlledScreen {

    ScreensController myController;
    public static ArrayList<User> users = new ArrayList<User>();

    public static int currUser;
    @FXML
    private TextField userName;


    /**
     * program start up
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     * set screen parent
     */
    public void setScreenParent(ScreensController screenParent){
        myController = screenParent;
    }

    /**
     * Just for testing purposes, log in as admin
     * @param event
     */
    @FXML
    private void goToAdmin(ActionEvent event){
        myController.setScreen(PhotoAlbum.screen2ID);
    }

    /**
     * Just for testing purposes, log in as user
     * @param event
     */
    @FXML
    private void goToUser(ActionEvent event){
        myController.setScreen(PhotoAlbum.screen3ID);
    }

    /**
     * log in button, can be admin or any username
     * @param event
     */
    @FXML
    private void logIn(ActionEvent event){
        Utility.printUserList();
        if (userName.getText().equalsIgnoreCase("admin")){
            myController.setScreen(PhotoAlbum.screen2ID);
        }
        else{
            //clears the 4 observableLists
            UserViewController.albums.clear();
            UserViewController.numberOfImages.clear();
            UserViewController.date_range.clear();
            UserViewController.oldest_photo.clear();
            for(int i = 0; i < AdminViewController.users.size(); i++){//search through the users

                if(userName.getText().equalsIgnoreCase(AdminViewController.users.get(i).getUserName())){
                	// if a user name found then will go to the UserView
                    currUser = i;//saves the index in a global variable to load the album list
                    System.out.println("LogInView currUser: "+currUser);
                    
                    for (int j = 0; j < AdminViewController.users.get(i).getAlbumList().size(); j++) {//set album ObservableList
                        // loop through all albums
                    	
                    	// update 4 obsLists
                    	
                    	// 1. update users
                        UserViewController.albums.add(AdminViewController.users.get(i).getAlbumList().get(j).getAlbumName());

                        // 2. update number of images
                        UserViewController.numberOfImages.add(AdminViewController.users.get(i).getAlbumList().get(j).getPicsList().size()+"");//images in albums

                        // 3. update date range
                        UserViewController.date_range.add(Utility.dateRange(i, j));

                        // 4. update oldest photo
                        UserViewController.oldest_photo.add(Utility.oldestPhoto(i, j));

                    }
                    myController.setScreen(PhotoAlbum.screen3ID);
                }
            }
        }
        userName.clear();
    }

    /**
     * quit button, saves all data and exits program
     * @param event
     */
    @FXML
    private void quit(ActionEvent event){
        Utility.output(AdminViewController.users);
        System.exit(0);
    }
}