package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.DoubleVector;
import java.io.Serializable;


































public class SynchronizedMatrix
  implements Matrix, AtomicMatrix, Serializable
{
  private static final long serialVersionUID = 1L;
  private final Matrix m;
  
  public SynchronizedMatrix(Matrix matrix)
  {
    m = matrix;
  }
  


  public synchronized double addAndGet(int row, int col, double delta)
  {
    double value = m.get(row, col) + delta;
    m.set(row, col, value);
    return value;
  }
  


  public synchronized double getAndAdd(int row, int col, double delta)
  {
    double value = m.get(row, col);
    m.set(row, col, value + delta);
    return value;
  }
  


  public synchronized int columns()
  {
    return m.columns();
  }
  


  public synchronized double get(int row, int col)
  {
    return m.get(row, col);
  }
  


  public synchronized double[] getColumn(int column)
  {
    return m.getColumn(column);
  }
  


  public synchronized DoubleVector getColumnVector(int column)
  {
    return m.getColumnVector(column);
  }
  


  public synchronized double[] getRow(int row)
  {
    return m.getRow(row);
  }
  


  public synchronized DoubleVector getRowVector(int row)
  {
    return m.getRowVector(row);
  }
  


  public synchronized int rows()
  {
    return m.rows();
  }
  


  public synchronized void set(int row, int col, double val)
  {
    m.set(row, col, val);
  }
  


  public synchronized void setColumn(int column, double[] values)
  {
    m.setColumn(column, values);
  }
  


  public synchronized void setColumn(int column, DoubleVector values)
  {
    m.setColumn(column, values);
  }
  


  public synchronized void setRow(int row, double[] values)
  {
    m.setRow(row, values);
  }
  


  public synchronized void setRow(int row, DoubleVector values)
  {
    m.setRow(row, values);
  }
  


  public synchronized double[][] toDenseArray()
  {
    return m.toDenseArray();
  }
}
