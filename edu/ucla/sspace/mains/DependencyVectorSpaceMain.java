package edu.ucla.sspace.mains;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.SemanticSpaceIO.SSpaceFormat;
import edu.ucla.sspace.dv.DependencyVectorSpace;
import java.util.Properties;



















































public class DependencyVectorSpaceMain
  extends DependencyGenericMain
{
  private DependencyVectorSpaceMain() {}
  
  public void addExtraOptions(ArgOptions options)
  {
    super.addExtraOptions(options);
    
    options.addOption('a', "pathAcceptor", 
      "the DependencyPathAcceptor to filter relations", 
      true, "CLASSNAME", "Algorithm Options");
    options.addOption('W', "pathWeighter", 
      "the DependencyPathWeight to weight parse tree paths", 
      true, "CLASSNAME", "Algorithm Options");
    options.addOption('b', "basisMapping", 
      "the BasisMapping to decide the dimension representations", 
      
      true, "CLASSNAME", "Algorithm Options");
    options.addOption('l', "pathLength", 
      "the maximum path length that will be accepted (default: any).", 
      
      true, "INT", "Algorithm Options");
  }
  
  public static void main(String[] args) {
    DependencyVectorSpaceMain svs = new DependencyVectorSpaceMain();
    try {
      svs.run(args);
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
    setupDependencyExtractor();
    return new DependencyVectorSpace(
      System.getProperties(), argOptions.getIntOption('l', 0));
  }
  


  protected String getAlgorithmSpecifics()
  {
    return 
      "The --basisMapping specifies how the dependency paths that connect two words are\nmapped into dimensions.  The default behavior is to use only the word at the end\nof the path.\n\nThe --pathAcceptor specifies which paths in the corpus are treated as valid\ncontexts.  The default behavior is to use the minimum set of paths defined in\nPadó and Lapata (2007) paper.\n\nThe --pathWeighter specifies how to score paths that are accepted.  The default\nbehavior is not to weight the paths.\n";
  }
  



















  protected Properties setupProperties()
  {
    Properties props = System.getProperties();
    
    if (argOptions.hasOption("pathAcceptor")) {
      props.setProperty(
        "edu.ucla.sspace.dri.DependencyVectorSpace.pathAcceptor", 
        argOptions.getStringOption("pathAcceptor"));
    }
    if (argOptions.hasOption("pathWeighter")) {
      props.setProperty(
        "edu.ucla.sspace.dri.DependencyVectorSpace.pathWeighting", 
        argOptions.getStringOption("pathWeighter"));
    }
    if (argOptions.hasOption("basisMapping")) {
      props.setProperty(
        "edu.ucla.sspace.dri.DependencyVectorSpace.basisMapping", 
        argOptions.getStringOption("basisMapping"));
    }
    return props;
  }
}
