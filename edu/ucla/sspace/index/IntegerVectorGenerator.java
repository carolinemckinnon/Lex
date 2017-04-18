package edu.ucla.sspace.index;

import edu.ucla.sspace.util.Generator;
import edu.ucla.sspace.vector.IntegerVector;

public abstract interface IntegerVectorGenerator<T extends IntegerVector>
  extends Generator<T>
{
  public abstract T generate();
}
