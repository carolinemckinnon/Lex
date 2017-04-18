package edu.ucla.sspace.graph;

import edu.ucla.sspace.util.primitive.IntSet;
import java.util.Set;

public abstract interface DirectedGraph<E extends DirectedEdge>
  extends Graph<E>
{
  public abstract DirectedGraph<E> copy(Set<Integer> paramSet);
  
  public abstract Set<E> getEdges(int paramInt1, int paramInt2);
  
  public abstract int inDegree(int paramInt);
  
  public abstract Set<E> inEdges(int paramInt);
  
  public abstract int outDegree(int paramInt);
  
  public abstract Set<E> outEdges(int paramInt);
  
  public abstract IntSet predecessors(int paramInt);
  
  public abstract IntSet successors(int paramInt);
  
  public abstract DirectedGraph<E> subgraph(Set<Integer> paramSet);
}
