package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseVector;
import java.io.Serializable;
import java.util.Arrays;
import java.util.BitSet;
import java.util.LinkedHashSet;
import java.util.Set;
































































public class ColumnMaskedMatrix
  extends AbstractMatrix
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private final Matrix backingMatrix;
  private final int columns;
  private final int[] columnToReal;
  
  public ColumnMaskedMatrix(Matrix matrix, BitSet included)
  {
    backingMatrix = matrix;
    columnToReal = new int[included.cardinality()];
    int i = included.nextSetBit(0); for (int column = 0; i >= 0; 
        column++) {
      if (i >= matrix.columns())
        throw new IllegalArgumentException(
          "specified column not present in original matrix: " + i);
      columnToReal[column] = i;i = included.nextSetBit(i + 1);
    }
    columns = columnToReal.length;
  }
  










  public ColumnMaskedMatrix(Matrix matrix, Set<Integer> included)
  {
    backingMatrix = matrix;
    columnToReal = new int[included.size()];
    


    int[] columnArr = new int[included.size()];
    int i = 0;
    for (Integer j : included) {
      if ((j.intValue() < 0) || (j.intValue() >= matrix.columns()))
        throw new IllegalArgumentException("Cannot specify a column outside the original matrix dimensions:" + 
          j);
      columnToReal[(i++)] = j.intValue();
    }
    Arrays.sort(columnToReal);
    columns = columnToReal.length;
  }
  








  public ColumnMaskedMatrix(Matrix matrix, LinkedHashSet<Integer> included)
  {
    backingMatrix = matrix;
    columnToReal = new int[included.size()];
    
    int i = 0;
    for (Integer j : included) {
      if ((j.intValue() < 0) || (j.intValue() >= matrix.columns()))
        throw new IllegalArgumentException("Cannot specify a column outside the original matrix dimensions:" + 
          j);
      columnToReal[(i++)] = j.intValue();
    }
    columns = columnToReal.length;
  }
  







  public ColumnMaskedMatrix(Matrix matrix, int[] reordering)
  {
    columnToReal = new int[reordering.length];
    columns = reordering.length;
    




    if ((matrix instanceof ColumnMaskedMatrix)) {
      ColumnMaskedMatrix rmm = (ColumnMaskedMatrix)matrix;
      backingMatrix = backingMatrix;
      
      for (int i = 0; i < reordering.length; i++) {
        int j = reordering[i];
        if ((j < 0) || (j >= matrix.columns())) {
          throw new IllegalArgumentException("Cannot specify a column outside the original matrix dimensions:" + 
            j);
        }
        columnToReal[i] = columnToReal[j];
      }
    } else {
      backingMatrix = matrix;
      for (int i = 0; i < reordering.length; i++) {
        int j = reordering[i];
        if ((j < 0) || (j >= matrix.columns()))
          throw new IllegalArgumentException("Cannot specify a column outside the original matrix dimensions:" + 
            j);
        columnToReal[i] = j;
      }
    }
  }
  



  protected int getRealColumn(int virtualColumn)
  {
    if ((virtualColumn < 0) || (virtualColumn >= columns))
      throw new IndexOutOfBoundsException(
        "column out of bounds: " + virtualColumn);
    return columnToReal[virtualColumn];
  }
  


  public double get(int row, int col)
  {
    return backingMatrix.get(row, getRealColumn(col));
  }
  


  public double[] getRow(int row)
  {
    double[] arr = new double[columns];
    for (int i = 0; i < columnToReal.length; i++)
      arr[i] = backingMatrix.get(row, columnToReal[i]);
    return arr;
  }
  


  public DoubleVector getRowVector(int row)
  {
    return new DenseVector(getRow(row));
  }
  


  public double[] getColumn(int column)
  {
    return backingMatrix.getColumn(getRealColumn(column));
  }
  


  public DoubleVector getColumnVector(int column)
  {
    return backingMatrix.getColumnVector(getRealColumn(column));
  }
  


  public int rows()
  {
    return backingMatrix.rows();
  }
  


  public double[][] toDenseArray()
  {
    double[][] arr = new double[backingMatrix.rows()][columns];
    for (int i = 0; i < columnToReal.length; i++)
      arr[i] = backingMatrix.getColumn(columnToReal[i]);
    return arr;
  }
  


  public int columns()
  {
    return columns;
  }
  


  public void set(int row, int col, double val)
  {
    backingMatrix.set(row, getRealColumn(col), val);
  }
  


  public void setRow(int row, double[] values)
  {
    if (values.length != columns) {
      throw new IllegalArgumentException("cannot set a row whose dimensions are different than the matrix");
    }
    for (int i = 0; i < columnToReal.length; i++) {
      backingMatrix.set(row, columnToReal[i], values[i]);
    }
  }
  

  public void setRow(int row, DoubleVector values)
  {
    if (values.length() != columns) {
      throw new IllegalArgumentException("cannot set a row whose dimensions are different than the matrix");
    }
    if ((values instanceof SparseVector)) {
      SparseVector sv = (SparseVector)values;
      for (int nz : sv.getNonZeroIndices()) {
        backingMatrix.set(nz, getRealColumn(nz), values.get(nz));
      }
    } else {
      for (int i = 0; i < columnToReal.length; i++) {
        backingMatrix.set(i, columnToReal[i], values.get(i));
      }
    }
  }
  

  public void setColumn(int column, double[] columns)
  {
    backingMatrix.setColumn(getRealColumn(column), columns);
  }
  


  public void setColumn(int column, DoubleVector values)
  {
    backingMatrix.setColumn(getRealColumn(column), values);
  }
  


  public Matrix backingMatrix()
  {
    return backingMatrix;
  }
  



  public int[] reordering()
  {
    return columnToReal;
  }
}
