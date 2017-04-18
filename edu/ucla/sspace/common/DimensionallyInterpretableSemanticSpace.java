package edu.ucla.sspace.common;

public abstract interface DimensionallyInterpretableSemanticSpace<T>
  extends SemanticSpace
{
  public abstract T getDimensionDescription(int paramInt);
}
