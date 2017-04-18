package edu.ucla.sspace.matrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;


























































class SvdlibcDenseTextFileIterator
  implements Iterator<MatrixEntry>
{
  private final BufferedReader reader;
  private MatrixEntry next;
  private int rows;
  private int cols;
  private int curCol;
  private int curRow;
  private String[] curLine;
  
  public SvdlibcDenseTextFileIterator(File matrixFile)
    throws IOException
  {
    reader = new BufferedReader(new FileReader(matrixFile));
    String[] numRowCol = reader.readLine().split("\\s");
    rows = Integer.parseInt(numRowCol[0]);
    cols = Integer.parseInt(numRowCol[1]);
    
    curLine = reader.readLine().split("\\s+");
    curCol = 0;
    curRow = 0;
    next = advance();
  }
  
  private MatrixEntry advance() throws IOException {
    if (curRow >= rows) {
      return null;
    }
    

    if (curCol >= cols) {
      curCol = 0;
      curRow += 1;
      

      if (curRow >= rows) {
        reader.close();
        return null;
      }
      curLine = reader.readLine().split("\\s+");
    }
    

    return new SimpleEntry(curRow, curCol, 
      Double.parseDouble(curLine[(curCol++)]));
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
