package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.SparseHashDoubleVector;
import java.io.Serializable;
import java.util.BitSet;
import java.util.Set;


























































public class SparseRowMaskedMatrix
  extends RowMaskedMatrix
  implements SparseMatrix, Serializable
{
  private static final long serialVersionUID = 1L;
  private final SparseMatrix matrix;
  
  public SparseRowMaskedMatrix(SparseMatrix matrix, BitSet included)
  {
    super(matrix, included);
    this.matrix = matrix;
  }
  










  public SparseRowMaskedMatrix(SparseMatrix matrix, Set<Integer> included)
  {
    super(matrix, included);
    this.matrix = matrix;
  }
  







  public SparseRowMaskedMatrix(SparseMatrix matrix, int[] reordering)
  {
    super(matrix, reordering);
    




    if ((matrix instanceof SparseRowMaskedMatrix)) {
      SparseRowMaskedMatrix srmm = (SparseRowMaskedMatrix)matrix;
      this.matrix = matrix;
    } else {
      this.matrix = matrix;
    }
  }
  


  public SparseDoubleVector getColumnVector(int column)
  {
    int rows = rows();
    SparseDoubleVector v = new SparseHashDoubleVector(rows);
    for (int row = 0; row < rows; row++) {
      double d = get(row, column);
      if (d != 0.0D)
        v.set(row, d);
    }
    return v;
  }
  


  public SparseDoubleVector getRowVector(int row)
  {
    return matrix.getRowVector(getRealRow(row));
  }
}
