package edu.ucla.sspace.matrix;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;


















































class SvdlibcDenseBinaryFileIterator
  implements Iterator<MatrixEntry>
{
  private final DataInputStream dis;
  private MatrixEntry next;
  private int rows;
  private int cols;
  private int curCol;
  private int curRow;
  
  public SvdlibcDenseBinaryFileIterator(File matrixFile)
    throws IOException
  {
    dis = new DataInputStream(
      new BufferedInputStream(new FileInputStream(matrixFile)));
    rows = dis.readInt();
    cols = dis.readInt();
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
    }
    

    if (curRow >= rows) {
      dis.close();
      return null;
    }
    

    return new SimpleEntry(curRow, curCol++, dis.readFloat());
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
