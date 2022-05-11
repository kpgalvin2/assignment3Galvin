//@Kevin Galvin
//@5 May 2022, Version 1.0
//CS245 Assignment3: Graph

import java.time.temporal.Temporal;
import java.util.*;

public class Graph {
    /**
     * Edge class
     * Has three parameters which hold the source currency, target currency, and tradecount
     */
    class Edge {
        public final String s; // source
        public final String t; // target 
        public final Double weight; // weight

        /**
         * Edge constructor 
         * @param s source currency 
         * @param t target currency
         * @param w tradecount
         */
        public Edge(String s, String t, Double w) {
            this.s = s;
            this.t = t;
            this.weight = w;
        }

        public String toString() {
            return s+"->"+t+":"+weight;
        }
    }

    /* instance variables for Graph class */
    private final HashSet<String> setV; // List of vertices 
    private final HashMap<String, List<Edge>> adjacencyList;

    /**
     * Graph constructor that initializes instance variables and builds the adjacencyList
     * @param HashMap<String, Double> "Base Asset, Quote Asset" and total trade counts data
     */
    public Graph(HashMap<String, Double> counts) {
        setV = new HashSet<>(); // Vertices list 
        adjacencyList = new HashMap<>(); 
        for (Map.Entry mapElement : counts.entrySet()) { // Initializes the vertices list 
            String key = (String)mapElement.getKey(); // Gets the key 
            String[] components = key.split(",",-1);
            String base = components[0]; // Source currency 
            String quote = components[1]; // Target currency 
            if (!(mapElement.getValue().equals(0.0))) { // Adds vertices 
                setV.add(quote); 
                setV.add(base);
            }
        }
        for (Map.Entry mapElement : counts.entrySet()) { // Initializes the adjacency list, builds the graph 
            String key = (String)mapElement.getKey(); // gets key
            String[] components = key.split(",",-1);
            String base = components[0]; // gets source 
            String quote = components[1]; // gets target 
            Double value = (Double)mapElement.getValue(); // gets tradecount 
            Edge edge = new Edge(base,quote,value); // creates new edge 
            if (!(value.equals(0.0))) { 
                List<Edge> list = new ArrayList<>();
                if (!(adjacencyList.containsKey(base))) { // Adds to adjacency list if not already present, creates ArrayList of edges for that source 
                    list.add(edge); 
                    adjacencyList.put(base,list);
                }
                else { // Adds to adjacency list if present, adds to existing ArrayList of that source 
                    adjacencyList.get(base).add(edge);
                }
            }
        }
    }

    /** 
     * findFlows takes the source cryptocurrency and finds a shortest path from the source 
     * to all reachable cryptocurrencies, and calculate the net trades count over the shortest path. 
     * @param String the source cryptocurrency name.
     * @returns (other cryptocurrency, the net trades count) pairs. 
     */
    public Map<String, Double> findFlows(String source) {
        Map<String,Double> flows = new HashMap<String,Double>(); // will be returned, contains all routes and tradecounts from source to all possible target currencies, by way of the least cost path 
        HashMap<String,Integer> visited = new HashMap<>(); // keeps track of the vertices that have been visited 
        for (String target : setV) { // sets each vertice to 0, to indicate they haven't been vistied 
            visited.put(target,0); 
        }
        // In order to find each least cost path to the target currencies from the source currency, I used the BFS method 
        ArrayDeque<Graph.Edge> q = new ArrayDeque<>(); // Creates queue for traversal 
        visited.replace(source,1); // Sets the source vertice's value to 1, to indicate it has been visited 
        q.addLast(new Graph.Edge("",source,Double.MAX_VALUE)); // Adds the source to the queue
        while (!q.isEmpty()) { // Will run as long as the queue is not empty 
            Graph.Edge curr = q.removeFirst(); // Gets the next element in the queue 
            if (curr.weight < Double.MAX_VALUE) { // This checks to see whether or not the tradecount is less than the max value, will add to flow 
                flows.put(curr.t,curr.weight); // adds the target currency, and the smallest tradecount found on the least path cost from source to target 
            }
            if (adjacencyList.get(curr.t) != null) { // Traverses adjacency list 
                for (Graph.Edge edge : adjacencyList.get(curr.t)) { // The adjacency list is made up of ArrayLists of edges 
                    if (visited.get(edge.t) == 0) { // if the edge has not yet been visited 
                        q.addLast(new Graph.Edge("",edge.t, Math.min(edge.weight, curr.weight))); // adds the edge to the queue
                    }
                    visited.replace(edge.t,1); // marks the edge as visited 
                }
            }
        } 
        return flows; // returns least path costs from source to all possible targets and their net transaction 
    }

    /** 
     * sortFlows takes the (cryptocurrency, net trades count) pairs and sort them 
     * in the descending order of the net trades count. Returns the sorted list as 
     * List of Strings, where each String is "cryptocurrency: net trades count" format. 
     * @param (cryptocurrency, net trades count) pairs
     * @returns List of cryptocurrency: net trades count Strings. 
     */
    public List<String> sortFlows(Map<String, Double> flows) {
        List<String> list = new ArrayList<>(); 
        LinkedHashMap<String, Double> sorted = new LinkedHashMap<>();
        flows.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEachOrdered(x -> sorted.put(x.getKey(),x.getValue())); // This sorts the hashmap in descending order 
        for (Map.Entry mapElement : sorted.entrySet()) { // traverses sorted LinkedHashmap 
            String key = (String)mapElement.getKey(); // target currency 
            double c = (double)mapElement.getValue(); // net transaction 
            Long value = Math.round(c); // converts double to long 
            list.add(key + ": " + value); // adds target currency and net transaction to list of strings to be returned 
        }
        return list; // returns list of strings, containing the target currencies in descending order of trade count 
    } 
}
