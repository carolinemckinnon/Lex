package edu.ucla.sspace.graph;

import java.util.Set;

public abstract interface WeightedGraph<E extends WeightedEdge>
  extends Graph<E>
{
  public abstract boolean add(E paramE);
  
  public abstract WeightedGraph<E> copy(Set<Integer> paramSet);
  
  public abstract Set<E> edges();
  
  public abstract Set<E> getAdjacencyList(int paramInt);
  
  public abstract Set<E> getEdges(int paramInt1, int paramInt2);
  
  public abstract double strength(int paramInt);
  
  public abstract WeightedGraph<E> subgraph(Set<Integer> paramSet);
}
