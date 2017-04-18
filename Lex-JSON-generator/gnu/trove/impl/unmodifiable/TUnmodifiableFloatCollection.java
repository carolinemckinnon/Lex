package gnu.trove.impl.unmodifiable;

import gnu.trove.TFloatCollection;
import gnu.trove.iterator.TFloatIterator;
import gnu.trove.procedure.TFloatProcedure;
import java.io.Serializable;
import java.util.Collection;










































public class TUnmodifiableFloatCollection
  implements TFloatCollection, Serializable
{
  private static final long serialVersionUID = 1820017752578914078L;
  final TFloatCollection c;
  
  public TUnmodifiableFloatCollection(TFloatCollection c)
  {
    if (c == null)
      throw new NullPointerException();
    this.c = c;
  }
  
  public int size() { return c.size(); }
  public boolean isEmpty() { return c.isEmpty(); }
  public boolean contains(float o) { return c.contains(o); }
  public float[] toArray() { return c.toArray(); }
  public float[] toArray(float[] a) { return c.toArray(a); }
  public String toString() { return c.toString(); }
  public float getNoEntryValue() { return c.getNoEntryValue(); }
  public boolean forEach(TFloatProcedure procedure) { return c.forEach(procedure); }
  
  public TFloatIterator iterator() {
    new TFloatIterator() {
      TFloatIterator i = c.iterator();
      
      public boolean hasNext() { return i.hasNext(); }
      public float next() { return i.next(); }
      public void remove() { throw new UnsupportedOperationException(); }
    };
  }
  
  public boolean add(float e) { throw new UnsupportedOperationException(); }
  public boolean remove(float o) { throw new UnsupportedOperationException(); }
  
  public boolean containsAll(Collection<?> coll) { return c.containsAll(coll); }
  public boolean containsAll(TFloatCollection coll) { return c.containsAll(coll); }
  public boolean containsAll(float[] array) { return c.containsAll(array); }
  
  public boolean addAll(TFloatCollection coll) { throw new UnsupportedOperationException(); }
  public boolean addAll(Collection<? extends Float> coll) { throw new UnsupportedOperationException(); }
  public boolean addAll(float[] array) { throw new UnsupportedOperationException(); }
  
  public boolean removeAll(Collection<?> coll) { throw new UnsupportedOperationException(); }
  public boolean removeAll(TFloatCollection coll) { throw new UnsupportedOperationException(); }
  public boolean removeAll(float[] array) { throw new UnsupportedOperationException(); }
  
  public boolean retainAll(Collection<?> coll) { throw new UnsupportedOperationException(); }
  public boolean retainAll(TFloatCollection coll) { throw new UnsupportedOperationException(); }
  public boolean retainAll(float[] array) { throw new UnsupportedOperationException(); }
  
  public void clear() { throw new UnsupportedOperationException(); }
}
