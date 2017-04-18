package edu.ucla.sspace.mains;

import edu.ucla.sspace.common.SemanticSpaceIO.SSpaceFormat;
import edu.ucla.sspace.wordsi.ContextExtractor;
import edu.ucla.sspace.wordsi.TopicModelContextExtractor;
































public class TopicWordsiMain
  extends GenericWordsiMain
{
  public TopicWordsiMain() {}
  
  protected ContextExtractor getExtractor()
  {
    return new TopicModelContextExtractor();
  }
  


  protected SemanticSpaceIO.SSpaceFormat getSpaceFormat()
  {
    return SemanticSpaceIO.SSpaceFormat.SPARSE_BINARY;
  }
  
  public static void main(String[] args) throws Exception {
    TopicWordsiMain main = new TopicWordsiMain();
    main.run(args);
  }
}
