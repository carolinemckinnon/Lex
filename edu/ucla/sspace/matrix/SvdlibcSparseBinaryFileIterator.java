package edu.ucla.sspace.matrix;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;














































class SvdlibcSparseBinaryFileIterator
  implements Iterator<MatrixEntry>
{
  private final DataInputStream dis;
  private MatrixEntry next;
  private int entry;
  private int nzEntriesInMatrix;
  private int curCol;
  private int nzInCurCol;
  private int curColEntry;
  
  public SvdlibcSparseBinaryFileIterator(File matrixFile)
    throws IOException
  {
    dis = new DataInputStream(
      new BufferedInputStream(new FileInputStream(matrixFile)));
    int rows = dis.readInt();
    int cols = dis.readInt();
    nzEntriesInMatrix = dis.readInt();
    nzInCurCol = dis.readInt();
    entry = 0;
    curCol = 0;
    curColEntry = 0;
    advance();
  }
  
  private void advance() throws IOException {
    if (entry >= nzEntriesInMatrix) {
      next = null;
      
      dis.close();
    }
    else
    {
      try
      {
        do {
          curColEntry = 0;
          nzInCurCol = dis.readInt();
          curCol += 1;
        } while (curColEntry == nzInCurCol);
        



        int row = dis.readInt();
        double value = dis.readFloat();
        next = new SimpleEntry(row, curCol, value);
        curColEntry += 1;
        entry += 1;
      }
      catch (IOException ioe) {
        throw new MatrixIOException(
          "Missing data when reading.  Truncated file?", ioe);
      }
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
