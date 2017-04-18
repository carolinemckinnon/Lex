package edu.ucla.sspace.wordsi;

import edu.ucla.sspace.clustering.Assignment;
import edu.ucla.sspace.clustering.Assignments;
import edu.ucla.sspace.clustering.Clustering;
import edu.ucla.sspace.matrix.Matrices;
import edu.ucla.sspace.matrix.SparseMatrix;
import edu.ucla.sspace.util.WorkQueue;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.Vectors;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;






































public class WaitingWordsi
  extends BaseWordsi
{
  private static final Logger LOG = Logger.getLogger(
    WaitingWordsi.class.getName());
  







  private final Clustering clustering;
  







  private final Map<String, List<SparseDoubleVector>> dataVectors;
  







  private final Map<String, SparseDoubleVector> wordSpace;
  






  private final int numClusters;
  






  private final AssignmentReporter reporter;
  







  public WaitingWordsi(Set<String> acceptedWords, ContextExtractor extractor, Clustering clustering, AssignmentReporter reporter)
  {
    this(acceptedWords, extractor, clustering, reporter, 0);
  }
  

















  public WaitingWordsi(Set<String> acceptedWords, ContextExtractor extractor, Clustering clustering, AssignmentReporter reporter, int numClusters)
  {
    super(acceptedWords, extractor);
    
    this.clustering = clustering;
    this.reporter = reporter;
    this.numClusters = numClusters;
    
    dataVectors = new HashMap();
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
    List<SparseDoubleVector> termContexts = (List)dataVectors.get(focusKey);
    if (termContexts == null) {
      synchronized (this) {
        termContexts = (List)dataVectors.get(focusKey);
        if (termContexts == null) {
          termContexts = new ArrayList();
          dataVectors.put(focusKey, termContexts);
        }
      }
    }
    

    int contextId = 0;
    synchronized (termContexts) {
      contextId = termContexts.size();
      termContexts.add(context);
    }
    

    if (reporter != null) {
      reporter.assignContextToKey(focusKey, secondaryKey, contextId);
    }
  }
  

  public void processSpace(final Properties props)
  {
    WorkQueue workQueue = WorkQueue.getWorkQueue();
    
    Object key = workQueue.registerTaskGroup(dataVectors.size());
    

    Iterator localIterator1 = dataVectors.entrySet().iterator();
    while (localIterator1.hasNext()) {
      Map.Entry<String, List<SparseDoubleVector>> entry = (Map.Entry)localIterator1.next();
      

      final String senseName = (String)entry.getKey();
      
      List<SparseDoubleVector> contextsWithNoLength = (List)entry.getValue();
      final List<SparseDoubleVector> contextSet = 
        new ArrayList(contextsWithNoLength.size());
      for (SparseDoubleVector v : contextsWithNoLength) {
        contextSet.add(Vectors.subview(v, 0, getVectorLength()));
      }
      workQueue.add(key, new Runnable() {
        public void run() {
          WaitingWordsi.this.clusterTerm(senseName, contextSet, props);
        }
      });
    }
    workQueue.await(key);
    LOG.info("Finished processing all terms");
  }
  





  private void clusterTerm(String senseName, List<SparseDoubleVector> contextSet, Properties props)
  {
    SparseMatrix contexts = Matrices.asSparseMatrix(contextSet);
    

    LOG.info("Clustering term: " + senseName);
    Assignments assignments = numClusters > 0 ? 
      clustering.cluster(contexts, numClusters, props) : 
      clustering.cluster(contexts, props);
    LOG.info("Finished clustering term: " + senseName);
    
    SparseDoubleVector[] centroids = assignments.getSparseCentroids();
    

    for (int index = 0; index < centroids.length; index++) {
      String sense = index > 0 ? 
        senseName + "-" + index : 
        senseName;
      wordSpace.put(sense, centroids[index]);
    }
    
    LOG.info("Finished creating centroids for term: " + senseName);
    

    contextSet.clear();
    

    if (reporter == null) {
      return;
    }
    
    String[] contextLabels = reporter.contextLabels(senseName);
    if (contextLabels.length == 0) {
      return;
    }
    LOG.info("Making assignment report: " + senseName);
    



    for (int i = 0; i < assignments.size(); i++)
      if (assignments.get(i).assignments().length > 0)
        reporter.updateAssignment(senseName, contextLabels[i], 
          assignments.get(i).assignments()[0]);
    LOG.info("Finished making assignment report: " + senseName);
  }
}
