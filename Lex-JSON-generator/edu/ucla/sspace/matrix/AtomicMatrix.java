package edu.ucla.sspace.matrix;

public abstract interface AtomicMatrix
  extends Matrix
{
  public abstract double getAndAdd(int paramInt1, int paramInt2, double paramDouble);
  
  public abstract double addAndGet(int paramInt1, int paramInt2, double paramDouble);
}
