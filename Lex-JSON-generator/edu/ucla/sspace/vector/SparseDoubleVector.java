package edu.ucla.sspace.vector;

public abstract interface SparseDoubleVector
  extends SparseVector<Double>, DoubleVector
{
  public abstract SparseDoubleVector instanceCopy();
}
