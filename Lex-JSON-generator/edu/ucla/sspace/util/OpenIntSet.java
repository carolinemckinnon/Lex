package edu.ucla.sspace.util;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;






































































public class OpenIntSet
  extends AbstractSet<Integer>
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private static final int EMPTY_MARKER = 0;
  private static final int DELETED_MARKER = Integer.MAX_VALUE;
  private int[] buckets;
  private boolean isEmptyMarkerValuePresent;
  private boolean isDeletedMarkerValuePresent;
  private int size;
  
  public OpenIntSet()
  {
    this(4);
  }
  








  public OpenIntSet(int size)
  {
    if (size < 0) {
      throw new IllegalArgumentException("size must be non-negative");
    }
    int n = 1;
    for (int i = 2; i < 32; i++) {
      if (n << i >= size) {
        buckets = new int[n << i];
        break;
      }
    }
    isEmptyMarkerValuePresent = false;
    isDeletedMarkerValuePresent = false;
    size = 0;
  }
  



  public OpenIntSet(Collection<Integer> ints)
  {
    this(ints.size());
    addAll(ints);
  }
  




  public OpenIntSet(OpenIntSet ints)
  {
    int len = buckets.length;
    buckets = new int[len];
    System.arraycopy(buckets, 0, buckets, 0, len);
    
    size = size;
    isEmptyMarkerValuePresent = isEmptyMarkerValuePresent;
    isDeletedMarkerValuePresent = isDeletedMarkerValuePresent;
  }
  


  public boolean add(Integer i)
  {
    return add(i.intValue());
  }
  

  public boolean add(int i)
  {
    if (i == 0) {
      if (!isEmptyMarkerValuePresent) {
        isEmptyMarkerValuePresent = true;
        size += 1;
        return true;
      }
      return false;
    }
    

    if (i == Integer.MAX_VALUE) {
      if (!isDeletedMarkerValuePresent) {
        isDeletedMarkerValuePresent = true;
        size += 1;
        return true;
      }
      return false;
    }
    

    int bucket = findIndex(buckets, i);
    while (bucket == -1) {
      rebuildTable();
      bucket = findIndex(buckets, i);
    }
    
    int curVal = buckets[bucket];
    if (curVal == i) {
      return false;
    }
    assert ((curVal == 0) || (curVal == Integer.MAX_VALUE)) : 
      "overwriting existing value";
    buckets[bucket] = i;
    size += 1;
    return true;
  }
  









  private static int findIndex(int[] buckets, int i)
  {
    int slot = i & buckets.length - 1;
    int initial = slot;
    
    int firstDeletedSeen = -1;
    do {
      int val = buckets[slot];
      






      if ((val == Integer.MAX_VALUE) && (firstDeletedSeen < 0)) {
        firstDeletedSeen = slot;
      } else {
        if (val == i) {
          return slot;
        }
        

        if (val == 0)
          return firstDeletedSeen < 0 ? slot : firstDeletedSeen;
      }
    } while ((slot = (slot + 1) % buckets.length) != initial);
    



    return -1;
  }
  
  public boolean contains(Object o) {
    if ((o instanceof Integer)) {
      return contains(((Integer)o).intValue());
    }
    throw new ClassCastException();
  }
  
  public boolean contains(int i)
  {
    if (i == 0)
      return isEmptyMarkerValuePresent;
    if (i == Integer.MAX_VALUE) {
      return isDeletedMarkerValuePresent;
    }
    

    int bucket = findIndex(buckets, i);
    return (bucket >= 0) && (buckets[bucket] == i);
  }
  
  public void clear() {
    Arrays.fill(buckets, 0);
    isEmptyMarkerValuePresent = false;
    isDeletedMarkerValuePresent = false;
    size = 0;
  }
  
  public boolean isEmpty() {
    return size == 0;
  }
  
  public Iterator<Integer> iterator() {
    return new IntIterator();
  }
  
  private void rebuildTable() {
    int newSize = buckets.length << 1;
    int[] newBuckets = new int[newSize];
    
    for (int i : buckets)
    {

      if ((i != 0) && (i != Integer.MAX_VALUE)) {
        int index = findIndex(newBuckets, i);
        newBuckets[index] = i;
      }
    }
    buckets = newBuckets;
  }
  
  public boolean remove(Integer i) {
    return remove(i.intValue());
  }
  
  public boolean remove(int i) {
    boolean wasPresent = false;
    
    if (i == 0) {
      wasPresent = isEmptyMarkerValuePresent;
      isEmptyMarkerValuePresent = false;
    }
    else if (i == Integer.MAX_VALUE) {
      wasPresent = isDeletedMarkerValuePresent;
      isDeletedMarkerValuePresent = false;
    }
    else
    {
      int bucket = findIndex(buckets, i);
      


      if ((bucket >= 0) && (buckets[bucket] == i)) {
        buckets[bucket] = Integer.MAX_VALUE;
        wasPresent = true;
      }
    }
    

    if (wasPresent)
      size -= 1;
    return wasPresent;
  }
  
  public int size() {
    return size;
  }
  
  private class IntIterator
    implements Iterator<Integer>
  {
    int cur;
    int next;
    int nextIndex;
    boolean alreadyRemoved;
    boolean returnedEmptyMarker = false;
    boolean returnedDeletedMarker = false;
    
    public IntIterator()
    {
      nextIndex = -1;
      cur = -1;
      next = -1;
      alreadyRemoved = true;
      returnedEmptyMarker = false;
      returnedDeletedMarker = false;
      advance();
    }
    
    private void advance() {
      if ((!returnedEmptyMarker) && (isEmptyMarkerValuePresent)) {
        next = 0;
      }
      else if ((!returnedDeletedMarker) && (isDeletedMarkerValuePresent)) {
        next = Integer.MAX_VALUE;
      }
      else {
        int j = nextIndex + 1;
        while ((j < buckets.length) && (
          (buckets[j] == 0) || 
          (buckets[j] == Integer.MAX_VALUE))) {
          j++;
        }
        
        nextIndex = (j == buckets.length ? -1 : j);
        next = (nextIndex >= 0 ? buckets[nextIndex] : -1);
      }
    }
    
    public boolean hasNext() {
      return (nextIndex >= 0) || 
        ((isEmptyMarkerValuePresent) && (!returnedEmptyMarker)) || (
        (isDeletedMarkerValuePresent) && (!returnedDeletedMarker));
    }
    
    public Integer next() {
      if (!hasNext())
        throw new NoSuchElementException();
      cur = next;
      if (next == 0) {
        returnedEmptyMarker = true;
      }
      else if (next == Integer.MAX_VALUE) {
        returnedDeletedMarker = true;
      }
      advance();
      
      alreadyRemoved = false;
      return Integer.valueOf(cur);
    }
    
    public void remove() {
      if (alreadyRemoved)
        throw new IllegalStateException();
      alreadyRemoved = true;
      remove(cur);
    }
  }
}
