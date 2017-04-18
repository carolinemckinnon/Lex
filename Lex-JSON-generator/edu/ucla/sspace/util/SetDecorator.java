package edu.ucla.sspace.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;



































public class SetDecorator<T>
  implements Set<T>, Serializable
{
  private static final long serialVersionUID = 1L;
  protected final Set<T> set;
  
  public SetDecorator(Set<T> set)
  {
    if (set == null)
      throw new NullPointerException();
    this.set = set;
  }
  
  public boolean add(T e) {
    return set.add(e);
  }
  
  public boolean addAll(Collection<? extends T> c) {
    return set.addAll(c);
  }
  
  public void clear() {
    set.clear();
  }
  
  public boolean contains(Object o) {
    return set.contains(o);
  }
  
  public boolean containsAll(Collection<?> c) {
    return set.containsAll(c);
  }
  
  public boolean equals(Object o) {
    return set.equals(o);
  }
  
  public int hashCode() {
    return set.hashCode();
  }
  
  public boolean isEmpty() {
    return set.isEmpty();
  }
  
  public Iterator<T> iterator() {
    return set.iterator();
  }
  
  public boolean remove(Object o) {
    return set.remove(o);
  }
  
  public boolean removeAll(Collection<?> c) {
    return set.removeAll(c);
  }
  
  public boolean retainAll(Collection<?> c) {
    return set.retainAll(c);
  }
  
  public int size() {
    return set.size();
  }
  
  public Object[] toArray() {
    return set.toArray();
  }
  
  public <E> E[] toArray(E[] a) {
    return set.toArray(a);
  }
  
  public String toString() {
    return set.toString();
  }
}
