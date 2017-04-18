package edu.ucla.sspace.wordsi;

import edu.ucla.sspace.clustering.Cluster;
import edu.ucla.sspace.clustering.HierarchicalAgglomerativeClustering;
import edu.ucla.sspace.clustering.HierarchicalAgglomerativeClustering.ClusterLinkage;
import edu.ucla.sspace.clustering.OnlineClustering;
import edu.ucla.sspace.common.Similarity.SimType;
import edu.ucla.sspace.matrix.Matrices;
import edu.ucla.sspace.util.Generator;
import edu.ucla.sspace.util.GeneratorMap;
import edu.ucla.sspace.util.WorkQueue;
import edu.ucla.sspace.vector.SparseDoubleVector;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;











































































public class StreamingWordsi
  extends BaseWordsi
{
  private GeneratorMap<OnlineClustering<SparseDoubleVector>> clusterMap;
  private final Map<String, SparseDoubleVector> wordSpace;
  private final AssignmentReporter reporter;
  private final int numClusters;
  
  public StreamingWordsi(Set<String> acceptedWords, ContextExtractor extractor, Generator<OnlineClustering<SparseDoubleVector>> clusterGenerator, AssignmentReporter reporter, int numClusters)
  {
    super(acceptedWords, extractor);
    clusterMap = new GeneratorMap(
      clusterGenerator);
    this.reporter = reporter;
    this.numClusters = numClusters;
    
    wordSpace = new ConcurrentHashMap();
  }
  


  public Set<String> getWords()
  {
    return wordSpace.keySet();
  }
  


  public SparseDoubleVector getVector(String term)
  {
    return (SparseDoubleVector)wordSpace.get(term);
  }
  




  public void handleContextVector(String focusKey, String secondaryKey, SparseDoubleVector context)
  {
    OnlineClustering<SparseDoubleVector> clustering = 
      (OnlineClustering)clusterMap.get(focusKey);
    int contextId = clustering.addVector(context);
    if (reporter != null) {
      reporter.assignContextToKey(focusKey, secondaryKey, contextId);
    }
  }
  

  public void processSpace(Properties props)
  {
    double mergeThreshold = 0.15D;
    
    WorkQueue workQueue = WorkQueue.getWorkQueue();
    Object key = workQueue.registerTaskGroup(clusterMap.size());
    




    Iterator localIterator = clusterMap.entrySet().iterator();
    while (localIterator.hasNext()) {
      Map.Entry<String, OnlineClustering<SparseDoubleVector>> entry = (Map.Entry)localIterator.next();
      final String primaryKey = (String)entry.getKey();
      final OnlineClustering<SparseDoubleVector> contexts = 
        (OnlineClustering)entry.getValue();
      workQueue.add(key, new Runnable() {
        public void run() {
          StreamingWordsi.this.clusterAndAssignSenses(contexts, primaryKey, 0.15D);
        }
      });
    }
    workQueue.await(key);
    


    clusterMap = null;
    
    if (reporter != null) {
      reporter.finalizeReport();
    }
  }
  




  private void clusterAndAssignSenses(OnlineClustering<SparseDoubleVector> contexts, String primaryKey, double mergeThreshold)
  {
    List<Cluster<SparseDoubleVector>> newClusters = clusterStream(
      contexts.getClusters(), numClusters, 0.0D);
    


    newClusters = clusterStream(contexts.getClusters(), 0, mergeThreshold);
    


    synchronized (wordSpace) {
      wordSpace.put(primaryKey, (SparseDoubleVector)((Cluster)newClusters.get(0)).centroid());
      for (int i = 1; i < newClusters.size(); i++) {
        wordSpace.put(primaryKey + "-" + i, (SparseDoubleVector)((Cluster)newClusters.get(i)).centroid());
      }
    }
    
    if (reporter == null) {
      return;
    }
    

    String[] contextLabels = reporter.contextLabels(primaryKey);
    if (contextLabels.length == 0) {
      return;
    }
    
    int clusterId = 0;
    for (Cluster<SparseDoubleVector> cluster : newClusters) {
      BitSet contextIds = cluster.dataPointIds();
      for (int contextId = contextIds.nextSetBit(0); contextId >= 0; 
          contextId = contextIds.nextSetBit(contextId + 1)) {
        reporter.updateAssignment(
          primaryKey, contextLabels[contextId], clusterId);
      }
      clusterId++;
    }
  }
  



  private List<Cluster<SparseDoubleVector>> clusterStream(List<Cluster<SparseDoubleVector>> clusters, int numClusters, double threshold)
  {
    List<SparseDoubleVector> centroids = 
      new ArrayList();
    
    for (Cluster<SparseDoubleVector> cluster : clusters) {
      centroids.add((SparseDoubleVector)cluster.centroid());
    }
    int[] assignments;
    int[] assignments;
    if (numClusters != 0) {
      assignments = HierarchicalAgglomerativeClustering.partitionRows(
        Matrices.asSparseMatrix(centroids), 
        numClusters, 
        HierarchicalAgglomerativeClustering.ClusterLinkage.MEAN_LINKAGE, 
        Similarity.SimType.COSINE);
    } else {
      assignments = HierarchicalAgglomerativeClustering.clusterRows(
        Matrices.asSparseMatrix(centroids), 
        threshold, 
        HierarchicalAgglomerativeClustering.ClusterLinkage.MEAN_LINKAGE, 
        Similarity.SimType.COSINE);
    }
    

    Object newClusters = 
      new ArrayList();
    




    for (int i = 0; i < assignments.length; i++) {
      int assignment = assignments[i];
      while (assignment >= ((List)newClusters).size())
        ((List)newClusters).add(null);
      Cluster<SparseDoubleVector> cluster = (Cluster)((List)newClusters).get(assignment);
      if (cluster == null) {
        ((List)newClusters).set(assignment, (Cluster)clusters.get(i));
      } else {
        cluster.merge((Cluster)clusters.get(i));
      }
    }
    return newClusters;
  }
}
