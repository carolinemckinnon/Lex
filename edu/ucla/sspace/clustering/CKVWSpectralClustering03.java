package edu.ucla.sspace.clustering;

import edu.ucla.sspace.common.Statistics;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.RowScaledMatrix;
import edu.ucla.sspace.matrix.RowScaledSparseMatrix;
import edu.ucla.sspace.matrix.SparseMatrix;
import edu.ucla.sspace.util.Generator;
import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import java.util.Properties;



















































public class CKVWSpectralClustering03
  implements Clustering
{
  public static final String PROPERTY_PREFIX = "edu.ucla.sspace.clustering.CKVWSpectralClustering03";
  public static final String USE_KMEANS = "edu.ucla.sspace.clustering.CKVWSpectralClustering03.useKMeans";
  
  public CKVWSpectralClustering03() {}
  
  public Assignments cluster(Matrix matrix, Properties props)
  {
    SpectralClustering cluster = new SpectralClustering(
      0.2D, new SpectralCutGenerator());
    return cluster.cluster(matrix);
  }
  




  public Assignments cluster(Matrix matrix, int numClusters, Properties props)
  {
    SpectralClustering cluster = new SpectralClustering(
      0.2D, new SpectralCutGenerator());
    return cluster.cluster(
      matrix, numClusters, props.getProperty("edu.ucla.sspace.clustering.CKVWSpectralClustering03.useKMeans") != null);
  }
  


  public class SpectralCut
    extends BaseSpectralCut
  {
    public SpectralCut() {}
    


    protected DoubleVector computeSecondEigenVector(Matrix matrix, int vectorLength)
    {
      DoubleVector Rinv = new DenseVector(vectorLength);
      DoubleVector baseVector = new DenseVector(vectorLength);
      for (int i = 0; i < vectorLength; i++) {
        Rinv.set(i, 1.0D / Math.sqrt(rho.get(i)));
        baseVector.set(i, rho.get(i) * Rinv.get(i));
      }
      


      DoubleVector v = new DenseVector(vectorLength);
      for (int i = 0; i < v.length(); i++) {
        v.set(i, Math.random());
      }
      Matrix RinvData = (matrix instanceof SparseMatrix) ? 
        new RowScaledSparseMatrix((SparseMatrix)matrix, Rinv) : 
        new RowScaledMatrix(matrix, Rinv);
      

      int log = (int)Statistics.log2(vectorLength);
      for (int k = 0; k < log; k++)
      {
        v = orthonormalize(v, baseVector);
        DoubleVector newV = computeMatrixTransposeV(RinvData, v);
        computeMatrixDotV(RinvData, newV, v);
      }
      
      return v;
    }
  }
  
  public String toString() {
    return "CKVWSpectralClustering03";
  }
  


  public class SpectralCutGenerator
    implements Generator<EigenCut>
  {
    public SpectralCutGenerator() {}
    

    public EigenCut generate()
    {
      return new CKVWSpectralClustering03.SpectralCut(CKVWSpectralClustering03.this);
    }
  }
}
