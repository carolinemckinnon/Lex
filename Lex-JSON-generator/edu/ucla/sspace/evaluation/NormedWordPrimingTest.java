package edu.ucla.sspace.evaluation;

import edu.ucla.sspace.common.SemanticSpace;

public abstract interface NormedWordPrimingTest
{
  public abstract NormedWordPrimingReport evaluate(SemanticSpace paramSemanticSpace);
}
