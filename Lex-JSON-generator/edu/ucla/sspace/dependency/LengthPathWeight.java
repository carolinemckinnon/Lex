package edu.ucla.sspace.dependency;















public class LengthPathWeight
  implements DependencyPathWeight
{
  public LengthPathWeight() {}
  













  public double scorePath(DependencyPath path)
  {
    return 1.0D / path.length();
  }
}
