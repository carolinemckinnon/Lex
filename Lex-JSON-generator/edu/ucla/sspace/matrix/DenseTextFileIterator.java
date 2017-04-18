package edu.ucla.sspace.matrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;


























































class DenseTextFileIterator
  implements Iterator<MatrixEntry>
{
  private final BufferedReader reader;
  private MatrixEntry next;
  private int rows;
  private int cols;
  private int curCol;
  private int curRow;
  private String[] curLine;
  
  public DenseTextFileIterator(File matrixFile)
    throws IOException
  {
    reader = new BufferedReader(new FileReader(matrixFile));
    curLine = reader.readLine().split("\\s+");
    curCol = 0;
    curRow = 0;
    next = advance();
  }
  
  private MatrixEntry advance() throws IOException {
    if (curLine == null) {
      return null;
    }
    

    if (curCol >= curLine.length) {
      curCol = 0;
      curRow += 1;
      

      String line = reader.readLine();
      if (line == null) {
        curLine = null;
        reader.close();
        return null;
      }
      
      curLine = line.split("\\s+");
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
