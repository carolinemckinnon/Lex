package gnu.trove.impl.sync;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TObjectByteIterator;
import gnu.trove.map.TObjectByteMap;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TObjectByteProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;



































public class TSynchronizedObjectByteMap<K>
  implements TObjectByteMap<K>, Serializable
{
  private static final long serialVersionUID = 1978198479659022715L;
  private final TObjectByteMap<K> m;
  final Object mutex;
  
  public TSynchronizedObjectByteMap(TObjectByteMap<K> m)
  {
    if (m == null)
      throw new NullPointerException();
    this.m = m;
    mutex = this;
  }
  
  public TSynchronizedObjectByteMap(TObjectByteMap<K> m, Object mutex) {
    this.m = m;
    this.mutex = mutex;
  }
  
  public int size() {
    synchronized (mutex) { return m.size();
    } }
  
  public boolean isEmpty() { synchronized (mutex) { return m.isEmpty();
    } }
  
  public boolean containsKey(Object key) { synchronized (mutex) { return m.containsKey(key);
    } }
  
  public boolean containsValue(byte value) { synchronized (mutex) { return m.containsValue(value);
    } }
  
  public byte get(Object key) { synchronized (mutex) { return m.get(key);
    }
  }
  
  public byte put(K key, byte value) { synchronized (mutex) { return m.put(key, value);
    } }
  
  public byte remove(Object key) { synchronized (mutex) { return m.remove(key);
    } }
  
  public void putAll(Map<? extends K, ? extends Byte> map) { synchronized (mutex) { m.putAll(map);
    } }
  
  public void putAll(TObjectByteMap<? extends K> map) { synchronized (mutex) { m.putAll(map);
    } }
  
  public void clear() { synchronized (mutex) { m.clear();
    } }
  
  private transient Set<K> keySet = null;
  private transient TByteCollection values = null;
  
  public Set<K> keySet() {
    synchronized (mutex) {
      if (keySet == null) {
        keySet = new SynchronizedSet(m.keySet(), mutex);
      }
      return keySet;
    }
  }
  
  public Object[] keys() { synchronized (mutex) { return m.keys();
    } }
  
  public K[] keys(K[] array) { synchronized (mutex) { return m.keys(array);
    }
  }
  
  public TByteCollection valueCollection() { synchronized (mutex) {
      if (values == null)
        values = new TSynchronizedByteCollection(m.valueCollection(), mutex);
      return values;
    }
  }
  
  public byte[] values() { synchronized (mutex) { return m.values();
    } }
  
  public byte[] values(byte[] array) { synchronized (mutex) { return m.values(array);
    }
  }
  
  public TObjectByteIterator<K> iterator() { return m.iterator(); }
  


  public byte getNoEntryValue() { return m.getNoEntryValue(); }
  
  public byte putIfAbsent(K key, byte value) {
    synchronized (mutex) { return m.putIfAbsent(key, value);
    } }
  
  public boolean forEachKey(TObjectProcedure<? super K> procedure) { synchronized (mutex) { return m.forEachKey(procedure);
    } }
  
  public boolean forEachValue(TByteProcedure procedure) { synchronized (mutex) { return m.forEachValue(procedure);
    } }
  
  public boolean forEachEntry(TObjectByteProcedure<? super K> procedure) { synchronized (mutex) { return m.forEachEntry(procedure);
    } }
  
  public void transformValues(TByteFunction function) { synchronized (mutex) { m.transformValues(function);
    } }
  
  public boolean retainEntries(TObjectByteProcedure<? super K> procedure) { synchronized (mutex) { return m.retainEntries(procedure);
    } }
  
  public boolean increment(K key) { synchronized (mutex) { return m.increment(key);
    } }
  
  public boolean adjustValue(K key, byte amount) { synchronized (mutex) { return m.adjustValue(key, amount);
    } }
  
  public byte adjustOrPutValue(K key, byte adjust_amount, byte put_amount) { synchronized (mutex) { return m.adjustOrPutValue(key, adjust_amount, put_amount);
    }
  }
  
  public boolean equals(Object o) { synchronized (mutex) { return m.equals(o);
    } }
  
  public int hashCode() { synchronized (mutex) { return m.hashCode();
    } }
  
  public String toString() { synchronized (mutex) { return m.toString();
    } }
  
  private void writeObject(ObjectOutputStream s) throws IOException { synchronized (mutex) { s.defaultWriteObject();
    }
  }
}
