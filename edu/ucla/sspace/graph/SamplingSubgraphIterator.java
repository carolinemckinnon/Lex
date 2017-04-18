package edu.ucla.sspace.graph;

import edu.ucla.sspace.common.Statistics;
import edu.ucla.sspace.util.primitive.IntSet;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;































































































public class SamplingSubgraphIterator<T extends Edge>
  implements Iterator<Graph<T>>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final Graph<T> g;
  private final int subgraphSize;
  private Iterator<Integer> vertexIter;
  private Queue<Graph<T>> nextSubgraphs;
  private final double[] traversalProbabilitiesAtDepth;
  
  public SamplingSubgraphIterator(Graph<T> g, int subgraphSize, double[] traversalProbabilitiesAtDepth)
  {
    this.g = g;
    this.subgraphSize = subgraphSize;
    if (g == null)
      throw new NullPointerException();
    if (subgraphSize < 1)
      throw new IllegalArgumentException("size must be positive");
    if (subgraphSize > g.order()) {
      throw new IllegalArgumentException("size must not be greater than the number of vertices in the graph");
    }
    if (traversalProbabilitiesAtDepth.length != subgraphSize) {
      throw new IllegalArgumentException("must specify the probability of traversal at each depth; i.e., one propability for each subgraph size");
    }
    
    this.traversalProbabilitiesAtDepth = 
      Arrays.copyOf(traversalProbabilitiesAtDepth, 
      traversalProbabilitiesAtDepth.length);
    
    for (int i = 0; i < this.traversalProbabilitiesAtDepth.length; i++) {
      double prob = this.traversalProbabilitiesAtDepth[i];
      if ((prob <= 0.0D) || (prob > 1.0D))
        throw new IllegalArgumentException("Invalid probability at depth " + 
          i + ": " + prob + "; probabilities must all " + 
          "be in the range (0, 1]");
    }
    vertexIter = g.vertices().iterator();
    nextSubgraphs = new ArrayDeque();
    advance();
  }
  




  private void advance()
  {
    while ((nextSubgraphs.isEmpty()) && (vertexIter.hasNext())) {
      Integer nextVertex = (Integer)vertexIter.next();
      
      Set<Integer> extension = new HashSet();
      for (Integer v : g.getNeighbors(nextVertex.intValue()))
        if (v.intValue() > nextVertex.intValue())
          extension.add(v);
      Set<Integer> subgraph = new HashSet();
      subgraph.add(nextVertex);
      extendSubgraph(subgraph, extension, nextVertex);
    }
  }
  












  private void extendSubgraph(Set<Integer> subgraph, Set<Integer> extension, Integer v)
  {
    if (subgraph.size() == subgraphSize) {
      Graph<T> sub = g.copy(subgraph);
      nextSubgraphs.add(sub);
      return;
    }
    


    int depth = subgraph.size();
    double explorationProbability = traversalProbabilitiesAtDepth[depth];
    int numChildren = extension.size();
    BitSet childrenToExplore = null;
    



    if (explorationProbability == 1.0D) {
      childrenToExplore = new BitSet(numChildren);
      
      childrenToExplore.set(0, numChildren);


    }
    else if (numChildren == 0) {
      childrenToExplore = new BitSet(numChildren);
    }
    else
    {
      int numChildrenToExplore = 
        Math.random() <= numChildren * explorationProbability - 
        Math.floor(numChildren * explorationProbability) ? 
        (int)Math.ceil(numChildren * explorationProbability) : 
        (int)Math.floor(numChildren * explorationProbability);
      




      childrenToExplore = 
        Statistics.randomDistribution(numChildrenToExplore, numChildren);
    }
    
    int child = 0;
    Iterator<Integer> iter = extension.iterator();
    while (extension.size() > 0)
    {
      Integer w = (Integer)iter.next();
      iter.remove();
      


      if (childrenToExplore.get(child++))
      {








        Set<Integer> nextExtension = new HashSet(extension);
        
        for (Integer n : g.getNeighbors(w.intValue()))
        {

          if ((n.intValue() > v.intValue()) && (!subgraph.contains(n)))
          {


            for (Iterator localIterator2 = subgraph.iterator(); localIterator2.hasNext();) { int inCur = ((Integer)localIterator2.next()).intValue();
              


              if (g.getNeighbors(inCur).contains(n)) {
                break;
              }
            }
            
            nextExtension.add(n);
          } }
        Set<Integer> nextSubgraph = new HashSet(subgraph);
        nextSubgraph.add(w);
        
        extendSubgraph(nextSubgraph, nextExtension, v);
      }
    }
  }
  

  public boolean hasNext()
  {
    return !nextSubgraphs.isEmpty();
  }
  


  public Graph<T> next()
  {
    if (nextSubgraphs.isEmpty())
      throw new NoSuchElementException();
    Graph<T> next = (Graph)nextSubgraphs.poll();
    


    if (nextSubgraphs.isEmpty())
      advance();
    return next;
  }
  


  public void remove()
  {
    throw new UnsupportedOperationException(
      "Cannot remove subgraphs during iteration");
  }
}
