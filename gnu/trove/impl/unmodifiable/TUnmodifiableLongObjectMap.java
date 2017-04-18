package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TLongObjectIterator;
import gnu.trove.map.TLongObjectMap;
import gnu.trove.procedure.TLongObjectProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TLongSet;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;






























public class TUnmodifiableLongObjectMap<V>
  implements TLongObjectMap<V>, Serializable
{
  private static final long serialVersionUID = -1034234728574286014L;
  private final TLongObjectMap<V> m;
  
  public TUnmodifiableLongObjectMap(TLongObjectMap<V> m)
  {
    if (m == null)
      throw new NullPointerException();
    this.m = m;
  }
  
  public int size() { return m.size(); }
  public boolean isEmpty() { return m.isEmpty(); }
  public boolean containsKey(long key) { return m.containsKey(key); }
  public boolean containsValue(Object val) { return m.containsValue(val); }
  public V get(long key) { return m.get(key); }
  
  public V put(long key, V value) { throw new UnsupportedOperationException(); }
  public V remove(long key) { throw new UnsupportedOperationException(); }
  public void putAll(TLongObjectMap<? extends V> m) { throw new UnsupportedOperationException(); }
  public void putAll(Map<? extends Long, ? extends V> map) { throw new UnsupportedOperationException(); }
  public void clear() { throw new UnsupportedOperationException(); }
  
  private transient TLongSet keySet = null;
  private transient Collection<V> values = null;
  
  public TLongSet keySet() {
    if (keySet == null)
      keySet = TCollections.unmodifiableSet(m.keySet());
    return keySet; }
  
  public long[] keys() { return m.keys(); }
  public long[] keys(long[] array) { return m.keys(array); }
  
  public Collection<V> valueCollection() {
    if (values == null)
      values = Collections.unmodifiableCollection(m.valueCollection());
    return values; }
  
  public Object[] values() { return m.values(); }
  public V[] values(V[] array) { return m.values(array); }
  
  public boolean equals(Object o) { return (o == this) || (m.equals(o)); }
  public int hashCode() { return m.hashCode(); }
  public String toString() { return m.toString(); }
  public long getNoEntryKey() { return m.getNoEntryKey(); }
  
  public boolean forEachKey(TLongProcedure procedure) {
    return m.forEachKey(procedure);
  }
  
  public boolean forEachValue(TObjectProcedure<? super V> procedure) { return m.forEachValue(procedure); }
  
  public boolean forEachEntry(TLongObjectProcedure<? super V> procedure) {
    return m.forEachEntry(procedure);
  }
  
  public TLongObjectIterator<V> iterator() {
    new TLongObjectIterator() {
      TLongObjectIterator<V> iter = m.iterator();
      
      public long key() { return iter.key(); }
      public V value() { return iter.value(); }
      public void advance() { iter.advance(); }
      public boolean hasNext() { return iter.hasNext(); }
      public V setValue(V val) { throw new UnsupportedOperationException(); }
      public void remove() { throw new UnsupportedOperationException(); }
    };
  }
  
  public V putIfAbsent(long key, V value) { throw new UnsupportedOperationException(); }
  public void transformValues(TObjectFunction<V, V> function) { throw new UnsupportedOperationException(); }
  public boolean retainEntries(TLongObjectProcedure<? super V> procedure) { throw new UnsupportedOperationException(); }
}
