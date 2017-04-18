package edu.ucla.sspace.graph;

import edu.ucla.sspace.util.HashMultiMap;
import edu.ucla.sspace.util.MultiMap;
import edu.ucla.sspace.util.primitive.IntSet;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;







































public class GenericEdgeSet<T extends Edge>
  extends AbstractSet<T>
  implements EdgeSet<T>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final int rootVertex;
  private final MultiMap<Integer, T> vertexToEdges;
  
  public GenericEdgeSet(int rootVertex)
  {
    this.rootVertex = rootVertex;
    vertexToEdges = new HashMultiMap();
  }
  


  public boolean add(T e)
  {
    return ((e.from() == rootVertex) && (vertexToEdges.put(Integer.valueOf(e.to()), e))) || (
      (e.to() == rootVertex) && (vertexToEdges.put(Integer.valueOf(e.from()), e)));
  }
  



  public IntSet connected()
  {
    throw new Error();
  }
  


  public boolean connects(int vertex)
  {
    return vertexToEdges.containsKey(Integer.valueOf(vertex));
  }
  



  public int disconnect(int vertex)
  {
    Set<T> edges = vertexToEdges.remove(Integer.valueOf(vertex));
    return edges == null ? 0 : edges.size();
  }
  


  public boolean contains(Object o)
  {
    if ((o instanceof Edge)) {
      Edge e = (Edge)o;
      return ((e.from() == rootVertex) && (vertexToEdges.containsMapping(Integer.valueOf(e.to()), e))) || (
        (e.to() == rootVertex) && (vertexToEdges.containsMapping(Integer.valueOf(e.from()), e)));
    }
    return false;
  }
  


  public GenericEdgeSet<T> copy(IntSet vertices)
  {
    GenericEdgeSet<T> copy = new GenericEdgeSet(rootVertex);
    for (Map.Entry<Integer, Set<T>> e : vertexToEdges.asMap().entrySet()) {
      if (vertices.contains(e.getKey())) {
        vertexToEdges.putMany((Integer)e.getKey(), (Collection)e.getValue());
      }
    }
    return copy;
  }
  


  public Set<T> getEdges(int vertex)
  {
    Set<T> s = vertexToEdges.get(Integer.valueOf(vertex));
    return s == null ? 
      Collections.emptySet() : 
      s;
  }
  


  public int getRoot()
  {
    return rootVertex;
  }
  


  public Iterator<T> iterator()
  {
    return vertexToEdges.values().iterator();
  }
  


  public boolean remove(Object o)
  {
    if ((o instanceof Edge)) {
      Edge e = (Edge)o;
      return ((e.from() == rootVertex) && (vertexToEdges.remove(Integer.valueOf(e.to()), e))) || (
        (e.to() == rootVertex) && (vertexToEdges.remove(Integer.valueOf(e.from()), e)));
    }
    return false;
  }
  


  public int size()
  {
    return vertexToEdges.range();
  }
}
