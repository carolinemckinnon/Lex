package edu.ucla.sspace.mains;

import edu.ucla.sspace.coals.Coals;
import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.SemanticSpaceIO.SSpaceFormat;
import edu.ucla.sspace.matrix.CorrelationTransform;
import edu.ucla.sspace.matrix.MatrixFactorization;
import edu.ucla.sspace.matrix.SVD;
import edu.ucla.sspace.matrix.Transform;

























































public class CoalsMain
  extends GenericMain
{
  private CoalsMain() {}
  
  public void addExtraOptions(ArgOptions options)
  {
    options.addOption('n', "dimensions", 
      "Set the number of columns to keep in the raw co-occurance matrix.", 
      
      true, "INT", "Optional");
    options.addOption('m', "maxWords", 
      "Set the maximum number of words to keep in the space, ordered by frequency", 
      
      true, "INT", "Optional");
    options.addOption('s', "reducedDimension", 
      "Set the number of dimension to reduce to using the Singular Value Decompositon.  This is used if --reduce is set.", 
      

      true, "INT", "Optional");
    options.addOption('r', "reduce", 
      "Set to true if the co-occurrance matrix should be reduced using the Singluar Value Decomposition", 
      
      false, null, "Optional");
  }
  
  public static void main(String[] args) throws Exception {
    CoalsMain coals = new CoalsMain();
    coals.run(args);
  }
  


  public SemanticSpace getSpace()
  {
    Transform transform = new CorrelationTransform();
    MatrixFactorization reducer = argOptions.hasOption("reduce") ? 
      SVD.getFastestAvailableFactorization() : 
      null;
    
    return new Coals(transform, reducer, 
      argOptions.getIntOption("reducedDimension", 0), 
      argOptions.getIntOption("maxWords", 0), 
      argOptions.getIntOption("dimensions", 0));
  }
  


  protected SemanticSpaceIO.SSpaceFormat getSpaceFormat()
  {
    return SemanticSpaceIO.SSpaceFormat.SPARSE_BINARY;
  }
}
