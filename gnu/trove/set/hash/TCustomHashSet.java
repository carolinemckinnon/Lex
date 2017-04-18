package gnu.trove.set.hash;

import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TCustomObjectHash;
import gnu.trove.iterator.hash.TObjectHashIterator;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.procedure.array.ToObjectArrayProceedure;
import gnu.trove.strategy.HashingStrategy;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;


































public class TCustomHashSet<E>
  extends TCustomObjectHash<E>
  implements Set<E>, Iterable<E>, Externalizable
{
  static final long serialVersionUID = 1L;
  
  public TCustomHashSet() {}
  
  public TCustomHashSet(HashingStrategy<? super E> strategy)
  {
    super(strategy);
  }
  







  public TCustomHashSet(HashingStrategy<? super E> strategy, int initialCapacity)
  {
    super(strategy, initialCapacity);
  }
  










  public TCustomHashSet(HashingStrategy<? super E> strategy, int initialCapacity, float loadFactor)
  {
    super(strategy, initialCapacity, loadFactor);
  }
  








  public TCustomHashSet(HashingStrategy<? super E> strategy, Collection<? extends E> collection)
  {
    this(strategy, collection.size());
    addAll(collection);
  }
  






  public boolean add(E obj)
  {
    int index = insertKey(obj);
    
    if (index < 0) {
      return false;
    }
    
    postInsertHook(consumeFreeSlot);
    return true;
  }
  

  public boolean equals(Object other)
  {
    if (!(other instanceof Set)) {
      return false;
    }
    Set that = (Set)other;
    if (that.size() != size()) {
      return false;
    }
    return containsAll(that);
  }
  
  public int hashCode()
  {
    TCustomHashSet<E>.HashProcedure p = new HashProcedure(null);
    forEach(p);
    return p.getHashCode();
  }
  
  private final class HashProcedure implements TObjectProcedure<E> { private HashProcedure() {}
    
    private int h = 0;
    
    public int getHashCode() {
      return h;
    }
    
    public final boolean execute(E key) {
      h += HashFunctions.hash(key);
      return true;
    }
  }
  






  protected void rehash(int newCapacity)
  {
    int oldCapacity = _set.length;
    int oldSize = size();
    Object[] oldSet = _set;
    
    _set = new Object[newCapacity];
    Arrays.fill(_set, FREE);
    
    for (int i = oldCapacity; i-- > 0;) {
      E o = oldSet[i];
      if ((o != FREE) && (o != REMOVED)) {
        int index = insertKey(o);
        if (index < 0) {
          throwObjectContractViolation(_set[(-index - 1)], o, size(), oldSize, oldSet);
        }
      }
    }
  }
  






  public Object[] toArray()
  {
    Object[] result = new Object[size()];
    forEach(new ToObjectArrayProceedure(result));
    return result;
  }
  







  public <T> T[] toArray(T[] a)
  {
    int size = size();
    if (a.length < size) {
      a = (Object[])Array.newInstance(a.getClass().getComponentType(), size);
    }
    
    forEach(new ToObjectArrayProceedure(a));
    








    if (a.length > size) {
      a[size] = null;
    }
    
    return a;
  }
  

  public void clear()
  {
    super.clear();
    
    Arrays.fill(_set, 0, _set.length, FREE);
  }
  







  public boolean remove(Object obj)
  {
    int index = index(obj);
    if (index >= 0) {
      removeAt(index);
      return true;
    }
    return false;
  }
  







  public TObjectHashIterator<E> iterator()
  {
    return new TObjectHashIterator(this);
  }
  








  public boolean containsAll(Collection<?> collection)
  {
    for (Iterator i = collection.iterator(); i.hasNext();) {
      if (!contains(i.next())) {
        return false;
      }
    }
    return true;
  }
  






  public boolean addAll(Collection<? extends E> collection)
  {
    boolean changed = false;
    int size = collection.size();
    
    ensureCapacity(size);
    Iterator<? extends E> it = collection.iterator();
    while (size-- > 0) {
      if (add(it.next())) {
        changed = true;
      }
    }
    return changed;
  }
  






  public boolean removeAll(Collection<?> collection)
  {
    boolean changed = false;
    int size = collection.size();
    

    Iterator it = collection.iterator();
    while (size-- > 0) {
      if (remove(it.next())) {
        changed = true;
      }
    }
    return changed;
  }
  








  public boolean retainAll(Collection<?> collection)
  {
    boolean changed = false;
    int size = size();
    Iterator<E> it = iterator();
    while (size-- > 0) {
      if (!collection.contains(it.next())) {
        it.remove();
        changed = true;
      }
    }
    return changed;
  }
  
  public String toString()
  {
    final StringBuilder buf = new StringBuilder("{");
    forEach(new TObjectProcedure() {
      private boolean first = true;
      
      public boolean execute(Object value)
      {
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
  
  public void writeExternal(ObjectOutput out)
    throws IOException
  {
    out.writeByte(1);
    

    super.writeExternal(out);
    

    out.writeInt(_size);
    

    for (int i = _set.length; i-- > 0;) {
      if ((_set[i] != REMOVED) && (_set[i] != FREE)) {
        out.writeObject(_set[i]);
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
    

    while (size-- > 0) {
      E val = in.readObject();
      add(val);
    }
  }
}
