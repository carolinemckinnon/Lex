package gnu.trove.impl.sync;

import gnu.trove.list.TByteList;
import java.util.RandomAccess;













































public class TSynchronizedRandomAccessByteList
  extends TSynchronizedByteList
  implements RandomAccess
{
  static final long serialVersionUID = 1530674583602358482L;
  
  public TSynchronizedRandomAccessByteList(TByteList list)
  {
    super(list);
  }
  
  public TSynchronizedRandomAccessByteList(TByteList list, Object mutex) {
    super(list, mutex);
  }
  
  public TByteList subList(int fromIndex, int toIndex) {
    synchronized (mutex) {
      return new TSynchronizedRandomAccessByteList(list.subList(fromIndex, toIndex), mutex);
    }
  }
  






  private Object writeReplace()
  {
    return new TSynchronizedByteList(list);
  }
}
