package edu.ucla.sspace.util;

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;





































































public class IntegerMap<V>
  extends AbstractMap<Integer, V>
  implements SparseArray<V>
{
  private static final long serialVersionUID = 1L;
  int[] keyIndices;
  Object[] values;
  
  public IntegerMap()
  {
    keyIndices = new int[0];
    values = new Object[0];
  }
  




  public IntegerMap(Map<Integer, ? extends V> m)
  {
    keyIndices = new int[m.size()];
    values = new Object[m.size()];
    


    Iterator<Integer> it = m.keySet().iterator();
    for (int i = 0; i < m.size(); i++) {
      keyIndices[i] = ((Integer)it.next()).intValue();
    }
    Arrays.sort(keyIndices);
    

    for (int i = 0; i < keyIndices.length; i++) {
      Integer key = Integer.valueOf(keyIndices[i]);
      values[i] = m.get(key);
    }
  }
  





  public IntegerMap(V[] array)
  {
    int nonNull = 0;
    for (int i = 0; i < array.length; i++) {
      if (array[i] != null) {
        nonNull++;
      }
    }
    keyIndices = new int[nonNull];
    values = new Object[nonNull];
    int keyIndex = 0;
    for (int i = 0; i < array.length; i++) {
      if (array[i] != null) {
        keyIndices[keyIndex] = i;
        values[(keyIndex++)] = array[i];
      }
    }
  }
  


  public int cardinality()
  {
    return keyIndices.length;
  }
  



  private int checkKey(Object key)
  {
    if (key == null)
      throw new NullPointerException("key cannot be null");
    if (!(key instanceof Integer)) {
      throw new IllegalArgumentException("key must be an Integer");
    }
    
    return ((Integer)key).intValue();
  }
  



  public void clear()
  {
    keyIndices = new int[0];
    values = new Object[0];
  }
  


  public boolean containsKey(Object key)
  {
    int k = checkKey(key);
    int index = Arrays.binarySearch(keyIndices, k);
    return index >= 0;
  }
  
  public boolean containsValue(Object value) {
    for (Object o : values) {
      if ((o == value) || ((o != null) && (o.equals(value)))) {
        return true;
      }
    }
    return false;
  }
  


  public Set<Map.Entry<Integer, V>> entrySet()
  {
    return new EntrySet();
  }
  



  public V get(Object key)
  {
    int k = checkKey(key);
    return get(k);
  }
  




  public V get(int index)
  {
    int pos = Arrays.binarySearch(keyIndices, index);
    return pos >= 0 ? values[pos] : null;
  }
  


  public int[] getElementIndices()
  {
    return keyIndices;
  }
  


  public Set<Integer> keySet()
  {
    return new KeySet();
  }
  
  public int length() {
    return Integer.MAX_VALUE;
  }
  











  public V put(Integer key, V value)
  {
    int k = checkKey(key);
    int index = Arrays.binarySearch(keyIndices, k);
    
    if (index >= 0) {
      V old = values[index];
      values[index] = value;
      return old;
    }
    
    int newIndex = 0 - (index + 1);
    Object[] newValues = Arrays.copyOf(values, values.length + 1);
    int[] newIndices = Arrays.copyOf(keyIndices, values.length + 1);
    

    for (int i = newIndex; i < values.length; i++) {
      newValues[(i + 1)] = values[i];
      newIndices[(i + 1)] = keyIndices[i];
    }
    

    newValues[newIndex] = value;
    newIndices[newIndex] = k;
    

    values = newValues;
    keyIndices = newIndices;
    
    return null;
  }
  











  public V remove(Object key)
  {
    int k = checkKey(key);
    int index = Arrays.binarySearch(keyIndices, k);
    
    if (index >= 0) {
      V old = values[index];
      
      Object[] newValues = Arrays.copyOf(values, values.length - 1);
      int[] newIndices = Arrays.copyOf(keyIndices, keyIndices.length - 1);
      

      for (int i = index; i < values.length - 1; i++) {
        newValues[i] = values[(i + 1)];
        newIndices[i] = keyIndices[(i + 1)];
      }
      

      values = newValues;
      keyIndices = newIndices;
      return old;
    }
    
    return null;
  }
  


  public void set(int index, V obj)
  {
    put(Integer.valueOf(index), obj);
  }
  


  public int size()
  {
    return keyIndices.length;
  }
  



  public <E> E[] toArray(E[] array)
  {
    for (int i = 0; i < keyIndices.length; i++) {
      int index = keyIndices[i];
      if (index >= 0)
      {
        if (index >= array.length)
          break;
        array[keyIndices[i]] = values[i];
      } }
    return array;
  }
  


  public Collection<V> values()
  {
    return new Values(null);
  }
  
  private class EntryIterator extends IntegerMap<V>.IntMapIterator<Map.Entry<Integer, V>> {
    private EntryIterator() { super(); }
    
    public Map.Entry<Integer, V> next() {
      return nextEntry();
    }
  }
  
  private class KeyIterator extends IntegerMap<V>.IntMapIterator<Integer> {
    private KeyIterator() { super(); }
    
    public Integer next() {
      return (Integer)nextEntry().getKey();
    }
  }
  
  private class ValueIterator extends IntegerMap<V>.IntMapIterator<V> {
    private ValueIterator() { super(); }
    
    public V next() {
      return nextEntry().getValue();
    }
  }
  
  abstract class IntMapIterator<E> implements Iterator<E>
  {
    private int next;
    
    public IntMapIterator()
    {
      next = 0;
    }
    
    public boolean hasNext() {
      return next < size();
    }
    
    public Map.Entry<Integer, V> nextEntry()
    {
      if (next >= size()) {
        throw new NoSuchElementException("no further elements");
      }
      int key = keyIndices[next];
      V value = values[next];
      next += 1;
      return new IntegerMap.IntEntry(IntegerMap.this, key, value);
    }
    

    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
  
  class IntEntry
    extends AbstractMap.SimpleEntry<Integer, V>
  {
    private static final long serialVersionUID = 1L;
    
    public IntEntry(V key)
    {
      super(value);
    }
    
    public V setValue(V newValue) {
      return put((Integer)getKey(), newValue);
    }
  }
  
  class EntrySet extends AbstractSet<Map.Entry<Integer, V>> {
    private static final long serialVersionUID = 1L;
    
    EntrySet() {}
    
    public void clear() { IntegerMap.this.clear(); }
    
    public boolean contains(Object o)
    {
      if ((o instanceof Map.Entry)) {
        Map.Entry e = (Map.Entry)o;
        Object key = e.getKey();
        Object val = e.getValue();
        Object mapVal = get(key);
        return (mapVal == val) || ((val != null) && (val.equals(mapVal)));
      }
      return false;
    }
    
    public Iterator<Map.Entry<Integer, V>> iterator() {
      return new IntegerMap.EntryIterator(IntegerMap.this, null);
    }
    
    public int size() {
      return IntegerMap.this.size();
    }
  }
  
  class KeySet extends AbstractSet<Integer>
  {
    private static final long serialVersionUID = 1L;
    
    public KeySet() {}
    
    public void clear() {
      IntegerMap.this.clear();
    }
    
    public boolean contains(Object o) {
      return containsKey(o);
    }
    
    public Iterator<Integer> iterator() {
      return new IntegerMap.KeyIterator(IntegerMap.this, null);
    }
    
    public boolean remove(Object o) {
      return remove(o) != null;
    }
    
    public int size() {
      return IntegerMap.this.size();
    }
  }
  
  private class Values
    extends AbstractCollection<V>
  {
    private static final long serialVersionUID = 1L;
    
    private Values() {}
    
    public void clear()
    {
      IntegerMap.this.clear();
    }
    
    public boolean contains(Object o) {
      return containsValue(o);
    }
    
    public Iterator<V> iterator() {
      return new IntegerMap.ValueIterator(IntegerMap.this, null);
    }
    
    public int size() {
      return IntegerMap.this.size();
    }
  }
}
