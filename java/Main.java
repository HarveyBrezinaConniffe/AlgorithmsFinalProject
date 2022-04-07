import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
	static Scanner inputScanner;

	public static void main(String[] args) {
		inputScanner = new Scanner(System.in);
		importTSTData();
		//mainMenu();
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
				nextLine = stopsScanner.nextLine();
				String[] components = nextLine.split("\\,");
				String name = components[2];
				name = moveKeywords(name);
				System.out.println(name);
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
		System.out.println("Search for bus stops by name.");
	}

	public static void searchByArrivalTime() {
		System.out.println();
		System.out.println("Search for trips by arrival time.");
	}
}
