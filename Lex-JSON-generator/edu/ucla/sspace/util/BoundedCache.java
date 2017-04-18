package edu.ucla.sspace.util;

import java.util.LinkedHashMap;
import java.util.Map.Entry;



































public class BoundedCache<K, V>
  extends LinkedHashMap<K, V>
{
  private static final long serialVersionUID = 1L;
  private final int maxSize;
  
  public BoundedCache(int maxSize)
  {
    super(maxSize, 0.75F, true);
    this.maxSize = maxSize;
  }
  



  protected boolean removeEldestEntry(Map.Entry<K, V> eldest)
  {
    return size() > maxSize;
  }
}
