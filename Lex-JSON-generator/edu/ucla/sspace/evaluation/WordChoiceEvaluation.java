package edu.ucla.sspace.evaluation;

import java.util.Collection;

public abstract interface WordChoiceEvaluation
{
  public abstract Collection<MultipleChoiceQuestion> getQuestions();
}
