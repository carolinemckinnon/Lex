package edu.ucla.sspace.vsm;

import edu.ucla.sspace.basis.BasisMapping;
import edu.ucla.sspace.basis.StringBasisMapping;
import edu.ucla.sspace.common.GenericTermDocumentVectorSpace;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.MatrixBuilder;
import edu.ucla.sspace.matrix.MatrixFile;
import edu.ucla.sspace.matrix.MatrixIO;
import edu.ucla.sspace.matrix.SvdlibcSparseBinaryMatrixBuilder;
import edu.ucla.sspace.matrix.Transform;
import edu.ucla.sspace.util.ReflectionUtil;
import edu.ucla.sspace.vector.DoubleVector;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;








































































































public class VectorSpaceModel
  extends GenericTermDocumentVectorSpace
{
  private static final String PROPERTY_PREFIX = "edu.ucla.sspace.vsm.VectorSpaceModel";
  public static final String MATRIX_TRANSFORM_PROPERTY = "edu.ucla.sspace.vsm.VectorSpaceModel.transform";
  private static final String VSM_SSPACE_NAME = "vector-space-model";
  
  public VectorSpaceModel()
    throws IOException
  {
    super(false, new StringBasisMapping(), new SvdlibcSparseBinaryMatrixBuilder());
  }
  

















  public VectorSpaceModel(boolean readHeaderToken, BasisMapping<String, String> termToIndex, MatrixBuilder termDocumentMatrixBuilder)
    throws IOException
  {
    super(readHeaderToken, termToIndex, termDocumentMatrixBuilder);
  }
  


  public String getSpaceName()
  {
    return "vector-space-model";
  }
  
























  public DoubleVector getDocumentVector(int documentNumber)
  {
    if (wordSpace == null) {
      throw new IllegalStateException(
        "The document space has not yet been generated.");
    }
    if ((documentNumber < 0) || (documentNumber >= wordSpace.columns())) {
      throw new IllegalArgumentException(
        "Document number is not within the bounds of the number of documents: " + 
        documentNumber);
    }
    return wordSpace.getColumnVector(documentNumber);
  }
  








  public int documentSpaceSize()
  {
    if (wordSpace == null) {
      throw new IllegalStateException(
        "The document space has not yet been generated.");
    }
    return wordSpace.columns();
  }
  




  public void processSpace(Properties properties)
  {
    try
    {
      Transform transform = null;
      

      String transformClass = 
        properties.getProperty("edu.ucla.sspace.vsm.VectorSpaceModel.transform");
      if (transformClass != null)
        transform = (Transform)ReflectionUtil.getObjectInstance(
          transformClass);
      MatrixFile processedSpace = super.processSpace(transform);
      System.out.printf("Matrix saved in %s as %s%n", new Object[] {
        processedSpace.getFile(), 
        processedSpace.getFormat() });
      wordSpace = MatrixIO.readMatrix(processedSpace.getFile(), 
        processedSpace.getFormat());
      System.out.printf("loaded word space of %d x %d%n", new Object[] {
        Integer.valueOf(wordSpace.rows()), Integer.valueOf(wordSpace.columns()) });
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
}
