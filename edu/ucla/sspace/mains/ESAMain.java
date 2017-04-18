package edu.ucla.sspace.mains;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.SemanticSpaceIO.SSpaceFormat;
import edu.ucla.sspace.esa.ExplicitSemanticAnalysis;
import java.io.IOError;
import java.io.IOException;
import java.util.Properties;





















































public class ESAMain
  extends GenericMain
{
  private ESAMain() {}
  
  protected void addExtraOptions(ArgOptions options) {}
  
  public static void main(String[] args)
  {
    ESAMain esa = new ESAMain();
    try {
      esa.run(args);
    }
    catch (Throwable t) {
      t.printStackTrace();
    }
  }
  

  public SemanticSpace getSpace()
  {
    try
    {
      return new ExplicitSemanticAnalysis();
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  


  protected SemanticSpaceIO.SSpaceFormat getSpaceFormat()
  {
    return SemanticSpaceIO.SSpaceFormat.SPARSE_BINARY;
  }
  




  public Properties setupProperties()
  {
    Properties props = System.getProperties();
    return props;
  }
}
