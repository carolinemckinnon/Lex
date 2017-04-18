package edu.ucla.sspace.util.primitive;

import edu.ucla.sspace.util.MultiMap;
import gnu.trove.TDecorators;
import gnu.trove.TIntCollection;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.procedure.TIntProcedure;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;










































public class IntIntHashMultiMap
  implements IntIntMultiMap
{
  private static final long serialVersionUID = 1L;
  private final TIntObjectMap<IntSet> map;
  private int range;
  
  public IntIntHashMultiMap()
  {
    map = new TIntObjectHashMap();
    range = 0;
  }
  



  public IntIntHashMultiMap(Map<Integer, Integer> m)
  {
    this();
    putAll(m);
  }
  

  public Map<Integer, Set<Integer>> asMap()
  {
    Map<Integer, ?> m = TDecorators.wrap(map);
    
    Map<Integer, Set<Integer>> m2 = m;
    return m2;
  }
  


  public void clear()
  {
    map.clear();
    range = 0;
  }
  


  public boolean containsKey(int key)
  {
    return map.containsKey(key);
  }
  


  public boolean containsKey(Object key)
  {
    if (!(key instanceof Integer))
      return false;
    Integer k = (Integer)key;
    return containsKey(k.intValue());
  }
  


  public boolean containsMapping(int key, int value)
  {
    IntSet s = (IntSet)map.get(key);
    return (s != null) && (s.contains(value));
  }
  


  public boolean containsMapping(Object key, Object value)
  {
    if ((!(key instanceof Integer)) || (!(value instanceof Integer)))
      return false;
    Integer i = (Integer)key;
    Integer j = (Integer)value;
    return containsMapping(i.intValue(), j.intValue());
  }
  


  public boolean containsValue(int value)
  {
    for (IntSet s : map.valueCollection()) {
      if (s.contains(value)) {
        return true;
      }
    }
    return false;
  }
  



  public boolean containsValue(Object value)
  {
    if (!(value instanceof Integer))
      return false;
    Integer v = (Integer)value;
    return containsValue(v.intValue());
  }
  


  public Set<Map.Entry<Integer, Integer>> entrySet()
  {
    throw new Error();
  }
  



  public IntSet get(int key)
  {
    IntSet vals = (IntSet)map.get(key);
    return vals == null ? new TroveIntSet() : vals;
  }
  


  public IntSet get(Object key)
  {
    if (!(key instanceof Integer))
      return PrimitiveCollections.emptyIntSet();
    Integer k = (Integer)key;
    return get(k.intValue());
  }
  


  public boolean isEmpty()
  {
    return map.isEmpty();
  }
  


  public IntSet keySet()
  {
    return TroveIntSet.wrap(map.keySet());
  }
  


  public boolean put(int key, int value)
  {
    IntSet values = (IntSet)map.get(key);
    if (values == null) {
      values = new TroveIntSet();
      map.put(key, values);
    }
    boolean added = values.add(value);
    if (added) {
      range += 1;
    }
    return added;
  }
  


  public boolean put(Integer key, Integer value)
  {
    return put(key.intValue(), value.intValue());
  }
  


  public void putAll(Map<? extends Integer, ? extends Integer> m)
  {
    for (Map.Entry<? extends Integer, ? extends Integer> e : m.entrySet()) {
      put((Integer)e.getKey(), (Integer)e.getValue());
    }
  }
  


  public void putAll(MultiMap<? extends Integer, ? extends Integer> m)
  {
    for (Map.Entry<? extends Integer, ? extends Integer> e : m.entrySet()) {
      put((Integer)e.getKey(), (Integer)e.getValue());
    }
  }
  




  public void putAll(IntIntMultiMap m)
  {
    IntIterator keys = m.keySet().iterator();
    while (keys.hasNext()) {
      int key = keys.nextInt();
      IntSet values = m.get(key);
      putMany(key, values);
    }
  }
  




  public boolean putMany(int key, Collection<Integer> values)
  {
    if (values.isEmpty())
      return false;
    IntSet vals = (IntSet)map.get(key);
    if (vals == null) {
      vals = new TroveIntSet(values.size());
      map.put(key, vals);
    }
    int oldSize = vals.size();
    boolean added = vals.addAll(values);
    range += vals.size() - oldSize;
    return added;
  }
  


  public boolean putMany(Integer key, Collection<Integer> values)
  {
    return putMany(key.intValue(), values);
  }
  




  public boolean putMany(int key, IntCollection values)
  {
    if (values.isEmpty())
      return false;
    IntSet vals = (IntSet)map.get(key);
    if (vals == null) {
      vals = new TroveIntSet(values.size());
      map.put(key, vals);
    }
    int oldSize = vals.size();
    boolean added = vals.addAll(values);
    range += vals.size() - oldSize;
    return added;
  }
  


  public boolean putMany(Integer key, IntCollection values)
  {
    return putMany(key.intValue(), values);
  }
  


  public int range()
  {
    return range;
  }
  


  public IntSet remove(int key)
  {
    IntSet v = (IntSet)map.remove(key);
    if (v != null)
      range -= v.size();
    return v;
  }
  


  public IntSet remove(Integer key)
  {
    return remove(key.intValue());
  }
  


  public boolean remove(int key, int value)
  {
    IntSet values = (IntSet)map.get(key);
    
    if (values == null)
      return false;
    boolean removed = values.remove(value);
    if (removed) {
      range -= 1;
    }
    
    if (values.size() == 0)
      map.remove(key);
    return removed;
  }
  


  public boolean remove(Object key, Object value)
  {
    if ((!(key instanceof Integer)) || (!(value instanceof Integer)))
      return false;
    Integer i = (Integer)key;
    Integer j = (Integer)value;
    return remove(i.intValue(), j.intValue());
  }
  


  public int size()
  {
    return map.size();
  }
  


  public String toString()
  {
    TIntObjectIterator<IntSet> it = map.iterator();
    if (!it.hasNext()) {
      return "{}";
    }
    
    StringBuilder sb = new StringBuilder();
    sb.append('{');
    for (;;) {
      int key = it.key();
      IntSet values = (IntSet)it.value();
      sb.append(key);
      sb.append("=[");
      IntIterator it2 = values.iterator();
      while (it2.hasNext()) {
        int value = ((Integer)it2.next()).intValue();
        sb.append(value);
        if (it2.hasNext()) {
          sb.append(",");
        }
      }
      sb.append("]");
      if (!it.hasNext())
        return '}';
      sb.append(", ");
      it.advance();
    }
  }
  




  public IntCollection values()
  {
    throw new Error();
  }
  





  public Collection<Set<Integer>> valueSets()
  {
    Collection<?> c = map.valueCollection();
    
    Collection<Set<Integer>> c2 = c;
    return c2;
  }
  

  class ValuesView
    implements TIntCollection, Serializable
  {
    private static final long serialVersionUID = 1L;
    

    public ValuesView() {}
    

    public boolean add(int i)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean addAll(Collection<? extends Integer> c) {
      throw new UnsupportedOperationException();
    }
    
    public boolean addAll(int[] array) {
      throw new UnsupportedOperationException();
    }
    
    public boolean addAll(TIntCollection c) {
      throw new UnsupportedOperationException();
    }
    
    public void clear() {
      map.clear();
    }
    
    public boolean contains(int i) {
      return containsValue(i);
    }
    
    public boolean containsAll(int[] arr) {
      for (int i : arr)
        if (!containsValue(i))
          return false;
      return true;
    }
    
    public boolean containsAll(Collection<?> c) {
      for (Object o : c) {
        if (!(o instanceof Integer))
          return false;
        Integer i = (Integer)o;
        if (!containsValue(i))
          return false;
      }
      return true;
    }
    
    public boolean containsAll(TIntCollection c) {
      throw new Error();
    }
    
    public boolean forEach(TIntProcedure p) {
      throw new Error();
    }
    
    public int getNoEntryValue() {
      throw new Error();
    }
    
    public int hashCode() {
      throw new Error();
    }
    
    public boolean isEmpty() {
      return IntIntHashMultiMap.this.isEmpty();
    }
    
    public TIntIterator iterator() {
      throw new Error();
    }
    
    public boolean remove(int i) {
      throw new UnsupportedOperationException();
    }
    
    public boolean removeAll(Collection<?> c) {
      throw new Error();
    }
    
    public boolean removeAll(TIntCollection c) {
      throw new Error();
    }
    
    public boolean removeAll(int[] c) {
      throw new Error();
    }
    
    public boolean retainAll(Collection<?> c) {
      throw new Error();
    }
    
    public boolean retainAll(TIntCollection c) {
      throw new Error();
    }
    
    public boolean retainAll(int[] c) {
      throw new Error();
    }
    
    public int size() {
      return range();
    }
    
    public int[] toArray() {
      throw new Error();
    }
    
    public int[] toArray(int[] dest) {
      throw new Error();
    }
  }
}
