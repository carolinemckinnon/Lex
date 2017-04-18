package gnu.trove.decorator;

import gnu.trove.list.TLongList;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.AbstractList;
import java.util.List;

















































public class TLongListDecorator
  extends AbstractList<Long>
  implements List<Long>, Externalizable, Cloneable
{
  static final long serialVersionUID = 1L;
  protected TLongList list;
  
  public TLongListDecorator() {}
  
  public TLongListDecorator(TLongList list)
  {
    this.list = list;
  }
  





  public TLongList getList()
  {
    return list;
  }
  

  public int size()
  {
    return list.size();
  }
  

  public Long get(int index)
  {
    long value = list.get(index);
    if (value == list.getNoEntryValue()) return null;
    return Long.valueOf(value);
  }
  

  public Long set(int index, Long value)
  {
    long previous_value = list.set(index, value.longValue());
    if (previous_value == list.getNoEntryValue()) return null;
    return Long.valueOf(previous_value);
  }
  

  public void add(int index, Long value)
  {
    list.insert(index, value.longValue());
  }
  

  public Long remove(int index)
  {
    long previous_value = list.removeAt(index);
    if (previous_value == list.getNoEntryValue()) return null;
    return Long.valueOf(previous_value);
  }
  



  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    in.readByte();
    

    list = ((TLongList)in.readObject());
  }
  

  public void writeExternal(ObjectOutput out)
    throws IOException
  {
    out.writeByte(0);
    

    out.writeObject(list);
  }
}
