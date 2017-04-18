package gnu.trove.map.hash;

import gnu.trove.TLongCollection;
import gnu.trove.TShortCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.impl.hash.TShortLongHash;
import gnu.trove.iterator.TLongIterator;
import gnu.trove.iterator.TShortIterator;
import gnu.trove.iterator.TShortLongIterator;
import gnu.trove.map.TShortLongMap;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.procedure.TShortLongProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Map.Entry;



































public class TShortLongHashMap
  extends TShortLongHash
  implements TShortLongMap, Externalizable
{
  static final long serialVersionUID = 1L;
  protected transient long[] _values;
  
  public TShortLongHashMap() {}
  
  public TShortLongHashMap(int initialCapacity)
  {
    super(initialCapacity);
  }
  








  public TShortLongHashMap(int initialCapacity, float loadFactor)
  {
    super(initialCapacity, loadFactor);
  }
  













  public TShortLongHashMap(int initialCapacity, float loadFactor, short noEntryKey, long noEntryValue)
  {
    super(initialCapacity, loadFactor, noEntryKey, noEntryValue);
  }
  







  public TShortLongHashMap(short[] keys, long[] values)
  {
    super(Math.max(keys.length, values.length));
    
    int size = Math.min(keys.length, values.length);
    for (int i = 0; i < size; i++) {
      put(keys[i], values[i]);
    }
  }
  






  public TShortLongHashMap(TShortLongMap map)
  {
    super(map.size());
    if ((map instanceof TShortLongHashMap)) {
      TShortLongHashMap hashmap = (TShortLongHashMap)map;
      _loadFactor = _loadFactor;
      no_entry_key = no_entry_key;
      no_entry_value = no_entry_value;
      
      if (no_entry_key != 0) {
        Arrays.fill(_set, no_entry_key);
      }
      
      if (no_entry_value != 0L) {
        Arrays.fill(_values, no_entry_value);
      }
      setUp((int)Math.ceil(10.0F / _loadFactor));
    }
    putAll(map);
  }
  









  protected int setUp(int initialCapacity)
  {
    int capacity = super.setUp(initialCapacity);
    _values = new long[capacity];
    return capacity;
  }
  






  protected void rehash(int newCapacity)
  {
    int oldCapacity = _set.length;
    
    short[] oldKeys = _set;
    long[] oldVals = _values;
    byte[] oldStates = _states;
    
    _set = new short[newCapacity];
    _values = new long[newCapacity];
    _states = new byte[newCapacity];
    
    for (int i = oldCapacity; i-- > 0;) {
      if (oldStates[i] == 1) {
        short o = oldKeys[i];
        int index = insertKey(o);
        _values[index] = oldVals[i];
      }
    }
  }
  

  public long put(short key, long value)
  {
    int index = insertKey(key);
    return doPut(key, value, index);
  }
  

  public long putIfAbsent(short key, long value)
  {
    int index = insertKey(key);
    if (index < 0)
      return _values[(-index - 1)];
    return doPut(key, value, index);
  }
  
  private long doPut(short key, long value, int index)
  {
    long previous = no_entry_value;
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
  

  public void putAll(Map<? extends Short, ? extends Long> map)
  {
    ensureCapacity(map.size());
    
    for (Map.Entry<? extends Short, ? extends Long> entry : map.entrySet()) {
      put(((Short)entry.getKey()).shortValue(), ((Long)entry.getValue()).longValue());
    }
  }
  

  public void putAll(TShortLongMap map)
  {
    ensureCapacity(map.size());
    TShortLongIterator iter = map.iterator();
    while (iter.hasNext()) {
      iter.advance();
      put(iter.key(), iter.value());
    }
  }
  

  public long get(short key)
  {
    int index = index(key);
    return index < 0 ? no_entry_value : _values[index];
  }
  

  public void clear()
  {
    super.clear();
    Arrays.fill(_set, 0, _set.length, no_entry_key);
    Arrays.fill(_values, 0, _values.length, no_entry_value);
    Arrays.fill(_states, 0, _states.length, (byte)0);
  }
  

  public boolean isEmpty()
  {
    return 0 == _size;
  }
  

  public long remove(short key)
  {
    long prev = no_entry_value;
    int index = index(key);
    if (index >= 0) {
      prev = _values[index];
      removeAt(index);
    }
    return prev;
  }
  

  protected void removeAt(int index)
  {
    _values[index] = no_entry_value;
    super.removeAt(index);
  }
  

  public TShortSet keySet()
  {
    return new TKeyView();
  }
  

  public short[] keys()
  {
    short[] keys = new short[size()];
    short[] k = _set;
    byte[] states = _states;
    
    int i = k.length; for (int j = 0; i-- > 0;) {
      if (states[i] == 1) {
        keys[(j++)] = k[i];
      }
    }
    return keys;
  }
  

  public short[] keys(short[] array)
  {
    int size = size();
    if (array.length < size) {
      array = new short[size];
    }
    
    short[] keys = _set;
    byte[] states = _states;
    
    int i = keys.length; for (int j = 0; i-- > 0;) {
      if (states[i] == 1) {
        array[(j++)] = keys[i];
      }
    }
    return array;
  }
  

  public TLongCollection valueCollection()
  {
    return new TValueView();
  }
  

  public long[] values()
  {
    long[] vals = new long[size()];
    long[] v = _values;
    byte[] states = _states;
    
    int i = v.length; for (int j = 0; i-- > 0;) {
      if (states[i] == 1) {
        vals[(j++)] = v[i];
      }
    }
    return vals;
  }
  

  public long[] values(long[] array)
  {
    int size = size();
    if (array.length < size) {
      array = new long[size];
    }
    
    long[] v = _values;
    byte[] states = _states;
    
    int i = v.length; for (int j = 0; i-- > 0;) {
      if (states[i] == 1) {
        array[(j++)] = v[i];
      }
    }
    return array;
  }
  

  public boolean containsValue(long val)
  {
    byte[] states = _states;
    long[] vals = _values;
    
    for (int i = vals.length; i-- > 0;) {
      if ((states[i] == 1) && (val == vals[i])) {
        return true;
      }
    }
    return false;
  }
  

  public boolean containsKey(short key)
  {
    return contains(key);
  }
  

  public TShortLongIterator iterator()
  {
    return new TShortLongHashIterator(this);
  }
  

  public boolean forEachKey(TShortProcedure procedure)
  {
    return forEach(procedure);
  }
  

  public boolean forEachValue(TLongProcedure procedure)
  {
    byte[] states = _states;
    long[] values = _values;
    for (int i = values.length; i-- > 0;) {
      if ((states[i] == 1) && (!procedure.execute(values[i]))) {
        return false;
      }
    }
    return true;
  }
  

  public boolean forEachEntry(TShortLongProcedure procedure)
  {
    byte[] states = _states;
    short[] keys = _set;
    long[] values = _values;
    for (int i = keys.length; i-- > 0;) {
      if ((states[i] == 1) && (!procedure.execute(keys[i], values[i]))) {
        return false;
      }
    }
    return true;
  }
  

  public void transformValues(TLongFunction function)
  {
    byte[] states = _states;
    long[] values = _values;
    for (int i = values.length; i-- > 0;) {
      if (states[i] == 1) {
        values[i] = function.execute(values[i]);
      }
    }
  }
  

  public boolean retainEntries(TShortLongProcedure procedure)
  {
    boolean modified = false;
    byte[] states = _states;
    short[] keys = _set;
    long[] values = _values;
    


    tempDisableAutoCompaction();
    try {
      for (i = keys.length; i-- > 0;) {
        if ((states[i] == 1) && (!procedure.execute(keys[i], values[i]))) {
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
  

  public boolean increment(short key)
  {
    return adjustValue(key, 1L);
  }
  

  public boolean adjustValue(short key, long amount)
  {
    int index = index(key);
    if (index < 0) {
      return false;
    }
    _values[index] += amount;
    return true;
  }
  


  public long adjustOrPutValue(short key, long adjust_amount, long put_amount)
  {
    int index = insertKey(key);
    boolean isNewMapping;
    long newValue;
    boolean isNewMapping; if (index < 0) {
      index = -index - 1;
      long newValue = _values[index] += adjust_amount;
      isNewMapping = false;
    } else {
      newValue = _values[index] = put_amount;
      isNewMapping = true;
    }
    
    byte previousState = _states[index];
    
    if (isNewMapping) {
      postInsertHook(consumeFreeSlot);
    }
    
    return newValue;
  }
  
  protected class TKeyView implements TShortSet
  {
    protected TKeyView() {}
    
    public TShortIterator iterator()
    {
      return new TShortLongHashMap.TShortLongKeyHashIterator(TShortLongHashMap.this, TShortLongHashMap.this);
    }
    

    public short getNoEntryValue()
    {
      return no_entry_key;
    }
    

    public int size()
    {
      return _size;
    }
    

    public boolean isEmpty()
    {
      return 0 == _size;
    }
    

    public boolean contains(short entry)
    {
      return TShortLongHashMap.this.contains(entry);
    }
    

    public short[] toArray()
    {
      return keys();
    }
    

    public short[] toArray(short[] dest)
    {
      return keys(dest);
    }
    





    public boolean add(short entry)
    {
      throw new UnsupportedOperationException();
    }
    

    public boolean remove(short entry)
    {
      return no_entry_value != remove(entry);
    }
    

    public boolean containsAll(Collection<?> collection)
    {
      for (Object element : collection) {
        if ((element instanceof Short)) {
          short ele = ((Short)element).shortValue();
          if (!containsKey(ele)) {
            return false;
          }
        } else {
          return false;
        }
      }
      return true;
    }
    

    public boolean containsAll(TShortCollection collection)
    {
      TShortIterator iter = collection.iterator();
      while (iter.hasNext()) {
        if (!containsKey(iter.next())) {
          return false;
        }
      }
      return true;
    }
    

    public boolean containsAll(short[] array)
    {
      for (short element : array) {
        if (!TShortLongHashMap.this.contains(element)) {
          return false;
        }
      }
      return true;
    }
    





    public boolean addAll(Collection<? extends Short> collection)
    {
      throw new UnsupportedOperationException();
    }
    





    public boolean addAll(TShortCollection collection)
    {
      throw new UnsupportedOperationException();
    }
    





    public boolean addAll(short[] array)
    {
      throw new UnsupportedOperationException();
    }
    


    public boolean retainAll(Collection<?> collection)
    {
      boolean modified = false;
      TShortIterator iter = iterator();
      while (iter.hasNext()) {
        if (!collection.contains(Short.valueOf(iter.next()))) {
          iter.remove();
          modified = true;
        }
      }
      return modified;
    }
    

    public boolean retainAll(TShortCollection collection)
    {
      if (this == collection) {
        return false;
      }
      boolean modified = false;
      TShortIterator iter = iterator();
      while (iter.hasNext()) {
        if (!collection.contains(iter.next())) {
          iter.remove();
          modified = true;
        }
      }
      return modified;
    }
    

    public boolean retainAll(short[] array)
    {
      boolean changed = false;
      Arrays.sort(array);
      short[] set = _set;
      byte[] states = _states;
      
      for (int i = set.length; i-- > 0;) {
        if ((states[i] == 1) && (Arrays.binarySearch(array, set[i]) < 0)) {
          removeAt(i);
          changed = true;
        }
      }
      return changed;
    }
    

    public boolean removeAll(Collection<?> collection)
    {
      boolean changed = false;
      for (Object element : collection) {
        if ((element instanceof Short)) {
          short c = ((Short)element).shortValue();
          if (remove(c)) {
            changed = true;
          }
        }
      }
      return changed;
    }
    

    public boolean removeAll(TShortCollection collection)
    {
      if (this == collection) {
        clear();
        return true;
      }
      boolean changed = false;
      TShortIterator iter = collection.iterator();
      while (iter.hasNext()) {
        short element = iter.next();
        if (remove(element)) {
          changed = true;
        }
      }
      return changed;
    }
    

    public boolean removeAll(short[] array)
    {
      boolean changed = false;
      for (int i = array.length; i-- > 0;) {
        if (remove(array[i])) {
          changed = true;
        }
      }
      return changed;
    }
    

    public void clear()
    {
      TShortLongHashMap.this.clear();
    }
    

    public boolean forEach(TShortProcedure procedure)
    {
      return forEachKey(procedure);
    }
    

    public boolean equals(Object other)
    {
      if (!(other instanceof TShortSet)) {
        return false;
      }
      TShortSet that = (TShortSet)other;
      if (that.size() != size()) {
        return false;
      }
      for (int i = _states.length; i-- > 0;) {
        if ((_states[i] == 1) && 
          (!that.contains(_set[i]))) {
          return false;
        }
      }
      
      return true;
    }
    

    public int hashCode()
    {
      int hashcode = 0;
      for (int i = _states.length; i-- > 0;) {
        if (_states[i] == 1) {
          hashcode += HashFunctions.hash(_set[i]);
        }
      }
      return hashcode;
    }
    

    public String toString()
    {
      final StringBuilder buf = new StringBuilder("{");
      forEachKey(new TShortProcedure() {
        private boolean first = true;
        
        public boolean execute(short key)
        {
          if (first) {
            first = false;
          } else {
            buf.append(", ");
          }
          
          buf.append(key);
          return true;
        }
      });
      buf.append("}");
      return buf.toString();
    }
  }
  
  protected class TValueView implements TLongCollection
  {
    protected TValueView() {}
    
    public TLongIterator iterator()
    {
      return new TShortLongHashMap.TShortLongValueHashIterator(TShortLongHashMap.this, TShortLongHashMap.this);
    }
    

    public long getNoEntryValue()
    {
      return no_entry_value;
    }
    

    public int size()
    {
      return _size;
    }
    

    public boolean isEmpty()
    {
      return 0 == _size;
    }
    

    public boolean contains(long entry)
    {
      return containsValue(entry);
    }
    

    public long[] toArray()
    {
      return values();
    }
    

    public long[] toArray(long[] dest)
    {
      return values(dest);
    }
    

    public boolean add(long entry)
    {
      throw new UnsupportedOperationException();
    }
    

    public boolean remove(long entry)
    {
      long[] values = _values;
      short[] set = _set;
      
      for (int i = values.length; i-- > 0;) {
        if ((set[i] != 0) && (set[i] != 2) && (entry == values[i])) {
          removeAt(i);
          return true;
        }
      }
      return false;
    }
    

    public boolean containsAll(Collection<?> collection)
    {
      for (Object element : collection) {
        if ((element instanceof Long)) {
          long ele = ((Long)element).longValue();
          if (!containsValue(ele)) {
            return false;
          }
        } else {
          return false;
        }
      }
      return true;
    }
    

    public boolean containsAll(TLongCollection collection)
    {
      TLongIterator iter = collection.iterator();
      while (iter.hasNext()) {
        if (!containsValue(iter.next())) {
          return false;
        }
      }
      return true;
    }
    

    public boolean containsAll(long[] array)
    {
      for (long element : array) {
        if (!containsValue(element)) {
          return false;
        }
      }
      return true;
    }
    

    public boolean addAll(Collection<? extends Long> collection)
    {
      throw new UnsupportedOperationException();
    }
    

    public boolean addAll(TLongCollection collection)
    {
      throw new UnsupportedOperationException();
    }
    

    public boolean addAll(long[] array)
    {
      throw new UnsupportedOperationException();
    }
    


    public boolean retainAll(Collection<?> collection)
    {
      boolean modified = false;
      TLongIterator iter = iterator();
      while (iter.hasNext()) {
        if (!collection.contains(Long.valueOf(iter.next()))) {
          iter.remove();
          modified = true;
        }
      }
      return modified;
    }
    

    public boolean retainAll(TLongCollection collection)
    {
      if (this == collection) {
        return false;
      }
      boolean modified = false;
      TLongIterator iter = iterator();
      while (iter.hasNext()) {
        if (!collection.contains(iter.next())) {
          iter.remove();
          modified = true;
        }
      }
      return modified;
    }
    

    public boolean retainAll(long[] array)
    {
      boolean changed = false;
      Arrays.sort(array);
      long[] values = _values;
      byte[] states = _states;
      
      for (int i = values.length; i-- > 0;) {
        if ((states[i] == 1) && (Arrays.binarySearch(array, values[i]) < 0)) {
          removeAt(i);
          changed = true;
        }
      }
      return changed;
    }
    

    public boolean removeAll(Collection<?> collection)
    {
      boolean changed = false;
      for (Object element : collection) {
        if ((element instanceof Long)) {
          long c = ((Long)element).longValue();
          if (remove(c)) {
            changed = true;
          }
        }
      }
      return changed;
    }
    

    public boolean removeAll(TLongCollection collection)
    {
      if (this == collection) {
        clear();
        return true;
      }
      boolean changed = false;
      TLongIterator iter = collection.iterator();
      while (iter.hasNext()) {
        long element = iter.next();
        if (remove(element)) {
          changed = true;
        }
      }
      return changed;
    }
    

    public boolean removeAll(long[] array)
    {
      boolean changed = false;
      for (int i = array.length; i-- > 0;) {
        if (remove(array[i])) {
          changed = true;
        }
      }
      return changed;
    }
    

    public void clear()
    {
      TShortLongHashMap.this.clear();
    }
    

    public boolean forEach(TLongProcedure procedure)
    {
      return forEachValue(procedure);
    }
    


    public String toString()
    {
      final StringBuilder buf = new StringBuilder("{");
      forEachValue(new TLongProcedure() {
        private boolean first = true;
        
        public boolean execute(long value) {
          if (first) {
            first = false;
          } else {
            buf.append(", ");
          }
          
          buf.append(value);
          return true;
        }
      });
      buf.append("}");
      return buf.toString();
    }
  }
  



  class TShortLongKeyHashIterator
    extends THashPrimitiveIterator
    implements TShortIterator
  {
    TShortLongKeyHashIterator(TPrimitiveHash hash)
    {
      super();
    }
    
    public short next()
    {
      moveToNextIndex();
      return _set[_index];
    }
    
    public void remove()
    {
      if (_expectedSize != _hash.size()) {
        throw new ConcurrentModificationException();
      }
      
      try
      {
        _hash.tempDisableAutoCompaction();
        removeAt(_index);
      }
      finally {
        _hash.reenableAutoCompaction(false);
      }
      
      _expectedSize -= 1;
    }
  }
  




  class TShortLongValueHashIterator
    extends THashPrimitiveIterator
    implements TLongIterator
  {
    TShortLongValueHashIterator(TPrimitiveHash hash)
    {
      super();
    }
    
    public long next()
    {
      moveToNextIndex();
      return _values[_index];
    }
    
    public void remove()
    {
      if (_expectedSize != _hash.size()) {
        throw new ConcurrentModificationException();
      }
      
      try
      {
        _hash.tempDisableAutoCompaction();
        removeAt(_index);
      }
      finally {
        _hash.reenableAutoCompaction(false);
      }
      
      _expectedSize -= 1;
    }
  }
  



  class TShortLongHashIterator
    extends THashPrimitiveIterator
    implements TShortLongIterator
  {
    TShortLongHashIterator(TShortLongHashMap map)
    {
      super();
    }
    
    public void advance()
    {
      moveToNextIndex();
    }
    
    public short key()
    {
      return _set[_index];
    }
    
    public long value()
    {
      return _values[_index];
    }
    
    public long setValue(long val)
    {
      long old = value();
      _values[_index] = val;
      return old;
    }
    
    public void remove()
    {
      if (_expectedSize != _hash.size()) {
        throw new ConcurrentModificationException();
      }
      try
      {
        _hash.tempDisableAutoCompaction();
        removeAt(_index);
      }
      finally {
        _hash.reenableAutoCompaction(false);
      }
      _expectedSize -= 1;
    }
  }
  


  public boolean equals(Object other)
  {
    if (!(other instanceof TShortLongMap)) {
      return false;
    }
    TShortLongMap that = (TShortLongMap)other;
    if (that.size() != size()) {
      return false;
    }
    long[] values = _values;
    byte[] states = _states;
    long this_no_entry_value = getNoEntryValue();
    long that_no_entry_value = that.getNoEntryValue();
    for (int i = values.length; i-- > 0;) {
      if (states[i] == 1) {
        short key = _set[i];
        long that_value = that.get(key);
        long this_value = values[i];
        if ((this_value != that_value) && (this_value != this_no_entry_value) && (that_value != that_no_entry_value))
        {

          return false;
        }
      }
    }
    return true;
  }
  


  public int hashCode()
  {
    int hashcode = 0;
    byte[] states = _states;
    for (int i = _values.length; i-- > 0;) {
      if (states[i] == 1) {
        hashcode += (HashFunctions.hash(_set[i]) ^ HashFunctions.hash(_values[i]));
      }
    }
    
    return hashcode;
  }
  


  public String toString()
  {
    final StringBuilder buf = new StringBuilder("{");
    forEachEntry(new TShortLongProcedure() {
      private boolean first = true;
      
      public boolean execute(short key, long value) { if (first) first = false; else {
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
  

  public void writeExternal(ObjectOutput out)
    throws IOException
  {
    out.writeByte(0);
    

    super.writeExternal(out);
    

    out.writeInt(_size);
    

    for (int i = _states.length; i-- > 0;) {
      if (_states[i] == 1) {
        out.writeShort(_set[i]);
        out.writeLong(_values[i]);
      }
    }
  }
  

  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    in.readByte();
    

    super.readExternal(in);
    

    int size = in.readInt();
    setUp(size);
    

    while (size-- > 0) {
      short key = in.readShort();
      long val = in.readLong();
      put(key, val);
    }
  }
}
