# assignment3Galvin
Q1. How does your program find the shortest paths from the source cryptocurrency to others? Explain your algorithm. In order to find the shortest paths from the source cryptocurrency to others, I used the breadth-first search (BFS) algorithm. This algorithm will find the shortest path from the source to target currency by traveling along edges with the minimum possible weight. In this case, an edge's weight is represented by the total trade count from a source currency to a target currency. Using a queue and the adjacency list, which represents the graph, I traversed from the source to each target currency by traveling along this least past cost using the BFS algorithm. Specifically, I used a HashMap which indicated whether a vertice had been visited, and cycled through the ArrayLists of edges contained in the adjacencey list, and then added them to the queue if they had not already been visited. I then remove the next element from the queue, and check whether or not its weight is less than the current minimum value. If it is, I then add it to the final HashMap that will be returned at the end of the findFlows function. Once the target currency is found, the minimum trade count that was found along the entire path is returned as the "net transaction" along with the name of the target currency.

Q2. What is the time and space complexity of the shortest-path algorithm in your answer above? Explain your answer. The time complexity of the BFS algorithm is O(|V|+|E|) or the number of total vertices plus the total number of edges. This is because in the worst possible case, all edges and vertices must be travered and checked in order to find the least cost path from the source currency to the target currency. Using a queue, I have to continually dequeue vertices, and then enqueue all of the neighbors of the dequeued vertice that have not been visited, and then mark of all of the neighbors of the vertice as visited in order to prevent them from enqueueing more than once, while the queue is not empty. The space complexity is O(|V|) for the BFS algorithm used, because in the worst case the queue would have to hold all vertices in order to find the least path cost from source to target.

Q3. How does your program sort the (cryptocurrency, net trades count) pairs? Explain your algorithm. For sort flows, I did not create my own sorting algorithm. Instead, I used this line "flows.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEachOrdered(x -> sorted.put(x.getKey(),x.getValue()));" This line sorted the HashMap flows in descending order of net transaction. This method utilizes the java Stream APIs and the static comparingByValue method. The HashMap flows is sorted into the LinkedHashMap labeled "sorted." This method returns a comparator that compares the Map.entry in natural order based on the net transaction of each item in flows. After sorting, the key and value is put into "sorted," which is indicated by the line "sorted.put(x.getKey(),x.getValue())." After sorting the HashMap flows in descending order of trade count, I then cycle through the sorted HashMap and add the corresponding key and net transaction to the list of strings that is returned at the end of sortFlows.

Q4. What is the time and space complexity of the sorting algorithm in your answer above? Explain your answer. According to the method I used to sort the HashMap flows in order of descending value, the time complexity is O(nlogn). This is because the sorting algorithm sorts the HashMap of keys and values and then stores them into a LinkedHashMap, so it actually becomes O(nlogn + n), which is just O(nlogn). Direct sorting of the Hashmap is not possible, therefore comparator must be used, the comparison will define the map object or value, which defines the order. Everytime a value is sorted it must be inserted, and then these values must be maintainted and added to the LinkedHashMap. Therefore, the time complexity is O(nlogn). This algorithm's space complexity is O(n) as each key/value pair in the hashmap is added to the list of strings to be returned at the end of sortFlows.
