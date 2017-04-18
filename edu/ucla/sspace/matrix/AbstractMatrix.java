package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.Vectors;















































public abstract class AbstractMatrix
  implements Matrix
{
  public AbstractMatrix() {}
  
  public abstract int columns();
  
  public boolean equals(Object o)
  {
    if ((o instanceof Matrix)) {
      Matrix m = (Matrix)o;
      int rows = rows();
      if (m.rows() != rows)
        return false;
      int cols = columns();
      if (m.columns() != cols) {
        return false;
      }
      
      for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
          if (m.get(r, c) != get(r, c))
            return false;
        }
      }
      return true;
    }
    return false;
  }
  


  public double get(int row, int col)
  {
    return getRowVector(row).get(col);
  }
  


  public double[] getColumn(int column)
  {
    return getColumnVector(column).toArray();
  }
  


  public DoubleVector getColumnVector(int column)
  {
    DoubleVector col = new DenseVector(rows());
    for (int r = 0; r < rows(); r++)
      col.set(r, getRowVector(r).get(column));
    return col;
  }
  


  public double[] getRow(int row)
  {
    return getRowVector(row).toArray();
  }
  



  public abstract DoubleVector getRowVector(int paramInt);
  


  public int hashCode()
  {
    int hash = 0;
    for (int i = 0; i < rows(); i++)
      hash += getRowVector(i).hashCode();
    return hash;
  }
  



  public abstract int rows();
  



  public void set(int row, int col, double val)
  {
    throw new UnsupportedOperationException("Matrix is immutable");
  }
  





  public void setColumn(int column, double[] values)
  {
    setColumn(column, Vectors.asVector(values));
  }
  





  public void setColumn(int column, DoubleVector values)
  {
    if (values.length() != rows())
      throw new IllegalArgumentException(
        "Number of values is not equal the number of rows");
    for (int r = 0; r < values.length(); r++) {
      set(r, column, values.get(r));
    }
  }
  




  public void setRow(int row, double[] columns)
  {
    setRow(row, Vectors.asVector(columns));
  }
  





  public void setRow(int row, DoubleVector values)
  {
    if (values.length() != columns())
      throw new IllegalArgumentException(
        "Number of values is not equal the number of rows");
    for (int c = 0; c < values.length(); c++) {
      set(row, c, values.get(c));
    }
  }
  

  public double[][] toDenseArray()
  {
    double[][] m = new double[rows()][columns()];
    for (int r = 0; r < rows(); r++) {
      m[r] = getRowVector(r).toArray();
    }
    return m;
  }
  
  public String toString() {
    int rows = rows();
    int columns = columns();
    StringBuilder sb = new StringBuilder(rows * columns * 2);
    for (int r = 0; r < rows; r++) {
      sb.append('[');
      for (int c = 0; c < columns; c++) {
        sb.append(get(r, c));
        if (c + 1 < columns)
          sb.append(", ");
      }
      sb.append("]\n");
    }
    return sb.toString();
  }
}
