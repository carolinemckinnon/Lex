package edu.ucla.sspace.graph.isomorphism;

import edu.ucla.sspace.graph.Edge;
import edu.ucla.sspace.graph.Graph;
import edu.ucla.sspace.util.CombinedIterator;
import edu.ucla.sspace.util.CombinedSet;
import edu.ucla.sspace.util.Counter;
import edu.ucla.sspace.util.Pair;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;











































public class IsomorphicGraphCounter<G extends Graph<? extends Edge>>
  implements Counter<G>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final IsomorphismTester isoTest;
  private final Map<Pair<Integer>, Map<G, Integer>> orderAndSizeToGraphs;
  private int sum;
  private final boolean allowNewMotifs;
  
  public IsomorphicGraphCounter()
  {
    this(new VF2IsomorphismTester());
  }
  





  public IsomorphicGraphCounter(IsomorphismTester isoTest)
  {
    this.isoTest = isoTest;
    orderAndSizeToGraphs = new HashMap();
    sum = 0;
    allowNewMotifs = true;
  }
  



  public IsomorphicGraphCounter(Collection<? extends G> motifs)
  {
    isoTest = new VF2IsomorphismTester();
    orderAndSizeToGraphs = new HashMap();
    sum = 0;
    allowNewMotifs = false;
    
    for (G g : motifs) {
      addInitial(g);
    }
  }
  




  public void add(Counter<? extends G> c)
  {
    for (Map.Entry<? extends G, Integer> e : c) {
      count((Graph)e.getKey(), ((Integer)e.getValue()).intValue());
    }
  }
  


  public void addInitial(G g)
  {
    Pair<Integer> orderAndSize = new Pair(Integer.valueOf(g.order()), Integer.valueOf(g.size()));
    Map<G, Integer> graphs = (Map)orderAndSizeToGraphs.get(orderAndSize);
    if (graphs == null) {
      graphs = new HashMap();
      orderAndSizeToGraphs.put(orderAndSize, graphs);
      graphs.put(g, Integer.valueOf(0));
    }
    else {
      for (Map.Entry<G, Integer> e : graphs.entrySet()) {
        if (isoTest.areIsomorphic(g, (Graph)e.getKey()))
          return;
      }
      graphs.put(g, Integer.valueOf(0));
    }
  }
  


  public int count(G g)
  {
    return count(g, 1);
  }
  







  public int count(G g, int count)
  {
    if (count < 1)
      throw new IllegalArgumentException("Count must be positive");
    sum += count;
    Pair<Integer> orderAndSize = new Pair(Integer.valueOf(g.order()), Integer.valueOf(g.size()));
    Map<G, Integer> graphs = (Map)orderAndSizeToGraphs.get(orderAndSize);
    if (graphs == null)
    {

      if (!allowNewMotifs)
        return 0;
      graphs = new HashMap();
      orderAndSizeToGraphs.put(orderAndSize, graphs);
      graphs.put(g, Integer.valueOf(count));
      return count;
    }
    
    for (Map.Entry<G, Integer> e : graphs.entrySet()) {
      if (isoTest.areIsomorphic(g, (Graph)e.getKey())) {
        int newCount = ((Integer)e.getValue()).intValue() + count;
        e.setValue(Integer.valueOf(newCount));
        return newCount;
      }
    }
    
    if (allowNewMotifs) {
      graphs.put(g, Integer.valueOf(count));
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
    Pair<Integer> orderAndSize = new Pair(Integer.valueOf(g.order()), Integer.valueOf(g.size()));
    Map<G, Integer> graphs = (Map)orderAndSizeToGraphs.get(orderAndSize);
    if (graphs == null)
      return 0;
    for (Map.Entry<G, Integer> e : graphs.entrySet()) {
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
    List<Set<G>> sets = new ArrayList(orderAndSizeToGraphs.size());
    for (Map<G, Integer> m : orderAndSizeToGraphs.values())
      sets.add(m.keySet());
    return new CombinedSet(sets);
  }
  


  public Iterator<Map.Entry<G, Integer>> iterator()
  {
    List<Iterator<Map.Entry<G, Integer>>> iters = 
      new ArrayList(orderAndSizeToGraphs.size());
    for (Map<G, Integer> m : orderAndSizeToGraphs.values())
      iters.add(m.entrySet().iterator());
    return new CombinedIterator(iters);
  }
  


  public G max()
  {
    int maxCount = -1;
    G max = null;
    Iterator localIterator2; for (Iterator localIterator1 = orderAndSizeToGraphs.values().iterator(); localIterator1.hasNext(); 
        localIterator2.hasNext())
    {
      Map<G, Integer> m = (Map)localIterator1.next();
      localIterator2 = m.entrySet().iterator(); continue;Map.Entry<G, Integer> e = (Map.Entry)localIterator2.next();
      if (((Integer)e.getValue()).intValue() > maxCount) {
        maxCount = ((Integer)e.getValue()).intValue();
        max = (Graph)e.getKey();
      }
    }
    
    return max;
  }
  


  public G min()
  {
    int minCount = Integer.MAX_VALUE;
    G min = null;
    Iterator localIterator2; for (Iterator localIterator1 = orderAndSizeToGraphs.values().iterator(); localIterator1.hasNext(); 
        localIterator2.hasNext())
    {
      Map<G, Integer> m = (Map)localIterator1.next();
      localIterator2 = m.entrySet().iterator(); continue;Map.Entry<G, Integer> e = (Map.Entry)localIterator2.next();
      if (((Integer)e.getValue()).intValue() < minCount) {
        minCount = ((Integer)e.getValue()).intValue();
        min = (Graph)e.getKey();
      }
    }
    
    return min;
  }
  


  public void reset()
  {
    orderAndSizeToGraphs.clear();
    sum = 0;
  }
  


  public int size()
  {
    int sz = 0;
    for (Map<G, Integer> m : orderAndSizeToGraphs.values())
      sz += m.size();
    return sz;
  }
  


  public int sum()
  {
    return sum;
  }
}
