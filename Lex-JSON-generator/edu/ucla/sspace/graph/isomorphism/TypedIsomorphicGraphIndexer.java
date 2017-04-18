package edu.ucla.sspace.graph.isomorphism;

import edu.ucla.sspace.graph.Graph;
import edu.ucla.sspace.graph.Multigraph;
import edu.ucla.sspace.graph.TypedEdge;
import edu.ucla.sspace.util.Indexer;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

































public class TypedIsomorphicGraphIndexer<T, G extends Multigraph<T, ? extends TypedEdge<T>>>
  implements Indexer<G>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final IsomorphismTester isoTest;
  private final Map<G, Integer> graphIndices;
  
  public TypedIsomorphicGraphIndexer()
  {
    isoTest = new TypedVF2IsomorphismTester();
    graphIndices = new HashMap();
  }
  
  public TypedIsomorphicGraphIndexer(Collection<? extends G> c) {
    this();
    for (G g : c) {
      index(g);
    }
  }
  

  public void clear()
  {
    graphIndices.clear();
  }
  


  public boolean contains(G g)
  {
    for (Map.Entry<G, Integer> e : graphIndices.entrySet()) {
      if (isoTest.areIsomorphic(g, (Graph)e.getKey()))
        return true;
    }
    return false;
  }
  


  public int find(G g)
  {
    for (Map.Entry<G, Integer> e : graphIndices.entrySet()) {
      if (isoTest.areIsomorphic(g, (Graph)e.getKey()))
        return ((Integer)e.getValue()).intValue();
    }
    return -1;
  }
  


  public int highestIndex()
  {
    return graphIndices.size() - 1;
  }
  


  public int index(G g)
  {
    for (Map.Entry<G, Integer> e : graphIndices.entrySet()) {
      if (isoTest.areIsomorphic(g, (Graph)e.getKey())) {
        return ((Integer)e.getValue()).intValue();
      }
    }
    int index = graphIndices.size();
    graphIndices.put(g, Integer.valueOf(index));
    return index;
  }
  


  public boolean indexAll(Collection<G> graphs)
  {
    boolean modified = false;
    
    for (G g : graphs) {
      for (Map.Entry<G, Integer> e : graphIndices.entrySet()) {
        if (isoTest.areIsomorphic(g, (Graph)e.getKey())) {
          break;
        }
      }
      int index = graphIndices.size();
      graphIndices.put(g, Integer.valueOf(index));
      modified = true;
    }
    return modified;
  }
  


  public Set<G> items()
  {
    return Collections.unmodifiableSet(graphIndices.keySet());
  }
  


  public Iterator<Map.Entry<G, Integer>> iterator()
  {
    return graphIndices.entrySet().iterator();
  }
  


  public G lookup(int index)
  {
    throw new Error();
  }
  


  public Map<Integer, G> mapping()
  {
    throw new Error();
  }
  


  public int size()
  {
    return graphIndices.size();
  }
}
