package edu.ucla.sspace.clustering;

import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.SymmetricMatrix;
import edu.ucla.sspace.similarity.CosineSimilarity;
import edu.ucla.sspace.similarity.SimilarityFunction;
import edu.ucla.sspace.vector.DoubleVector;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Set;





















































public class NeighborChainAgglomerativeClustering
  implements Clustering
{
  private final ClusterLink method;
  private final SimilarityFunction simFunc;
  
  public static enum ClusterLink
  {
    SINGLE_LINK, 
    




    COMPLETE_LINK, 
    




    MEAN_LINK, 
    




    MEDIAN_LINK, 
    



    MCQUITTY_LINK;
  }
  












  public NeighborChainAgglomerativeClustering()
  {
    this(ClusterLink.MEAN_LINK, new CosineSimilarity());
  }
  






  public NeighborChainAgglomerativeClustering(ClusterLink method, SimilarityFunction simFunc)
  {
    if (!simFunc.isSymmetric()) {
      throw new IllegalArgumentException(
        "Agglomerative Clustering requires a symmetric similarity function");
    }
    
    this.method = method;
    this.simFunc = simFunc;
  }
  




  public Assignments cluster(Matrix m, Properties props)
  {
    throw new UnsupportedOperationException(
      "Cannot cluster without specifying the number of clusters.");
  }
  






  public Assignments cluster(Matrix m, int numClusters, Properties props)
  {
    Matrix adj = new SymmetricMatrix(m.rows(), m.rows());
    for (int r = 0; r < m.rows(); r++) {
      DoubleVector v = m.getRowVector(r);
      for (int c = r + 1; c < m.rows(); c++)
        adj.set(r, c, simFunc.sim(v, m.getRowVector(c)));
    }
    return clusterAdjacencyMatrix(adj, method, numClusters);
  }
  















  public static Assignments clusterAdjacencyMatrix(Matrix adj, ClusterLink method, int numClusters)
  {
    Map<Integer, Set<Integer>> clusterMap = 
      new HashMap();
    

    Set<Integer> remaining = new HashSet();
    


    for (int r = 0; r < adj.rows(); r++) {
      remaining.add(Integer.valueOf(r));
      Set<Integer> points = new HashSet();
      points.add(Integer.valueOf(r));
      clusterMap.put(Integer.valueOf(r), points);
    }
    



    Deque<Link> chain = new LinkedList();
    

    initializeChain(chain, remaining);
    
    while (clusterMap.size() > numClusters)
    {
      Link top = (Link)chain.peek();
      


      Link best = findBest(remaining, adj, x);
      




      if (sim > sim)
      {

        chain.push(best);
        remaining.remove(Integer.valueOf(x));

      }
      else
      {
        chain.pop();
        

        Link parent = (Link)chain.pop();
        


        Set<Integer> c1Points = (Set)clusterMap.get(Integer.valueOf(x));
        int c1Size = c1Points.size();
        Set<Integer> c2Points = (Set)clusterMap.get(Integer.valueOf(x));
        int c2Size = c2Points.size();
        clusterMap.remove(Integer.valueOf(x));
        clusterMap.remove(Integer.valueOf(x));
        


        for (Iterator localIterator = clusterMap.keySet().iterator(); localIterator.hasNext();) { int o = ((Integer)localIterator.next()).intValue();
          adj.set(x, o, 
            updateSimilarity(method, adj, x, c1Size, x, c2Size, 0));
        }
        
        c1Points.addAll(c2Points);
        
        clusterMap.put(Integer.valueOf(x), c1Points);
        


        remaining.add(Integer.valueOf(x));
        

        if (chain.size() == 0) {
          initializeChain(chain, remaining);
        }
      }
    }
    return formAssignments(clusterMap.values(), adj.rows());
  }
  











  private static double updateSimilarity(ClusterLink method, Matrix adj, int c1Index, int c1Size, int c2Index, int c2Size, int otherIndex)
  {
    double s1 = adj.get(c1Index, otherIndex);
    double s2 = adj.get(c2Index, otherIndex);
    switch (method) {
    case MEAN_LINK: 
      return (c1Size * s1 + c2Size * s2) / (c1Size + c2Size);
    case MEDIAN_LINK: 
      return 0.5D * s1 + 0.5D * s2 - 0.25D * adj.get(c1Index, c2Index);
    case COMPLETE_LINK: 
      return 0.5D * s1 + 0.5D * s2 + Math.min(s1, s2);
    case MCQUITTY_LINK: 
      return 0.5D * s1 + 0.5D * s2 + Math.max(s1, s2);
    case SINGLE_LINK: 
      return 0.5D * s1 + 0.5D * s2; }
    throw new IllegalArgumentException(
      "Unsupported ClusterLink Method");
  }
  





  private static void initializeChain(Deque<Link> chain, Set<Integer> remaining)
  {
    Iterator<Integer> iter = remaining.iterator();
    chain.push(new Link(-1.7976931348623157E308D, ((Integer)iter.next()).intValue()));
    iter.remove();
  }
  






  private static Link findBest(Set<Integer> remaining, Matrix adj, int cluster)
  {
    int best = -1;
    double sim = -1.7976931348623157E308D;
    for (Iterator localIterator = remaining.iterator(); localIterator.hasNext();) { int i = ((Integer)localIterator.next()).intValue();
      double s = adj.get(cluster, i);
      if (s > sim) {
        best = i;
        sim = s;
      }
    }
    
    return new Link(sim, best);
  }
  




  public static Assignments formAssignments(Collection<Set<Integer>> clusters, int numPoints)
  {
    int cid = 0;
    Assignment[] assignments = new Assignment[numPoints];
    for (Set<Integer> cluster : clusters) {
      for (Iterator localIterator2 = cluster.iterator(); localIterator2.hasNext();) { int point = ((Integer)localIterator2.next()).intValue();
        assignments[point] = new HardAssignment(cid); }
      cid++;
    }
    return new Assignments(clusters.size(), assignments, null);
  }
  





  public static class Link
  {
    public double sim;
    



    public int x;
    




    public Link(double sim, int x)
    {
      this.sim = sim;
      this.x = x;
    }
  }
}
