package edu.ucla.sspace.util;

import java.io.Serializable;
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




























































public class CharMap<V>
  extends AbstractMap<Character, V>
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  char[] keyIndices;
  Object[] values;
  
  public CharMap()
  {
    keyIndices = new char[0];
    values = new Object[0];
  }
  




  public CharMap(Map<Character, ? extends V> m)
  {
    keyIndices = new char[m.size()];
    values = new Object[m.size()];
    


    Iterator<Character> it = m.keySet().iterator();
    for (int i = 0; i < m.size(); i++) {
      keyIndices[i] = ((Character)it.next()).charValue();
    }
    Arrays.sort(keyIndices);
    

    for (int i = 0; i < keyIndices.length; i++) {
      values[i] = m.get(Character.valueOf(keyIndices[i]));
    }
  }
  



  private char checkKey(Object key)
  {
    if (key == null)
      throw new NullPointerException("key cannot be null");
    if (!(key instanceof Character)) {
      throw new IllegalArgumentException("key must be an Character");
    }
    
    return ((Character)key).charValue();
  }
  



  public void clear()
  {
    keyIndices = new char[0];
    values = new Object[0];
  }
  


  public boolean containsKey(Object key)
  {
    char k = checkKey(key);
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
  


  public Set<Map.Entry<Character, V>> entrySet()
  {
    return new EntrySet();
  }
  



  public V get(Object key)
  {
    char k = checkKey(key);
    return get(k);
  }
  





  public V get(char key)
  {
    int pos = Arrays.binarySearch(keyIndices, key);
    return pos >= 0 ? values[pos] : null;
  }
  


  public Set<Character> keySet()
  {
    return new KeySet();
  }
  









  public V put(Character key, V value)
  {
    char k = checkKey(key);
    return put(k, value);
  }
  










  public V put(char key, V value)
  {
    char k = key;
    int index = Arrays.binarySearch(keyIndices, k);
    
    if (index >= 0) {
      V old = values[index];
      values[index] = value;
      return old;
    }
    
    int newIndex = 0 - (index + 1);
    Object[] newValues = Arrays.copyOf(values, values.length + 1);
    char[] newIndices = Arrays.copyOf(keyIndices, values.length + 1);
    

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
    char k = checkKey(key);
    return remove(k);
  }
  










  public V remove(char key)
  {
    char k = key;
    int index = Arrays.binarySearch(keyIndices, k);
    
    if (index >= 0) {
      V old = values[index];
      
      Object[] newValues = Arrays.copyOf(values, values.length - 1);
      char[] newIndices = Arrays.copyOf(keyIndices, keyIndices.length - 1);
      

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
  


  public int size()
  {
    return keyIndices.length;
  }
  


  public Collection<V> values()
  {
    return new Values(null);
  }
  
  private class EntryIterator extends CharMap<V>.CharMapIterator<Map.Entry<Character, V>> {
    private EntryIterator() { super(); }
    
    public Map.Entry<Character, V> next() {
      return nextEntry();
    }
  }
  
  private class KeyIterator extends CharMap<V>.CharMapIterator<Character> {
    private KeyIterator() { super(); }
    
    public Character next() {
      return (Character)nextEntry().getKey();
    }
  }
  
  private class ValueIterator extends CharMap<V>.CharMapIterator<V> {
    private ValueIterator() { super(); }
    
    public V next() {
      return nextEntry().getValue();
    }
  }
  
  abstract class CharMapIterator<E> implements Iterator<E>
  {
    private int next;
    
    public CharMapIterator()
    {
      next = 0;
    }
    
    public boolean hasNext() {
      return next < size();
    }
    
    public Map.Entry<Character, V> nextEntry()
    {
      if (next >= size()) {
        throw new NoSuchElementException("no further elements");
      }
      char key = keyIndices[next];
      V value = values[next];
      next += 1;
      return new CharMap.CharEntry(CharMap.this, key, value);
    }
    

    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
  
  class CharEntry
    extends AbstractMap.SimpleEntry<Character, V>
  {
    private static final long serialVersionUID = 1L;
    
    public CharEntry(V key)
    {
      super(value);
    }
    
    public V setValue(V newValue) {
      return put((Character)getKey(), newValue);
    }
  }
  
  class EntrySet extends AbstractSet<Map.Entry<Character, V>> {
    private static final long serialVersionUID = 1L;
    
    EntrySet() {}
    
    public void clear() { CharMap.this.clear(); }
    
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
    
    public Iterator<Map.Entry<Character, V>> iterator() {
      return new CharMap.EntryIterator(CharMap.this, null);
    }
    
    public int size() {
      return CharMap.this.size();
    }
  }
  
  class KeySet extends AbstractSet<Character>
  {
    private static final long serialVersionUID = 1L;
    
    public KeySet() {}
    
    public void clear() {
      CharMap.this.clear();
    }
    
    public boolean contains(Object o) {
      return containsKey(o);
    }
    
    public Iterator<Character> iterator() {
      return new CharMap.KeyIterator(CharMap.this, null);
    }
    
    public boolean remove(Object o) {
      return remove(o) != null;
    }
    
    public int size() {
      return CharMap.this.size();
    }
  }
  
  private class Values
    extends AbstractCollection<V>
  {
    private static final long serialVersionUID = 1L;
    
    private Values() {}
    
    public void clear()
    {
      CharMap.this.clear();
    }
    
    public boolean contains(Object o) {
      return containsValue(o);
    }
    
    public Iterator<V> iterator() {
      return new CharMap.ValueIterator(CharMap.this, null);
    }
    
    public int size() {
      return CharMap.this.size();
    }
  }
}
