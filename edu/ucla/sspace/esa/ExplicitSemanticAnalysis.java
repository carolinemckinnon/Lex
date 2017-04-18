package edu.ucla.sspace.esa;

import edu.ucla.sspace.basis.BasisMapping;
import edu.ucla.sspace.basis.StringBasisMapping;
import edu.ucla.sspace.common.GenericTermDocumentVectorSpace;
import edu.ucla.sspace.matrix.MatrixBuilder;
import edu.ucla.sspace.matrix.MatrixFile;
import edu.ucla.sspace.matrix.MatrixIO;
import edu.ucla.sspace.matrix.SvdlibcSparseBinaryMatrixBuilder;
import edu.ucla.sspace.matrix.TfIdfTransform;
import edu.ucla.sspace.util.GrowableArrayList;
import edu.ucla.sspace.util.SparseArray;
import edu.ucla.sspace.util.SparseHashArray;
import edu.ucla.sspace.vector.SparseVector;
import edu.ucla.sspace.vector.Vector;
import java.io.IOError;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;




























































public class ExplicitSemanticAnalysis
  extends GenericTermDocumentVectorSpace
{
  public static final String ESA_SSPACE_NAME = "esa-semantic-space";
  private final List<String> documentLabels;
  
  public ExplicitSemanticAnalysis()
    throws IOException
  {
    super(true, new StringBasisMapping(), new SvdlibcSparseBinaryMatrixBuilder());
    



    documentLabels = Collections.synchronizedList(
      new GrowableArrayList());
  }
  













  public ExplicitSemanticAnalysis(BasisMapping<String, String> termToIndex, MatrixBuilder termDocumentMatrixBuilder)
    throws IOException
  {
    super(true, termToIndex, termDocumentMatrixBuilder);
    



    documentLabels = Collections.synchronizedList(
      new GrowableArrayList());
  }
  


  protected void handleDocumentHeader(int docIndex, String header)
  {
    documentLabels.set(docIndex, header);
  }
  








  public SparseArray<String> getDocumentDescriptors(Vector documentVector)
  {
    if (documentVector.length() != getVectorLength()) {
      throw new IllegalArgumentException(
        "An documentVector with an invalid length cannot be interpreted by ESA.");
    }
    
    SparseArray<String> docLabels = new SparseHashArray();
    


    if ((documentVector instanceof SparseVector)) {
      int[] nonZeros = ((SparseVector)documentVector).getNonZeroIndices();
      for (int index : nonZeros)
        docLabels.set(index, (String)documentLabels.get(index));
    } else {
      for (int index = 0; index < documentVector.length(); index++) {
        if (documentVector.getValue(index).doubleValue() != 0.0D)
          docLabels.set(index, (String)documentLabels.get(index));
      }
    }
    return docLabels;
  }
  

  public void processSpace(Properties properties)
  {
    try
    {
      MatrixFile processedSpace = processSpace(
        new TfIdfTransform());
      wordSpace = MatrixIO.readMatrix(
        processedSpace.getFile(), processedSpace.getFormat());
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  


  public String getSpaceName()
  {
    return "esa-semantic-space";
  }
}
