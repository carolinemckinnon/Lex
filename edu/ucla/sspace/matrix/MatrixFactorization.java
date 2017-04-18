package edu.ucla.sspace.matrix;

public abstract interface MatrixFactorization
{
  public abstract MatrixBuilder getBuilder();
  
  public abstract void factorize(SparseMatrix paramSparseMatrix, int paramInt);
  
  public abstract void factorize(MatrixFile paramMatrixFile, int paramInt);
  
  public abstract Matrix dataClasses();
  
  public abstract Matrix classFeatures();
}
