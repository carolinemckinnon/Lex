package edu.ucla.sspace.matrix;

import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.common.Similarity.SimType;
import edu.ucla.sspace.similarity.SimilarityFunction;
import edu.ucla.sspace.util.BoundedSortedMultiMap;
import edu.ucla.sspace.util.MultiMap;
import edu.ucla.sspace.util.SortedMultiMap;
import edu.ucla.sspace.util.WorkQueue;
import edu.ucla.sspace.vector.Vector;









































public class RowComparator
{
  private final WorkQueue workQueue;
  
  public RowComparator()
  {
    this(Runtime.getRuntime().availableProcessors());
  }
  


  public RowComparator(int numProcs)
  {
    workQueue = WorkQueue.getWorkQueue(numProcs);
  }
  














  public SortedMultiMap<Double, Integer> getMostSimilar(Matrix m, int row, int kNearestRows, Similarity.SimType similarityType)
  {
    return getMostSimilar(m, row, kNearestRows, 
      Similarity.getSimilarityFunction(similarityType));
  }
  















  public SortedMultiMap<Double, Integer> getMostSimilar(Matrix m, int row, int kNearestRows, SimilarityFunction simFunction)
  {
    Object key = workQueue.registerTaskGroup(m.rows() - 1);
    


    SortedMultiMap<Double, Integer> mostSimilar = 
      new BoundedSortedMultiMap(kNearestRows, false);
    

    int rows = m.rows();
    Vector v = m.getRowVector(row);
    for (int i = 0; i < rows; i++)
    {
      if (i != row)
      {

        workQueue.add(key, 
          new Comparison(m, v, i, simFunction, mostSimilar));
      }
    }
    
    workQueue.await(key);
    
    return mostSimilar;
  }
  

  private static class Comparison
    implements Runnable
  {
    private final Matrix m;
    
    private final Vector row;
    
    private final int otherRow;
    
    private final SimilarityFunction simFunction;
    
    private final MultiMap<Double, Integer> mostSimilar;
    

    public Comparison(Matrix m, Vector row, int otherRow, SimilarityFunction simFunction, MultiMap<Double, Integer> mostSimilar)
    {
      this.m = m;
      this.row = row;
      this.otherRow = otherRow;
      this.simFunction = simFunction;
      this.mostSimilar = mostSimilar;
    }
    
    public void run() {
      Double similarity = Double.valueOf(simFunction.sim(
        row, m.getRowVector(otherRow)));
      

      synchronized (mostSimilar) {
        mostSimilar.put(similarity, Integer.valueOf(otherRow));
      }
    }
  }
}
