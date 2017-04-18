package edu.ucla.sspace.clustering;

import edu.ucla.sspace.util.Generator;
import edu.ucla.sspace.util.Properties;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.Vectors;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;






















































































public class OnlineKMeans<T extends DoubleVector>
  implements Generator<OnlineClustering<T>>
{
  private static final String PROPERTY_PREFIX = "edu.ucla.sspace.cluster.OnlineKMeans";
  public static final String MERGE_THRESHOLD_PROPERTY = "edu.ucla.sspace.cluster.OnlineKMeans.merge";
  public static final double DEFAULT_MERGE_THRESHOLD = 0.35D;
  public static final int DEFAULT_MAX_CLUSTERS = 15;
  private final double clusterThreshold;
  private final int maxNumClusters;
  
  public OnlineKMeans()
  {
    this(new Properties());
  }
  


  public OnlineKMeans(Properties props)
  {
    clusterThreshold = props.getProperty(
      "edu.ucla.sspace.cluster.OnlineKMeans.merge", 0.35D);
    maxNumClusters = props.getProperty(
      "edu.ucla.sspace.clustering.OnlineClustering.numClusters", 15);
  }
  



  public OnlineClustering<T> generate()
  {
    return new OnlineKMeansClustering(clusterThreshold, maxNumClusters);
  }
  
  public String toString() {
    return "OnLineKMeans_" + maxNumClusters + "c_";
  }
  





  public class OnlineKMeansClustering<T extends DoubleVector>
    implements OnlineClustering<T>
  {
    private final double clusterThreshold;
    




    private final int maxNumClusters;
    




    private final List<Cluster<T>> elements;
    




    private final AtomicInteger idCounter;
    





    public OnlineKMeansClustering(double mergeThreshold, int maxNumClusters)
    {
      elements = new CopyOnWriteArrayList();
      idCounter = new AtomicInteger(0);
      
      clusterThreshold = mergeThreshold;
      this.maxNumClusters = maxNumClusters;
    }
    


    public int addVector(T value)
    {
      int id = idCounter.getAndAdd(1);
      
      Iterator<Cluster<T>> elementIter = elements.iterator();
      

      Cluster<T> bestMatch = null;
      int bestIndex = elements.size();
      double bestScore = -1.0D;
      double similarity = -1.0D;
      int i = 0;
      while (elementIter.hasNext()) {
        Cluster<T> cluster = (Cluster)elementIter.next();
        similarity = cluster.compareWithVector(value);
        if (similarity >= bestScore) {
          bestScore = similarity;
          bestMatch = cluster;
          bestIndex = i;
        }
        i++;
      }
      


      if ((bestScore >= clusterThreshold) || 
        (elements.size() >= maxNumClusters)) {
        bestMatch.addVector(value, id);
      }
      else
      {
        synchronized (elements)
        {

          if (elements.size() < maxNumClusters) {
            bestMatch = new SynchronizedCluster(
              new CentroidCluster((DoubleVector)Vectors.instanceOf(value)));
            elements.add(bestMatch);
          }
          if (bestMatch != null)
            bestMatch.addVector(value, id);
        }
      }
      return id;
    }
    


    public Cluster<T> getCluster(int clusterIndex)
    {
      if (elements.size() <= clusterIndex)
        return null;
      return (Cluster)elements.get(clusterIndex);
    }
    


    public List<Cluster<T>> getClusters()
    {
      return elements;
    }
    


    public synchronized int size()
    {
      return elements.size();
    }
    


    public String toString()
    {
      return 
        "OnlineKMeansClustering-maxNumClusters" + maxNumClusters + "-threshold" + clusterThreshold;
    }
  }
}
