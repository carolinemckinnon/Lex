package edu.ucla.sspace.mains;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.SemanticSpaceIO.SSpaceFormat;
import edu.ucla.sspace.purandare.PurandareFirstOrder;
import java.util.Properties;













































public class PurandareMain
  extends GenericMain
{
  private PurandareMain() {}
  
  protected void addExtraOptions(ArgOptions options)
  {
    options.addOption('m', "maxContexts", "The maximum number of contexts to use per word", 
      true, "INT", "Algorithm Options");
  }
  
  public static void main(String[] args) {
    PurandareMain lsa = new PurandareMain();
    try {
      lsa.run(args);
    }
    catch (Throwable t) {
      t.printStackTrace();
    }
  }
  


  protected SemanticSpace getSpace()
  {
    return new PurandareFirstOrder();
  }
  



  protected SemanticSpaceIO.SSpaceFormat getSpaceFormat()
  {
    return SemanticSpaceIO.SSpaceFormat.SPARSE_BINARY;
  }
  


  protected Properties setupProperties()
  {
    Properties props = System.getProperties();
    


    if (argOptions.hasOption("maxContexts")) {
      props.setProperty("edu.ucla.sspace.purandare.PurandareFirstOrder.maxContexts", 
        argOptions.getStringOption("maxContexts"));
    }
    return props;
  }
}
