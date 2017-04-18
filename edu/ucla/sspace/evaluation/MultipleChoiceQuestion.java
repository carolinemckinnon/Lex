package edu.ucla.sspace.evaluation;

import java.util.List;

public abstract interface MultipleChoiceQuestion
{
  public abstract String getPrompt();
  
  public abstract List<String> getOptions();
  
  public abstract int getCorrectAnswer();
}
