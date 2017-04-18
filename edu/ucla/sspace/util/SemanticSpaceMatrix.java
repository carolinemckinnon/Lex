package edu.ucla.sspace.util;

import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.matrix.AbstractMatrix;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.Vectors;
import java.util.Set;



















































public class SemanticSpaceMatrix
  extends AbstractMatrix
{
  private final SemanticSpace sspace;
  private final BiMap<Integer, String> rowToWord;
  private final int columns;
  
  public SemanticSpaceMatrix(SemanticSpace sspace)
  {
    this.sspace = sspace;
    rowToWord = new HashBiMap();
    columns = sspace.getVectorLength();
    
    for (String word : sspace.getWords()) {
      rowToWord.put(Integer.valueOf(rowToWord.size()), word);
    }
  }
  


  private boolean checkModifications()
  {
    return (sspace.getWords().size() != rowToWord.size()) || 
      (columns != sspace.getVectorLength());
  }
  



  public Integer getRowIndex(String term)
  {
    return (Integer)rowToWord.inverse().get(term);
  }
  


  public DoubleVector getRowVector(int row)
  {
    if ((row < 0) || (row >= rowToWord.size()))
      throw new IndexOutOfBoundsException("Row is out of bounds: " + row);
    return Vectors.asDouble(sspace.getVector((String)rowToWord.get(Integer.valueOf(row))));
  }
  


  public int columns()
  {
    return columns;
  }
  


  public int rows()
  {
    return rowToWord.size();
  }
  


  public void set(int row, int col, double val)
  {
    throw new UnsupportedOperationException(
      "Cannot modify SemanticSpace-backed matrix");
  }
  


  public void setColumn(int column, DoubleVector values)
  {
    throw new UnsupportedOperationException(
      "Cannot modify SemanticSpace-backed matrix");
  }
  


  public void setRow(int row, DoubleVector values)
  {
    throw new UnsupportedOperationException(
      "Cannot modify SemanticSpace-backed matrix");
  }
}
