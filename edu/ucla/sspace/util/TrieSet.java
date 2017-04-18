package edu.ucla.sspace.util;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;












































public class TrieSet
  extends AbstractSet<String>
{
  private final TrieMap<Object> backingMap;
  
  public TrieSet()
  {
    backingMap = new TrieMap();
  }
  


  public TrieSet(Collection<String> c)
  {
    this();
    for (String s : c) {
      backingMap.put(s, Boolean.valueOf(true));
    }
  }
  

  public boolean add(String s)
  {
    return backingMap.put(s, Boolean.valueOf(true)) == null;
  }
  


  public void clear()
  {
    backingMap.clear();
  }
  


  public boolean contains(Object o)
  {
    return backingMap.containsKey(o);
  }
  


  public boolean isEmpty()
  {
    return backingMap.isEmpty();
  }
  


  public Iterator<String> iterator()
  {
    return backingMap.keySet().iterator();
  }
  


  public boolean remove(Object o)
  {
    return backingMap.remove(o) != null;
  }
  


  public int size()
  {
    return backingMap.size();
  }
  
  public String toString() {
    return backingMap.keySet().toString();
  }
}
