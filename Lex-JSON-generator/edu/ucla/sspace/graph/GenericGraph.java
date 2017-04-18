package edu.ucla.sspace.graph;

import edu.ucla.sspace.util.primitive.IntSet;
import java.util.Iterator;
import java.util.Set;




























public class GenericGraph<T extends Edge>
  extends AbstractGraph<T, GenericEdgeSet<T>>
{
  private static final long serialVersionUID = 1L;
  
  public GenericGraph() {}
  
  public GenericGraph(Graph<? extends T> g)
  {
    for (Iterator localIterator = g.vertices().iterator(); localIterator.hasNext();) { int v = ((Integer)localIterator.next()).intValue();
      add(v); }
    for (T edge : g.edges()) {
      add(edge);
    }
  }
  



  public Graph<T> copy(Set<Integer> vertices)
  {
    if ((vertices.size() == order()) && (vertices.equals(vertices())))
      return new GenericGraph(this);
    GenericGraph<T> g = new GenericGraph();
    Iterator localIterator2; for (Iterator localIterator1 = vertices.iterator(); localIterator1.hasNext(); 
        



        localIterator2.hasNext())
    {
      int v = ((Integer)localIterator1.next()).intValue();
      if (!contains(v))
        throw new IllegalArgumentException(
          "Requested copy with non-existant vertex: " + v);
      g.add(v);
      localIterator2 = getAdjacencyList(v).iterator(); continue;T e = (Edge)localIterator2.next();
      g.add(e);
    }
    return g;
  }
  
  protected GenericEdgeSet<T> createEdgeSet(int vertex) {
    return new GenericEdgeSet(vertex);
  }
}
