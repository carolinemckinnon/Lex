package edu.ucla.sspace.util.primitive;

import edu.ucla.sspace.util.MultiMap;
import java.util.Collection;

public abstract interface IntIntMultiMap
  extends MultiMap<Integer, Integer>
{
  public abstract boolean containsKey(int paramInt);
  
  public abstract boolean containsMapping(int paramInt1, int paramInt2);
  
  public abstract boolean containsValue(int paramInt);
  
  public abstract IntSet get(int paramInt);
  
  public abstract IntSet keySet();
  
  public abstract boolean put(int paramInt1, int paramInt2);
  
  public abstract void putAll(IntIntMultiMap paramIntIntMultiMap);
  
  public abstract boolean putMany(int paramInt, IntCollection paramIntCollection);
  
  public abstract boolean putMany(int paramInt, Collection<Integer> paramCollection);
  
  public abstract IntSet remove(int paramInt);
  
  public abstract boolean remove(int paramInt1, int paramInt2);
  
  public abstract IntCollection values();
}
