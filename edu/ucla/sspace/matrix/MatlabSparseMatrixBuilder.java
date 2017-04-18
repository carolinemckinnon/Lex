package edu.ucla.sspace.matrix;

import edu.ucla.sspace.util.SparseArray;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseVector;
import edu.ucla.sspace.vector.Vector;
import edu.ucla.sspace.vector.Vectors;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;







































public class MatlabSparseMatrixBuilder
  implements MatrixBuilder
{
  private static final Logger LOGGER = Logger.getLogger(MatlabSparseMatrixBuilder.class.getName());
  




  private final File matrixFile;
  



  private final PrintWriter matrixWriter;
  



  private final boolean transposeData;
  



  private boolean isFinished;
  



  private int curColumn;
  




  public MatlabSparseMatrixBuilder()
  {
    this(getTempMatrixFile(), false);
  }
  







  public MatlabSparseMatrixBuilder(boolean transposeData)
  {
    this(getTempMatrixFile(), transposeData);
  }
  






  public MatlabSparseMatrixBuilder(File backingFile)
  {
    this(backingFile, false);
  }
  








  public MatlabSparseMatrixBuilder(File backingFile, boolean transposeData)
  {
    matrixFile = backingFile;
    this.transposeData = transposeData;
    curColumn = 0;
    isFinished = false;
    try {
      matrixWriter = new PrintWriter(matrixFile);
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  




  private static File getTempMatrixFile()
  {
    File tmp = null;
    try {
      tmp = File.createTempFile("matlab-sparse-matrix", ".dat");
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
    tmp.deleteOnExit();
    return tmp;
  }
  


  public synchronized int addColumn(double[] column)
  {
    if (isFinished)
      throw new IllegalStateException(
        "Cannot add rows to a MatrixBuilder that is finished");
    for (int r = 0; r < column.length; r++) {
      if (column[r] != 0.0D)
      {



        addEntry(r + 1, curColumn + 1, column[r]);
      }
    }
    return ++curColumn;
  }
  


  public synchronized int addColumn(SparseArray<? extends Number> column)
  {
    if (isFinished) {
      throw new IllegalStateException(
        "Cannot add columns to a MatrixBuilder that is finished");
    }
    for (int r : column.getElementIndices())
    {



      addEntry(r + 1, curColumn + 1, ((Number)column.get(r)).doubleValue());
    }
    return ++curColumn;
  }
  


  public synchronized int addColumn(Vector col)
  {
    DoubleVector column = Vectors.asDouble(col);
    if (isFinished)
      throw new IllegalStateException(
        "Cannot add columns to a MatrixBuilder that is finished");
    if ((column instanceof SparseVector)) {
      SparseVector s = (SparseVector)column;
      for (int r : s.getNonZeroIndices())
      {



        addEntry(r + 1, curColumn + 1, column.get(r));
      }
    }
    else {
      for (int r = 0; r < column.length(); r++) {
        double d = column.get(r);
        if (d != 0.0D)
        {

          addEntry(r + 1, curColumn + 1, d);
        }
      }
    }
    return ++curColumn;
  }
  
  private void addEntry(int row, int col, double value) {
    if (transposeData) {
      matrixWriter.println(col + " " + row + " " + value);
    } else {
      matrixWriter.println(row + " " + col + " " + value);
    }
  }
  


  public synchronized void finish()
  {
    if (!isFinished) {
      if (LOGGER.isLoggable(Level.FINE)) {
        LOGGER.fine("Finished writing matrix in MATLAB_SPARSE format with " + 
          curColumn + " columns");
      }
      isFinished = true;
      matrixWriter.close();
    }
  }
  


  public synchronized File getFile()
  {
    if (!isFinished)
      throw new IllegalStateException(
        "Cannot access matrix file until finish has been called");
    return matrixFile;
  }
  


  public MatrixIO.Format getMatrixFormat()
  {
    return MatrixIO.Format.MATLAB_SPARSE;
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
