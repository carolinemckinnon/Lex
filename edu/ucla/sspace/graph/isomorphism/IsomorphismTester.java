package edu.ucla.sspace.graph.isomorphism;

import edu.ucla.sspace.graph.Edge;
import edu.ucla.sspace.graph.Graph;
import java.util.Map;

public abstract interface IsomorphismTester
{
  public abstract boolean areIsomorphic(Graph<? extends Edge> paramGraph1, Graph<? extends Edge> paramGraph2);
  
  public abstract Map<Integer, Integer> findIsomorphism(Graph<? extends Edge> paramGraph1, Graph<? extends Edge> paramGraph2);
}
