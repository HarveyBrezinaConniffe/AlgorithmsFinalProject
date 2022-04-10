import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

public class Main {
	static Scanner inputScanner;
	static TST stopNameSearchTree;
	static HashMap<String, BusStopData> busStopDetails;
	static HashMap<Integer, String> stopIDToName;
	static ShortestPaths shortestPaths;

	static HashMap<String, HashMap<Integer, Trip>> arrivalTimeToTrips;

	public static void main(String[] args) {
		inputScanner = new Scanner(System.in);
		busStopDetails = new HashMap<String, BusStopData>();
		stopIDToName = new HashMap<Integer, String>();
		stopNameSearchTree = importTSTData();
		shortestPaths = new ShortestPaths();
		arrivalTimeToTrips = new HashMap<String, HashMap<Integer, Trip>>();
		loadTrips();
		mainMenu();
	}

	public static void loadTrips() {
		try {
			File tripsFile = new File("../data/stop_times.txt");
			Scanner tripsScanner = new Scanner(tripsFile);

			String nextLine = tripsScanner.nextLine();
			while(tripsScanner.hasNextLine()) {
				nextLine = tripsScanner.nextLine();
				String[] components = nextLine.split("\\,");

				String shapeDistTraveled = "";
				if(components.length == 9) {
					shapeDistTraveled = components[8];
				}

				Trip currentTrip = new Trip(
					components[0],
					components[1],
					components[2],
					components[3],
					components[4],
					components[5],
					components[6],
					components[7],
					shapeDistTraveled
				);

				String arrivalTime = components[1];
				if(!arrivalTimeToTrips.containsKey(arrivalTime)) {
					arrivalTimeToTrips.put(arrivalTime, new HashMap<Integer, Trip>());
				}

				int tripID = Integer.parseInt(components[0]);
				arrivalTimeToTrips.get(arrivalTime).put(tripID, currentTrip);
			}
		}
		catch(FileNotFoundException e) {}
	}

	// Function that imports bus stop names for use by TST.
	public static TST importTSTData() {
		// Create TST
		TST tree = new TST();

		// Read in file.
		try {
			File busStopsFile = new File("../data/stops.txt");

			Scanner stopsScanner = new Scanner(busStopsFile);

			// Go through each line in file.
			String nextLine = stopsScanner.nextLine();
			while(stopsScanner.hasNextLine()) {
				// Load next line.
				nextLine = stopsScanner.nextLine();
				// Split components apart.
				String[] components = nextLine.split("\\,");
				// Get the name.
				String name = components[2];
				// Move keywords to end of name.
				name = moveKeywords(name);
				// Add name to TST.
				tree.insert(name);

				// Parent station field is sometimes blank
				String parentStation;
				if(components.length == 9) {
					parentStation = "";
				}
				else {
					parentStation = components[9];
				}

				// Create new BusStopData object
				BusStopData data = new BusStopData(components[0], 
								   components[1], 
								   components[2], 
								   components[3], 
								   components[4], 
								   components[5], 
								   components[6], 
								   components[7], 
								   components[8], 
								   parentStation);
				// Add data to hashmaps
				busStopDetails.put(name, data);
				stopIDToName.put(Integer.parseInt(components[0]), name);	
			}
		}
		catch(FileNotFoundException e) {}

		return tree;
	}

	// Function that moves the keywords from a bus stop name to end.
	public static String moveKeywords(String stopName) {
		String output = "";
		// Split name by space
		String[] stringComponents = stopName.split(" ");

		// First pass through split string
		for(String component : stringComponents) {
			// If it's not a keyword append it to the end of the output.
			if(!(component.equals("FLAGSTOP") || component.equals("WB") || component.equals("NB") || component.equals("SB") || component.equals("EB"))) {
				output += component+" ";
			}
		}

		// Second pass through split string
		for(String component : stringComponents) {
			// If it is a keyword append it to the end of the output.
			if(component.equals("FLAGSTOP") || component.equals("WB") || component.equals("NB") || component.equals("SB") || component.equals("EB")) {
				output += component+" ";
			}
		}

		// Remove final " " from output.
		output = output.substring(0, output.length()-1);

		return output;
	}

