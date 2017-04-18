package edu.ucla.sspace.matrix.factorization;

import edu.ucla.sspace.matrix.MatlabSparseMatrixBuilder;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.Matrix.Type;
import edu.ucla.sspace.matrix.MatrixBuilder;
import edu.ucla.sspace.matrix.MatrixFile;
import edu.ucla.sspace.matrix.MatrixIO;
import edu.ucla.sspace.matrix.MatrixIO.Format;
import edu.ucla.sspace.matrix.SparseMatrix;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;


































public class SingularValueDecompositionOctave
  extends AbstractSvd
  implements SingularValueDecomposition, Serializable
{
  private static final long serialVersionUID = 1L;
  private static final Logger LOG = Logger.getLogger(SingularValueDecompositionOctave.class.getName());
  
  public SingularValueDecompositionOctave() {}
  
  public void factorize(SparseMatrix matrix, int dimensions) { try { File mFile = File.createTempFile("octave-input", ".dat");
      mFile.deleteOnExit();
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
      
      File octaveFile = File.createTempFile("octave-svds", ".m");
      File uOutput = File.createTempFile("octave-svds-U", ".dat");
      File sOutput = File.createTempFile("octave-svds-S", ".dat");
      File vOutput = File.createTempFile("octave-svds-V", ".dat");
      octaveFile.deleteOnExit();
      uOutput.deleteOnExit();
      sOutput.deleteOnExit();
      vOutput.deleteOnExit();
      

      PrintWriter pw = new PrintWriter(octaveFile);
      pw.printf(
        "Z = load('%s','-ascii');\nA = spconvert(Z);\nclear Z;\n[U, S, V] = svds(A, %d);\nsave(\"-ascii\", \"%s\", \"U\");\nsave(\"-ascii\", \"%s\", \"S\");\nsave(\"-ascii\", \"%s\", \"V\");\nfprintf('Octave Finished\\n');\n", new Object[] {
        






        matrix.getAbsolutePath(), Integer.valueOf(dimensions), uOutput.getAbsolutePath(), 
        sOutput.getAbsolutePath(), vOutput.getAbsolutePath() });
      pw.close();
      


      String commandLine = "octave " + octaveFile.getAbsolutePath();
      LOG.fine(commandLine);
      Process octave = Runtime.getRuntime().exec(commandLine);
      
      BufferedReader br = new BufferedReader(
        new InputStreamReader(octave.getInputStream()));
      BufferedReader stderr = new BufferedReader(
        new InputStreamReader(octave.getErrorStream()));
      

      StringBuilder output = new StringBuilder("Octave svds output:\n");
      for (String line = null; (line = br.readLine()) != null;) {
        output.append(line).append("\n");
      }
      LOG.fine(output.toString());
      
      int exitStatus = octave.waitFor();
      LOG.fine("Octave svds exit status: " + exitStatus);
      

      if (exitStatus == 0)
      {


        U = MatrixIO.readMatrix(uOutput, MatrixIO.Format.DENSE_TEXT, 
          Matrix.Type.DENSE_IN_MEMORY);
        scaledClassFeatures = false;
        

        Matrix S = MatrixIO.readMatrix(sOutput, MatrixIO.Format.DENSE_TEXT, 
          Matrix.Type.SPARSE_ON_DISK);
        singularValues = new double[dimensions];
        for (int s = 0; s < dimensions; s++) {
          singularValues[s] = S.get(s, s);
        }
        
        V = MatrixIO.readMatrix(vOutput, MatrixIO.Format.DENSE_TEXT, 
          Matrix.Type.DENSE_ON_DISK, true);
        scaledDataClasses = false;
      }
      else {
        StringBuilder sb = new StringBuilder();
        for (String line = null; (line = stderr.readLine()) != null;) {
          sb.append(line).append("\n");
        }
        
        LOG.warning("Octave exited with error status.  stderr:\n" + 
          sb.toString());
      }
    } catch (IOException ioe) {
      LOG.log(Level.SEVERE, "Octave svds", ioe);
    } catch (InterruptedException ie) {
      LOG.log(Level.SEVERE, "Octave svds", ie);
    }
  }
  


  public MatrixBuilder getBuilder()
  {
    return new MatlabSparseMatrixBuilder();
  }
}
