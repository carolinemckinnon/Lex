package edu.ucla.sspace.nonlinear;

import edu.ucla.sspace.basis.StringBasisMapping;
import edu.ucla.sspace.common.GenericTermDocumentVectorSpace;
import edu.ucla.sspace.matrix.AffinityMatrixCreator;
import edu.ucla.sspace.matrix.LocalityPreservingProjection;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.Matrix.Type;
import edu.ucla.sspace.matrix.MatrixFile;
import edu.ucla.sspace.matrix.MatrixIO;
import edu.ucla.sspace.matrix.SvdlibcSparseBinaryMatrixBuilder;
import edu.ucla.sspace.matrix.Transform;
import edu.ucla.sspace.util.LoggerUtil;
import edu.ucla.sspace.util.ReflectionUtil;
import java.io.IOError;
import java.io.IOException;
import java.util.Properties;


























































































































public class LocalityPreservingSemanticAnalysis
  extends GenericTermDocumentVectorSpace
{
  private static final String PROPERTY_PREFIX = "edu.ucla.sspace.lpsa.LocalityPreservingSemanticAnalysis";
  public static final String MATRIX_TRANSFORM_PROPERTY = "edu.ucla.sspace.lpsa.LocalityPreservingSemanticAnalysis.transform";
  public static final String LPSA_DIMENSIONS_PROPERTY = "edu.ucla.sspace.lpsa.LocalityPreservingSemanticAnalysis.dimensions";
  private final AffinityMatrixCreator affinityCreator;
  private static final String LPSA_SSPACE_NAME = "lpsa-semantic-space";
  
  public LocalityPreservingSemanticAnalysis(AffinityMatrixCreator creator)
    throws IOException
  {
    super(false, new StringBasisMapping(), new SvdlibcSparseBinaryMatrixBuilder(true));
    affinityCreator = creator;
  }
  


  public String getSpaceName()
  {
    return "lpsa-semantic-space";
  }
  





  public void processSpace(Properties properties)
  {
    try
    {
      Transform transform = null;
      

      String transformClass = 
        properties.getProperty("edu.ucla.sspace.lpsa.LocalityPreservingSemanticAnalysis.transform");
      if (transformClass != null) {
        transform = (Transform)ReflectionUtil.getObjectInstance(transformClass);
      }
      MatrixFile transformedMatrix = processSpace(transform);
      

      int dimensions = 300;
      

      String dimensionsProp = 
        properties.getProperty("edu.ucla.sspace.lpsa.LocalityPreservingSemanticAnalysis.dimensions");
      if (dimensionsProp != null) {
        try {
          dimensions = Integer.parseInt(dimensionsProp);
        } catch (NumberFormatException nfe) {
          throw new IllegalArgumentException(
            "edu.ucla.sspace.lpsa.LocalityPreservingSemanticAnalysis.dimensions is not an integer: " + 
            dimensionsProp);
        }
      }
      
      LoggerUtil.verbose(LOG, "reducing to %d dimensions", new Object[] { Integer.valueOf(dimensions) });
      
      Matrix termDocMatrix = MatrixIO.readMatrix(
        transformedMatrix.getFile(), 
        transformedMatrix.getFormat(), 
        Matrix.Type.SPARSE_IN_MEMORY, true);
      

      MatrixFile affinityMatrix = affinityCreator.calculate(
        termDocMatrix);
      


      wordSpace = LocalityPreservingProjection.project(
        termDocMatrix, affinityMatrix, dimensions);
    }
    catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
}
