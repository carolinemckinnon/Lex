package edu.ucla.sspace.vector;

import java.util.Arrays;
import java.util.Map;

























































public class MaskedSparseDoubleVectorView
  extends MaskedDoubleVectorView
  implements SparseDoubleVector
{
  private static final long serialVersionUID = 1L;
  private final SparseVector sparseVector;
  private final Map<Integer, Integer> reverseColumnMask;
  private int[] nonZeroIndices;
  
  public <T extends DoubleVector,  extends SparseVector<Double>> MaskedSparseDoubleVectorView(T v, int[] columnMask, Map<Integer, Integer> reverseMask)
  {
    super(v, columnMask);
    sparseVector = ((SparseVector)v);
    reverseColumnMask = reverseMask;
  }
  


  public int[] getNonZeroIndices()
  {
    if ((updated) || (nonZeroIndices == null)) {
      int[] indices = sparseVector.getNonZeroIndices();
      int[] newIndices = new int[indices.length];
      int i = 0;
      for (int index = 0; index < indices.length; index++) {
        Integer newIndex = (Integer)reverseColumnMask.get(Integer.valueOf(indices[index]));
        if (newIndex != null)
        {
          newIndices[(i++)] = newIndex.intValue(); }
      }
      nonZeroIndices = Arrays.copyOf(newIndices, i);
    }
    return nonZeroIndices;
  }
  


  public SparseDoubleVector instanceCopy()
  {
    throw new UnsupportedOperationException(
      "Cannot return a new instance of the decorated Double vector");
  }
}
