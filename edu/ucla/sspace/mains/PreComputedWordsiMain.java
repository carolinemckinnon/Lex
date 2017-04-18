package edu.ucla.sspace.mains;

import edu.ucla.sspace.basis.BasisMapping;
import edu.ucla.sspace.basis.StringBasisMapping;
import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpaceIO.SSpaceFormat;
import edu.ucla.sspace.wordsi.ContextExtractor;
import edu.ucla.sspace.wordsi.PreComputedContextExtractor;























public class PreComputedWordsiMain
  extends GenericWordsiMain
{
  private BasisMapping<String, String> basis;
  
  public PreComputedWordsiMain() {}
  
  protected void handleExtraOptions()
  {
    if (argOptions.hasOption('L')) {
      basis = ((BasisMapping)loadObject(openLoadFile()));
    } else {
      basis = new StringBasisMapping();
    }
  }
  

  protected void postProcessing()
  {
    if (argOptions.hasOption('S')) {
      saveObject(openSaveFile(), basis);
    }
  }
  

  protected ContextExtractor getExtractor()
  {
    return new PreComputedContextExtractor(basis);
  }
  


  protected SemanticSpaceIO.SSpaceFormat getSpaceFormat()
  {
    return SemanticSpaceIO.SSpaceFormat.SPARSE_BINARY;
  }
  
  public static void main(String[] args) throws Exception {
    PreComputedWordsiMain main = new PreComputedWordsiMain();
    main.run(args);
  }
}
