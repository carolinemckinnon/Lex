package edu.ucla.sspace.graph;

import java.io.Serializable;























public class SimpleEdge
  implements Edge, Serializable
{
  private static final long serialVersionUID = 1L;
  private final int from;
  private final int to;
  
  public SimpleEdge(int from, int to)
  {
    this.from = from;
    this.to = to;
  }
  



  public <T extends Edge> T clone(int from, int to)
  {
    return new SimpleEdge(from, to);
  }
  



  public boolean equals(Object o)
  {
    if ((o instanceof Edge)) {
      Edge e = (Edge)o;
      return ((e.from() == from) && (e.to() == to)) || (
        (e.to() == from) && (e.from() == to));
    }
    return false;
  }
  


  public int hashCode()
  {
    return from ^ to;
  }
  



  public <T extends Edge> T flip()
  {
    return new SimpleEdge(to, from);
  }
  


  public int from()
  {
    return from;
  }
  


  public int to()
  {
    return to;
  }
  


  public String toString()
  {
    return "(" + from + "<-->" + to + ")";
  }
}
