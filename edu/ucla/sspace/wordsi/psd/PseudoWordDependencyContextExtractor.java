package edu.ucla.sspace.wordsi.psd;

import edu.ucla.sspace.dependency.DependencyExtractor;
import edu.ucla.sspace.dependency.DependencyTreeNode;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.wordsi.DependencyContextExtractor;
import edu.ucla.sspace.wordsi.DependencyContextGenerator;
import edu.ucla.sspace.wordsi.Wordsi;
import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.util.Map;






















































public class PseudoWordDependencyContextExtractor
  extends DependencyContextExtractor
{
  private Map<String, String> pseudoWordMap;
  
  public PseudoWordDependencyContextExtractor(DependencyExtractor extractor, DependencyContextGenerator generator, Map<String, String> pseudoWordMap)
  {
    super(extractor, generator, true);
    this.pseudoWordMap = pseudoWordMap;
  }
  




  public void processDocument(BufferedReader document, Wordsi wordsi)
  {
    try
    {
      String contextHeader = handleContextHeader(document);
      String[] contextTokens = contextHeader.split("\\s+");
      int focusIndex = Integer.parseInt(contextTokens[3]);
      

      DependencyTreeNode[] nodes = extractor.readNextTree(document);
      if (nodes.length == 0)
        return;
      DependencyTreeNode focusNode = nodes[focusIndex];
      

      String focusWord = getPrimaryKey(focusNode);
      String secondarykey = (String)pseudoWordMap.get(focusWord);
      

      if (secondarykey == null) {
        return;
      }
      
      if (!acceptWord(focusNode, contextTokens[1], wordsi)) {
        return;
      }
      
      SparseDoubleVector focusMeaning = generator.generateContext(
        nodes, focusIndex);
      wordsi.handleContextVector(secondarykey, focusWord, focusMeaning);
      

      document.close();
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  




  protected boolean acceptWord(DependencyTreeNode focusNode, String contextHeader, Wordsi wordsi)
  {
    return (pseudoWordMap.containsKey(focusNode.word())) && 
      (focusNode.word().equals(contextHeader));
  }
}
