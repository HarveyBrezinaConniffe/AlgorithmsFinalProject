import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
	static Scanner inputScanner;
	static TST stopNameSearchTree;

	public static void main(String[] args) {
		inputScanner = new Scanner(System.in);
		stopNameSearchTree = importTSTData();
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
			String nextLine;
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

	public static void shortestPathMenu() {
		System.out.println();
		System.out.println("Find the shortest path between two bus stops.");
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
			System.out.println("--------------------");
			System.out.println(completedStopName);
			System.out.println("--------------------");
		}
	}

	public static void searchByArrivalTime() {
		System.out.println();
		System.out.println("Search for trips by arrival time.");
	}
}
