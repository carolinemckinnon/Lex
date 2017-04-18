package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.DoubleVector;

public abstract interface GlobalTransform
{
  public abstract double transform(int paramInt1, int paramInt2, double paramDouble);
  
  public abstract double transform(int paramInt, DoubleVector paramDoubleVector);
}
