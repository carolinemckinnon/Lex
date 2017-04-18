package edu.ucla.sspace.evaluation;

public abstract interface NormedWordPrimingReport
{
  public abstract int numberOfCues();
  
  public abstract int numberOfUnanswerableCues();
  
  public abstract double averageCorrelation();
}
