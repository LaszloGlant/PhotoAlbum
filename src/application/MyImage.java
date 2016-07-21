package application;

/**
 * 
 * @author Brian Wong, Laszlo Glant
 * Sets up an individual image object
 * location The location tag (searchable)
 * people The people tag (searchable)
 * month, day, year The date the image was last modified (searchable)
 * caption The caption associated with the image
 * picturePath The access path to the image stored as a string
 */
public class MyImage implements java.io.Serializable{
	private String location;
	private String people;
	private int month; //month day year
	private int day;
	private int year;
	private String caption;
	private String picturePath; //name of picture in users pics directory
	
	public MyImage(){
		this.location = null;
		this.people = null;
		this.month = 0;
		this.day = 0;
		this.year = 0;
		this.caption = null;
		this.picturePath = null;
	}
	public MyImage(String location, String people){
		this.location = location;
		this.people = people;
		this.month = 0;
		this.day = 0;
		this.year = 0;
		this.caption = null;
		this.picturePath = null;
	}
	public MyImage(String picturePath){
		this.location = null;
		this.people = null;
		this.month = 0;
		this.day = 0;
		this.year = 0;
		this.caption = null;
		this.picturePath = picturePath;
	}
	public MyImage(String tagLocation, String tagPeople, int [] tagTime, String caption, String picturePath){//add picture when datatype is determined
		this.location = tagLocation;
		this.people = tagPeople;
		this.month = tagTime[0];
		this.day = tagTime[1];
		this.year = tagTime[2];
		this.caption = caption;
		this.picturePath = picturePath;
	}
	
	// getters
	public int getDay() {
		return day;
	}
	
	public int getMonth() {
		return month;
	}
	
	public int getYear() {
		return year;
	}
	
	public String getLocation(){
		return location;
	}
	
	public String getPeople(){
		return people;
	}
	
	public String getCaption(){
		return caption;
	}
	
	public String getPicturePath(){
		return picturePath;
	}
	
	// setters
	public void setDay(int i) {
		day = i;
	}
	
	public void setMonth(int i) {
		month = i;
	}
	
	public void setYear(int i) {
		year = i;
	}
	
	public void setLocation(String location){
		this.location = location;
	}
	
	public void setPeople(String people){
		this.people = people;
	}
	public void setCaption(String caption){
		this.caption = caption;
	}
	public void setPicturePath(String picturePath){
		this.picturePath = picturePath;
	}
	
}
