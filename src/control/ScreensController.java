package control;

import java.io.IOException;
import java.util.HashMap;

import application.PhotoAlbum;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Controls the behavior of the different screens
 * @author Brian Wong, Laszlo Glant
 *
 */

public class ScreensController  extends StackPane {
    //Holds the screens to be displayed
	
    private HashMap<String, Node> screens = new HashMap<>();
    private static AnchorPane deleteUser, deleteAlbum, openAlbum, openAlbumWarning, sameUser, sameAlbum, transferPhoto;
    public ScreensController() {
        super();
    }

    /**
     * Add the screen to the collection
     * @param name name of the screen
     * @param screen screen node
     */
    public void addScreen(String name, Node screen) {    	
        screens.put(name, screen);
    }

    /**
     * Returns the Node with the appropriate name
     * @param name screen name
     * @return
     */
    public Node getScreen(String name) {
        return screens.get(name);
    }   
    
    /**
     * Loads the fxml file, add the screen to the screens collection and
     * finally injects the screenPane to the controller.
     * @param name screen name
     * @param resource fxml to be loaded
     * @return
     */
    public boolean loadScreen(String name, String resource) {
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            Parent loadScreen = (Parent) myLoader.load();
            ControlledScreen myScreenControler = ((ControlledScreen) myLoader.getController());
            myScreenControler.setScreenParent(this);            
            addScreen(name, loadScreen);
            return true;
        } catch (Exception e) {
        	System.out.println("Load screen fail: "+ name);
        	System.out.println(e.getStackTrace());            
            return false;
        }
    }
    
    /**
     * This method tries to display the screen with a predefined name.
     * First it makes sure the screen has been already loaded.  Then if there is more than
     * one screen the new screen is been added second, and then the current screen is removed.
     * If there isn't any screen being displayed, the new screen is just added to the root.
     * @param name screen name
     * @return true if screen is set
     */
    public boolean setScreen(final String name) {  
    	
        if (screens.get(name) != null) {   //screen loaded
            final DoubleProperty opacity = opacityProperty();

            if (!getChildren().isEmpty()) {    //if there is more than one screen
                Timeline fade = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                        new KeyFrame(new Duration(500), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        getChildren().remove(0);                    //remove the displayed screen
                        getChildren().add(0, screens.get(name));     //add the screen
                        Timeline fadeIn = new Timeline(
                                new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                new KeyFrame(new Duration(500), new KeyValue(opacity, 1.0)));
                        fadeIn.play();
                    }
                }, new KeyValue(opacity, 0.0)));
                fade.play();

            } else {
                setOpacity(0.0);
                getChildren().add(screens.get(name));       //no one else been displayed, then just show
                Timeline fadeIn = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(500), new KeyValue(opacity, 1.0)));
                fadeIn.play();
            }
            return true;
        } else {
            System.out.println("screen is null, screen hasn't been loaded!!! \n");
            return false;
        }
    }

    /**
     * This method will remove the screen with the given name from the collection of screens
     * @param name screen name
     * @return true if removed
     */
    public boolean unloadScreen(String name) {
        if (screens.remove(name) == null) {
            System.out.println("Screen didn't exist");
            return false;
        } else {
            return true;
        }
    }
    /**
     * Displays the delete user confirmation scene
     * @throws IOException
     */
    public static void showDeleteUserScene() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ScreensController.class.getResource("/view/DeleteUser.fxml"));
		deleteUser = loader.load();
		Stage addWindow = new Stage();
		addWindow.setTitle("Delete User");
		addWindow.initModality(Modality.WINDOW_MODAL);
		addWindow.initStyle(StageStyle.UNDECORATED);
		addWindow.initOwner(PhotoAlbum.primarySTAGE);
		Scene scene = new Scene (deleteUser);
		addWindow.setScene(scene);
		addWindow.showAndWait();
	}
    /**
     * Displays the delete album confirmation scene
     * @throws IOException
     */
    public static void showDeleteAlbumScene() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ScreensController.class.getResource("/view/DeleteAlbum.fxml"));
		deleteAlbum = loader.load();
		Stage addWindow = new Stage();
		addWindow.setTitle("Delete Album");
		addWindow.initModality(Modality.WINDOW_MODAL);
		addWindow.initStyle(StageStyle.UNDECORATED);
		addWindow.initOwner(PhotoAlbum.primarySTAGE);
		Scene scene = new Scene (deleteAlbum);
		addWindow.setScene(scene);
		addWindow.showAndWait();
	}
    /**
     * Displays the load album warning scene
     * @throws IOException
     */
    public static void showLoadAlbumScene() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ScreensController.class.getResource("/view/LoadAlbum.fxml"));
		openAlbum = loader.load();
		Stage addWindow = new Stage();
		addWindow.setTitle("Load Album");
		addWindow.initModality(Modality.WINDOW_MODAL);
		addWindow.initStyle(StageStyle.UNDECORATED);
		addWindow.initOwner(PhotoAlbum.primarySTAGE);
		Scene scene = new Scene (openAlbum);
		addWindow.setScene(scene);
		addWindow.showAndWait();
	}
    /**
     * Displays the same album warning scene
     * @throws IOException
     */
    public static void showSameAlbumScene() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ScreensController.class.getResource("/view/SameAlbumName.fxml"));
		sameAlbum = loader.load();
		Stage addWindow = new Stage();
		//addWindow.setTitle("Same Album");
		addWindow.initModality(Modality.WINDOW_MODAL);
		addWindow.initStyle(StageStyle.UNDECORATED);
		addWindow.initOwner(PhotoAlbum.primarySTAGE);
		Scene scene = new Scene (sameAlbum);
		addWindow.setScene(scene);
		addWindow.showAndWait();
	}
    /**
     * Displays the same user warning scene
     * @throws IOException
     */
    public static void showSameUserScene() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ScreensController.class.getResource("/view/SameUserName.fxml"));
		sameUser = loader.load();
		Stage addWindow = new Stage();
		//addWindow.setTitle("Load Album");
		addWindow.initModality(Modality.WINDOW_MODAL);
		addWindow.initStyle(StageStyle.UNDECORATED);
		addWindow.initOwner(PhotoAlbum.primarySTAGE);
		Scene scene = new Scene (sameUser);
		addWindow.setScene(scene);
		addWindow.showAndWait();
	}
    /**
     * Displays the transfer photo scene
     * @throws IOException
     */
    public static void showTransferPhotoScene() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ScreensController.class.getResource("/view/TransferPhoto.fxml"));
		transferPhoto = loader.load();
		Stage addWindow = new Stage();
		//addWindow.setTitle("Load Album");
		addWindow.initModality(Modality.WINDOW_MODAL);
		addWindow.initStyle(StageStyle.UNDECORATED);
		addWindow.initOwner(PhotoAlbum.primarySTAGE);
		Scene scene = new Scene (transferPhoto);
		addWindow.setScene(scene);
		addWindow.showAndWait();
	}
    /**
     * Displays the no album open warning scene
     * @throws IOException
     */
    public static void showOpenAlbumWaningScene() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ScreensController.class.getResource("/view/OpenAlbumWarning.fxml"));
		openAlbumWarning = loader.load();
		Stage addWindow = new Stage();
		//addWindow.setTitle("Load Album");
		addWindow.initModality(Modality.WINDOW_MODAL);
		addWindow.initStyle(StageStyle.UNDECORATED);
		addWindow.initOwner(PhotoAlbum.primarySTAGE);
		Scene scene = new Scene (openAlbumWarning);
		addWindow.setScene(scene);
		addWindow.showAndWait();
	}
}

