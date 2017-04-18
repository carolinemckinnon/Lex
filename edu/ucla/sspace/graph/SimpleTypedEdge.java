package edu.ucla.sspace.graph;

import java.io.Serializable;
























public class SimpleTypedEdge<T>
  implements TypedEdge<T>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final int from;
  private final int to;
  private final T type;
  
  public SimpleTypedEdge(T edgeType, int from, int to)
  {
    type = edgeType;
    if (type == null)
      throw new NullPointerException("edge type cannot be null");
    this.from = from;
    this.to = to;
  }
  



  public <E extends Edge> E clone(int from, int to)
  {
    return new SimpleTypedEdge(type, from, to);
  }
  


  public T edgeType()
  {
    return type;
  }
  




  public boolean equals(Object o)
  {
    if ((o instanceof Edge)) {
      TypedEdge<?> e = (TypedEdge)o;
      if (e.edgeType().equals(type))
      {
        if (((e.from() == from) && (e.to() == to)) || (
          (e.from() == to) && (e.to() == from)))
          return true; } return false;
    }
    


    return false;
  }
  
  public int hashCode() {
    return from ^ to;
  }
  



  public <E extends Edge> E flip()
  {
    return new SimpleTypedEdge(type, to, from);
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
    return "(" + from + "<-->" + to + "):" + type;
  }
}
