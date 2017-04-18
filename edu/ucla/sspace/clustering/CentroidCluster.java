package edu.ucla.sspace.clustering;

import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.VectorMath;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;


















































public class CentroidCluster<T extends DoubleVector>
  implements Cluster<T>
{
  private T centroid;
  private BitSet assignments;
  
  public CentroidCluster(T emptyVector)
  {
    centroid = emptyVector;
    assignments = new BitSet();
  }
  


  public void addVector(T vector, int id)
  {
    VectorMath.add(centroid, vector);
    if (id >= 0) {
      assignments.set(id);
    }
  }
  

  public double compareWithVector(T vector)
  {
    return Similarity.cosineSimilarity(centroid, vector);
  }
  



  public T centroid()
  {
    return centroid;
  }
  


  public List<T> dataPointValues()
  {
    return new ArrayList();
  }
  


  public BitSet dataPointIds()
  {
    return assignments;
  }
  


  public void merge(Cluster<T> other)
  {
    VectorMath.add(centroid, other.centroid());
    for (T otherDataPoint : other.dataPointValues()) {
      VectorMath.add(centroid, otherDataPoint);
    }
    for (int i = other.dataPointIds().nextSetBit(0); i >= 0; 
        i = other.dataPointIds().nextSetBit(i + 1)) {
      assignments.set(i);
    }
  }
  

  public int size()
  {
    return assignments.size();
  }
}
