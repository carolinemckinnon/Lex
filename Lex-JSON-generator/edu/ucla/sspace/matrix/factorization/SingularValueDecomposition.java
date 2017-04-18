package edu.ucla.sspace.matrix.factorization;

import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.MatrixFactorization;

public abstract interface SingularValueDecomposition
  extends MatrixFactorization
{
  public abstract Matrix getLeftVectors();
  
  public abstract Matrix getRightVectors();
  
  public abstract Matrix getSingularValues();
}
