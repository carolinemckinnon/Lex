package gnu.trove.stack.array;

import gnu.trove.list.array.TLongArrayList;
import gnu.trove.stack.TLongStack;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;






































public class TLongArrayStack
  implements TLongStack, Externalizable
{
  static final long serialVersionUID = 1L;
  protected TLongArrayList _list;
  public static final int DEFAULT_CAPACITY = 10;
  
  public TLongArrayStack()
  {
    this(10);
  }
  






  public TLongArrayStack(int capacity)
  {
    _list = new TLongArrayList(capacity);
  }
  







  public TLongArrayStack(int capacity, long no_entry_value)
  {
    _list = new TLongArrayList(capacity, no_entry_value);
  }
  






  public TLongArrayStack(TLongStack stack)
  {
    if ((stack instanceof TLongArrayStack)) {
      TLongArrayStack array_stack = (TLongArrayStack)stack;
      _list = new TLongArrayList(_list);
    } else {
      throw new UnsupportedOperationException("Only support TLongArrayStack");
    }
  }
  







  public long getNoEntryValue()
  {
    return _list.getNoEntryValue();
  }
  





  public void push(long val)
  {
    _list.add(val);
  }
  





  public long pop()
  {
    return _list.removeAt(_list.size() - 1);
  }
  





  public long peek()
  {
    return _list.get(_list.size() - 1);
  }
  



  public int size()
  {
    return _list.size();
  }
  



  public void clear()
  {
    _list.clear();
  }
  






  public long[] toArray()
  {
    long[] retval = _list.toArray();
    reverse(retval, 0, size());
    return retval;
  }
  











  public void toArray(long[] dest)
  {
    int size = size();
    int start = size - dest.length;
    if (start < 0) {
      start = 0;
    }
    
    int length = Math.min(size, dest.length);
    _list.toArray(dest, start, length);
    reverse(dest, 0, length);
    if (dest.length > size) {
      dest[size] = _list.getNoEntryValue();
    }
  }
  







  private void reverse(long[] dest, int from, int to)
  {
    if (from == to) {
      return;
    }
    if (from > to) {
      throw new IllegalArgumentException("from cannot be greater than to");
    }
    int i = from; for (int j = to - 1; i < j; j--) {
      swap(dest, i, j);i++;
    }
  }
  







  private void swap(long[] dest, int i, int j)
  {
    long tmp = dest[i];
    dest[i] = dest[j];
    dest[j] = tmp;
  }
  





  public String toString()
  {
    StringBuilder buf = new StringBuilder("{");
    for (int i = _list.size() - 1; i > 0; i--) {
      buf.append(_list.get(i));
      buf.append(", ");
    }
    if (size() > 0) {
      buf.append(_list.get(0));
    }
    buf.append("}");
    return buf.toString();
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    
    TLongArrayStack that = (TLongArrayStack)o;
    
    return _list.equals(_list);
  }
  
  public int hashCode()
  {
    return _list.hashCode();
  }
  
  public void writeExternal(ObjectOutput out)
    throws IOException
  {
    out.writeByte(0);
    

    out.writeObject(_list);
  }
  


  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    in.readByte();
    

    _list = ((TLongArrayList)in.readObject());
  }
}
