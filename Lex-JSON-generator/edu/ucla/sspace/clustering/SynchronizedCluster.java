package edu.ucla.sspace.clustering;

import edu.ucla.sspace.vector.DoubleVector;
import java.util.BitSet;
import java.util.List;

































public class SynchronizedCluster<T extends DoubleVector>
  implements Cluster<T>
{
  private Cluster<T> cluster;
  
  public SynchronizedCluster(Cluster<T> cluster)
  {
    this.cluster = cluster;
  }
  


  public synchronized void addVector(T vector, int id)
  {
    cluster.addVector(vector, id);
  }
  


  public synchronized double compareWithVector(T vector)
  {
    return cluster.compareWithVector(vector);
  }
  


  public synchronized T centroid()
  {
    return cluster.centroid();
  }
  


  public synchronized List<T> dataPointValues()
  {
    return cluster.dataPointValues();
  }
  


  public synchronized BitSet dataPointIds()
  {
    return cluster.dataPointIds();
  }
  


  public synchronized void merge(Cluster<T> other)
  {
    cluster.merge(other);
  }
  


  public synchronized int size()
  {
    return cluster.size();
  }
}
