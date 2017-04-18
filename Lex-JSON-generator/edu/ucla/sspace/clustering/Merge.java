package edu.ucla.sspace.clustering;

import java.io.Serializable;

































public class Merge
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private final int remainingCluster;
  private final int mergedCluster;
  private final double similarity;
  
  public Merge(int remainingCluster, int mergedCluster, double similarity)
  {
    this.remainingCluster = remainingCluster;
    this.mergedCluster = mergedCluster;
    this.similarity = similarity;
  }
  
  public boolean equals(Object o) {
    if ((o instanceof Merge)) {
      Merge m = (Merge)o;
      return (remainingCluster == remainingCluster) && 
        (mergedCluster == mergedCluster) && 
        (similarity == similarity);
    }
    return false;
  }
  
  public int hashCode() {
    return remainingCluster ^ mergedCluster;
  }
  


  public int mergedCluster()
  {
    return mergedCluster;
  }
  



  public int remainingCluster()
  {
    return remainingCluster;
  }
  


  public double similarity()
  {
    return similarity;
  }
  
  public String toString() {
    return 
      "(" + mergedCluster + " -> " + remainingCluster + ": " + similarity + ")";
  }
}
