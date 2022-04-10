import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
	static Scanner inputScanner;
	static TST stopNameSearchTree;
	static HashMap<String, BusStopData> busStopDetails;
	static ShortestPaths shortestPaths;

	public static void main(String[] args) {
		inputScanner = new Scanner(System.in);
		busStopDetails = new HashMap<String, BusStopData>();
		stopNameSearchTree = importTSTData();
		shortestPaths = new ShortestPaths();
		mainMenu();
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
				// Add data to hashmap
				busStopDetails.put(name, data);
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
		for(Integer stop:route.stops) {
			System.out.println(stop);
		}
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

	public static void searchByArrivalTime() {
		System.out.println();
		System.out.println("Search for trips by arrival time.");
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
}
