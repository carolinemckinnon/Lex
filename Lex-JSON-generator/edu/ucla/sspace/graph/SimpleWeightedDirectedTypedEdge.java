package edu.ucla.sspace.graph;

import java.io.Serializable;



























public class SimpleWeightedDirectedTypedEdge<T>
  implements WeightedDirectedTypedEdge<T>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final int from;
  private final int to;
  private final T type;
  private final double weight;
  
  public SimpleWeightedDirectedTypedEdge(T edgeType, int from, int to, double weight)
  {
    type = edgeType;
    if (type == null)
      throw new NullPointerException("edge type cannot be null");
    this.from = from;
    this.to = to;
    this.weight = weight;
  }
  



  public <E extends Edge> E clone(int from, int to)
  {
    return new SimpleWeightedDirectedTypedEdge(
      type, from, to, weight);
  }
  


  public T edgeType()
  {
    return type;
  }
  




  public boolean equals(Object o)
  {
    if ((o instanceof Edge)) {
      WeightedDirectedTypedEdge<?> e = (WeightedDirectedTypedEdge)o;
      return (e.weight() == weight) && 
        (e.from() == from) && 
        (e.to() == to) && 
        (e.edgeType().equals(type));
    }
    return false;
  }
  
  public int hashCode() {
    return from ^ to;
  }
  



  public <E extends Edge> E flip()
  {
    return new SimpleWeightedDirectedTypedEdge(
      type, to, from, weight);
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
    return "(" + from + "<-->" + to + ")[" + weight + "]:" + type;
  }
  


  public double weight()
  {
    return weight;
  }
}
