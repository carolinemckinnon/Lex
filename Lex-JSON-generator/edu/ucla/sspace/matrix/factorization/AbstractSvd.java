package edu.ucla.sspace.matrix.factorization;

import edu.ucla.sspace.matrix.ArrayMatrix;
import edu.ucla.sspace.matrix.DiagonalMatrix;
import edu.ucla.sspace.matrix.Matrix;








































































public abstract class AbstractSvd
  implements SingularValueDecomposition
{
  protected Matrix classFeatures;
  protected Matrix U;
  protected Matrix V;
  protected boolean scaledClassFeatures;
  protected Matrix dataClasses;
  protected boolean scaledDataClasses;
  protected double[] singularValues;
  
  public AbstractSvd() {}
  
  public Matrix dataClasses()
  {
    if (!scaledDataClasses) {
      scaledDataClasses = true;
      dataClasses = new ArrayMatrix(U.rows(), U.columns());
      




      for (int r = 0; r < dataClasses.rows(); r++) {
        for (int c = 0; c < dataClasses.columns(); c++)
          dataClasses.set(r, c, U.get(r, c) * 
            singularValues[c]);
      }
    }
    return dataClasses;
  }
  


  public Matrix classFeatures()
  {
    if (!scaledClassFeatures) {
      scaledClassFeatures = true;
      classFeatures = new ArrayMatrix(V.rows(), V.columns());
      




      for (int r = 0; r < classFeatures.rows(); r++) {
        for (int c = 0; c < classFeatures.columns(); c++)
          classFeatures.set(r, c, V.get(r, c) * 
            singularValues[r]);
      }
    }
    return classFeatures;
  }
  



  public Matrix getLeftVectors()
  {
    if (U == null) {
      throw new IllegalStateException(
        "The matrix has not been factorized yet");
    }
    return U;
  }
  


  public Matrix getRightVectors()
  {
    if (V == null) {
      throw new IllegalStateException(
        "The matrix has not been factorized yet");
    }
    return V;
  }
  


  public Matrix getSingularValues()
  {
    if (singularValues == null) {
      throw new IllegalStateException(
        "The matrix has not been factorized yet");
    }
    return new DiagonalMatrix(singularValues);
  }
  


  public double[] singularValues()
  {
    return singularValues;
  }
}
