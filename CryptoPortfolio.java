//@Kevin Galvin
//@5 May 2022, Version 1.0
//CS245 Assignment3: CryptoPortfolio

import java.util.HashMap;
import java.util.Map;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.*; 

public class CryptoPortfolio {
    public static void main(String[] args) {
        String filename, source;
        source = args[0]; // Takes in the source cryptocurrency
        filename = args[1]; // Takes in the filename 
        //Do not modify the code in the try/catch block below.
        try {
            HashMap<String, Double> counts = aggregateCounts(filename); // Gets the aggregated counts from the input file 
            Graph g = new Graph(counts); // Graph constructor, creates adjacencyList and vertices list
            Map<String, Double> flows = g.findFlows(source); // Finds the least cost path from the source currency to all other possible, non-zero trade count, currencies 
            System.out.println(g.sortFlows(flows)); // Sorts the least cost paths in descending order 
        } catch (FileNotFoundException e) {
            System.out.println(filename + " does not exist"); // Catches if the input file does not exist 
            System.exit(1);
        }
    }

    /**
     * aggregateCounts opens the csv file using the filename parameter and aggregate
     * trade (transaction) counts over the same cryptocurrency Base Asset and Quote Asset pair.
     * @param String filename represents the csv file name
     * @returns HashMap<String, Double> list of "Base Asset, Quote Asset" and total trade counts.
     * @throws FileNotFoundException if the file does not exist.
     */
    private static HashMap<String, Double> aggregateCounts(String filename) throws FileNotFoundException {
        HashMap<String, Double> counts = new HashMap<>(); // HashMap of the aggregated counts 
        try (BufferedReader r = new BufferedReader(new FileReader(filename))) { // I use BufferedReader to read in the input file
            String str;
            int count = 0; 
            while ((str = r.readLine()) != null) { // This while loop rus as long as there are lines in the file to read
                if (count != 0) { // Accounts for the first line of the file, which contains the headers 
                    String[] components = str.split(",",-1); // Splis the line by the comma delimeter 
                    String baseAsset = components[3]; // takes in the source currency 
                    String quoteAsset = components[5]; // takes in the target currency 
                    Double tradesCount = Double.parseDouble(components[12]); // takes in the tradesCount
                    if (!(baseAsset.equals(quoteAsset))) { // Does not account for transactions where a source bought more source currency 
                        String combined = baseAsset+","+quoteAsset; // Since counts is a HashMap, can only take in one key, therefore I combined the source and target currency 
                        if (!(counts.containsKey(combined))) { // If the key does not exists yet in counts 
                            counts.put(combined,tradesCount);  
                        }
                        else { // If the key already exists in counts 
                            counts.put(combined,counts.get(combined)+tradesCount); 
                        }
                    }
                }
                count = 1;
            }
        }
        catch (IOException e) { // Catches file not found error 
            System.out.println(e);
        }
        return counts;
    }
}
