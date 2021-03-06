package gnu.trove.impl.unmodifiable;

import gnu.trove.function.TCharFunction;
import gnu.trove.list.TCharList;
import gnu.trove.procedure.TCharProcedure;
import java.util.Random;
import java.util.RandomAccess;









































public class TUnmodifiableCharList
  extends TUnmodifiableCharCollection
  implements TCharList
{
  static final long serialVersionUID = -283967356065247728L;
  final TCharList list;
  
  public TUnmodifiableCharList(TCharList list)
  {
    super(list);
    this.list = list;
  }
  
  public boolean equals(Object o) { return (o == this) || (list.equals(o)); }
  public int hashCode() { return list.hashCode(); }
  
  public char get(int index) { return list.get(index); }
  public int indexOf(char o) { return list.indexOf(o); }
  public int lastIndexOf(char o) { return list.lastIndexOf(o); }
  
  public char[] toArray(int offset, int len) {
    return list.toArray(offset, len);
  }
  
  public char[] toArray(char[] dest, int offset, int len) { return list.toArray(dest, offset, len); }
  
  public char[] toArray(char[] dest, int source_pos, int dest_pos, int len) {
    return list.toArray(dest, source_pos, dest_pos, len);
  }
  
  public boolean forEachDescending(TCharProcedure procedure) {
    return list.forEachDescending(procedure);
  }
  
  public int binarySearch(char value) { return list.binarySearch(value); }
  
  public int binarySearch(char value, int fromIndex, int toIndex) { return list.binarySearch(value, fromIndex, toIndex); }
  

  public int indexOf(int offset, char value) { return list.indexOf(offset, value); }
  public int lastIndexOf(int offset, char value) { return list.lastIndexOf(offset, value); }
  public TCharList grep(TCharProcedure condition) { return list.grep(condition); }
  public TCharList inverseGrep(TCharProcedure condition) { return list.inverseGrep(condition); }
  
  public char max() { return list.max(); }
  public char min() { return list.min(); }
  public char sum() { return list.sum(); }
  
  public TCharList subList(int fromIndex, int toIndex) {
    return new TUnmodifiableCharList(list.subList(fromIndex, toIndex));
  }
  





































  private Object readResolve()
  {
    return (list instanceof RandomAccess) ? new TUnmodifiableRandomAccessCharList(list) : this;
  }
  


  public void add(char[] vals) { throw new UnsupportedOperationException(); }
  public void add(char[] vals, int offset, int length) { throw new UnsupportedOperationException(); }
  
  public char removeAt(int offset) { throw new UnsupportedOperationException(); }
  public void remove(int offset, int length) { throw new UnsupportedOperationException(); }
  
  public void insert(int offset, char value) { throw new UnsupportedOperationException(); }
  public void insert(int offset, char[] values) { throw new UnsupportedOperationException(); }
  public void insert(int offset, char[] values, int valOffset, int len) { throw new UnsupportedOperationException(); }
  
  public char set(int offset, char val) { throw new UnsupportedOperationException(); }
  public void set(int offset, char[] values) { throw new UnsupportedOperationException(); }
  public void set(int offset, char[] values, int valOffset, int length) { throw new UnsupportedOperationException(); }
  
  public char replace(int offset, char val) { throw new UnsupportedOperationException(); }
  
  public void transformValues(TCharFunction function) { throw new UnsupportedOperationException(); }
  
  public void reverse() { throw new UnsupportedOperationException(); }
  public void reverse(int from, int to) { throw new UnsupportedOperationException(); }
  public void shuffle(Random rand) { throw new UnsupportedOperationException(); }
  
  public void sort() { throw new UnsupportedOperationException(); }
  public void sort(int fromIndex, int toIndex) { throw new UnsupportedOperationException(); }
  public void fill(char val) { throw new UnsupportedOperationException(); }
  public void fill(int fromIndex, int toIndex, char val) { throw new UnsupportedOperationException(); }
}