	public static void mainMenu() {
		System.out.println("Welcome to my algorithms final project!");
		boolean exit = false;
		while(!exit) {	
			System.out.println("What would you like to do?");
			System.out.println("	1. Find the shortest path between two bus stops.");
			System.out.println("	2. Search for bus stops by name.");
			System.out.println("	3. Search for trips by arrival time.");
			System.out.print(">> ");
			
			int choice = -1;
			while(choice == -1) {
				if(inputScanner.hasNextInt()) {
					int nextInt = inputScanner.nextInt();
					// Clear newline from buffer
					inputScanner.skip("\\R?");
					if(nextInt >= 1 && nextInt <= 3) {
						choice = nextInt;
					}
					else {
						System.out.println("Sorry! I don't understand that input, Please enter a number between 1 and 3!");
						System.out.print(">> ");
					}
				}	
				else if(inputScanner.hasNext()) {
					inputScanner.next();
					System.out.println("Sorry! I don't understand that input, Please enter a number between 1 and 3!");
					System.out.print(">> ");
				}
			}

			if(choice == 1) {
				shortestPathMenu();
			}
			else if(choice == 2) {
				searchByNameMenu();
			}
			else if(choice == 3) {
				searchByArrivalTime();
			}
		}
	}

	public static void shortestPathMenu() {
		System.out.println();
		System.out.println("Find the shortest path between two bus stops.");
		System.out.println("Enter first bus stop:");
		System.out.print(">> ");

		// Get first stop.
		boolean stopExists = false;
		String firstStop = "";
		while(!stopExists) {
			firstStop = inputScanner.nextLine();
			firstStop = firstStop.toUpperCase();
			stopExists = busStopDetails.containsKey(firstStop);
			if(!stopExists) {
				System.out.println("Sorry! I couldn't find that bus stop?");
				System.out.println("Enter first bus stop:");
				System.out.print(">> ");
			}
		}

		System.out.println("Enter second bus stop:");
		System.out.print(">> ");
		// Get second stop.
		stopExists = false;
		String secondStop = "";
		while(!stopExists) {
			secondStop = inputScanner.nextLine();
			secondStop = secondStop.toUpperCase();
			stopExists = busStopDetails.containsKey(secondStop);
			if(!stopExists) {
				System.out.println("Sorry! I couldn't find that bus stop?");
				System.out.println("Enter second bus stop:");
				System.out.print(">> ");
			}
		}
		
		int firstID = Integer.parseInt(busStopDetails.get(firstStop).stopID);	
		int secondID = Integer.parseInt(busStopDetails.get(secondStop).stopID);
		Path route = shortestPaths.shortestPaths(firstID, secondID);

		System.out.println();
		System.out.printf("Shortest route has a cost of: %f\n", route.cost);
		System.out.println("Stops on route:");
		for(Integer stop:route.stops) {
			String name = stopIDToName.get(stop);
			System.out.println(name);
		}
		System.out.println();
	}

	public static void searchByNameMenu() {
		System.out.println();
		System.out.println("Searching for bus stops by name.");
		System.out.println("Enter some or all of a bus stop name to seach for matching stops.");
		System.out.print(">> ");

		String stopName = inputScanner.nextLine();
		// Convert to upper case
		stopName = stopName.toUpperCase();
		// Get completions
		ArrayList<String> autocompletions = stopNameSearchTree.autocomplete(stopName);

		for(String completion : autocompletions) {
			String completedStopName = stopName+completion;
			BusStopData data = busStopDetails.get(completedStopName);

			System.out.println("--------------------");
			System.out.println(completedStopName);
			System.out.printf("ID: %s\n", data.stopID);
			System.out.printf("Code: %s\n", data.stopCode);
			System.out.printf("Description: %s\n", data.stopDesc);
			System.out.printf("Lat: %s\n", data.stopLat);
			System.out.printf("Lon: %s\n", data.stopLon);
			System.out.printf("Zone ID: %s\n", data.zoneID);
			System.out.printf("URL: %s\n", data.stopURL);
			System.out.printf("Location Type: %s\n", data.stopLocationType);
			System.out.printf("Parent Station: %s\n", data.parentStation);
			System.out.println("--------------------");
		}
	}

