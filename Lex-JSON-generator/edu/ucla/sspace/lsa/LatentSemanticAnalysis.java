package edu.ucla.sspace.lsa;

import comp6803.plainly.CorpusCreate;
import edu.ucla.sspace.basis.BasisMapping;
import edu.ucla.sspace.basis.StringBasisMapping;
import edu.ucla.sspace.common.GenericTermDocumentVectorSpace;
import edu.ucla.sspace.common.Statistics;
import edu.ucla.sspace.matrix.ArrayMatrix;
import edu.ucla.sspace.matrix.DiagonalMatrix;
import edu.ucla.sspace.matrix.LogEntropyTransform;
import edu.ucla.sspace.matrix.Matrices;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.MatrixFile;
import edu.ucla.sspace.matrix.MatrixIO;
import edu.ucla.sspace.matrix.MatrixIO.Format;
import edu.ucla.sspace.matrix.SVD;
import edu.ucla.sspace.matrix.Transform;
import edu.ucla.sspace.matrix.factorization.SingularValueDecomposition;
import edu.ucla.sspace.text.Document;
import edu.ucla.sspace.text.IteratorFactory;
import edu.ucla.sspace.util.LoggerUtil;
import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.SparseHashDoubleVector;
import edu.ucla.sspace.vector.Vectors;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Properties;













































































































































































































































