package edu.ucla.sspace.graph;

import java.util.Set;

public abstract interface Multigraph<T, E extends TypedEdge<T>>
  extends Graph<E>
{
  public abstract void clearEdges(T paramT);
  
  public abstract boolean contains(int paramInt1, int paramInt2, T paramT);
  
  public abstract Multigraph<T, E> copy(Set<Integer> paramSet);
  
  public abstract Set<E> edges();
  
  public abstract Set<E> edges(T paramT);
  
  public abstract Set<T> edgeTypes();
  
  public abstract Set<E> getAdjacencyList(int paramInt);
  
  public abstract Set<E> getEdges(int paramInt1, int paramInt2);
  
  public abstract Multigraph<T, E> subgraph(Set<Integer> paramSet);
  
  public abstract Multigraph<T, E> subgraph(Set<Integer> paramSet, Set<T> paramSet1);
}
