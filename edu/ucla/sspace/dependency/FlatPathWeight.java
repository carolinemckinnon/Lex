package edu.ucla.sspace.dependency;














public class FlatPathWeight
  implements DependencyPathWeight
{
  public FlatPathWeight() {}
  













  public double scorePath(DependencyPath path)
  {
    return 1.0D;
  }
}
