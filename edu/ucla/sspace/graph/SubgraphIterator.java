package edu.ucla.sspace.graph;

import edu.ucla.sspace.util.LoggerUtil;
import edu.ucla.sspace.util.primitive.IntIterator;
import edu.ucla.sspace.util.primitive.IntSet;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.set.hash.TIntHashSet;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Logger;





























































public class SubgraphIterator<E extends Edge, G extends Graph<E>>
  implements Iterator<G>, Serializable
{
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = Logger.getLogger(SubgraphIterator.class.getName());
  





  private final G g;
  





  private final int subgraphSize;
  





  private IntIterator vertexIter;
  




  private SubgraphIterator<E, G>.Extension ext;
  




  private G next;
  





  public SubgraphIterator(G g, int subgraphSize)
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
    vertexIter = g.vertices().iterator();
    advance();
  }
  



  private void advance()
  {
    next = null;
    while (next == null)
    {
      if (ext != null) {
        next = ext.next();
        if (next != null)
          return;
      }
      if (!vertexIter.hasNext()) {
        return;
      }
      

      int nextVertex = vertexIter.nextInt();
      LoggerUtil.veryVerbose(LOGGER, "Loading next round of subgraphs starting from vertex %d", new Object[] {
        Integer.valueOf(nextVertex) });
      ext = new Extension(nextVertex);
    }
  }
  
  public boolean hasNext() {
    return next != null;
  }
  
  public G next() {
    if (!hasNext())
      throw new NoSuchElementException();
    G n = next;
    advance();
    return n;
  }
  


  public void remove()
  {
    throw new UnsupportedOperationException(
      "Cannot remove subgraphs during iteration");
  }
  






  class Extension
  {
    private final int v;
    





    private final Deque<Integer> vertsInSubgraph;
    




    private final Deque<TIntHashSet> extensionStack;
    





    public Extension(int v)
    {
      this.v = v;
      vertsInSubgraph = new ArrayDeque();
      extensionStack = new ArrayDeque();
      
      IntSet neighbors = g.getNeighbors(v);
      TIntHashSet extension = new TIntHashSet();
      IntIterator iter = neighbors.iterator();
      while (iter.hasNext()) {
        int u = iter.nextInt();
        if (u > v) {
          extension.add(u);
        }
      }
      vertsInSubgraph.push(Integer.valueOf(v));
      extensionStack.push(extension);
    }
    




    public G next()
    {
      TIntHashSet curExtension = (TIntHashSet)extensionStack.peek();
      if (curExtension == null) {
        return null;
      }
      while ((extensionStack.size() < subgraphSize - 1) && 
        (!extensionStack.isEmpty())) {
        loadNextExtension();
        
        curExtension = (TIntHashSet)extensionStack.peek();
        if (curExtension.isEmpty()) {
          extensionStack.pop();
        }
      }
      

      curExtension = (TIntHashSet)extensionStack.peek();
      if (curExtension == null) {
        return null;
      }
      TIntIterator iter = curExtension.iterator();
      int w = iter.next();
      iter.remove();
      
      vertsInSubgraph.push(Integer.valueOf(w));
      






      G next = g.copy(new HashSet(vertsInSubgraph));
      


      vertsInSubgraph.pop();
      


      if (curExtension.isEmpty()) {
        extensionStack.pop();
        vertsInSubgraph.pop();
      }
      
      return next;
    }
    









    private void loadNextExtension()
    {
      TIntHashSet extension = (TIntHashSet)extensionStack.peek();
      if (extension == null) {
        throw new IllegalStateException();
      }
      if (extension.isEmpty()) {
        return;
      }
      
      TIntIterator iter = extension.iterator();
      Integer w = Integer.valueOf(iter.next());
      iter.remove();
      







      TIntHashSet nextExtension = new TIntHashSet(extension);
      
      for (Integer n : g.getNeighbors(w.intValue()))
      {

        if ((n.intValue() > v) && (!vertsInSubgraph.contains(n)))
        {


          Iterator<Integer> subIter = vertsInSubgraph.iterator();
          while (subIter.hasNext()) {
            int inCur = ((Integer)subIter.next()).intValue();
            


            if (g.contains(inCur, n.intValue())) {
              break;
            }
          }
          
          nextExtension.add(n.intValue());
        }
      }
      vertsInSubgraph.push(w);
      extensionStack.push(nextExtension);
    }
  }
}
