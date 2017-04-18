package edu.ucla.sspace.util;

public abstract interface SparseArray<T>
{
  public abstract int cardinality();
  
  public abstract T get(int paramInt);
  
  public abstract int[] getElementIndices();
  
  public abstract int length();
  
  public abstract void set(int paramInt, T paramT);
  
  public abstract <E> E[] toArray(E[] paramArrayOfE);
}
