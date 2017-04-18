package edu.ucla.sspace.index;

import edu.ucla.sspace.vector.Vector;

public abstract interface PermutationFunction<T extends Vector>
{
  public abstract T permute(T paramT, int paramInt);
}
