package edu.ucla.sspace.util;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap.SimpleEntry;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
































































public class TreeMultiMap<K, V>
  implements SortedMultiMap<K, V>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final SortedMap<K, Set<V>> map;
  private int range;
  private boolean recalculateRange;
  private final TreeMultiMap<K, V> parent;
  
  public TreeMultiMap()
  {
    map = new TreeMap();
    range = 0;
    recalculateRange = false;
    parent = null;
  }
  



  public TreeMultiMap(Comparator<? super K> c)
  {
    map = new TreeMap(c);
    range = 0;
    recalculateRange = false;
    parent = null;
  }
  



  public TreeMultiMap(Map<? extends K, ? extends V> m)
  {
    this();
    putAll(m);
  }
  







  private TreeMultiMap(SortedMap<K, Set<V>> subMap, TreeMultiMap<K, V> parent)
  {
    map = subMap;
    


    range = -1;
    recalculateRange = true;
    this.parent = parent;
  }
  




  public Map<K, Set<V>> asMap()
  {
    return map;
  }
  


  public void clear()
  {
    map.clear();
    if (parent != null) {
      int curRange = range();
      parent.updateParentRange(-curRange);
    }
    range = 0;
  }
  



  private void updateParentRange(int valueDifference)
  {
    range += valueDifference;
    if (parent != null) {
      parent.updateParentRange(valueDifference);
    }
  }
  


  public Comparator<? super K> comparator()
  {
    return map.comparator();
  }
  


  public boolean containsKey(Object key)
  {
    return map.containsKey(key);
  }
  


  public boolean containsMapping(Object key, Object value)
  {
    Set<V> s = (Set)map.get(key);
    return (s != null) && (s.contains(value));
  }
  


  public boolean containsValue(Object value)
  {
    for (Set<V> s : map.values()) {
      if (s.contains(value)) {
        return true;
      }
    }
    return false;
  }
  


  public Set<Map.Entry<K, V>> entrySet()
  {
    return new EntryView();
  }
  


  public K firstKey()
  {
    return map.firstKey();
  }
  


  public Set<V> get(Object key)
  {
    return (Set)map.get(key);
  }
  


  public SortedMultiMap<K, V> headMap(K toKey)
  {
    return new TreeMultiMap(map.headMap(toKey), this);
  }
  


  public boolean isEmpty()
  {
    return map.isEmpty();
  }
  


  public Set<K> keySet()
  {
    return map.keySet();
  }
  


  public K lastKey()
  {
    return map.lastKey();
  }
  


  public boolean put(K key, V value)
  {
    Set<V> values = (Set)map.get(key);
    if (values == null) {
      values = new HashSet();
      map.put(key, values);
    }
    boolean added = values.add(value);
    if (added) {
      range += 1;
      if (parent != null) {
        parent.updateParentRange(1);
      }
    }
    return added;
  }
  


  public void putAll(Map<? extends K, ? extends V> m)
  {
    for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
      put(e.getKey(), e.getValue());
    }
  }
  


  public void putAll(MultiMap<? extends K, ? extends V> m)
  {
    for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
      put(e.getKey(), e.getValue());
    }
  }
  




  public boolean putMany(K key, Collection<V> values)
  {
    if (values.isEmpty())
      return false;
    Set<V> vals = (Set)map.get(key);
    if (vals == null) {
      vals = new HashSet();
      map.put(key, vals);
    }
    int oldSize = vals.size();
    boolean added = vals.addAll(values);
    range += vals.size() - oldSize;
    return added;
  }
  





  public int range()
  {
    if (recalculateRange) {
      recalculateRange = false;
      range = 0;
      for (V v : values())
        range += 1;
    }
    return range;
  }
  


  public Set<V> remove(Object key)
  {
    Set<V> v = (Set)map.remove(key);
    if (v != null)
      range -= v.size();
    if (parent != null) {
      parent.updateParentRange(-v.size());
    }
    
    return v;
  }
  


  public boolean remove(Object key, Object value)
  {
    Set<V> values = (Set)map.get(key);
    
    if (values == null)
      return false;
    boolean removed = values.remove(value);
    if (removed) {
      range -= 1;
      if (parent != null) {
        parent.updateParentRange(-1);
      }
    }
    

    if (values.size() == 0)
      map.remove(key);
    return removed;
  }
  


  public int size()
  {
    return map.size();
  }
  


  public SortedMultiMap<K, V> subMap(K fromKey, K toKey)
  {
    return new TreeMultiMap(map.subMap(fromKey, toKey), this);
  }
  


  public SortedMultiMap<K, V> tailMap(K fromKey)
  {
    return new TreeMultiMap(map.tailMap(fromKey), this);
  }
  


  public String toString()
  {
    Iterator<Map.Entry<K, Set<V>>> it = map.entrySet().iterator();
    if (!it.hasNext()) {
      return "{}";
    }
    
    StringBuilder sb = new StringBuilder();
    sb.append('{');
    for (;;) {
      Map.Entry<K, Set<V>> e = (Map.Entry)it.next();
      K key = e.getKey();
      Set<V> values = (Set)e.getValue();
      sb.append(key == this ? "(this Map)" : key);
      sb.append("=[");
      Iterator<V> it2 = values.iterator();
      while (it2.hasNext()) {
        V value = it2.next();
        sb.append(value == this ? "(this Map)" : value);
        if (it2.hasNext()) {
          sb.append(",");
        }
      }
      sb.append("]");
      if (!it.hasNext())
        return '}';
      sb.append(", ");
    }
  }
  




  public Collection<V> values()
  {
    return new ValuesView();
  }
  


  public Collection<Set<V>> valueSets()
  {
    return map.values();
  }
  


  class ValuesView
    extends AbstractCollection<V>
    implements Serializable
  {
    private static final long serialVersionUID = 1L;
    


    public ValuesView() {}
    

    public void clear()
    {
      map.clear();
    }
    


    public boolean contains(Object o)
    {
      return containsValue(o);
    }
    



    public Iterator<V> iterator()
    {
      Collection<Iterator<V>> iterators = 
        new ArrayList(size());
      for (Set<V> s : map.values()) {
        iterators.add(s.iterator());
      }
      



      return new CombinedIterator(iterators);
    }
    


    public int size()
    {
      return range();
    }
  }
  


  class EntryView
    extends AbstractSet<Map.Entry<K, V>>
    implements Serializable
  {
    private static final long serialVersionUID = 1L;
    


    public EntryView() {}
    


    public void clear()
    {
      map.clear();
    }
    


    public boolean contains(Object o)
    {
      if ((o instanceof Map.Entry)) {
        Map.Entry<?, ?> e = (Map.Entry)o;
        Set<V> vals = get(e.getKey());
        return vals.contains(e.getValue());
      }
      return false;
    }
    


    public Iterator<Map.Entry<K, V>> iterator()
    {
      return new TreeMultiMap.EntryIterator(TreeMultiMap.this);
    }
    


    public int size()
    {
      return range();
    }
  }
  



  class EntryIterator
    implements Iterator<Map.Entry<K, V>>, Serializable
  {
    private static final long serialVersionUID = 1L;
    

    K curKey;
    

    Iterator<V> curValues;
    

    Iterator<Map.Entry<K, Set<V>>> multiMapIterator = map.entrySet().iterator();
    public EntryIterator() { Map.Entry<K, Set<V>> e; if (multiMapIterator.hasNext()) {
        e = (Map.Entry)multiMapIterator.next();
        curKey = e.getKey();
        curValues = ((Set)e.getValue()).iterator();
      }
      
      advance();
    }
    
    Map.Entry<K, V> next;
    private void advance()
    {
      if ((curValues != null) && (curValues.hasNext())) {
        next = new MultiMapEntry(curKey, curValues.next());

      }
      else if (multiMapIterator.hasNext()) {
        Map.Entry<K, Set<V>> e = (Map.Entry)multiMapIterator.next();
        curKey = e.getKey();
        curValues = ((Set)e.getValue()).iterator();
        

        next = new MultiMapEntry(curKey, curValues.next());
      } else {
        next = null;
      }
    }
    
    public boolean hasNext() {
      return next != null;
    }
    
    public Map.Entry<K, V> next() {
      Map.Entry<K, V> e = next;
      previous = e;
      advance();
      return e;
    }
    
    public void remove() {
      if (previous == null) {
        throw new IllegalStateException(
          "No previous element to remove");
      }
      remove(previous.getKey(), previous.getValue());
      previous = null;
    }
    

    Map.Entry<K, V> previous;
    private class MultiMapEntry
      extends AbstractMap.SimpleEntry<K, V>
      implements Serializable
    {
      private static final long serialVersionUID = 1L;
      
      public MultiMapEntry(V key)
      {
        super(value);
      }
      
      public V setValue(V value) {
        Set<V> values = get(getKey());
        values.remove(getValue());
        values.add(value);
        return super.setValue(value);
      }
    }
  }
}
