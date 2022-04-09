import java.util.ArrayList;

// Edge Weighted Directed Graph
public class EWDGraph {
	ArrayList<WeightedEdge>[] adjacencyList;
	ArrayList<WeightedEdge> edges;

	/**
	 * @param numNodes: The number of nodes in the graph.
	**/
	EWDGraph(int numNodes) {
		adjacencyList = new ArrayList[numNodes];
		edges = new ArrayList<WeightedEdge>();
		for(int i = 0;i < numNodes;i++) {
			adjacencyList[i] = new ArrayList<WeightedEdge>();
		}
	}

	/**
	 * @param from:   Node to add an edge from.
	 * @param to:     Node this edge is to.
	 * @param weight: Weight of this edge.
	**/
	public void addEdge(int from, int to, double weight) {
		WeightedEdge edge = new WeightedEdge(from, to, weight);
		adjacencyList[from].add(edge);
		edges.add(edge);
	}

	// Get all edges in graph.
	public ArrayList<WeightedEdge> getEdges() {
		return edges;
	}

	/**
	 * @return: Size of the graph.
	**/
	public int getSize() {
		return adjacencyList.length;
	}

	/**
	 * @param node: Node to get the neighbours of.
	 *
	 * @return list containing edges with weights.
	**/
	public ArrayList<WeightedEdge> getNeighbours(int node) {
		return adjacencyList[node];
	}
}
