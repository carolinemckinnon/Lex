package edu.ucla.sspace.clustering;

import edu.ucla.sspace.vector.DoubleVector;
import java.util.BitSet;
import java.util.List;

public abstract interface Cluster<T extends DoubleVector>
{
  public abstract void addVector(T paramT, int paramInt);
  
  public abstract double compareWithVector(T paramT);
  
  public abstract T centroid();
  
  public abstract List<T> dataPointValues();
  
  public abstract BitSet dataPointIds();
  
  public abstract void merge(Cluster<T> paramCluster);
  
  public abstract int size();
}
