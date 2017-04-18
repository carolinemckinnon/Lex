package edu.ucla.sspace.graph;

public abstract interface DirectedTypedEdge<T>
  extends DirectedEdge, TypedEdge<T>
{
  public abstract boolean equals(Object paramObject);
}
