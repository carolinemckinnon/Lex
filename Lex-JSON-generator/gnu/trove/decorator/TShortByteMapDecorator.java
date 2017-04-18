package gnu.trove.decorator;

import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.map.TShortByteMap;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;













































public class TShortByteMapDecorator
  extends AbstractMap<Short, Byte>
  implements Map<Short, Byte>, Externalizable, Cloneable
{
  static final long serialVersionUID = 1L;
  protected TShortByteMap _map;
  
  public TShortByteMapDecorator() {}
  
  public TShortByteMapDecorator(TShortByteMap map)
  {
    _map = map;
  }
  





  public TShortByteMap getMap()
  {
    return _map;
  }
  



  public Byte put(Short key, Byte value)
  {
    short k;
    


    short k;
    

    if (key == null) {
      k = _map.getNoEntryKey();
    } else
      k = unwrapKey(key);
    byte v;
    byte v; if (value == null) {
      v = _map.getNoEntryValue();
    } else {
      v = unwrapValue(value);
    }
    byte retval = _map.put(k, v);
    if (retval == _map.getNoEntryValue()) {
      return null;
    }
    return wrapValue(retval);
  }
  



  public Byte get(Object key)
  {
    short k;
    


    if (key != null) { short k;
      if ((key instanceof Short)) {
        k = unwrapKey(key);
      } else {
        return null;
      }
    } else {
      k = _map.getNoEntryKey();
    }
    byte v = _map.get(k);
    


    if (v == _map.getNoEntryValue()) {
      return null;
    }
    return wrapValue(v);
  }
  




  public void clear()
  {
    _map.clear();
  }
  



  public Byte remove(Object key)
  {
    short k;
    


    if (key != null) { short k;
      if ((key instanceof Short)) {
        k = unwrapKey(key);
      } else {
        return null;
      }
    } else {
      k = _map.getNoEntryKey();
    }
    byte v = _map.remove(k);
    


    if (v == _map.getNoEntryValue()) {
      return null;
    }
    return wrapValue(v);
  }
  






  public Set<Map.Entry<Short, Byte>> entrySet()
  {
    new AbstractSet() {
      public int size() {
        return _map.size();
      }
      
      public boolean isEmpty() {
        return TShortByteMapDecorator.this.isEmpty();
      }
      
      public boolean contains(Object o) {
        if ((o instanceof Map.Entry)) {
          Object k = ((Map.Entry)o).getKey();
          Object v = ((Map.Entry)o).getValue();
          return (containsKey(k)) && (get(k).equals(v));
        }
        
        return false;
      }
      
      public Iterator<Map.Entry<Short, Byte>> iterator()
      {
        new Iterator() {
          private final TShortByteIterator it = _map.iterator();
          
          public Map.Entry<Short, Byte> next() {
            it.advance();
            short ik = it.key();
            final Short key = ik == _map.getNoEntryKey() ? null : wrapKey(ik);
            byte iv = it.value();
            final Byte v = iv == _map.getNoEntryValue() ? null : wrapValue(iv);
            new Map.Entry() {
              private Byte val = v;
              
              public boolean equals(Object o) {
                return ((o instanceof Map.Entry)) && (((Map.Entry)o).getKey().equals(key)) && (((Map.Entry)o).getValue().equals(val));
              }
              

              public Short getKey()
              {
                return key;
              }
              
              public Byte getValue() {
                return val;
              }
              
              public int hashCode() {
                return key.hashCode() + val.hashCode();
              }
              
              public Byte setValue(Byte value) {
                val = value;
                return put(key, value);
              }
            };
          }
          
          public boolean hasNext() {
            return it.hasNext();
          }
          
          public void remove() {
            it.remove();
          }
        };
      }
      
      public boolean add(Map.Entry<Short, Byte> o) {
        throw new UnsupportedOperationException();
      }
      
      public boolean remove(Object o) {
        boolean modified = false;
        if (contains(o))
        {
          Short key = (Short)((Map.Entry)o).getKey();
          _map.remove(unwrapKey(key));
          modified = true;
        }
        return modified;
      }
      
      public boolean addAll(Collection<? extends Map.Entry<Short, Byte>> c) {
        throw new UnsupportedOperationException();
      }
      
      public void clear() {
        TShortByteMapDecorator.this.clear();
      }
    };
  }
  






  public boolean containsValue(Object val)
  {
    return ((val instanceof Byte)) && (_map.containsValue(unwrapValue(val)));
  }
  






  public boolean containsKey(Object key)
  {
    if (key == null) return _map.containsKey(_map.getNoEntryKey());
    return ((key instanceof Short)) && (_map.containsKey(unwrapKey(key)));
  }
  





  public int size()
  {
    return _map.size();
  }
  





  public boolean isEmpty()
  {
    return size() == 0;
  }
  







  public void putAll(Map<? extends Short, ? extends Byte> map)
  {
    Iterator<? extends Map.Entry<? extends Short, ? extends Byte>> it = map.entrySet().iterator();
    
    for (int i = map.size(); i-- > 0;) {
      Map.Entry<? extends Short, ? extends Byte> e = (Map.Entry)it.next();
      put((Short)e.getKey(), (Byte)e.getValue());
    }
  }
  






  protected Short wrapKey(short k)
  {
    return Short.valueOf(k);
  }
  






  protected short unwrapKey(Object key)
  {
    return ((Short)key).shortValue();
  }
  






  protected Byte wrapValue(byte k)
  {
    return Byte.valueOf(k);
  }
  






  protected byte unwrapValue(Object value)
  {
    return ((Byte)value).byteValue();
  }
  



  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    in.readByte();
    

    _map = ((TShortByteMap)in.readObject());
  }
  

  public void writeExternal(ObjectOutput out)
    throws IOException
  {
    out.writeByte(0);
    

    out.writeObject(_map);
  }
}
