package edu.ucla.sspace.util;

import java.util.ArrayList;
import java.util.Collection;




























public class GrowableArrayList<E>
  extends ArrayList<E>
{
  private static final long serialVersionUID = 1L;
  
  public GrowableArrayList() {}
  
  public GrowableArrayList(Collection<? extends E> c)
  {
    super(c);
  }
  
  public GrowableArrayList(int capacity) {
    super(capacity);
  }
  
  private void checkIndex(int index)
  {
    if (index >= size()) {
      for (int i = size(); i <= index; i++) {
        super.add(null);
      }
    }
  }
  
  public void add(int index, E element) {
    checkIndex(index);
    super.add(index, element);
  }
  
  public E get(int index)
  {
    return index >= size() ? null : super.get(index);
  }
  
  public E remove(int index) {
    return index >= size() ? null : super.remove(index);
  }
  
  protected void removeRange(int fromIndex, int toIndex) {
    if ((toIndex >= size()) && (fromIndex < size())) {
      super.removeRange(fromIndex, size() - 1);
    }
  }
  
  public E set(int index, E element) {
    checkIndex(index);
    return super.set(index, element);
  }
}
