package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.ScaledSparseDoubleVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import java.io.Serializable;









































public class RowScaledSparseMatrix
  extends RowScaledMatrix
  implements SparseMatrix, Serializable
{
  private static final long serialVersionUID = 1L;
  private final SparseMatrix m;
  private final DoubleVector scales;
  
  public RowScaledSparseMatrix(SparseMatrix matrix, DoubleVector v)
  {
    super(matrix, v);
    m = matrix;
    scales = v;
  }
  


  public SparseDoubleVector getColumnVector(int row)
  {
    throw new UnsupportedOperationException("cannot get row");
  }
  


  public SparseDoubleVector getRowVector(int row)
  {
    return new ScaledSparseDoubleVector(
      m.getRowVector(row), scales.get(row));
  }
}
