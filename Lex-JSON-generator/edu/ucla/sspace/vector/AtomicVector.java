package edu.ucla.sspace.vector;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

















































public class AtomicVector
  implements DoubleVector, Serializable
{
  private static final long serialVersionUID = 1L;
  private final DoubleVector vector;
  private final Lock readLock;
  private final Lock writeLock;
  
  public AtomicVector(DoubleVector v)
  {
    vector = v;
    
    ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    readLock = rwLock.readLock();
    writeLock = rwLock.writeLock();
  }
  


  public double addAndGet(int index, double delta)
  {
    return add(index, delta);
  }
  


  public double getAndAdd(int index, double delta)
  {
    writeLock.lock();
    double value = vector.get(index);
    vector.set(index, value + delta);
    writeLock.unlock();
    return value;
  }
  


  public double add(int index, double delta)
  {
    writeLock.lock();
    double value = vector.add(index, delta);
    writeLock.unlock();
    return value;
  }
  


  public double get(int index)
  {
    readLock.lock();
    double value = vector.get(index);
    readLock.unlock();
    return value;
  }
  


  public Double getValue(int index)
  {
    return Double.valueOf(get(index));
  }
  


  public DoubleVector getVector()
  {
    return vector;
  }
  


  public double magnitude()
  {
    readLock.lock();
    double m = vector.magnitude();
    readLock.unlock();
    return m;
  }
  


  public void set(int index, double value)
  {
    writeLock.lock();
    vector.set(index, value);
    writeLock.unlock();
  }
  


  public void set(int index, Number value)
  {
    set(index, value.doubleValue());
  }
  


  public double[] toArray()
  {
    readLock.lock();
    double[] array = vector.toArray();
    readLock.lock();
    return array;
  }
  


  public int length()
  {
    readLock.lock();
    int length = vector.length();
    readLock.unlock();
    return length;
  }
}
