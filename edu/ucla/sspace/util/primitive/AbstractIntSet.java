package edu.ucla.sspace.util.primitive;

import java.util.AbstractSet;
























public abstract class AbstractIntSet
  extends AbstractSet<Integer>
  implements IntSet
{
  public AbstractIntSet() {}
  
  public boolean add(int i)
  {
    throw new UnsupportedOperationException();
  }
  
  public boolean addAll(IntCollection ints) {
    IntIterator it = ints.iterator();
    boolean changed = false;
    while (it.hasNext())
      if (add(it.nextInt()))
        changed = true;
    return changed;
  }
  
  public boolean containsAll(IntCollection ints) {
    IntIterator it = ints.iterator();
    while (it.hasNext())
      if (!contains(it.nextInt()))
        return false;
    return true;
  }
  
  public abstract IntIterator iterator();
  
  public boolean remove(int i) {
    throw new UnsupportedOperationException();
  }
  
  public boolean removeAll(IntCollection ints) {
    IntIterator it = ints.iterator();
    boolean changed = false;
    while (it.hasNext())
      if (remove(it.nextInt()))
        changed = true;
    return changed;
  }
  



  public boolean retainAll(IntCollection ints)
  {
    IntIterator it = iterator();
    boolean changed = false;
    while (it.hasNext()) {
      if (!ints.contains(it.nextInt())) {
        it.remove();
        changed = true;
      }
    }
    return changed;
  }
  
  public int[] toPrimitiveArray() {
    int[] arr = new int[size()];
    IntIterator it = iterator();
    int i = 0;
    while (it.hasNext()) {
      arr[(i++)] = it.nextInt();
    }
    return arr;
  }
}
