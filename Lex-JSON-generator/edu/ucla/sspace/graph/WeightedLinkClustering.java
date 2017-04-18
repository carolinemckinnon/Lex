package edu.ucla.sspace.graph;

import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.util.MultiMap;
import edu.ucla.sspace.vector.CompactSparseVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.io.Serializable;
import java.util.Properties;
import java.util.Set;
































public class WeightedLinkClustering
  extends LinkClustering
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private static final String PROPERTY_PREFIX = "edu.ucla.sspace.graph.WeightedLinkClustering";
  public static final String KEEP_WEIGHT_VECTORS_PROPERTY = "edu.ucla.sspace.graph.WeightedLinkClustering.keepWeightVectors";
  private final TIntObjectMap<SparseDoubleVector> vertexToWeightVector;
  private boolean keepWeightVectors;
  
  public WeightedLinkClustering()
  {
    vertexToWeightVector = new TIntObjectHashMap();
    keepWeightVectors = true;
  }
  






  public <E extends WeightedEdge> MultiMap<Integer, Integer> cluster(WeightedGraph<E> graph, int numClusters, Properties props)
  {
    if (props.getProperty("edu.ucla.sspace.graph.WeightedLinkClustering.keepWeightVectors") != null)
      keepWeightVectors = Boolean.parseBoolean(
        props.getProperty("edu.ucla.sspace.graph.WeightedLinkClustering.keepWeightVectors"));
    vertexToWeightVector.clear();
    return super.cluster(graph, numClusters, props);
  }
  





  public <E extends WeightedEdge> MultiMap<Integer, Integer> cluster(WeightedGraph<E> graph, Properties props)
  {
    if (props.getProperty("edu.ucla.sspace.graph.WeightedLinkClustering.keepWeightVectors") != null)
      keepWeightVectors = Boolean.parseBoolean(
        props.getProperty("edu.ucla.sspace.graph.WeightedLinkClustering.keepWeightVectors"));
    vertexToWeightVector.clear();
    return super.cluster(graph, props);
  }
  

  protected <E extends Edge> double getConnectionSimilarity(Graph<E> graph, int keystone, int impost1, int impost2)
  {
    WeightedGraph<WeightedEdge> wg = (WeightedGraph)graph;
    

    return Similarity.tanimotoCoefficient(
      getVertexWeightVector(wg, impost1), 
      getVertexWeightVector(wg, impost2));
  }
  







  private <E extends WeightedEdge> SparseDoubleVector getVertexWeightVector(WeightedGraph<E> g, int vertex)
  {
    if (keepWeightVectors) {
      SparseDoubleVector weightVec = (SparseDoubleVector)vertexToWeightVector.get(vertex);
      if (weightVec == null) {
        synchronized (this) {
          weightVec = (SparseDoubleVector)vertexToWeightVector.get(vertex);
          if (weightVec == null) {
            weightVec = computeWeightVector(g, vertex);
            vertexToWeightVector.put(vertex, weightVec);
          }
        }
      }
      return weightVec;
    }
    
    return computeWeightVector(g, vertex);
  }
  

  private <E extends WeightedEdge> SparseDoubleVector computeWeightVector(WeightedGraph<E> g, int vertex)
  {
    SparseDoubleVector weightVec = new CompactSparseVector();
    Set<E> adjacent = g.getAdjacencyList(vertex);
    


    double normalizer = 1.0D / adjacent.size();
    


    for (E e : adjacent) {
      int v = e.from() == vertex ? e.to() : e.from();
      weightVec.set(v, normalizer * e.weight());
    }
    




    weightVec.set(vertex, normalizer);
    return weightVec;
  }
}
