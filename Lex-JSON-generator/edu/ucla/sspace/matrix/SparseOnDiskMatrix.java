package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.SparseHashDoubleVector;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;
















































































public class SparseOnDiskMatrix
  extends OnDiskMatrix
  implements SparseMatrix
{
  private final Map<Integer, VersionedVector> rowToVectorCache;
  private final Map<Integer, VersionedVector> colToVectorCache;
  private final AtomicInteger version;
  
  public SparseOnDiskMatrix(int rows, int cols)
  {
    super(rows, cols);
    version = new AtomicInteger(0);
    rowToVectorCache = new WeakHashMap();
    colToVectorCache = new WeakHashMap();
  }
  




  public SparseDoubleVector getColumnVector(int column)
  {
    VersionedVector cachedCol = (VersionedVector)colToVectorCache.get(Integer.valueOf(column));
    

    if ((cachedCol == null) || (version != version.get())) {
      cachedCol = new VersionedVector(rows(), version.get());
      double[] col = getColumn(column);
      for (int i = 0; i < col.length; i++) {
        double d = col[i];
        if (d != 0.0D)
          cachedCol.set(i, d);
      }
      colToVectorCache.put(Integer.valueOf(column), cachedCol);
    }
    return cachedCol;
  }
  




  public SparseDoubleVector getRowVector(int row)
  {
    VersionedVector cachedRow = (VersionedVector)colToVectorCache.get(Integer.valueOf(row));
    

    if ((cachedRow == null) || (version != version.get())) {
      cachedRow = new VersionedVector(columns(), version.get());
      double[] r = getRow(row);
      for (int i = 0; i < r.length; i++) {
        double d = r[i];
        if (d != 0.0D)
          cachedRow.set(i, d);
      }
      rowToVectorCache.put(Integer.valueOf(row), cachedRow);
    }
    return cachedRow;
  }
  



  public void set(int row, int col, double val)
  {
    super.set(row, col, val);
    version.incrementAndGet();
  }
  



  public void setColumn(int column, double[] values)
  {
    super.setColumn(column, values);
    version.incrementAndGet();
  }
  



  public void setColumn(int column, DoubleVector values)
  {
    super.setColumn(column, values);
    version.incrementAndGet();
  }
  



  public void setRow(int row, double[] vals)
  {
    super.setRow(row, vals);
    version.incrementAndGet();
  }
  



  public void setRow(int row, DoubleVector values)
  {
    super.setRow(row, values);
    version.incrementAndGet();
  }
  


  private static class VersionedVector
    extends SparseHashDoubleVector
  {
    private static final long serialVersionUID = 1L;
    

    private final int version;
    

    public VersionedVector(int length, int version)
    {
      super();
      this.version = version;
    }
  }
}
