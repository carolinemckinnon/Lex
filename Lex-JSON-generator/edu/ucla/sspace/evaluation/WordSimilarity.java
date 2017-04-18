package edu.ucla.sspace.evaluation;

public abstract interface WordSimilarity
{
  public abstract String getFirstWord();
  
  public abstract String getSecondWord();
  
  public abstract double getSimilarity();
}
