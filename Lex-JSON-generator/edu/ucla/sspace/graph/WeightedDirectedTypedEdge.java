package edu.ucla.sspace.graph;

public abstract interface WeightedDirectedTypedEdge<T>
  extends DirectedTypedEdge<T>, WeightedDirectedEdge
{
  public abstract boolean equals(Object paramObject);
}
