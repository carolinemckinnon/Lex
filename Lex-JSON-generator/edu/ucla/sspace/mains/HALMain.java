package edu.ucla.sspace.mains;

import edu.ucla.sspace.basis.StringBasisMapping;
import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.SemanticSpaceIO.SSpaceFormat;
import edu.ucla.sspace.hal.HyperspaceAnalogueToLanguage;
import edu.ucla.sspace.hal.LinearWeighting;
import edu.ucla.sspace.hal.WeightingFunction;
import edu.ucla.sspace.util.ReflectionUtil;























































































































public class HALMain
  extends GenericMain
{
  private HALMain() {}
  
  protected void addExtraOptions(ArgOptions options)
  {
    options.addOption('h', "threshold", "minimum entropy for semantic dimensions (default: disabled)", 
      true, 
      "DOUBLE", "Algorithm Options");
    options.addOption('r', "retain", 
      "maximum number of dimensions to retain(default: disabled)", 
      true, 
      "INT", "Algorithm Options");
    options.addOption('s', "windowSize", 
      "The number of words to inspect to the left and right of a focus word (default: 5)", 
      
      true, "INT", "Algorithm Options");
    options.addOption('W', "weighting", "WeightingFunction class name(default: LinearWeighting)", 
      true, 
      "CLASSNAME", "Algorithm Options");
  }
  
  public static void main(String[] args) throws Exception {
    HALMain hal = new HALMain();
    hal.run(args);
  }
  


  protected SemanticSpaceIO.SSpaceFormat getSpaceFormat()
  {
    return SemanticSpaceIO.SSpaceFormat.SPARSE_BINARY;
  }
  

  protected SemanticSpace getSpace()
  {
    WeightingFunction weighting;
    WeightingFunction weighting;
    if (argOptions.hasOption('W')) {
      weighting = (WeightingFunction)ReflectionUtil.getObjectInstance(argOptions.getStringOption('W'));
    } else
      weighting = new LinearWeighting();
    int windowSize = argOptions.getIntOption('s', 5);
    double threshold = argOptions.getDoubleOption('h', -1.0D);
    int retain = argOptions.getIntOption('r', -1);
    
    return new HyperspaceAnalogueToLanguage(
      new StringBasisMapping(), windowSize, weighting, 
      threshold, retain);
  }
  


  protected String getAlgorithmSpecifics()
  {
    return "Note that the --retain and --threshold properties are mutually exclusive;\nusing both will cause an exception";
  }
}
