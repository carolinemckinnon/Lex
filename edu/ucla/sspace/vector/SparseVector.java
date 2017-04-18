package edu.ucla.sspace.vector;

public abstract interface SparseVector<T extends Number>
  extends Vector<T>
{
  public abstract int[] getNonZeroIndices();
}
