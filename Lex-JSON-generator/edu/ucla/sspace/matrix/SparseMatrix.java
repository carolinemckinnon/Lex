package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.SparseDoubleVector;

public abstract interface SparseMatrix
  extends Matrix
{
  public abstract SparseDoubleVector getColumnVector(int paramInt);
  
  public abstract SparseDoubleVector getRowVector(int paramInt);
}