public class LatentSemanticAnalysis
  extends GenericTermDocumentVectorSpace
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private static final String PROPERTY_PREFIX = "edu.ucla.sspace.lsa.LatentSemanticAnalysis";
  public static final String MATRIX_TRANSFORM_PROPERTY = "edu.ucla.sspace.lsa.LatentSemanticAnalysis.transform";
  public static final String LSA_DIMENSIONS_PROPERTY = "edu.ucla.sspace.lsa.LatentSemanticAnalysis.dimensions";
  public static final String LSA_SVD_ALGORITHM_PROPERTY = "edu.ucla.sspace.lsa.LatentSemanticAnalysis.svd.algorithm";
  public static final String RETAIN_DOCUMENT_SPACE_PROPERTY = "edu.ucla.sspace.lsa.LatentSemanticAnalysis.retainDocSpace";
  private static final String LSA_SSPACE_NAME = "lsa-semantic-space";
  private Matrix documentSpace;
  private Matrix sigma;
  private transient WeakReference<Matrix> UtimesSigmaInvRef;
  private Matrix U;
  private Matrix UtimesSigmaInv;
  private double[] rowEntropy;
  private final SingularValueDecomposition reducer;
  private final Transform transform;
  private final int dimensions;
  private final boolean retainDocumentSpace;
  
  public LatentSemanticAnalysis()
    throws IOException
  {
    this(false, 300, new LogEntropyTransform(), SVD.getFastestAvailableFactorization(), false, new StringBasisMapping());
  }
  









  public LatentSemanticAnalysis(int numDimensions)
    throws IOException
  {
    this(false, numDimensions, new LogEntropyTransform(), SVD.getFastestAvailableFactorization(), false, new StringBasisMapping());
  }
  












  public LatentSemanticAnalysis(int numDimensions, SingularValueDecomposition svdMethod)
    throws IOException
  {
    this(false, numDimensions, new LogEntropyTransform(), svdMethod, false, new StringBasisMapping());
  }
  












  public LatentSemanticAnalysis(int numDimensions, boolean retainDocumentSpace)
    throws IOException
  {
    this(retainDocumentSpace, numDimensions, new LogEntropyTransform(), SVD.getFastestAvailableFactorization(), false, new StringBasisMapping());
  }
  























  public LatentSemanticAnalysis(boolean retainDocumentSpace, int dimensions, Transform transform, SingularValueDecomposition reducer, boolean readHeaderToken, BasisMapping<String, String> termToIndex)
    throws IOException
  {
    super(readHeaderToken, termToIndex, reducer.getBuilder());
    this.reducer = reducer;
    this.transform = transform;
    this.dimensions = dimensions;
    this.retainDocumentSpace = retainDocumentSpace;
  }
  


  public String getSpaceName()
  {
    return "lsa-semantic-space";
  }
  

































  public DoubleVector getDocumentVector(int documentNumber)
  {
    if (documentSpace == null) {
      throw new IllegalStateException(
        "The document space has not been retained or generated.");
    }
    if ((documentNumber < 0) || (documentNumber >= documentSpace.rows())) {
      throw new IllegalArgumentException(
        "Document number is not within the bounds of the number of documents: " + 
        documentNumber);
    }
    return documentSpace.getRowVector(documentNumber);
  }
  







  public int documentSpaceSize()
  {
    if (documentSpace == null) {
      throw new IllegalStateException(
        "The document space has not yet been generated.");
    }
    return documentSpace.rows();
  }
  








  public void processSpace(Properties properties)
  {
    MatrixFile processedSpace = processSpace(transform);
    LoggerUtil.info(LOG, "reducing to %d dimensions", new Object[] { Integer.valueOf(dimensions) });
    

    reducer.factorize(processedSpace, dimensions);
    


    U = reducer.getLeftVectors();
    sigma = reducer.getSingularValues();
    
    int rows = sigma.rows();
    double[] sigmaInv = new double[rows];
    for (int i = 0; i < rows; i++)
      sigmaInv[i] = (1.0D / sigma.get(i, i));
    DiagonalMatrix sigmaInvMatrix = new DiagonalMatrix(sigmaInv);
    
    Matrix UtimesSigmaInv = Matrices.multiply(U, sigmaInvMatrix);
    try {
      MatrixIO.writeMatrix(UtimesSigmaInv, new File("corpus/" + 
        CorpusCreate.getInstance().getName().replaceAll(" ", "-").toLowerCase() + 
        "-u-times-sigma-inv.dat"), MatrixIO.Format.SVDLIBC_DENSE_BINARY);
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
    
    try
    {
      new ObjectOutputStream(new FileOutputStream("corpus/" + CorpusCreate.getInstance().getName().replaceAll(" ", "-").toLowerCase() + "-term-document-index.dat")).writeObject(termToIndex);
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    

    if (retainDocumentSpace) {
      LoggerUtil.verbose(LOG, "loading in document space", new Object[0]);
      



      documentSpace = Matrices.transpose(reducer.classFeatures());
    }
  }
  
  public void processTransform(File rowEntropyMatrix, File termToIndexMatrix, File USigmaFile)
  {
    try {
      System.out.println("Reading Row Entropy...");
      rowEntropy = ((double[])new ObjectInputStream(new FileInputStream(rowEntropyMatrix)).readObject());
      System.out.println("Reading Term-Document Index...");
      termToIndex = ((StringBasisMapping)new ObjectInputStream(new FileInputStream(termToIndexMatrix)).readObject());
    } catch (ClassNotFoundException e) {
      System.err.println(e.getMessage());
    } catch (FileNotFoundException e) {
      System.err.println(e.getMessage());
    } catch (IOException e) {
      System.err.println(e.getMessage());
    } catch (Exception e) {
      System.err.println(e.getMessage());
    }
    try
    {
      System.out.println("Reading U-Times Sigma Matrix...");
      UtimesSigmaInv = MatrixIO.readMatrix(USigmaFile, MatrixIO.Format.SVDLIBC_DENSE_BINARY);
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
  



















  public DoubleVector project(Document doc)
  {
    Iterator<String> docTokens = 
      IteratorFactory.tokenize(doc.reader());
    



    termToIndex.setReadOnly(true);
    int numDims = termToIndex.numDimensions();
    



    SparseDoubleVector docVec = new SparseHashDoubleVector(numDims);
    while (docTokens.hasNext()) {
      int dim = termToIndex.getDimension((String)docTokens.next());
      if (dim >= 0) {
        docVec.add(dim, 1.0D);
      }
    }
    



    DoubleVector transformed = entropyTransform(docVec);
    

    Matrix queryAsMatrix = new ArrayMatrix(1, numDims);
    for (int nz : docVec.getNonZeroIndices()) {
      queryAsMatrix.set(0, nz, transformed.get(nz));
    }
    
    Matrix result = Matrices.multiply(queryAsMatrix, UtimesSigmaInv);
    



    int cols = result.columns();
    Object projected = new DenseVector(result.columns());
    for (int i = 0; i < cols; i++)
      ((DoubleVector)projected).set(i, result.get(0, i));
    return projected;
  }
  
  public DoubleVector entropyTransform(DoubleVector column) {
    DoubleVector transformed = (DoubleVector)Vectors.instanceOf(column);
    int length = column.length();
    for (int row = 0; row < length; row++) {
      transformed.set(row, Statistics.log2_1p(column.get(row)) * rowEntropy[row]);
    }
    return transformed;
  }
}
