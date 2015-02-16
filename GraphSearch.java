import java.util.*;
class GraphSearch{
	
	static Comparator compr;
	
	public static void main(String[] args){
		
		compr = new Comparator(){
			public int compare(Object n1, Object n2){
				int one = Integer.parseInt(((Node) n1).name());
				int two = Integer.parseInt(((Node) n2).name());
				return Integer.compare(one, two);
			}		
		};
		
		//Reader reader = new Reader();
		//readIn(reader, args[1]);
		/*		
		if(args[0].equals("-p1")){
			printOut(reader.graph().nodes());
		}else if(args[0].equals("-p2")){
			int i = Integer.parseInt(args[2]);
			int n = neighbourSearch(reader.graph(), i).size();
			System.out.println("Number of nodes with at least " +  i + " neighbours: " + n);
		}else if(args[0].equals("-p3")){
			System.out.println("number of nodes with fully connected neighbours: " + findFullyConnectedNeighbours(reader.graph()).size());
		}else if(args[0].equals("-p4")){
			int i = Integer.parseInt(args[2]);
			System.out.println("Number of cliques of size " +  i + ": " + findNumberOfCliques(reader.graph(), i));		
		}
		*/
		Graph test 	= new Graph();
		Node[] n 	= {new Node("0"), new Node("1"),new Node("2"), new Node("3"), new Node("4"), new Node("5")};
		n[0].addNeighbour(n[4]);
		n[1].addNeighbour(n[2]);n[1].addNeighbour(n[5]);
		n[2].addNeighbour(n[1]);n[2].addNeighbour(n[3]);n[2].addNeighbour(n[4]);
		n[3].addNeighbour(n[2]);
		n[4].addNeighbour(n[5]);n[4].addNeighbour(n[0]);n[4].addNeighbour(n[2]);
		n[5].addNeighbour(n[1]);n[5].addNeighbour(n[4]);
		for(int i = 0; i < 6; i++)		
			test.add(n[i]);
		for(int i = 1; i < 10; i++)
			System.out.println("Number of cliques of size " +  i + ": " + findNumberOfCliques(test, i));
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
			Set<Node> visited = new HashSet<Node>();
			visited.add(node);
			total = total + sumPaths(doneNodes, node, visited, n);
			doneNodes.add(node);
		}
		return total;
	}
				
	public static int sumPaths(Set<Node> doneNodes, Node node, Set<Node> visited, int n){
		int total = 0;
		if((n == 1) && (!doneNodes.contains(node))){
			return 1;
		}else if(n == 1){
			return 0;
		}else{
			Iterator<Node> it = getSortedIterator(node.neighbours());
			while(it.hasNext()){
				Node neighbour = it.next();
				if(!visited.contains(neighbour)){
					visited.add(node);
					total = total + sumPaths(doneNodes, neighbour, visited, n-1);
				}			
			}
			return total;
		}
	} 

}
