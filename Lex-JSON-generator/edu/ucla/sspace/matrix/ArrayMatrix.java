package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.AbstractDoubleVector;
import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import java.io.Serializable;
import java.util.Arrays;




















































public class ArrayMatrix
  extends AbstractMatrix
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private final int rows;
  private final int cols;
  private final double[] matrix;
  
  public ArrayMatrix(int rows, int cols)
  {
    this.rows = rows;
    this.cols = cols;
    matrix = new double[rows * cols];
  }
  






  public ArrayMatrix(double[][] matrix2d)
  {
    if (matrix2d == null) {
      throw new IllegalArgumentException("invalid matrix dimensions");
    }
    rows = matrix2d.length;
    if (rows < 1) {
      throw new IllegalArgumentException("invalid matrix dimensions");
    }
    cols = matrix2d[0].length;
    if (cols < 1) {
      throw new IllegalArgumentException("invalid matrix dimensions");
    }
    matrix = new double[rows * cols];
    for (int i = 0; i < rows; i++) {
      if (cols != matrix2d[i].length)
        throw new IllegalArgumentException("invalid matrix dimensions");
      for (int j = 0; j < cols; j++) {
        set(i, j, matrix2d[i][j]);
      }
    }
  }
  












  public ArrayMatrix(int rows, int cols, double[] matrix1D)
  {
    this.rows = rows;
    this.cols = cols;
    if ((rows < 1) || (cols < 1))
      throw new IllegalArgumentException("invalid matrix dimensions");
    if (matrix1D == null)
      throw new NullPointerException("provided matrix cannot be null");
    if (matrix1D.length != rows * cols) {
      throw new IllegalArgumentException("provided matrix is wrong size");
    }
    matrix = matrix1D;
  }
  







  private void checkIndices(int row, int col)
  {
    if ((row < 0) || (row >= rows))
      throw new ArrayIndexOutOfBoundsException("row: " + row);
    if ((col < 0) || (col >= cols)) {
      throw new ArrayIndexOutOfBoundsException("column: " + col);
    }
  }
  

  public double get(int row, int col)
  {
    checkIndices(row, col);
    
    int index = getIndex(row, col);
    return matrix[index];
  }
  


  public double[] getColumn(int column)
  {
    checkIndices(0, column);
    
    double[] values = new double[rows];
    for (int row = 0; row < rows; row++)
      values[row] = get(row, column);
    return values;
  }
  


  public DoubleVector getColumnVector(int column)
  {
    return new DenseVector(getColumn(column));
  }
  


  public double[] getRow(int row)
  {
    checkIndices(row, 0);
    
    double[] rowArr = new double[cols];
    int index = getIndex(row, 0);
    for (int i = 0; i < cols; i++)
      rowArr[i] = matrix[(index++)];
    return rowArr;
  }
  


  public DoubleVector getRowVector(int row)
  {
    checkIndices(row, 0);
    return new RowVector(getIndex(row, 0));
  }
  


  public int columns()
  {
    return cols;
  }
  


  private int getIndex(int row, int col)
  {
    return row * cols + col;
  }
  


  public void set(int row, int col, double val)
  {
    checkIndices(row, col);
    
    int index = getIndex(row, col);
    matrix[index] = val;
  }
  


  public void setColumn(int column, double[] values)
  {
    checkIndices(values.length - 1, column);
    
    for (int row = 0; row < rows; row++) {
      matrix[getIndex(row, column)] = values[column];
    }
  }
  

  public void setColumn(int column, DoubleVector values)
  {
    checkIndices(values.length() - 1, column);
    
    for (int row = 0; row < rows; row++) {
      matrix[getIndex(row, column)] = values.get(row);
    }
  }
  

  public void setRow(int row, double[] columns)
  {
    checkIndices(row, columns.length - 1);
    
    for (int col = 0; col < cols; col++) {
      matrix[getIndex(row, col)] = columns[col];
    }
  }
  

  public void setRow(int row, DoubleVector values)
  {
    checkIndices(row, values.length() - 1);
    
    for (int col = 0; col < cols; col++) {
      matrix[getIndex(row, col)] = values.get(col);
    }
  }
  

  public double[][] toDenseArray()
  {
    double[][] m = new double[rows][cols];
    int i = 0;
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++)
        m[row][col] = matrix[(i++)];
    }
    return m;
  }
  


  public int rows()
  {
    return rows;
  }
  



  class RowVector
    extends AbstractDoubleVector
    implements Serializable
  {
    private static final long serialVersionUID = 1L;
    


    final int startIndex;
    

    double magnitude;
    


    public RowVector(int startIndex)
    {
      this.startIndex = startIndex;
      magnitude = -1.0D;
    }
    




    private int toMatrixIndex(int index)
    {
      int i = startIndex + index;
      if ((i < 0) || (i >= startIndex + cols))
        throw new IndexOutOfBoundsException(
          index + " outside vector bounds");
      return i;
    }
    


    public double add(int index, double delta)
    {
      int i = toMatrixIndex(index);
      magnitude = -1.0D;
      matrix[i] += delta;
      return matrix[i];
    }
    


    public void set(int index, double value)
    {
      int i = toMatrixIndex(index);
      magnitude = -1.0D;
      matrix[i] = value;
    }
    


    public void set(int index, Number value)
    {
      set(index, value.doubleValue());
    }
    


    public double get(int index)
    {
      int i = toMatrixIndex(index);
      return matrix[i];
    }
    


    public Double getValue(int index)
    {
      return Double.valueOf(get(index));
    }
    


    public double magnitude()
    {
      if (magnitude < 0.0D) {
        double m = 0.0D;
        for (int i = startIndex; i < startIndex + cols; i++) {
          double d = matrix[i];
          m += d * d;
        }
        magnitude = Math.sqrt(m);
      }
      return magnitude;
    }
    


    public double[] toArray()
    {
      return Arrays.copyOfRange(matrix, startIndex, startIndex + cols);
    }
    


    public int length()
    {
      return cols;
    }
  }
}
