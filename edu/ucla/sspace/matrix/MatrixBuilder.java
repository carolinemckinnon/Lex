package edu.ucla.sspace.matrix;

import edu.ucla.sspace.util.SparseArray;
import edu.ucla.sspace.vector.Vector;
import java.io.File;

public abstract interface MatrixBuilder
{
  public abstract int addColumn(double[] paramArrayOfDouble);
  
  public abstract int addColumn(SparseArray<? extends Number> paramSparseArray);
  
  public abstract int addColumn(Vector paramVector);
  
  public abstract void finish();
  
  public abstract File getFile();
  
  public abstract MatrixIO.Format getMatrixFormat();
  
  public abstract MatrixFile getMatrixFile();
  
  public abstract boolean isFinished();
}
