package edu.ucla.sspace.graph.isomorphism;

import edu.ucla.sspace.graph.Edge;
import edu.ucla.sspace.graph.Graph;




















































public class VF2IsomorphismTester
  extends AbstractIsomorphismTester
{
  private static final long SerialVersionUID = 1L;
  
  public VF2IsomorphismTester() {}
  
  protected State makeInitialState(Graph<? extends Edge> g1, Graph<? extends Edge> g2)
  {
    return new VF2State(g1, g2);
  }
}
