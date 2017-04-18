package edu.ucla.sspace.graph.isomorphism;

import edu.ucla.sspace.graph.Edge;
import edu.ucla.sspace.graph.Graph;
import edu.ucla.sspace.util.Pair;
import java.util.Map;

public abstract interface State
{
  public static final int NULL_NODE = -1;
  
  public abstract Graph<? extends Edge> getGraph1();
  
  public abstract Graph<? extends Edge> getGraph2();
  
  public abstract Pair<Integer> nextPair(int paramInt1, int paramInt2);
  
  public abstract void addPair(int paramInt1, int paramInt2);
  
  public abstract boolean isFeasiblePair(int paramInt1, int paramInt2);
  
  public abstract boolean isGoal();
  
  public abstract boolean isDead();
  
  public abstract Map<Integer, Integer> getVertexMapping();
  
  public abstract State copy();
  
  public abstract void backTrack();
}
