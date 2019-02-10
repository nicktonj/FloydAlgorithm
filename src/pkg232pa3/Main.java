/*
 * Authors: Alex Rueb, Nicolas Tonjum, Connor Lowe
 * Date: Spring 2018
 * Overview: Programming Assignment 3 -> Graph Algorithms
 *      Using Prim's, Kruskal's, and Floyd-Warshall's algorithms
 */
package pkg232pa3;

import java.util.*;
import java.lang.*;
import java.io.*;

public class Main {

    private static int[][] matrix; //the adjacency matrix -- DO NOT MODIFY
    private static final String inputFile = "input/input.txt";
    private static int INF = 999999999;
    public static int edges; //The amount of edges
    public static int vertices; //the amout of vertices or 'nodes'

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            Scanner sc = new Scanner(new File(inputFile));
            int matrixPos = 0;
            while (sc.hasNext()) {
                String s = sc.nextLine();
                String[] thisOne = s.split(",");
                vertices = thisOne.length;
                if (matrix == null) {
                    matrix = new int[vertices][vertices];
                }
                int[] thisTwo = new int[vertices];
                for (int i = 0; i < vertices; i++) {
                    if(thisOne[i].equals("∞")){                 //and this
                    	thisTwo[i] = INF;
                	} else{
                    	thisTwo[i] = Integer.parseInt(thisOne[i]);
                	}
                    if (thisTwo[i] != 0) {
                        edges++;
                    }
                }
                matrix[matrixPos] = thisTwo;
                matrixPos++;
            }
            prim(matrix);
            kruskal();
            floyd(matrix);
        } catch (IOException x) {
            System.out.println("File not found");
        }
    }

    //Prim's algorithm -- Connor Lowe
    public static void prim(int matrix[][]) {
        System.out.println("Prim's Algorith-------------");
        System.out.println("");
        int nodes = matrix.length; //Number of vertices in graph
        int inf = Integer.MAX_VALUE; //Easy wasy to represent infinity 

        //Array for the MST 
        int tree[] = new int[nodes];

        //Set to help choose minimum weights 
        int weight[] = new int[nodes];
        //Set for all nodes not in the tree
        boolean outside[] = new boolean[nodes];

        //Set all the keys to inf and sets them to false to denote 
        //that they're not in the tree yet
        for (int i = 0; i < nodes; i++) {
            outside[i] = false;
            weight[i] = inf;
        }

        //Makes the weight 0 so it gets selected first 
        weight[0] = 0;

        for (int j = 0; j < nodes - 1; j++) {
            //Calls method below to put lowest cost into a variable 
            int lowest = lowestCost(outside, weight);

            //Put the selected node into the "outside" set 
            outside[lowest] = true;

            for (int k = 0; k < nodes; k++) {
                if (matrix[lowest][k] < weight[k] && outside[k] == false && matrix[lowest][k] != 0) {
                    tree[k] = lowest;
                    weight[k] = matrix[lowest][k];
                }
            }
        }

        //Prints the tree 
        int totWeight = 0;
        for (int i = 1; i < matrix.length; i++) {
            char s =(char) (tree[i] + 66);
            char d =(char) (i + 66);
            System.out.print(s + "" + d + " ");
        }
        System.out.println("");
    }

    //Kruskals algorithm - Alex Rueb
    public static void kruskal() {
        System.out.println("");
        System.out.println("Kruskal's Algorith-------------");
        System.out.println("");
        Graph graph = new Graph(); //creates the graph
        int edgePos = 0; //variable for which edge we're at

        //loop through every space in the matrix
        for (int i = 0; i < Main.vertices; i++) {
            for (int j = 0; j < Main.vertices; j++) {
                //if its a zero, there is no edge connecting the corresponding vertices
                if (matrix[i][j] == 0) {
                } //if it's not a zero, there is an edge. 
                else {
                    //put the egge weight, source vertice, and destination vertices in the graph
                    graph.allEdges[edgePos].weight = matrix[i][j];
                    graph.allEdges[edgePos].source = i + 1;
                    graph.allEdges[edgePos].dest = j + 1;

                    edgePos++;
                }
            }
        }
        //sort the graph.edge array for the MST
        Arrays.sort(graph.allEdges);

        //generate the MST
        graph.findMST();
    }

    //Function to find the node with the smallest key 
    public static int lowestCost(boolean outside[], int weight[]) {
        int inf = Integer.MAX_VALUE;
        int smallest = -1;
        for (int i = 0; i < matrix.length; i++) {
            if (weight[i] < inf && outside[i] == false) {
                inf = weight[i];
                smallest = i;
            }
        }
        return smallest;
    }
    
    public static void floyd(int graph[][])
    {
        System.out.println("");
        System.out.println("Floyd's Algorithm------------- ");  
        int table[][] = new int[vertices][vertices];           //he wants all values to be infinity????
        int i, j, k;
        for (i = 0; i < vertices; i++)                      // start copy graph to the new table
        {
            for (j = 0; j < vertices; j++)
            {
                table[i][j] = graph[i][j];
            }
        }                                                   // end copy graph to the new table
        for (k = 0; k < vertices; k++)                              // starting loop through the table
        {
            for (i = 0; i < vertices; i++)
            {
                for (j = 0; j < vertices; j++)
                {
                    if (table[i][k] + table[k][j] < table[i][j])        // check if distance is smaller    
                    {
                        table[i][j] = table[i][k] + table[k][j];        // change it if distance is smaller
                        System.out.println("");                         // start printing method
                        System.out.println("Printing:");
                        for (int a=0; a<vertices; ++a)
                        {
                            for (int b=0; b<vertices; ++b)
                            {
                                if (table[a][b]==INF)
                                {
                                    System.out.print("INF ");
                                }
                                else
                                {
                                    System.out.print(table[a][b]+"   ");
                                }
                            }
                            System.out.println();
                        }
                        System.out.println("");                         // end printing method
                    }
                }
            }                                                       // ending loop through the table
        }
    }
