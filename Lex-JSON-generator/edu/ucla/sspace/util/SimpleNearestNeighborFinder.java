package edu.ucla.sspace.util;

import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.similarity.CosineSimilarity;
import edu.ucla.sspace.similarity.SimilarityFunction;
import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.Vector;
import edu.ucla.sspace.vector.VectorMath;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;




















































public class SimpleNearestNeighborFinder
  implements NearestNeighborFinder
{
  private static final Logger LOGGER = Logger.getLogger(SimpleNearestNeighborFinder.class.getName());
  




  private final WorkQueue workQueue;
  



  private final SemanticSpace sspace;
  



  private final SimilarityFunction simFunc;
  




  public SimpleNearestNeighborFinder(SemanticSpace sspace)
  {
    this(sspace, new CosineSimilarity(), Runtime.getRuntime().availableProcessors());
  }
  



  public SimpleNearestNeighborFinder(SemanticSpace sspace, int numThreads)
  {
    this(sspace, new CosineSimilarity(), numThreads);
  }
  



  public SimpleNearestNeighborFinder(SemanticSpace sspace, SimilarityFunction similarity)
  {
    this(sspace, similarity, Runtime.getRuntime().availableProcessors());
  }
  






  public SimpleNearestNeighborFinder(SemanticSpace sspace, SimilarityFunction similarity, int numThreads)
  {
    this.sspace = sspace;
    simFunc = similarity;
    workQueue = WorkQueue.getWorkQueue(numThreads);
  }
  




  public SortedMultiMap<Double, String> getMostSimilar(String word, int numberOfSimilarWords)
  {
    Vector v = sspace.getVector(word);
    
    if (v == null) {
      return null;
    }
    

    SortedMultiMap<Double, String> mostSim = 
      getMostSimilar(v, numberOfSimilarWords + 1);
    Iterator<Map.Entry<Double, String>> iter = mostSim.entrySet().iterator();
    while (iter.hasNext()) {
      Map.Entry<Double, String> e = (Map.Entry)iter.next();
      if (word.equals(e.getValue())) {
        iter.remove();
        break;
      }
    }
    return mostSim;
  }
  



  public SortedMultiMap<Double, String> getMostSimilar(Set<String> terms, int numberOfSimilarWords)
  {
    if (terms.isEmpty()) {
      return null;
    }
    DoubleVector mean = new DenseVector(sspace.getVectorLength());
    int found = 0;
    for (String term : terms) {
      Vector v = sspace.getVector(term);
      if (v == null) {
        LoggerUtil.info(LOGGER, "No vector for term " + term, new Object[0]);
      } else {
        VectorMath.add(mean, v);
        found++;
      }
    }
    
    if (found == 0) {
      return null;
    }
    



    SortedMultiMap<Double, String> mostSim = 
      getMostSimilar(mean, numberOfSimilarWords + terms.size());
    Object iter = mostSim.entrySet().iterator();
    Set<Map.Entry<Double, String>> toRemove = 
      new HashSet();
    while (((Iterator)iter).hasNext()) {
      Map.Entry<Double, String> e = (Map.Entry)((Iterator)iter).next();
      if (terms.contains(e.getValue()))
        toRemove.add(e);
    }
    for (Map.Entry<Double, String> e : toRemove) {
      mostSim.remove(e.getKey(), e.getValue());
    }
    

    while (mostSim.size() > numberOfSimilarWords) {
      mostSim.remove((Double)mostSim.firstKey());
    }
    return mostSim;
  }
  









  public SortedMultiMap<Double, String> getMostSimilar(final Vector v, int numberOfSimilarWords)
  {
    if (v == null)
      return null;
    final int k = numberOfSimilarWords;
    


    int numThreads = workQueue.availableThreads();
    int numWords = sspace.getWords().size();
    List<List<String>> wordSets = 
      new ArrayList(numThreads);
    for (int i = 0; i < numThreads; i++)
      wordSets.add(new ArrayList(numWords / numThreads));
    Iterator<String> iter = sspace.getWords().iterator();
    for (int i = 0; iter.hasNext(); i++) {
      ((List)wordSets.get(i % numThreads)).add((String)iter.next());
    }
    


    final SortedMultiMap<Double, String> kMostSimTerms = 
      new BoundedSortedMultiMap(k, false);
    


    Object taskId = workQueue.registerTaskGroup(wordSets.size());
    for (List<String> words : wordSets) {
      final List<String> terms = words;
      


      workQueue.add(taskId, new Runnable()
      {
        public void run()
        {
          SortedMultiMap<Double, String> localMostSimTerms = 
            new BoundedSortedMultiMap(k, false);
          Vector tVec; for (String term : terms) {
            tVec = sspace.getVector(term);
            double sim = simFunc.sim(v, tVec);
            localMostSimTerms.put(Double.valueOf(sim), term);
          }
          
          synchronized (kMostSimTerms)
          {
            tVec = localMostSimTerms.entrySet().iterator();
            while (tVec.hasNext()) {
              Object e = (Map.Entry)tVec.next();
              kMostSimTerms.put((Double)((Map.Entry)e).getKey(), (String)((Map.Entry)e).getValue());
            }
          }
        }
      });
    }
    workQueue.await(taskId);
    
    return kMostSimTerms;
  }
}
