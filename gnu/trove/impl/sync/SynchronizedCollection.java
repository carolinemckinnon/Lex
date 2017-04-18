package gnu.trove.impl.sync;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;





















class SynchronizedCollection<E>
  implements Collection<E>, Serializable
{
  private static final long serialVersionUID = 3053995032091335093L;
  final Collection<E> c;
  final Object mutex;
  
  SynchronizedCollection(Collection<E> c, Object mutex)
  {
    this.c = c;
    this.mutex = mutex;
  }
  
  public int size() {
    synchronized (mutex) { return c.size();
    } }
  
  public boolean isEmpty() { synchronized (mutex) { return c.isEmpty();
    } }
  
  public boolean contains(Object o) { synchronized (mutex) { return c.contains(o);
    } }
  
  public Object[] toArray() { synchronized (mutex) { return c.toArray();
    }
  }
  
  public <T> T[] toArray(T[] a) { synchronized (mutex) { return c.toArray(a);
    }
  }
  
  public Iterator<E> iterator() { return c.iterator(); }
  
  public boolean add(E e)
  {
    synchronized (mutex) { return c.add(e);
    } }
  
  public boolean remove(Object o) { synchronized (mutex) { return c.remove(o);
    }
  }
  
  public boolean containsAll(Collection<?> coll) { synchronized (mutex) { return c.containsAll(coll);
    } }
  
  public boolean addAll(Collection<? extends E> coll) { synchronized (mutex) { return c.addAll(coll);
    } }
  
  public boolean removeAll(Collection<?> coll) { synchronized (mutex) { return c.removeAll(coll);
    } }
  
  public boolean retainAll(Collection<?> coll) { synchronized (mutex) { return c.retainAll(coll);
    } }
  
  public void clear() { synchronized (mutex) { c.clear();
    } }
  
  public String toString() { synchronized (mutex) { return c.toString();
    } }
  
  private void writeObject(ObjectOutputStream s) throws IOException { synchronized (mutex) { s.defaultWriteObject();
    }
  }
}
