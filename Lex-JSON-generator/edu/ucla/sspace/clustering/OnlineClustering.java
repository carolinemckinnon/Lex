package edu.ucla.sspace.clustering;

import edu.ucla.sspace.vector.DoubleVector;
import java.util.List;

public abstract interface OnlineClustering<T extends DoubleVector>
{
  public static final String PROPERTY_PREFIX = "edu.ucla.sspace.clustering.OnlineClustering";
  public static final String NUM_CLUSTERS_PROPERTY = "edu.ucla.sspace.clustering.OnlineClustering.numClusters";
  
  public abstract int addVector(T paramT);
  
  public abstract Cluster<T> getCluster(int paramInt);
  
  public abstract List<Cluster<T>> getClusters();
  
  public abstract int size();
}
