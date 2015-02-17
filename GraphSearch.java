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
		
		Reader reader = new Reader();
		readIn(reader, args[1]);
			
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
		/*//Test Code
		Graph test 	= new Graph();
		Node[] n 	= {new Node("0"), new Node("1"),new Node("2"), new Node("3"), new Node("4"), new Node("5")};
		n[0].addNeighbour(n[4]);
		n[1].addNeighbour(n[2]);n[1].addNeighbour(n[5]);n[1].addNeighbour(n[3]);
		n[2].addNeighbour(n[1]);n[2].addNeighbour(n[3]);n[2].addNeighbour(n[4]);
		n[3].addNeighbour(n[2]);n[3].addNeighbour(n[1]);
		n[4].addNeighbour(n[5]);n[4].addNeighbour(n[0]);n[4].addNeighbour(n[2]);
		n[5].addNeighbour(n[1]);n[5].addNeighbour(n[4]);
		for(int i = 0; i < 6; i++)		
			test.add(n[i]);
		for(int i = 1; i < 10; i++)
			System.out.println("Number of cliques of size " +  i + ": " + findNumberOfCliques(reader.graph(), i));
		*/
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
		
		Set<Set<Node>> SingletCliques = new HashSet<Set<Node>>();
		
		for(Node node: graph.nodes()){//makes a load of cliques each with one node
			Set<Node> tmp = new HashSet<Node>();
			tmp.add(node);
			SingletCliques.add(tmp);
		}
		Set<Set<Node>> cliquesOld = SingletCliques;
		Set<Set<Node>> cliquesNew;
		for(int i = 1; i<n; i++){//adds every possibility of a new neighbour to each clique each time
			cliquesNew = new HashSet<Set<Node>>();
			for(Set<Node> clique: cliquesOld){//goes through every clique
				for(Node neighbour: clique.iterator().next().neighbours()){//goes through every neighbour of first node to see if it should add
					boolean neighboursIsConnectedToAllNodesInClique = true;//init
					for(Node node: clique){//checks all node in clique to see if connects to current neighbour
						if(!neighbour.neighbours().contains(node)){
							neighboursIsConnectedToAllNodesInClique = false;
						}
					}
					if(neighboursIsConnectedToAllNodesInClique){//if good make copy of clique with new neighbour added
						Set<Node> newClique = new HashSet<Node>(clique);
						newClique.add(neighbour);
						if(cliqueNotAlreadyThere(cliquesNew, newClique)){//only add to set of cliques if it's not already there
							cliquesNew.add(newClique);
						}
					}
				}
			}
				
			cliquesOld = cliquesNew;//get sets ready for next iteration.
			
		}
		
		return cliquesOld.size();
		
	}
	
	private static boolean cliqueNotAlreadyThere(Set<Set<Node>> cliques, Set<Node> newClique) {
		
		for(Set<Node> clique: cliques){
			if(cliquesAreEqual(clique, newClique)){
				return false;
			}
		}
		return true;
	}

	private static boolean cliquesAreEqual(Set<Node> clique, Set<Node> newClique) {
		
		for(Node node: clique){
			if(!newClique.contains(node)){
				return false;
			}
		}
		
		return true;
	}
	
}

	
