package edu.ucla.sspace.util;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract interface MultiMap<K, V>
{
  public abstract Map<K, Set<V>> asMap();
  
  public abstract void clear();
  
  public abstract boolean containsKey(Object paramObject);
  
  public abstract boolean containsMapping(Object paramObject1, Object paramObject2);
  
  public abstract boolean containsValue(Object paramObject);
  
  public abstract Set<Map.Entry<K, V>> entrySet();
  
  public abstract Set<V> get(Object paramObject);
  
  public abstract boolean isEmpty();
  
  public abstract Set<K> keySet();
  
  public abstract boolean put(K paramK, V paramV);
  
  public abstract void putAll(Map<? extends K, ? extends V> paramMap);
  
  public abstract void putAll(MultiMap<? extends K, ? extends V> paramMultiMap);
  
  public abstract boolean putMany(K paramK, Collection<V> paramCollection);
  
  public abstract Set<V> remove(K paramK);
  
  public abstract boolean remove(Object paramObject1, Object paramObject2);
  
  public abstract int range();
  
  public abstract int size();
  
  public abstract Collection<V> values();
  
  public abstract Collection<Set<V>> valueSets();
}
