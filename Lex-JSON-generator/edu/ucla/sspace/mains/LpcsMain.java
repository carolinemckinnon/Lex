package edu.ucla.sspace.mains;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.SemanticSpaceIO.SSpaceFormat;
import edu.ucla.sspace.matrix.AffinityMatrixCreator;
import edu.ucla.sspace.nonlinear.LocalityPreservingCooccurrenceSpace;
import edu.ucla.sspace.similarity.CosineSimilarity;
import edu.ucla.sspace.similarity.SimilarityFunction;
import edu.ucla.sspace.util.ReflectionUtil;
import java.util.Properties;























































public class LpcsMain
  extends GenericMain
{
  private LpcsMain() {}
  
  protected void addExtraOptions(ArgOptions options)
  {
    options.addOption('n', "dimensions", 
      "the number of dimensions in the semantic space", 
      true, "INT", "Algorithm Options");
    options.addOption('e', "edgeType", 
      "the AffinityMatrixCreator that will select edges for an affinity matrix", 
      
      true, "CLASSNAME", "Required");
    options.addOption('E', "edgeSimParam", 
      "a parameter that the edge selection method may use.", 
      true, "DOUBLE", "Algorithm Options");
    options.addOption('W', "kernelSim", 
      "the SimilarityFunction for weighting edges in the affinity matrix", 
      
      true, "CLASSNAME", "Required");
    options.addOption('G', "edgeWeightingParam", 
      "a parameter that the kernelSim method may use.", 
      true, "DOUBLE", "Algorithm Options");
    options.addOption('s', "windowSize", 
      "The number of words to inspect to the left and right of a focus word (default: 5)", 
      
      true, "INT", "Algorithm Options");
  }
  
  public static void main(String[] args) {
    LpcsMain lpsa = new LpcsMain();
    try {
      lpsa.run(args);
    }
    catch (Throwable t) {
      t.printStackTrace();
    }
  }
  
  protected SemanticSpace getSpace() {
    AffinityMatrixCreator creator = (AffinityMatrixCreator)ReflectionUtil.getObjectInstance(
      argOptions.getStringOption('e'));
    if (argOptions.hasOption("edgeTypeParam")) {
      creator.setParams(new double[] { argOptions.getIntOption("edgeTypeParam") });
    }
    SimilarityFunction edgeSim = new CosineSimilarity();
    SimilarityFunction kernelSim = (SimilarityFunction)ReflectionUtil.getObjectInstance(
      argOptions.getStringOption("edgeWeighting"));
    if (argOptions.hasOption("edgeWeighting"))
      kernelSim.setParams(new double[] { argOptions.getIntOption("edgeWeightingParam") });
    creator.setFunctions(edgeSim, kernelSim);
    
    return new LocalityPreservingCooccurrenceSpace(creator);
  }
  



  protected SemanticSpaceIO.SSpaceFormat getSpaceFormat()
  {
    return SemanticSpaceIO.SSpaceFormat.BINARY;
  }
  

  protected Properties setupProperties()
  {
    Properties props = System.getProperties();
    if (argOptions.hasOption("windowSize")) {
      props.setProperty(
        "edu.ucla.sspace.lpsa.LocalityPreservingCooccurrenceSpace.windowSize", 
        argOptions.getStringOption("windowSize"));
    }
    return props;
  }
  


  protected String getAlgorithmSpecifics()
  {
    return "The --edgeType option specifies the method by which words are connected in\nthe affinity matrix.  Two options are provided: NEAREST_NEIGHBORS and\nMIN_SIMILARITY.  Each takes a parameter specified by the --edgeTypeParam\noption.  For NEAREST_NEIGHBORS, the parameter specifies how many words will\nbe counted as edges in the affinity matrix.  For MIN_SIMILARITY, the parameter\nspecifies the minimum similarity for two words to have an edge.\n\nThe --edgeWeighting option specifies how edges in the affinity matrix should\nbe weighted.  Valid options are: BINARY, GAUSSIAN_KERNEL, POLYNOMIAL_KERNEL,\nDOT_PRODUCT, COSINE_SIMILARITY.  The Gaussian and polynomial kernels take an\noptional parameter specified by --edgeWeightingParam that weights the kernel\nfunction.  The other options ignore the value of the parameter.\n\nThe default behavior is the use NEAREST_NEIGHBORS with a value of 20 and\nCOSINE_SIMILARITY edge weighting.";
  }
}
