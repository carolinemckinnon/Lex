package edu.ucla.sspace.evaluation;

public abstract interface WordChoiceReport
{
  public abstract int numberOfQuestions();
  
  public abstract int correctAnswers();
  
  public abstract int unanswerableQuestions();
  
  public abstract double score();
}
