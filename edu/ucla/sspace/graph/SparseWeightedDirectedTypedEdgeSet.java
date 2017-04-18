package edu.ucla.sspace.graph;

import edu.ucla.sspace.util.CombinedIterator;
import edu.ucla.sspace.util.DisjointSets;
import edu.ucla.sspace.util.HashMultiMap;
import edu.ucla.sspace.util.MultiMap;
import edu.ucla.sspace.util.primitive.IntSet;
import edu.ucla.sspace.util.primitive.TroveIntSet;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;































public class SparseWeightedDirectedTypedEdgeSet<T>
  extends AbstractSet<WeightedDirectedTypedEdge<T>>
  implements EdgeSet<WeightedDirectedTypedEdge<T>>, Serializable
{
  private final int rootVertex;
  MultiMap<Integer, WeightedDirectedTypedEdge<T>> inEdges;
  MultiMap<Integer, WeightedDirectedTypedEdge<T>> outEdges;
  
  public SparseWeightedDirectedTypedEdgeSet(int root)
  {
    rootVertex = root;
    inEdges = new HashMultiMap();
    outEdges = new HashMultiMap();
  }
  



  public boolean add(WeightedDirectedTypedEdge<T> e)
  {
    if (e.from() == rootVertex) {
      Set<WeightedDirectedTypedEdge<T>> edges = outEdges.get(Integer.valueOf(e.to()));
      if (edges.contains(e)) {
        return false;
      }
      



      if (!edges.isEmpty()) {
        Iterator<WeightedDirectedTypedEdge<T>> iter = edges.iterator();
        WeightedDirectedTypedEdge<T> existing = null;
        while (iter.hasNext()) {
          WeightedDirectedTypedEdge<T> n = (WeightedDirectedTypedEdge)iter.next();
          if ((n.to() == e.to()) && 
            (n.edgeType().equals(e.edgeType()))) {
            existing = n;
            break;
          }
        }
        if (existing == null) {
          outEdges.put(Integer.valueOf(e.to()), e);
          return true;
        }
        
        if (e.weight() != existing.weight()) {
          outEdges.remove(Integer.valueOf(e.to()), existing);
          outEdges.put(Integer.valueOf(e.to()), e);
          return true;
        }
        
        return false;
      }
      
      return outEdges.put(Integer.valueOf(e.to()), e);
    }
    
    if (e.to() == rootVertex) {
      Set<WeightedDirectedTypedEdge<T>> edges = inEdges.get(Integer.valueOf(e.from()));
      if (edges.contains(e)) {
        return false;
      }
      



      if (!edges.isEmpty()) {
        Iterator<WeightedDirectedTypedEdge<T>> iter = edges.iterator();
        WeightedDirectedTypedEdge<T> existing = null;
        while (iter.hasNext()) {
          WeightedDirectedTypedEdge<T> n = (WeightedDirectedTypedEdge)iter.next();
          if ((n.from() == e.from()) && 
            (n.edgeType().equals(e.edgeType()))) {
            existing = n;
            break;
          }
        }
        
        if (existing == null) {
          inEdges.put(Integer.valueOf(e.from()), e);
          return true;
        }
        
        if (e.weight() != existing.weight()) {
          inEdges.remove(Integer.valueOf(e.from()), existing);
          inEdges.put(Integer.valueOf(e.from()), e);
          return true;
        }
        return false;
      }
      
      return inEdges.put(Integer.valueOf(e.from()), e);
    }
    return false;
  }
  


  public void clear()
  {
    inEdges.clear();
    outEdges.clear();
  }
  


  public IntSet connected()
  {
    TroveIntSet t = new TroveIntSet();
    t.addAll(inEdges.keySet());
    t.addAll(outEdges.keySet());
    return t;
  }
  


  public boolean connects(int vertex)
  {
    return (inEdges.containsKey(Integer.valueOf(vertex))) || (outEdges.containsKey(Integer.valueOf(vertex)));
  }
  


  public boolean connects(int vertex, T type)
  {
    if (inEdges.containsKey(Integer.valueOf(vertex))) {
      for (WeightedDirectedTypedEdge<T> e : inEdges.get(Integer.valueOf(vertex))) {
        if (e.edgeType().equals(type)) {
          return true;
        }
      }
    } else if (outEdges.containsKey(Integer.valueOf(vertex))) {
      for (WeightedDirectedTypedEdge<T> e : outEdges.get(Integer.valueOf(vertex))) {
        if (e.edgeType().equals(type))
          return true;
      }
    }
    return false;
  }
  


  public boolean contains(Object o)
  {
    if (!(o instanceof WeightedDirectedTypedEdge)) {
      return false;
    }
    
    WeightedDirectedTypedEdge<T> e = (WeightedDirectedTypedEdge)o;
    
    if (e.from() == rootVertex) {
      Set<WeightedDirectedTypedEdge<T>> edges = outEdges.get(Integer.valueOf(e.to()));
      return edges.contains(e);
    }
    
    if (e.to() == rootVertex) {
      Set<WeightedDirectedTypedEdge<T>> edges = inEdges.get(Integer.valueOf(e.from()));
      return edges.contains(e);
    }
    return false;
  }
  
  public SparseWeightedDirectedTypedEdgeSet<T> copy(IntSet vertices)
  {
    throw new Error();
  }
  


  public int disconnect(int vertex)
  {
    Set<WeightedDirectedTypedEdge<T>> in = inEdges.remove(Integer.valueOf(vertex));
    Set<WeightedDirectedTypedEdge<T>> out = outEdges.remove(Integer.valueOf(vertex));
    return in.size() + out.size();
  }
  


  public Set<WeightedDirectedTypedEdge<T>> getEdges(T type)
  {
    Set<WeightedDirectedTypedEdge<T>> edges = 
      new HashSet();
    for (WeightedDirectedTypedEdge<T> e : inEdges.values())
      if (e.edgeType().equals(type))
        edges.add(e);
    for (WeightedDirectedTypedEdge<T> e : outEdges.values())
      if (e.edgeType().equals(type))
        edges.add(e);
    return edges;
  }
  


  public Set<WeightedDirectedTypedEdge<T>> getEdges(Set<T> types)
  {
    Set<WeightedDirectedTypedEdge<T>> edges = 
      new HashSet();
    for (WeightedDirectedTypedEdge<T> e : inEdges.values())
      if (types.contains(e.edgeType()))
        edges.add(e);
    for (WeightedDirectedTypedEdge<T> e : outEdges.values())
      if (types.contains(e.edgeType()))
        edges.add(e);
    return edges;
  }
  


  public Set<WeightedDirectedTypedEdge<T>> getEdges(int vertex)
  {
    DisjointSets<WeightedDirectedTypedEdge<T>> edges = 
      new DisjointSets(new Set[0]);
    edges.append(inEdges.get(Integer.valueOf(vertex)));
    edges.append(outEdges.get(Integer.valueOf(vertex)));
    return edges;
  }
  


  public Set<WeightedDirectedTypedEdge<T>> getEdges(int vertex, Set<T> types)
  {
    Set<WeightedDirectedTypedEdge<T>> edges = 
      new HashSet();
    for (WeightedDirectedTypedEdge<T> e : inEdges.get(Integer.valueOf(vertex)))
      if (types.contains(e.edgeType()))
        edges.add(e);
    for (WeightedDirectedTypedEdge<T> e : outEdges.get(Integer.valueOf(vertex)))
      if (types.contains(e.edgeType()))
        edges.add(e);
    return edges;
  }
  



  public int getRoot()
  {
    return rootVertex;
  }
  
  public Set<WeightedDirectedTypedEdge<T>> incoming() {
    return Collections.unmodifiableSet(
      new HashSet(inEdges.values()));
  }
  


  public boolean isEmpty()
  {
    return (inEdges.isEmpty()) && (outEdges.isEmpty());
  }
  


  public Iterator<WeightedDirectedTypedEdge<T>> iterator()
  {
    List<Iterator<WeightedDirectedTypedEdge<T>>> iters = 
      new ArrayList();
    iters.add(
      Collections.unmodifiableCollection(inEdges.values()).iterator());
    iters.add(
      Collections.unmodifiableCollection(outEdges.values()).iterator());
    return new CombinedIterator(iters);
  }
  
  public Set<WeightedDirectedTypedEdge<T>> outgoing() {
    return Collections.unmodifiableSet(
      new HashSet(outEdges.values()));
  }
  
  public IntSet predecessors() {
    return new TroveIntSet(inEdges.keySet());
  }
  


  public int size()
  {
    return inEdges.range() + outEdges.range();
  }
  


  public boolean remove(Object o)
  {
    if (!(o instanceof WeightedDirectedTypedEdge)) {
      return false;
    }
    
    WeightedDirectedTypedEdge<T> e = (WeightedDirectedTypedEdge)o;
    
    if (e.from() == rootVertex) {
      return outEdges.remove(Integer.valueOf(e.to()), e);
    }
    
    if (e.to() == rootVertex) {
      return inEdges.remove(Integer.valueOf(e.from()), e);
    }
    return false;
  }
  
  public IntSet successors() {
    return new TroveIntSet(outEdges.keySet());
  }
  


  public double sum()
  {
    double sum = 0.0D;
    for (WeightedDirectedTypedEdge<T> e : inEdges.values())
      sum += e.weight();
    for (WeightedDirectedTypedEdge<T> e : outEdges.values())
      sum += e.weight();
    return sum;
  }
  


  public Iterator<WeightedDirectedTypedEdge<T>> uniqueIterator()
  {
    return new UniqueEdgeIterator();
  }
  


  private class UniqueEdgeIterator
    implements Iterator<WeightedDirectedTypedEdge<T>>
  {
    Iterator<WeightedDirectedTypedEdge<T>> iter;
    

    WeightedDirectedTypedEdge<T> next;
    

    public UniqueEdgeIterator()
    {
      iter = iterator();
      next = null;
      advance();
    }
    
    private void advance() {
      next = null;
      while ((next == null) && (iter.hasNext())) {
        WeightedDirectedTypedEdge<T> e = (WeightedDirectedTypedEdge)iter.next();
        boolean isInEdge = e.to() == rootVertex;
        int otherVertex = isInEdge ? e.from() : e.to();
        if (((isInEdge) && (rootVertex < otherVertex)) || (
          (!isInEdge) && (rootVertex < otherVertex)))
        {

          next = e; }
      }
    }
    
    public boolean hasNext() {
      return next != null;
    }
    
    public WeightedDirectedTypedEdge<T> next() {
      if (next == null)
        throw new NoSuchElementException();
      WeightedDirectedTypedEdge<T> n = next;
      
      advance();
      return n;
    }
    
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
}
