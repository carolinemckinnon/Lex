package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.CompactSparseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.SparseHashDoubleVector;
import edu.ucla.sspace.vector.Vectors;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;






























































public class GrowingSparseMatrix
  extends AbstractMatrix
  implements SparseMatrix, Serializable
{
  private static final long serialVersionUID = 1L;
  private int rows;
  private int cols;
  private final Map<Integer, SparseDoubleVector> rowToColumns;
  private final SparseDoubleVector emptyVector;
  
  public GrowingSparseMatrix()
  {
    this(new CompactSparseVector());
  }
  






  public GrowingSparseMatrix(SparseDoubleVector emptyVector)
  {
    this(0, 0, emptyVector);
  }
  








  public GrowingSparseMatrix(int rows, int columns, SparseDoubleVector emptyVector)
  {
    if (emptyVector.length() != Integer.MAX_VALUE) {
      throw new IllegalArgumentException(
        "The given empty vector has a restricted bound.  Given empty vectors must have no bound");
    }
    
    this.rows = rows;
    cols = columns;
    this.emptyVector = emptyVector;
    rowToColumns = new HashMap();
  }
  


  private void checkIndices(int row, int col)
  {
    if ((row < 0) || (col < 0)) {
      throw new ArrayIndexOutOfBoundsException("Negative index");
    }
  }
  

  public double get(int row, int col)
  {
    checkIndices(row, col);
    SparseDoubleVector sv = (SparseDoubleVector)rowToColumns.get(Integer.valueOf(row));
    return sv == null ? 0.0D : sv.get(col);
  }
  


  public double[] getColumn(int column)
  {
    double[] values = new double[rows()];
    for (int row = 0; row < rows(); row++)
      values[row] = get(row, column);
    return values;
  }
  





  public SparseDoubleVector getColumnVector(int column)
  {
    SparseDoubleVector values = new SparseHashDoubleVector(rows);
    for (int row = 0; row < rows; row++) {
      double d = get(row, column);
      if (d != 0.0D)
        values.set(row, d);
    }
    return values;
  }
  


  public double[] getRow(int row)
  {
    return toArray((DoubleVector)rowToColumns.get(Integer.valueOf(row)), cols);
  }
  







  public SparseDoubleVector getRowVector(int row)
  {
    SparseDoubleVector v = (SparseDoubleVector)rowToColumns.get(Integer.valueOf(row));
    return v != null ? 
      Vectors.subview(v, 0, cols) : 
      Vectors.subview(emptyVector, 0, cols);
  }
  


  public int columns()
  {
    return cols;
  }
  







  public void set(int row, int col, double val)
  {
    checkIndices(row, col);
    

    if (row + 1 > rows)
      rows = (row + 1);
    if (col + 1 > cols) {
      cols = (col + 1);
    }
    updateRow(row).set(col, val);
  }
  


  public void setColumn(int column, double[] values)
  {
    for (int row = 0; row < rows(); row++) {
      set(row, column, values[row]);
    }
  }
  

  public void setColumn(int column, DoubleVector values)
  {
    for (int row = 0; row < rows(); row++) {
      set(row, column, values.get(row));
    }
  }
  






  public void setRow(int row, double[] columns)
  {
    checkIndices(row, columns.length - 1);
    
    if (cols <= columns.length) {
      cols = columns.length;
    }
    SparseDoubleVector rowVec = updateRow(row);
    
    for (int col = 0; col < cols; col++) {
      double val = columns[col];
      rowVec.set(col, val);
    }
  }
  



  private SparseDoubleVector updateRow(int row)
  {
    SparseDoubleVector rowVec = (SparseDoubleVector)rowToColumns.get(Integer.valueOf(row));
    if (rowVec == null) {
      rowVec = emptyVector.instanceCopy();
      rowToColumns.put(Integer.valueOf(row), rowVec);
    }
    
    return rowVec;
  }
  







  public void setRow(int row, DoubleVector data)
  {
    checkIndices(row, data.length() - 1);
    
    if (cols <= data.length()) {
      cols = data.length();
    }
    Vectors.copy(updateRow(row), data);
  }
  


  public double[][] toDenseArray()
  {
    double[][] m = new double[rows][cols];
    
    for (int r = 0; r < rows; r++)
      m[r] = toArray((DoubleVector)rowToColumns.get(Integer.valueOf(r)), cols);
    return m;
  }
  


  public int rows()
  {
    return rows;
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
