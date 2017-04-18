package edu.ucla.sspace.graph;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;















































public class SparseWeightedGraph
  extends AbstractGraph<WeightedEdge, SparseWeightedEdgeSet>
  implements WeightedGraph<WeightedEdge>
{
  private static final long serialVersionUID = 1L;
  
  public SparseWeightedGraph() {}
  
  public SparseWeightedGraph(Set<Integer> vertices)
  {
    super(vertices);
  }
  



  public SparseWeightedGraph(Graph<? extends WeightedEdge> g)
  {
    for (Integer v : g.vertices())
      add(v.intValue());
    for (WeightedEdge e : g.edges()) {
      add(e);
    }
  }
  



  public WeightedGraph<WeightedEdge> copy(Set<Integer> toCopy)
  {
    if ((toCopy.size() == order()) && (toCopy.equals(vertices())))
      return new SparseWeightedGraph(this);
    SparseWeightedGraph g = new SparseWeightedGraph();
    for (Iterator localIterator1 = toCopy.iterator(); localIterator1.hasNext();) { int v = ((Integer)localIterator1.next()).intValue();
      if (!contains(v))
        throw new IllegalArgumentException(
          "Requested copy with non-existant vertex: " + v);
      g.add(v);
      SparseWeightedEdgeSet edges = (SparseWeightedEdgeSet)getEdgeSet(v);
      for (Iterator localIterator2 = toCopy.iterator(); localIterator2.hasNext();) { int v2 = ((Integer)localIterator2.next()).intValue();
        if (v == v2)
          break;
        if (edges.connects(v2)) {
          for (WeightedEdge e : edges.getEdges(v2)) {
            g.add(e);
          }
        }
      }
    }
    

    return g;
  }
  



  protected SparseWeightedEdgeSet createEdgeSet(int vertex)
  {
    return new SparseWeightedEdgeSet(vertex);
  }
  


  public double strength(int vertex)
  {
    SparseWeightedEdgeSet e = (SparseWeightedEdgeSet)getEdgeSet(vertex);
    return e == null ? 0.0D : e.sum();
  }
  




  public WeightedGraph subgraph(Set<Integer> vertices)
  {
    Graph<WeightedEdge> g = super.subgraph(vertices);
    return new SubgraphAdaptor(g);
  }
  



  private class SubgraphAdaptor
    extends GraphAdaptor<WeightedEdge>
    implements WeightedGraph<WeightedEdge>, Serializable
  {
    private static final long serialVersionUID = 1L;
    



    public SubgraphAdaptor()
    {
      super();
    }
    




    public WeightedGraph<WeightedEdge> copy(Set<Integer> vertices)
    {
      if ((vertices.size() == order()) && (vertices.equals(vertices())))
        return new SparseWeightedGraph(this);
      SparseWeightedGraph g = new SparseWeightedGraph();
      Iterator localIterator2; for (Iterator localIterator1 = vertices.iterator(); localIterator1.hasNext(); 
          



          localIterator2.hasNext())
      {
        int v = ((Integer)localIterator1.next()).intValue();
        if (!contains(v))
          throw new IllegalArgumentException(
            "Requested copy with non-existant vertex: " + v);
        g.add(v);
        localIterator2 = getAdjacencyList(v).iterator(); continue;WeightedEdge e = (WeightedEdge)localIterator2.next();
        if ((vertices.contains(Integer.valueOf(e.from()))) && 
          (vertices.contains(Integer.valueOf(e.to()))))
          g.add(e);
      }
      return g;
    }
    


    public double strength(int vertex)
    {
      double sum = 0.0D;
      for (WeightedEdge e : getAdjacencyList(vertex))
        sum += e.weight();
      return sum;
    }
    


    public WeightedGraph subgraph(Set<Integer> vertices)
    {
      Graph<WeightedEdge> g = SparseWeightedGraph.this.subgraph(vertices);
      return new SubgraphAdaptor(SparseWeightedGraph.this, g);
    }
  }
}
