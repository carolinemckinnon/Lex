package edu.ucla.sspace.matrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;


























































class ClutoSparseFileIterator
  implements Iterator<MatrixEntry>
{
  private final BufferedReader reader;
  private MatrixEntry next;
  private int rows;
  private int cols;
  private int curColIndex;
  private int curRow;
  private String[] curLine;
  
  public ClutoSparseFileIterator(File matrixFile)
    throws IOException
  {
    reader = new BufferedReader(new FileReader(matrixFile));
    String[] numRowCol = reader.readLine().split("\\s");
    rows = Integer.parseInt(numRowCol[0]);
    cols = Integer.parseInt(numRowCol[1]);
    
    curRow = 0;
    curColIndex = 0;
    curLine = readLine();
    next = advance();
  }
  
  private MatrixEntry advance() throws IOException {
    if (curRow >= rows) {
      return null;
    }
    

    if (curColIndex >= curLine.length) {
      curColIndex = 0;
      curRow += 1;
      

      if (curRow >= rows) {
        reader.close();
        return null;
      }
      
      curLine = readLine();
    }
    


    return new SimpleEntry(curRow, 
      Integer.parseInt(curLine[(curColIndex++)]) - 1, 
      Double.parseDouble(curLine[(curColIndex++)]));
  }
  




  private String[] readLine()
    throws IOException
  {
    String line = reader.readLine();
    if (line == null)
      throw new IllegalArgumentException(
        "The matrix file is improperly formatted");
    return line.split("\\s+");
  }
  


  public boolean hasNext()
  {
    return next != null;
  }
  


  public MatrixEntry next()
  {
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
