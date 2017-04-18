package edu.ucla.sspace.clustering;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;





































public class SoftAssignment
  implements Assignment
{
  private int[] assignments;
  
  public SoftAssignment(int[] assignments)
  {
    this.assignments = assignments;
  }
  



  public SoftAssignment(Collection<Integer> clusterIds)
  {
    assignments = new int[clusterIds.size()];
    Iterator<Integer> it = clusterIds.iterator();
    for (int i = 0; i < assignments.length; i++) {
      assignments[i] = ((Integer)it.next()).intValue();
    }
  }
  


  public SoftAssignment(Integer... clusterIds)
  {
    assignments = new int[clusterIds.length];
    for (int i = 0; i < clusterIds.length; i++) {
      assignments[i] = clusterIds[i].intValue();
    }
  }
  

  public int[] assignments()
  {
    return assignments;
  }
  


  public int length()
  {
    return assignments.length;
  }
  
  public String toString() {
    return "SoftAssignment" + Arrays.toString(assignments);
  }
}
