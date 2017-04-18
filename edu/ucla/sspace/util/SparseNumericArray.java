package edu.ucla.sspace.util;

public abstract interface SparseNumericArray<T extends Number>
  extends SparseArray<T>
{
  public abstract T add(int paramInt, T paramT);
  
  public abstract T multiply(int paramInt, T paramT);
  
  public abstract T divide(int paramInt, T paramT);
}
