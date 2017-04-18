package edu.ucla.sspace.matrix.factorization;

import edu.ucla.sspace.matrix.MatlabSparseMatrixBuilder;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.MatrixBuilder;
import edu.ucla.sspace.matrix.MatrixFile;
import edu.ucla.sspace.matrix.SVD;
import edu.ucla.sspace.matrix.SVD.Algorithm;
import edu.ucla.sspace.matrix.SparseMatrix;
import java.util.logging.Logger;



































public class SingularValueDecompositionJAMA
  extends AbstractSvd
{
  private static final Logger LOG = Logger.getLogger(SingularValueDecompositionJAMA.class.getName());
  
  public SingularValueDecompositionJAMA() {}
  
  public void factorize(SparseMatrix matrix, int dimensions)
  {
    Matrix[] svd = SVD.svd(matrix, SVD.Algorithm.JAMA, dimensions);
    
    dataClasses = svd[0];
    U = dataClasses;
    scaledDataClasses = false;
    
    classFeatures = svd[2];
    V = classFeatures;
    scaledClassFeatures = false;
    
    singularValues = new double[dimensions];
    for (int k = 0; k < dimensions; k++) {
      singularValues[k] = svd[1].get(k, k);
    }
  }
  

  public void factorize(MatrixFile mFile, int dimensions)
  {
    Matrix[] svd = SVD.svd(mFile.getFile(), SVD.Algorithm.JAMA, 
      mFile.getFormat(), dimensions);
    
    dataClasses = svd[0];
    U = dataClasses;
    scaledDataClasses = false;
    
    classFeatures = svd[2];
    V = classFeatures;
    scaledClassFeatures = false;
    
    singularValues = new double[dimensions];
    for (int k = 0; k < dimensions; k++) {
      singularValues[k] = svd[1].get(k, k);
    }
  }
  

  public MatrixBuilder getBuilder()
  {
    return new MatlabSparseMatrixBuilder();
  }
}
