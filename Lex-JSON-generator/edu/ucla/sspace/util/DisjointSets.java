package edu.ucla.sspace.util;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;





























public class DisjointSets<T>
  extends AbstractSet<T>
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private final List<Set<T>> sets;
  
  public DisjointSets(Set<T>... sets)
  {
    if (sets == null)
      throw new NullPointerException("Sets cannot be null");
    this.sets = new ArrayList();
    for (Set<T> s : sets) {
      if (s == null)
        throw new NullPointerException("Cannot wrap null set");
      this.sets.add(s);
    }
  }
  
  public DisjointSets(Collection<? extends Set<T>> sets) {
    if (sets == null)
      throw new NullPointerException("Sets cannot be null");
    this.sets = new ArrayList();
    for (Set<T> s : sets) {
      if (s == null)
        throw new NullPointerException("Cannot wrap null set");
      this.sets.add(s);
    }
  }
  





  public void append(Set<T> set)
  {
    if (set == null)
      throw new NullPointerException("set cannot be null");
    sets.add(set);
  }
  



  public boolean contains(Object o)
  {
    for (Set<T> s : sets)
      if (s.contains(o))
        return true;
    return false;
  }
  


  public void clear()
  {
    for (Set<T> s : sets) {
      s.clear();
    }
  }
  

  public Iterator<T> iterator()
  {
    List<Iterator<T>> iters = new ArrayList(sets.size());
    for (Set<T> s : sets)
      iters.add(s.iterator());
    return new CombinedIterator(iters);
  }
  


  public boolean isEmpty()
  {
    for (Set<T> s : sets)
      if (!s.isEmpty())
        return false;
    return true;
  }
  





  public boolean remove(Object o)
  {
    for (Set<T> s : sets)
      if (s.remove(o))
        return true;
    return false;
  }
  



  public int size()
  {
    int size = 0;
    for (Set<T> s : sets)
      size += s.size();
    return size;
  }
}
