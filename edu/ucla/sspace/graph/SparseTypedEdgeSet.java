package edu.ucla.sspace.graph;

import edu.ucla.sspace.util.primitive.IntSet;
import edu.ucla.sspace.util.primitive.TroveIntSet;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.procedure.TIntObjectProcedure;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
































































public class SparseTypedEdgeSet<T>
  extends AbstractSet<TypedEdge<T>>
  implements EdgeSet<TypedEdge<T>>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final int rootVertex;
  private final TIntObjectMap<Set<T>> edges;
  private int size;
  
  public SparseTypedEdgeSet(int rootVertex)
  {
    this.rootVertex = rootVertex;
    edges = new TIntObjectHashMap();
    size = 0;
  }
  



  public boolean add(TypedEdge<T> e)
  {
    if (e.from() == rootVertex)
      return add(e.to(), e.edgeType());
    if (e.to() == rootVertex)
      return add(e.from(), e.edgeType());
    return false;
  }
  



  private boolean add(int i, T type)
  {
    Set<T> types = (Set)edges.get(i);
    

    if (types == null) {
      types = new HashSet();
      edges.put(i, types);
    }
    
    boolean b = types.add(type);
    if (b)
      size += 1;
    return b;
  }
  


  public void clear()
  {
    edges.clear();
  }
  


  public IntSet connected()
  {
    return TroveIntSet.wrap(edges.keySet());
  }
  


  public boolean connects(int vertex)
  {
    return edges.containsKey(vertex);
  }
  


  public boolean connects(int vertex, T type)
  {
    Set<T> types = (Set)edges.get(vertex);
    return (types != null) && (types.contains(type));
  }
  


  public boolean contains(Object o)
  {
    if (!(o instanceof TypedEdge)) {
      return false;
    }
    TypedEdge<T> e = (TypedEdge)o;
    
    if (e.from() == rootVertex)
      return contains(edges, e.to(), e.edgeType());
    if (e.to() == rootVertex)
      return contains(edges, e.from(), e.edgeType());
    return false;
  }
  
  private boolean contains(TIntObjectMap<Set<T>> edges, int i, T type) {
    Set<T> types = (Set)edges.get(i);
    return (types != null) && (types.contains(type));
  }
  


  public SparseTypedEdgeSet<T> copy(IntSet vertices)
  {
    SparseTypedEdgeSet<T> copy = new SparseTypedEdgeSet(rootVertex);
    TIntObjectIterator<Set<T>> iter = edges.iterator();
    while (iter.hasNext()) {
      iter.advance();
      int v = iter.key();
      if (vertices.contains(v)) {
        Set<T> types = (Set)iter.value();
        edges.put(v, types);
        size += types.size();
      }
    }
    return copy;
  }
  


  public int disconnect(int v)
  {
    Set<T> types = (Set)this.edges.remove(v);
    if (types != null) {
      int edges = types.size();
      size -= edges;
      return edges;
    }
    return 0;
  }
  


  public Set<TypedEdge<T>> getEdges(final T type)
  {
    final Set<TypedEdge<T>> s = new HashSet();
    edges.forEachEntry(new TIntObjectProcedure() {
      public boolean execute(int v, Set<T> types) {
        if (types.contains(type))
          s.add(new SimpleTypedEdge(
            type, v, rootVertex));
        return true;
      }
    });
    return s;
  }
  


  public Set<TypedEdge<T>> getEdges(int vertex)
  {
    return new EdgesForVertex(vertex);
  }
  






















  public Set<TypedEdge<T>> getEdges(int vertex, Set<T> types)
  {
    Set<TypedEdge<T>> set = new HashSet();
    for (TypedEdge<T> e : new EdgesForVertex(vertex))
      if (types.contains(e.edgeType()))
        set.add(e);
    return set;
  }
  


  public int getRoot()
  {
    return rootVertex;
  }
  


  public boolean isEmpty()
  {
    return edges.isEmpty();
  }
  


  public Iterator<TypedEdge<T>> iterator()
  {
    return new EdgeIterator();
  }
  


  public boolean remove(Object o)
  {
    if (!(o instanceof TypedEdge)) {
      return false;
    }
    
    TypedEdge<T> e = (TypedEdge)o;
    
    if (e.from() == rootVertex)
      return remove(e.to(), e.edgeType());
    if (e.to() == rootVertex)
      return remove(e.from(), e.edgeType());
    return false;
  }
  
  private boolean remove(int i, T type) {
    Set<T> types = (Set)edges.get(i);
    boolean b = types.remove(type);
    if (b)
      size -= 1;
    return b;
  }
  


  public int size()
  {
    return size;
  }
  



  public Set<T> types()
  {
    Set<T> types = new HashSet();
    for (Object o : edges.values()) {
      Set<T> s = (Set)o;
      types.addAll(s);
    }
    return types;
  }
  


  public Iterator<TypedEdge<T>> uniqueIterator()
  {
    return new UniqueEdgeIterator();
  }
  



  private class EdgesForVertex
    extends AbstractSet<TypedEdge<T>>
  {
    private final int otherVertex;
    


    public EdgesForVertex(int otherVertex)
    {
      this.otherVertex = otherVertex;
    }
    
    public boolean add(TypedEdge<T> e) {
      return ((e.to() == rootVertex) && (e.from() == otherVertex)) || (
        (e.from() == rootVertex) && (e.to() == otherVertex) && 
        (SparseTypedEdgeSet.this.add(e)));
    }
    
    public boolean contains(Object o) {
      if (!(o instanceof TypedEdge))
        return false;
      TypedEdge<?> e = (TypedEdge)o;
      return ((e.to() == rootVertex) && (e.from() == otherVertex)) || (
        (e.from() == rootVertex) && (e.to() == otherVertex) && 
        (SparseTypedEdgeSet.this.contains(e)));
    }
    
    public boolean isEmpty() {
      return !connects(otherVertex);
    }
    
    public Iterator<TypedEdge<T>> iterator() {
      return new SparseTypedEdgeSet.EdgesForVertexIterator(SparseTypedEdgeSet.this, otherVertex);
    }
    
    public boolean remove(Object o) {
      if (!(o instanceof TypedEdge))
        return false;
      TypedEdge<?> e = (TypedEdge)o;
      return ((e.to() == rootVertex) && (e.from() == otherVertex)) || (
        (e.from() == rootVertex) && (e.to() == otherVertex) && 
        (SparseTypedEdgeSet.this.remove(e)));
    }
    
    public int size() {
      Set<T> types = (Set)edges.get(otherVertex);
      return types == null ? 0 : types.size();
    }
  }
  


  private class EdgesForVertexIterator
    implements Iterator<TypedEdge<T>>
  {
    private final Iterator<T> typeIter;
    
    int otherVertex;
    

    public EdgesForVertexIterator(int otherVertex)
    {
      this.otherVertex = otherVertex;
      Set<T> types = (Set)edges.get(otherVertex);
      
      typeIter = (types != null ? 
        types.iterator() : 
        Collections.emptySet().iterator());
    }
    
    public boolean hasNext() {
      return typeIter.hasNext();
    }
    
    public TypedEdge<T> next() {
      if (!typeIter.hasNext())
        throw new NoSuchElementException();
      return new SimpleTypedEdge(
        typeIter.next(), rootVertex, otherVertex);
    }
    
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
  



  private class EdgeIterator
    implements Iterator<TypedEdge<T>>
  {
    private TIntObjectIterator<Set<T>> iter;
    

    private Iterator<T> typeIter;
    

    TypedEdge<T> next;
    


    public EdgeIterator()
    {
      iter = edges.iterator();
      typeIter = null;
      advance();
    }
    
    private void advance() {
      next = null;
      while (next == null) {
        if ((typeIter == null) || (!typeIter.hasNext()))
        {
          if (!iter.hasNext())
            break;
          iter.advance();
          typeIter = ((Set)iter.value()).iterator();
        }
        
        if (typeIter.hasNext()) {
          next = new SimpleTypedEdge(
            typeIter.next(), iter.key(), rootVertex);
        }
      }
    }
    
    public boolean hasNext() {
      return next != null;
    }
    
    public TypedEdge<T> next() {
      if (next == null)
        throw new NoSuchElementException();
      TypedEdge<T> n = next;
      advance();
      return n;
    }
    
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
  


  private class UniqueEdgeIterator
    implements Iterator<TypedEdge<T>>
  {
    Iterator<TypedEdge<T>> it;
    
    TypedEdge<T> next;
    

    public UniqueEdgeIterator()
    {
      it = iterator();
      advance();
    }
    
    private void advance() {
      next = null;
      while ((it.hasNext()) && (next == null)) {
        TypedEdge<T> e = (TypedEdge)it.next();
        if (((e.from() == rootVertex) && (e.to() < rootVertex)) || (
          (e.to() == rootVertex) && (e.from() < rootVertex)))
          next = e;
      }
    }
    
    public boolean hasNext() {
      return next != null;
    }
    
    public TypedEdge<T> next() {
      if (next == null)
        throw new NoSuchElementException();
      TypedEdge<T> n = next;
      
      advance();
      return n;
    }
    
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
}
