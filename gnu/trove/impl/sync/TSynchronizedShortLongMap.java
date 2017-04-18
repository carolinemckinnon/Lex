package gnu.trove.impl.sync;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TShortLongIterator;
import gnu.trove.map.TShortLongMap;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.procedure.TShortLongProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;



































public class TSynchronizedShortLongMap
  implements TShortLongMap, Serializable
{
  private static final long serialVersionUID = 1978198479659022715L;
  private final TShortLongMap m;
  final Object mutex;
  
  public TSynchronizedShortLongMap(TShortLongMap m)
  {
    if (m == null)
      throw new NullPointerException();
    this.m = m;
    mutex = this;
  }
  
  public TSynchronizedShortLongMap(TShortLongMap m, Object mutex) {
    this.m = m;
    this.mutex = mutex;
  }
  
  public int size() {
    synchronized (mutex) { return m.size();
    } }
  
  public boolean isEmpty() { synchronized (mutex) { return m.isEmpty();
    } }
  
  public boolean containsKey(short key) { synchronized (mutex) { return m.containsKey(key);
    } }
  
  public boolean containsValue(long value) { synchronized (mutex) { return m.containsValue(value);
    } }
  
  public long get(short key) { synchronized (mutex) { return m.get(key);
    }
  }
  
  public long put(short key, long value) { synchronized (mutex) { return m.put(key, value);
    } }
  
  public long remove(short key) { synchronized (mutex) { return m.remove(key);
    } }
  
  public void putAll(Map<? extends Short, ? extends Long> map) { synchronized (mutex) { m.putAll(map);
    } }
  
  public void putAll(TShortLongMap map) { synchronized (mutex) { m.putAll(map);
    } }
  
  public void clear() { synchronized (mutex) { m.clear();
    } }
  
  private transient TShortSet keySet = null;
  private transient TLongCollection values = null;
  
  public TShortSet keySet() {
    synchronized (mutex) {
      if (keySet == null)
        keySet = new TSynchronizedShortSet(m.keySet(), mutex);
      return keySet;
    }
  }
  
  public short[] keys() { synchronized (mutex) { return m.keys();
    } }
  
  public short[] keys(short[] array) { synchronized (mutex) { return m.keys(array);
    }
  }
  
  public TLongCollection valueCollection() { synchronized (mutex) {
      if (values == null)
        values = new TSynchronizedLongCollection(m.valueCollection(), mutex);
      return values;
    }
  }
  
  public long[] values() { synchronized (mutex) { return m.values();
    } }
  
  public long[] values(long[] array) { synchronized (mutex) { return m.values(array);
    }
  }
  
  public TShortLongIterator iterator() { return m.iterator(); }
  


  public short getNoEntryKey() { return m.getNoEntryKey(); }
  public long getNoEntryValue() { return m.getNoEntryValue(); }
  
  public long putIfAbsent(short key, long value) {
    synchronized (mutex) { return m.putIfAbsent(key, value);
    } }
  
  public boolean forEachKey(TShortProcedure procedure) { synchronized (mutex) { return m.forEachKey(procedure);
    } }
  
  public boolean forEachValue(TLongProcedure procedure) { synchronized (mutex) { return m.forEachValue(procedure);
    } }
  
  public boolean forEachEntry(TShortLongProcedure procedure) { synchronized (mutex) { return m.forEachEntry(procedure);
    } }
  
  public void transformValues(TLongFunction function) { synchronized (mutex) { m.transformValues(function);
    } }
  
  public boolean retainEntries(TShortLongProcedure procedure) { synchronized (mutex) { return m.retainEntries(procedure);
    } }
  
  public boolean increment(short key) { synchronized (mutex) { return m.increment(key);
    } }
  
  public boolean adjustValue(short key, long amount) { synchronized (mutex) { return m.adjustValue(key, amount);
    } }
  
  public long adjustOrPutValue(short key, long adjust_amount, long put_amount) { synchronized (mutex) { return m.adjustOrPutValue(key, adjust_amount, put_amount);
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
