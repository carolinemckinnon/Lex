package gnu.trove.impl.hash;

import gnu.trove.impl.Constants;
import gnu.trove.impl.HashFunctions;
import gnu.trove.procedure.TCharProcedure;
import java.util.Arrays;

















































public abstract class TCharHash
  extends TPrimitiveHash
{
  static final long serialVersionUID = 1L;
  public transient char[] _set;
  protected char no_entry_value;
  protected boolean consumeFreeSlot;
  
  public TCharHash()
  {
    no_entry_value = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
    
    if (no_entry_value != 0) {
      Arrays.fill(_set, no_entry_value);
    }
  }
  







  public TCharHash(int initialCapacity)
  {
    super(initialCapacity);
    no_entry_value = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
    
    if (no_entry_value != 0) {
      Arrays.fill(_set, no_entry_value);
    }
  }
  








  public TCharHash(int initialCapacity, float loadFactor)
  {
    super(initialCapacity, loadFactor);
    no_entry_value = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
    
    if (no_entry_value != 0) {
      Arrays.fill(_set, no_entry_value);
    }
  }
  









  public TCharHash(int initialCapacity, float loadFactor, char no_entry_value)
  {
    super(initialCapacity, loadFactor);
    this.no_entry_value = no_entry_value;
    
    if (no_entry_value != 0) {
      Arrays.fill(_set, no_entry_value);
    }
  }
  







  public char getNoEntryValue()
  {
    return no_entry_value;
  }
  









  protected int setUp(int initialCapacity)
  {
    int capacity = super.setUp(initialCapacity);
    _set = new char[capacity];
    return capacity;
  }
  






  public boolean contains(char val)
  {
    return index(val) >= 0;
  }
  







  public boolean forEach(TCharProcedure procedure)
  {
    byte[] states = _states;
    char[] set = _set;
    for (int i = set.length; i-- > 0;) {
      if ((states[i] == 1) && (!procedure.execute(set[i]))) {
        return false;
      }
    }
    return true;
  }
  





  protected void removeAt(int index)
  {
    _set[index] = no_entry_value;
    super.removeAt(index);
  }
  








  protected int index(char val)
  {
    byte[] states = _states;
    char[] set = _set;
    int length = states.length;
    int hash = HashFunctions.hash(val) & 0x7FFFFFFF;
    int index = hash % length;
    byte state = states[index];
    
    if (state == 0) {
      return -1;
    }
    if ((state == 1) && (set[index] == val)) {
      return index;
    }
    return indexRehashed(val, index, hash, state);
  }
  
  int indexRehashed(char key, int index, int hash, byte state)
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
  









  protected int insertKey(char val)
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
  
  int insertKeyRehash(char val, int index, int hash, byte state)
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
  
  void insertKeyAt(int index, char val) {
    _set[index] = val;
    _states[index] = 1;
  }
}
