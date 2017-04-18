package edu.ucla.sspace.vector;

import java.io.Serializable;









































class SynchronizedVector
  implements DoubleVector, Serializable
{
  private static final long serialVersionUID = 1L;
  private final DoubleVector vector;
  
  public SynchronizedVector(DoubleVector v)
  {
    vector = v;
  }
  


  public synchronized double add(int index, double delta)
  {
    return vector.add(index, delta);
  }
  


  public synchronized double get(int index)
  {
    return vector.get(index);
  }
  


  public synchronized Double getValue(int index)
  {
    return Double.valueOf(get(index));
  }
  


  public double magnitude()
  {
    return vector.magnitude();
  }
  


  public synchronized void set(int index, double value)
  {
    vector.set(index, value);
  }
  


  public synchronized void set(int index, Number value)
  {
    vector.set(index, value.doubleValue());
  }
  


  public synchronized double[] toArray()
  {
    return vector.toArray();
  }
  


  public synchronized int length()
  {
    return vector.length();
  }
}
