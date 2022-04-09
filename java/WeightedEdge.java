public class WeightedEdge {
	int to;
	int from;
	double weight;

	/**
	 * @param to:     Node this edge is to.
	 * @param weight: Weight of this edge.
	**/
	WeightedEdge(int from, int to, double weight) {
		this.from = from;
		this.to = to;
		this.weight = weight;
	}
}
