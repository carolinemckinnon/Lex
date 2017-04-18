package gnu.trove.decorator;

import gnu.trove.iterator.TByteLongIterator;
import gnu.trove.map.TByteLongMap;
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













































public class TByteLongMapDecorator
  extends AbstractMap<Byte, Long>
  implements Map<Byte, Long>, Externalizable, Cloneable
{
  static final long serialVersionUID = 1L;
  protected TByteLongMap _map;
  
  public TByteLongMapDecorator() {}
  
  public TByteLongMapDecorator(TByteLongMap map)
  {
    _map = map;
  }
  





  public TByteLongMap getMap()
  {
    return _map;
  }
  



  public Long put(Byte key, Long value)
  {
    byte k;
    


    byte k;
    

    if (key == null) {
      k = _map.getNoEntryKey();
    } else
      k = unwrapKey(key);
    long v;
    long v; if (value == null) {
      v = _map.getNoEntryValue();
    } else {
      v = unwrapValue(value);
    }
    long retval = _map.put(k, v);
    if (retval == _map.getNoEntryValue()) {
      return null;
    }
    return wrapValue(retval);
  }
  



  public Long get(Object key)
  {
    byte k;
    


    if (key != null) { byte k;
      if ((key instanceof Byte)) {
        k = unwrapKey(key);
      } else {
        return null;
      }
    } else {
      k = _map.getNoEntryKey();
    }
    long v = _map.get(k);
    


    if (v == _map.getNoEntryValue()) {
      return null;
    }
    return wrapValue(v);
  }
  




  public void clear()
  {
    _map.clear();
  }
  



  public Long remove(Object key)
  {
    byte k;
    


    if (key != null) { byte k;
      if ((key instanceof Byte)) {
        k = unwrapKey(key);
      } else {
        return null;
      }
    } else {
      k = _map.getNoEntryKey();
    }
    long v = _map.remove(k);
    


    if (v == _map.getNoEntryValue()) {
      return null;
    }
    return wrapValue(v);
  }
  






  public Set<Map.Entry<Byte, Long>> entrySet()
  {
    new AbstractSet() {
      public int size() {
        return _map.size();
      }
      
      public boolean isEmpty() {
        return TByteLongMapDecorator.this.isEmpty();
      }
      
      public boolean contains(Object o) {
        if ((o instanceof Map.Entry)) {
          Object k = ((Map.Entry)o).getKey();
          Object v = ((Map.Entry)o).getValue();
          return (containsKey(k)) && (get(k).equals(v));
        }
        
        return false;
      }
      
      public Iterator<Map.Entry<Byte, Long>> iterator()
      {
        new Iterator() {
          private final TByteLongIterator it = _map.iterator();
          
          public Map.Entry<Byte, Long> next() {
            it.advance();
            byte ik = it.key();
            final Byte key = ik == _map.getNoEntryKey() ? null : wrapKey(ik);
            long iv = it.value();
            final Long v = iv == _map.getNoEntryValue() ? null : wrapValue(iv);
            new Map.Entry() {
              private Long val = v;
              
              public boolean equals(Object o) {
                return ((o instanceof Map.Entry)) && (((Map.Entry)o).getKey().equals(key)) && (((Map.Entry)o).getValue().equals(val));
              }
              

              public Byte getKey()
              {
                return key;
              }
              
              public Long getValue() {
                return val;
              }
              
              public int hashCode() {
                return key.hashCode() + val.hashCode();
              }
              
              public Long setValue(Long value) {
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
      
      public boolean add(Map.Entry<Byte, Long> o) {
        throw new UnsupportedOperationException();
      }
      
      public boolean remove(Object o) {
        boolean modified = false;
        if (contains(o))
        {
          Byte key = (Byte)((Map.Entry)o).getKey();
          _map.remove(unwrapKey(key));
          modified = true;
        }
        return modified;
      }
      
      public boolean addAll(Collection<? extends Map.Entry<Byte, Long>> c) {
        throw new UnsupportedOperationException();
      }
      
      public void clear() {
        TByteLongMapDecorator.this.clear();
      }
    };
  }
  






  public boolean containsValue(Object val)
  {
    return ((val instanceof Long)) && (_map.containsValue(unwrapValue(val)));
  }
  






  public boolean containsKey(Object key)
  {
    if (key == null) return _map.containsKey(_map.getNoEntryKey());
    return ((key instanceof Byte)) && (_map.containsKey(unwrapKey(key)));
  }
  





  public int size()
  {
    return _map.size();
  }
  





  public boolean isEmpty()
  {
    return size() == 0;
  }
  







  public void putAll(Map<? extends Byte, ? extends Long> map)
  {
    Iterator<? extends Map.Entry<? extends Byte, ? extends Long>> it = map.entrySet().iterator();
    
    for (int i = map.size(); i-- > 0;) {
      Map.Entry<? extends Byte, ? extends Long> e = (Map.Entry)it.next();
      put((Byte)e.getKey(), (Long)e.getValue());
    }
  }
  






  protected Byte wrapKey(byte k)
  {
    return Byte.valueOf(k);
  }
  






  protected byte unwrapKey(Object key)
  {
    return ((Byte)key).byteValue();
  }
  






  protected Long wrapValue(long k)
  {
    return Long.valueOf(k);
  }
  






  protected long unwrapValue(Object value)
  {
    return ((Long)value).longValue();
  }
  



  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    in.readByte();
    

    _map = ((TByteLongMap)in.readObject());
  }
  

  public void writeExternal(ObjectOutput out)
    throws IOException
  {
    out.writeByte(0);
    

    out.writeObject(_map);
  }
}
