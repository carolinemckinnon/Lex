package gnu.trove.list.linked;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.iterator.TShortIterator;
import gnu.trove.list.TShortList;
import gnu.trove.procedure.TShortProcedure;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Random;




























public class TShortLinkedList
  implements TShortList, Externalizable
{
  short no_entry_value;
  int size;
  TShortLink head = null;
  TShortLink tail = head;
  
  public TShortLinkedList() {}
  
  public TShortLinkedList(short no_entry_value)
  {
    this.no_entry_value = no_entry_value;
  }
  
  public TShortLinkedList(TShortList list) {
    no_entry_value = list.getNoEntryValue();
    
    for (TShortIterator iterator = list.iterator(); iterator.hasNext();) {
      short next = iterator.next();
      add(next);
    }
  }
  
  public short getNoEntryValue()
  {
    return no_entry_value;
  }
  
  public int size()
  {
    return size;
  }
  
  public boolean isEmpty()
  {
    return size() == 0;
  }
  
  public boolean add(short val)
  {
    TShortLink l = new TShortLink(val);
    if (no(head)) {
      head = l;
      tail = l;
    } else {
      l.setPrevious(tail);
      tail.setNext(l);
      
      tail = l;
    }
    
    size += 1;
    return true;
  }
  
  public void add(short[] vals)
  {
    for (short val : vals) {
      add(val);
    }
  }
  
  public void add(short[] vals, int offset, int length)
  {
    for (int i = 0; i < length; i++) {
      short val = vals[(offset + i)];
      add(val);
    }
  }
  
  public void insert(int offset, short value)
  {
    TShortLinkedList tmp = new TShortLinkedList();
    tmp.add(value);
    insert(offset, tmp);
  }
  
  public void insert(int offset, short[] values)
  {
    insert(offset, link(values, 0, values.length));
  }
  
  public void insert(int offset, short[] values, int valOffset, int len)
  {
    insert(offset, link(values, valOffset, len));
  }
  
  void insert(int offset, TShortLinkedList tmp) {
    TShortLink l = getLinkAt(offset);
    
    size += size;
    
    if (l == head)
    {
      tail.setNext(head);
      head.setPrevious(tail);
      head = head;
      
      return;
    }
    
    if (no(l)) {
      if (size == 0)
      {
        head = head;
        tail = tail;
      }
      else {
        tail.setNext(head);
        head.setPrevious(tail);
        tail = tail;
      }
    } else {
      TShortLink prev = l.getPrevious();
      l.getPrevious().setNext(head);
      

      tail.setNext(l);
      l.setPrevious(tail);
      
      head.setPrevious(prev);
    }
  }
  
  static TShortLinkedList link(short[] values, int valOffset, int len) {
    TShortLinkedList ret = new TShortLinkedList();
    
    for (int i = 0; i < len; i++) {
      ret.add(values[(valOffset + i)]);
    }
    
    return ret;
  }
  
  public short get(int offset)
  {
    if (offset > size) {
      throw new IndexOutOfBoundsException("index " + offset + " exceeds size " + size);
    }
    TShortLink l = getLinkAt(offset);
    
    if (no(l)) {
      return no_entry_value;
    }
    return l.getValue();
  }
  









  public TShortLink getLinkAt(int offset)
  {
    if (offset >= size()) {
      return null;
    }
    if (offset <= size() >>> 1) {
      return getLink(head, 0, offset, true);
    }
    return getLink(tail, size() - 1, offset, false);
  }
  







  private static TShortLink getLink(TShortLink l, int idx, int offset)
  {
    return getLink(l, idx, offset, true);
  }
  







  private static TShortLink getLink(TShortLink l, int idx, int offset, boolean next)
  {
    int i = idx;
    
    while (got(l)) {
      if (i == offset) {
        return l;
      }
      
      i += (next ? 1 : -1);
      l = next ? l.getNext() : l.getPrevious();
    }
    
    return null;
  }
  

  public short set(int offset, short val)
  {
    if (offset > size) {
      throw new IndexOutOfBoundsException("index " + offset + " exceeds size " + size);
    }
    TShortLink l = getLinkAt(offset);
    
    if (no(l)) {
      throw new IndexOutOfBoundsException("at offset " + offset);
    }
    short prev = l.getValue();
    l.setValue(val);
    return prev;
  }
  
  public void set(int offset, short[] values)
  {
    set(offset, values, 0, values.length);
  }
  
  public void set(int offset, short[] values, int valOffset, int length)
  {
    for (int i = 0; i < length; i++) {
      short value = values[(valOffset + i)];
      set(offset + i, value);
    }
  }
  
  public short replace(int offset, short val)
  {
    return set(offset, val);
  }
  
  public void clear()
  {
    size = 0;
    
    head = null;
    tail = null;
  }
  
  public boolean remove(short value)
  {
    boolean changed = false;
    for (TShortLink l = head; got(l); l = l.getNext())
    {
      if (l.getValue() == value) {
        changed = true;
        
        removeLink(l);
      }
    }
    
    return changed;
  }
  




  private void removeLink(TShortLink l)
  {
    if (no(l)) {
      return;
    }
    size -= 1;
    
    TShortLink prev = l.getPrevious();
    TShortLink next = l.getNext();
    
    if (got(prev)) {
      prev.setNext(next);
    }
    else {
      head = next;
    }
    
    if (got(next)) {
      next.setPrevious(prev);
    }
    else {
      tail = prev;
    }
    
    l.setNext(null);
    l.setPrevious(null);
  }
  
  public boolean containsAll(Collection<?> collection)
  {
    if (isEmpty()) {
      return false;
    }
    for (Object o : collection) {
      if ((o instanceof Short)) {
        Short i = (Short)o;
        if (!contains(i.shortValue()))
          return false;
      } else {
        return false;
      }
    }
    return true;
  }
  
  public boolean containsAll(TShortCollection collection)
  {
    if (isEmpty()) {
      return false;
    }
    for (TShortIterator it = collection.iterator(); it.hasNext();) {
      short i = it.next();
      if (!contains(i))
        return false;
    }
    return true;
  }
  
  public boolean containsAll(short[] array)
  {
    if (isEmpty()) {
      return false;
    }
    for (short i : array) {
      if (!contains(i))
        return false;
    }
    return true;
  }
  
  public boolean addAll(Collection<? extends Short> collection)
  {
    boolean ret = false;
    for (Short v : collection) {
      if (add(v.shortValue())) {
        ret = true;
      }
    }
    return ret;
  }
  
  public boolean addAll(TShortCollection collection)
  {
    boolean ret = false;
    for (TShortIterator it = collection.iterator(); it.hasNext();) {
      short i = it.next();
      if (add(i)) {
        ret = true;
      }
    }
    return ret;
  }
  
  public boolean addAll(short[] array)
  {
    boolean ret = false;
    for (short i : array) {
      if (add(i)) {
        ret = true;
      }
    }
    return ret;
  }
  
  public boolean retainAll(Collection<?> collection)
  {
    boolean modified = false;
    TShortIterator iter = iterator();
    while (iter.hasNext()) {
      if (!collection.contains(Short.valueOf(iter.next()))) {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public boolean retainAll(TShortCollection collection)
  {
    boolean modified = false;
    TShortIterator iter = iterator();
    while (iter.hasNext()) {
      if (!collection.contains(iter.next())) {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public boolean retainAll(short[] array)
  {
    Arrays.sort(array);
    
    boolean modified = false;
    TShortIterator iter = iterator();
    while (iter.hasNext()) {
      if (Arrays.binarySearch(array, iter.next()) < 0) {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public boolean removeAll(Collection<?> collection)
  {
    boolean modified = false;
    TShortIterator iter = iterator();
    while (iter.hasNext()) {
      if (collection.contains(Short.valueOf(iter.next()))) {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public boolean removeAll(TShortCollection collection)
  {
    boolean modified = false;
    TShortIterator iter = iterator();
    while (iter.hasNext()) {
      if (collection.contains(iter.next())) {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public boolean removeAll(short[] array)
  {
    Arrays.sort(array);
    
    boolean modified = false;
    TShortIterator iter = iterator();
    while (iter.hasNext()) {
      if (Arrays.binarySearch(array, iter.next()) >= 0) {
        iter.remove();
        modified = true;
      }
    }
    return modified;
  }
  
  public short removeAt(int offset)
  {
    TShortLink l = getLinkAt(offset);
    if (no(l)) {
      throw new ArrayIndexOutOfBoundsException("no elemenet at " + offset);
    }
    short prev = l.getValue();
    removeLink(l);
    return prev;
  }
  
  public void remove(int offset, int length)
  {
    for (int i = 0; i < length; i++) {
      removeAt(offset);
    }
  }
  
  public void transformValues(TShortFunction function)
  {
    for (TShortLink l = head; got(l);)
    {
      l.setValue(function.execute(l.getValue()));
      
      l = l.getNext();
    }
  }
  
  public void reverse()
  {
    TShortLink h = head;
    TShortLink t = tail;
    


    TShortLink l = head;
    while (got(l)) {
      TShortLink next = l.getNext();
      TShortLink prev = l.getPrevious();
      
      TShortLink tmp = l;
      l = l.getNext();
      
      tmp.setNext(prev);
      tmp.setPrevious(next);
    }
    

    head = t;
    tail = h;
  }
  
  public void reverse(int from, int to)
  {
    if (from > to) {
      throw new IllegalArgumentException("from > to : " + from + ">" + to);
    }
    TShortLink start = getLinkAt(from);
    TShortLink stop = getLinkAt(to);
    
    TShortLink tmp = null;
    
    TShortLink tmpHead = start.getPrevious();
    

    TShortLink l = start;
    while (l != stop) {
      TShortLink next = l.getNext();
      TShortLink prev = l.getPrevious();
      
      tmp = l;
      l = l.getNext();
      
      tmp.setNext(prev);
      tmp.setPrevious(next);
    }
    

    if (got(tmp)) {
      tmpHead.setNext(tmp);
      stop.setPrevious(tmpHead);
    }
    start.setNext(stop);
    stop.setPrevious(start);
  }
  
  public void shuffle(Random rand)
  {
    for (int i = 0; i < size; i++) {
      TShortLink l = getLinkAt(rand.nextInt(size()));
      removeLink(l);
      add(l.getValue());
    }
  }
  
  public TShortList subList(int begin, int end)
  {
    if (end < begin) {
      throw new IllegalArgumentException("begin index " + begin + " greater than end index " + end);
    }
    
    if (size < begin) {
      throw new IllegalArgumentException("begin index " + begin + " greater than last index " + size);
    }
    
    if (begin < 0) {
      throw new IndexOutOfBoundsException("begin index can not be < 0");
    }
    if (end > size) {
      throw new IndexOutOfBoundsException("end index < " + size);
    }
    
    TShortLinkedList ret = new TShortLinkedList();
    TShortLink tmp = getLinkAt(begin);
    for (int i = begin; i < end; i++) {
      ret.add(tmp.getValue());
      tmp = tmp.getNext();
    }
    
    return ret;
  }
  
  public short[] toArray()
  {
    return toArray(new short[size], 0, size);
  }
  
  public short[] toArray(int offset, int len)
  {
    return toArray(new short[len], offset, 0, len);
  }
  
  public short[] toArray(short[] dest)
  {
    return toArray(dest, 0, size);
  }
  
  public short[] toArray(short[] dest, int offset, int len)
  {
    return toArray(dest, offset, 0, len);
  }
  
  public short[] toArray(short[] dest, int source_pos, int dest_pos, int len)
  {
    if (len == 0) {
      return dest;
    }
    if ((source_pos < 0) || (source_pos >= size())) {
      throw new ArrayIndexOutOfBoundsException(source_pos);
    }
    
    TShortLink tmp = getLinkAt(source_pos);
    for (int i = 0; i < len; i++) {
      dest[(dest_pos + i)] = tmp.getValue();
      tmp = tmp.getNext();
    }
    
    return dest;
  }
  
  public boolean forEach(TShortProcedure procedure)
  {
    for (TShortLink l = head; got(l); l = l.getNext()) {
      if (!procedure.execute(l.getValue()))
        return false;
    }
    return true;
  }
  
  public boolean forEachDescending(TShortProcedure procedure)
  {
    for (TShortLink l = tail; got(l); l = l.getPrevious()) {
      if (!procedure.execute(l.getValue()))
        return false;
    }
    return true;
  }
  
  public void sort()
  {
    sort(0, size);
  }
  
  public void sort(int fromIndex, int toIndex)
  {
    TShortList tmp = subList(fromIndex, toIndex);
    short[] vals = tmp.toArray();
    Arrays.sort(vals);
    set(fromIndex, vals);
  }
  
  public void fill(short val)
  {
    fill(0, size, val);
  }
  
  public void fill(int fromIndex, int toIndex, short val)
  {
    if (fromIndex < 0) {
      throw new IndexOutOfBoundsException("begin index can not be < 0");
    }
    

    TShortLink l = getLinkAt(fromIndex);
    if (toIndex > size) {
      for (int i = fromIndex; i < size; i++) {
        l.setValue(val);
        l = l.getNext();
      }
      for (int i = size; i < toIndex; i++) {
        add(val);
      }
    } else {
      for (int i = fromIndex; i < toIndex; i++) {
        l.setValue(val);
        l = l.getNext();
      }
    }
  }
  

  public int binarySearch(short value)
  {
    return binarySearch(value, 0, size());
  }
  
  public int binarySearch(short value, int fromIndex, int toIndex)
  {
    if (fromIndex < 0) {
      throw new IndexOutOfBoundsException("begin index can not be < 0");
    }
    
    if (toIndex > size) {
      throw new IndexOutOfBoundsException("end index > size: " + toIndex + " > " + size);
    }
    

    if (toIndex < fromIndex) {
      return -(fromIndex + 1);
    }
    


    int from = fromIndex;
    TShortLink fromLink = getLinkAt(fromIndex);
    int to = toIndex;
    
    while (from < to) {
      int mid = from + to >>> 1;
      TShortLink middle = getLink(fromLink, from, mid);
      if (middle.getValue() == value) {
        return mid;
      }
      if (middle.getValue() < value) {
        from = mid + 1;
        fromLink = next;
      } else {
        to = mid - 1;
      }
    }
    
    return -(from + 1);
  }
  
  public int indexOf(short value)
  {
    return indexOf(0, value);
  }
  
  public int indexOf(int offset, short value)
  {
    int count = offset;
    for (TShortLink l = getLinkAt(offset); got(l.getNext()); l = l.getNext()) {
      if (l.getValue() == value) {
        return count;
      }
      count++;
    }
    
    return -1;
  }
  
  public int lastIndexOf(short value)
  {
    return lastIndexOf(0, value);
  }
  
  public int lastIndexOf(int offset, short value)
  {
    if (isEmpty()) {
      return -1;
    }
    int last = -1;
    int count = offset;
    for (TShortLink l = getLinkAt(offset); got(l.getNext()); l = l.getNext()) {
      if (l.getValue() == value) {
        last = count;
      }
      count++;
    }
    
    return last;
  }
  
  public boolean contains(short value)
  {
    if (isEmpty()) {
      return false;
    }
    for (TShortLink l = head; got(l); l = l.getNext()) {
      if (l.getValue() == value)
        return true;
    }
    return false;
  }
  

  public TShortIterator iterator()
  {
    new TShortIterator() {
      TShortLinkedList.TShortLink l = head;
      TShortLinkedList.TShortLink current;
      
      public short next() {
        if (TShortLinkedList.no(l)) {
          throw new NoSuchElementException();
        }
        short ret = l.getValue();
        current = l;
        l = l.getNext();
        
        return ret;
      }
      
      public boolean hasNext() {
        return TShortLinkedList.got(l);
      }
      
      public void remove() {
        if (current == null) {
          throw new IllegalStateException();
        }
        TShortLinkedList.this.removeLink(current);
        current = null;
      }
    };
  }
  
  public TShortList grep(TShortProcedure condition)
  {
    TShortList ret = new TShortLinkedList();
    for (TShortLink l = head; got(l); l = l.getNext()) {
      if (condition.execute(l.getValue()))
        ret.add(l.getValue());
    }
    return ret;
  }
  
  public TShortList inverseGrep(TShortProcedure condition)
  {
    TShortList ret = new TShortLinkedList();
    for (TShortLink l = head; got(l); l = l.getNext()) {
      if (!condition.execute(l.getValue()))
        ret.add(l.getValue());
    }
    return ret;
  }
  
  public short max()
  {
    short ret = Short.MIN_VALUE;
    
    if (isEmpty()) {
      throw new IllegalStateException();
    }
    for (TShortLink l = head; got(l); l = l.getNext()) {
      if (ret < l.getValue()) {
        ret = l.getValue();
      }
    }
    return ret;
  }
  
  public short min()
  {
    short ret = Short.MAX_VALUE;
    
    if (isEmpty()) {
      throw new IllegalStateException();
    }
    for (TShortLink l = head; got(l); l = l.getNext()) {
      if (ret > l.getValue()) {
        ret = l.getValue();
      }
    }
    return ret;
  }
  
  public short sum()
  {
    short sum = 0;
    
    for (TShortLink l = head; got(l); l = l.getNext()) {
      sum = (short)(sum + l.getValue());
    }
    
    return sum;
  }
  

  static class TShortLink
  {
    short value;
    TShortLink previous;
    TShortLink next;
    
    TShortLink(short value)
    {
      this.value = value;
    }
    
    public short getValue() {
      return value;
    }
    
    public void setValue(short value) {
      this.value = value;
    }
    
    public TShortLink getPrevious() {
      return previous;
    }
    
    public void setPrevious(TShortLink previous) {
      this.previous = previous;
    }
    
    public TShortLink getNext() {
      return next;
    }
    
    public void setNext(TShortLink next) {
      this.next = next;
    }
  }
  
  class RemoveProcedure implements TShortProcedure {
    boolean changed = false;
    



    RemoveProcedure() {}
    



    public boolean execute(short value)
    {
      if (remove(value)) {
        changed = true;
      }
      return true;
    }
    
    public boolean isChanged() {
      return changed;
    }
  }
  
  public void writeExternal(ObjectOutput out)
    throws IOException
  {
    out.writeByte(0);
    

    out.writeShort(no_entry_value);
    

    out.writeInt(size);
    for (TShortIterator iterator = iterator(); iterator.hasNext();) {
      short next = iterator.next();
      out.writeShort(next);
    }
  }
  



  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    in.readByte();
    

    no_entry_value = in.readShort();
    

    int len = in.readInt();
    for (int i = 0; i < len; i++) {
      add(in.readShort());
    }
  }
  
  static boolean got(Object ref) {
    return ref != null;
  }
  
  static boolean no(Object ref) {
    return ref == null;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if ((o == null) || (getClass() != o.getClass())) { return false;
    }
    TShortLinkedList that = (TShortLinkedList)o;
    
    if (no_entry_value != no_entry_value) return false;
    if (size != size) { return false;
    }
    TShortIterator iterator = iterator();
    TShortIterator thatIterator = that.iterator();
    while (iterator.hasNext()) {
      if (!thatIterator.hasNext()) {
        return false;
      }
      if (iterator.next() != thatIterator.next()) {
        return false;
      }
    }
    return true;
  }
  
  public int hashCode()
  {
    int result = HashFunctions.hash(no_entry_value);
    result = 31 * result + size;
    for (TShortIterator iterator = iterator(); iterator.hasNext();) {
      result = 31 * result + HashFunctions.hash(iterator.next());
    }
    
    return result;
  }
  
  public String toString()
  {
    StringBuilder buf = new StringBuilder("{");
    TShortIterator it = iterator();
    while (it.hasNext()) {
      short next = it.next();
      buf.append(next);
      if (it.hasNext())
        buf.append(", ");
    }
    buf.append("}");
    return buf.toString();
  }
}
