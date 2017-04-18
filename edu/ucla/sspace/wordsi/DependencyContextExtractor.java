package edu.ucla.sspace.wordsi;

import edu.ucla.sspace.dependency.DependencyExtractor;
import edu.ucla.sspace.dependency.DependencyTreeNode;
import edu.ucla.sspace.vector.SparseDoubleVector;
import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;

























































public class DependencyContextExtractor
  implements ContextExtractor
{
  protected final DependencyExtractor extractor;
  protected final DependencyContextGenerator generator;
  protected final boolean readHeader;
  
  public DependencyContextExtractor(DependencyExtractor extractor, DependencyContextGenerator generator)
  {
    this(extractor, generator, false);
  }
  











  public DependencyContextExtractor(DependencyExtractor extractor, DependencyContextGenerator generator, boolean readHeader)
  {
    this.extractor = extractor;
    this.generator = generator;
    this.readHeader = readHeader;
  }
  


  public int getVectorLength()
  {
    return generator.getVectorLength();
  }
  



  public void processDocument(BufferedReader document, Wordsi wordsi)
  {
    try
    {
      String contextHeader = handleContextHeader(document);
      


      DependencyTreeNode[] nodes = extractor.readNextTree(document);
      

      if (nodes.length == 0) {
        return;
      }
      
      for (int wordIndex = 0; wordIndex < nodes.length; wordIndex++) {
        DependencyTreeNode focusNode = nodes[wordIndex];
        





        String focusWord = getPrimaryKey(focusNode);
        String secondarykey = getSecondaryKey(focusNode, contextHeader);
        

        if (acceptWord(focusNode, contextHeader, wordsi))
        {


          SparseDoubleVector focusMeaning = generator.generateContext(
            nodes, wordIndex);
          wordsi.handleContextVector(
            focusWord, secondarykey, focusMeaning);
        } }
      document.close();
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  





  protected boolean acceptWord(DependencyTreeNode focusNode, String contextHeader, Wordsi wordsi)
  {
    return wordsi.acceptWord(focusNode.word());
  }
  



  protected String getPrimaryKey(DependencyTreeNode focusNode)
  {
    return focusNode.word();
  }
  





  protected String getSecondaryKey(DependencyTreeNode focusNode, String contextHeader)
  {
    return contextHeader == null ? focusNode.word() : contextHeader;
  }
  



  protected String handleContextHeader(BufferedReader document)
    throws IOException
  {
    return readHeader ? document.readLine().trim() : null;
  }
}
