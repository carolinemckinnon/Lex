package edu.ucla.sspace.graph;

public abstract interface WeightedTypedEdge<T>
  extends TypedEdge, WeightedEdge
{
  public abstract boolean equals(Object paramObject);
}
