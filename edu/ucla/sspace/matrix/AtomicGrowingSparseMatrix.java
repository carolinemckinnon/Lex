package edu.ucla.sspace.matrix;

import edu.ucla.sspace.util.IntegerMap;
import edu.ucla.sspace.vector.AtomicSparseVector;
import edu.ucla.sspace.vector.CompactSparseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.SparseHashDoubleVector;
import edu.ucla.sspace.vector.Vectors;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;































































public class AtomicGrowingSparseMatrix
  implements AtomicMatrix, SparseMatrix, Serializable
{
  private static final long serialVersionUID = 1L;
  private Lock rowReadLock;
  private Lock rowWriteLock;
  private Lock denseArrayReadLock;
  private Lock denseArrayWriteLock;
  private AtomicInteger rows;
  private AtomicInteger cols;
  private final Map<Integer, AtomicSparseVector> sparseMatrix;
  
  public AtomicGrowingSparseMatrix()
  {
    rows = new AtomicInteger(0);
    cols = new AtomicInteger(0);
    sparseMatrix = new IntegerMap();
    
    ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    rowReadLock = rwLock.readLock();
    rowWriteLock = rwLock.writeLock();
    
    rwLock = new ReentrantReadWriteLock();
    denseArrayReadLock = rwLock.readLock();
    denseArrayWriteLock = rwLock.writeLock();
  }
  


  public double addAndGet(int row, int col, double delta)
  {
    checkIndices(row, col);
    AtomicSparseVector rowEntry = getRow(row, col, true);
    return rowEntry.addAndGet(col, delta);
  }
  





  private void checkIndices(int row, int col)
  {
    if ((row < 0) || (col < 0)) {
      throw new ArrayIndexOutOfBoundsException();
    }
  }
  


  public int columns()
  {
    return cols.get();
  }
  


  public double get(int row, int col)
  {
    checkIndices(row, col);
    AtomicSparseVector rowEntry = getRow(row, col, false);
    return rowEntry == null ? 0.0D : rowEntry.get(col);
  }
  


  public double getAndAdd(int row, int col, double delta)
  {
    checkIndices(row, col);
    AtomicSparseVector rowEntry = getRow(row, col, true);
    return rowEntry.getAndAdd(col, delta);
  }
  





  public double[] getColumn(int column)
  {
    checkIndices(0, column);
    rowReadLock.lock();
    double[] values = new double[rows.get()];
    for (int row = 0; row < rows.get(); row++)
      values[row] = get(row, column);
    rowReadLock.unlock();
    return values;
  }
  




  public SparseDoubleVector getColumnVector(int column)
  {
    checkIndices(0, column);
    rowReadLock.lock();
    SparseDoubleVector values = new SparseHashDoubleVector(rows.get());
    for (int row = 0; row < rows.get(); row++) {
      AtomicSparseVector rowEntry = getRow(row, -1, false);
      double value = 0.0D;
      if ((rowEntry != null) && ((value = rowEntry.get(column)) != 0.0D))
        values.set(row, value);
    }
    rowReadLock.unlock();
    return values;
  }
  






  public SparseDoubleVector getColumnVectorUnsafe(int column)
  {
    checkIndices(0, column);
    SparseDoubleVector values = new SparseHashDoubleVector(rows.get());
    for (int row = 0; row < rows.get(); row++) {
      AtomicSparseVector rowEntry = getRow(row, -1, false);
      double value = 0.0D;
      if ((rowEntry != null) && ((value = rowEntry.get(column)) != 0.0D))
        values.set(row, value);
    }
    return values;
  }
  




  public double[] getRow(int row)
  {
    checkIndices(row, 0);
    AtomicSparseVector rowEntry = getRow(row, -1, false);
    return rowEntry == null ? 
      new double[cols.get()] : 
      toArray(rowEntry, cols.get());
  }
  
















  private AtomicSparseVector getRow(int row, int col, boolean createIfAbsent)
  {
    rowReadLock.lock();
    AtomicSparseVector rowEntry = (AtomicSparseVector)sparseMatrix.get(Integer.valueOf(row));
    
    if (col >= cols.get())
      cols.set(col + 1);
    rowReadLock.unlock();
    

    if ((rowEntry == null) && (createIfAbsent)) {
      rowWriteLock.lock();
      

      rowEntry = (AtomicSparseVector)sparseMatrix.get(Integer.valueOf(row));
      if (rowEntry == null) {
        rowEntry = new AtomicSparseVector(new CompactSparseVector());
        

        if (row >= rows.get()) {
          rows.set(row + 1);
        }
        sparseMatrix.put(Integer.valueOf(row), rowEntry);
      }
      rowWriteLock.unlock();
    }
    return rowEntry;
  }
  




  public SparseDoubleVector getRowVector(int row)
  {
    SparseDoubleVector v = getRow(row, -1, false);
    


    return v == null ? 
      new CompactSparseVector(cols.get()) : 
      Vectors.subview(v, 0, cols.get());
  }
  










  public SparseDoubleVector getRowVectorUnsafe(int row)
  {
    AtomicSparseVector rowEntry = (AtomicSparseVector)sparseMatrix.get(Integer.valueOf(row));
    return rowEntry == null ? 
      new CompactSparseVector(cols.get()) : 
      Vectors.immutable(Vectors.subview(rowEntry.getVector(), 
      0, cols.get()));
  }
  


  public int rows()
  {
    return rows.get();
  }
  


  public void set(int row, int col, double val)
  {
    checkIndices(row, col);
    
    AtomicSparseVector rowEntry = getRow(row, col, true);
    denseArrayReadLock.lock();
    rowEntry.set(col, val);
    denseArrayReadLock.unlock();
  }
  


  public void setColumn(int column, double[] values)
  {
    checkIndices(0, column);
    for (int row = 0; row < rows.get(); row++) {
      set(row, column, values[row]);
    }
  }
  

  public void setColumn(int column, DoubleVector values)
  {
    checkIndices(0, column);
    for (int row = 0; row < rows.get(); row++) {
      set(row, column, values.get(row));
    }
  }
  

  public void setRow(int row, double[] columns)
  {
    checkIndices(row, 0);
    AtomicSparseVector rowEntry = getRow(row, columns.length - 1, true);
    denseArrayReadLock.lock();
    for (int i = 0; i < columns.length; i++)
      rowEntry.set(i, columns[i]);
    denseArrayReadLock.unlock();
  }
  


  public void setRow(int row, DoubleVector values)
  {
    checkIndices(row, 0);
    AtomicSparseVector rowEntry = getRow(row, values.length() - 1, true);
    denseArrayReadLock.lock();
    Vectors.copy(rowEntry, values);
    denseArrayReadLock.unlock();
  }
  



  public double[][] toDenseArray()
  {
    rowWriteLock.lock();
    

    denseArrayWriteLock.lock();
    int c = cols.get();
    double[][] m = new double[rows.get()][c];
    
    Iterator localIterator = sparseMatrix.entrySet().iterator();
    while (localIterator.hasNext()) {
      Map.Entry<Integer, AtomicSparseVector> e = (Map.Entry)localIterator.next();
      m[((Integer)e.getKey()).intValue()] = toArray((DoubleVector)e.getValue(), c);
    }
    denseArrayWriteLock.unlock();
    rowWriteLock.unlock();
    return m;
  }
  





  private static double[] toArray(DoubleVector v, int length)
  {
    double[] arr = new double[length];
    for (int i = 0; i < arr.length; i++) {
      arr[i] = v.get(i);
    }
    return arr;
  }
}
