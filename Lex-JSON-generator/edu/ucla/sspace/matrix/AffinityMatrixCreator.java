package edu.ucla.sspace.matrix;

import edu.ucla.sspace.similarity.SimilarityFunction;

public abstract interface AffinityMatrixCreator
{
  public abstract void setParams(double... paramVarArgs);
  
  public abstract void setFunctions(SimilarityFunction paramSimilarityFunction1, SimilarityFunction paramSimilarityFunction2);
  
  public abstract MatrixFile calculate(Matrix paramMatrix);
  
  public abstract MatrixFile calculate(MatrixFile paramMatrixFile);
  
  public abstract MatrixFile calculate(MatrixFile paramMatrixFile, boolean paramBoolean);
}
