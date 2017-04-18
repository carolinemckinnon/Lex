package edu.ucla.sspace.graph;

import java.io.Serializable;






















public class LabeledEdge
  implements Edge, Serializable
{
  private static final long serialVersionUID = 1L;
  private final int from;
  private final int to;
  private final String fromLabel;
  private final String toLabel;
  
  public LabeledEdge(int from, int to, String fromLabel, String toLabel)
  {
    this.from = from;
    this.to = to;
    this.fromLabel = fromLabel;
    this.toLabel = toLabel;
  }
  
  public <T extends Edge> T clone(int from, int to)
  {
    return new LabeledEdge(from, to, fromLabel, toLabel);
  }
  
  public boolean equals(Object o) {
    if ((o instanceof Edge)) {
      Edge e = (Edge)o;
      return ((e.from() == from) && (e.to() == to)) || (
        (e.to() == from) && (e.from() == to));
    }
    return false;
  }
  
  public int hashCode() {
    return from ^ to;
  }
  
  public <T extends Edge> T flip()
  {
    return new LabeledEdge(to, from, toLabel, fromLabel);
  }
  
  public int from() {
    return from;
  }
  
  public int to() {
    return to;
  }
  
  public String toString() {
    return "(" + fromLabel + "<-->" + toLabel + ")";
  }
}
