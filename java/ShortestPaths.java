import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ShortestPaths {
	EWDGraph graph;
	// Maps bus stop ids to consecutive integers.
	HashMap<Integer, Integer> idToIndex;
	// Maps consecutive integers to bus stop IDs.
	HashMap<Integer, Integer> indexToID;

	public ShortestPaths() {
		idToIndex = new HashMap<Integer, Integer>();
		indexToID = new HashMap<Integer, Integer>();

		// Read in all bus stops, Creating an ID -> Consecutive int mapping.
		try {
			File busStopsFile = new File("../data/stops.txt");
			Scanner stopsScanner = new Scanner(busStopsFile);
			int currentNumber = 0;

			String nextLine = stopsScanner.nextLine();
			while(stopsScanner.hasNextLine()) {
				// Load next line.
				nextLine = stopsScanner.nextLine();
				// Split components apart.
				String[] components = nextLine.split("\\,");
				int stopID = Integer.parseInt(components[0]);
				// Create mappings.
				idToIndex.put(stopID, currentNumber);
				indexToID.put(currentNumber, stopID);
				currentNumber += 1;
			}

			graph = new EWDGraph(currentNumber);
		}
		catch(FileNotFoundException e) {}
	}

	public void importData() {
		
	}
}
