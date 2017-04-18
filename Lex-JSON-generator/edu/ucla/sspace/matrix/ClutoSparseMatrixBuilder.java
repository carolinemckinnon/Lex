package edu.ucla.sspace.matrix;

import edu.ucla.sspace.util.SparseArray;
import edu.ucla.sspace.vector.SparseVector;
import edu.ucla.sspace.vector.Vector;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.logging.Logger;





































public class ClutoSparseMatrixBuilder
  implements MatrixBuilder
{
  private static final Logger LOGGER = Logger.getLogger(ClutoSparseMatrixBuilder.class.getName());
  




  private final File matrixFile;
  




  private final PrintWriter writer;
  




  private boolean isFinished;
  




  private int curRow;
  




  private int numCols;
  



  private int nonZeroValues;
  




  public ClutoSparseMatrixBuilder()
  {
    this(getTempMatrixFile());
  }
  



  public ClutoSparseMatrixBuilder(File backingFile)
  {
    matrixFile = backingFile;
    curRow = 0;
    numCols = 0;
    nonZeroValues = 0;
    isFinished = false;
    try {
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
      tmp = File.createTempFile("cluto-sparse-matrix", ".dat");
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
    
    int nonZero = 0;
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < row.length; i++) {
      if (row[i] != 0.0D) {
        sb.append(i + 1).append(" ").append(row[i]).append(" ");
        nonZero++;
      }
    }
    writer.println(sb.toString());
    

    nonZeroValues += nonZero;
    
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
    



    int[] nonZero = row.getElementIndices();
    nonZeroValues += nonZero.length;
    StringBuilder sb = new StringBuilder();
    for (int i : nonZero) {
      sb.append(i + 1).append(" ");
      sb.append(((Number)row.get(i)).floatValue()).append(" ");
    }
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
    




    if ((row instanceof SparseVector)) {
      SparseVector s = (SparseVector)row;
      int[] nonZero = s.getNonZeroIndices();
      nonZeroValues += nonZero.length;
      StringBuilder sb = new StringBuilder();
      for (int i : nonZero) {
        sb.append(i + 1).append(" ");
        sb.append(row.getValue(i).doubleValue()).append(" ");
      }
      writer.println(sb.toString());

    }
    else
    {
      int nonZero = 0;
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < row.length(); i++) {
        double d = row.getValue(i).doubleValue();
        if (d != 0.0D) {
          sb.append(i + 1).append(" ").append(d).append(" ");
          nonZero++;
        }
      }
      writer.println(sb.toString());
      

      nonZeroValues += nonZero;
    }
    return ++curRow;
  }
  



  public synchronized void finish()
  {
    if (!isFinished) {
      isFinished = true;
      try {
        writer.close();
        



        RandomAccessFile matrixRaf = 
          new RandomAccessFile(matrixFile, "rw");
        



        StringBuilder sb = new StringBuilder();
        sb.append(curRow).append(" ");
        sb.append(numCols).append(" ");
        sb.append(nonZeroValues).append(" ");
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
    return MatrixIO.Format.CLUTO_SPARSE;
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
