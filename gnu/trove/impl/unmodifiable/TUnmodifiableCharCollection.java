package gnu.trove.impl.unmodifiable;

import gnu.trove.TCharCollection;
import gnu.trove.iterator.TCharIterator;
import gnu.trove.procedure.TCharProcedure;
import java.io.Serializable;
import java.util.Collection;










































public class TUnmodifiableCharCollection
  implements TCharCollection, Serializable
{
  private static final long serialVersionUID = 1820017752578914078L;
  final TCharCollection c;
  
  public TUnmodifiableCharCollection(TCharCollection c)
  {
    if (c == null)
      throw new NullPointerException();
    this.c = c;
  }
  
  public int size() { return c.size(); }
  public boolean isEmpty() { return c.isEmpty(); }
  public boolean contains(char o) { return c.contains(o); }
  public char[] toArray() { return c.toArray(); }
  public char[] toArray(char[] a) { return c.toArray(a); }
  public String toString() { return c.toString(); }
  public char getNoEntryValue() { return c.getNoEntryValue(); }
  public boolean forEach(TCharProcedure procedure) { return c.forEach(procedure); }
  
  public TCharIterator iterator() {
    new TCharIterator() {
      TCharIterator i = c.iterator();
      
      public boolean hasNext() { return i.hasNext(); }
      public char next() { return i.next(); }
      public void remove() { throw new UnsupportedOperationException(); }
    };
  }
  
  public boolean add(char e) { throw new UnsupportedOperationException(); }
  public boolean remove(char o) { throw new UnsupportedOperationException(); }
  
  public boolean containsAll(Collection<?> coll) { return c.containsAll(coll); }
  public boolean containsAll(TCharCollection coll) { return c.containsAll(coll); }
  public boolean containsAll(char[] array) { return c.containsAll(array); }
  
  public boolean addAll(TCharCollection coll) { throw new UnsupportedOperationException(); }
  public boolean addAll(Collection<? extends Character> coll) { throw new UnsupportedOperationException(); }
  public boolean addAll(char[] array) { throw new UnsupportedOperationException(); }
  
  public boolean removeAll(Collection<?> coll) { throw new UnsupportedOperationException(); }
  public boolean removeAll(TCharCollection coll) { throw new UnsupportedOperationException(); }
  public boolean removeAll(char[] array) { throw new UnsupportedOperationException(); }
  
  public boolean retainAll(Collection<?> coll) { throw new UnsupportedOperationException(); }
  public boolean retainAll(TCharCollection coll) { throw new UnsupportedOperationException(); }
  public boolean retainAll(char[] array) { throw new UnsupportedOperationException(); }
  
  public void clear() { throw new UnsupportedOperationException(); }
}
