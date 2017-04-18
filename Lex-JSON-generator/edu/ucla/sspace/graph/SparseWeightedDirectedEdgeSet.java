package edu.ucla.sspace.graph;

import java.io.Serializable;
import java.util.AbstractSet;

public abstract class SparseWeightedDirectedEdgeSet
  extends AbstractSet<WeightedEdge>
  implements EdgeSet<WeightedEdge>, Serializable
{
  private static final long serialVersionUID = 1L;
  
  public SparseWeightedDirectedEdgeSet() {}
}
