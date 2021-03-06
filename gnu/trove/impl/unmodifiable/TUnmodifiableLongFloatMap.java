package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TLongFloatIterator;
import gnu.trove.map.TLongFloatMap;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TLongFloatProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.io.Serializable;
import java.util.Map;




































public class TUnmodifiableLongFloatMap
  implements TLongFloatMap, Serializable
{
  private static final long serialVersionUID = -1034234728574286014L;
  private final TLongFloatMap m;
  
  public TUnmodifiableLongFloatMap(TLongFloatMap m)
  {
    if (m == null)
      throw new NullPointerException();
    this.m = m;
  }
  
  public int size() { return m.size(); }
  public boolean isEmpty() { return m.isEmpty(); }
  public boolean containsKey(long key) { return m.containsKey(key); }
  public boolean containsValue(float val) { return m.containsValue(val); }
  public float get(long key) { return m.get(key); }
  
  public float put(long key, float value) { throw new UnsupportedOperationException(); }
  public float remove(long key) { throw new UnsupportedOperationException(); }
  public void putAll(TLongFloatMap m) { throw new UnsupportedOperationException(); }
  public void putAll(Map<? extends Long, ? extends Float> map) { throw new UnsupportedOperationException(); }
  public void clear() { throw new UnsupportedOperationException(); }
  
  private transient TLongSet keySet = null;
  private transient TFloatCollection values = null;
  
  public TLongSet keySet() {
    if (keySet == null)
      keySet = TCollections.unmodifiableSet(m.keySet());
    return keySet; }
  
  public long[] keys() { return m.keys(); }
  public long[] keys(long[] array) { return m.keys(array); }
  
  public TFloatCollection valueCollection() {
    if (values == null)
      values = TCollections.unmodifiableCollection(m.valueCollection());
    return values; }
  
  public float[] values() { return m.values(); }
  public float[] values(float[] array) { return m.values(array); }
  
  public boolean equals(Object o) { return (o == this) || (m.equals(o)); }
  public int hashCode() { return m.hashCode(); }
  public String toString() { return m.toString(); }
  public long getNoEntryKey() { return m.getNoEntryKey(); }
  public float getNoEntryValue() { return m.getNoEntryValue(); }
  
  public boolean forEachKey(TLongProcedure procedure) {
    return m.forEachKey(procedure);
  }
  
  public boolean forEachValue(TFloatProcedure procedure) { return m.forEachValue(procedure); }
  
  public boolean forEachEntry(TLongFloatProcedure procedure) {
    return m.forEachEntry(procedure);
  }
  
  public TLongFloatIterator iterator() {
    new TLongFloatIterator() {
      TLongFloatIterator iter = m.iterator();
      
      public long key() { return iter.key(); }
      public float value() { return iter.value(); }
      public void advance() { iter.advance(); }
      public boolean hasNext() { return iter.hasNext(); }
      public float setValue(float val) { throw new UnsupportedOperationException(); }
      public void remove() { throw new UnsupportedOperationException(); }
    };
  }
  
  public float putIfAbsent(long key, float value) { throw new UnsupportedOperationException(); }
  public void transformValues(TFloatFunction function) { throw new UnsupportedOperationException(); }
  public boolean retainEntries(TLongFloatProcedure procedure) { throw new UnsupportedOperationException(); }
  public boolean increment(long key) { throw new UnsupportedOperationException(); }
  public boolean adjustValue(long key, float amount) { throw new UnsupportedOperationException(); }
  public float adjustOrPutValue(long key, float adjust_amount, float put_amount) { throw new UnsupportedOperationException(); }
}
