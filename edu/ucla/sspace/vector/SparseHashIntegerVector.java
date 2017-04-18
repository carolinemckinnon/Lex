package edu.ucla.sspace.vector;

import edu.ucla.sspace.util.IntegerEntry;
import gnu.trove.TIntCollection;
import gnu.trove.iterator.TIntIntIterator;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;










































public class SparseHashIntegerVector
  extends AbstractIntegerVector
  implements SparseIntegerVector, Iterable<IntegerEntry>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final TIntIntMap map;
  private final int length;
  private double magnitude;
  
  public SparseHashIntegerVector(int length)
  {
    this.length = length;
    map = new TIntIntHashMap();
    magnitude = -1.0D;
  }
  






  public SparseHashIntegerVector(int[] values)
  {
    this(values.length);
    for (int i = 0; i < values.length; i++)
      if (values[i] != 0)
        map.put(i, values[i]);
    magnitude = -1.0D;
  }
  




  public SparseHashIntegerVector(IntegerVector values)
  {
    length = values.length();
    magnitude = -1.0D;
    if ((values instanceof SparseHashIntegerVector)) {
      SparseHashIntegerVector v = (SparseHashIntegerVector)values;
      map = new TIntIntHashMap(map);
    }
    else if ((values instanceof SparseVector)) {
      int[] nonZeros = ((SparseVector)values).getNonZeroIndices();
      map = new TIntIntHashMap(nonZeros.length);
      for (int index : nonZeros)
        map.put(index, values.get(index));
    } else {
      map = new TIntIntHashMap();
      for (int index = 0; index < values.length(); index++) {
        int value = values.get(index);
        if (value != 0) {
          map.put(index, value);
        }
      }
    }
  }
  


  public int add(int index, int delta)
  {
    int val = map.get(index);
    int newVal = val + delta;
    if (newVal == 0) {
      map.remove(index);
    } else
      map.put(index, newVal);
    magnitude = -1.0D;
    return newVal;
  }
  


  public int get(int index)
  {
    return map.get(index);
  }
  


  public int[] getNonZeroIndices()
  {
    int[] nz = map.keys();
    Arrays.sort(nz);
    return nz;
  }
  




  public Iterator<IntegerEntry> iterator()
  {
    return new IntegerIterator();
  }
  


  public int length()
  {
    return length;
  }
  



  public double magnitude()
  {
    if (magnitude < 0.0D) {
      magnitude = 0.0D;
      TIntIterator iter = map.valueCollection().iterator();
      while (iter.hasNext()) {
        int i = iter.next();
        magnitude += i * i;
      }
      magnitude = Math.sqrt(magnitude);
    }
    return magnitude;
  }
  


  public void set(int index, int value)
  {
    int cur = map.get(index);
    if (value == 0) {
      if (cur != 0) {
        map.remove(index);
      }
    } else
      map.put(index, value);
    magnitude = -1.0D;
  }
  

  class IntegerIterator
    implements Iterator<IntegerEntry>
  {
    TIntIntIterator iter;
    

    public IntegerIterator()
    {
      iter = map.iterator();
    }
    
    public boolean hasNext() {
      return iter.hasNext();
    }
    
    public IntegerEntry next() {
      iter.advance();
      new IntegerEntry() {
        public int index() { return iter.key(); }
        public int value() { return iter.value(); }
      };
    }
    
    public void remove() {
      throw new UnsupportedOperationException(
        "Cannot remove from vector");
    }
  }
}
