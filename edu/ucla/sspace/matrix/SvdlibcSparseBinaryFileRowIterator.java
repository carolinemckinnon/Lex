package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.SparseHashDoubleVector;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Iterator;
import java.util.NoSuchElementException;



































































class SvdlibcSparseBinaryFileRowIterator
  implements Iterator<SparseDoubleVector>
{
  private final ByteBuffer data;
  private SparseDoubleVector next;
  private int entry;
  private int nzEntriesInMatrix;
  private int curCol;
  private final int rows;
  private final int cols;
  
  public SvdlibcSparseBinaryFileRowIterator(File matrixFile)
    throws IOException
  {
    RandomAccessFile raf = new RandomAccessFile(matrixFile, "r");
    FileChannel fc = raf.getChannel();
    data = fc.map(FileChannel.MapMode.READ_ONLY, 0L, fc.size());
    fc.close();
    

    rows = data.getInt();
    cols = data.getInt();
    nzEntriesInMatrix = data.getInt();
    

    curCol = 0;
    entry = 0;
    advance();
  }
  

  private void advance()
    throws IOException
  {
    if (entry >= nzEntriesInMatrix) {
      next = null;
    }
    else
    {
      next = new SparseHashDoubleVector(rows);
      int nzInCol = data.getInt();
      for (int i = 0; i < nzInCol; entry += 1) {
        int row = data.getInt();
        double value = data.getFloat();
        next.set(row, value);i++;
      }
      

      curCol += 1;
    }
  }
  


  public boolean hasNext()
  {
    return next != null;
  }
  


  public SparseDoubleVector next()
  {
    if (next == null) {
      throw new NoSuchElementException("No futher entries");
    }
    SparseDoubleVector curCol = next;
    try {
      advance();
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
    return curCol;
  }
  


  public void remove()
  {
    throw new UnsupportedOperationException("Cannot remove from file");
  }
  


  public void reset()
  {
    data.rewind();
    

    data.getInt();
    data.getInt();
    data.getInt();
    

    curCol = 0;
    entry = 0;
    try {
      advance();
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
}
