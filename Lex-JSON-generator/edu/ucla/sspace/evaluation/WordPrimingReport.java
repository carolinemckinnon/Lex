package edu.ucla.sspace.evaluation;

public abstract interface WordPrimingReport
{
  public abstract int numberOfWordPairs();
  
  public abstract double relatedPriming();
  
  public abstract double unrelatedPriming();
  
  public abstract double effect();
}
