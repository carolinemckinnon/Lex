package edu.ucla.sspace.vector;

public abstract interface DoubleVector
  extends Vector<Double>
{
  public abstract double add(int paramInt, double paramDouble);
  
  public abstract double get(int paramInt);
  
  public abstract Double getValue(int paramInt);
  
  public abstract void set(int paramInt, double paramDouble);
  
  public abstract double[] toArray();
}
