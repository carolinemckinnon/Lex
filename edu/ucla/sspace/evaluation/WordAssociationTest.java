package edu.ucla.sspace.evaluation;

import edu.ucla.sspace.common.SemanticSpace;

public abstract interface WordAssociationTest
{
  public abstract WordAssociationReport evaluate(SemanticSpace paramSemanticSpace);
}
