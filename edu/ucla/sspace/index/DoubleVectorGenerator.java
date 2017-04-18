package edu.ucla.sspace.index;

import edu.ucla.sspace.util.Generator;
import edu.ucla.sspace.vector.DoubleVector;

public abstract interface DoubleVectorGenerator<T extends DoubleVector>
  extends Generator<T>
{
  public abstract T generate();
}
