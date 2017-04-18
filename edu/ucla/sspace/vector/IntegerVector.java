package edu.ucla.sspace.vector;

public abstract interface IntegerVector
  extends Vector<Integer>
{
  public abstract int add(int paramInt1, int paramInt2);
  
  public abstract int get(int paramInt);
  
  public abstract Integer getValue(int paramInt);
  
  public abstract void set(int paramInt1, int paramInt2);
  
  public abstract int[] toArray();
}
