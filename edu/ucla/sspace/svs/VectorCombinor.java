package edu.ucla.sspace.svs;

import edu.ucla.sspace.vector.SparseDoubleVector;

public abstract interface VectorCombinor
{
  public abstract SparseDoubleVector combine(SparseDoubleVector paramSparseDoubleVector1, SparseDoubleVector paramSparseDoubleVector2);
  
  public abstract SparseDoubleVector combineUnmodified(SparseDoubleVector paramSparseDoubleVector1, SparseDoubleVector paramSparseDoubleVector2);
}
