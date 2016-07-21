package application;

import java.util.ArrayList;

/**
 * 
 * @author Brian Wong, Laszlo Glant
 * Sets up user object
 * userName The user name
 * albumList The list of albums the user have
 * imageList The master list of images the user have in any of the albums
 *
 */
public class User implements java.io.Serializable{
	private String userName;
	private ArrayList<Album> albumList;
	private ArrayList<MyImage> imageList;
	public User(){
		this.userName = null;
		albumList = new ArrayList<Album>();
		imageList = new ArrayList<MyImage>();
	}
	public User(String userName){
		this.userName = userName;
		albumList = new ArrayList<Album>();
		imageList = new ArrayList<MyImage>();
	}
	public String getUserName(){
		return userName;
	}
	public void setUserName(String userName){
		this.userName = userName;
	}
	public ArrayList<Album> getAlbumList(){
		return albumList;
	}
	public ArrayList<MyImage> getImageList(){
		return imageList;
	}
}