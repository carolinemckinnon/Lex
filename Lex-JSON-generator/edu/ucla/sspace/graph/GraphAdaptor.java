package edu.ucla.sspace.graph;

import edu.ucla.sspace.util.primitive.IntSet;
import java.util.Set;



































public abstract class GraphAdaptor<T extends Edge>
  implements Graph<T>
{
  private final Graph<T> g;
  
  public GraphAdaptor(Graph<T> g)
  {
    if (g == null)
      throw new NullPointerException("Cannot wrap null graph");
    this.g = g;
  }
  


  public boolean add(int i)
  {
    return g.add(i);
  }
  


  public boolean add(T edge)
  {
    return g.add(edge);
  }
  


  public void clear()
  {
    g.clear();
  }
  


  public void clearEdges()
  {
    g.clearEdges();
  }
  


  public Graph<T> copy(Set<Integer> vertices)
  {
    return g.copy(vertices);
  }
  


  public boolean contains(int vertex)
  {
    return g.contains(vertex);
  }
  


  public boolean contains(int from, int to)
  {
    return g.contains(from, to);
  }
  


  public boolean contains(Edge e)
  {
    return g.contains(e);
  }
  


  public int degree(int vertex)
  {
    return g.degree(vertex);
  }
  


  public Set<T> edges()
  {
    return g.edges();
  }
  


  public Set<T> getAdjacencyList(int vertex)
  {
    return g.getAdjacencyList(vertex);
  }
  


  public IntSet getNeighbors(int vertex)
  {
    return g.getNeighbors(vertex);
  }
  


  public Set<T> getEdges(int vertex1, int vertex2)
  {
    return g.getEdges(vertex1, vertex2);
  }
  


  public boolean hasCycles()
  {
    return g.hasCycles();
  }
  


  public int order()
  {
    return g.order();
  }
  


  public boolean remove(T e)
  {
    return g.remove(e);
  }
  


  public boolean remove(int vertex)
  {
    return g.remove(vertex);
  }
  


  public int size()
  {
    return g.size();
  }
  


  public Graph<T> subgraph(Set<Integer> vertices)
  {
    return g.subgraph(vertices);
  }
  


  public String toString()
  {
    return g.toString();
  }
  


  public IntSet vertices()
  {
    return g.vertices();
  }
}
