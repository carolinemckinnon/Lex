package edu.ucla.sspace.graph;

import edu.ucla.sspace.util.primitive.IntIterator;
import edu.ucla.sspace.util.primitive.IntSet;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;



































public class BetweennessCentrality
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  
  public BetweennessCentrality() {}
  
  public <E extends Edge> double[] compute(Graph<E> g)
  {
    if (!hasContiguousVertices(g)) {
      throw new IllegalArgumentException(
        "Vertices must be in continugous order");
    }
    double[] centralities = new double[g.order()];
    IntIterator vertexIter = g.vertices().iterator();
    Deque<Integer> S; for (; vertexIter.hasNext(); 
        





































        !S.isEmpty())
    {
      int s = vertexIter.nextInt();
      S = new ArrayDeque();
      

      List<List<Integer>> P = new ArrayList(g.order());
      for (int i = 0; i < g.order(); i++) {
        P.add(new ArrayList());
      }
      double[] sigma = new double[g.order()];
      sigma[s] = 1.0D;
      
      double[] d = new double[g.order()];
      Arrays.fill(d, -1.0D);
      d[s] = 0.0D;
      
      Queue<Integer> Q = new ArrayDeque();
      Q.add(Integer.valueOf(s));
      IntIterator neighborIter; for (; !Q.isEmpty(); 
          


          neighborIter.hasNext())
      {
        int v = ((Integer)Q.poll()).intValue();
        S.offer(Integer.valueOf(v));
        neighborIter = g.getNeighbors(v).iterator();
        continue;
        int w = neighborIter.nextInt();
        
        if (d[w] < 0.0D) {
          Q.offer(Integer.valueOf(w));
          d[v] += 1.0D;
        }
        
        if (d[w] == d[v] + 1.0D) {
          sigma[w] += sigma[v];
          ((List)P.get(w)).add(Integer.valueOf(v));
        }
      }
      
      double[] delta = new double[g.order()];
      

      continue;
      int w = ((Integer)S.pollLast()).intValue();
      for (Iterator localIterator = ((List)P.get(w)).iterator(); localIterator.hasNext();) { int v = ((Integer)localIterator.next()).intValue();
        delta[v] += sigma[v] / sigma[w] * (1.0D + delta[w]);
      }
      if (w != s) {
        centralities[w] += delta[w];
      }
    }
    
    return centralities;
  }
  
  private static boolean hasContiguousVertices(Graph<?> g) {
    int order = g.order();
    for (Iterator localIterator = g.vertices().iterator(); localIterator.hasNext();) { int v = ((Integer)localIterator.next()).intValue();
      if (v >= order)
        return false;
    }
    return true;
  }
}
