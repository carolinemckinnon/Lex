package edu.ucla.sspace.mains;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.SemanticSpaceIO.SSpaceFormat;
import edu.ucla.sspace.rri.ReflectiveRandomIndexing;
import java.util.Properties;
import java.util.logging.Logger;























































public class ReflectiveRandomIndexingMain
  extends GenericMain
{
  private static final Logger LOGGER = Logger.getLogger(ReflectiveRandomIndexingMain.class.getName());
  

  private Properties props;
  

  private ReflectiveRandomIndexing ri;
  

  private ReflectiveRandomIndexingMain()
  {
    ri = null;
  }
  


  protected void addExtraOptions(ArgOptions options)
  {
    options.addOption('l', "vectorLength", "length of semantic vectors", 
      true, "INT", "Algorithm Options");
    options.addOption('n', "permutationFunction", 
      "permutation function to use.  This should be genric for TernaryVectors", 
      
      true, "CLASSNAME", "Advanced Algorithm Options");
    options.addOption('p', "usePermutations", "whether to permute index vectors based on word order", 
      true, 
      "BOOL", "Algorithm Options");
    options.addOption('r', "useSparseSemantics", "use a sparse encoding of semantics to save memory", 
      true, 
      "BOOL", "Algorithm Options");
    options.addOption('s', "windowSize", "how many words to consider in each direction", 
      true, 
      "INT", "Algorithm Options");
    options.addOption('S', "saveVectors", "save word-to-IndexVector mapping after processing", 
      true, 
      "FILE", "Algorithm Options");
    options.addOption('L', "loadVectors", "load word-to-IndexVector mapping before processing", 
      true, 
      "FILE", "Algorithm Options");
  }
  
  public static void main(String[] args) {
    try {
      ReflectiveRandomIndexingMain main = new ReflectiveRandomIndexingMain();
      main.run(args);
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }
  


  protected Properties setupProperties()
  {
    props = System.getProperties();
    


    if (argOptions.hasOption("vectorLength")) {
      props.setProperty("edu.ucla.sspace.ri.ReflectiveRandomIndexing.vectorLength", 
        argOptions.getStringOption("vectorLength"));
    }
    
    if (argOptions.hasOption("useSparseSemantics")) {
      props.setProperty("edu.ucla.sspace.ri.ReflectiveRandomIndexing.sparseSemantics", 
        argOptions.getStringOption("useSparseSemantics"));
    }
    
    return props;
  }
  






  protected SemanticSpace getSpace()
  {
    ri = new ReflectiveRandomIndexing(props);
    return ri;
  }
  



  protected SemanticSpaceIO.SSpaceFormat getSpaceFormat()
  {
    return SemanticSpaceIO.SSpaceFormat.SPARSE_BINARY;
  }
}
