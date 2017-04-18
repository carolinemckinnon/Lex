package edu.ucla.sspace.matrix;

import edu.ucla.sspace.util.SparseArray;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseVector;
import edu.ucla.sspace.vector.Vector;
import edu.ucla.sspace.vector.Vectors;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;










































public class SvdlibcSparseBinaryMatrixBuilder
  implements MatrixBuilder
{
  private static final Logger LOGGER = Logger.getLogger(SvdlibcSparseBinaryMatrixBuilder.class.getName());
  





  private transient DataOutputStream matrixDos;
  




  private final boolean transposeData;
  




  private boolean isFinished;
  




  private int curCol;
  




  private File matrixFile;
  




  private int numRows;
  




  private int nonZeroValues;
  




  private File transposedMatrixFile;
  





  public SvdlibcSparseBinaryMatrixBuilder()
  {
    this(getTempMatrixFile(), false);
  }
  







  public SvdlibcSparseBinaryMatrixBuilder(boolean transposeData)
  {
    this(getTempMatrixFile(), transposeData);
  }
  






  public SvdlibcSparseBinaryMatrixBuilder(File backingFile)
  {
    this(backingFile, false);
  }
  









  public SvdlibcSparseBinaryMatrixBuilder(File backingFile, boolean transposeData)
  {
    matrixFile = backingFile;
    this.transposeData = transposeData;
    curCol = 0;
    numRows = 0;
    nonZeroValues = 0;
    isFinished = false;
    try {
      File matrixDataFile = transposeData ? 
        (this.transposedMatrixFile = getTempMatrixFile()) : 
        backingFile;
      

      matrixDos = new DataOutputStream(
        new BufferedOutputStream(new FileOutputStream(matrixDataFile)));
      



      for (int i = 0; i < 3; i++) {
        matrixDos.writeInt(0);
      }
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  




  private static File getTempMatrixFile()
  {
    File tmp = null;
    try {
      tmp = File.createTempFile("svdlibc-sparse-binary-matrix", ".dat");
      tmp.deleteOnExit();
    }
    catch (IOException ioe) {
      throw new IOError(ioe);
    }
    return tmp;
  }
  


  public synchronized int addColumn(double[] column)
  {
    if (isFinished) {
      throw new IllegalStateException(
        "Cannot add columns to a MatrixBuilder that is finished");
    }
    
    if (column.length > numRows) {
      numRows = column.length;
    }
    
    int nonZero = 0;
    for (int i = 0; i < column.length; i++) {
      if (column[i] != 0.0D) {
        nonZero++;
      }
    }
    
    nonZeroValues += nonZero;
    
    try
    {
      matrixDos.writeInt(nonZero);
      for (int i = 0; i < column.length; i++) {
        if (column[i] != 0.0D) {
          matrixDos.writeInt(i);
          matrixDos.writeFloat((float)column[i]);
        }
      }
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
    
    return ++curCol;
  }
  


  public synchronized int addColumn(SparseArray<? extends Number> column)
  {
    if (isFinished) {
      throw new IllegalStateException(
        "Cannot add columns to a MatrixBuilder that is finished");
    }
    if (column.length() > numRows) {
      numRows = column.length();
    }
    






    assert (column.length() != Integer.MAX_VALUE) : "adding a column whose length is Integer.MAX_VALUE (was likley left unspecified in the  constructor).";
    


    int[] nonZero = column.getElementIndices();
    nonZeroValues += nonZero.length;
    try {
      matrixDos.writeInt(nonZero.length);
      for (int i : nonZero) {
        matrixDos.writeInt(i);
        matrixDos.writeFloat(((Number)column.get(i)).floatValue());
      }
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
    return ++curCol;
  }
  


  public synchronized int addColumn(Vector col)
  {
    if (isFinished) {
      throw new IllegalStateException(
        "Cannot add columns to a MatrixBuilder that is finished");
    }
    DoubleVector column = Vectors.asDouble(col);
    
    if (column.length() > numRows) {
      numRows = column.length();
    }
    






    assert (column.length() != Integer.MAX_VALUE) : "adding a column whose length is Integer.MAX_VALUE (was likley left unspecified in the  constructor).";
    




    if ((column instanceof SparseVector)) {
      SparseVector s = (SparseVector)column;
      int[] nonZero = s.getNonZeroIndices();
      nonZeroValues += nonZero.length;
      System.out.println(nonZero.length);
      try {
        matrixDos.writeInt(nonZero.length);
        for (int i : nonZero) {
          double val = column.get(i);
          matrixDos.writeInt(i);
          matrixDos.writeFloat((float)val);
        }
      } catch (IOException ioe) {
        throw new IOError(ioe);
      }
      
    }
    else
    {
      int nonZero = 0;
      for (int i = 0; i < column.length(); i++) {
        double d = column.get(i);
        if (d != 0.0D) {
          nonZero++;
        }
      }
      
      nonZeroValues += nonZero;
      try {
        matrixDos.writeInt(nonZero);
        
        for (int i = 0; i < column.length(); i++) {
          double value = column.get(i);
          if (value != 0.0D) {
            matrixDos.writeInt(i);
            matrixDos.writeFloat((float)value);
          }
        }
      } catch (IOException ioe) {
        throw new IOError(ioe);
      }
    }
    return ++curCol;
  }
  



  public synchronized void finish()
  {
    if (!isFinished) {
      isFinished = true;
      try {
        matrixDos.close();
        



        File dataFile = transposeData ? 
          transposedMatrixFile : 
          matrixFile;
        RandomAccessFile matrixRaf = 
          new RandomAccessFile(dataFile, "rw");
        


        matrixRaf.writeInt(numRows);
        matrixRaf.writeInt(curCol);
        matrixRaf.writeInt(nonZeroValues);
        matrixRaf.close();
      }
      catch (IOException ioe) {
        throw new IOError(ioe);
      }
      




      if (transposeData) {
        boolean svdlibcFailed = false;
        try {
          String commandLine = "svd  -r sb  -w sb -t -c " + 
            transposedMatrixFile + " " + matrixFile;
          Process svdlibc = Runtime.getRuntime().exec(commandLine);
          LOGGER.fine("transposing svdlibc sparse matrix: " + commandLine);
          BufferedReader stdout = new BufferedReader(
            new InputStreamReader(svdlibc.getInputStream()));
          BufferedReader stderr = new BufferedReader(
            new InputStreamReader(svdlibc.getErrorStream()));
          
          StringBuilder output = new StringBuilder("SVDLIBC output:\n");
          for (String line = null; (line = stderr.readLine()) != null;) {
            output.append(line).append("\n");
          }
          LOGGER.fine(output.toString());
          
          int exitStatus = svdlibc.waitFor();
          LOGGER.fine("svdlibc exit status: " + exitStatus);
          if (exitStatus != 0) {
            StringBuilder sb = new StringBuilder();
            for (String line = null; (line = stderr.readLine()) != null;) {
              sb.append(line).append("\n");
            }
            
            LOGGER.warning("svdlibc exited with error status.  stderr:\n" + 
              sb.toString());
            svdlibcFailed = true;
          }
        }
        catch (Exception e) {
          svdlibcFailed = true;
          LOGGER.log(Level.WARNING, 
            "an exception occurred when trying to transpose with svdlibc; retrying with Java code", 
            e);
        }
        

        if (svdlibcFailed) {
          LOGGER.fine("retrying failed svdlibc transpose with MatrixIO transpose");
          try
          {
            matrixFile = MatrixIO.convertFormat(
              transposedMatrixFile, MatrixIO.Format.SVDLIBC_SPARSE_BINARY, 
              MatrixIO.Format.SVDLIBC_SPARSE_BINARY, true);
          } catch (IOException ioe) {
            throw new IOError(ioe);
          }
        }
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
    return MatrixIO.Format.SVDLIBC_SPARSE_BINARY;
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
