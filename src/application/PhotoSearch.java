package application;

import java.util.ArrayList;
import java.util.StringTokenizer;
import view.AdminViewController;
import view.LogInViewController;

/**
 * 
 * @author Brian Wong, Laszlo Glant
 * COntains the search functions used in the application
 *
 */
public class PhotoSearch {

	/**
	 * move MyImage from source to destination album
	 * @param MyImagesSrc array list of MyImages source album
	 * @param MyImagesDest array list of MyImages destination album
	 * @param p MyImage to move from source album to destination album
	 */
	public static void movePhoto(ArrayList<MyImage> MyImagesSrc, ArrayList<MyImage> MyImagesDest, MyImage p) {
		for (int i = 0; i < MyImagesSrc.size(); i++) {
			if (MyImagesSrc.get(i).equals(p)) {
				MyImagesSrc.remove(i);
			}
		}

		MyImagesDest.add(p);
	}

	/**

	 * produce array list of MyImages with dates that are within the 2 dates given in the parameters
	 * @param MyImages array list of MyImages
	 * @param year1 year of first date
	 * @param month1 month of first date
	 * @param day1 day of first date
	 * @param year2 year of second date
	 * @param month2 month of second date
	 * @param day2 day of second date
	 * @return array list that holds MyImages within the 2 dates
	 */

	public static void searchDateRange(ArrayList<MyImage> withinRange, int year1, int month1, int day1, int year2, int month2, int day2) {
		if (compareDates(year1, month1, day1, year2, month2, day2) < 0) {
			// if second date not greater than first date, won't work!
			return;
		}

		for (int i = 0; i < AdminViewController.users.get(LogInViewController.currUser).getImageList().size(); i++) {
			if (isInside(withinRange, AdminViewController.users.get(LogInViewController.currUser).getImageList().get(i))) {
				// already have this picture, no need to add again
				continue;
			}
			if (inRange(year1, month1, day1, year2, month2, day2, AdminViewController.users.get(LogInViewController.currUser).getImageList().get(i))) {
				// MyImage is within the 2 dates, good, add to new array list
				System.out.println("adding date, within range");
				withinRange.add(AdminViewController.users.get(LogInViewController.currUser).getImageList().get(i));
			}
		}
	}



	/**
	 * return true if MyImage i is within the 2 dates in the parameters
	 * @param year1 starting year
	 * @param month1 starting month
	 * @param day1 starting day
	 * @param year2 starting year
	 * @param month2 starting month
	 * @param day2 starting day
	 * @param i MyImage instance being checked for date
	 * @return true if MyImage i in range, false otherwise
	 */

	public static boolean inRange(int year1, int month1, int day1, int year2, int month2, int day2, MyImage i) {
		// date1 <= i <= date2 (good)

		if (compareDates(year1, month1, day1, i.getYear(), i.getMonth(), i.getDay()) < 0) {
			// dates are in wrong order, not within range
			return false;
		}

		if (compareDates(i.getYear(), i.getMonth(), i.getDay(), year2, month2, day2) < 0) {
			// dates are in wrong order, not within range
			return false;
		}

		System.out.println(year1 + "/" + month1 + "/" + day1 + " < " + i.getYear() + "/" + i.getMonth() + "/" + i.getDay() + " < " + year2 + "/" + month2 + "/" + day2);
		return true;
	}


	/**
	 * return compare these 2 dates
	 * (2015 vs 2016 => 1, pos if correct order)
	 * @param year1 year of first date
	 * @param month1 month of first date
	 * @param day1 day of first date
	 * @param year2 year of second date
	 * @param month2 month of second date
	 * @param day2 day of second date
	 * @return -1 if first date bigger (wrong order), 1 if second date bigger (good order), 0 if 2 dates are exactly equal
	 */
	public static int compareDates(int year1, int month1, int day1, int year2, int month2, int day2) {
		if (year1 == year2 && month1 == month2 && day1 == day2) {
			// dates are exactly the same, return 0
			return 0;
		}

		if (year1 > year2) {
			// first date is bigger
			return -1;
		}

		if (year1 < year2) {
			// second date is bigger
			return 1;
		}

		if (month1 > month2) {
			// years are same, but month1 is bigger, so first date is bigger
			return -1;
		}

		if (month1 < month2) {
			// years are same, but month2 is bigger, so second date is bigger
			return 1;
		}

		if (day1 > day2) {
			// years and months are same, but day1 is bigger
			return -1;
		}

		if (day1 < day2) {
			// years and months are same, but day2 is bigger
			return 1;
		}

		// shouldn't happen
		System.out.println("bad dates");
		System.exit(0);
		return 100;
	}

