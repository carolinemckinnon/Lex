package edu.ucla.sspace.evaluation;

public abstract interface WordSimilarityReport
{
  public abstract int numberOfWordPairs();
  
  public abstract double correlation();
  
  public abstract int unanswerableQuestions();
}
