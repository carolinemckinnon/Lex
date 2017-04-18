package gnu.trove.impl.unmodifiable;

import gnu.trove.TByteCollection;
import gnu.trove.TCollections;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TFloatByteIterator;
import gnu.trove.map.TFloatByteMap;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TFloatByteProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TFloatSet;
import java.io.Serializable;
import java.util.Map;




































public class TUnmodifiableFloatByteMap
  implements TFloatByteMap, Serializable
{
  private static final long serialVersionUID = -1034234728574286014L;
  private final TFloatByteMap m;
  
  public TUnmodifiableFloatByteMap(TFloatByteMap m)
  {
    if (m == null)
      throw new NullPointerException();
    this.m = m;
  }
  
  public int size() { return m.size(); }
  public boolean isEmpty() { return m.isEmpty(); }
  public boolean containsKey(float key) { return m.containsKey(key); }
  public boolean containsValue(byte val) { return m.containsValue(val); }
  public byte get(float key) { return m.get(key); }
  
  public byte put(float key, byte value) { throw new UnsupportedOperationException(); }
  public byte remove(float key) { throw new UnsupportedOperationException(); }
  public void putAll(TFloatByteMap m) { throw new UnsupportedOperationException(); }
  public void putAll(Map<? extends Float, ? extends Byte> map) { throw new UnsupportedOperationException(); }
  public void clear() { throw new UnsupportedOperationException(); }
  
  private transient TFloatSet keySet = null;
  private transient TByteCollection values = null;
  
  public TFloatSet keySet() {
    if (keySet == null)
      keySet = TCollections.unmodifiableSet(m.keySet());
    return keySet; }
  
  public float[] keys() { return m.keys(); }
  public float[] keys(float[] array) { return m.keys(array); }
  
  public TByteCollection valueCollection() {
    if (values == null)
      values = TCollections.unmodifiableCollection(m.valueCollection());
    return values; }
  
  public byte[] values() { return m.values(); }
  public byte[] values(byte[] array) { return m.values(array); }
  
  public boolean equals(Object o) { return (o == this) || (m.equals(o)); }
  public int hashCode() { return m.hashCode(); }
  public String toString() { return m.toString(); }
  public float getNoEntryKey() { return m.getNoEntryKey(); }
  public byte getNoEntryValue() { return m.getNoEntryValue(); }
  
  public boolean forEachKey(TFloatProcedure procedure) {
    return m.forEachKey(procedure);
  }
  
  public boolean forEachValue(TByteProcedure procedure) { return m.forEachValue(procedure); }
  
  public boolean forEachEntry(TFloatByteProcedure procedure) {
    return m.forEachEntry(procedure);
  }
  
  public TFloatByteIterator iterator() {
    new TFloatByteIterator() {
      TFloatByteIterator iter = m.iterator();
      
      public float key() { return iter.key(); }
      public byte value() { return iter.value(); }
      public void advance() { iter.advance(); }
      public boolean hasNext() { return iter.hasNext(); }
      public byte setValue(byte val) { throw new UnsupportedOperationException(); }
      public void remove() { throw new UnsupportedOperationException(); }
    };
  }
  
  public byte putIfAbsent(float key, byte value) { throw new UnsupportedOperationException(); }
  public void transformValues(TByteFunction function) { throw new UnsupportedOperationException(); }
  public boolean retainEntries(TFloatByteProcedure procedure) { throw new UnsupportedOperationException(); }
  public boolean increment(float key) { throw new UnsupportedOperationException(); }
  public boolean adjustValue(float key, byte amount) { throw new UnsupportedOperationException(); }
  public byte adjustOrPutValue(float key, byte adjust_amount, byte put_amount) { throw new UnsupportedOperationException(); }
}