package edu.ucla.sspace.evaluation;

import edu.ucla.sspace.common.SemanticSpace;

public abstract interface WordPrimingTest
{
  public abstract WordPrimingReport evaluate(SemanticSpace paramSemanticSpace);
}
