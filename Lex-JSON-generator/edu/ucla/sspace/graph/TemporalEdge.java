package edu.ucla.sspace.graph;

public abstract interface TemporalEdge
  extends Edge
{
  public abstract boolean equals(Object paramObject);
  
  public abstract long time();
}
