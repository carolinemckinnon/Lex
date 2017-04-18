package edu.ucla.sspace.graph;

import edu.ucla.sspace.util.Counter;
import edu.ucla.sspace.util.HashMultiMap;
import edu.ucla.sspace.util.MultiMap;
import edu.ucla.sspace.util.ObjectCounter;
import edu.ucla.sspace.util.primitive.IntIterator;
import edu.ucla.sspace.util.primitive.IntSet;
import edu.ucla.sspace.util.primitive.PrimitiveCollections;
import gnu.trove.iterator.TIntDoubleIterator;
import gnu.trove.map.TIntDoubleMap;
import gnu.trove.map.hash.TIntDoubleHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import java.io.Serializable;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;





















































public class ChineseWhispersClustering
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = Logger.getLogger(ChineseWhispersClustering.class.getName());
  

  private static final String PROPERTY_PREFIX = "edu.ucla.sspace.graph.LinkClustering";
  
  private static final int DEFAULT_MAX_ITERATIONS = 100;
  
  private static final double DEFAULT_RANDOM_ASSIGNMENT_PROB = 0.0D;
  
  private static final Random RANDOM = new Random();
  

  public ChineseWhispersClustering() {}
  

  public <E extends Edge> MultiMap<Integer, Integer> cluster(Graph<E> graph)
  {
    return cluster(graph, 100, 
      0.0D);
  }
  





  public <E extends Edge> MultiMap<Integer, Integer> cluster(Graph<E> graph, int maxIterations)
  {
    return cluster(graph, maxIterations, 0.0D);
  }
  







  public <E extends Edge> MultiMap<Integer, Integer> cluster(Graph<E> graph, int maxIterations, double randomAssignmentProb)
  {
    if (!areVerticesContiguous(graph)) {
      throw new IllegalArgumentException(
        "Graph vertex indices must be contiguous");
    }
    int[] vertexAssignments = new int[graph.order()];
    int[] vertices = new int[graph.order()];
    for (int i = 0; i < vertices.length; i++) {
      vertices[i] = i;
      vertexAssignments[i] = i;
    }
    
    boolean assignmentsChanged = true;
    double mutationRate = randomAssignmentProb;
    for (int iter = 0; (iter < maxIterations) && (assignmentsChanged); iter++) {
      assignmentsChanged = false;
      

      PrimitiveCollections.shuffle(vertices);
      
      for (int i = 0; i < vertices.length; i++) {
        int vertex = vertices[i];
        

        if (RANDOM.nextDouble() < mutationRate) {
          int randomClass = RANDOM.nextInt(vertices.length);
          int oldClass = vertexAssignments[vertex];
          if (oldClass != randomClass) {
            vertexAssignments[vertex] = randomClass;
            assignmentsChanged = true;
          }
          

        }
        else
        {
          int maxClass = (graph instanceof WeightedGraph) ? 
            getMaxClassWeighted(vertex, vertexAssignments, 
            (WeightedGraph)graph) : 
            getMaxClass(vertex, vertexAssignments, graph);
          int oldClass = vertexAssignments[vertex];
          if (oldClass != maxClass) {
            vertexAssignments[vertex] = maxClass;
            assignmentsChanged = true;
          }
        }
      }
    }
    

    MultiMap<Integer, Integer> toReturn = 
      new HashMultiMap();
    for (int i = 0; i < vertices.length; i++)
      toReturn.put(Integer.valueOf(vertexAssignments[i]), Integer.valueOf(i));
    return toReturn;
  }
  
  static <E extends Edge> boolean areVerticesContiguous(Graph<E> g) {
    return true;
  }
  
  static int getMaxClass(int v, int[] vertexAssignments, Graph g) {
    IntSet neighbors = g.getNeighbors(v);
    IntIterator iter = neighbors.iterator();
    Counter<Integer> classes = new ObjectCounter();
    classes.count(Integer.valueOf(vertexAssignments[v]));
    while (iter.hasNext()) {
      int n = iter.nextInt();
      classes.count(Integer.valueOf(vertexAssignments[n]));
    }
    
    TIntSet ties = new TIntHashSet();
    int max = 0;
    for (Map.Entry<Integer, Integer> e : classes) {
      int clazz = ((Integer)e.getKey()).intValue();
      int count = ((Integer)e.getValue()).intValue();
      if (count > max) {
        ties.clear();
        max = count;
      }
      if (count == max) {
        ties.add(clazz);
      }
    }
    int[] options = ties.toArray(new int[ties.size()]);
    return options.length == 1 ? 
      options[0] : 
      options[RANDOM.nextInt(options.length)];
  }
  

  static <E extends WeightedEdge> int getMaxClassWeighted(int v, int[] vertexAssignments, WeightedGraph<E> g)
  {
    Set<E> edges = g.getAdjacencyList(v);
    TIntDoubleMap classSums = new TIntDoubleHashMap();
    for (WeightedEdge e : edges) {
      int n = e.to() == v ? e.from() : e.to();
      int nClass = vertexAssignments[n];
      double weight = e.weight();
      if (classSums.containsKey(nClass)) {
        double curWeight = classSums.get(nClass);
        classSums.put(nClass, weight + curWeight);
      }
      else {
        classSums.put(nClass, weight);
      }
    }
    
    double maxSum = -1.0D;
    TIntSet ties = new TIntHashSet();
    TIntDoubleIterator iter = classSums.iterator();
    while (iter.hasNext()) {
      iter.advance();
      double weight = iter.value();
      if (weight > maxSum) {
        maxSum = weight;
        ties.clear();
      }
      if (weight == maxSum) {
        ties.add(iter.key());
      }
    }
    

    int[] options = ties.toArray();
    return options.length == 1 ? 
      options[0] : 
      options[RANDOM.nextInt(options.length)];
  }
}
