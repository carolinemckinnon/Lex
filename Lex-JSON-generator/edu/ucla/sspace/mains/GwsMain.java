package edu.ucla.sspace.mains;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.SemanticSpaceIO.SSpaceFormat;
import edu.ucla.sspace.gws.GenericWordSpace;
import java.util.Properties;








































public class GwsMain
  extends GenericMain
{
  private GwsMain() {}
  
  protected void addExtraOptions(ArgOptions options)
  {
    options.addOption('s', "windowSize", 
      "The number of words to inspect to the left and right of a focus word (default: 5)", 
      
      true, "INT", "Algorithm Options");
    options.addOption('W', "useWordOrder", "Distinguish between relative positions of co-occurrences of the same word (default: false)", 
    
      false, 
      null, "Algorithm Options");
  }
  
  public static void main(String[] args) {
    GwsMain gws = new GwsMain();
    try {
      gws.run(args);
    }
    catch (Throwable t) {
      t.printStackTrace();
    }
  }
  


  protected SemanticSpaceIO.SSpaceFormat getSpaceFormat()
  {
    return SemanticSpaceIO.SSpaceFormat.SPARSE_BINARY;
  }
  


  protected SemanticSpace getSpace()
  {
    return new GenericWordSpace();
  }
  




  protected Properties setupProperties()
  {
    Properties props = System.getProperties();
    
    if (argOptions.hasOption("windowSize")) {
      props.setProperty(
        "edu.ucla.sspace.gws.GenericWordSpace.windowSize", 
        argOptions.getStringOption("windowSize"));
    }
    
    if (argOptions.hasOption("useWordOrder")) {
      props.setProperty(
        "edu.ucla.sspace.gws.GenericWordSpace.useWordOrder", "true");
    }
    
    return props;
  }
}
