package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TDoubleFloatIterator;
import gnu.trove.map.TDoubleFloatMap;
import gnu.trove.procedure.TDoubleFloatProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TDoubleSet;
import java.io.Serializable;
import java.util.Map;




































public class TUnmodifiableDoubleFloatMap
  implements TDoubleFloatMap, Serializable
{
  private static final long serialVersionUID = -1034234728574286014L;
  private final TDoubleFloatMap m;
  
  public TUnmodifiableDoubleFloatMap(TDoubleFloatMap m)
  {
    if (m == null)
      throw new NullPointerException();
    this.m = m;
  }
  
  public int size() { return m.size(); }
  public boolean isEmpty() { return m.isEmpty(); }
  public boolean containsKey(double key) { return m.containsKey(key); }
  public boolean containsValue(float val) { return m.containsValue(val); }
  public float get(double key) { return m.get(key); }
  
  public float put(double key, float value) { throw new UnsupportedOperationException(); }
  public float remove(double key) { throw new UnsupportedOperationException(); }
  public void putAll(TDoubleFloatMap m) { throw new UnsupportedOperationException(); }
  public void putAll(Map<? extends Double, ? extends Float> map) { throw new UnsupportedOperationException(); }
  public void clear() { throw new UnsupportedOperationException(); }
  
  private transient TDoubleSet keySet = null;
  private transient TFloatCollection values = null;
  
  public TDoubleSet keySet() {
    if (keySet == null)
      keySet = TCollections.unmodifiableSet(m.keySet());
    return keySet; }
  
  public double[] keys() { return m.keys(); }
  public double[] keys(double[] array) { return m.keys(array); }
  
  public TFloatCollection valueCollection() {
    if (values == null)
      values = TCollections.unmodifiableCollection(m.valueCollection());
    return values; }
  
  public float[] values() { return m.values(); }
  public float[] values(float[] array) { return m.values(array); }
  
  public boolean equals(Object o) { return (o == this) || (m.equals(o)); }
  public int hashCode() { return m.hashCode(); }
  public String toString() { return m.toString(); }
  public double getNoEntryKey() { return m.getNoEntryKey(); }
  public float getNoEntryValue() { return m.getNoEntryValue(); }
  
  public boolean forEachKey(TDoubleProcedure procedure) {
    return m.forEachKey(procedure);
  }
  
  public boolean forEachValue(TFloatProcedure procedure) { return m.forEachValue(procedure); }
  
  public boolean forEachEntry(TDoubleFloatProcedure procedure) {
    return m.forEachEntry(procedure);
  }
  
  public TDoubleFloatIterator iterator() {
    new TDoubleFloatIterator() {
      TDoubleFloatIterator iter = m.iterator();
      
      public double key() { return iter.key(); }
      public float value() { return iter.value(); }
      public void advance() { iter.advance(); }
      public boolean hasNext() { return iter.hasNext(); }
      public float setValue(float val) { throw new UnsupportedOperationException(); }
      public void remove() { throw new UnsupportedOperationException(); }
    };
  }
  
  public float putIfAbsent(double key, float value) { throw new UnsupportedOperationException(); }
  public void transformValues(TFloatFunction function) { throw new UnsupportedOperationException(); }
  public boolean retainEntries(TDoubleFloatProcedure procedure) { throw new UnsupportedOperationException(); }
  public boolean increment(double key) { throw new UnsupportedOperationException(); }
  public boolean adjustValue(double key, float amount) { throw new UnsupportedOperationException(); }
  public float adjustOrPutValue(double key, float adjust_amount, float put_amount) { throw new UnsupportedOperationException(); }
}
