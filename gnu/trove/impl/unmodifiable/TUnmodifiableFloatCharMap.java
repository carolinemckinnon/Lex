package gnu.trove.impl.unmodifiable;

import gnu.trove.TCharCollection;
import gnu.trove.TCollections;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TFloatCharIterator;
import gnu.trove.map.TFloatCharMap;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TFloatCharProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TFloatSet;
import java.io.Serializable;
import java.util.Map;




































public class TUnmodifiableFloatCharMap
  implements TFloatCharMap, Serializable
{
  private static final long serialVersionUID = -1034234728574286014L;
  private final TFloatCharMap m;
  
  public TUnmodifiableFloatCharMap(TFloatCharMap m)
  {
    if (m == null)
      throw new NullPointerException();
    this.m = m;
  }
  
  public int size() { return m.size(); }
  public boolean isEmpty() { return m.isEmpty(); }
  public boolean containsKey(float key) { return m.containsKey(key); }
  public boolean containsValue(char val) { return m.containsValue(val); }
  public char get(float key) { return m.get(key); }
  
  public char put(float key, char value) { throw new UnsupportedOperationException(); }
  public char remove(float key) { throw new UnsupportedOperationException(); }
  public void putAll(TFloatCharMap m) { throw new UnsupportedOperationException(); }
  public void putAll(Map<? extends Float, ? extends Character> map) { throw new UnsupportedOperationException(); }
  public void clear() { throw new UnsupportedOperationException(); }
  
  private transient TFloatSet keySet = null;
  private transient TCharCollection values = null;
  
  public TFloatSet keySet() {
    if (keySet == null)
      keySet = TCollections.unmodifiableSet(m.keySet());
    return keySet; }
  
  public float[] keys() { return m.keys(); }
  public float[] keys(float[] array) { return m.keys(array); }
  
  public TCharCollection valueCollection() {
    if (values == null)
      values = TCollections.unmodifiableCollection(m.valueCollection());
    return values; }
  
  public char[] values() { return m.values(); }
  public char[] values(char[] array) { return m.values(array); }
  
  public boolean equals(Object o) { return (o == this) || (m.equals(o)); }
  public int hashCode() { return m.hashCode(); }
  public String toString() { return m.toString(); }
  public float getNoEntryKey() { return m.getNoEntryKey(); }
  public char getNoEntryValue() { return m.getNoEntryValue(); }
  
  public boolean forEachKey(TFloatProcedure procedure) {
    return m.forEachKey(procedure);
  }
  
  public boolean forEachValue(TCharProcedure procedure) { return m.forEachValue(procedure); }
  
  public boolean forEachEntry(TFloatCharProcedure procedure) {
    return m.forEachEntry(procedure);
  }
  
  public TFloatCharIterator iterator() {
    new TFloatCharIterator() {
      TFloatCharIterator iter = m.iterator();
      
      public float key() { return iter.key(); }
      public char value() { return iter.value(); }
      public void advance() { iter.advance(); }
      public boolean hasNext() { return iter.hasNext(); }
      public char setValue(char val) { throw new UnsupportedOperationException(); }
      public void remove() { throw new UnsupportedOperationException(); }
    };
  }
  
  public char putIfAbsent(float key, char value) { throw new UnsupportedOperationException(); }
  public void transformValues(TCharFunction function) { throw new UnsupportedOperationException(); }
  public boolean retainEntries(TFloatCharProcedure procedure) { throw new UnsupportedOperationException(); }
  public boolean increment(float key) { throw new UnsupportedOperationException(); }
  public boolean adjustValue(float key, char amount) { throw new UnsupportedOperationException(); }
  public char adjustOrPutValue(float key, char adjust_amount, char put_amount) { throw new UnsupportedOperationException(); }
}
