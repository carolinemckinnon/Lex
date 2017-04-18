package edu.ucla.sspace.vector;

public abstract interface Vector<T extends Number>
{
  public abstract boolean equals(Object paramObject);
  
  public abstract Number getValue(int paramInt);
  
  public abstract int hashCode();
  
  public abstract int length();
  
  public abstract double magnitude();
  
  public abstract void set(int paramInt, Number paramNumber);
}
