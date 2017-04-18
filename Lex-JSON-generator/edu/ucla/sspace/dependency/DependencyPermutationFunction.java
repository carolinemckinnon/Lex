package edu.ucla.sspace.dependency;

import edu.ucla.sspace.vector.Vector;

public abstract interface DependencyPermutationFunction<T extends Vector>
{
  public abstract T permute(T paramT, DependencyPath paramDependencyPath);
}
