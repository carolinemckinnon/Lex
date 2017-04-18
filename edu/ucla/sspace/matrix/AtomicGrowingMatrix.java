package edu.ucla.sspace.matrix;

import edu.ucla.sspace.util.IntegerMap;
import edu.ucla.sspace.vector.AtomicVector;
import edu.ucla.sspace.vector.CompactSparseVector;
import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.Vectors;
import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;






























































public class AtomicGrowingMatrix
  implements AtomicMatrix, Serializable
{
  private static final long serialVersionUID = 1L;
  private Lock rowReadLock;
  private Lock rowWriteLock;
  private Lock denseArrayReadLock;
  private Lock denseArrayWriteLock;
  private AtomicInteger rows;
  private AtomicInteger cols;
  private final Map<Integer, AtomicVector> sparseMatrix;
  
  public AtomicGrowingMatrix()
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
    AtomicVector rowEntry = getRow(row, col, true);
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
    AtomicVector rowEntry = getRow(row, col, false);
    return rowEntry == null ? 0.0D : rowEntry.get(col);
  }
  


  public double getAndAdd(int row, int col, double delta)
  {
    checkIndices(row, col);
    AtomicVector rowEntry = getRow(row, col, true);
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
  




  public DoubleVector getColumnVector(int column)
  {
    checkIndices(0, column);
    rowReadLock.lock();
    DoubleVector values = new DenseVector(rows.get());
    for (int row = 0; row < rows.get(); row++) {
      double value = get(row, column);
      if (value != 0.0D)
        values.set(row, value);
    }
    rowReadLock.unlock();
    return values;
  }
  






  public DoubleVector getColumnVectorUnsafe(int column)
  {
    checkIndices(0, column);
    DoubleVector values = new DenseVector(rows.get());
    for (int row = 0; row < rows.get(); row++) {
      AtomicVector rowEntry = getRow(row, -1, false);
      double value = 0.0D;
      if ((rowEntry != null) && ((value = rowEntry.get(column)) != 0.0D))
        values.set(row, value);
    }
    return values;
  }
  




  public double[] getRow(int row)
  {
    checkIndices(row, 0);
    AtomicVector rowEntry = getRow(row, -1, false);
    return rowEntry == null ? 
      new double[cols.get()] : 
      toArray(rowEntry, cols.get());
  }
  














  private AtomicVector getRow(int row, int col, boolean createIfAbsent)
  {
    rowReadLock.lock();
    AtomicVector rowEntry = (AtomicVector)sparseMatrix.get(Integer.valueOf(row));
    rowReadLock.unlock();
    

    if ((rowEntry == null) && (createIfAbsent)) {
      rowWriteLock.lock();
      

      rowEntry = (AtomicVector)sparseMatrix.get(Integer.valueOf(row));
      if (rowEntry == null) {
        rowEntry = new AtomicVector(new CompactSparseVector());
        

        if (row >= rows.get()) {
          rows.set(row + 1);
        }
        if (col >= cols.get()) {
          cols.set(col + 1);
        }
        sparseMatrix.put(Integer.valueOf(row), rowEntry);
      }
      rowWriteLock.unlock();
    }
    return rowEntry;
  }
  




  public DoubleVector getRowVector(int row)
  {
    DoubleVector v = getRow(row, -1, false);
    


    return v == null ? 
      new CompactSparseVector(cols.get()) : 
      Vectors.subview(v, 0, cols.get());
  }
  










  public DoubleVector getRowVectorUnsafe(int row)
  {
    AtomicVector rowEntry = (AtomicVector)sparseMatrix.get(Integer.valueOf(row));
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
    
    AtomicVector rowEntry = getRow(row, col, true);
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
    AtomicVector rowEntry = getRow(row, columns.length - 1, true);
    denseArrayReadLock.lock();
    for (int i = 0; i < columns.length; i++)
      rowEntry.set(i, columns[i]);
    denseArrayReadLock.unlock();
  }
  


  public void setRow(int row, DoubleVector values)
  {
    checkIndices(row, 0);
    AtomicVector rowEntry = getRow(row, values.length() - 1, true);
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
    for (Map.Entry<Integer, AtomicVector> e : sparseMatrix.entrySet()) {
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
