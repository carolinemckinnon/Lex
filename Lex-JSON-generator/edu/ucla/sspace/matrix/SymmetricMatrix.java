package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import java.io.Serializable;









































public class SymmetricMatrix
  extends AbstractMatrix
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private final double[][] values;
  private final int rows;
  private final int columns;
  
  public SymmetricMatrix(int rows, int columns)
  {
    this.rows = rows;
    this.columns = columns;
    
    values = new double[rows][];
    for (int r = 0; r < rows; r++) {
      values[r] = new double[r + 1];
    }
  }
  

  public int columns()
  {
    return columns;
  }
  



  public double get(int row, int column)
  {
    if (column > row) {
      int tmp = column;
      column = row;
      row = tmp;
    }
    return values[row][column];
  }
  


  public DoubleVector getColumnVector(int column)
  {
    DenseVector col = new DenseVector(rows);
    for (int r = 0; r < rows; r++)
      col.set(r, get(r, column));
    return col;
  }
  


  public DoubleVector getRowVector(int row)
  {
    DenseVector rowVec = new DenseVector(columns);
    for (int c = 0; c < columns; c++)
      rowVec.set(c, get(row, c));
    return rowVec;
  }
  


  public int rows()
  {
    return rows;
  }
  



  public void set(int row, int column, double val)
  {
    if (column > row) {
      int tmp = column;
      column = row;
      row = tmp;
    }
    values[row][column] = val;
  }
}
