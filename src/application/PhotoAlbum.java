package application;

import control.ScreensController;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 
 * @author Brian Wong, Laszlo Glant
 * Contains the main method of the application
 * 
 *
 */
public class PhotoAlbum extends Application{
	
	public static String screen1ID = "login";
    public static String screen1File = "/view/LogInView.fxml";
    public static String screen2ID = "adminView";
    public static String screen2File = "/view/AdminView.fxml";
    public static String screen3ID = "userView";
    public static String screen3File = "/view/UserView.fxml";
    public static String screen4ID = "albumView";
    public static String screen4File = "/view/AlbumView.fxml";
    public static Stage primarySTAGE;
    
    
    /**
     * Controls the screens to be loaded
     */
    @Override
    public void start(Stage primaryStage) {
    	//primaryStage.initStyle(StageStyle.UNDECORATED);
        ScreensController mainContainer = new ScreensController();
        mainContainer.loadScreen(PhotoAlbum.screen1ID, PhotoAlbum.screen1File);
        mainContainer.loadScreen(PhotoAlbum.screen2ID, PhotoAlbum.screen2File);
        mainContainer.loadScreen(PhotoAlbum.screen3ID, PhotoAlbum.screen3File);      
        mainContainer.loadScreen(PhotoAlbum.screen4ID, PhotoAlbum.screen4File);       
        
        mainContainer.setScreen(PhotoAlbum.screen1ID);
        
        Group root = new Group();
        root.getChildren().addAll(mainContainer);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primarySTAGE = primaryStage;
        primaryStage.show();
    }
	/**
	 * Starts application
	 * @param args arguments to be passed in
	 */
	public static void main(String[] args) {
		
		launch(args);

	}
}
