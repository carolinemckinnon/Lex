package edu.ucla.sspace.graph.isomorphism;

import edu.ucla.sspace.graph.Graph;
import edu.ucla.sspace.graph.Graphs;
import edu.ucla.sspace.graph.Multigraph;
import edu.ucla.sspace.graph.TypedEdge;
import edu.ucla.sspace.util.CombinedIterator;
import edu.ucla.sspace.util.Counter;
import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;












































public class TypedIsomorphicGraphCounter<T, G extends Multigraph<T, ? extends TypedEdge<T>>>
  implements Counter<G>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final IsomorphismTester isoTest;
  private final Map<Set<T>, LinkedList<Map.Entry<G, Integer>>> typesToGraphs;
  private int sum;
  private final boolean allowNewMotifs;
  
  public TypedIsomorphicGraphCounter()
  {
    this(new TypedVF2IsomorphismTester());
  }
  





  public TypedIsomorphicGraphCounter(IsomorphismTester isoTest)
  {
    this.isoTest = isoTest;
    typesToGraphs = new HashMap();
    sum = 0;
    allowNewMotifs = true;
  }
  



  private TypedIsomorphicGraphCounter(Collection<? extends G> motifs)
  {
    isoTest = new TypedVF2IsomorphismTester();
    typesToGraphs = new HashMap();
    sum = 0;
    allowNewMotifs = false;
    

    for (G g : motifs) {
      addInitial(g);
    }
  }
  


  public void add(Counter<? extends G> c)
  {
    for (Map.Entry<? extends G, Integer> e : c) {
      count((Multigraph)e.getKey(), ((Integer)e.getValue()).intValue());
    }
  }
  




  private void addInitial(G g)
  {
    Set<T> typeCounts = g.edgeTypes();
    LinkedList<Map.Entry<G, Integer>> graphs = (LinkedList)typesToGraphs.get(typeCounts);
    if (graphs == null) {
      graphs = new LinkedList();
      typesToGraphs.put(new HashSet(typeCounts), graphs);
    }
    
    graphs.add(new AbstractMap.SimpleEntry(g, Integer.valueOf(0)));
  }
  










  public static <T, G extends Multigraph<T, ? extends TypedEdge<T>>> TypedIsomorphicGraphCounter<T, G> asMotifs(Set<? extends G> motifs)
  {
    return new TypedIsomorphicGraphCounter(motifs);
  }
  


  public int count(G g)
  {
    return count(g, 1);
  }
  







  public int count(G g, int count)
  {
    if (count < 1) {
      throw new IllegalArgumentException("Count must be positive");
    }
    







    Multigraph<T, ? extends TypedEdge<T>> g2 = 
      g;
    g2 = (Multigraph)Graphs.pack(g2);
    g = g2;
    


    Set<T> typeCounts = g.edgeTypes();
    LinkedList<Map.Entry<G, Integer>> graphs = (LinkedList)typesToGraphs.get(typeCounts);
    if (graphs == null)
    {

      if (!allowNewMotifs)
        return 0;
      sum += count;
      graphs = new LinkedList();
      typesToGraphs.put(new HashSet(typeCounts), graphs);
      graphs.add(new AbstractMap.SimpleEntry(g, Integer.valueOf(count)));
      return count;
    }
    
    Iterator<Map.Entry<G, Integer>> iter = graphs.iterator();
    while (iter.hasNext()) {
      Map.Entry<G, Integer> e = (Map.Entry)iter.next();
      if (isoTest.areIsomorphic(g, (Graph)e.getKey())) {
        int newCount = ((Integer)e.getValue()).intValue() + count;
        e.setValue(Integer.valueOf(newCount));
        

        iter.remove();
        graphs.addFirst(e);
        sum += count;
        return newCount;
      }
    }
    
    if (allowNewMotifs) {
      sum += count;
      graphs.addFirst(new AbstractMap.SimpleEntry(g, Integer.valueOf(count)));
      return count;
    }
    
    return 0;
  }
  



  public void countAll(Collection<? extends G> c)
  {
    for (G g : c) {
      count(g);
    }
  }
  

  public int getCount(G g)
  {
    Set<T> typeCounts = g.edgeTypes();
    LinkedList<Map.Entry<G, Integer>> graphs = (LinkedList)typesToGraphs.get(typeCounts);
    if (graphs == null) {
      return 0;
    }
    for (Map.Entry<G, Integer> e : graphs) {
      if (isoTest.areIsomorphic(g, (Graph)e.getKey()))
        return ((Integer)e.getValue()).intValue();
    }
    return 0;
  }
  


  public double getFrequency(G obj)
  {
    double count = getCount(obj);
    return sum == 0 ? 0.0D : count / sum;
  }
  











  public Set<G> items()
  {
    return new Items();
  }
  


  public Iterator<Map.Entry<G, Integer>> iterator()
  {
    List<Iterator<Map.Entry<G, Integer>>> iters = 
      new ArrayList(typesToGraphs.size());
    for (LinkedList<Map.Entry<G, Integer>> list : typesToGraphs.values())
      iters.add(list.iterator());
    return new CombinedIterator(iters);
  }
  


  public G max()
  {
    int maxCount = -1;
    G max = null;
    Iterator localIterator2; for (Iterator localIterator1 = typesToGraphs.values().iterator(); localIterator1.hasNext(); 
        localIterator2.hasNext())
    {
      LinkedList<Map.Entry<G, Integer>> graphs = (LinkedList)localIterator1.next();
      localIterator2 = graphs.iterator(); continue;Map.Entry<G, Integer> e = (Map.Entry)localIterator2.next();
      if (((Integer)e.getValue()).intValue() > maxCount) {
        maxCount = ((Integer)e.getValue()).intValue();
        max = (Multigraph)e.getKey();
      }
    }
    
    return max;
  }
  


  public G min()
  {
    int minCount = Integer.MAX_VALUE;
    G min = null;
    Iterator localIterator2; for (Iterator localIterator1 = typesToGraphs.values().iterator(); localIterator1.hasNext(); 
        localIterator2.hasNext())
    {
      LinkedList<Map.Entry<G, Integer>> graphs = (LinkedList)localIterator1.next();
      localIterator2 = graphs.iterator(); continue;Map.Entry<G, Integer> e = (Map.Entry)localIterator2.next();
      if (((Integer)e.getValue()).intValue() < minCount) {
        minCount = ((Integer)e.getValue()).intValue();
        min = (Multigraph)e.getKey();
      }
    }
    
    return min;
  }
  


  public void reset()
  {
    typesToGraphs.clear();
    sum = 0;
  }
  


  public int size()
  {
    int sz = 0;
    for (LinkedList<Map.Entry<G, Integer>> m : typesToGraphs.values())
      sz += m.size();
    return sz;
  }
  


  public int sum()
  {
    return sum;
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder("{");
    Iterator localIterator2; for (Iterator localIterator1 = typesToGraphs.values().iterator(); localIterator1.hasNext(); 
        localIterator2.hasNext())
    {
      LinkedList<Map.Entry<G, Integer>> m = (LinkedList)localIterator1.next();
      localIterator2 = m.iterator(); continue;Map.Entry<G, Integer> e = (Map.Entry)localIterator2.next();
      if (((Integer)e.getValue()).intValue() > 0)
      {
        sb.append(e.getKey()).append(':').append(e.getValue()).append(' ');
      }
    }
    sb.append('}');
    return sb.toString();
  }
  
  class Items extends AbstractSet<G> {
    Items() {}
    
    public boolean contains(G graph) { Set<T> typeCounts = graph.edgeTypes();
      LinkedList<Map.Entry<G, Integer>> graphs = (LinkedList)typesToGraphs.get(typeCounts);
      if (graphs == null)
        return false;
      for (Map.Entry<G, Integer> e : graphs) {
        if (((Multigraph)e.getKey()).equals(graph))
          return true;
      }
      return false;
    }
    
    public Iterator<G> iterator() {
      return new MotifIter();
    }
    
    public int size() {
      return TypedIsomorphicGraphCounter.this.size();
    }
    
    private class MotifIter implements Iterator<G>
    {
      Iterator<LinkedList<Map.Entry<G, Integer>>> graphs;
      Iterator<Map.Entry<G, Integer>> curIter;
      
      public MotifIter()
      {
        graphs = typesToGraphs.values().iterator();
        advance();
      }
      
      private void advance() {
        while (((curIter == null) || (!curIter.hasNext())) && 
          (graphs.hasNext()))
          curIter = ((LinkedList)graphs.next()).iterator();
      }
      
      public boolean hasNext() {
        return curIter.hasNext();
      }
      
      public G next() {
        if (!hasNext())
          throw new NoSuchElementException();
        Map.Entry<G, Integer> e = (Map.Entry)curIter.next();
        advance();
        return (Multigraph)e.getKey();
      }
      
      public void remove() {
        throw new UnsupportedOperationException();
      }
    }
  }
}
