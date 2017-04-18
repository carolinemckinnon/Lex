package edu.ucla.sspace.graph.isomorphism;

import edu.ucla.sspace.graph.Edge;
import edu.ucla.sspace.graph.Graph;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;















































public class IsomorphicSet<G extends Graph<? extends Edge>>
  extends AbstractSet<G>
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private final List<G> graphs;
  private final IsomorphismTester tester;
  
  public IsomorphicSet()
  {
    this(new VF2IsomorphismTester());
  }
  



  public IsomorphicSet(IsomorphismTester tester)
  {
    graphs = new ArrayList();
    this.tester = tester;
  }
  



  public IsomorphicSet(Collection<? extends G> graphs)
  {
    this();
    addAll(graphs);
  }
  




  public IsomorphicSet(IsomorphismTester tester, Collection<? extends G> graphs)
  {
    this(tester);
    addAll(graphs);
  }
  
  public boolean add(G graph) {
    for (G g : graphs)
      if (tester.areIsomorphic(g, graph))
        return false;
    graphs.add(graph);
    return true;
  }
  
  public boolean contains(Object o) {
    if (!(o instanceof Graph))
      return false;
    Graph<? extends Edge> graph = (Graph)o;
    for (G g : graphs)
      if (tester.areIsomorphic(g, graph))
        return true;
    return false;
  }
  
  public Iterator<G> iterator() {
    return graphs.iterator();
  }
  
  public boolean remove(Object o) {
    if (!(o instanceof Graph))
      return false;
    Graph<? extends Edge> graph = (Graph)o;
    Iterator<G> iter = graphs.iterator();
    while (iter.hasNext()) {
      G g = (Graph)iter.next();
      if (tester.areIsomorphic(g, graph)) {
        iter.remove();
        return true;
      }
    }
    return false;
  }
  
  public int size() {
    return graphs.size();
  }
}
