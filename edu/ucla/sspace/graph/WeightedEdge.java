package edu.ucla.sspace.graph;

public abstract interface WeightedEdge
  extends Edge
{
  public abstract boolean equals(Object paramObject);
  
  public abstract double weight();
}
