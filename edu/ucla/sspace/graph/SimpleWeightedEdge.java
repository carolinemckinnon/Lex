package edu.ucla.sspace.graph;

import java.io.Serializable;




























public class SimpleWeightedEdge
  implements WeightedEdge, Serializable
{
  private static final long serialVersionUID = 1L;
  private final int from;
  private final int to;
  private final double weight;
  
  public SimpleWeightedEdge(int from, int to, double weight)
  {
    this.from = from;
    this.to = to;
    this.weight = weight;
  }
  



  public <T extends Edge> T clone(int from, int to)
  {
    return new SimpleWeightedEdge(from, to, weight);
  }
  



  public boolean equals(Object o)
  {
    if ((o instanceof WeightedEdge)) {
      WeightedEdge e = (WeightedEdge)o;
      return (e.weight() == weight) && (
        ((e.from() == from) && (e.to() == to)) || (
        (e.to() == from) && (e.from() == to)));
    }
    return false;
  }
  


  public int hashCode()
  {
    return from ^ to;
  }
  



  public <T extends Edge> T flip()
  {
    return new SimpleWeightedEdge(to, from, weight);
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
    return "(" + from + "<-->" + to + ")[" + weight + "]";
  }
  


  public double weight()
  {
    return weight;
  }
}
