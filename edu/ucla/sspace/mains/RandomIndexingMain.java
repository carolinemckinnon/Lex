package edu.ucla.sspace.mains;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.SemanticSpaceIO.SSpaceFormat;
import edu.ucla.sspace.ri.IndexVectorUtil;
import edu.ucla.sspace.ri.RandomIndexing;
import edu.ucla.sspace.vector.TernaryVector;
import java.io.File;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;




























































































































public class RandomIndexingMain
  extends GenericMain
{
  private static final Logger LOGGER = Logger.getLogger(RandomIndexingMain.class.getName());
  

  private Properties props;
  

  private RandomIndexing ri;
  

  private RandomIndexingMain()
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
      RandomIndexingMain main = new RandomIndexingMain();
      main.run(args);
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }
  


  protected Properties setupProperties()
  {
    props = System.getProperties();
    


    if (argOptions.hasOption("usePermutations")) {
      props.setProperty("edu.ucla.sspace.ri.RandomIndexing.usePermutations", 
        argOptions.getStringOption("usePermutations"));
    }
    
    if (argOptions.hasOption("permutationFunction")) {
      props.setProperty(
        "edu.ucla.sspace.ri.RandomIndexing.permutationFunction", 
        argOptions.getStringOption("permutationFunction"));
    }
    
    if (argOptions.hasOption("windowSize")) {
      props.setProperty("edu.ucla.sspace.ri.RandomIndexing.windowSize", 
        argOptions.getStringOption("windowSize"));
    }
    
    if (argOptions.hasOption("vectorLength")) {
      props.setProperty("edu.ucla.sspace.ri.RandomIndexing.vectorLength", 
        argOptions.getStringOption("vectorLength"));
    }
    
    if (argOptions.hasOption("useSparseSemantics")) {
      props.setProperty("edu.ucla.sspace.ri.RandomIndexing.sparseSemantics", 
        argOptions.getStringOption("useSparseSemantics"));
    }
    
    return props;
  }
  






  protected SemanticSpace getSpace()
  {
    ri = new RandomIndexing(props);
    


    if (argOptions.hasOption("loadVectors")) {
      String fileName = argOptions.getStringOption("loadVectors");
      LOGGER.info("loading index vectors from " + fileName);
      Map<String, TernaryVector> wordToIndexVector = 
        IndexVectorUtil.load(new File(fileName));
      ri.setWordToIndexVector(wordToIndexVector);
    }
    
    return ri;
  }
  



  protected SemanticSpaceIO.SSpaceFormat getSpaceFormat()
  {
    return SemanticSpaceIO.SSpaceFormat.SPARSE_BINARY;
  }
  



  protected void postProcessing()
  {
    if (argOptions.hasOption("saveVectors")) {
      String fileName = argOptions.getStringOption("saveVectors");
      LOGGER.info("saving index vectors to " + fileName);
      IndexVectorUtil.save(ri.getWordToIndexVector(), 
        new File(fileName));
    }
  }
}
