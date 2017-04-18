package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.DoubleVector;









public abstract interface Matrix
{
  public abstract double get(int paramInt1, int paramInt2);
  
  public abstract double[] getColumn(int paramInt);
  
  public abstract DoubleVector getColumnVector(int paramInt);
  
  public abstract double[] getRow(int paramInt);
  
  public abstract DoubleVector getRowVector(int paramInt);
  
  public abstract int columns();
  
  public abstract double[][] toDenseArray();
  
  public abstract int rows();
  
  public abstract void set(int paramInt1, int paramInt2, double paramDouble);
  
  public abstract void setColumn(int paramInt, double[] paramArrayOfDouble);
  
  public abstract void setColumn(int paramInt, DoubleVector paramDoubleVector);
  
  public abstract void setRow(int paramInt, double[] paramArrayOfDouble);
  
  public abstract void setRow(int paramInt, DoubleVector paramDoubleVector);
  
  public static enum Type
  {
    SPARSE_IN_MEMORY, 
    




    DENSE_IN_MEMORY, 
    




    SPARSE_ON_DISK, 
    




    DENSE_ON_DISK, 
    




    DIAGONAL_IN_MEMORY;
  }
}
