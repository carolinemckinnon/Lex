package edu.ucla.sspace.clustering;

import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.common.Similarity.SimType;
import edu.ucla.sspace.matrix.Matrices;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.Matrix.Type;
import edu.ucla.sspace.matrix.OnDiskMatrix;
import edu.ucla.sspace.util.WorkQueue;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Level;
import java.util.logging.Logger;





















































































































public class HierarchicalAgglomerativeClustering
  implements Clustering
{
  public static final String PROPERTY_PREFIX = "edu.ucla.sspace.clustering.HierarchicalAgglomerativeClustering";
  public static final String MIN_CLUSTER_SIMILARITY_PROPERTY = "edu.ucla.sspace.clustering.HierarchicalAgglomerativeClustering.clusterThreshold";
  public static final String CLUSTER_LINKAGE_PROPERTY = "edu.ucla.sspace.clustering.HierarchicalAgglomerativeClustering.clusterLinkage";
  public static final String SIMILARITY_FUNCTION_PROPERTY = "edu.ucla.sspace.clustering.HierarchicalAgglomerativeClustering.simFunc";
  public static final String NUM_CLUSTERS_PROPERTY = "edu.ucla.sspace.clustering.HierarchicalAgglomerativeClustering.numClusters";
  private static final String DEFAULT_MIN_CLUSTER_SIMILARITY_PROPERTY = "-1";
  public static final String DEFAULT_CLUSTER_LINKAGE_PROPERTY = "COMPLETE_LINKAGE";
  private static final String DEFAULT_SIMILARITY_FUNCTION_PROPERTY = "COSINE";
  
  public static enum ClusterLinkage
  {
    SINGLE_LINKAGE, 
    




    COMPLETE_LINKAGE, 
    




    MEAN_LINKAGE, 
    





    MEDIAN_LINKAGE;
  }
  























































  private static final Logger LOGGER = Logger.getLogger(HierarchicalAgglomerativeClustering.class.getName());
  

  private final WorkQueue workQueue;
  

  public HierarchicalAgglomerativeClustering()
  {
    workQueue = WorkQueue.getWorkQueue();
  }
  


  public Assignments cluster(Matrix matrix, Properties props)
  {
    String minSimProp = props.getProperty("edu.ucla.sspace.clustering.HierarchicalAgglomerativeClustering.clusterThreshold");
    String numClustProp = props.getProperty("edu.ucla.sspace.clustering.HierarchicalAgglomerativeClustering.numClusters");
    String simFuncProp = props.getProperty("edu.ucla.sspace.clustering.HierarchicalAgglomerativeClustering.simFunc");
    String linkageProp = props.getProperty("edu.ucla.sspace.clustering.HierarchicalAgglomerativeClustering.clusterLinkage");
    
    Similarity.SimType simFunc = Similarity.SimType.valueOf(simFuncProp == null ? 
      "COSINE" : 
      simFuncProp);
    ClusterLinkage linkage = ClusterLinkage.valueOf(linkageProp == null ? 
      "COMPLETE_LINKAGE" : 
      linkageProp);
    

    if ((minSimProp == null) && (numClustProp == null)) {
      throw new IllegalArgumentException(
        "This class requires either a specified number of clusters or a minimum cluster similarity threshold in order to partition throw rows of the input.  Either needs to be provided as a property");
    }
    

    if ((minSimProp != null) && (numClustProp != null)) {
      throw new IllegalArgumentException(
        "Cannot specify both a fixed number of clusters AND a minimum cluster similarity as input properties");
    }
    if (minSimProp != null) {
      try {
        double clusterSimThresh = Double.parseDouble(minSimProp);
        return toAssignments(cluster(matrix, clusterSimThresh, 
          linkage, simFunc, -1), matrix, -1);
      } catch (NumberFormatException nfe) {
        throw new IllegalArgumentException(
          "Cluster similarity threshold was not a valid double: " + 
          minSimProp);
      }
    }
    
    return cluster(matrix, -1, props);
  }
  






  public Assignments cluster(Matrix m, int numClusters, Properties props)
  {
    double clusterSimilarityThreshold = 
      Double.parseDouble(props.getProperty(
      "edu.ucla.sspace.clustering.HierarchicalAgglomerativeClustering.clusterThreshold", 
      "-1"));
    
    ClusterLinkage linkage = ClusterLinkage.valueOf(props.getProperty(
      "edu.ucla.sspace.clustering.HierarchicalAgglomerativeClustering.clusterLinkage", 
      "COMPLETE_LINKAGE"));
    
    Similarity.SimType similarityFunction = Similarity.SimType.valueOf(props.getProperty(
      "edu.ucla.sspace.clustering.HierarchicalAgglomerativeClustering.simFunc", 
      "COSINE"));
    return toAssignments(cluster(m, clusterSimilarityThreshold, linkage, 
      similarityFunction, numClusters), 
      m, numClusters);
  }
  
















  public static int[] partitionRows(Matrix m, int numClusters, ClusterLinkage linkage, Similarity.SimType similarityFunction)
  {
    return cluster(m, -1.0D, linkage, similarityFunction, numClusters);
  }
  




















  public static int[] clusterRows(Matrix m, double clusterSimilarityThreshold, ClusterLinkage linkage, Similarity.SimType similarityFunction)
  {
    return cluster(m, clusterSimilarityThreshold, linkage, 
      similarityFunction, -1);
  }
  
























  private static int[] cluster(Matrix m, double clusterSimilarityThreshold, ClusterLinkage linkage, Similarity.SimType similarityFunction, int maxNumberOfClusters)
  {
    int rows = m.rows();
    LOGGER.info("Generating similarity matrix for " + rows + " data points");
    Matrix similarityMatrix = 
      computeSimilarityMatrix(m, similarityFunction);
    


    Map<Integer, Set<Integer>> clusterAssignment = 
      generateInitialAssignment(rows);
    
    LOGGER.info("Calculating initial inter-cluster similarity using " + 
      linkage);
    



    Map<Integer, Pairing> clusterSimilarities = 
      new HashMap();
    for (Integer clusterId : clusterAssignment.keySet()) {
      clusterSimilarities.put(clusterId, 
        findMostSimilar(clusterAssignment, clusterId.intValue(), 
        linkage, similarityMatrix));
    }
    
    LOGGER.info("Assigning clusters");
    

    int nextClusterId = rows;
    




    while (clusterAssignment.size() > maxNumberOfClusters)
    {

      int cluster1index = 0;
      int cluster2index = 0;
      double highestSimilarity = -1.0D;
      


      Iterator localIterator2 = clusterSimilarities.entrySet().iterator();
      while (localIterator2.hasNext()) {
        Map.Entry<Integer, Pairing> e = (Map.Entry)localIterator2.next();
        
        Pairing p = (Pairing)e.getValue();
        Integer i = (Integer)e.getKey();
        Integer j = Integer.valueOf(pairedIndex);
        if (similarity > highestSimilarity) {
          cluster1index = i.intValue();
          cluster2index = j.intValue();
          highestSimilarity = similarity;
        }
      }
      



      if ((maxNumberOfClusters < 1) && 
        (highestSimilarity < clusterSimilarityThreshold)) {
        break;
      }
      


      int newClusterId = nextClusterId++;
      
      Object cluster1 = (Set)clusterAssignment.get(Integer.valueOf(cluster1index));
      Set<Integer> cluster2 = (Set)clusterAssignment.get(Integer.valueOf(cluster2index));
      
      LOGGER.log(Level.FINE, "Merged cluster {0} with {1}", 
        new Object[] { cluster1, cluster2 });
      


      ((Set)cluster1).addAll(cluster2);
      clusterAssignment.put(Integer.valueOf(newClusterId), cluster1);
      clusterAssignment.remove(Integer.valueOf(cluster1index));
      clusterAssignment.remove(Integer.valueOf(cluster2index));
      clusterSimilarities.remove(Integer.valueOf(cluster1index));
      clusterSimilarities.remove(Integer.valueOf(cluster2index));
      

      double mostSimilarToMerged = -1.0D;
      Integer mostSimilarToMergedId = null;
      


      if (clusterSimilarities.isEmpty()) {
        break;
      }
      




      Iterator localIterator3 = clusterSimilarities.entrySet().iterator();
      while (localIterator3.hasNext()) {
        Map.Entry<Integer, Pairing> e = (Map.Entry)localIterator3.next();
        
        Integer clusterId = (Integer)e.getKey();
        


        double simToNewCluster = 
          getSimilarity(similarityMatrix, (Set)cluster1, 
          (Set)clusterAssignment.get(clusterId), linkage);
        if (simToNewCluster > mostSimilarToMerged) {
          mostSimilarToMerged = simToNewCluster;
          mostSimilarToMergedId = clusterId;
        }
        


        Pairing p = (Pairing)e.getValue();
        if ((pairedIndex == cluster1index) || 
          (pairedIndex == cluster2index))
        {
          e.setValue(findMostSimilar(clusterAssignment, clusterId.intValue(), 
            linkage, similarityMatrix));
        }
      }
      

      clusterSimilarities.put(Integer.valueOf(newClusterId), 
        new Pairing(mostSimilarToMerged, 
        mostSimilarToMergedId.intValue()));
    }
    
    return toAssignArray(clusterAssignment, rows);
  }
  







































  public List<Merge> buildDendogram(Matrix m, ClusterLinkage linkage, Similarity.SimType similarityFunction)
  {
    int rows = m.rows();
    LOGGER.finer("Generating similarity matrix for " + rows + " data points");
    Matrix similarityMatrix = 
      computeSimilarityMatrix(m, similarityFunction);
    return buildDendrogram(similarityMatrix, linkage);
  }
  



















  public List<Merge> buildDendrogram(Matrix similarityMatrix, ClusterLinkage linkage)
  {
    if (similarityMatrix.rows() != similarityMatrix.columns()) {
      throw new IllegalArgumentException(
        "Similarity matrix must be square");
    }
    if (!(similarityMatrix instanceof OnDiskMatrix)) {
      LOGGER.fine("Similarity matrix supports fast multi-threaded access; switching to multi-threaded clustering");
      
      return buildDendogramMultithreaded(similarityMatrix, linkage);
    }
    
    int rows = similarityMatrix.rows();
    


    Map<Integer, Set<Integer>> clusterAssignment = 
      generateInitialAssignment(rows);
    
    LOGGER.finer("Calculating initial inter-cluster similarity using " + 
      linkage);
    



    Map<Integer, Pairing> clusterSimilarities = 
      new HashMap();
    

    for (Integer clusterId : clusterAssignment.keySet()) {
      clusterSimilarities.put(clusterId, 
        findMostSimilar(clusterAssignment, clusterId.intValue(), 
        linkage, similarityMatrix));
    }
    
    LOGGER.finer("Assigning clusters");
    List<Merge> merges = new ArrayList(rows - 1);
    

    for (int mergeIter = 0; mergeIter < rows - 1; mergeIter++) {
      LOGGER.finer("Computing dendogram merge" + 
        mergeIter + "/" + (rows - 1));
      

      int cluster1index = 0;
      int cluster2index = 0;
      double highestSimilarity = -1.0D;
      



      Iterator localIterator2 = clusterSimilarities.entrySet().iterator();
      while (localIterator2.hasNext()) {
        Map.Entry<Integer, Pairing> e = (Map.Entry)localIterator2.next();
        
        Pairing p = (Pairing)e.getValue();
        Integer i = (Integer)e.getKey();
        Integer j = Integer.valueOf(pairedIndex);
        if (similarity > highestSimilarity) {
          cluster1index = i.intValue();
          cluster2index = j.intValue();
          highestSimilarity = similarity;
        }
      }
      

      if (cluster1index > cluster2index) {
        int tmp = cluster2index;
        cluster2index = cluster1index;
        cluster1index = tmp;
      }
      


      Merge merge = 
        new Merge(cluster1index, cluster2index, highestSimilarity);
      merges.add(merge);
      
      Object cluster1 = (Set)clusterAssignment.get(Integer.valueOf(cluster1index));
      Set<Integer> cluster2 = (Set)clusterAssignment.get(Integer.valueOf(cluster2index));
      
      LOGGER.log(Level.FINER, 
        "Merged cluster {0} with {1}, similarity {2}", 
        new Object[] { Integer.valueOf(cluster1index), Integer.valueOf(cluster2index), 
        Double.valueOf(highestSimilarity) });
      



      ((Set)cluster1).addAll(cluster2);
      clusterAssignment.remove(Integer.valueOf(cluster2index));
      clusterSimilarities.remove(Integer.valueOf(cluster2index));
      

      if (clusterAssignment.size() == 1) {
        break;
      }
      
      double mostSimilarToMerged = -1.0D;
      Integer mostSimilarToMergedId = null;
      








      Iterator localIterator3 = clusterSimilarities.entrySet().iterator();
      while (localIterator3.hasNext()) {
        Map.Entry<Integer, Pairing> e = (Map.Entry)localIterator3.next();
        
        Integer clusterId = (Integer)e.getKey();
        

        if (clusterId.intValue() != cluster1index)
        {



          double simToNewCluster = 
            getSimilarity(similarityMatrix, (Set)cluster1, 
            (Set)clusterAssignment.get(clusterId), linkage);
          


          if (simToNewCluster > mostSimilarToMerged) {
            mostSimilarToMerged = simToNewCluster;
            mostSimilarToMergedId = clusterId;
          }
          


          Pairing p = (Pairing)e.getValue();
          if ((pairedIndex == cluster1index) || 
            (pairedIndex == cluster2index))
          {
            e.setValue(findMostSimilar(clusterAssignment, clusterId.intValue(), 
              linkage, similarityMatrix));
          }
        }
      }
      
      clusterSimilarities.put(Integer.valueOf(cluster1index), 
        new Pairing(mostSimilarToMerged, 
        mostSimilarToMergedId.intValue()));
    }
    
    return merges;
  }
  


  private List<Merge> buildDendogramMultithreaded(final Matrix similarityMatrix, final ClusterLinkage linkage)
  {
    int rows = similarityMatrix.rows();
    


    final Map<Integer, Set<Integer>> clusterAssignment = 
      generateInitialAssignment(rows);
    
    LOGGER.finer("Calculating initial inter-cluster similarity using " + 
      linkage);
    



    final Map<Integer, Pairing> clusterSimilarities = 
      new ConcurrentHashMap(clusterAssignment.size());
    



    Object taskKey = 
      workQueue.registerTaskGroup(clusterAssignment.size());
    for (Integer clusterId : clusterAssignment.keySet()) {
      final Integer clustId = clusterId;
      workQueue.add(taskKey, new Runnable() {
        public void run() {
          clusterSimilarities.put(clustId, 
            HierarchicalAgglomerativeClustering.findMostSimilar(clusterAssignment, clustId.intValue(), 
            linkage, similarityMatrix));
        }
      });
    }
    workQueue.await(taskKey);
    
    LOGGER.finer("Assigning clusters");
    List<Merge> merges = new ArrayList(rows - 1);
    


    for (int mergeIter = 0; mergeIter < rows - 1; mergeIter++) {
      LOGGER.finer("Computing dendogram merge " + mergeIter);
      System.out.println("Computing dendogram merge " + 
        mergeIter + "/" + (rows - 1));
      


      int cluster1index = 0;
      int cluster2index = 0;
      double highestSimilarity = -1.0D;
      



      Iterator localIterator2 = clusterSimilarities.entrySet().iterator();
      while (localIterator2.hasNext()) {
        Map.Entry<Integer, Pairing> e = (Map.Entry)localIterator2.next();
        
        Pairing p = (Pairing)e.getValue();
        Integer i = (Integer)e.getKey();
        Integer j = Integer.valueOf(pairedIndex);
        if (similarity > highestSimilarity) {
          cluster1index = i.intValue();
          cluster2index = j.intValue();
          highestSimilarity = similarity;
        }
      }
      

      if (cluster1index > cluster2index) {
        int tmp = cluster2index;
        cluster2index = cluster1index;
        cluster1index = tmp;
      }
      


      Merge merge = 
        new Merge(cluster1index, cluster2index, highestSimilarity);
      merges.add(merge);
      
      Object cluster1 = (Set)clusterAssignment.get(Integer.valueOf(cluster1index));
      Set<Integer> cluster2 = (Set)clusterAssignment.get(Integer.valueOf(cluster2index));
      
      LOGGER.log(Level.FINER, 
        "Merged cluster {0} with {1}, similarity {2}", 
        new Object[] { Integer.valueOf(cluster1index), Integer.valueOf(cluster2index), 
        Double.valueOf(highestSimilarity) });
      



      ((Set)cluster1).addAll(cluster2);
      clusterAssignment.remove(Integer.valueOf(cluster2index));
      clusterSimilarities.remove(Integer.valueOf(cluster2index));
      

      if (clusterAssignment.size() == 1) {
        break;
      }
      






      final ConcurrentNavigableMap<Double, Integer> mostSimilarMap = 
        new ConcurrentSkipListMap();
      

      taskKey = 
        workQueue.registerTaskGroup(clusterSimilarities.size() - 1);
      

      Iterator localIterator3 = clusterSimilarities.entrySet().iterator();
      while (localIterator3.hasNext()) {
        Map.Entry<Integer, Pairing> entry = (Map.Entry)localIterator3.next();
        

        final Map.Entry<Integer, Pairing> e = entry;
        final Integer clusterId = (Integer)e.getKey();
        final Pairing p = (Pairing)e.getValue();
        final int c1index = cluster1index;
        final int c2index = cluster2index;
        

        if (clusterId.intValue() != c1index)
        {

          workQueue.add(taskKey, new Runnable()
          {
            public void run()
            {
              double mostSimilarToMerged = -1.0D;
              Integer mostSimilarToMergedId = null;
              


              double simToNewCluster = 
                HierarchicalAgglomerativeClustering.getSimilarity(similarityMatrix, val$cluster1, 
                (Set)clusterAssignment.get(clusterId), 
                linkage);
              


              if (simToNewCluster > mostSimilarToMerged) {
                mostSimilarToMerged = simToNewCluster;
                mostSimilarToMergedId = clusterId;
              }
              



              if ((ppairedIndex == c1index) || 
                (ppairedIndex == c2index))
              {
                e.setValue(HierarchicalAgglomerativeClustering.findMostSimilar(clusterAssignment, 
                  clusterId.intValue(), linkage, similarityMatrix));
              }
              


              mostSimilarMap.put(Double.valueOf(mostSimilarToMerged), 
                mostSimilarToMergedId);
            }
          });
        }
      }
      
      workQueue.await(taskKey);
      





      Map.Entry<Double, Integer> highest = mostSimilarMap.lastEntry();
      

      clusterSimilarities.put(Integer.valueOf(cluster1index), 
        new Pairing(((Double)highest.getKey()).doubleValue(), 
        ((Integer)highest.getValue()).intValue()));
    }
    
    return merges;
  }
  























































































































  private static Pairing findMostSimilar(Map<Integer, Set<Integer>> curAssignment, int curCluster, ClusterLinkage linkage, Matrix similarityMatrix)
  {
    double mostSimilar = -1.0D;
    Integer paired = Integer.valueOf(-1);
    
    Iterator localIterator = curAssignment.entrySet().iterator();
    while (localIterator.hasNext()) {
      Map.Entry<Integer, Set<Integer>> otherIdAndCluster = (Map.Entry)localIterator.next();
      Integer otherId = (Integer)otherIdAndCluster.getKey();
      if (!otherId.equals(Integer.valueOf(curCluster)))
      {
        double similarity = getSimilarity(
          similarityMatrix, (Set)curAssignment.get(Integer.valueOf(curCluster)), 
          (Set)otherIdAndCluster.getValue(), linkage);
        if (similarity > mostSimilar) {
          mostSimilar = similarity;
          paired = otherId;
        }
      } }
    return new Pairing(mostSimilar, paired.intValue());
  }
  











  private static int[] toAssignArray(Map<Integer, Set<Integer>> assignment, int numDataPoints)
  {
    int[] clusters = new int[numDataPoints];
    for (int i = 0; i < numDataPoints; i++)
      clusters[i] = -1;
    int clusterIndex = 0;
    for (Set<Integer> cluster : assignment.values())
    {


      int r = ((Integer)cluster.iterator().next()).intValue();
      if (clusters[r] == -1)
      {


        for (Iterator localIterator2 = cluster.iterator(); localIterator2.hasNext();) { int row = ((Integer)localIterator2.next()).intValue();
          clusters[row] = clusterIndex;
        }
        clusterIndex++;
      } }
    LOGGER.info("total number of clusters: " + clusterIndex);
    return clusters;
  }
  





  private static Assignments toAssignments(int[] rowAssignments, Matrix matrix, int numClusters)
  {
    if (numClusters == -1) {
      int[] arrayOfInt = rowAssignments;int j = rowAssignments.length; for (int i = 0; i < j; i++) { int assignment = arrayOfInt[i];
        numClusters = Math.max(numClusters, assignment + 1);
      } }
    Assignment[] assignments = new Assignment[rowAssignments.length];
    for (int i = 0; i < rowAssignments.length; i++)
      assignments[i] = new HardAssignment(rowAssignments[i]);
    return new Assignments(numClusters, assignments, matrix);
  }
  




  private static Map<Integer, Set<Integer>> generateInitialAssignment(int numDataPoints)
  {
    Map<Integer, Set<Integer>> clusterAssignment = 
      new HashMap(numDataPoints);
    for (int i = 0; i < numDataPoints; i++) {
      HashSet<Integer> cluster = new HashSet();
      cluster.add(Integer.valueOf(i));
      clusterAssignment.put(Integer.valueOf(i), cluster);
    }
    return clusterAssignment;
  }
  




  private static Matrix computeSimilarityMatrix(Matrix m, Similarity.SimType similarityFunction)
  {
    Matrix similarityMatrix = 
      Matrices.create(m.rows(), m.rows(), Matrix.Type.DENSE_ON_DISK);
    for (int i = 0; i < m.rows(); i++) {
      for (int j = i + 1; j < m.rows(); j++) {
        double similarity = 
          Similarity.getSimilarity(similarityFunction, 
          m.getRowVector(i), 
          m.getRowVector(j));
        similarityMatrix.set(i, j, similarity);
        similarityMatrix.set(j, i, similarity);
      }
    }
    return similarityMatrix;
  }
  















  private static double getSimilarity(Matrix similarityMatrix, Set<Integer> cluster, Set<Integer> toAdd, ClusterLinkage linkage)
  {
    double similarity = -1.0D;
    Iterator localIterator1; Iterator localIterator2; switch (linkage) {
    case COMPLETE_LINKAGE: 
      double highestSimilarity = -1.0D;
      for (localIterator1 = cluster.iterator(); localIterator1.hasNext(); 
          localIterator2.hasNext())
      {
        int i = ((Integer)localIterator1.next()).intValue();
        localIterator2 = toAdd.iterator(); continue;int j = ((Integer)localIterator2.next()).intValue();
        double s = similarityMatrix.get(i, j);
        if (s > highestSimilarity) {
          highestSimilarity = s;
        }
      }
      similarity = highestSimilarity;
      break;
    

    case MEAN_LINKAGE: 
      double lowestSimilarity = 1.0D;
      for (localIterator1 = cluster.iterator(); localIterator1.hasNext(); 
          localIterator2.hasNext())
      {
        int i = ((Integer)localIterator1.next()).intValue();
        localIterator2 = toAdd.iterator(); continue;int j = ((Integer)localIterator2.next()).intValue();
        double s = similarityMatrix.get(i, j);
        if (s < lowestSimilarity) {
          lowestSimilarity = s;
        }
      }
      similarity = lowestSimilarity;
      break;
    

    case MEDIAN_LINKAGE: 
      double similaritySum = 0.0D;
      for (localIterator1 = cluster.iterator(); localIterator1.hasNext(); 
          localIterator2.hasNext())
      {
        int i = ((Integer)localIterator1.next()).intValue();
        localIterator2 = toAdd.iterator(); continue;int j = ((Integer)localIterator2.next()).intValue();
        similaritySum += similarityMatrix.get(i, j);
      }
      
      similarity = similaritySum / (cluster.size() * toAdd.size());
      break;
    

    case SINGLE_LINKAGE: 
      double[] similarities = new double[cluster.size() * toAdd.size()];
      int index = 0;
      for (localIterator1 = cluster.iterator(); localIterator1.hasNext(); 
          localIterator2.hasNext())
      {
        int i = ((Integer)localIterator1.next()).intValue();
        localIterator2 = toAdd.iterator(); continue;int j = ((Integer)localIterator2.next()).intValue();
        similarities[(index++)] = similarityMatrix.get(i, j);
      }
      
      Arrays.sort(similarities);
      similarity = similarities[(similarities.length / 2)];
      break;
    

    default: 
      if (!$assertionsDisabled) throw new AssertionError("unknown linkage method");
      break; }
    return similarity;
  }
  




  private static class Pairing
    implements Comparable<Pairing>
  {
    public final double similarity;
    


    public final int pairedIndex;
    



    public Pairing(double similarity, int pairedIndex)
    {
      this.similarity = similarity;
      this.pairedIndex = pairedIndex;
    }
    
    public int compareTo(Pairing p) {
      return (int)((similarity - similarity) * 2.147483647E9D);
    }
    
    public boolean equals(Object o) {
      return ((o instanceof Pairing)) && 
        (pairedIndex == pairedIndex);
    }
    
    public int hashCode() {
      return pairedIndex;
    }
  }
}
