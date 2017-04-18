package edu.ucla.sspace.wordsi;

import edu.ucla.sspace.vector.SparseDoubleVector;

public abstract interface Wordsi
{
  public abstract boolean acceptWord(String paramString);
  
  public abstract void handleContextVector(String paramString1, String paramString2, SparseDoubleVector paramSparseDoubleVector);
}
