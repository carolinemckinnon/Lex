package edu.ucla.sspace.wordsi.semeval;

import edu.ucla.sspace.dependency.DependencyExtractor;
import edu.ucla.sspace.dependency.DependencyTreeNode;
import edu.ucla.sspace.wordsi.DependencyContextExtractor;
import edu.ucla.sspace.wordsi.DependencyContextGenerator;
import edu.ucla.sspace.wordsi.Wordsi;
















































public class SemEvalDependencyContextExtractor
  extends DependencyContextExtractor
{
  public SemEvalDependencyContextExtractor(DependencyExtractor extractor, DependencyContextGenerator generator)
  {
    super(extractor, generator, true);
  }
  




  protected boolean acceptWord(DependencyTreeNode focusNode, String contextHeader, Wordsi wordsi)
  {
    return (wordsi.acceptWord(focusNode.word())) && 
      (focusNode.lemma().equals(contextHeader));
  }
  



  protected String getSecondaryKey(DependencyTreeNode focusNode, String contextHeader)
  {
    return focusNode.lemma();
  }
}
