package edu.ucla.sspace.graph;

import java.io.Serializable;


























public class SimpleDirectedTypedEdge<T>
  implements DirectedTypedEdge<T>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final int from;
  private final int to;
  private final T type;
  
  public SimpleDirectedTypedEdge(T edgeType, int from, int to)
  {
    type = edgeType;
    this.from = from;
    this.to = to;
  }
  



  public <E extends Edge> E clone(int from, int to)
  {
    return new SimpleDirectedTypedEdge(type, from, to);
  }
  


  public T edgeType()
  {
    return type;
  }
  




  public boolean equals(Object o)
  {
    if ((o instanceof TypedEdge))
    {
      TypedEdge<?> e = (TypedEdge)o;
      return (e.edgeType().equals(type)) && 
        (e.from() == from) && 
        (e.to() == to);
    }
    return false;
  }
  
  public int hashCode() {
    return from ^ to;
  }
  



  public <E extends Edge> E flip()
  {
    return new SimpleDirectedTypedEdge(type, to, from);
  }
  


  public int from()
  {
    return from;
  }
  


  public int to()
  {
    return to;
  }
  
  public String toString() {
    return "(" + from + "->" + to + "):" + type;
  }
}
