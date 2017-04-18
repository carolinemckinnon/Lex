package edu.ucla.sspace.matrix;

import edu.ucla.sspace.util.SparseArray;
import edu.ucla.sspace.vector.Vector;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.logging.Logger;





































public class ClutoDenseMatrixBuilder
  implements MatrixBuilder
{
  private static final Logger LOGGER = Logger.getLogger(ClutoSparseMatrixBuilder.class.getName());
  




  private final File matrixFile;
  




  private final PrintWriter writer;
  




  private boolean isFinished;
  



  private int curRow;
  



  private int numCols;
  




  public ClutoDenseMatrixBuilder()
  {
    this(getTempMatrixFile());
  }
  



  public ClutoDenseMatrixBuilder(File backingFile)
  {
    matrixFile = backingFile;
    curRow = 0;
    numCols = 0;
    isFinished = false;
    try
    {
      writer = new PrintWriter(backingFile);
      char[] whiteSpace = new char[100];
      Arrays.fill(whiteSpace, ' ');
      writer.println(whiteSpace);
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  




  private static File getTempMatrixFile()
  {
    File tmp = null;
    try {
      tmp = File.createTempFile("cluto-dense-matrix", ".dat");
      tmp.deleteOnExit();
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
    return tmp;
  }
  


  public synchronized int addColumn(double[] row)
  {
    if (isFinished) {
      throw new IllegalStateException(
        "Cannot add columns to a MatrixBuilder that is finished");
    }
    
    if (row.length > numCols) {
      numCols = row.length;
    }
    
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < row.length; i++)
      sb.append(row[i]).append(" ");
    writer.println(sb.toString());
    
    return ++curRow;
  }
  


  public synchronized int addColumn(SparseArray<? extends Number> row)
  {
    if (isFinished) {
      throw new IllegalStateException(
        "Cannot add columns to a MatrixBuilder that is finished");
    }
    
    if (row.length() > numCols) {
      numCols = row.length();
    }
    






    assert (row.length() != Integer.MAX_VALUE) : "adding a column whose length is Integer.MAX_VALUE (was likley left unspecified in the  constructor).";
    


    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < row.length(); i++)
      sb.append(((Number)row.get(i)).floatValue()).append(" ");
    writer.println(sb.toString());
    return ++curRow;
  }
  


  public synchronized int addColumn(Vector row)
  {
    if (isFinished) {
      throw new IllegalStateException(
        "Cannot add columns to a MatrixBuilder that is finished");
    }
    
    if (row.length() > numCols) {
      numCols = row.length();
    }
    






    assert (row.length() != Integer.MAX_VALUE) : "adding a column whose length is Integer.MAX_VALUE (was likley left unspecified in the  constructor).";
    


    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < row.length(); i++)
      sb.append(row.getValue(i).doubleValue()).append(" ");
    writer.println(sb.toString());
    return ++curRow;
  }
  



  public synchronized void finish()
  {
    if (!isFinished) {
      isFinished = true;
      try {
        writer.flush();
        writer.close();
        



        RandomAccessFile matrixRaf = 
          new RandomAccessFile(matrixFile, "rw");
        


        StringBuilder sb = new StringBuilder();
        sb.append(curRow).append(" ");
        sb.append(numCols).append(" ");
        matrixRaf.write(sb.toString().getBytes());
      } catch (IOException ioe) {
        throw new IOError(ioe);
      }
    }
  }
  


  public synchronized File getFile()
  {
    if (!isFinished)
      throw new IllegalStateException(
        "Cannot access matrix file until finished has been called");
    return matrixFile;
  }
  


  public MatrixIO.Format getMatrixFormat()
  {
    return MatrixIO.Format.CLUTO_DENSE;
  }
  


  public MatrixFile getMatrixFile()
  {
    return new MatrixFile(getFile(), getMatrixFormat());
  }
  


  public synchronized boolean isFinished()
  {
    return isFinished;
  }
}
