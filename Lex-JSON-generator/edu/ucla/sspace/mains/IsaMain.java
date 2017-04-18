package edu.ucla.sspace.mains;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.SemanticSpaceIO.SSpaceFormat;
import edu.ucla.sspace.isa.IncrementalSemanticAnalysis;
import edu.ucla.sspace.ri.IndexVectorUtil;
import edu.ucla.sspace.vector.TernaryVector;
import java.io.File;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

























































































































public class IsaMain
  extends GenericMain
{
  private static final Logger LOGGER = Logger.getLogger(IsaMain.class.getName());
  



  private Properties props;
  


  private IncrementalSemanticAnalysis isa;
  



  private IsaMain()
  {
    super(false);
    props = null;
    isa = null;
  }
  


  protected void addExtraOptions(ArgOptions options)
  {
    options.addOption('h', "historyDecayRate", "the decay rate for history effects of co-occurring words", 
      true, 
      "DOUBLE", "Algorithm Options");
    options.addOption('i', "impactRate", "the rate at which co-occurrence affects semantics", 
      true, "DOUBLE", 
      "Algorithm Options");
    options.addOption('l', "vectorLength", "length of semantic vectors", 
      true, "INT", "Algorithm Options");
    options.addOption('L', "loadVectors", 
      "load word-to-TernaryVector mapping before processing", 
      true, "FILE", "Algorithm Options");
    options.addOption('n', "permutationFunction", 
      "permutation function to use.  This should be generic for TernaryVectors", 
      true, 
      "CLASSNAME", "Advanced Algorithm Options");
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
  }
  
  public static void main(String[] args) {
    IsaMain isa = new IsaMain();
    try {
      isa.run(args);
    }
    catch (Throwable t) {
      t.printStackTrace();
    }
  }
  
  protected SemanticSpace getSpace() {
    isa = new IncrementalSemanticAnalysis();
    


    if (argOptions.hasOption("loadVectors")) {
      String fileName = argOptions.getStringOption("loadVectors");
      LOGGER.info("loading index vectors from " + fileName);
      Map<String, TernaryVector> wordToIndexVector = 
        IndexVectorUtil.load(new File(fileName));
      isa.setWordToIndexVector(wordToIndexVector);
    }
    return isa;
  }
  

  protected Properties setupProperties()
  {
    props = System.getProperties();
    



    if (argOptions.hasOption("usePermutations")) {
      props.setProperty(
        "edu.ucla.sspace.isa.IncrementalSemanticAnalysis.usePermutations", 
        argOptions.getStringOption("usePermutations"));
    }
    
    if (argOptions.hasOption("permutationFunction")) {
      props.setProperty(
        "edu.ucla.sspace.isa.IncrementalSemanticAnalysis.permutationFunction", 
        argOptions.getStringOption("permutationFunction"));
    }
    
    if (argOptions.hasOption("windowSize")) {
      props.setProperty(
        "edu.ucla.sspace.isa.IncrementalSemanticAnalysis.windowSize", 
        argOptions.getStringOption("windowSize"));
    }
    
    if (argOptions.hasOption("vectorLength")) {
      props.setProperty(
        "edu.ucla.sspace.isa.IncrementalSemanticAnalysis.vectorLength", 
        argOptions.getStringOption("vectorLength"));
    }
    
    if (argOptions.hasOption("useSparseSemantics")) {
      props.setProperty(
        "edu.ucla.sspace.isa.IncrementalSemanticAnalysis.sparseSemantics", 
        argOptions.getStringOption("useSparseSemantics"));
    }
    
    if (argOptions.hasOption("impactRate")) {
      props.setProperty(
        "edu.ucla.sspace.isa.IncrementalSemanticAnalysis.impactRate", 
        argOptions.getStringOption("impactRate"));
    }
    
    if (argOptions.hasOption("historyDecayRate")) {
      props.setProperty(
        "edu.ucla.sspace.isa.IncrementalSemanticAnalysis.historyDecayRate", 
        argOptions.getStringOption("historyDecayRate"));
    }
    
    return props;
  }
  




  protected void postProcessing()
  {
    if (argOptions.hasOption("saveVectors")) {
      String fileName = argOptions.getStringOption("saveVectors");
      LOGGER.info("saving index vectors to " + fileName);
      IndexVectorUtil.save(isa.getWordToIndexVector(), 
        new File(fileName));
    }
  }
  


  protected SemanticSpaceIO.SSpaceFormat getSpaceFormat()
  {
    return SemanticSpaceIO.SSpaceFormat.SPARSE_BINARY;
  }
  



  protected String getAlgorithmSpecifics()
  {
    return 
      "ISA is an incremental algorithm where the semantics is based on co-occurrence\nof words.  Semantics accumulate as a function both the index vectors and\nthe semantics of the co-occurring words.  Documents are processed in the\norder they are specified,with documents in --fileList processed before\ndocuments specified by the --docFile option.\n\nThe impact rate specifies the degree to which the co-occurrence of a word\nimpacts the semantics of another word.  This value affects both of the\nimpact of index vectors and semantics.  Thedefault rate is 0.003.\n\nThe history decay rate specifies how quickly to reduce the impact of the\nsemantics of a co-occurring word as the total number of occurrences for that\nword increases.  High decay rates cause the semantics to be discounted more\nquickly.  The default rate is 100.";
  }
}
