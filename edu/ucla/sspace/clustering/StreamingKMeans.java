package edu.ucla.sspace.clustering;

import edu.ucla.sspace.util.Generator;
import edu.ucla.sspace.util.Properties;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.Vectors;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;



































































































































public class StreamingKMeans<T extends DoubleVector>
  implements Generator<OnlineClustering<T>>
{
  private static final String PROPERTY_PREFIX = "edu.ucla.sspace.cluster.StreamingKMeans";
  public static final String NUM_POINTS_PROPERTY = "edu.ucla.sspace.cluster.StreamingKMeans.maxPoints";
  public static final String ALPHA_PROPERTY = "edu.ucla.sspace.cluster.StreamingKMeans.alpha";
  public static final String COFL_PROPERTY = "edu.ucla.sspace.cluster.StreamingKMeans.cofl";
  public static final String KOFL_PROPERTY = "edu.ucla.sspace.cluster.StreamingKMeans.kofl";
  public static final String BETA_PROPERTY = "edu.ucla.sspace.cluster.StreamingKMeans.beta";
  public static final String GAMMA_PROPERTY = "edu.ucla.sspace.cluster.StreamingKMeans.gamma";
  public static final int DEFAULT_NUM_CLUSTERS = 2;
  public static final int DEFAULT_NUM_POINTS = 1000;
  public static final double DEFAULT_ALPHA = 2.0D;
  public static final double DEFAULT_COFL = 2.0D;
  public static final double DEFAULT_KOFL = 2.0D;
  public static final double DEFAULT_BETA = 216.25D;
  public static final double DEFAULT_GAMMA = 42.25D;
  private final int numClusters;
  private final double logNumPoints;
  private final double alpha;
  private final double cofl;
  private final double kofl;
  private final double beta;
  private final double gamma;
  
  public StreamingKMeans()
  {
    this(new Properties());
  }
  


  public StreamingKMeans(Properties props)
  {
    numClusters = props.getProperty(
      "edu.ucla.sspace.clustering.OnlineClustering.numClusters", 2);
    int numPoints = props.getProperty(
      "edu.ucla.sspace.cluster.StreamingKMeans.maxPoints", 1000);
    logNumPoints = (Math.log(numPoints) / Math.log(2.0D));
    
    alpha = props.getProperty("edu.ucla.sspace.cluster.StreamingKMeans.alpha", 2.0D);
    cofl = props.getProperty("edu.ucla.sspace.cluster.StreamingKMeans.cofl", 2.0D);
    kofl = props.getProperty("edu.ucla.sspace.cluster.StreamingKMeans.kofl", 2.0D);
    
    beta = (2.0D * alpha * alpha * cofl + 2.0D * alpha);
    gamma = Math.max(
      4.0D * alpha * alpha * alpha * cofl * cofl + 2.0D * alpha * alpha * cofl, 
      beta * kofl + 1.0D);
  }
  



  public OnlineClustering<T> generate()
  {
    return new StreamingKMeansClustering(
      alpha, beta, gamma, numClusters, logNumPoints);
  }
  


  public String toString()
  {
    return "StreamingKMeans";
  }
  





  public class StreamingKMeansClustering<T extends DoubleVector>
    implements OnlineClustering<T>
  {
    private List<T> firstKPoints;
    




    private double LCost;
    




    private double facilityCost;
    




    private double totalCost;
    




    private final double alpha;
    




    private final double beta;
    




    private final double gamma;
    




    private final double logNumPoints;
    




    private final int numClusters;
    



    private List<Cluster<T>> facilities;
    



    private final AtomicInteger idCounter;
    



    private final double costThreshold;
    



    private final double facilityThreshold;
    




    public StreamingKMeansClustering(double alpha, double beta, double gamma, int numClusters, double logNumPoints)
    {
      facilities = new CopyOnWriteArrayList();
      idCounter = new AtomicInteger(1);
      firstKPoints = new ArrayList(numClusters);
      

      this.numClusters = numClusters;
      this.alpha = alpha;
      this.beta = beta;
      this.gamma = gamma;
      this.logNumPoints = logNumPoints;
      

      costThreshold = gamma;
      facilityThreshold = ((1.0D + logNumPoints) * numClusters);
      
      LCost = 1.0D;
      facilityCost = (LCost / (numClusters * (1.0D + logNumPoints)));
      totalCost = 0.0D;
    }
    



    public synchronized int addVector(T value)
    {
      int id = idCounter.getAndAdd(1);
      






      if (addDataPoint(value, id) < 0)
      {
        List<Cluster<T>> clusters = facilities;
        facilities = new ArrayList();
        LCost *= beta;
        facilityCost = (LCost / (numClusters * (1.0D + logNumPoints)));
        


        for (Cluster<T> cluster : clusters) {
          int assignment = addDataPoint(cluster.centroid(), 0);
          Cluster<T> newCluster = (Cluster)facilities.get(assignment);
          newCluster.dataPointIds().or(cluster.dataPointIds());
        }
      }
      return id;
    }
    









    private int addDataPoint(T value, int id)
    {
      double bestCost = Double.MAX_VALUE;
      int bestClusterId = 0;
      Cluster<T> bestCluster = null;
      int i = 0;
      for (Cluster<T> cluster : facilities) {
        double cost = cluster.compareWithVector(value);
        


        cost = -1.0D * cost + 1.0D;
        if (cost < bestCost) {
          bestCost = cost;
          bestCluster = cluster;
          bestClusterId = i;
        }
        i++;
      }
      




      double makeFacilityProb = Math.min(bestCost / facilityCost, 1.0D);
      boolean makeFacility = (facilities.size() == 0) || (
        Math.random() < makeFacilityProb);
      
      if (makeFacility) {
        Cluster<T> newCluster = new CentroidCluster(
          (DoubleVector)Vectors.instanceOf(value));
        newCluster.addVector(value, id > 0 ? id : -1);
        facilities.add(newCluster);
        bestClusterId = facilities.size() - 1;
      } else {
        bestCluster.addVector(value, id > 0 ? id : -1);
        totalCost += bestCost;
      }
      
      if (id != 0) {
        if (totalCost > gamma * LCost)
          return -1;
        if (facilities.size() >= facilityThreshold) {
          return -2;
        }
      }
      return bestClusterId;
    }
    


    public Cluster<T> getCluster(int clusterIndex)
    {
      if ((facilities.size() <= clusterIndex) || (clusterIndex < 0))
        throw new ArrayIndexOutOfBoundsException();
      return (Cluster)facilities.get(clusterIndex);
    }
    


    public List<Cluster<T>> getClusters()
    {
      return facilities;
    }
    


    public synchronized int size()
    {
      return facilities.size();
    }
    


    public String toString()
    {
      return "StreamingKMeansClustering-numClusters" + numClusters;
    }
  }
}
