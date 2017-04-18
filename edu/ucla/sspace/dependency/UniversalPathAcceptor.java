package edu.ucla.sspace.dependency;














public class UniversalPathAcceptor
  implements DependencyPathAcceptor
{
  public UniversalPathAcceptor() {}
  













  public boolean accepts(DependencyPath relation)
  {
    return true;
  }
  


  public int maxPathLength()
  {
    return Integer.MAX_VALUE;
  }
}
