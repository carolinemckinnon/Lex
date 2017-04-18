package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.SparseHashDoubleVector;
import edu.ucla.sspace.vector.Vectors;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;










































































































public class AtomicGrowingSparseHashMatrix
  implements AtomicMatrix, SparseMatrix, Serializable
{
  private static final long serialVersionUID = 1L;
  private final ConcurrentMap<Entry, Object> lockedEntries;
  private final ConcurrentMap<Entry, Double> matrixEntries;
  private final AtomicInteger rows;
  private final AtomicInteger cols;
  private final AtomicInteger modifications;
  private final AtomicInteger lastVectorCacheUpdate;
  private int[][] rowToColsCache;
  private int[][] colToRowsCache;
  
  public AtomicGrowingSparseHashMatrix()
  {
    rows = new AtomicInteger(0);
    cols = new AtomicInteger(0);
    modifications = new AtomicInteger(0);
    lastVectorCacheUpdate = new AtomicInteger(0);
    
    rowToColsCache = null;
    colToRowsCache = null;
    



    int threads = Runtime.getRuntime().availableProcessors();
    lockedEntries = new ConcurrentHashMap(
      1000, 0.75F, threads * 16);
    matrixEntries = new ConcurrentHashMap(
      10000, 4.0F, threads * 16);
  }
  


  public double addAndGet(int row, int col, double delta)
  {
    checkIndices(row, col, true);
    Entry e = new Entry(row, col);
    
    while (lockedEntries.putIfAbsent(e, new Object()) != null) {}
    
    Double val = (Double)matrixEntries.get(e);
    double newVal = val == null ? delta : delta + val.doubleValue();
    if (newVal != 0.0D) {
      matrixEntries.put(e, Double.valueOf(newVal));
      

      if (val == null) {
        modifications.incrementAndGet();
      }
    } else {
      matrixEntries.remove(e);
      modifications.incrementAndGet();
    }
    lockedEntries.remove(e);
    return newVal;
  }
  









  private void checkIndices(int row, int col, boolean expand)
  {
    if ((row < 0) || (col < 0)) {
      throw new ArrayIndexOutOfBoundsException();
    }
    if (expand) {
      int r = row + 1;
      int cur = 0;
      while ((r > (cur = rows.get())) && (!rows.compareAndSet(cur, r))) {}
      
      int c = col + 1;
      cur = 0;
      while ((c > (cur = cols.get())) && (!cols.compareAndSet(cur, c))) {}
    }
  }
  



  public int columns()
  {
    return cols.get();
  }
  


  public double get(int row, int col)
  {
    checkIndices(row, col, false);
    Double val = (Double)matrixEntries.get(new Entry(row, col));
    return val == null ? 0.0D : val.doubleValue();
  }
  


  public double getAndAdd(int row, int col, double delta)
  {
    checkIndices(row, col, true);
    Entry e = new Entry(row, col);
    
    while (lockedEntries.putIfAbsent(e, new Object()) != null) {}
    
    Double val = (Double)matrixEntries.get(e);
    double newVal = val == null ? delta : delta + val.doubleValue();
    if (newVal != 0.0D) {
      matrixEntries.put(e, Double.valueOf(newVal));
      

      if (val == null) {
        modifications.incrementAndGet();
      }
    } else {
      matrixEntries.remove(e);
      modifications.incrementAndGet();
    }
    lockedEntries.remove(e);
    return val == null ? 0.0D : val.doubleValue();
  }
  





  public double[] getColumn(int column)
  {
    return getColumnVector(column).toArray();
  }
  




  public SparseDoubleVector getColumnVector(int column)
  {
    return getColumnVector(column, true);
  }
  






  public SparseDoubleVector getColumnVectorUnsafe(int column)
  {
    return getColumnVector(column, false);
  }
  



  private SparseDoubleVector getColumnVector(int column, boolean shouldLock)
  {
    int r = rows.get();
    if (shouldLock) {
      lockColumn(column, r);
    }
    while (lastVectorCacheUpdate.get() != modifications.get())
      updateVectorCache();
    int[] rowArr = colToRowsCache[column];
    SparseDoubleVector colVec = new SparseHashDoubleVector(r);
    for (int row : rowArr)
      colVec.set(row, (Number)matrixEntries.get(new Entry(row, column)));
    if (shouldLock)
      unlockColumn(column, r);
    return colVec;
  }
  




  public double[] getRow(int row)
  {
    return getRowVector(row).toArray();
  }
  




  public SparseDoubleVector getRowVector(int row)
  {
    return getRowVector(row, true);
  }
  






  public SparseDoubleVector getRowVectorUnsafe(int row)
  {
    return getRowVector(row, false);
  }
  



  private SparseDoubleVector getRowVector(int row, boolean shouldLock)
  {
    int c = cols.get();
    if (shouldLock) {
      lockRow(row, c);
    }
    while (lastVectorCacheUpdate.get() != modifications.get())
      updateVectorCache();
    int[] colArr = rowToColsCache[row];
    SparseDoubleVector rowVec = new SparseHashDoubleVector(c);
    for (int column : colArr)
      rowVec.set(column, (Number)matrixEntries.get(new Entry(row, column)));
    if (shouldLock)
      unlockRow(row, c);
    return rowVec;
  }
  











  private void lockRow(int row, int colsToLock)
  {
    for (int col = 0; col < colsToLock; col++) {
      Entry e = new Entry(row, col);
      
      while (lockedEntries.putIfAbsent(e, new Object()) != null) {}
    }
  }
  












  private void lockColumn(int col, int rowsToLock)
  {
    for (int row = 0; row < rowsToLock; row++) {
      Entry e = new Entry(row, col);
      
      while (lockedEntries.putIfAbsent(e, new Object()) != null) {}
    }
  }
  



  public int rows()
  {
    return rows.get();
  }
  


  public void set(int row, int col, double val)
  {
    checkIndices(row, col, true);
    Entry e = new Entry(row, col);
    
    while (lockedEntries.putIfAbsent(e, new Object()) != null) {}
    
    boolean present = matrixEntries.containsKey(e);
    if (val != 0.0D) {
      matrixEntries.put(e, Double.valueOf(val));
      

      if (!present) {
        modifications.incrementAndGet();
      }
    } else if (present) {
      matrixEntries.remove(e);
      modifications.incrementAndGet();
    }
    
    lockedEntries.remove(e);
  }
  


  public void setColumn(int column, double[] values)
  {
    setColumn(column, Vectors.asVector(values));
  }
  


  public void setColumn(int column, DoubleVector rowValues)
  {
    checkIndices(rowValues.length(), column, true);
    int r = rows.get();
    lockColumn(column, r);
    boolean modified = false;
    for (int row = 0; row < r; row++) {
      double val = rowValues.get(row);
      Entry e = new Entry(row, column);
      boolean present = matrixEntries.containsKey(e);
      if (val != 0.0D) {
        matrixEntries.put(e, Double.valueOf(val));
        

        modified = (modified) || (!present);
      }
      else if (present) {
        matrixEntries.remove(e);
        modified = true;
      }
    }
    if (modified)
      modifications.incrementAndGet();
    unlockColumn(column, r);
  }
  


  public void setRow(int row, double[] columns)
  {
    setRow(row, Vectors.asVector(columns));
  }
  


  public void setRow(int row, DoubleVector colValues)
  {
    checkIndices(row, colValues.length(), true);
    int c = cols.get();
    lockRow(row, c);
    boolean modified = false;
    for (int col = 0; col < c; col++) {
      double val = colValues.get(col);
      Entry e = new Entry(row, col);
      boolean present = matrixEntries.containsKey(e);
      if (val != 0.0D) {
        matrixEntries.put(e, Double.valueOf(val));
        

        modified = (modified) || (!present);
      }
      else if (present) {
        matrixEntries.remove(e);
        modified = true;
      }
    }
    if (modified)
      modifications.incrementAndGet();
    unlockRow(row, c);
  }
  


  public double[][] toDenseArray()
  {
    int r = rows.get();
    int c = cols.get();
    for (int i = 0; i < r; i++) {
      lockRow(i, c);
    }
    double[][] m = new double[r][0];
    for (int i = 0; i < r; i++) {
      DoubleVector row = getRowVector(i);
      
      if (row.length() != c)
        row = Vectors.subview(row, 0, c);
      m[i] = row.toArray();
    }
    
    for (int i = 0; i < r; i++) {
      unlockRow(i, c);
    }
    return m;
  }
  








  private void unlockColumn(int col, int rowsToUnlock)
  {
    for (int row = 0; row < rowsToUnlock; row++) {
      lockedEntries.remove(new Entry(row, col));
    }
  }
  







  private void unlockRow(int row, int colsToUnlock)
  {
    for (int col = 0; col < colsToUnlock; col++) {
      lockedEntries.remove(new Entry(row, col));
    }
  }
  







  private synchronized void updateVectorCache()
  {
    while (lastVectorCacheUpdate.get() != modifications.get()) {
      lastVectorCacheUpdate.set(modifications.get());
      Map<Integer, List<Integer>> rowVals = 
        new HashMap();
      Map<Integer, List<Integer>> colVals = 
        new HashMap();
      
      for (Entry e : matrixEntries.keySet()) {
        int row = row;
        int col = col;
        List<Integer> c = (List)rowVals.get(Integer.valueOf(row));
        if (c == null) {
          c = new ArrayList();
          rowVals.put(Integer.valueOf(row), c);
        }
        c.add(Integer.valueOf(col));
        List<Integer> r = (List)colVals.get(Integer.valueOf(col));
        if (r == null) {
          r = new ArrayList();
          colVals.put(Integer.valueOf(col), r);
        }
        r.add(Integer.valueOf(row));
      }
      


      rowToColsCache = new int[this.rows.get()][0];
      for (Map.Entry<Integer, List<Integer>> e : rowVals.entrySet()) {
        int row = ((Integer)e.getKey()).intValue();
        List<Integer> cols = (List)e.getValue();
        int[] colArr = new int[cols.size()];
        for (int i = 0; i < cols.size(); i++)
          colArr[i] = ((Integer)cols.get(i)).intValue();
        rowToColsCache[row] = colArr;
        

        e.setValue(null);
      }
      rowVals = null;
      
      colToRowsCache = new int[this.cols.get()][0];
      for (Map.Entry<Integer, List<Integer>> e : colVals.entrySet()) {
        int col = ((Integer)e.getKey()).intValue();
        List<Integer> rows = (List)e.getValue();
        int[] rowArr = new int[rows.size()];
        for (int i = 0; i < rows.size(); i++)
          rowArr[i] = ((Integer)rows.get(i)).intValue();
        colToRowsCache[col] = rowArr;
        

        e.setValue(null);
      }
    }
  }
  

  private static class Entry
  {
    final int row;
    final int col;
    
    public Entry(int row, int col)
    {
      this.row = row;
      this.col = col;
    }
    
    public boolean equals(Object o) {
      if ((o instanceof Entry)) {
        Entry e = (Entry)o;
        return (row == row) && (col == col);
      }
      return false;
    }
    
    public int hashCode() {
      return row << 16 ^ col;
    }
  }
}
