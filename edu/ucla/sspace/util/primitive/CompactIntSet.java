package edu.ucla.sspace.util.primitive;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Collection;
import java.util.NoSuchElementException;


























































public class CompactIntSet
  extends AbstractIntSet
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private final BitSet bitSet;
  
  public CompactIntSet()
  {
    this(new BitSet());
  }
  




  private CompactIntSet(BitSet bitSet)
  {
    this.bitSet = bitSet;
  }
  


  public CompactIntSet(Collection<Integer> ints)
  {
    this();
    for (Integer i : ints)
      bitSet.set(i.intValue());
  }
  
  public boolean add(Integer i) {
    return add(i.intValue());
  }
  
  public boolean add(int i) {
    if (i < 0)
      throw new IllegalArgumentException(
        "Cannot store negative values in an CompactIntSet");
    boolean isPresent = bitSet.get(i);
    bitSet.set(i);
    return !isPresent;
  }
  




  public boolean addAll(CompactIntSet ints)
  {
    int oldSize = size();
    bitSet.or(bitSet);
    return oldSize != size();
  }
  
  public boolean contains(Integer i) {
    return contains(i.intValue());
  }
  
  public boolean contains(int i) {
    return (i >= 0) && (bitSet.get(i));
  }
  
  public boolean isEmpty() {
    return bitSet.isEmpty();
  }
  
  public IntIterator iterator() {
    return new BitSetIterator();
  }
  
  public boolean remove(Integer i) {
    return remove(i.intValue());
  }
  
  public boolean remove(int i) {
    if (i < 0)
      return false;
    boolean isPresent = bitSet.get(i);
    if (isPresent)
      bitSet.set(i, false);
    return isPresent;
  }
  



  public boolean removeAll(CompactIntSet ints)
  {
    int oldSize = size();
    bitSet.andNot(bitSet);
    return oldSize != size();
  }
  



  public boolean retainAll(CompactIntSet ints)
  {
    int oldSize = size();
    bitSet.and(bitSet);
    return oldSize != size();
  }
  
  public int size() {
    return bitSet.cardinality();
  }
  



  public static IntSet wrap(BitSet b)
  {
    return new CompactIntSet(b);
  }
  


  private class BitSetIterator
    implements IntIterator
  {
    int next = -1;
    int cur = -1;
    
    public BitSetIterator() {
      advance();
    }
    
    private void advance() {
      if (next < -1)
        return;
      next = bitSet.nextSetBit(next + 1);
      
      if (next == -1)
        next = -2;
    }
    
    public boolean hasNext() {
      return next >= 0;
    }
    
    public Integer next() {
      return Integer.valueOf(nextInt());
    }
    
    public int nextInt() {
      if (!hasNext())
        throw new NoSuchElementException();
      cur = next;
      advance();
      return cur;
    }
    
    public void remove() {
      if (cur == -1)
        throw new IllegalStateException("Item already removed");
      bitSet.set(cur, false);
      cur = -1;
    }
  }
}
