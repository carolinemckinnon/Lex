package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.CompactSparseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import java.io.Serializable;
import java.util.List;


































class SparseListMatrix<T extends SparseDoubleVector>
  extends ListMatrix<T>
  implements SparseMatrix, Serializable
{
  private static final long serialVersionUID = 1L;
  
  public SparseListMatrix(List<T> vectorList)
  {
    super(vectorList);
  }
  
  public SparseListMatrix(List<T> vectorList, int columns) {
    super(vectorList, columns);
  }
  


  public SparseDoubleVector getColumnVector(int column)
  {
    int i = 0;
    SparseDoubleVector columnValues = 
      new CompactSparseVector(vectors.size());
    
    for (DoubleVector vector : vectors)
      columnValues.set(i++, vector.get(column));
    return columnValues;
  }
  


  public void setColumn(int column, SparseDoubleVector values)
  {
    int i = 0;
    int[] nonZeros = values.getNonZeroIndices();
    for (int index : nonZeros) {
      ((SparseDoubleVector)vectors.get(index)).set(column, values.get(index));
    }
  }
  

  public void setRow(int row, SparseDoubleVector values)
  {
    SparseDoubleVector v = (SparseDoubleVector)vectors.get(row);
    int[] nonZeros = values.getNonZeroIndices();
    for (int index : nonZeros) {
      v.set(index, values.get(index));
    }
  }
}
