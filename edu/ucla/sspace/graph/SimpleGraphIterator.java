package edu.ucla.sspace.graph;

import edu.ucla.sspace.util.primitive.IntPair;
import edu.ucla.sspace.util.primitive.IntSet;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;





















































public class SimpleGraphIterator<T, E extends TypedEdge<T>>
  implements Iterator<Multigraph<T, E>>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final Iterator<Multigraph<T, E>> subgraphIterator;
  private Queue<Multigraph<T, E>> next;
  
  public SimpleGraphIterator(Multigraph<T, E> g, int subgraphSize)
  {
    subgraphIterator = 
      new SubgraphIterator(g, subgraphSize);
    next = new ArrayDeque();
    advance();
  }
  




  private void advance()
  {
    if ((next.isEmpty()) && (subgraphIterator.hasNext())) {
      Multigraph<T, E> g = (Multigraph)subgraphIterator.next();
      


      boolean isSimpleAlready = true;
      List<IntPair> connected = new ArrayList();
      for (Iterator localIterator1 = g.vertices().iterator(); localIterator1.hasNext();) { int i = ((Integer)localIterator1.next()).intValue();
        for (Iterator localIterator2 = g.vertices().iterator(); localIterator2.hasNext();) { int j = ((Integer)localIterator2.next()).intValue();
          if (i == j)
            break;
          Set<E> edges = null;
          try {
            edges = g.getEdges(i, j);
          } catch (NullPointerException npe) {
            System.out.println("Bad graph? : " + g);
            throw npe;
          }
          int size = edges.size();
          if (size > 0)
            connected.add(new IntPair(i, j));
          if (size > 1) {
            isSimpleAlready = false;
          }
        }
      }
      
      if (isSimpleAlready)
      {
        next.add(g);


      }
      else
      {

        Multigraph<T, E> m = g.copy(Collections.emptySet());
        next.addAll(enumerateSimpleGraphs(g, connected, 0, m));
      }
    }
  }
  















  private Collection<Multigraph<T, E>> enumerateSimpleGraphs(Multigraph<T, E> input, List<IntPair> connected, int curPair, Multigraph<T, E> toCopy)
  {
    List<Multigraph<T, E>> simpleGraphs = new LinkedList();
    IntPair p = (IntPair)connected.get(curPair);
    
    Set<E> edges = input.getEdges(x, y);
    
    for (E e : edges)
    {
      Multigraph<T, E> m = toCopy.copy(toCopy.vertices());
      

      m.add(e);
      

      if (curPair + 1 < connected.size()) {
        simpleGraphs.addAll(
          enumerateSimpleGraphs(input, connected, curPair + 1, m));

      }
      else
      {

        simpleGraphs.add(m);
      }
    }
    return simpleGraphs;
  }
  


  public boolean hasNext()
  {
    return !next.isEmpty();
  }
  


  public Multigraph<T, E> next()
  {
    if (!hasNext())
      throw new NoSuchElementException();
    Multigraph<T, E> cur = (Multigraph)next.poll();
    if (next.isEmpty())
      advance();
    return cur;
  }
  


  public void remove()
  {
    throw new UnsupportedOperationException();
  }
}
