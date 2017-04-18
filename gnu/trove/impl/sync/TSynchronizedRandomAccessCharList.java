package gnu.trove.impl.sync;

import gnu.trove.list.TCharList;
import java.util.RandomAccess;













































public class TSynchronizedRandomAccessCharList
  extends TSynchronizedCharList
  implements RandomAccess
{
  static final long serialVersionUID = 1530674583602358482L;
  
  public TSynchronizedRandomAccessCharList(TCharList list)
  {
    super(list);
  }
  
  public TSynchronizedRandomAccessCharList(TCharList list, Object mutex) {
    super(list, mutex);
  }
  
  public TCharList subList(int fromIndex, int toIndex) {
    synchronized (mutex) {
      return new TSynchronizedRandomAccessCharList(list.subList(fromIndex, toIndex), mutex);
    }
  }
  






  private Object writeReplace()
  {
    return new TSynchronizedCharList(list);
  }
}
