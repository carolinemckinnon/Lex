package gnu.trove.impl.hash;

import gnu.trove.impl.HashFunctions;
import gnu.trove.procedure.TLongProcedure;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;





























































public abstract class TLongDoubleHash
  extends TPrimitiveHash
{
  static final long serialVersionUID = 1L;
  public transient long[] _set;
  protected long no_entry_key;
  protected double no_entry_value;
  protected boolean consumeFreeSlot;
  
  public TLongDoubleHash()
  {
    no_entry_key = 0L;
    no_entry_value = 0.0D;
  }
  







  public TLongDoubleHash(int initialCapacity)
  {
    super(initialCapacity);
    no_entry_key = 0L;
    no_entry_value = 0.0D;
  }
  








  public TLongDoubleHash(int initialCapacity, float loadFactor)
  {
    super(initialCapacity, loadFactor);
    no_entry_key = 0L;
    no_entry_value = 0.0D;
  }
  










  public TLongDoubleHash(int initialCapacity, float loadFactor, long no_entry_key, double no_entry_value)
  {
    super(initialCapacity, loadFactor);
    this.no_entry_key = no_entry_key;
    this.no_entry_value = no_entry_value;
  }
  







  public long getNoEntryKey()
  {
    return no_entry_key;
  }
  







  public double getNoEntryValue()
  {
    return no_entry_value;
  }
  









  protected int setUp(int initialCapacity)
  {
    int capacity = super.setUp(initialCapacity);
    _set = new long[capacity];
    return capacity;
  }
  






  public boolean contains(long val)
  {
    return index(val) >= 0;
  }
  







  public boolean forEach(TLongProcedure procedure)
  {
    byte[] states = _states;
    long[] set = _set;
    for (int i = set.length; i-- > 0;) {
      if ((states[i] == 1) && (!procedure.execute(set[i]))) {
        return false;
      }
    }
    return true;
  }
  





  protected void removeAt(int index)
  {
    _set[index] = no_entry_key;
    super.removeAt(index);
  }
  








  protected int index(long key)
  {
    byte[] states = _states;
    long[] set = _set;
    int length = states.length;
    int hash = HashFunctions.hash(key) & 0x7FFFFFFF;
    int index = hash % length;
    byte state = states[index];
    
    if (state == 0) {
      return -1;
    }
    if ((state == 1) && (set[index] == key)) {
      return index;
    }
    return indexRehashed(key, index, hash, state);
  }
  
  int indexRehashed(long key, int index, int hash, byte state)
  {
    int length = _set.length;
    int probe = 1 + hash % (length - 2);
    int loopIndex = index;
    do
    {
      index -= probe;
      if (index < 0) {
        index += length;
      }
      state = _states[index];
      
      if (state == 0) {
        return -1;
      }
      
      if ((key == _set[index]) && (state != 2))
        return index;
    } while (index != loopIndex);
    
    return -1;
  }
  










  protected int insertKey(long val)
  {
    int hash = HashFunctions.hash(val) & 0x7FFFFFFF;
    int index = hash % _states.length;
    byte state = _states[index];
    
    consumeFreeSlot = false;
    
    if (state == 0) {
      consumeFreeSlot = true;
      insertKeyAt(index, val);
      
      return index;
    }
    
    if ((state == 1) && (_set[index] == val)) {
      return -index - 1;
    }
    

    return insertKeyRehash(val, index, hash, state);
  }
  
  int insertKeyRehash(long val, int index, int hash, byte state)
  {
    int length = _set.length;
    int probe = 1 + hash % (length - 2);
    int loopIndex = index;
    int firstRemoved = -1;
    



    do
    {
      if ((state == 2) && (firstRemoved == -1)) {
        firstRemoved = index;
      }
      index -= probe;
      if (index < 0) {
        index += length;
      }
      state = _states[index];
      

      if (state == 0) {
        if (firstRemoved != -1) {
          insertKeyAt(firstRemoved, val);
          return firstRemoved;
        }
        consumeFreeSlot = true;
        insertKeyAt(index, val);
        return index;
      }
      

      if ((state == 1) && (_set[index] == val)) {
        return -index - 1;
      }
      
    }
    while (index != loopIndex);
    


    if (firstRemoved != -1) {
      insertKeyAt(firstRemoved, val);
      return firstRemoved;
    }
    

    throw new IllegalStateException("No free or removed slots available. Key set full?!!");
  }
  
  void insertKeyAt(int index, long val) {
    _set[index] = val;
    _states[index] = 1;
  }
  

  protected int XinsertKey(long key)
  {
    byte[] states = _states;
    long[] set = _set;
    int length = states.length;
    int hash = HashFunctions.hash(key) & 0x7FFFFFFF;
    int index = hash % length;
    byte state = states[index];
    
    consumeFreeSlot = false;
    
    if (state == 0) {
      consumeFreeSlot = true;
      set[index] = key;
      states[index] = 1;
      
      return index; }
    if ((state == 1) && (set[index] == key)) {
      return -index - 1;
    }
    
    int probe = 1 + hash % (length - 2);
    












    if (state != 2)
    {
      do
      {
        index -= probe;
        if (index < 0) {
          index += length;
        }
        state = states[index];
      } while ((state == 1) && (set[index] != key));
    }
    



    if (state == 2) {
      int firstRemoved = index;
      while ((state != 0) && ((state == 2) || (set[index] != key))) {
        index -= probe;
        if (index < 0) {
          index += length;
        }
        state = states[index];
      }
      
      if (state == 1) {
        return -index - 1;
      }
      set[index] = key;
      states[index] = 1;
      
      return firstRemoved;
    }
    

    if (state == 1) {
      return -index - 1;
    }
    consumeFreeSlot = true;
    set[index] = key;
    states[index] = 1;
    
    return index;
  }
  



  public void writeExternal(ObjectOutput out)
    throws IOException
  {
    out.writeByte(0);
    

    super.writeExternal(out);
    

    out.writeLong(no_entry_key);
    

    out.writeDouble(no_entry_value);
  }
  

  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    in.readByte();
    

    super.readExternal(in);
    

    no_entry_key = in.readLong();
    

    no_entry_value = in.readDouble();
  }
}