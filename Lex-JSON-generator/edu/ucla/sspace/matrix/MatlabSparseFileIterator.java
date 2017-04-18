package edu.ucla.sspace.matrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;






























class MatlabSparseFileIterator
  implements Iterator<MatrixEntry>
{
  private final BufferedReader matrixFileReader;
  private MatrixEntry next;
  private int lineNo;
  
  public MatlabSparseFileIterator(File matrixFile)
    throws IOException
  {
    matrixFileReader = new BufferedReader(new FileReader(matrixFile));
    lineNo = 0;
    advance();
  }
  
  private void advance() throws IOException {
    String line = matrixFileReader.readLine();
    lineNo += 1;
    if (line == null) {
      next = null;
      
      matrixFileReader.close();
    }
    else {
      String[] rowColVal = line.split("\\s+");
      if (rowColVal.length != 3)
        throw new MatrixIOException(
          "Incorrect number of values on line: " + lineNo);
      int row = Integer.parseInt(rowColVal[0]) - 1;
      int col = Integer.parseInt(rowColVal[1]) - 1;
      double value = Double.parseDouble(rowColVal[2]);
      next = new SimpleEntry(row, col, value);
    }
  }
  
  public boolean hasNext() {
    return next != null;
  }
  
  public MatrixEntry next() {
    if (next == null)
      throw new NoSuchElementException("No futher entries");
    MatrixEntry me = next;
    try {
      advance();
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
