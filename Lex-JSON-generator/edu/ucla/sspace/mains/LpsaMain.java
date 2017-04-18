package edu.ucla.sspace.mains;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.SemanticSpaceIO.SSpaceFormat;
import edu.ucla.sspace.matrix.AffinityMatrixCreator;
import edu.ucla.sspace.nonlinear.LocalityPreservingSemanticAnalysis;
import edu.ucla.sspace.similarity.CosineSimilarity;
import edu.ucla.sspace.similarity.SimilarityFunction;
import edu.ucla.sspace.util.ReflectionUtil;
import java.io.IOError;
import java.io.IOException;
import java.util.Properties;






















































public class LpsaMain
  extends GenericMain
{
  private LpsaMain() {}
  
  protected void addExtraOptions(ArgOptions options)
  {
    options.addOption('n', "dimensions", 
      "the number of dimensions in the semantic space", 
      true, "INT", "Algorithm Options");
    options.addOption('p', "preprocess", "a MatrixTransform class to use for preprocessing", 
      true, "CLASSNAME", 
      "Algorithm Options");
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
  }
  
  public static void main(String[] args) {
    LpsaMain lpsa = new LpsaMain();
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
    try
    {
      return new LocalityPreservingSemanticAnalysis(creator);
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  



  protected SemanticSpaceIO.SSpaceFormat getSpaceFormat()
  {
    return SemanticSpaceIO.SSpaceFormat.BINARY;
  }
  

  protected Properties setupProperties()
  {
    Properties props = System.getProperties();
    
    if (argOptions.hasOption("dimensions")) {
      props.setProperty("edu.ucla.sspace.lpsa.LocalityPreservingSemanticAnalysis.dimensions", 
        argOptions.getStringOption("dimensions"));
    }
    if (argOptions.hasOption("preprocess")) {
      props.setProperty("edu.ucla.sspace.lpsa.LocalityPreservingSemanticAnalysis.transform", 
        argOptions.getStringOption("preprocess"));
    }
    return props;
  }
  


  protected String getAlgorithmSpecifics()
  {
    return "The --edgeType option specifies the method by which words are connected in\nthe affinity matrix.  Two options are provided: NEAREST_NEIGHBORS and\nMIN_SIMILARITY.  Each takes a parameter specified by the --edgeTypeParam\noption.  For NEAREST_NEIGHBORS, the parameter specifies how many words will\nbe counted as edges in the affinity matrix.  For MIN_SIMILARITY, the parameter\nspecifies the minimum similarity for two words to have an edge.\n\nThe --edgeWeighting option specifies how edges in the affinity matrix should\nbe weighted.  Valid options are: BINARY, GAUSSIAN_KERNEL, POLYNOMIAL_KERNEL,\nDOT_PRODUCT, COSINE_SIMILARITY.  The Gaussian and polynomial kernels take an\noptional parameter specified by --edgeWeightingParam that weights the kernel\nfunction.  The other options ignore the value of the parameter.\n\nThe default behavior is the use NEAREST_NEIGHBORS with a value of 20 and\nCOSINE_SIMILARITY edge weighting.";
  }
}
