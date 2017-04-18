package gnu.trove.impl.sync;

import gnu.trove.function.TIntFunction;
import gnu.trove.list.TIntList;
import gnu.trove.procedure.TIntProcedure;
import java.util.Random;
import java.util.RandomAccess;











































public class TSynchronizedIntList
  extends TSynchronizedIntCollection
  implements TIntList
{
  static final long serialVersionUID = -7754090372962971524L;
  final TIntList list;
  
  public TSynchronizedIntList(TIntList list)
  {
    super(list);
    this.list = list;
  }
  
  public TSynchronizedIntList(TIntList list, Object mutex) { super(list, mutex);
    this.list = list;
  }
  
  public boolean equals(Object o) {
    synchronized (mutex) { return list.equals(o);
    } }
  
  public int hashCode() { synchronized (mutex) { return list.hashCode();
    }
  }
  
  public int get(int index) { synchronized (mutex) { return list.get(index);
    } }
  
  public int set(int index, int element) { synchronized (mutex) { return list.set(index, element);
    } }
  
  public void set(int offset, int[] values) { synchronized (mutex) { list.set(offset, values);
    } }
  
  public void set(int offset, int[] values, int valOffset, int length) { synchronized (mutex) { list.set(offset, values, valOffset, length);
    }
  }
  
  public int replace(int offset, int val) { synchronized (mutex) { return list.replace(offset, val);
    } }
  
  public void remove(int offset, int length) { synchronized (mutex) { list.remove(offset, length);
    } }
  
  public int removeAt(int offset) { synchronized (mutex) { return list.removeAt(offset);
    }
  }
  
  public void add(int[] vals) { synchronized (mutex) { list.add(vals);
    } }
  
  public void add(int[] vals, int offset, int length) { synchronized (mutex) { list.add(vals, offset, length);
    }
  }
  
  public void insert(int offset, int value) { synchronized (mutex) { list.insert(offset, value);
    } }
  
  public void insert(int offset, int[] values) { synchronized (mutex) { list.insert(offset, values);
    } }
  
  public void insert(int offset, int[] values, int valOffset, int len) { synchronized (mutex) { list.insert(offset, values, valOffset, len);
    }
  }
  
  public int indexOf(int o) { synchronized (mutex) { return list.indexOf(o);
    } }
  
  public int lastIndexOf(int o) { synchronized (mutex) { return list.lastIndexOf(o);
    }
  }
  






  public TIntList subList(int fromIndex, int toIndex)
  {
    synchronized (mutex) {
      return new TSynchronizedIntList(list.subList(fromIndex, toIndex), mutex);
    }
  }
  
  public int[] toArray(int offset, int len)
  {
    synchronized (mutex) { return list.toArray(offset, len);
    } }
  
  public int[] toArray(int[] dest, int offset, int len) { synchronized (mutex) { return list.toArray(dest, offset, len);
    } }
  
  public int[] toArray(int[] dest, int source_pos, int dest_pos, int len) { synchronized (mutex) { return list.toArray(dest, source_pos, dest_pos, len);
    }
  }
  
  public int indexOf(int offset, int value) { synchronized (mutex) { return list.indexOf(offset, value);
    } }
  
  public int lastIndexOf(int offset, int value) { synchronized (mutex) { return list.lastIndexOf(offset, value);
    }
  }
  
  public void fill(int val) { synchronized (mutex) { list.fill(val);
    } }
  
  public void fill(int fromIndex, int toIndex, int val) { synchronized (mutex) { list.fill(fromIndex, toIndex, val);
    }
  }
  
  public void reverse() { synchronized (mutex) { list.reverse();
    } }
  
  public void reverse(int from, int to) { synchronized (mutex) { list.reverse(from, to);
    }
  }
  
  public void shuffle(Random rand) { synchronized (mutex) { list.shuffle(rand);
    }
  }
  
  public void sort() { synchronized (mutex) { list.sort();
    } }
  
  public void sort(int fromIndex, int toIndex) { synchronized (mutex) { list.sort(fromIndex, toIndex);
    }
  }
  
  public int binarySearch(int value) { synchronized (mutex) { return list.binarySearch(value);
    } }
  
  public int binarySearch(int value, int fromIndex, int toIndex) { synchronized (mutex) { return list.binarySearch(value, fromIndex, toIndex);
    }
  }
  
  public TIntList grep(TIntProcedure condition) { synchronized (mutex) { return list.grep(condition);
    } }
  
  public TIntList inverseGrep(TIntProcedure condition) { synchronized (mutex) { return list.inverseGrep(condition);
    } }
  
  public int max() { synchronized (mutex) { return list.max(); } }
  public int min() { synchronized (mutex) { return list.min(); } }
  public int sum() { synchronized (mutex) { return list.sum();
    } }
  
  public boolean forEachDescending(TIntProcedure procedure) { synchronized (mutex) { return list.forEachDescending(procedure);
    }
  }
  
  public void transformValues(TIntFunction function) { synchronized (mutex) { list.transformValues(function);
    }
  }
  










  private Object readResolve()
  {
    return (list instanceof RandomAccess) ? new TSynchronizedRandomAccessIntList(list) : this;
  }
}
