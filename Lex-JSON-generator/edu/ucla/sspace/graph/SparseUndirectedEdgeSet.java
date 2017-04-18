package edu.ucla.sspace.graph;

import edu.ucla.sspace.util.primitive.IntIterator;
import edu.ucla.sspace.util.primitive.IntSet;
import edu.ucla.sspace.util.primitive.TroveIntSet;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;



































public class SparseUndirectedEdgeSet
  extends AbstractSet<Edge>
  implements EdgeSet<Edge>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final int rootVertex;
  private final TIntSet edges;
  
  public SparseUndirectedEdgeSet(int rootVertex)
  {
    this.rootVertex = rootVertex;
    edges = new TIntHashSet();
  }
  
  public SparseUndirectedEdgeSet(int rootVertex, int capacity) {
    this.rootVertex = rootVertex;
    edges = new TIntHashSet(capacity);
  }
  



  public boolean add(Edge e)
  {
    int toAdd = -1;
    if (e.from() == rootVertex) {
      toAdd = e.to();
    } else if (e.to() == rootVertex) {
      toAdd = e.from();
    } else
      return false;
    boolean b = edges.add(toAdd);
    return b;
  }
  


  public IntSet connected()
  {
    return TroveIntSet.wrap(edges);
  }
  



  public boolean connects(int vertex)
  {
    return edges.contains(vertex);
  }
  


  public boolean contains(Object o)
  {
    if ((o instanceof Edge)) {
      Edge e = (Edge)o;
      int toFind = 0;
      if (e.to() == rootVertex) {
        toFind = e.from();
      } else if (e.from() == rootVertex)
        toFind = e.to(); else {
        return false;
      }
      boolean b = edges.contains(toFind);
      return b;
    }
    return false;
  }
  


  public SparseUndirectedEdgeSet copy(IntSet vertices)
  {
    SparseUndirectedEdgeSet copy = new SparseUndirectedEdgeSet(rootVertex);
    if (edges.size() < vertices.size()) {
      TIntIterator iter = edges.iterator();
      while (iter.hasNext()) {
        int v = iter.next();
        if (vertices.contains(v)) {
          edges.add(v);
        }
      }
    } else {
      IntIterator iter = vertices.iterator();
      while (iter.hasNext()) {
        int v = iter.nextInt();
        if (edges.contains(v))
          edges.add(v);
      }
    }
    return copy;
  }
  


  public int disconnect(int vertex)
  {
    return edges.remove(vertex) ? 1 : 0;
  }
  


  public Set<Edge> getEdges(int vertex)
  {
    return edges.contains(vertex) ? 
      Collections.singleton(new SimpleEdge(rootVertex, vertex)) : 
      Collections.emptySet();
  }
  


  public int getRoot()
  {
    return rootVertex;
  }
  


  public Iterator<Edge> iterator()
  {
    return new EdgeIterator();
  }
  


  public boolean remove(Object o)
  {
    if ((o instanceof Edge)) {
      Edge e = (Edge)o;
      int toRemove = 0;
      if (e.to() == rootVertex) {
        toRemove = e.from();
      } else if (e.from() == rootVertex)
        toRemove = e.to(); else
        return false;
      boolean b = edges.remove(toRemove);
      return b;
    }
    return false;
  }
  


  public int size()
  {
    return edges.size();
  }
  

  private class EdgeIterator
    implements Iterator<Edge>
  {
    private TIntIterator otherVertices;
    
    private boolean alreadyRemoved;
    

    public EdgeIterator()
    {
      otherVertices = edges.iterator();
      alreadyRemoved = false;
    }
    
    public boolean hasNext() {
      return otherVertices.hasNext();
    }
    
    public Edge next() {
      int i = otherVertices.next();
      alreadyRemoved = false;
      return new SimpleEdge(rootVertex, i);
    }
    
    public void remove() {
      if (alreadyRemoved) {
        throw new IllegalStateException();
      }
      try
      {
        otherVertices.remove();
        alreadyRemoved = true;
      } catch (ArrayIndexOutOfBoundsException aioobe) {
        throw new IllegalStateException();
      }
    }
  }
}
