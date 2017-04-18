package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.SparseHashDoubleVector;
import java.io.Serializable;






































public class DiagonalMatrix
  extends AbstractMatrix
  implements SparseMatrix, Serializable
{
  private static final long serialVersionUID = 1L;
  private double[] values;
  
  public DiagonalMatrix(int numValues)
  {
    values = new double[numValues];
  }
  






  public DiagonalMatrix(double[] newValues)
  {
    values = new double[newValues.length];
    for (int i = 0; i < values.length; i++) {
      values[i] = newValues[i];
    }
  }
  







  private void checkIndices(int row, int col)
  {
    if ((row < 0) || (col < 0) || (row >= values.length) || (col >= values.length)) {
      throw new ArrayIndexOutOfBoundsException();
    }
  }
  

  public double get(int row, int col)
  {
    checkIndices(row, col);
    
    if (row == col)
      return values[row];
    return 0.0D;
  }
  


  public double[] getColumn(int column)
  {
    checkIndices(0, column);
    
    double[] columnValues = new double[values.length];
    columnValues[column] = values[column];
    return columnValues;
  }
  


  public SparseDoubleVector getColumnVector(int column)
  {
    checkIndices(0, column);
    SparseDoubleVector columnValues = 
      new SparseHashDoubleVector(values.length);
    columnValues.set(column, values[column]);
    return columnValues;
  }
  


  public double[] getRow(int row)
  {
    checkIndices(row, 0);
    
    double[] returnRow = new double[values.length];
    returnRow[row] = values[row];
    return returnRow;
  }
  


  public SparseDoubleVector getRowVector(int row)
  {
    checkIndices(row, 0);
    SparseDoubleVector vector = new SparseHashDoubleVector(values.length);
    vector.set(row, values[row]);
    return vector;
  }
  


  public int columns()
  {
    return values.length;
  }
  




  public void set(int row, int col, double val)
  {
    checkIndices(row, col);
    
    if (row != col) {
      throw new IllegalArgumentException(
        "cannot set non-diagonal elements in a DiagonalMatrix");
    }
    values[row] = val;
  }
  




  public void setColumn(int column, double[] values)
  {
    checkIndices(values.length - 1, column);
    
    values[column] = values[column];
  }
  




  public void setColumn(int column, DoubleVector vector)
  {
    checkIndices(vector.length() - 1, column);
    
    values[column] = vector.get(column);
  }
  




  public void setRow(int row, double[] values)
  {
    checkIndices(row, values.length - 1);
    
    values[row] = values[row];
  }
  




  public void setRow(int row, DoubleVector vector)
  {
    checkIndices(row, vector.length() - 1);
    
    values[row] = vector.get(row);
  }
  


  public double[][] toDenseArray()
  {
    double[][] m = new double[values.length][values.length];
    for (int r = 0; r < values.length; r++) {
      m[r][r] = values[r];
    }
    return m;
  }
  


  public int rows()
  {
    return values.length;
  }
}
