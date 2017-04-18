package edu.ucla.sspace.mains;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.SemanticSpaceIO.SSpaceFormat;
import edu.ucla.sspace.dependency.DependencyExtractor;
import edu.ucla.sspace.dependency.DependencyExtractorManager;
import edu.ucla.sspace.dependency.DependencyPathAcceptor;
import edu.ucla.sspace.dependency.UniversalPathAcceptor;
import edu.ucla.sspace.svs.PointWiseCombinor;
import edu.ucla.sspace.svs.StructuredVectorSpace;
import edu.ucla.sspace.svs.VectorCombinor;
import edu.ucla.sspace.util.ReflectionUtil;
import java.util.Properties;


















































































































public class StructuredVectorSpaceMain
  extends DependencyGenericMain
{
  private StructuredVectorSpaceMain() {}
  
  public void addExtraOptions(ArgOptions options)
  {
    super.addExtraOptions(options);
    
    options.addOption('a', "pathAcceptor", 
      "The DependencyPathAcceptor to use", 
      true, "CLASSNAME", "Algorithm Options");
    options.addOption('c', "vectorCombinor", 
      "The VectorCombinor to use", 
      true, "CLASSNAME", "Algorithm Options");
  }
  
  public static void main(String[] args) throws Exception {
    StructuredVectorSpaceMain svs = new StructuredVectorSpaceMain();
    svs.run(args);
  }
  




  protected SemanticSpace getSpace()
  {
    setupDependencyExtractor();
    DependencyPathAcceptor acceptor;
    DependencyPathAcceptor acceptor;
    if (argOptions.hasOption("pathAcceptor")) {
      acceptor = (DependencyPathAcceptor)ReflectionUtil.getObjectInstance(
        argOptions.getStringOption("pathAcceptor"));
    } else
      acceptor = new UniversalPathAcceptor();
    VectorCombinor combinor;
    VectorCombinor combinor;
    if (argOptions.hasOption("pathAcceptor")) {
      combinor = (VectorCombinor)ReflectionUtil.getObjectInstance(
        argOptions.getStringOption("vectorCombinor"));
    } else {
      combinor = new PointWiseCombinor();
    }
    DependencyExtractor extractor = 
      DependencyExtractorManager.getDefaultExtractor();
    return new StructuredVectorSpace(extractor, acceptor, combinor);
  }
  


  protected Properties setupProperties()
  {
    return System.getProperties();
  }
  


  protected SemanticSpaceIO.SSpaceFormat getSpaceFormat()
  {
    return SemanticSpaceIO.SSpaceFormat.SERIALIZE;
  }
}
