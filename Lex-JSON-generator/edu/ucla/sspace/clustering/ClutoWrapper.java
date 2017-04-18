package edu.ucla.sspace.clustering;

import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.MatrixIO;
import edu.ucla.sspace.matrix.MatrixIO.Format;
import edu.ucla.sspace.matrix.SparseMatrix;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;






































class ClutoWrapper
{
  private static final Logger LOGGER = Logger.getLogger(ClutoWrapper.class.getName());
  












  private ClutoWrapper() {}
  












  static Assignments cluster(Matrix matrix, String clmethod, String crtFunction, int numClusters)
    throws IOException
  {
    Assignment[] assignments = new Assignment[matrix.rows()];
    File outputFile = File.createTempFile("cluto-output", ".matrix");
    outputFile.deleteOnExit();
    cluster(assignments, matrix, clmethod, crtFunction, 
      numClusters, outputFile);
    extractAssignments(outputFile, assignments);
    return new Assignments(numClusters, assignments, matrix);
  }
  


























  static String cluster(Assignment[] clusterAssignment, Matrix matrix, String clmethod, String crtFun, int numClusters, File outputFile)
    throws IOException
  {
    LOGGER.log(Level.FINE, "clustering {0} data points with {1} features", 
      new Object[] { Integer.valueOf(matrix.rows()), Integer.valueOf(matrix.columns()) });
    File matrixFile = File.createTempFile("cluto-input", ".matrix");
    


    MatrixIO.writeMatrix(matrix, matrixFile, 
      (matrix instanceof SparseMatrix) ? 
      MatrixIO.Format.CLUTO_SPARSE : 
      MatrixIO.Format.CLUTO_DENSE);
    String output = cluster(clusterAssignment, matrixFile, clmethod, crtFun, 
      outputFile, numClusters);
    

    if (!matrixFile.delete())
      matrixFile.deleteOnExit();
    return output;
  }
  




























  public static String cluster(Assignment[] clusterAssignment, File matrixFile, String clmethod, String crtFun, File outputFile, int numClusters)
    throws IOException
  {
    String commandLine = "vcluster -clmethod=" + 
      clmethod + " " + 
      "-clustfile=" + outputFile + " " + 
      "-crfun=" + crtFun + 
      " " + matrixFile + 
      " " + numClusters;
    LOGGER.fine("executing: " + commandLine);
    Process cluto = Runtime.getRuntime().exec(commandLine);
    
    BufferedReader stdout = new BufferedReader(
      new InputStreamReader(cluto.getInputStream()));
    BufferedReader stderr = new BufferedReader(
      new InputStreamReader(cluto.getErrorStream()));
    
    String clutoOutput = null;
    StringBuilder output = new StringBuilder("Cluto output:\n");
    for (String line = null; (line = stdout.readLine()) != null;)
      output.append(line).append("\n");
    clutoOutput = output.toString();
    if (LOGGER.isLoggable(Level.FINE)) {
      System.err.println(clutoOutput);
    }
    int exitStatus = 0;
    try {
      exitStatus = cluto.waitFor();
    } catch (InterruptedException ie) {
      LOGGER.log(Level.SEVERE, "Cluto", ie);
    }
    
    LOGGER.finer("Cluto exit status: " + exitStatus);
    


    if ((exitStatus == 0) && (clusterAssignment != null)) {
      extractAssignments(outputFile, clusterAssignment);
    } else if (exitStatus != 0) {
      StringBuilder sb = new StringBuilder();
      for (String line = null; (line = stderr.readLine()) != null;) {
        sb.append(line).append("\n");
      }
      
      LOGGER.warning("Cluto exited with error status.  " + exitStatus + 
        " stderr:\n" + sb.toString());
      throw new Error("Clustering failed");
    }
    
    stdout.close();
    stderr.close();
    
    return clutoOutput;
  }
  










  static void extractAssignments(File outputFile, Assignment[] clusterAssignment)
    throws IOException
  {
    BufferedReader br = new BufferedReader(new FileReader(outputFile));
    for (int i = 0; i < clusterAssignment.length; i++) {
      int j = Integer.parseInt(br.readLine());
      clusterAssignment[i] = (j < 0 ? 
        new HardAssignment() : 
        new HardAssignment(j));
    }
    br.close();
  }
}
