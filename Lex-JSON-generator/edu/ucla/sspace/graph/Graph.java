package edu.ucla.sspace.graph;

import edu.ucla.sspace.util.primitive.IntSet;
import java.util.Set;

public abstract interface Graph<E extends Edge>
{
  public abstract boolean add(int paramInt);
  
  public abstract boolean add(E paramE);
  
  public abstract void clear();
  
  public abstract void clearEdges();
  
  public abstract boolean contains(int paramInt);
  
  public abstract boolean contains(int paramInt1, int paramInt2);
  
  public abstract boolean contains(Edge paramEdge);
  
  public abstract Graph<E> copy(Set<Integer> paramSet);
  
  public abstract int degree(int paramInt);
  
  public abstract Set<E> edges();
  
  public abstract Set<E> getAdjacencyList(int paramInt);
  
  public abstract IntSet getNeighbors(int paramInt);
  
  public abstract Set<E> getEdges(int paramInt1, int paramInt2);
  
  public abstract boolean hasCycles();
  
  public abstract int order();
  
  public abstract boolean remove(E paramE);
  
  public abstract boolean remove(int paramInt);
  
  public abstract int size();
  
  public abstract Graph<E> subgraph(Set<Integer> paramSet);
  
  public abstract IntSet vertices();
}
