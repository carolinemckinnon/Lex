package edu.ucla.sspace.clustering;

import edu.ucla.sspace.common.Statistics;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.util.Generator;
import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import java.util.Properties;



















































public class CKVWSpectralClustering06
  implements Clustering
{
  public static final String PROPERTY_PREFIX = "edu.ucla.sspace.clustering.CKVWSpectralClustering06";
  public static final String USE_KMEANS = "edu.ucla.sspace.clustering.CKVWSpectralClustering06.useKMeans";
  
  public CKVWSpectralClustering06() {}
  
  public Assignments cluster(Matrix matrix, Properties props)
  {
    SpectralClustering cluster = new SpectralClustering(
      0.2D, new SuperSpectralGenerator());
    return cluster.cluster(matrix);
  }
  




  public Assignments cluster(Matrix matrix, int numClusters, Properties props)
  {
    SpectralClustering cluster = new SpectralClustering(
      0.2D, new SuperSpectralGenerator());
    return cluster.cluster(
      matrix, numClusters, props.getProperty("edu.ucla.sspace.clustering.CKVWSpectralClustering06.useKMeans") != null);
  }
  




  public class SuperSpectralCut
    extends BaseSpectralCut
  {
    public SuperSpectralCut() {}
    



    protected DoubleVector computeSecondEigenVector(Matrix matrix, int vectorLength)
    {
      DoubleVector pi = new DenseVector(vectorLength);
      DoubleVector D = new DenseVector(vectorLength);
      DoubleVector piDInverse = new DenseVector(vectorLength);
      for (int i = 0; i < vectorLength; i++) {
        double piValue = rho.get(i) / pSum;
        pi.set(i, piValue);
        if (piValue > 0.0D) {
          D.set(i, Math.sqrt(piValue));
          piDInverse.set(i, piValue / D.get(i));
        }
      }
      











      DoubleVector v = new DenseVector(vectorLength);
      for (int i = 0; i < v.length(); i++) {
        v.set(i, Math.random());
      }
      
      int log = (int)Statistics.log2(vectorLength);
      for (int k = 0; k < log; k++)
      {
        v = orthonormalize(v, piDInverse);
        






        for (int i = 0; i < vectorLength; i++) {
          if (D.get(i) != 0.0D) {
            v.set(i, v.get(i) / D.get(i));
          }
        }
        DoubleVector newV = computeMatrixTransposeV(matrix, v);
        

        computeMatrixDotV(matrix, newV, v);
        


        for (int i = 0; i < vectorLength; i++) {
          double oldValue = v.get(i);
          double newValue = oldValue * D.get(i) / rho.get(i);
          v.set(i, newValue);
        }
      }
      
      return v;
    }
  }
  
  public String toString() {
    return "CKVWSpectralClustering06";
  }
  


  public class SuperSpectralGenerator
    implements Generator<EigenCut>
  {
    public SuperSpectralGenerator() {}
    

    public EigenCut generate()
    {
      return new CKVWSpectralClustering06.SuperSpectralCut(CKVWSpectralClustering06.this);
    }
  }
}
