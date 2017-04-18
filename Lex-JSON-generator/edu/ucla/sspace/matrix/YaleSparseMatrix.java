package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.CompactSparseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.SparseHashDoubleVector;
import edu.ucla.sspace.vector.Vectors;
import java.io.Serializable;














































public class YaleSparseMatrix
  implements SparseMatrix, Serializable
{
  private static final long serialVersionUID = 1L;
  private final int rows;
  private final int cols;
  private final CompactSparseVector[] sparseMatrix;
  
  public YaleSparseMatrix(int rows, int cols)
  {
    this.rows = rows;
    this.cols = cols;
    sparseMatrix = new CompactSparseVector[rows];
    for (int i = 0; i < rows; i++) {
      sparseMatrix[i] = new CompactSparseVector(cols);
    }
  }
  





  public YaleSparseMatrix(double[][] values)
  {
    if (values.length == 0) {
      throw new IllegalArgumentException(
        "Matrix must have non zero size");
    }
    rows = values.length;
    cols = values[0].length;
    
    sparseMatrix = new CompactSparseVector[rows];
    for (int r = 0; r < rows; r++) {
      if (values[r].length != cols) {
        throw new IllegalArgumentException(
          "Cannot form matrix from jagged array");
      }
      sparseMatrix[r] = new CompactSparseVector(cols);
      for (int c = 0; c < cols; c++) {
        if (values[r][c] != 0.0D) {
          set(r, c, values[r][c]);
        }
      }
    }
  }
  

  private void checkIndices(int row, int col)
  {
    if ((row < 0) || (col < 0) || (row >= rows) || (col >= cols)) {
      throw new IndexOutOfBoundsException();
    }
  }
  


  public double get(int row, int col)
  {
    checkIndices(row, col);
    return sparseMatrix[row].get(col);
  }
  


  public double[] getColumn(int column)
  {
    double[] values = new double[rows];
    for (int row = 0; row < rows; row++)
      values[row] = get(row, column);
    return values;
  }
  


  public SparseDoubleVector getColumnVector(int column)
  {
    SparseDoubleVector values = new SparseHashDoubleVector(rows);
    for (int row = 0; row < rows; row++)
      values.set(row, get(row, column));
    return values;
  }
  


  public double[] getRow(int row)
  {
    return sparseMatrix[row].toArray();
  }
  


  public SparseDoubleVector getRowVector(int row)
  {
    return Vectors.immutable(sparseMatrix[row]);
  }
  


  public int columns()
  {
    return cols;
  }
  


  public void set(int row, int col, double val)
  {
    checkIndices(row, col);
    sparseMatrix[row].set(col, val);
  }
  


  public void setColumn(int column, double[] values)
  {
    if (values.length != rows) {
      throw new IllegalArgumentException(
        "invalid number of rows: " + values.length);
    }
    for (int row = 0; row < rows; row++) {
      set(row, column, values[row]);
    }
  }
  

  public void setColumn(int column, DoubleVector values)
  {
    if (values.length() != rows) {
      throw new IllegalArgumentException(
        "invalid number of rows: " + values.length());
    }
    for (int row = 0; row < rows; row++) {
      set(row, column, values.get(row));
    }
  }
  

  public void setRow(int row, double[] columns)
  {
    if (columns.length != cols) {
      throw new IllegalArgumentException(
        "invalid number of columns: " + columns.length);
    }
    for (int col = 0; col < cols; col++) {
      sparseMatrix[row].set(col, columns[col]);
    }
  }
  


  public void setRow(int row, DoubleVector values)
  {
    if (values.length() != cols) {
      throw new IllegalArgumentException(
        "invalid number of columns: " + values.length());
    }
    Vectors.copy(sparseMatrix[row], values);
  }
  


  public double[][] toDenseArray()
  {
    double[][] m = new double[rows][cols];
    for (int r = 0; r < rows; r++) {
      m[r] = sparseMatrix[r].toArray();
    }
    return m;
  }
  


  public int rows()
  {
    return rows;
  }
}
