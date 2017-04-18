package edu.ucla.sspace.matrix;

public abstract interface MatrixEntry
{
  public abstract int column();
  
  public abstract int row();
  
  public abstract double value();
}
