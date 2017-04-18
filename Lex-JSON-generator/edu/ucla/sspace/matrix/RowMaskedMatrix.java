package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseVector;
import java.io.Serializable;
import java.util.Arrays;
import java.util.BitSet;
import java.util.LinkedHashSet;
import java.util.Set;
































































public class RowMaskedMatrix
  extends AbstractMatrix
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private final Matrix backingMatrix;
  private final int rows;
  private final int[] rowToReal;
  
  public RowMaskedMatrix(Matrix matrix, BitSet included)
  {
    backingMatrix = matrix;
    rowToReal = new int[included.cardinality()];
    int i = included.nextSetBit(0); for (int row = 0; i >= 0; 
        row++) {
      if (i >= matrix.rows())
        throw new IllegalArgumentException(
          "specified row not present in original matrix: " + i);
      rowToReal[row] = i;i = included.nextSetBit(i + 1);
    }
    rows = rowToReal.length;
  }
  










  public RowMaskedMatrix(Matrix matrix, Set<Integer> included)
  {
    backingMatrix = matrix;
    rowToReal = new int[included.size()];
    


    int[] rowArr = new int[included.size()];
    int i = 0;
    for (Integer j : included) {
      if ((j.intValue() < 0) || (j.intValue() >= matrix.rows()))
        throw new IllegalArgumentException("Cannot specify a row outside the original matrix dimensions:" + 
          j);
      rowToReal[(i++)] = j.intValue();
    }
    Arrays.sort(rowToReal);
    rows = rowToReal.length;
  }
  








  public RowMaskedMatrix(Matrix matrix, LinkedHashSet<Integer> included)
  {
    backingMatrix = matrix;
    rowToReal = new int[included.size()];
    
    int i = 0;
    for (Integer j : included) {
      if ((j.intValue() < 0) || (j.intValue() >= matrix.rows()))
        throw new IllegalArgumentException("Cannot specify a row outside the original matrix dimensions:" + 
          j);
      rowToReal[(i++)] = j.intValue();
    }
    rows = rowToReal.length;
  }
  







  public RowMaskedMatrix(Matrix matrix, int[] reordering)
  {
    rowToReal = new int[reordering.length];
    rows = reordering.length;
    




    if ((matrix instanceof RowMaskedMatrix)) {
      RowMaskedMatrix rmm = (RowMaskedMatrix)matrix;
      backingMatrix = backingMatrix;
      
      for (int i = 0; i < reordering.length; i++) {
        int j = reordering[i];
        if ((j < 0) || (j >= matrix.rows())) {
          throw new IllegalArgumentException("Cannot specify a row outside the original matrix dimensions:" + 
            j);
        }
        rowToReal[i] = rowToReal[j];
      }
    } else {
      backingMatrix = matrix;
      for (int i = 0; i < reordering.length; i++) {
        int j = reordering[i];
        if ((j < 0) || (j >= matrix.rows()))
          throw new IllegalArgumentException("Cannot specify a row outside the original matrix dimensions:" + 
            j);
        rowToReal[i] = j;
      }
    }
  }
  



  protected int getRealRow(int virtualRow)
  {
    if ((virtualRow < 0) || (virtualRow >= rows))
      throw new IndexOutOfBoundsException(
        "row out of bounds: " + virtualRow);
    return rowToReal[virtualRow];
  }
  


  public double get(int row, int col)
  {
    return backingMatrix.get(getRealRow(row), col);
  }
  


  public double[] getColumn(int column)
  {
    double[] col = new double[rows];
    for (int i = 0; i < rowToReal.length; i++)
      col[i] = backingMatrix.get(rowToReal[i], column);
    return col;
  }
  


  public DoubleVector getColumnVector(int column)
  {
    return new DenseVector(getColumn(column));
  }
  


  public double[] getRow(int row)
  {
    return backingMatrix.getRow(getRealRow(row));
  }
  


  public DoubleVector getRowVector(int row)
  {
    return backingMatrix.getRowVector(getRealRow(row));
  }
  


  public int columns()
  {
    return backingMatrix.columns();
  }
  


  public double[][] toDenseArray()
  {
    double[][] arr = new double[rows][backingMatrix.columns()];
    for (int i = 0; i < rowToReal.length; i++)
      arr[i] = backingMatrix.getRow(rowToReal[i]);
    return arr;
  }
  


  public int rows()
  {
    return rows;
  }
  


  public void set(int row, int col, double val)
  {
    backingMatrix.set(getRealRow(row), col, val);
  }
  


  public void setColumn(int column, double[] values)
  {
    if (values.length != rows) {
      throw new IllegalArgumentException("cannot set a column whose dimensions are different than the matrix");
    }
    for (int i = 0; i < rowToReal.length; i++) {
      backingMatrix.set(rowToReal[i], column, values[i]);
    }
  }
  

  public void setColumn(int column, DoubleVector values)
  {
    if (values.length() != rows) {
      throw new IllegalArgumentException("cannot set a column whose dimensions are different than the matrix");
    }
    if ((values instanceof SparseVector)) {
      SparseVector sv = (SparseVector)values;
      for (int nz : sv.getNonZeroIndices()) {
        backingMatrix.set(getRealRow(nz), nz, values.get(nz));
      }
    } else {
      for (int i = 0; i < rowToReal.length; i++) {
        backingMatrix.set(rowToReal[i], i, values.get(i));
      }
    }
  }
  

  public void setRow(int row, double[] columns)
  {
    backingMatrix.setRow(getRealRow(row), columns);
  }
  


  public void setRow(int row, DoubleVector values)
  {
    backingMatrix.setRow(getRealRow(row), values);
  }
  


  public Matrix backingMatrix()
  {
    return backingMatrix;
  }
  



  public int[] reordering()
  {
    return rowToReal;
  }
}
