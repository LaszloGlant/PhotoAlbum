package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import view.AdminViewController;

/**
 * 
 * @author Brian Wong, Laszlo Glant
 * Contains the IO and other helper methods
 *
 */
public class Utility {

    /**
     * Outputs the database to the user.ser file
     * @param users all the users in the database
     */
	public static void output(ArrayList<User> users) {
        try
        {
            FileOutputStream fileOut = new FileOutputStream("users.ser");

            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(users);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in users.ser");
        }catch(IOException i)
        {
            i.printStackTrace();
        }
    }
	/**
	 * Loads the database from the user.ser file
	 * @return The database of users
	 */
    public static ArrayList<User> input() {

        ArrayList<User> newUsers= null;
        try
        {

            FileInputStream fileIn = new FileInputStream("users.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            newUsers = (ArrayList<User>) in.readObject();
            in.close();
            fileIn.close();
        }catch(IOException i)
        {
            i.printStackTrace();
            return newUsers;
        }catch(ClassNotFoundException c)
        {
            System.out.println("Employee class not found");
            c.printStackTrace();
            return newUsers;
        }
        return newUsers;
    }


    /**
     * return the newest image in the album
     * @param i index of user
     * @param j index of album
     * @return newest image
     */
    public static String newestPhoto(int i, int j) {
        if (AdminViewController.users.get(i).getAlbumList().get(j).getPicsList().size() == 0) {
            // no images, return "N/A"
            return "N/A";
        }

        String currImage = AdminViewController.users.get(i).getAlbumList().get(j).getPicsList().get(0);    // set current newest to first item

        String latestImage = currImage;


        int latestMonth = Integer.parseInt(extractDate(new File(latestImage))[0]);
        int latestDay = Integer.parseInt(extractDate(new File(latestImage))[1]);
        int latestYear = Integer.parseInt(extractDate(new File(latestImage))[2]);

        for (int k = 1; k < AdminViewController.users.get(i).getAlbumList().get(j).getPicsList().size(); k++) {
            currImage = AdminViewController.users.get(i).getAlbumList().get(j).getPicsList().get(k);

            int currMonth = Integer.parseInt(extractDate(new File(currImage))[0]);
            int currDay = Integer.parseInt(extractDate(new File(currImage))[1]);
            int currYear = Integer.parseInt(extractDate(new File(currImage))[2]);

            if (PhotoSearch.compareDates(currYear, currMonth, currDay, latestYear, latestMonth, latestDay) < 0) {
                // images are in correct order, update
                latestMonth = currMonth;
                latestDay = currDay;
                latestYear = currYear;
            }
        }
        return latestMonth + "/" + latestDay + "/" + latestYear;
    }

    /**
     * return the oldest image in the album
     * @param i user index
     * @param j album index
     * @return oldest image in the album
     */
    public static String oldestPhoto(int i, int j) {
        if (AdminViewController.users.get(i).getAlbumList().get(j).getPicsList().size() == 0) {
            // no images, return "N/A"
            return "N/A";
        }

        String currImage = AdminViewController.users.get(i).getAlbumList().get(j).getPicsList().get(0);    // set current newest to first item
        String latestImage = currImage;


        int latestMonth = Integer.parseInt(extractDate(new File(latestImage))[0]);
        int latestDay = Integer.parseInt(extractDate(new File(latestImage))[1]);
        int latestYear = Integer.parseInt(extractDate(new File(latestImage))[2]);

        for (int k = 1; k < AdminViewController.users.get(i).getAlbumList().get(j).getPicsList().size(); k++) {
            currImage = AdminViewController.users.get(i).getAlbumList().get(j).getPicsList().get(k);

            int currMonth = Integer.parseInt(extractDate(new File(currImage))[0]);
            int currDay = Integer.parseInt(extractDate(new File(currImage))[1]);
            int currYear = Integer.parseInt(extractDate(new File(currImage))[2]);

            if (PhotoSearch.compareDates(currYear, currMonth, currDay, latestYear, latestMonth, latestDay) > 0) {
                // images are in correct order, update
                latestMonth = currMonth;
                latestDay = currDay;
                latestYear = currYear;
            }
        }
        return latestMonth + "/" + latestDay + "/" + latestYear;
    }

    /**
     * set mi's year, month, and day fields
     * @param mi instance of MyImage, one image in the album
     */
    public static void setDateTaken(MyImage mi) {
        File f = new File(mi.getPicturePath());       
        //        System.out.println(f);                       
        //        System.out.println("incoming path"+mi.getPicturePath());
        //        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        //        String date = sdf.format(f.lastModified());
        //        System.out.println("After Format : " + date);

        String[] arr = extractDate(f);

        mi.setMonth(Integer.parseInt(arr[0]));
        mi.setDay(Integer.parseInt(arr[1]));
        mi.setYear(Integer.parseInt(arr[2]));
    }
    /**
     * Extracts the last modified date of the given file
     * @param f The file to get the date from
     * @return The date in a string format
     */
    public static String[] extractDate(File f) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String date = sdf.format(f.lastModified());
        String[] arr = date.split("/");
        return arr;
    }

    /**
     * return range of dates in album as String format
     * @param userNum integer index for user
     * @param albumNum integer index for album
     * @return String for range of dates, used to be displayed in GUI
     */
    public static String dateRange(int userNum, int albumNum) {
        if (AdminViewController.users.get(userNum).getImageList().size() == 0) {
            return "N/A";
        }

        return oldestPhoto(userNum, albumNum) + " - " + newestPhoto(userNum, albumNum);
    }
    /**
     * Prints the user database to the console
     * For testing purposes only
     */
    public static void printUserList(){
        int i, j, k;
        for (i = 0; i < AdminViewController.users.size(); i++){//user selector

            System.out.println("\nuser name "+AdminViewController.users.get(i).getUserName()+ " number of albums for the user "
                    +AdminViewController.users.get(i).getAlbumList().size());

            for (j = 0; j < AdminViewController.users.get(i).getAlbumList().size(); j++){//album selector

                System.out.println(" album name is " +AdminViewController.users.get(i).getAlbumList().get(j).getAlbumName()
                        +" Number of images in this album: "+AdminViewController.users.get(i).getAlbumList().get(j).getPicsList().size());

                for (k = 0; k < AdminViewController.users.get(i).getAlbumList().get(j).getPicsList().size(); k++){//images in albums

                    System.out.println(" picture  name: "+AdminViewController.users.get(i).getAlbumList().get(j).getPicsList().get(k)

                            //                                +";     new location: "+AdminViewController.users.get(i).getAlbumList().get(j).getImageList().get(k).getLocation()
                            //                                +";     new people: "+AdminViewController.users.get(i).getAlbumList().get(j).getImageList().get(k).getPeople()
                            );
                }
            }
        }
    }
}