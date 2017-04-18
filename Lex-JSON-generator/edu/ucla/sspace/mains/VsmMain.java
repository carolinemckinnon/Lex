package edu.ucla.sspace.mains;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.SemanticSpaceIO.SSpaceFormat;
import edu.ucla.sspace.vsm.VectorSpaceModel;
import java.io.IOError;
import java.io.IOException;
import java.util.Properties;








































public class VsmMain
  extends GenericMain
{
  private VsmMain() {}
  
  protected void addExtraOptions(ArgOptions options)
  {
    options.addOption('T', "transform", "a MatrixTransform class to use for preprocessing", 
      true, "CLASSNAME", 
      "Algorithm Options");
  }
  
  public static void main(String[] args) {
    VsmMain vsm = new VsmMain();
    try {
      vsm.run(args);
    }
    catch (Throwable t) {
      t.printStackTrace();
    }
  }
  

  protected SemanticSpace getSpace()
  {
    try
    {
      return new VectorSpaceModel();
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  



  protected SemanticSpaceIO.SSpaceFormat getSpaceFormat()
  {
    return SemanticSpaceIO.SSpaceFormat.SPARSE_BINARY;
  }
  




  protected Properties setupProperties()
  {
    Properties props = System.getProperties();
    
    if (argOptions.hasOption("transform")) {
      props.setProperty("edu.ucla.sspace.vsm.VectorSpaceModel.transform", 
        argOptions.getStringOption("transform"));
    }
    
    return props;
  }
}
