package edu.ucla.sspace.matrix;

import edu.ucla.sspace.util.Duple;
import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseVector;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.DoubleBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;






































































public class OnDiskMatrix
  implements Matrix
{
  private static final int BYTES_PER_DOUBLE = 8;
  private static final int MAX_ELEMENTS_PER_REGION = 268435455;
  private final DoubleBuffer[] matrixRegions;
  private final File[] backingFiles;
  private final int rows;
  private final int cols;
  
  public OnDiskMatrix(int rows, int cols)
  {
    if ((rows <= 0) || (cols <= 0)) {
      throw new IllegalArgumentException("dimensions must be positive");
    }
    this.rows = rows;
    this.cols = cols;
    





    int numRegions = 
      (int)(rows * cols / 268435455L) + 1;
    matrixRegions = new DoubleBuffer[numRegions];
    backingFiles = new File[numRegions];
    for (int region = 0; region < numRegions; region++) {
      int sizeInBytes = region + 1 == numRegions ? 
        (int)(rows * cols % 
        268435455L * 8L) : 
        2147483640;
      Duple<DoubleBuffer, File> d = createTempBuffer(sizeInBytes);
      matrixRegions[region] = ((DoubleBuffer)x);
      backingFiles[region] = ((File)y);
    }
  }
  


  private static Duple<DoubleBuffer, File> createTempBuffer(int size)
  {
    try
    {
      File f = File.createTempFile("OnDiskMatrix", ".matrix");
      

      f.deleteOnExit();
      RandomAccessFile raf = new RandomAccessFile(f, "rw");
      FileChannel fc = raf.getChannel();
      DoubleBuffer contextBuffer = 
        fc.map(FileChannel.MapMode.READ_WRITE, 0L, size).asDoubleBuffer();
      fc.close();
      return new Duple(contextBuffer, f);
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  



  private void checkIndices(int row, int col)
  {
    if ((row < 0) || (row >= rows))
      throw new ArrayIndexOutOfBoundsException("row: " + row);
    if ((col < 0) || (col >= cols)) {
      throw new ArrayIndexOutOfBoundsException("column: " + col);
    }
  }
  

  public double get(int row, int col)
  {
    int region = getMatrixRegion(row, col);
    int regionOffset = getRegionOffset(row, col);
    return matrixRegions[region].get(regionOffset);
  }
  


  public double[] getColumn(int column)
  {
    double[] values = new double[rows];
    for (int row = 0; row < rows; row++)
      values[row] = get(row, column);
    return values;
  }
  


  public DoubleVector getColumnVector(int column)
  {
    return new DenseVector(getColumn(column));
  }
  


  public double[] getRow(int row)
  {
    int rowStartRegion = getMatrixRegion(row, 0L);
    int rowEndRegion = getMatrixRegion(row + 1, 0L);
    double[] rowVal = new double[cols];
    if (rowStartRegion == rowEndRegion) {
      int rowStartIndex = getRegionOffset(row, 0L);
      DoubleBuffer region = matrixRegions[rowStartRegion];
      for (int col = 0; col < cols; col++) {
        rowVal[col] = region.get(col + rowStartIndex);
      }
    } else {
      DoubleBuffer firstRegion = matrixRegions[rowStartRegion];
      DoubleBuffer secondRegion = matrixRegions[rowEndRegion];
      int rowStartIndex = getRegionOffset(row, 0L);
      for (int rowOffset = 0; 
          rowStartIndex + rowOffset < 268435455; 
          rowOffset++) {
        rowVal[rowOffset] = firstRegion.get(rowOffset + rowStartIndex);
      }
      
      for (int i = 0; rowOffset < rowVal.length; rowOffset++) {
        rowVal[rowOffset] = secondRegion.get(i);i++;
      } }
    return rowVal;
  }
  


  public DoubleVector getRowVector(int row)
  {
    return new DenseVector(getRow(row));
  }
  


  public int columns()
  {
    return cols;
  }
  
  private int getMatrixRegion(long row, long col) {
    long element = row * cols + col;
    return (int)(element / 268435455L);
  }
  
  private int getRegionOffset(long row, long col) {
    long element = row * cols + col;
    return (int)(element % 268435455L);
  }
  


  public void set(int row, int col, double val)
  {
    int region = getMatrixRegion(row, col);
    int regionOffset = getRegionOffset(row, col);
    matrixRegions[region].put(regionOffset, val);
  }
  


  public void setColumn(int column, double[] values)
  {
    for (int row = 0; row < rows; row++) {
      set(row, column, values[row]);
    }
  }
  

  public void setColumn(int column, DoubleVector values)
  {
    for (int row = 0; row < rows; row++) {
      set(row, column, values.get(row));
    }
  }
  

  public void setRow(int row, double[] vals)
  {
    if (vals.length != cols)
      throw new IllegalArgumentException(
        "The number of values does not match the number of columns");
    for (int i = 0; i < vals.length; i++) {
      set(row, i, vals[i]);
    }
  }
  

  public void setRow(int row, DoubleVector values)
  {
    if (values.length() != cols) {
      throw new IllegalArgumentException(
        "The number of values does not match the number of columns");
    }
    if ((values instanceof SparseVector)) {
      SparseVector sv = (SparseVector)values;
      for (int i : sv.getNonZeroIndices()) {
        set(row, i, values.get(i));
      }
    } else {
      for (int i = 0; i < values.length(); i++) {
        set(row, i, values.get(i));
      }
    }
  }
  

  public double[][] toDenseArray()
  {
    if (matrixRegions.length > 1)
      throw new UnsupportedOperationException(
        "matrix is too large to fit into memory");
    double[][] m = new double[rows][cols];
    DoubleBuffer b = matrixRegions[0];
    b.rewind();
    for (int row = 0; row < rows; row++)
      b.get(m[row]);
    return m;
  }
  


  public int rows()
  {
    return rows;
  }
  





  protected void finalize()
  {
    for (File f : backingFiles) {
      try {
        f.delete();
      }
      catch (Throwable localThrowable) {}
    }
  }
}
