package edu.ucla.sspace.matrix.factorization;

import edu.ucla.sspace.matrix.Matrix.Type;
import edu.ucla.sspace.matrix.MatrixBuilder;
import edu.ucla.sspace.matrix.MatrixFile;
import edu.ucla.sspace.matrix.MatrixIO;
import edu.ucla.sspace.matrix.MatrixIO.Format;
import edu.ucla.sspace.matrix.SparseMatrix;
import edu.ucla.sspace.matrix.SvdlibcSparseBinaryMatrixBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;



































public class SingularValueDecompositionLibC
  extends AbstractSvd
  implements SingularValueDecomposition, Serializable
{
  private static final long serialVersionUID = 1L;
  private static final Logger LOG = Logger.getLogger(SingularValueDecompositionLibC.class.getName());
  
  public SingularValueDecompositionLibC() {}
  
  public void factorize(SparseMatrix matrix, int dimensions)
  {
    try {
      File temp = File.createTempFile("svdlibc.svd.matrix", "dat");
      temp.deleteOnExit();
      MatrixIO.writeMatrix(matrix, temp, MatrixIO.Format.SVDLIBC_SPARSE_TEXT);
      MatrixFile mFile = new MatrixFile(temp, MatrixIO.Format.SVDLIBC_SPARSE_TEXT);
      factorize(mFile, dimensions);
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  






  public void factorize(MatrixFile mFile, int dimensions)
  {
    if (!isSVDLIBCavailable()) {
      throw new IllegalStateException(
        "Use of this class requires the SVDLIBC command line program, which is either not installed on this system or is not available to be executed from the command line.  Check that your PATH settings are correct or see http://tedlab.mit.edu/~dr/SVDLIBC/ to download and install the program.");
    }
    



    try
    {
      String formatString = "";
      switch (mFile.getFormat()) {
      case SVDLIBC_DENSE_TEXT: 
        formatString = " -r db ";
        break;
      case MATLAB_SPARSE: 
        formatString = " -r dt ";
        break;
      case SVDLIBC_DENSE_BINARY: 
        formatString = " -r sb ";
        break;
      case DENSE_TEXT: 
        break;
      
      default: 
        throw new UnsupportedOperationException(
          "Format type is not accepted");
      }
      
      File outputMatrixFile = File.createTempFile("svdlibc", ".dat");
      outputMatrixFile.deleteOnExit();
      String outputMatrixPrefix = outputMatrixFile.getAbsolutePath();
      
      LOG.fine("creating SVDLIBC factor matrices at: " + 
        outputMatrixPrefix);
      String commandLine = "svd -o " + outputMatrixPrefix + formatString + 
        " -w dt " + 
        " -d " + dimensions + " " + mFile.getFile().getAbsolutePath();
      LOG.fine(commandLine);
      Process svdlibc = Runtime.getRuntime().exec(commandLine);
      
      BufferedReader stdout = new BufferedReader(
        new InputStreamReader(svdlibc.getInputStream()));
      BufferedReader stderr = new BufferedReader(
        new InputStreamReader(svdlibc.getErrorStream()));
      
      StringBuilder output = new StringBuilder("SVDLIBC output:\n");
      for (String line = null; (line = stderr.readLine()) != null;) {
        output.append(line).append("\n");
      }
      LOG.fine(output.toString());
      
      int exitStatus = svdlibc.waitFor();
      LOG.fine("svdlibc exit status: " + exitStatus);
      

      if (exitStatus == 0) {
        File Ut = new File(outputMatrixPrefix + "-Ut");
        File S = new File(outputMatrixPrefix + "-S");
        File Vt = new File(outputMatrixPrefix + "-Vt");
        




        U = MatrixIO.readMatrix(
          Ut, MatrixIO.Format.SVDLIBC_DENSE_TEXT, 
          Matrix.Type.DENSE_IN_MEMORY, true);
        scaledDataClasses = false;
        
        V = MatrixIO.readMatrix(
          Vt, MatrixIO.Format.SVDLIBC_DENSE_TEXT, 
          Matrix.Type.DENSE_IN_MEMORY);
        scaledClassFeatures = false;
        



        singularValues = readSVDLIBCsingularVector(S, dimensions);
      } else {
        StringBuilder sb = new StringBuilder();
        for (String line = null; (line = stderr.readLine()) != null;) {
          sb.append(line).append("\n");
        }
        LOG.warning("svdlibc exited with error status.  stderr:\n" + 
          sb.toString());
      }
    } catch (IOException ioe) {
      LOG.log(Level.SEVERE, "SVDLIBC", ioe);
    } catch (InterruptedException ie) {
      LOG.log(Level.SEVERE, "SVDLIBC", ie);
    }
  }
  


  public MatrixBuilder getBuilder()
  {
    return new SvdlibcSparseBinaryMatrixBuilder();
  }
  




  private static double[] readSVDLIBCsingularVector(File sigmaMatrixFile, int dimensions)
    throws IOException
  {
    BufferedReader br = new BufferedReader(new FileReader(sigmaMatrixFile));
    double[] m = new double[dimensions];
    


    int readDimensions = Integer.parseInt(br.readLine());
    if (readDimensions != dimensions) {
      throw new RuntimeException(
        "SVDLIBC generated the incorrect number of dimensions: " + 
        readDimensions + " versus " + dimensions);
    }
    
    int i = 0;
    for (String line = null; (line = br.readLine()) != null;)
      m[(i++)] = Double.parseDouble(line);
    return m;
  }
  

  public static boolean isSVDLIBCavailable()
  {
    try
    {
      Process svdlibc = Runtime.getRuntime().exec("svd");
      BufferedReader br = new BufferedReader(
        new InputStreamReader(svdlibc.getInputStream()));
      


      String line = null; while ((line = br.readLine()) != null) {}
      
      br.close();
      
      svdlibc.waitFor();
    } catch (Exception e) {
      return false;
    }
    return true;
  }
}
