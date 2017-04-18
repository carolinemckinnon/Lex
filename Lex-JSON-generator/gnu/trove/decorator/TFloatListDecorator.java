package gnu.trove.decorator;

import gnu.trove.list.TFloatList;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.AbstractList;
import java.util.List;

















































public class TFloatListDecorator
  extends AbstractList<Float>
  implements List<Float>, Externalizable, Cloneable
{
  static final long serialVersionUID = 1L;
  protected TFloatList list;
  
  public TFloatListDecorator() {}
  
  public TFloatListDecorator(TFloatList list)
  {
    this.list = list;
  }
  





  public TFloatList getList()
  {
    return list;
  }
  

  public int size()
  {
    return list.size();
  }
  

  public Float get(int index)
  {
    float value = list.get(index);
    if (value == list.getNoEntryValue()) return null;
    return Float.valueOf(value);
  }
  

  public Float set(int index, Float value)
  {
    float previous_value = list.set(index, value.floatValue());
    if (previous_value == list.getNoEntryValue()) return null;
    return Float.valueOf(previous_value);
  }
  

  public void add(int index, Float value)
  {
    list.insert(index, value.floatValue());
  }
  

  public Float remove(int index)
  {
    float previous_value = list.removeAt(index);
    if (previous_value == list.getNoEntryValue()) return null;
    return Float.valueOf(previous_value);
  }
  



  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    in.readByte();
    

    list = ((TFloatList)in.readObject());
  }
  

  public void writeExternal(ObjectOutput out)
    throws IOException
  {
    out.writeByte(0);
    

    out.writeObject(list);
  }
}
