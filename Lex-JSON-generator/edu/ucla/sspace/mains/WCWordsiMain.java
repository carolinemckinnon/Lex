package edu.ucla.sspace.mains;

import edu.ucla.sspace.basis.BasisMapping;
import edu.ucla.sspace.basis.StringBasisMapping;
import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpaceIO.SSpaceFormat;
import edu.ucla.sspace.hal.LinearWeighting;
import edu.ucla.sspace.hal.WeightingFunction;
import edu.ucla.sspace.util.ReflectionUtil;
import edu.ucla.sspace.wordsi.ContextExtractor;
import edu.ucla.sspace.wordsi.ContextGenerator;
import edu.ucla.sspace.wordsi.WordOccrrenceContextGenerator;
































































public class WCWordsiMain
  extends GenericWordsiMain
{
  private BasisMapping<String, String> basis;
  private WeightingFunction weighting;
  
  public WCWordsiMain() {}
  
  protected void addExtraOptions(ArgOptions options)
  {
    super.addExtraOptions(options);
    
    options.addOption('G', "weightingFunction", 
      "Specifies the class that will weight co-occurrences based on the window distance. (Default: LinearWeighting)", 
      

      true, "CLASSNAME", "Optional");
  }
  





  protected void handleExtraOptions()
  {
    if (argOptions.hasOption('G')) {
      weighting = ((WeightingFunction)ReflectionUtil.getObjectInstance(
        argOptions.getStringOption('G')));
    } else {
      weighting = new LinearWeighting();
    }
    
    if (argOptions.hasOption('L')) {
      basis = ((BasisMapping)loadObject(openLoadFile()));
      basis.setReadOnly(true);
    }
    else {
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
    ContextGenerator generator = new WordOccrrenceContextGenerator(
      basis, weighting, windowSize());
    return contextExtractorFromGenerator(generator);
  }
  


  protected SemanticSpaceIO.SSpaceFormat getSpaceFormat()
  {
    return SemanticSpaceIO.SSpaceFormat.SPARSE_BINARY;
  }
  
  public static void main(String[] args) throws Exception {
    WCWordsiMain main = new WCWordsiMain();
    main.run(args);
  }
}
