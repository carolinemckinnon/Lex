package edu.ucla.sspace.clustering;

import edu.ucla.sspace.matrix.Matrix;
import java.io.IOError;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

























































public class ClutoClustering
  implements Clustering
{
  public static final String PROPERTY_PREFIX = "edu.ucla.sspace.clustering.ClutoClustering";
  public static final String CLUSTER_METHOD = "edu.ucla.sspace.clustering.ClutoClustering.clusterSimilarity";
  public static final String CLUSTER_CRITERION = "edu.ucla.sspace.clustering.ClutoClustering.clusterCriterion";
  public ClutoClustering() {}
  
  public static enum Method
  {
    REPEATED_BISECTIONS_REPEATED("rbr"), 
    KMEANS("direct"), 
    AGGLOMERATIVE("agglo"), 
    NEAREST_NEIGHBOOR("graph"), 
    BAGGLO("bagglo");
    

    private final String name;
    

    private Method(String name)
    {
      this.name = name;
    }
    


    String getClutoName()
    {
      return name;
    }
  }
  




  public static enum Criterion
  {
    I1("i1"), 
    I2("i2"), 
    E1("e1"), 
    G1("g1"), 
    G1P("g1p"), 
    H1("h1"), 
    H2("h2"), 
    SLINK("slink"), 
    WSLINK("wslink"), 
    CLINK("clink"), 
    WCLINK("wclink"), 
    UPGMA("upgma"), 
    WUPGMA("wupgma");
    

    private final String name;
    

    private Criterion(String name)
    {
      this.name = name;
    }
    


    String getClutoName()
    {
      return name;
    }
  }
  



  private static Method DEFAULT_CLUSTER_METHOD = Method.AGGLOMERATIVE;
  
  private static Criterion DEFAULT_CRITERION = Criterion.UPGMA;
  




  private static final Logger LOGGER = Logger.getLogger(ClutoClustering.class.getName());
  








  public Assignments cluster(Matrix matrix, Properties properties)
  {
    throw new UnsupportedOperationException(
      "CLUTO requires the number of clusters to be specified and therefore cannot be invoked using this method.");
  }
  







  public Assignments cluster(Matrix matrix, int numClusters, Properties properties)
  {
    Method clmethod = DEFAULT_CLUSTER_METHOD;
    String methodProp = properties.getProperty("edu.ucla.sspace.clustering.ClutoClustering.clusterSimilarity");
    if (methodProp != null)
      clmethod = Method.valueOf(methodProp);
    Criterion criterion = DEFAULT_CRITERION;
    String criterionProp = properties.getProperty("edu.ucla.sspace.clustering.ClutoClustering.clusterCriterion");
    if (criterionProp != null)
      criterion = Criterion.valueOf(criterionProp);
    return cluster(matrix, numClusters, clmethod, criterion);
  }
  











  public Assignments cluster(Matrix matrix, int numClusters, Method clusterMethod, Criterion criterionMethod)
  {
    try
    {
      String clmethod = clusterMethod.getClutoName();
      String crtmethod = criterionMethod.getClutoName();
      return ClutoWrapper.cluster(matrix, clmethod, 
        crtmethod, numClusters);
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
}
