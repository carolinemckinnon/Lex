package edu.ucla.sspace.util;

import java.util.Comparator;

public abstract interface SortedMultiMap<K, V>
  extends MultiMap<K, V>
{
  public abstract Comparator<? super K> comparator();
  
  public abstract K firstKey();
  
  public abstract SortedMultiMap<K, V> headMap(K paramK);
  
  public abstract K lastKey();
  
  public abstract SortedMultiMap<K, V> subMap(K paramK1, K paramK2);
  
  public abstract SortedMultiMap<K, V> tailMap(K paramK);
}
