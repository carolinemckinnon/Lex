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
import java.io.PrintStream;
import java.io.Serializable;

public class SingularValueDecompositionSaved
  extends AbstractSvd implements SingularValueDecomposition, Serializable
{
  private static final long serialVersionUID = 1L;
  
  public SingularValueDecompositionSaved() {}
  
  public MatrixBuilder getBuilder()
  {
    System.out.println("Matrix Builder");
    return new MatlabSparseMatrixBuilder();
  }
  
  public void factorize(SparseMatrix m, int dimensions)
  {
    System.out.println("Factorizing 1");
  }
  































  public void factorize(int dimensions)
  {
    File uOutput = null;
    File sOutput = null;
    File vOutput = null;
    

    try
    {
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
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
  



  public void factorize(MatrixFile m, int dimensions)
  {
    File uOutput = null;
    File sOutput = null;
    File vOutput = null;
    

    try
    {
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
      System.out.println("U: " + U.columns() + "x" + U.rows() + " S: " + S.columns() + "x" + S.rows() + 
        " V: " + V.columns() + "x" + V.rows());
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
  }
}
