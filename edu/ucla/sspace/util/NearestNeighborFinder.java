package edu.ucla.sspace.util;

import edu.ucla.sspace.vector.Vector;
import java.util.Set;

public abstract interface NearestNeighborFinder
{
  public abstract SortedMultiMap<Double, String> getMostSimilar(String paramString, int paramInt);
  
  public abstract SortedMultiMap<Double, String> getMostSimilar(Set<String> paramSet, int paramInt);
  
  public abstract SortedMultiMap<Double, String> getMostSimilar(Vector paramVector, int paramInt);
}
