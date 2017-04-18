package edu.ucla.sspace.clustering;

















public class HardAssignment
  implements Assignment
{
  private final int[] assignments;
  















  public HardAssignment()
  {
    assignments = new int[0];
  }
  



  public HardAssignment(int assignment)
  {
    assignments = new int[1];
    assignments[0] = assignment;
  }
  


  public int[] assignments()
  {
    return assignments;
  }
  


  public int length()
  {
    return assignments.length;
  }
}
