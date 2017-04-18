package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.SparseHashDoubleVector;
import java.io.Serializable;










































public class SparseSymmetricMatrix
  extends AbstractMatrix
  implements SparseMatrix, Serializable
{
  private static final long serialVersionUID = 1L;
  private final SparseMatrix backing;
  
  public SparseSymmetricMatrix(SparseMatrix backing)
  {
    this.backing = backing;
  }
  


  public int columns()
  {
    return backing.columns();
  }
  




  public double get(int row, int column)
  {
    if (row > column) {
      int tmp = column;
      column = row;
      row = tmp;
    }
    return backing.get(row, column);
  }
  


  public SparseDoubleVector getColumnVector(int column)
  {
    int rows = rows();
    SparseHashDoubleVector col = new SparseHashDoubleVector(rows);
    for (int r = 0; r < rows; r++)
      col.set(r, get(r, column));
    return col;
  }
  


  public SparseDoubleVector getRowVector(int row)
  {
    int cols = columns();
    SparseHashDoubleVector rowVec = new SparseHashDoubleVector(cols);
    for (int c = 0; c < cols; c++)
      rowVec.set(c, get(row, c));
    return rowVec;
  }
  


  public int rows()
  {
    return backing.rows();
  }
  



  public void set(int row, int column, double val)
  {
    if (row > column) {
      int tmp = column;
      column = row;
      row = tmp;
    }
    backing.set(row, column, val);
  }
}
