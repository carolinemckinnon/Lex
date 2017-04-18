package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.MaskedDoubleVectorView;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;





















































public class CellMaskedMatrix
  implements Matrix, Serializable
{
  private static final long serialVersionUID = 1L;
  protected final int[] rowMaskMap;
  protected final int[] colMaskMap;
  private final Matrix matrix;
  
  public CellMaskedMatrix(Matrix matrix, int[] rowMaskMap, int[] colMaskMap)
  {
    this.matrix = matrix;
    this.rowMaskMap = rowMaskMap;
    this.colMaskMap = colMaskMap;
    assert (arrayToSet(rowMaskMap).size() == rowMaskMap.length) : 
      "input mapping contains duplicates mappings to the same row";
    assert (arrayToSet(colMaskMap).size() == colMaskMap.length) : 
      "input mapping contains duplicates mappings to the same column";
  }
  



  private Set<Integer> arrayToSet(int[] arr)
  {
    Set<Integer> s = new HashSet();
    for (int value : arr)
      s.add(Integer.valueOf(value));
    return s;
  }
  



  protected int getIndexFromMap(int[] maskMap, int index)
  {
    if ((index < 0) || (index >= maskMap.length))
      throw new IndexOutOfBoundsException(
        "The given index is beyond the bounds of the matrix");
    int newIndex = maskMap[index];
    if ((newIndex < 0) || 
      ((maskMap == rowMaskMap) && (newIndex >= matrix.rows())) || (
      (maskMap == colMaskMap) && (newIndex >= matrix.columns())))
      throw new IndexOutOfBoundsException(
        "The mapped index is beyond the bounds of the base matrix");
    return newIndex;
  }
  


  public double get(int row, int col)
  {
    row = getIndexFromMap(rowMaskMap, row);
    col = getIndexFromMap(colMaskMap, col);
    
    return matrix.get(row, col);
  }
  


  public double[] getColumn(int column)
  {
    column = getIndexFromMap(colMaskMap, column);
    double[] values = new double[rows()];
    for (int r = 0; r < rows(); r++)
      values[r] = matrix.get(getIndexFromMap(rowMaskMap, r), column);
    return values;
  }
  


  public DoubleVector getColumnVector(int column)
  {
    column = getIndexFromMap(colMaskMap, column);
    DoubleVector v = matrix.getColumnVector(column);
    return new MaskedDoubleVectorView(v, rowMaskMap);
  }
  



  public double[] getRow(int row)
  {
    row = getIndexFromMap(rowMaskMap, row);
    double[] values = new double[columns()];
    for (int c = 0; c < columns(); c++)
      values[c] = matrix.get(row, getIndexFromMap(colMaskMap, c));
    return values;
  }
  


  public DoubleVector getRowVector(int row)
  {
    row = getIndexFromMap(rowMaskMap, row);
    DoubleVector v = matrix.getRowVector(row);
    return new MaskedDoubleVectorView(v, colMaskMap);
  }
  


  public int columns()
  {
    return colMaskMap.length;
  }
  


  public double[][] toDenseArray()
  {
    double[][] values = new double[rows()][columns()];
    for (int r = 0; r < rows(); r++)
      for (int c = 0; c < columns(); c++)
        values[r][c] = get(r, c);
    return values;
  }
  


  public int rows()
  {
    return rowMaskMap.length;
  }
  


  public void set(int row, int col, double val)
  {
    row = getIndexFromMap(rowMaskMap, row);
    col = getIndexFromMap(colMaskMap, col);
    
    matrix.set(row, col, val);
  }
  


  public void setColumn(int column, double[] values)
  {
    column = getIndexFromMap(colMaskMap, column);
    for (int r = 0; r < rows(); r++) {
      matrix.set(getIndexFromMap(rowMaskMap, r), column, values[r]);
    }
  }
  

  public void setColumn(int column, DoubleVector values)
  {
    column = getIndexFromMap(colMaskMap, column);
    for (int r = 0; r < rows(); r++) {
      matrix.set(getIndexFromMap(rowMaskMap, r), column, values.get(r));
    }
  }
  

  public void setRow(int row, double[] columns)
  {
    row = getIndexFromMap(rowMaskMap, row);
    for (int c = 0; c < columns(); c++) {
      matrix.set(row, getIndexFromMap(colMaskMap, c), columns[c]);
    }
  }
  

  public void setRow(int row, DoubleVector values)
  {
    row = getIndexFromMap(rowMaskMap, row);
    for (int c = 0; c < columns(); c++) {
      matrix.set(row, getIndexFromMap(colMaskMap, c), values.get(c));
    }
  }
}
