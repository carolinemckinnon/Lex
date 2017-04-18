package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.SparseHashDoubleVector;
import java.io.Serializable;


















































public class SparseHashMatrix
  extends AbstractMatrix
  implements SparseMatrix, Serializable
{
  private static final long serialVersionUID = 1L;
  private final int rows;
  private final int columns;
  private final SparseHashDoubleVector[] sparseMatrix;
  
  public SparseHashMatrix(int rows, int columns)
  {
    this.rows = rows;
    this.columns = columns;
    sparseMatrix = new SparseHashDoubleVector[rows];
    for (int r = 0; r < rows; r++) {
      sparseMatrix[r] = new SparseHashDoubleVector(columns);
    }
  }
  


  private void checkIndices(int row, int col)
  {
    if ((row < 0) || (col < 0) || (row >= rows) || (col >= columns)) {
      throw new IndexOutOfBoundsException();
    }
  }
  


  public int columns()
  {
    return columns;
  }
  


  public SparseDoubleVector getColumnVector(int column)
  {
    SparseHashDoubleVector col = new SparseHashDoubleVector(rows);
    for (int r = 0; r < rows(); r++)
      col.set(r, getRowVector(r).get(column));
    return col;
  }
  


  public SparseDoubleVector getRowVector(int row)
  {
    return sparseMatrix[row];
  }
  


  public int rows()
  {
    return rows;
  }
  


  public void set(int row, int col, double val)
  {
    checkIndices(row, col);
    sparseMatrix[row].set(col, val);
  }
}
