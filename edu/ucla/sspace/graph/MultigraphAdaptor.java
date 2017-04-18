package edu.ucla.sspace.graph;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;






























class MultigraphAdaptor<T, E extends TypedEdge<T>>
  extends GraphAdaptor<E>
  implements Multigraph<T, E>, Serializable
{
  private static final long serialVersionUID = 1L;
  
  public MultigraphAdaptor(Graph<E> g)
  {
    super(g);
  }
  


  public void clearEdges(T edgeType)
  {
    Iterator<E> it = edges().iterator();
    while (it.hasNext()) {
      if (((TypedEdge)it.next()).edgeType().equals(edgeType)) {
        it.remove();
      }
    }
  }
  

  public Multigraph<T, E> copy(Set<Integer> vertices)
  {
    Graph<E> g = super.copy(vertices);
    return new MultigraphAdaptor(g);
  }
  


  public Multigraph<T, E> copy(Set<Integer> vertices, T type)
  {
    throw new Error("to do");
  }
  


  public boolean contains(int vertex1, int vertex2, T edgeType)
  {
    Set<E> edges = getEdges(vertex1, vertex2);
    for (E e : edges) {
      if (e.edgeType().equals(edgeType))
        return true;
    }
    return false;
  }
  


  public Set<T> edgeTypes()
  {
    Set<T> types = new HashSet();
    for (E e : edges())
      types.add(e.edgeType());
    return types;
  }
  


  public Set<E> edges(T t)
  {
    throw new Error("TODO");
  }
  


  public Multigraph<T, E> subgraph(Set<Integer> vertices)
  {
    return new MultigraphAdaptor(super.subgraph(vertices));
  }
  


  public Multigraph<T, E> subgraph(Set<Integer> vertices, Set<T> edgeTypes)
  {
    throw new Error("TODO");
  }
}
