package edu.ucla.sspace.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;





























































public class BoundedSortedMultiMap<K, V>
  extends TreeMultiMap<K, V>
{
  private static final long serialVersionUID = 1L;
  private final int bound;
  private final boolean isKeyBound;
  private final boolean isFair;
  
  public BoundedSortedMultiMap(int bound)
  {
    this(bound, false, true, false);
  }
  












  public BoundedSortedMultiMap(int bound, boolean keyBound)
  {
    this(bound, keyBound, true, false);
  }
  




















  public BoundedSortedMultiMap(int bound, boolean keyBound, boolean retainHighest, boolean isFair)
  {
    super(retainHighest ? null : new BoundedSortedMap.ReverseComparator());
    isKeyBound = keyBound;
    this.bound = bound;
    this.isFair = isFair;
  }
  









  public boolean put(K key, V value)
  {
    boolean added = super.put(key, value);
    if (isKeyBound) {
      if (size() > bound) {
        remove(firstKey());
      }
    }
    else if (range() > bound) {
      K first = firstKey();
      Set<V> values = get(first);
      


      int elementToRemove = 0;
      if (isFair) {
        elementToRemove = (int)(Math.random() * values.size());
        V toRemove = null;
        Iterator<V> it = values.iterator();
        for (int i = 0; i <= elementToRemove; i++) {
          toRemove = it.next();
        }
        remove(first, toRemove);
      }
      else {
        remove(first, values.iterator().next());
      }
    }
    return added;
  }
  









  public boolean putMulti(K key, Collection<V> values)
  {
    boolean added = false;
    for (V v : values) {
      if (put(key, v))
        added = true;
    }
    return added;
  }
  








  public void putAll(Map<? extends K, ? extends V> m)
  {
    for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
      put(e.getKey(), e.getValue());
    }
  }
}
