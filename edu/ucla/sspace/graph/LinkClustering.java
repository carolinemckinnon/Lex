package edu.ucla.sspace.graph;

import edu.ucla.sspace.util.HashIndexer;
import edu.ucla.sspace.util.HashMultiMap;
import edu.ucla.sspace.util.Indexer;
import edu.ucla.sspace.util.LoggerUtil;
import edu.ucla.sspace.util.MultiMap;
import edu.ucla.sspace.util.WorkQueue;
import edu.ucla.sspace.util.primitive.IntIterator;
import edu.ucla.sspace.util.primitive.IntSet;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;























































































public class LinkClustering
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = Logger.getLogger(LinkClustering.class.getName());
  


  private static final String PROPERTY_PREFIX = "edu.ucla.sspace.graph.LinkClustering";
  


  public static final String MINIMUM_EDGE_SIMILARITY_PROPERTY = "edu.ucla.sspace.graph.LinkClustering.minEdgeSimilarity";
  


  private static final WorkQueue WORK_QUEUE = WorkQueue.getWorkQueue();
  





  public LinkClustering() {}
  




  public <E extends Edge> MultiMap<Integer, Integer> cluster(Graph<E> graph, int numClusters, Properties props)
  {
    return singleLink(graph, numClusters);
  }
  
































  public <E extends Edge> MultiMap<Integer, Integer> cluster(Graph<E> graph, Properties props)
  {
    return singleLink(graph);
  }
  































































































































































  private <E extends Edge> MultiMap<Integer, Integer> singleLink(final Graph<E> g)
  {
    final Indexer<Edge> edgeIndexer = new HashIndexer();
    
    for (Edge e : g.edges())
    {

      edgeIndexer.index(new SimpleEdge(e.from(), e.to()));
    }
    
    int numEdges = edgeIndexer.size();
    



    int[] edgeToCluster = new int[numEdges];
    final int[] edgeToMostSim = new int[numEdges];
    final double[] edgeToSimOfMostSim = new double[numEdges];
    


    MultiMap<Integer, Integer> clusterToVertices = new HashMultiMap();
    MultiMap<Integer, Integer> densestSolution = new HashMultiMap();
    


    for (Edge e : g.edges())
    {

      int initialCluster = edgeIndexer.index(
        new SimpleEdge(e.from(), e.to()));
      edgeToCluster[initialCluster] = initialCluster;
      clusterToVertices.put(Integer.valueOf(initialCluster), Integer.valueOf(e.to()));
      clusterToVertices.put(Integer.valueOf(initialCluster), Integer.valueOf(e.from()));
    }
    


    edgeIndexer.lookup(0);
    


    Object taskKey = WORK_QUEUE.registerTaskGroup(g.order());
    IntIterator iter1 = g.vertices().iterator();
    while (iter1.hasNext()) {
      final int v1 = iter1.nextInt();
      WORK_QUEUE.add(taskKey, new Runnable() {
        public void run() {
          LoggerUtil.veryVerbose(LinkClustering.LOGGER, "Computing similarities for vertex %d", new Object[] {
            Integer.valueOf(v1) });
          IntSet neighbors = g.getNeighbors(v1);
          IntIterator it1 = neighbors.iterator();
          while (it1.hasNext()) {
            int v2 = it1.nextInt();
            IntIterator it2 = neighbors.iterator();
            while (it2.hasNext()) {
              int v3 = it2.nextInt();
              if (v2 == v3)
                break;
              double sim = getConnectionSimilarity(
                g, v1, v2, v3);
              
              int e1index = edgeIndexer
                .find(new SimpleEdge(v1, v2));
              int e2index = edgeIndexer
                .find(new SimpleEdge(v1, v3));
              if ((!LinkClustering.$assertionsDisabled) && (e1index < 0)) throw new AssertionError("missing e1");
              if ((!LinkClustering.$assertionsDisabled) && (e2index < 0)) throw new AssertionError("missing e2");
              if ((!LinkClustering.$assertionsDisabled) && (edgeIndexer.lookup(e1index) == null)) throw new AssertionError("e1 is null");
              if ((!LinkClustering.$assertionsDisabled) && (edgeIndexer.lookup(e2index) == null)) { throw new AssertionError("e2 is null");
              }
              

              synchronized ((Edge)edgeIndexer.lookup(e1index)) {
                if (sim > edgeToSimOfMostSim[e1index]) {
                  edgeToSimOfMostSim[e1index] = sim;
                  edgeToMostSim[e1index] = e2index;
                }
              }
              


              synchronized ((Edge)edgeIndexer.lookup(e2index)) {
                if (sim > edgeToSimOfMostSim[e2index]) {
                  edgeToSimOfMostSim[e2index] = sim;
                  edgeToMostSim[e2index] = e1index;
                }
              }
            }
          }
        }
      });
    }
    WORK_QUEUE.await(taskKey);
    



    int[] clusterToNumEdges = new int[numEdges];
    Arrays.fill(clusterToNumEdges, 1);
    


    double highestDensity = 0.0D;
    int mergeStepsWithHighestDensity = 0;
    
    LoggerUtil.verbose(LOGGER, "Clustering edges", new Object[0]);
    

    int mergeIter = 0;
    while (clusterToVertices.size() > 1) {
      if (mergeIter % 1000 == 0) {
        LoggerUtil.verbose(LOGGER, "Computing dendrogram merge %d/%d", new Object[] {
          Integer.valueOf(mergeIter + 1), Integer.valueOf(numEdges - 1) });
      }
      


      int edge1index = -1;
      int edge2index = -1;
      double highestSim = -1.0D;
      for (int i = 0; i < edgeToSimOfMostSim.length; i++)
      {

        if (edgeToSimOfMostSim[i] > highestSim) {
          int c1 = edgeToCluster[i];
          int mostSim = edgeToMostSim[i];
          int c2 = edgeToCluster[mostSim];
          if (c1 != c2) {
            highestSim = edgeToSimOfMostSim[i];
            edge1index = i;
            edge2index = edgeToMostSim[i];
          }
        }
      }
      
      int cluster1index = -1;
      int cluster2index = -1;
      


      if (edge1index == -1) {
        Iterator<Integer> it = clusterToVertices.keySet().iterator();
        cluster1index = ((Integer)it.next()).intValue();
        cluster2index = ((Integer)it.next()).intValue();
      }
      else
      {
        cluster1index = edgeToCluster[edge1index];
        cluster2index = edgeToCluster[edge2index];
      }
      








      if (cluster1index == cluster2index) {
        edgeToSimOfMostSim[edge1index] = -2.0D;

      }
      else
      {

        mergeIter++;
        





        Set<Integer> verticesInSmaller = clusterToVertices.get(Integer.valueOf(cluster2index));
        



        clusterToVertices.putMany(Integer.valueOf(cluster1index), verticesInSmaller);
        clusterToVertices.remove(Integer.valueOf(cluster2index));
        clusterToNumEdges[cluster1index] += clusterToNumEdges[cluster2index];
        


        if (clusterToVertices.size() == 1) {
          break;
        }
        



        edgeToSimOfMostSim[edge1index] = -3.0D;
        edgeToSimOfMostSim[edge2index] = -4.0D;
        




        int mostSimEdgeToCurCluster = -1;
        highestSim = -5.0D;
        Edge e1 = (Edge)edgeIndexer.lookup(edge1index);
        Edge e2 = (Edge)edgeIndexer.lookup(edge2index);
        

        for (int i = 0; i < numEdges; i++) {
          int cId = edgeToCluster[i];
          if (cId != cluster1index)
          {







            if (cId == cluster2index) {
              edgeToCluster[i] = cluster1index;



            }
            else
            {



              Edge e3 = (Edge)edgeIndexer.lookup(i);
              assert (e1 != null) : ("e1 is null, edge1index: " + edge1index);
              assert (e2 != null) : ("e2 is null, edge2index: " + edge2index);
              assert (e3 != null) : ("e3 is null, edge3index: " + i + ", debug:" + debug(edgeIndexer, numEdges));
              
              double simToE1 = getConnectionSimilarity(g, e1, e3);
              simToE2 = getConnectionSimilarity(g, e2, e3);
              



              double sim = Math.max(simToE1, simToE2);
              if (sim > highestSim) {
                highestSim = sim;
                mostSimEdgeToCurCluster = i;
              }
            }
          }
        }
        edgeToMostSim[edge1index] = mostSimEdgeToCurCluster;
        edgeToSimOfMostSim[edge1index] = highestSim;
        

        double partitionDensitySum = 0.0D;
        int edgeSum = 0;int nodeSum = 0;
        
        double simToE2 = clusterToVertices.asMap().entrySet().iterator();
        while (simToE2.hasNext()) {
          Map.Entry<Integer, Set<Integer>> cluster = (Map.Entry)simToE2.next();
          int numNodesInCluster = ((Set)cluster.getValue()).size();
          int numEdgesInCluster = clusterToNumEdges[((Integer)cluster.getKey()).intValue()];
          edgeSum += numEdgesInCluster;
          






          partitionDensitySum = partitionDensitySum + computeDensity(numNodesInCluster, numEdgesInCluster);
        }
        
        assert (edgeSum == numEdges) : "Adding edges somewhere";
        



        double partitionDensity = 
          2.0D / numEdges * partitionDensitySum;
        




        LoggerUtil.veryVerbose(LOGGER, "Merge %d/%d had density %f", new Object[] {
          Integer.valueOf(mergeIter), Integer.valueOf(numEdges - 1), Double.valueOf(partitionDensity) });
        if (mergeIter % 1000 == 0) {
          LoggerUtil.verbose(LOGGER, "Merge %d/%d had density %f", new Object[] {
            Integer.valueOf(mergeIter), Integer.valueOf(numEdges - 1), Double.valueOf(partitionDensity) });
        }
        if (partitionDensity > highestDensity) {
          highestDensity = partitionDensity;
          mergeStepsWithHighestDensity = mergeIter;
          densestSolution.clear();
          densestSolution.putAll(clusterToVertices);
        }
      }
    }
    


    LoggerUtil.verbose(LOGGER, "Merge %d had the highest density: %f", new Object[] {
      Integer.valueOf(mergeStepsWithHighestDensity), Double.valueOf(highestDensity) });
    
    return densestSolution;
  }
  
  private static String debug(Indexer<Edge> indexer, int numEdges) {
    for (int i = 0; i < numEdges; i++)
      System.out.println(i + ": " + indexer.lookup(i));
    return "";
  }
  










  private <E extends Edge> MultiMap<Integer, Integer> singleLink(final Graph<E> g, int numClusters)
  {
    int numEdges = g.size();
    if ((numClusters < 1) || (numClusters > numEdges)) {
      throw new IllegalArgumentException(
        "Invalid range for number of clusters: " + numClusters);
    }
    

    final Indexer<Edge> edgeIndexer = new HashIndexer();
    



    int[] edgeToCluster = new int[numEdges];
    final int[] edgeToMostSim = new int[numEdges];
    final double[] edgeToSimOfMostSim = new double[numEdges];
    

    MultiMap<Integer, Integer> clusterToVertices = new HashMultiMap();
    


    for (Edge e : g.edges()) {
      int initialCluster = edgeIndexer.index(
        new SimpleEdge(e.from(), e.to()));
      edgeToCluster[initialCluster] = initialCluster;
      clusterToVertices.put(Integer.valueOf(initialCluster), Integer.valueOf(e.to()));
      clusterToVertices.put(Integer.valueOf(initialCluster), Integer.valueOf(e.from()));
    }
    


    edgeIndexer.lookup(0);
    


    Object taskKey = WORK_QUEUE.registerTaskGroup(g.order());
    IntIterator iter1 = g.vertices().iterator();
    while (iter1.hasNext()) {
      final int v1 = iter1.nextInt();
      WORK_QUEUE.add(taskKey, new Runnable() {
        public void run() {
          LoggerUtil.veryVerbose(LinkClustering.LOGGER, "Computing similarities for vertex %d", new Object[] {
            Integer.valueOf(v1) });
          IntSet neighbors = g.getNeighbors(v1);
          IntIterator it1 = neighbors.iterator();
          while (it1.hasNext()) {
            int v2 = it1.nextInt();
            IntIterator it2 = neighbors.iterator();
            while (it2.hasNext()) {
              int v3 = it2.nextInt();
              if (v2 == v3)
                break;
              double sim = getConnectionSimilarity(
                g, v1, v2, v3);
              
              int e1index = edgeIndexer
                .index(new SimpleEdge(v1, v2));
              int e2index = edgeIndexer
                .index(new SimpleEdge(v1, v3));
              


              synchronized ((Edge)edgeIndexer.lookup(e1index)) {
                if (sim > edgeToSimOfMostSim[e1index]) {
                  edgeToSimOfMostSim[e1index] = sim;
                  edgeToMostSim[e1index] = e2index;
                }
              }
              


              synchronized ((Edge)edgeIndexer.lookup(e2index)) {
                if (sim > edgeToSimOfMostSim[e2index]) {
                  edgeToSimOfMostSim[e2index] = sim;
                  edgeToMostSim[e2index] = e1index;
                }
              }
            }
          }
        }
      });
    }
    WORK_QUEUE.await(taskKey);
    


    int[] clusterToNumEdges = new int[numEdges];
    Arrays.fill(clusterToNumEdges, 1);
    
    LoggerUtil.verbose(LOGGER, "Clustering edges", new Object[0]);
    

    int mergeIter = 0;
    while (clusterToVertices.size() > numClusters) {
      if (LOGGER.isLoggable(Level.FINE)) {
        LOGGER.log(Level.FINE, "Computing dendrogram merge {0}/{1}", 
          new Object[] { Integer.valueOf(mergeIter + 1), Integer.valueOf(numEdges - 1) });
      }
      
      int edge1index = -1;
      int edge2index = -1;
      double highestSim = -1.0D;
      for (int i = 0; i < edgeToSimOfMostSim.length; i++) {
        if (edgeToSimOfMostSim[i] > highestSim) {
          int c1 = edgeToCluster[i];
          int mostSim = edgeToMostSim[i];
          int c2 = edgeToCluster[mostSim];
          if (c1 != c2) {
            highestSim = edgeToSimOfMostSim[i];
            edge1index = i;
            edge2index = edgeToMostSim[i];
          }
        }
      }
      
      int cluster1index = -1;
      int cluster2index = -1;
      


      if (edge1index == -1) {
        Iterator<Integer> it = clusterToVertices.keySet().iterator();
        cluster1index = ((Integer)it.next()).intValue();
        cluster2index = ((Integer)it.next()).intValue();
      }
      else
      {
        cluster1index = edgeToCluster[edge1index];
        cluster2index = edgeToCluster[edge2index];
      }
      assert (cluster1index != cluster2index) : "merging same cluster";
      



      Set<Integer> verticesInSmaller = clusterToVertices.get(Integer.valueOf(cluster2index));
      clusterToVertices.putMany(Integer.valueOf(cluster1index), verticesInSmaller);
      clusterToVertices.remove(Integer.valueOf(cluster2index));
      clusterToNumEdges[cluster1index] += clusterToNumEdges[cluster2index];
      


      if (mergeIter == numEdges - 2) {
        break;
      }
      



      edgeToSimOfMostSim[edge1index] = -2.0D;
      edgeToSimOfMostSim[edge2index] = -3.0D;
      




      int mostSimEdgeToCurCluster = -1;
      highestSim = -4.0D;
      Edge e1 = (Edge)edgeIndexer.lookup(edge1index);
      Edge e2 = (Edge)edgeIndexer.lookup(edge2index);
      
      for (int i = 0; i < numEdges; i++) {
        int cId = edgeToCluster[i];
        if (cId != cluster1index)
        {







          if (cId == cluster2index) {
            edgeToCluster[i] = cluster1index;




          }
          else
          {




            Edge e3 = (Edge)edgeIndexer.lookup(i);
            double simToE1 = getConnectionSimilarity(g, e1, e3);
            double simToE2 = getConnectionSimilarity(g, e2, e3);
            
            double sim = Math.max(simToE1, simToE2);
            if (sim > highestSim) {
              highestSim = sim;
              mostSimEdgeToCurCluster = i;
            }
          } }
      }
      edgeToMostSim[edge1index] = mostSimEdgeToCurCluster;
      edgeToSimOfMostSim[edge1index] = highestSim;
    }
    
    return clusterToVertices;
  }
  




  protected double computeDensity(int numNodes, int numEdges)
  {
    if (numNodes == 2) {
      return 0.0D;
    }
    return numEdges * (numEdges - numNodes + 1.0D) / (
      numNodes - 1.0D) / (numNodes - 2.0D);
  }
  
















  private <E extends Edge> PriorityQueue<EdgePair> calcuateEdgeSimQueue(final Graph<E> graph, final double minSimilarity)
  {
    int numVertices = graph.order();
    int numEdges = graph.size();
    double avgDegree = numEdges / numVertices;
    final int numComparisons = (int)(avgDegree * (avgDegree + 1.0D) / 2.0D * numVertices);
    


    PriorityQueue<EdgePair> pq = 
      new PriorityQueue(numComparisons);
    Object key = WORK_QUEUE.registerTaskGroup(graph.order());
    
    IntIterator iter1 = graph.vertices().iterator();
    while (iter1.hasNext()) {
      final int v1 = iter1.nextInt();
      WORK_QUEUE.add(key, new Runnable() {
        public void run() {
          LoggerUtil.veryVerbose(LinkClustering.LOGGER, "Computing similarities for vertex %d", new Object[] {
            Integer.valueOf(v1) });
          
          IntSet neighbors = graph.getNeighbors(v1);
          



          PriorityQueue<LinkClustering.EdgePair> localQ = 
            new PriorityQueue(neighbors.size());
          IntIterator it1 = neighbors.iterator();
          
          while (it1.hasNext())
          {
            int v2 = it1.nextInt();
            
            IntIterator it2 = neighbors.iterator();
            
            while (it2.hasNext()) {
              int v3 = it2.nextInt();
              if (v2 == v3) {
                break;
              }
              

              float sim = 
                (float)getConnectionSimilarity(graph, v1, v2, v3);
              




              if (sim > minSimilarity)
              {
                localQ.add(new LinkClustering.EdgePair(-sim, v1, v2, v3)); }
            }
          }
          synchronized (numComparisons) {
            numComparisons.addAll(localQ);
            int comps = numComparisons.size();
            LoggerUtil.veryVerbose(LinkClustering.LOGGER, "%d/%d comparisons completed (%f)", new Object[] {
              Integer.valueOf(comps), Integer.valueOf(val$numComparisons), 
              Double.valueOf(comps / val$numComparisons) });
          }
        }
      });
    }
    WORK_QUEUE.await(key);
    return pq;
  }
  







  private <E extends Edge> double getConnectionSimilarity(Graph<E> graph, Edge e1, Edge e2)
  {
    int e1to = e1.to();
    int e1from = e1.from();
    int e2to = e2.to();
    int e2from = e2.from();
    if (e1to == e2to)
      return getConnectionSimilarity(graph, e1to, e1from, e2from);
    if (e1to == e2from)
      return getConnectionSimilarity(graph, e1to, e1from, e2to);
    if (e1from == e2to)
      return getConnectionSimilarity(graph, e1from, e1to, e2from);
    if (e1from == e2from) {
      return getConnectionSimilarity(graph, e1from, e1to, e2to);
    }
    return 0.0D;
  }
  






















  protected <E extends Edge> double getConnectionSimilarity(Graph<E> graph, int keystone, int impost1, int impost2)
  {
    IntSet n1 = graph.getNeighbors(impost1);
    IntSet n2 = graph.getNeighbors(impost2);
    int n1size = n1.size();
    int n2size = n2.size();
    

    if (n1size > n2size) {
      IntSet tmp = n2;
      n2 = n1;
      n1 = tmp;
      int t = impost1;
      impost1 = impost2;
      impost2 = t;
    }
    
    int inCommon = 0;
    IntIterator it = n1.iterator();
    while (it.hasNext()) {
      int v = it.nextInt();
      if (n2.contains(v)) {
        inCommon++;
      }
    }
    if (n2.contains(impost1))
      inCommon++;
    if (n1.contains(impost2)) {
      inCommon++;
    }
    

    return inCommon / (n1size + n2size + 2 - inCommon);
  }
  



  private static class EdgePair
    implements Comparable<EdgePair>
  {
    int keystone;
    

    int impost1;
    

    int impost2;
    

    float negSim;
    


    public EdgePair(float negSim, Edge e1, Edge e2)
    {
      throw new Error();
    }
    
    public EdgePair(float negSim, int keystone, int impost1, int impost2) {
      this.keystone = keystone;
      this.impost1 = impost1;
      this.impost2 = impost2;
      this.negSim = negSim;
    }
    
    public int compareTo(EdgePair ep)
    {
      return Float.compare(negSim, negSim);
    }
    
    public Edge edge1() { return new SimpleEdge(keystone, impost1); }
    public Edge edge2() { return new SimpleEdge(keystone, impost2); }
    
    public boolean equals(Object o) {
      if ((o instanceof EdgePair)) {
        EdgePair ep = (EdgePair)o;
        return (keystone == keystone) && 
          (impost1 == impost1) && 
          (impost2 == impost2) && 
          (negSim == negSim);
      }
      
      return false;
    }
    
    public int hashCode()
    {
      return impost1 ^ impost2 ^ keystone;
    }
    
    public String toString() {
      int i = Math.min(impost1, impost2);
      int j = Math.max(impost1, impost2);
      return i + "-" + keystone + "-" + j + ": " + -negSim;
    }
  }
}
