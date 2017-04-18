package edu.ucla.sspace.dependency;

public abstract interface DependencyPathAcceptor
{
  public abstract boolean accepts(DependencyPath paramDependencyPath);
  
  public abstract int maxPathLength();
}
