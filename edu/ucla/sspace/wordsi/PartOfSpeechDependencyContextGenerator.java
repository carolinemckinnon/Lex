package edu.ucla.sspace.wordsi;

import edu.ucla.sspace.basis.BasisMapping;
import edu.ucla.sspace.dependency.DependencyTreeNode;


































public class PartOfSpeechDependencyContextGenerator
  extends AbstractOccurrenceDependencyContextGenerator
{
  public PartOfSpeechDependencyContextGenerator(BasisMapping<String, String> basis, int windowSize)
  {
    super(basis, windowSize);
  }
  



  protected String getFeature(DependencyTreeNode node, int index)
  {
    return node.word() + "-" + node.pos();
  }
}