	public static boolean isValidTime(String currentTime) {
		String[] components = currentTime.split("\\:");
		if(components.length != 3) {
			System.out.println("Please enter a time in the format HH:MM:SS");
			return false;
		}

		int hours, minutes, seconds;

		try {
			hours = Integer.parseInt(components[0]);
			minutes = Integer.parseInt(components[1]);
			seconds = Integer.parseInt(components[2]);
		}
		catch(Exception e) {
			System.out.println("Hours, Minutes and Seconds must be valid whole numbers!");	
			return false;
		}
		if(hours < 0 || hours > 23) {
			System.out.println("Hours must be between 0 and 23");
			return false;
		}
		if(minutes < 0 || minutes > 59) {
			System.out.println("Minutes must be between 0 and 59");
			return false;
		}
		if(seconds < 0 || seconds > 59) {
			System.out.println("Seconds must be between 0 and 59");
			return false;
		}
		return true;
	}

	public static void searchByArrivalTime() {
		System.out.println();
		System.out.println("Search for trips by arrival time.");
		System.out.println("Please enter an arrival time in HH:MM:SS format:");
		System.out.print(">> ");

		String arrivalTime = inputScanner.nextLine();
		while(!isValidTime(arrivalTime)) {
			System.out.print(">> ");
			arrivalTime = inputScanner.nextLine();
		}

		HashMap<Integer, Trip> trips = arrivalTimeToTrips.get(arrivalTime);

		ArrayList<Integer> tripIDs = new ArrayList<Integer>(trips.keySet());
		Collections.sort(tripIDs);

		for(Integer tripID : tripIDs) {
			Trip currentTrip = trips.get(tripID);
			System.out.println("--------------------");
			System.out.printf("Trip ID: %d\n", tripID);
			System.out.printf("Arrival Time: %s\n", currentTrip.arrivalTime);
			System.out.printf("Departure Time: %s\n", currentTrip.departureTime);
			System.out.printf("Stop ID: %s\n", currentTrip.stopID);
			System.out.printf("Stop Sequence: %s\n", currentTrip.stopSequence);
			System.out.printf("Stop Head Sign: %s\n", currentTrip.stopHeadsign);
			System.out.printf("Pickup Type: %s\n", currentTrip.pickupType);
			System.out.printf("Drop Off Type: %s\n", currentTrip.dropOffType);
			System.out.printf("Shape Dist Traveled: %s\n", currentTrip.shapeDistTraveled);
			System.out.println("--------------------");
		}
	}

	// Class used to store data about a bus stop.
	private static class BusStopData {
		String stopName, stopDesc, stopURL, stopLocationType, stopLat, stopLon, stopID, stopCode, zoneID, parentStation;

		public BusStopData(String stopID, String stopCode, String stopName, String stopDesc, String stopLat, String stopLon, 
				   String zoneID, String stopURL, String stopLocationType, String parentStation) {
			this.stopID = stopID;
			this.stopCode = stopCode;
			this.zoneID = zoneID;
			this.parentStation = parentStation;
			this.stopLat = stopLat;
			this.stopLon = stopLon;
			this.stopName = stopName;
			this.stopDesc = stopDesc;
			this.stopURL = stopURL;
			this.stopLocationType = stopLocationType;
		}
	}

	private static class Trip {
		String tripID, arrivalTime, departureTime, stopID, stopSequence, stopHeadsign, pickupType ,dropOffType, shapeDistTraveled;

		public Trip(String tripID, String arrivalTime, String departureTime, String stopID, String stopSequence, String stopHeadsign, String pickupType, String dropOffType, String shapeDistTraveled) {
			this.tripID = tripID;
			this.arrivalTime = arrivalTime;
			this.departureTime = departureTime;
			this.stopID = stopID;
			this.stopSequence = stopSequence;
			this.stopHeadsign = stopHeadsign;
			this.pickupType = pickupType;
			this.dropOffType = dropOffType;
			this.shapeDistTraveled = shapeDistTraveled;
		}
	}
}
