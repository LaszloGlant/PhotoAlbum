package application;

import java.util.ArrayList;


/**
 * 
 * @author Brian Wong, Laszlo Glant
 * Sets up the Album
 * albumName Name of the album instance
 * picsName the access path to the image in the given album stored as a string
 *
 */
public class Album implements java.io.Serializable{
	private String albumName;
	ArrayList<String> picsName;
	public Album(){
		albumName = null;
		picsName = new ArrayList<String>();
	}
	public Album(String albumName){
		this.albumName = albumName;
		picsName = new ArrayList<String>();
	}
	public String getAlbumName(){
		return albumName;
	}
	public void setAlbumName(String albumName){
		this.albumName = albumName;
	}
	public ArrayList<String> getPicsList(){
		return picsName;
	}
}
