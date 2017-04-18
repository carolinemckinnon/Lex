package edu.ucla.sspace.evaluation;

import java.util.Collection;

public abstract interface WordSimilarityEvaluation
{
  public abstract Collection<WordSimilarity> getPairs();
  
  public abstract double getMostSimilarValue();
  
  public abstract double getLeastSimilarValue();
}
