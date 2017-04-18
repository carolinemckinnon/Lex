package edu.ucla.sspace.graph;

import edu.ucla.sspace.util.primitive.IntSet;
import edu.ucla.sspace.util.primitive.TroveIntSet;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;































class DirectedGraphAdaptor<T extends DirectedEdge>
  extends GraphAdaptor<T>
  implements DirectedGraph<T>, Serializable
{
  private static final long serialVersionUID = 1L;
  
  public DirectedGraphAdaptor(Graph<T> g)
  {
    super(g);
  }
  


  public DirectedGraph<T> copy(Set<Integer> vertices)
  {
    Graph<T> g = super.copy(vertices);
    return new DirectedGraphAdaptor(g);
  }
  


  public int inDegree(int vertex)
  {
    int degree = 0;
    Set<T> edges = getAdjacencyList(vertex);
    if (edges == null)
      return 0;
    for (T e : edges) {
      if (e.to() == vertex)
        degree++;
    }
    return degree;
  }
  




  public Set<T> inEdges(int vertex)
  {
    Set<T> edges = getAdjacencyList(vertex);
    if (edges.isEmpty()) {
      return Collections.emptySet();
    }
    Set<T> in = new HashSet();
    for (T e : edges) {
      if (e.to() == vertex)
        in.add(e);
    }
    return in;
  }
  


  public int outDegree(int vertex)
  {
    int degree = 0;
    Set<T> edges = getAdjacencyList(vertex);
    for (T e : edges) {
      if (e.from() == vertex)
        degree++;
    }
    return degree;
  }
  





  public Set<T> outEdges(int vertex)
  {
    Set<T> edges = getAdjacencyList(vertex);
    if (edges.isEmpty()) {
      return Collections.emptySet();
    }
    Set<T> out = new HashSet();
    for (T e : edges) {
      if (e.from() == vertex)
        out.add(e);
    }
    return out;
  }
  


  public IntSet predecessors(int vertex)
  {
    Set<T> in = inEdges(vertex);
    IntSet preds = new TroveIntSet();
    if (in.isEmpty())
      return preds;
    for (T e : in)
      preds.add(e.from());
    return preds;
  }
  


  public IntSet successors(int vertex)
  {
    Set<T> out = outEdges(vertex);
    IntSet succs = new TroveIntSet();
    if (out.isEmpty())
      return succs;
    for (T e : out)
      succs.add(e.to());
    return succs;
  }
  


  public DirectedGraph<T> subgraph(Set<Integer> vertices)
  {
    Graph<T> g = super.subgraph(vertices);
    return new DirectedGraphAdaptor(g);
  }
}
