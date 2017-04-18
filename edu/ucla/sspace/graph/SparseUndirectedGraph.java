package edu.ucla.sspace.graph;

import java.util.Iterator;
import java.util.Set;













































public class SparseUndirectedGraph
  extends AbstractGraph<Edge, SparseUndirectedEdgeSet>
{
  private static final long serialVersionUID = 1L;
  public static final int DEFAULT_INITIAL_EDGE_CAPACITY = 4;
  private final int initialEdgeCapacity;
  
  public SparseUndirectedGraph()
  {
    this(16, 4);
  }
  



  public SparseUndirectedGraph(int initialVertexCapacity)
  {
    this(initialVertexCapacity, 4);
  }
  



  public SparseUndirectedGraph(int initialVertexCapacity, int initialEdgeCapacity)
  {
    super(initialVertexCapacity);
    if (initialEdgeCapacity < 0)
      throw new IllegalArgumentException("Edge capcity must be positive");
    this.initialEdgeCapacity = initialEdgeCapacity;
  }
  


  public SparseUndirectedGraph(Set<Integer> vertices)
  {
    super(vertices);
    initialEdgeCapacity = 4;
  }
  




  public SparseUndirectedGraph(Graph<? extends Edge> g)
  {
    super(Math.max(g.order(), 16));
    initialEdgeCapacity = (g.order() > 0 ? 
      (int)Math.ceil(g.size() / g.order()) : 
      4);
    for (Integer v : g.vertices())
      add(v.intValue());
    for (Edge e : g.edges()) {
      add(e);
    }
  }
  



  public SparseUndirectedGraph copy(Set<Integer> vertices)
  {
    if ((vertices.size() == order()) && (vertices.equals(vertices())))
      return new SparseUndirectedGraph(this);
    SparseUndirectedGraph g = new SparseUndirectedGraph();
    Iterator localIterator2; for (Iterator localIterator1 = vertices.iterator(); localIterator1.hasNext(); 
        



        localIterator2.hasNext())
    {
      int v = ((Integer)localIterator1.next()).intValue();
      if (!contains(v))
        throw new IllegalArgumentException(
          "Requested copy with non-existant vertex: " + v);
      g.add(v);
      localIterator2 = getAdjacencyList(v).iterator(); continue;Edge e = (Edge)localIterator2.next();
      if ((vertices.contains(Integer.valueOf(e.from()))) && (vertices.contains(Integer.valueOf(e.to()))))
        g.add(e);
    }
    return g;
  }
  


  protected SparseUndirectedEdgeSet createEdgeSet(int vertex)
  {
    return new SparseUndirectedEdgeSet(vertex, initialEdgeCapacity);
  }
}