	/**
	 * populate a2 with all images in a1 that have the correct location specified
	 * @param a2 destination array list
	 * @param location result of location text field
	 * @param year result of year text field
	 * @param month result of month text field
	 * @param day result of day text field
	 * @param people result of people text field
	 */
	public static void searchAllFields(ArrayList<MyImage> a2, String location, String year, String month, String day, String people) {
		for (int i = 0; i < AdminViewController.users.get(LogInViewController.currUser).getImageList().size(); i++) {
			System.out.println("i " + i);
			if (isInside(a2, AdminViewController.users.get(LogInViewController.currUser).getImageList().get(i))) {
				// is inside already, don't add again
				continue;
			}
			
			if (location == null) {
				
			} else if (location.equals("")) {
				// location is blank
				System.out.println("location is blank");
			} else if (location.equals(AdminViewController.users.get(LogInViewController.currUser).getImageList().get(i).getLocation())){
				// matching location
				a2.add(AdminViewController.users.get(LogInViewController.currUser).getImageList().get(i));
				System.out.println("adding due to same location");
				continue;
			}

			if (year.equals("")) {
				// year is blank
			} else if (year.equals(AdminViewController.users.get(LogInViewController.currUser).getImageList().get(i).getYear() + "")){
				// year matches
				a2.add(AdminViewController.users.get(LogInViewController.currUser).getImageList().get(i));
				System.out.println("adding due to same year");
				continue;
			}

			if (month.equals("")) {
				// month is blank
			} else if (month.equals(AdminViewController.users.get(LogInViewController.currUser).getImageList().get(i).getMonth() + "")){
				// month matches
				a2.add(AdminViewController.users.get(LogInViewController.currUser).getImageList().get(i));
				System.out.println("adding due to same month");
				continue;
			}

			if (day.equals("")) {
				// day is blank
			} else if (day.equals(AdminViewController.users.get(LogInViewController.currUser).getImageList().get(i).getDay() + "")){
				// day matches
				a2.add(AdminViewController.users.get(LogInViewController.currUser).getImageList().get(i));
				System.out.println("adding due to same day");
				continue;
			}

			if (people == null) {
				
			} else if (people.equals("")) {
				// people is blank
			} else if (numMatchingPeople(AdminViewController.users.get(LogInViewController.currUser).getImageList().get(i).getPeople(), people) > 0) {
				// at least one person matches
				a2.add(AdminViewController.users.get(LogInViewController.currUser).getImageList().get(i));
				System.out.println("adding due to matching person");
				continue;
			}
		}
	}
	
	/**
	 * check if mi exists in arr
	 * @param arr arrayList of MyImage instances
	 * @return true if exists inside, false otherwise
	 */
	public static boolean isInside(ArrayList<MyImage> arr, MyImage mi) {
		for (int i = 0; i < arr.size(); i++) {
			if (arr.get(i).getPicturePath().equals(mi.getPicturePath())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * return the number of names that match
	 * @param names1 String of names in master list
	 * @param names2 String of names in people text field
	 * @return number of names that match, 0 if none match
	 */
	public static int numMatchingPeople(String names1, String names2) {
		if ((names1 == null) || (names2 == null)) {
			return 0;
		}

		if (names1.length() == 0) {
			return 0;
		}

		if (names2.length() == 0) {
			return 0;
		}

		ArrayList<String> arr1 = new ArrayList<String>();
		ArrayList<String> arr2 = new ArrayList<String>();

		StringTokenizer st = new StringTokenizer(names1);
		while (st.hasMoreTokens()) {
			arr1.add(st.nextToken());
		}

		StringTokenizer st2 = new StringTokenizer(names2);
		while (st2.hasMoreTokens()) {
			arr2.add(st2.nextToken());
		}

		arr1.retainAll(arr2);
		return arr1.size();
	}

	/**
	 * return false if str has any duplicate names (ex. Brian Brian), true otherwise
	 * @param str people.getText
	 * @return true if good, false if bad
	 */
	public static boolean namesNoDups(String str) {
		StringTokenizer st = new StringTokenizer(str);
		ArrayList<String> arr = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			arr.add(st.nextToken());
		}
		
		if (arr.size() == 1) {
			return false;
		}
		
		for (int i = 0; i < arr.size(); i++) {
			for (int j = 0; j < arr.size(); j++) {
				if (i == j) {
					continue;
				}
				if (arr.get(i).equalsIgnoreCase(arr.get(j))) {
					System.out.println(arr.get(i) + " = " + arr.get(j));
					return true;
				}
			}
		}
		return false;
	}

}
