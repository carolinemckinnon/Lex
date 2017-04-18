package edu.ucla.sspace.graph;

import edu.ucla.sspace.util.primitive.IntIterator;
import edu.ucla.sspace.util.primitive.IntSet;
import edu.ucla.sspace.util.primitive.TroveIntSet;
import gnu.trove.iterator.TIntDoubleIterator;
import gnu.trove.map.TIntDoubleMap;
import gnu.trove.map.hash.TIntDoubleHashMap;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;






























































public class SparseWeightedEdgeSet
  extends AbstractSet<WeightedEdge>
  implements EdgeSet<WeightedEdge>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final int rootVertex;
  private final TIntDoubleMap edges;
  
  public SparseWeightedEdgeSet(int rootVertex)
  {
    this.rootVertex = rootVertex;
    edges = new TIntDoubleHashMap();
  }
  





  public boolean add(WeightedEdge e)
  {
    int toAdd = -1;
    if (e.from() == rootVertex) {
      toAdd = e.to();
    } else if (e.to() == rootVertex) {
      toAdd = e.from();
    } else {
      return false;
    }
    
    double w = e.weight();
    if (edges.containsKey(toAdd)) {
      double w2 = edges.put(toAdd, w);
      
      return false;
    }
    
    edges.put(toAdd, w);
    return true;
  }
  



  public IntSet connected()
  {
    return TroveIntSet.wrap(edges.keySet());
  }
  


  public boolean connects(int vertex)
  {
    return edges.containsKey(vertex);
  }
  


  public boolean contains(Object o)
  {
    if ((o instanceof WeightedEdge)) {
      WeightedEdge e = (WeightedEdge)o;
      int toFind = 0;
      if (e.to() == rootVertex) {
        toFind = e.from();
      } else if (e.from() == rootVertex)
        toFind = e.to(); else {
        return false;
      }
      return edges.get(toFind) == e.weight();
    }
    return false;
  }
  


  public SparseWeightedEdgeSet copy(IntSet vertices)
  {
    SparseWeightedEdgeSet copy = new SparseWeightedEdgeSet(rootVertex);
    if (edges.size() < vertices.size()) {
      TIntDoubleIterator iter = edges.iterator();
      while (iter.hasNext()) {
        iter.advance();
        int v = iter.key();
        if (vertices.contains(v)) {
          edges.put(v, iter.value());
        }
      }
    }
    else {
      IntIterator iter = vertices.iterator();
      while (iter.hasNext()) {
        int v = iter.nextInt();
        if (edges.containsKey(v))
          edges.put(v, edges.get(v));
      }
    }
    return copy;
  }
  


  public int disconnect(int vertex)
  {
    if (edges.containsKey(vertex)) {
      edges.remove(vertex);
      return 1;
    }
    return 0;
  }
  


  public Set<WeightedEdge> getEdges(int vertex)
  {
    return edges.containsKey(vertex) ? 
      Collections.singleton(
      new SimpleWeightedEdge(rootVertex, vertex, edges.get(vertex))) : 
      Collections.emptySet();
  }
  


  public int getRoot()
  {
    return rootVertex;
  }
  


  public Iterator<WeightedEdge> iterator()
  {
    return new WeightedEdgeIterator();
  }
  


  public boolean remove(Object o)
  {
    if ((o instanceof WeightedEdge)) {
      WeightedEdge e = (WeightedEdge)o;
      int toRemove = 0;
      if (e.to() == rootVertex) {
        toRemove = e.from();
      } else if (e.from() == rootVertex)
        toRemove = e.to(); else
        return false;
      if (edges.containsKey(toRemove)) {
        edges.remove(toRemove);
        return true;
      }
      return false;
    }
    return false;
  }
  


  public int size()
  {
    return edges.size();
  }
  


  public double sum()
  {
    double sum = 0.0D;
    TIntDoubleIterator iter = edges.iterator();
    while (iter.hasNext()) {
      iter.advance();
      sum += iter.value();
    }
    return sum;
  }
  

  private class WeightedEdgeIterator
    implements Iterator<WeightedEdge>
  {
    private TIntDoubleIterator iter;
    
    private boolean alreadyRemoved;
    

    public WeightedEdgeIterator()
    {
      iter = edges.iterator();
      alreadyRemoved = false;
    }
    
    public boolean hasNext() {
      return iter.hasNext();
    }
    
    public WeightedEdge next() {
      iter.advance();
      alreadyRemoved = false;
      return new SimpleWeightedEdge(rootVertex, iter.key(), iter.value());
    }
    
    public void remove() {
      if (alreadyRemoved) {
        throw new IllegalStateException();
      }
      try
      {
        iter.remove();
        alreadyRemoved = true;
      } catch (ArrayIndexOutOfBoundsException aioobe) {
        throw new IllegalStateException();
      }
    }
  }
}
