package edu.ucla.sspace.dependency;

public abstract interface DependencyPathWeight
{
  public abstract double scorePath(DependencyPath paramDependencyPath);
}
