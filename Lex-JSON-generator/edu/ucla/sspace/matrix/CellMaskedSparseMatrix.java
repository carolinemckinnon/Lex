package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.MaskedSparseDoubleVectorView;
import edu.ucla.sspace.vector.SparseDoubleVector;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;



































































public class CellMaskedSparseMatrix
  extends CellMaskedMatrix
  implements SparseMatrix, Serializable
{
  private static final long serialVersionUID = 1L;
  private final SparseMatrix matrix;
  private final int[] rowMaskMap;
  private final Map<Integer, Integer> reverseRowMaskMap;
  private final int[] colMaskMap;
  private final Map<Integer, Integer> reverseColMaskMap;
  
  public CellMaskedSparseMatrix(SparseMatrix matrix, int[] rowMaskMap, int[] colMaskMap)
  {
    super(matrix, rowMaskMap, colMaskMap);
    this.matrix = matrix;
    this.rowMaskMap = rowMaskMap;
    this.colMaskMap = colMaskMap;
    reverseRowMaskMap = new HashMap();
    for (int i = 0; i < rowMaskMap.length; i++)
      reverseRowMaskMap.put(Integer.valueOf(rowMaskMap[i]), Integer.valueOf(i));
    reverseColMaskMap = new HashMap();
    for (int i = 0; i < colMaskMap.length; i++) {
      reverseColMaskMap.put(Integer.valueOf(colMaskMap[i]), Integer.valueOf(i));
    }
  }
  

  public SparseDoubleVector getRowVector(int row)
  {
    row = getIndexFromMap(rowMaskMap, row);
    return new MaskedSparseDoubleVectorView(
      matrix.getRowVector(row), colMaskMap, reverseColMaskMap);
  }
  


  public SparseDoubleVector getColumnVector(int col)
  {
    col = getIndexFromMap(colMaskMap, col);
    return new MaskedSparseDoubleVectorView(
      matrix.getColumnVector(col), rowMaskMap, reverseRowMaskMap);
  }
}
