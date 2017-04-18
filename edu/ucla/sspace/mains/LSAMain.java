package edu.ucla.sspace.mains;

import edu.ucla.sspace.basis.BasisMapping;
import edu.ucla.sspace.basis.StringBasisMapping;
import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.SemanticSpaceIO.SSpaceFormat;
import edu.ucla.sspace.lsa.LatentSemanticAnalysis;
import edu.ucla.sspace.matrix.LogEntropyTransform;
import edu.ucla.sspace.matrix.SVD;
import edu.ucla.sspace.matrix.SVD.Algorithm;
import edu.ucla.sspace.matrix.Transform;
import edu.ucla.sspace.matrix.factorization.SingularValueDecomposition;
import edu.ucla.sspace.util.ReflectionUtil;
import edu.ucla.sspace.util.SerializableUtil;
import java.io.IOError;
import java.io.IOException;



























































































































public class LSAMain
  extends GenericMain
{
  private BasisMapping<String, String> basis;
  
  private LSAMain() {}
  
  protected void addExtraOptions(ArgOptions options)
  {
    options.addOption('n', "dimensions", 
      "the number of dimensions in the semantic space", 
      true, "INT", "Algorithm Options");
    options.addOption('p', "preprocess", "a MatrixTransform class to use for preprocessing", 
      true, "CLASSNAME", 
      "Algorithm Options");
    options.addOption('S', "svdAlgorithm", "a specific SVD algorithm to use", 
      true, "SVD.Algorithm", 
      "Advanced Algorithm Options");
    options.addOption('B', "saveTermBasis", 
      "If true, the term basis mapping will be stored to the given file name", 
      
      true, "FILE", "Optional");
  }
  
  public static void main(String[] args) throws Exception {
    LSAMain lsa = new LSAMain();
    lsa.run(args);
  }
  
  protected SemanticSpace getSpace() {
    try {
      int dimensions = argOptions.getIntOption("dimensions", 300);
      Transform transform = new LogEntropyTransform();
      if (argOptions.hasOption("preprocess"))
        transform = (Transform)ReflectionUtil.getObjectInstance(
          argOptions.getStringOption("preprocess"));
      String algName = argOptions.getStringOption("svdAlgorithm", "ANY");
      SingularValueDecomposition factorization = SVD.getFactorization(
        SVD.Algorithm.valueOf(algName.toUpperCase()));
      basis = new StringBasisMapping();
      
      return new LatentSemanticAnalysis(
        false, dimensions, transform, factorization, false, basis);
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  



  protected SemanticSpaceIO.SSpaceFormat getSpaceFormat()
  {
    return SemanticSpaceIO.SSpaceFormat.BINARY;
  }
  
  protected void postProcessing() {
    if (argOptions.hasOption('B')) {
      SerializableUtil.save(basis, argOptions.getStringOption('B'));
    }
  }
  

  protected String getAlgorithmSpecifics()
  {
    return 
      "The --svdAlgorithm provides a way to manually specify which algorithm should\nbe used internally.  This option should not be used normally, as LSA will\nselect the fastest algorithm available.  However, in the event that it\nis needed, valid options are: SVDLIBC, SVDLIBJ, MATLAB, OCTAVE, JAMA and COLT\n";
  }
}
