package edu.ucla.sspace.util.primitive;

import java.util.Set;

public abstract interface IntSet
  extends Set<Integer>, IntCollection
{
  public abstract boolean add(int paramInt);
  
  public abstract boolean addAll(IntCollection paramIntCollection);
  
  public abstract boolean contains(int paramInt);
  
  public abstract boolean containsAll(IntCollection paramIntCollection);
  
  public abstract IntIterator iterator();
  
  public abstract boolean remove(int paramInt);
  
  public abstract boolean removeAll(IntCollection paramIntCollection);
  
  public abstract boolean retainAll(IntCollection paramIntCollection);
  
  public abstract int[] toPrimitiveArray();
}
