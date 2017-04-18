package edu.ucla.sspace.matrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;






















































class SvdlibcSparseTextFileIterator
  implements Iterator<MatrixEntry>
{
  private final BufferedReader reader;
  private MatrixEntry next;
  private int rows;
  private int cols;
  private int curNonZeros;
  private int curCol;
  
  public SvdlibcSparseTextFileIterator(File matrixFile)
    throws IOException
  {
    reader = new BufferedReader(new FileReader(matrixFile));
    String[] numRowCol = reader.readLine().split("\\s");
    rows = Integer.parseInt(numRowCol[0]);
    cols = Integer.parseInt(numRowCol[1]);
    
    curCol = 0;
    curNonZeros = Integer.parseInt(reader.readLine());
    next = advance();
  }
  
  private MatrixEntry advance() throws IOException {
    if (curCol >= cols) {
      return null;
    }
    

    if (curNonZeros == 0) {
      curCol += 1;
      

      if (curCol >= cols) {
        reader.close();
        return null;
      }
      

      curNonZeros = Integer.parseInt(reader.readLine());
    }
    


    String[] rowValue = reader.readLine().split("\\s+");
    curNonZeros -= 1;
    return new SimpleEntry(Integer.parseInt(rowValue[0]), 
      curCol, 
      Double.parseDouble(rowValue[1]));
  }
  
  public boolean hasNext() {
    return next != null;
  }
  
  public MatrixEntry next() {
    if (next == null)
      throw new NoSuchElementException("No futher entries");
    MatrixEntry me = next;
    try {
      next = advance();
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
    return me;
  }
  


  public void remove()
  {
    throw new UnsupportedOperationException("Cannot remove from file");
  }
}
