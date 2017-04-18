package edu.ucla.sspace.graph.isomorphism;

import edu.ucla.sspace.graph.Edge;
import edu.ucla.sspace.graph.Graph;
import edu.ucla.sspace.util.Pair;
import edu.ucla.sspace.util.primitive.IntSet;
import gnu.trove.TDecorators;
import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;































public abstract class AbstractIsomorphismTester
  implements IsomorphismTester
{
  public AbstractIsomorphismTester() {}
  
  public boolean areIsomorphic(Graph<? extends Edge> g1, Graph<? extends Edge> g2)
  {
    Graph<? extends Edge> g1mapped = remap(g1, null);
    Graph<? extends Edge> g2mapped = remap(g2, null);
    State state = makeInitialState(g1mapped, g2mapped);
    return match(state);
  }
  






  public Map<Integer, Integer> findIsomorphism(Graph<? extends Edge> g1, Graph<? extends Edge> g2)
  {
    TIntIntHashMap g1vMap = new TIntIntHashMap(g1.order());
    TIntIntHashMap g2vMap = new TIntIntHashMap(g2.order());
    


    Graph<? extends Edge> g1mapped = remap(g1, g1vMap);
    Graph<? extends Edge> g2mapped = remap(g2, g2vMap);
    State state = makeInitialState(g1mapped, g2mapped);
    
    Map<Integer, Integer> isoMapping = new HashMap();
    

    if (match(state, isoMapping))
    {

      TIntIntMap fixedIsoMapping = new TIntIntHashMap(isoMapping.size());
      for (Map.Entry<Integer, Integer> e : isoMapping.entrySet()) {
        int v1 = ((Integer)e.getKey()).intValue();
        v1 = g1vMap.isEmpty() ? v1 : g1vMap.get(v1);
        int v2 = ((Integer)e.getValue()).intValue();
        v2 = g2vMap.isEmpty() ? v2 : g2vMap.get(v2);
        fixedIsoMapping.put(v1, v2);
      }
      return TDecorators.wrap(fixedIsoMapping);
    }
    
    return Collections.emptyMap();
  }
  




  protected abstract State makeInitialState(Graph<? extends Edge> paramGraph1, Graph<? extends Edge> paramGraph2);
  




  private boolean match(State s)
  {
    if (s.isGoal()) {
      return true;
    }
    if (s.isDead()) {
      return false;
    }
    int n1 = -1;int n2 = -1;
    Pair<Integer> next = null;
    boolean found = false;
    while ((!found) && ((next = s.nextPair(n1, n2)) != null)) {
      n1 = ((Integer)x).intValue();
      n2 = ((Integer)y).intValue();
      if (s.isFeasiblePair(n1, n2)) {
        State copy = s.copy();
        copy.addPair(n1, n2);
        found = match(copy);
        

        if (!found)
          copy.backTrack();
      }
    }
    return found;
  }
  



  private boolean match(State s, Map<Integer, Integer> isoMap)
  {
    if (s.isGoal()) {
      return true;
    }
    if (s.isDead()) {
      return false;
    }
    int n1 = -1;int n2 = -1;
    Pair<Integer> next = null;
    boolean found = false;
    while ((!found) && ((next = s.nextPair(n1, n2)) != null)) {
      n1 = ((Integer)x).intValue();
      n2 = ((Integer)y).intValue();
      if (s.isFeasiblePair(n1, n2)) {
        State copy = s.copy();
        copy.addPair(n1, n2);
        found = match(copy, isoMap);
        
        if (found) {
          isoMap.putAll(copy.getVertexMapping());
        }
        else
          copy.backTrack();
      }
    }
    return found;
  }
  








  private <E extends Edge> Graph<E> remap(Graph<E> g, TIntIntHashMap rvMap)
  {
    int order = g.order();
    boolean isContiguous = true;
    for (Iterator localIterator1 = g.vertices().iterator(); localIterator1.hasNext();) { int i = ((Integer)localIterator1.next()).intValue();
      if (i >= order) {
        isContiguous = false;
        break;
      }
    }
    if (isContiguous) {
      return g;
    }
    
    TIntIntMap vMap = new TIntIntHashMap(g.order());
    int j = 0;
    for (Iterator localIterator2 = g.vertices().iterator(); localIterator2.hasNext();) { int i = ((Integer)localIterator2.next()).intValue();
      vMap.put(i, j++);
    }
    Iterator localIterator3;
    if (rvMap != null)
    {


      int k = 0;
      for (localIterator3 = g.vertices().iterator(); localIterator3.hasNext();) { int i = ((Integer)localIterator3.next()).intValue();
        rvMap.put(k++, i);
      }
    }
    
    Graph<E> copy = g.copy(Collections.emptySet());
    for (int i = 0; i < order; i++)
      copy.add(i);
    for (Object e : g.edges()) {
      copy.add(((Edge)e).clone(vMap.get(((Edge)e).from()), vMap.get(((Edge)e).to())));
    }
    return copy;
  }
}
