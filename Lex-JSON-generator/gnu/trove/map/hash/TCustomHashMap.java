package gnu.trove.map.hash;

import gnu.trove.function.TObjectFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TCustomObjectHash;
import gnu.trove.impl.hash.TObjectHash;
import gnu.trove.iterator.hash.TObjectHashIterator;
import gnu.trove.map.TMap;
import gnu.trove.procedure.TObjectObjectProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.strategy.HashingStrategy;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Array;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;



























public class TCustomHashMap<K, V>
  extends TCustomObjectHash<K>
  implements TMap<K, V>, Externalizable
{
  static final long serialVersionUID = 1L;
  protected transient V[] _values;
  
  public TCustomHashMap() {}
  
  public TCustomHashMap(HashingStrategy<? super K> strategy)
  {
    super(strategy);
  }
  







  public TCustomHashMap(HashingStrategy<? super K> strategy, int initialCapacity)
  {
    super(strategy, initialCapacity);
  }
  










  public TCustomHashMap(HashingStrategy<? super K> strategy, int initialCapacity, float loadFactor)
  {
    super(strategy, initialCapacity, loadFactor);
  }
  








  public TCustomHashMap(HashingStrategy<? super K> strategy, Map<? extends K, ? extends V> map)
  {
    this(strategy, map.size());
    putAll(map);
  }
  








  public TCustomHashMap(HashingStrategy<? super K> strategy, TCustomHashMap<? extends K, ? extends V> map)
  {
    this(strategy, map.size());
    putAll(map);
  }
  








  public int setUp(int initialCapacity)
  {
    int capacity = super.setUp(initialCapacity);
    
    _values = ((Object[])new Object[capacity]);
    return capacity;
  }
  








  public V put(K key, V value)
  {
    int index = insertKey(key);
    return doPut(value, index);
  }
  









  public V putIfAbsent(K key, V value)
  {
    int index = insertKey(key);
    if (index < 0) {
      return _values[(-index - 1)];
    }
    return doPut(value, index);
  }
  
  private V doPut(V value, int index)
  {
    V previous = null;
    boolean isNewMapping = true;
    if (index < 0) {
      index = -index - 1;
      previous = _values[index];
      isNewMapping = false;
    }
    
    _values[index] = value;
    
    if (isNewMapping) {
      postInsertHook(consumeFreeSlot);
    }
    
    return previous;
  }
  








  public boolean equals(Object other)
  {
    if (!(other instanceof Map)) {
      return false;
    }
    Map<K, V> that = (Map)other;
    if (that.size() != size()) {
      return false;
    }
    return forEachEntry(new EqProcedure(that));
  }
  
  public int hashCode()
  {
    TCustomHashMap<K, V>.HashProcedure p = new HashProcedure(null);
    forEachEntry(p);
    return p.getHashCode();
  }
  
  public String toString()
  {
    final StringBuilder buf = new StringBuilder("{");
    forEachEntry(new TObjectObjectProcedure() {
      private boolean first = true;
      
      public boolean execute(K key, V value)
      {
        if (first) {
          first = false;
        } else {
          buf.append(", ");
        }
        
        buf.append(key);
        buf.append("=");
        buf.append(value);
        return true;
      }
    });
    buf.append("}");
    return buf.toString();
  }
  
  private final class HashProcedure implements TObjectObjectProcedure<K, V> { private HashProcedure() {}
    
    private int h = 0;
    
    public int getHashCode() {
      return h;
    }
    
    public final boolean execute(K key, V value) {
      h += (HashFunctions.hash(key) ^ (value == null ? 0 : value.hashCode()));
      return true;
    }
  }
  
  private static final class EqProcedure<K, V> implements TObjectObjectProcedure<K, V>
  {
    private final Map<K, V> _otherMap;
    
    EqProcedure(Map<K, V> otherMap) {
      _otherMap = otherMap;
    }
    



    public final boolean execute(K key, V value)
    {
      if ((value == null) && (!_otherMap.containsKey(key))) {
        return false;
      }
      
      V oValue = _otherMap.get(key);
      return (oValue == value) || ((oValue != null) && (oValue.equals(value)));
    }
  }
  







  public boolean forEachKey(TObjectProcedure<? super K> procedure)
  {
    return forEach(procedure);
  }
  







  public boolean forEachValue(TObjectProcedure<? super V> procedure)
  {
    V[] values = _values;
    Object[] set = _set;
    for (int i = values.length; i-- > 0;) {
      if ((set[i] != FREE) && (set[i] != REMOVED) && (!procedure.execute(values[i])))
      {

        return false;
      }
    }
    return true;
  }
  









  public boolean forEachEntry(TObjectObjectProcedure<? super K, ? super V> procedure)
  {
    Object[] keys = _set;
    V[] values = _values;
    for (int i = keys.length; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED) && (!procedure.execute(keys[i], values[i])))
      {

        return false;
      }
    }
    return true;
  }
  








  public boolean retainEntries(TObjectObjectProcedure<? super K, ? super V> procedure)
  {
    boolean modified = false;
    Object[] keys = _set;
    V[] values = _values;
    

    tempDisableAutoCompaction();
    try {
      for (i = keys.length; i-- > 0;) {
        if ((keys[i] != FREE) && (keys[i] != REMOVED) && (!procedure.execute(keys[i], values[i])))
        {

          removeAt(i);
          modified = true;
        }
      }
    } finally {
      int i;
      reenableAutoCompaction(true);
    }
    
    return modified;
  }
  





  public void transformValues(TObjectFunction<V, V> function)
  {
    V[] values = _values;
    Object[] set = _set;
    for (int i = values.length; i-- > 0;) {
      if ((set[i] != FREE) && (set[i] != REMOVED)) {
        values[i] = function.execute(values[i]);
      }
    }
  }
  






  protected void rehash(int newCapacity)
  {
    int oldCapacity = _set.length;
    int oldSize = size();
    Object[] oldKeys = _set;
    V[] oldVals = _values;
    
    _set = new Object[newCapacity];
    Arrays.fill(_set, FREE);
    _values = ((Object[])new Object[newCapacity]);
    


    for (int i = oldCapacity; i-- > 0;) {
      Object o = oldKeys[i];
      if ((o != FREE) && (o != REMOVED))
      {
        int index = insertKey(o);
        if (index < 0) {
          throwObjectContractViolation(_set[(-index - 1)], o, size(), oldSize, oldKeys);
        }
        _values[index] = oldVals[i];
      }
    }
  }
  






  public V get(Object key)
  {
    int index = index(key);
    if ((index < 0) || (!strategy.equals(_set[index], key))) {
      return null;
    }
    return _values[index];
  }
  

  public void clear()
  {
    if (size() == 0) {
      return;
    }
    
    super.clear();
    
    Arrays.fill(_set, 0, _set.length, FREE);
    Arrays.fill(_values, 0, _values.length, null);
  }
  







  public V remove(Object key)
  {
    V prev = null;
    int index = index(key);
    if (index >= 0) {
      prev = _values[index];
      removeAt(index);
    }
    return prev;
  }
  





  public void removeAt(int index)
  {
    _values[index] = null;
    super.removeAt(index);
  }
  





  public Collection<V> values()
  {
    return new ValueView();
  }
  





  public Set<K> keySet()
  {
    return new KeyView();
  }
  





  public Set<Map.Entry<K, V>> entrySet()
  {
    return new EntryView();
  }
  






  public boolean containsValue(Object val)
  {
    Object[] set = _set;
    V[] vals = _values;
    
    int i;
    int i;
    if (null == val) {
      for (i = vals.length; i-- > 0;) {
        if ((set[i] != FREE) && (set[i] != REMOVED) && (val == vals[i]))
        {
          return true;
        }
      }
    } else {
      for (i = vals.length; i-- > 0;) {
        if ((set[i] != FREE) && (set[i] != REMOVED) && ((val == vals[i]) || (strategy.equals(val, vals[i]))))
        {
          return true;
        }
      }
    }
    return false;
  }
  






  public boolean containsKey(Object key)
  {
    return contains(key);
  }
  





  public void putAll(Map<? extends K, ? extends V> map)
  {
    ensureCapacity(map.size());
    
    for (Map.Entry<? extends K, ? extends V> e : map.entrySet()) {
      put(e.getKey(), e.getValue());
    }
  }
  
  protected class ValueView extends TCustomHashMap<K, V>.MapBackedView<V> {
    protected ValueView() {
      super(null);
    }
    
    public Iterator<V> iterator() {
      new TObjectHashIterator(TCustomHashMap.this) {
        protected V objectAtIndex(int index) {
          return _values[index];
        }
      };
    }
    
    public boolean containsElement(V value)
    {
      return containsValue(value);
    }
    
    public boolean removeElement(V value)
    {
      Object[] values = _values;
      Object[] set = _set;
      
      for (int i = values.length; i-- > 0;) {
        if (((set[i] != TObjectHash.FREE) && (set[i] != TObjectHash.REMOVED) && (value == values[i])) || ((null != values[i]) && (strategy.equals(values[i], value))))
        {


          removeAt(i);
          return true;
        }
      }
      
      return false;
    }
  }
  
  protected class EntryView extends TCustomHashMap<K, V>.MapBackedView<Map.Entry<K, V>> {
    protected EntryView() { super(null); }
    
    private final class EntryIterator extends TObjectHashIterator
    {
      EntryIterator() {
        super();
      }
      

      public TCustomHashMap<K, V>.Entry objectAtIndex(int index)
      {
        return new TCustomHashMap.Entry(TCustomHashMap.this, _set[index], _values[index], index);
      }
    }
    

    public Iterator<Map.Entry<K, V>> iterator()
    {
      return new EntryIterator(TCustomHashMap.this);
    }
    












    public boolean removeElement(Map.Entry<K, V> entry)
    {
      K key = keyForEntry(entry);
      int index = index(key);
      if (index >= 0) {
        Object val = valueForEntry(entry);
        if ((val == _values[index]) || ((null != val) && (strategy.equals(val, _values[index]))))
        {
          removeAt(index);
          return true;
        }
      }
      return false;
    }
    
    public boolean containsElement(Map.Entry<K, V> entry)
    {
      Object val = get(keyForEntry(entry));
      Object entryValue = entry.getValue();
      return (entryValue == val) || ((null != val) && (strategy.equals(val, entryValue)));
    }
    

    protected V valueForEntry(Map.Entry<K, V> entry)
    {
      return entry.getValue();
    }
    
    protected K keyForEntry(Map.Entry<K, V> entry)
    {
      return entry.getKey();
    }
  }
  
  private abstract class MapBackedView<E>
    extends AbstractSet<E>
    implements Set<E>, Iterable<E>
  {
    private MapBackedView() {}
    
    public abstract Iterator<E> iterator();
    
    public abstract boolean removeElement(E paramE);
    
    public abstract boolean containsElement(E paramE);
    
    public boolean contains(Object key)
    {
      return containsElement(key);
    }
    

    public boolean remove(Object o)
    {
      return removeElement(o);
    }
    










    public void clear()
    {
      TCustomHashMap.this.clear();
    }
    
    public boolean add(E obj)
    {
      throw new UnsupportedOperationException();
    }
    
    public int size()
    {
      return TCustomHashMap.this.size();
    }
    
    public Object[] toArray()
    {
      Object[] result = new Object[size()];
      Iterator<E> e = iterator();
      for (int i = 0; e.hasNext(); i++) {
        result[i] = e.next();
      }
      return result;
    }
    

    public <T> T[] toArray(T[] a)
    {
      int size = size();
      if (a.length < size) {
        a = (Object[])Array.newInstance(a.getClass().getComponentType(), size);
      }
      
      Iterator<E> it = iterator();
      Object[] result = a;
      for (int i = 0; i < size; i++) {
        result[i] = it.next();
      }
      
      if (a.length > size) {
        a[size] = null;
      }
      
      return a;
    }
    
    public boolean isEmpty()
    {
      return TCustomHashMap.this.isEmpty();
    }
    
    public boolean addAll(Collection<? extends E> collection)
    {
      throw new UnsupportedOperationException();
    }
    

    public boolean retainAll(Collection<?> collection)
    {
      boolean changed = false;
      Iterator<E> i = iterator();
      while (i.hasNext()) {
        if (!collection.contains(i.next())) {
          i.remove();
          changed = true;
        }
      }
      return changed;
    }
    
    public String toString() {
      Iterator<E> i = iterator();
      if (!i.hasNext()) { return "{}";
      }
      StringBuilder sb = new StringBuilder();
      sb.append('{');
      for (;;) {
        E e = i.next();
        sb.append(e == this ? "(this Collection)" : e);
        if (!i.hasNext()) return '}';
        sb.append(", ");
      }
    }
  }
  
  protected class KeyView extends TCustomHashMap<K, V>.MapBackedView<K> {
    protected KeyView() { super(null); }
    
    public Iterator<K> iterator()
    {
      return new TObjectHashIterator(TCustomHashMap.this);
    }
    
    public boolean removeElement(K key)
    {
      return null != remove(key);
    }
    
    public boolean containsElement(K key)
    {
      return contains(key);
    }
  }
  
  final class Entry implements Map.Entry<K, V>
  {
    private K key;
    private V val;
    private final int index;
    
    Entry(V key, int value)
    {
      this.key = key;
      val = value;
      this.index = index;
    }
    
    public K getKey()
    {
      return key;
    }
    
    public V getValue()
    {
      return val;
    }
    
    public V setValue(V o)
    {
      if (_values[index] != val) {
        throw new ConcurrentModificationException();
      }
      
      V retval = val;
      
      _values[index] = o;
      val = o;
      return retval;
    }
    
    public boolean equals(Object o)
    {
      if ((o instanceof Map.Entry)) {
        Map.Entry<K, V> e1 = this;
        Map.Entry e2 = (Map.Entry)o;
        return (e1.getKey() == null ? e2.getKey() == null : strategy.equals(e1.getKey(), e2.getKey())) && (e1.getValue() == null ? e2.getValue() == null : e1.getValue().equals(e2.getValue()));
      }
      


      return false;
    }
    
    public int hashCode()
    {
      return (getKey() == null ? 0 : getKey().hashCode()) ^ (getValue() == null ? 0 : getValue().hashCode());
    }
    

    public String toString()
    {
      return key + "=" + val;
    }
  }
  
  public void writeExternal(ObjectOutput out)
    throws IOException
  {
    out.writeByte(1);
    

    super.writeExternal(out);
    

    out.writeInt(_size);
    

    for (int i = _set.length; i-- > 0;) {
      if ((_set[i] != REMOVED) && (_set[i] != FREE)) {
        out.writeObject(_set[i]);
        out.writeObject(_values[i]);
      }
    }
  }
  


  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    byte version = in.readByte();
    

    if (version != 0) {
      super.readExternal(in);
    }
    

    int size = in.readInt();
    setUp(size);
    

    while (size-- > 0)
    {
      K key = in.readObject();
      
      V val = in.readObject();
      put(key, val);
    }
  }
}
