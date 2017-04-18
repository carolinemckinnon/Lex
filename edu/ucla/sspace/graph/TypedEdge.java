package edu.ucla.sspace.graph;

public abstract interface TypedEdge<T>
  extends Edge
{
  public abstract T edgeType();
  
  public abstract boolean equals(Object paramObject);
}
