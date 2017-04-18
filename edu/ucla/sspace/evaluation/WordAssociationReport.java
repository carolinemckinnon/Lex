package edu.ucla.sspace.evaluation;

public abstract interface WordAssociationReport
{
  public abstract int numberOfWordPairs();
  
  public abstract double correlation();
  
  public abstract int unanswerableQuestions();
}