//Graph class and methods heavily inspired by:https://www.geeksforgeeks.org/greedy-algorithms-set-2-kruskals-minimum-spanning-tree-mst/

    static class Graph {

        int vertices, totalEdges; //int values for # of vertices and edges
        Edge allEdges[]; //the array of the sorted edges

        Graph() {
            vertices = Main.vertices;
            totalEdges = Main.edges;
            allEdges = new Edge[totalEdges];
            for (int i = 0; i < totalEdges; ++i) {
                allEdges[i] = new Edge();
            }
        }

        //Class to represent the edges between vertices
        class Edge implements Comparable<Edge> {

            int weight, source, dest;
            Edge[] connected = new Edge[vertices - 1];

            @Override //used for sorting the edge array
            public int compareTo(Edge o) {
                return weight - o.weight;
            }
        }

        //class to represent the set of edges connected in a subset
        class subset {

            int parent, rank;
        }

        //used to find if the given subset is in the subsetList already
        //this is used in determining if an edge needs to be discarded
        int find(subset subsetList[], int i) {
            if (subsetList[i].parent == i) {
                //do nothing
            } else {
                subsetList[i].parent = find(subsetList, subsetList[i].parent);
            }
            return subsetList[i].parent;
        }

        //used to combine two subsets
        //this makes sure that when an edge is added to the tree, 
        //all of its connected vertices are accounted for
        void Combine(subset subsetList[], int x, int y) {
            int xroot = find(subsetList, x);
            int yroot = find(subsetList, y);

            // Make higher rank tree the parent of the smaller rank
            if (subsetList[xroot].rank < subsetList[yroot].rank) {
                subsetList[xroot].parent = yroot;
            } else if (subsetList[xroot].rank > subsetList[yroot].rank) {
                subsetList[yroot].parent = xroot;
            } // If ranks are same, then make one as root and make it's rank 1 higher
            else {
                subsetList[yroot].parent = xroot;
                subsetList[xroot].rank++;
            }
        }

        //the method that loops through the sorted list and finds the MST
        void findMST() {
            Edge result[] = new Edge[vertices];  // This will store the MST
            int e = 0;
            int i = 0;
            for (i = 0; i < vertices; ++i) {
                result[i] = new Edge();
            }

            // Create an array of subsets, one for each edge
            subset subsetList[] = new subset[totalEdges];
            for (i = 0; i < totalEdges; ++i) {
                subsetList[i] = new subset();
            }

            // Create subsetList with single elements equal to # of vertices
            for (int v = 0; v < vertices; v++) {
                subsetList[v].parent = v;
                subsetList[v].rank = 0;
            }

            i = 0;

            // there will be one less edge than vertices
            while (e < vertices - 1) {
                //the currentEdge will be the smallest edge not sorted already
                Edge currentEdge = allEdges[i++];

                //finds all the vertices connected to the source vertice
                int x = find(subsetList, currentEdge.source);
                //finds all the vertices connected to the destination vertice
                int y = find(subsetList, currentEdge.dest);

                //if these two subsets are not the same, then adding it will not cause
                //a cycle. It is safe to merge them, we add the current edge to the
                //results array and add the two subsetList together as they are one larger
                //subset now
                if (x != y) {
                    result[e++] = currentEdge;
                    Combine(subsetList, x, y);
                }
                // if they are the same, discard the currentEdge
            }

            // print the built MST
            int resTotal = 0;
            for (i = 0; i < e; ++i) {
                char s = (char) (result[i].source + 65);
                char d = (char) (result[i].dest + 65);
                System.out.print((s) +"" + (d) + " ");
                resTotal += result[i].weight;
            }
            System.out.println("\n\nThe total travel cost is: " + resTotal);

        }
    }
}