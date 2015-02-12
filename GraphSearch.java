import java.util.*;
class GraphSearch{
	
	static Comparator compr;
	
	public static void main(String[] in){
		
		compr = new Comparator(){
			public int compare(Object n1, Object n2){
				int one = Integer.parseInt(((Node) n1).name());
				int two = Integer.parseInt(((Node) n2).name());
				return Integer.compare(one, two);
			}		
		};
		
		Reader reader = new Reader();
		String inStr = "graph.txt";
		readIn(reader, inStr);
		//printOut(reader.graph().nodes());
		//neighbourSearch(reader.graph(), 0);
		//printOut(findFullyConnectedNeighbours(reader.graph()));
		System.out.println(findNumberOfCliques(reader.graph(), 3));
	}

	private static void printOut(Set<Node> nodes) {
		
		Iterator<Node> it = getSortedIterator(nodes);
		
		while(it.hasNext()){
			Node n = it.next();
						
			Iterator<Node> itNb = getSortedIterator(n.neighbours());
			String s = "";
			
			while(itNb.hasNext()){
				Node ne = itNb.next();
				s = s+" "+ne.name();
			}
			
			System.out.println(n.name() + s);
		}
		
	}

	private static Iterator getSortedIterator(Set<Node> nodes) {
		List<Node> sortedNodes = new ArrayList<Node>(nodes);
		Collections.sort(sortedNodes, compr);
		return sortedNodes.iterator();
		
	}

	private static void readIn(Reader reader, String s) {
		try{
			reader.read(s);
		}catch(Exception e){
			System.err.println("File Doesn't Exist");
		}
	}
	
	public static Set<Node> neighbourSearch(Graph graph, int minSize)
	{
		// create the output list
		Set<Node> output = new HashSet<Node>();
		
		Iterator<Node> it = graph.nodes().iterator();
		
		while(it.hasNext()){
			Node node = it.next();
			if(node.neighbours().size()>=minSize)
				output.add(node);
		}
		
		return output;
	}
	
	public static Set<Node> findFullyConnectedNeighbours(Graph graph){
		Set<Node> output = new HashSet<Node>();
		Iterator<Node> it = getSortedIterator(graph.nodes());
		
		while(it.hasNext()){
			Node node = it.next();
			
			if(areAllNeighboursFullyConnected(graph.nodes(), node.neighbours())){
				output.add(node);
			}
		}
		
		return output;
	}

	private static boolean areAllNeighboursFullyConnected(Set<Node> allNodes, Set<Node> neighbours) {
		Iterator<Node> neighboursIt = getSortedIterator(neighbours);
		
		String[] neighboursArray = new String[neighbours.size()];
		int i = 0;
		while(neighboursIt.hasNext()){
			Node node = neighboursIt.next();
			neighboursArray[i] = node.name();
			i++;
		}
		
		for(i = 0; i < neighboursArray.length; i++){
			for(int j = i+1; j < neighboursArray.length; j++){
				if(!areNodesConnected(neighboursArray[i], neighboursArray[j], allNodes))
					return false;
			}
		}
		
		return true; 
	}

	private static boolean areNodesConnected(String s1, String s2, Set<Node> allNodes) {
		Iterator<Node> it = getSortedIterator(allNodes);
		while(it.hasNext()){
			Node node = it.next();
			if(node.name().equals(s1)){
				Iterator<Node> itNeighbours = getSortedIterator(node.neighbours());
				while(itNeighbours.hasNext()){
					Node neighbour = itNeighbours.next();
					if(neighbour.name().equals(s2))
						return true;
				}
			}
		}
		return false;
	}
	
	public static int findNumberOfCliques(Graph graph, int n){
		Set<Node> doneNodes = new HashSet<Node>();
		Iterator<Node> it = getSortedIterator(graph.nodes());
		int total = 0;
		while(it.hasNext()){
			Node node = it.next();
			total = total + sumPaths(doneNodes, node, n);
			doneNodes.add(node);
		}
		return total;
	}
			
			
			
	public static int sumPaths(Set<Node> doneNodes, Node node, int n){
		int total = 0;
		if(n == 1){
			return 1;
		}else{
			Iterator<Node> it = getSortedIterator(node.neighbours());
			while(it.hasNext()){
				Node neighbour = it.next();
				if(!doneNodes.contains(neighbour))
					total = total + sumPaths(doneNodes, neighbour, n-1);
			}
			return total;
		}
	}
	
	/*public static int findNumberOfCliques(Graph graph, int n){
		Set<Node> nodesLeft = new HashSet<Node>(graph.nodes());
		Iterator<Node> it = getSortedIterator(graph.nodes());
		int numberOfCliques = 0;
		while(it.hasNext()){
			Node node = it.next();
			if(nodesLeft.contains(node)){
				int size = 1;
				nodesLeft.remove(node);
				size += addNeighboursToClique(node, nodesLeft);
				if(size==n)
					numberOfCliques++;
			}
		}
		return numberOfCliques;
	}

	private static int addNeighboursToClique(Node node, Set<Node> nodesLeft) {
		int tot=0;
		for(Node neighbour : node.neighbours()){
			if(nodesLeft.contains(neighbour)){
				nodesLeft.remove(neighbour);
				tot += addNeighboursToClique(neighbour, nodesLeft);
			}
		}
		return node.neighbours().size()+tot;
		
	}
*/	
}
