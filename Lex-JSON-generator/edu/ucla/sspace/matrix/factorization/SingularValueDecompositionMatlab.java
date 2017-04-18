package edu.ucla.sspace.matrix.factorization;

import edu.ucla.sspace.matrix.MatlabSparseMatrixBuilder;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.Matrix.Type;
import edu.ucla.sspace.matrix.MatrixBuilder;
import edu.ucla.sspace.matrix.MatrixFile;
import edu.ucla.sspace.matrix.MatrixIO;
import edu.ucla.sspace.matrix.MatrixIO.Format;
import edu.ucla.sspace.matrix.SparseMatrix;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabProxyFactoryOptions;
import matlabcontrol.MatlabProxyFactoryOptions.Builder;






























public class SingularValueDecompositionMatlab
  extends AbstractSvd
  implements SingularValueDecomposition, Serializable
{
  private static final long serialVersionUID = 1L;
  private static final Logger LOG = Logger.getLogger(SingularValueDecompositionMatlab.class.getName());
  
  public SingularValueDecompositionMatlab() {}
  
  public void factorize(SparseMatrix matrix, int dimensions) { try { File mFile = File.createTempFile("matlab-input", ".dat");
      MatrixIO.writeMatrix(matrix, mFile, MatrixIO.Format.MATLAB_SPARSE);
      factorize(new MatrixFile(mFile, MatrixIO.Format.MATLAB_SPARSE), dimensions);
    } catch (IOException ioe) {
      LOG.log(Level.SEVERE, "Converting to matlab file", ioe);
    }
  }
  
  public void factorize(MatrixFile mfile, int dimensions) {
    try {
      File matrix;
      File matrix;
      if (mfile.getFormat() == MatrixIO.Format.MATLAB_SPARSE) {
        matrix = mfile.getFile();
      } else {
        matrix = MatrixIO.convertFormat(mfile.getFile(), 
          mfile.getFormat(), 
          MatrixIO.Format.MATLAB_SPARSE);
      }
      
      File uOutput = File.createTempFile("matlab-svds-U", ".dat");
      File sOutput = File.createTempFile("matlab-svds-S", ".dat");
      File vOutput = File.createTempFile("matlab-svds-V", ".dat");
      





      LOG.fine("writing Matlab output to files:\n  " + 
        uOutput + "\n" + 
        "  " + sOutput + "\n" + 
        "  " + vOutput + "\n");
      
      uOutput.deleteOnExit();
      sOutput.deleteOnExit();
      vOutput.deleteOnExit();
      
      MatlabProxyFactoryOptions options = new MatlabProxyFactoryOptions.Builder()
        .setUsePreviouslyControlledSession(true)
        .setHidden(true).build();
      MatlabProxyFactory factory = new MatlabProxyFactory(options);
      MatlabProxy proxy = factory.getProxy();
      proxy.eval("disp('Hello World')");
      proxy.eval("Z = load('" + matrix.getAbsolutePath() + "','-ascii');\n" + 
        "A = spconvert(Z);\n" + 
        "% Remove the raw data file to save space\n" + 
        "clear Z;\n" + 
        "[U, S, V] = svds(A, " + dimensions + " );\n" + 
        "save " + uOutput.getAbsolutePath() + " U -ASCII\n" + 
        "save " + sOutput.getAbsolutePath() + " S -ASCII\n" + 
        "save " + vOutput.getAbsolutePath() + " V -ASCII\n" + 
        "fprintf('Matlab Finished\\n');");
      proxy.exit();
      proxy.disconnect();
      


      U = MatrixIO.readMatrix(uOutput, MatrixIO.Format.DENSE_TEXT, 
        Matrix.Type.DENSE_IN_MEMORY);
      scaledDataClasses = false;
      

      Matrix S = MatrixIO.readMatrix(sOutput, MatrixIO.Format.DENSE_TEXT, 
        Matrix.Type.SPARSE_ON_DISK);
      singularValues = new double[dimensions];
      for (int s = 0; s < dimensions; s++) {
        singularValues[s] = S.get(s, s);
      }
      
      V = MatrixIO.readMatrix(vOutput, MatrixIO.Format.DENSE_TEXT, 
        Matrix.Type.DENSE_ON_DISK, true);
      scaledDataClasses = false;
      
      System.err.println("Matlab Function Completed");
    } catch (IOException ioe) {
      LOG.log(Level.SEVERE, "Matlab svds", ioe);
    } catch (MatlabInvocationException e) {
      System.err.println(e.getMessage());
    } catch (MatlabConnectionException e) {
      System.err.println(e.getMessage());
    }
  }
  


  public MatrixBuilder getBuilder()
  {
    return new MatlabSparseMatrixBuilder();
  }
}
