package edu.ucla.sspace.common;

import edu.ucla.sspace.util.BoundedSortedMultiMap;
import edu.ucla.sspace.util.MultiMap;
import edu.ucla.sspace.util.SortedMultiMap;
import edu.ucla.sspace.util.WorkerThread;
import edu.ucla.sspace.vector.Vector;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;


















































public class WordComparator
{
  private final BlockingQueue<Runnable> workQueue;
  
  public WordComparator()
  {
    this(Runtime.getRuntime().availableProcessors());
  }
  


  public WordComparator(int numThreads)
  {
    workQueue = new LinkedBlockingQueue();
    for (int i = 0; i < numThreads; i++) {
      new WorkerThread(workQueue, 10).start();
    }
  }
  










  public SortedMultiMap<Double, String> getMostSimilar(String word, SemanticSpace sspace, int numberOfSimilarWords, Similarity.SimType similarityType)
  {
    Vector v = sspace.getVector(word);
    

    if (v == null) {
      return null;
    }
    
    Vector vector = v;
    
    Set<String> words = sspace.getWords();
    


    SortedMultiMap<Double, String> mostSimilar = 
      new BoundedSortedMultiMap(numberOfSimilarWords, 
      false);
    





    Semaphore comparisons = new Semaphore(0 - (words.size() - 2));
    


    int submitted = 0;
    for (String s : words)
    {
      String other = s;
      

      if (!word.equals(other))
      {

        workQueue.offer(new Comparison(
          comparisons, sspace, vector, 
          other, similarityType, mostSimilar));
      }
    }
    try {
      comparisons.acquire();
    }
    catch (InterruptedException ie)
    {
      if (comparisons.availablePermits() < 1) {
        throw new IllegalStateException(
          "interrupted while waiting for word comparisons to finish", 
          ie);
      }
    }
    

    return mostSimilar;
  }
  


  private static class Comparison
    implements Runnable
  {
    private final Semaphore semaphore;
    
    SemanticSpace sspace;
    
    Vector vector;
    
    String other;
    
    Similarity.SimType similarityMeasure;
    
    MultiMap<Double, String> mostSimilar;
    

    public Comparison(Semaphore semaphore, SemanticSpace sspace, Vector vector, String other, Similarity.SimType similarityMeasure, MultiMap<Double, String> mostSimilar)
    {
      this.semaphore = semaphore;
      this.sspace = sspace;
      this.vector = vector;
      this.other = other;
      this.similarityMeasure = similarityMeasure;
      this.mostSimilar = mostSimilar;
    }
    
    public void run() {
      try {
        Vector otherV = sspace.getVector(other);
        
        Double similarity = Double.valueOf(Similarity.getSimilarity(
          similarityMeasure, vector, otherV));
        

        synchronized (mostSimilar) {
          mostSimilar.put(similarity, other);
        }
      } catch (Exception e) { e = e;
        


        throw new Error(e);
      } finally { localObject1 = finally;
        

        semaphore.release();
        throw localObject1;
      }
      semaphore.release();
    }
  }
}
