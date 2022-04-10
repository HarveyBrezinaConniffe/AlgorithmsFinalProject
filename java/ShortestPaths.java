import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

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

		importData();
	}

	public void importData() {
		try {
			File busStopsFile = new File("../data/transfers.txt");
			Scanner scanner = new Scanner(busStopsFile);

			String nextLine = scanner.nextLine();
			while(scanner.hasNextLine()) {
				nextLine = scanner.nextLine();
				String[] components = nextLine.split("\\,");
				int fromID = Integer.parseInt(components[0]);
				int toID = Integer.parseInt(components[1]);
				int transferType = Integer.parseInt(components[2]);
				int minTransferTime = -1;
				// If transferType == 0 there won't be a minTransferTime
				if(components.length > 3) {
					minTransferTime = Integer.parseInt(components[3]);
				}

				int fromIndex = idToIndex.get(fromID);
				int toIndex = idToIndex.get(toID);

				double cost = 99999;
				if(transferType == 0) {
					cost = 2;
				}
				else if(transferType == 2) {
					cost = minTransferTime/100.;
				}
				graph.addEdge(fromIndex, toIndex, cost);
			}
		}	
		catch(FileNotFoundException e) {}

		try {
			File busStopsFile = new File("../data/stop_times.txt");
			Scanner scanner = new Scanner(busStopsFile);

			String nextLine = scanner.nextLine();
			String lastLine = scanner.nextLine();
			int lastID = Integer.parseInt(lastLine.split("\\,")[0]);
			int lastStop = Integer.parseInt(lastLine.split("\\,")[3]);
			while(scanner.hasNextLine()) {
				nextLine = scanner.nextLine();
				String[] components = nextLine.split("\\,");
				int currentID = Integer.parseInt(components[0]);
				int currentStop = Integer.parseInt(components[3]);
				if(currentID == lastID) {
					int fromIndex = idToIndex.get(lastStop);
					int toIndex = idToIndex.get(currentStop);
					graph.addEdge(fromIndex, toIndex, 1);
				}
				lastID = currentID;
				lastStop = currentStop;
			}
		}
		catch(FileNotFoundException e) {}
	}

	/**
	 * @param sourceNode: The source node.
	 * @param destinationNode: The destination node.
	 *
	 * @return: A Path object containing the shortest path and it's cost.
	**/
	public Path shortestPaths(int sourceStop, int destStop) {
		int sourceNode = idToIndex.get(sourceStop);
		int destNode = idToIndex.get(destStop);

		// Initialize array to store which nodes are finalized and the distances from the source to each.
		Set<Integer> finalizedNodes = new HashSet<Integer>();	
		double[] distances = new double[graph.getSize()];

		// Array to store the parent of each node
		int[] parents = new int[graph.getSize()];

		// Set all distances to max value and the distance to source node 0.	
		for(int i = 0;i < graph.getSize();i++) {
			distances[i] = Double.MAX_VALUE;
			// Initialize parent of node to -1
			parents[i] = -1;
		}
		distances[sourceNode] = 0;
		parents[sourceNode] = -2;

		// Keep going until all nodes are finalized.
		while(finalizedNodes.size() < graph.getSize()) {
			// Find the node in distances with the lowest distance.
			double minDistance = Double.MAX_VALUE;
			int minNode = -1;

			for(int i = 0;i < graph.getSize();i++) {
				if(distances[i] < minDistance && !finalizedNodes.contains(i)) {
					minDistance = distances[i];
					minNode = i;
				}
			}
			
			// Check if minNode == -1. If so then this graph is not connected.
			if(minNode == -1) {
				double[] returnVal = {-1};
				return null;
			}

			// Get the neighbours of min node and update their distances.
			ArrayList<WeightedEdge> neighbours = graph.getNeighbours(minNode);
			for(WeightedEdge neighbour : neighbours) {
				double newDistance = distances[minNode]+neighbour.weight;
				if(distances[neighbour.to] > newDistance) {
					distances[neighbour.to] = newDistance;
					parents[neighbour.to] = minNode;
				}
			}

			// Finalize the min node.
			finalizedNodes.add(minNode);

			if(minNode == destNode) {
				break;			
			}
		}

		// Backtrace from destination to source.
		ArrayList<Integer> stopIndexes = new ArrayList<Integer>();
		int currentNode = destNode;
		while(currentNode != sourceNode) {
			stopIndexes.add(currentNode);
			currentNode = parents[currentNode];
		}
		stopIndexes.add(currentNode);

		// Convert from indexes to ID's and reverse.
		ArrayList<Integer> stops = new ArrayList<Integer>();
		for(int i = stopIndexes.size()-1;i>=0;i--) {
			currentNode = stopIndexes.get(i);
			int currentStop = indexToID.get(i); 
			stops.add(currentStop);
		}

		return new Path(stops, distances[destNode]);
	}
}
