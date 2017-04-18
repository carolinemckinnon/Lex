package edu.ucla.sspace.graph;

import edu.ucla.sspace.util.SetDecorator;
import edu.ucla.sspace.util.primitive.AbstractIntSet;
import edu.ucla.sspace.util.primitive.IntIterator;
import edu.ucla.sspace.util.primitive.IntSet;
import edu.ucla.sspace.util.primitive.PrimitiveCollections;
import edu.ucla.sspace.util.primitive.TroveIntSet;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.set.TIntSet;
import java.io.PrintStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.AbstractSet;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;







































































public class UndirectedMultigraph<T>
  implements Multigraph<T, TypedEdge<T>>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final TObjectIntMap<T> typeCounts;
  private final TIntObjectMap<SparseTypedEdgeSet<T>> vertexToEdges;
  private Collection<WeakReference<UndirectedMultigraph<T>.Subgraph>> subgraphs;
  private int size;
  
  public UndirectedMultigraph()
  {
    this(16);
  }
  



  public UndirectedMultigraph(int vertexCapacity)
  {
    typeCounts = new TObjectIntHashMap();
    vertexToEdges = 
      new TIntObjectHashMap(vertexCapacity);
    subgraphs = new ArrayList();
    size = 0;
  }
  



  public UndirectedMultigraph(Graph<? extends TypedEdge<T>> g)
  {
    this();
    for (Integer v : g.vertices())
      add(v.intValue());
    for (TypedEdge<T> e : g.edges()) {
      add(e);
    }
  }
  

  public boolean add(int vertex)
  {
    if (!vertexToEdges.containsKey(vertex)) {
      vertexToEdges.put(vertex, new SparseTypedEdgeSet(vertex));
      return true;
    }
    return false;
  }
  


  public boolean add(TypedEdge<T> e)
  {
    SparseTypedEdgeSet<T> from = (SparseTypedEdgeSet)vertexToEdges.get(e.from());
    if (from == null) {
      from = new SparseTypedEdgeSet(e.from());
      vertexToEdges.put(e.from(), from);
    }
    if (from.add(e)) {
      updateTypeCounts(e.edgeType(), 1);
      SparseTypedEdgeSet<T> to = (SparseTypedEdgeSet)vertexToEdges.get(e.to());
      if (to == null) {
        to = new SparseTypedEdgeSet(e.to());
        vertexToEdges.put(e.to(), to);
      }
      to.add(e);
      size += 1;
      return true;
    }
    return false;
  }
  


  public void clear()
  {
    vertexToEdges.clear();
    typeCounts.clear();
    size = 0;
  }
  





  public void clearEdges()
  {
    Iterator localIterator = vertexToEdges.valueCollection().iterator();
    while (localIterator.hasNext()) {
      SparseTypedEdgeSet<T> edges = (SparseTypedEdgeSet)localIterator.next();
      edges.clear(); }
    size = 0;
  }
  


  public void clearEdges(T edgeType)
  {
    throw new Error();
  }
  


  public boolean contains(int vertex)
  {
    return vertexToEdges.containsKey(vertex);
  }
  


  public boolean contains(Edge e)
  {
    SparseTypedEdgeSet<T> edges = (SparseTypedEdgeSet)vertexToEdges.get(e.to());
    return (edges != null) && (edges.contains(e));
  }
  


  public boolean contains(int vertex1, int vertex2)
  {
    SparseTypedEdgeSet<T> edges = (SparseTypedEdgeSet)vertexToEdges.get(vertex1);
    return (edges != null) && (edges.connects(vertex2));
  }
  


  public boolean contains(int vertex1, int vertex2, T edgeType)
  {
    SparseTypedEdgeSet<T> edges = (SparseTypedEdgeSet)vertexToEdges.get(vertex2);
    return (edges != null) && (edges.connects(vertex2, edgeType));
  }
  




  public UndirectedMultigraph<T> copy(Set<Integer> toCopy)
  {
    if ((toCopy.size() == order()) && (toCopy.equals(vertices()))) {
      return new UndirectedMultigraph(this);
    }
    UndirectedMultigraph<T> g = new UndirectedMultigraph();
    
    for (Iterator localIterator1 = toCopy.iterator(); localIterator1.hasNext();) { int v = ((Integer)localIterator1.next()).intValue();
      if (!vertexToEdges.containsKey(v)) {
        throw new IllegalArgumentException(
          "Request copy of non-present vertex: " + v);
      }
      g.add(v);
      SparseTypedEdgeSet<T> edges = (SparseTypedEdgeSet)vertexToEdges.get(v);
      if (edges == null)
        throw new IllegalArgumentException();
      for (Iterator localIterator2 = toCopy.iterator(); localIterator2.hasNext();) { int v2 = ((Integer)localIterator2.next()).intValue();
        if (v == v2)
          break;
        if (edges.connects(v2))
          for (TypedEdge<T> e : edges.getEdges(v2))
            g.add(e);
      }
    }
    return g;
  }
  


  public int degree(int vertex)
  {
    SparseTypedEdgeSet<T> edges = (SparseTypedEdgeSet)vertexToEdges.get(vertex);
    return edges == null ? 0 : edges.size();
  }
  


  public Set<TypedEdge<T>> edges()
  {
    return new EdgeView();
  }
  


  public Set<TypedEdge<T>> edges(T t)
  {
    Set<TypedEdge<T>> edges = 
      new HashSet();
    for (SparseTypedEdgeSet<T> set : vertexToEdges.valueCollection()) {
      if (set.types().contains(t))
        edges.addAll(set.getEdges(t));
    }
    return edges;
  }
  


  public Set<T> edgeTypes()
  {
    return Collections.unmodifiableSet(typeCounts.keySet());
  }
  


  public boolean equals(Object o)
  {
    if ((o instanceof UndirectedMultigraph)) {
      UndirectedMultigraph<?> dm = (UndirectedMultigraph)o;
      if (typeCounts.equals(typeCounts)) {
        return vertexToEdges.equals(vertexToEdges);
      }
      return false;
    }
    if ((o instanceof Multigraph))
    {
      Multigraph<?, TypedEdge<?>> m = (Multigraph)o;
      if (m.edgeTypes().equals(typeCounts.keySet())) {
        return (m.order() == order()) && 
          (m.size() == size()) && 
          (m.vertices().equals(vertices())) && 
          (m.edges().equals(edges()));
      }
      return false;
    }
    if ((o instanceof Graph)) {
      Graph<?> g = (Graph)o;
      return (g.order() == order()) && 
        (g.size() == size()) && 
        (g.vertices().equals(vertices())) && 
        (g.edges().equals(edges()));
    }
    return false;
  }
  


  public Set<TypedEdge<T>> getAdjacencyList(int vertex)
  {
    SparseTypedEdgeSet<T> edges = (SparseTypedEdgeSet)vertexToEdges.get(vertex);
    return edges == null ? 
      Collections.emptySet() : 
      new EdgeListWrapper(edges);
  }
  


  public Set<TypedEdge<T>> getEdges(int vertex1, int vertex2)
  {
    SparseTypedEdgeSet<T> edges = (SparseTypedEdgeSet)vertexToEdges.get(vertex1);
    return edges == null ? 
      Collections.emptySet() : 
      edges.getEdges(vertex2);
  }
  




  public Set<TypedEdge<T>> getEdges(int vertex1, int vertex2, Set<T> types)
  {
    SparseTypedEdgeSet<T> edges = (SparseTypedEdgeSet)vertexToEdges.get(vertex1);
    return edges == null ? 
      Collections.emptySet() : 
      edges.getEdges(vertex2, types);
  }
  


  public IntSet getNeighbors(int vertex)
  {
    SparseTypedEdgeSet<T> edges = (SparseTypedEdgeSet)vertexToEdges.get(vertex);
    return edges == null ? 
      PrimitiveCollections.emptyIntSet() : 
      PrimitiveCollections.unmodifiableSet(edges.connected());
  }
  


  public boolean hasCycles()
  {
    throw new Error();
  }
  


  public int hashCode()
  {
    return vertexToEdges.keySet().hashCode() ^ 
      typeCounts.keySet().hashCode() * size;
  }
  


  public int order()
  {
    return vertexToEdges.size();
  }
  




  public boolean remove(int vertex)
  {
    SparseTypedEdgeSet<T> edges = (SparseTypedEdgeSet)vertexToEdges.remove(vertex);
    if (edges != null) {
      size -= edges.size();
      for (Iterator localIterator = edges.connected().iterator(); localIterator.hasNext();) { int other = ((Integer)localIterator.next()).intValue();
        ((SparseTypedEdgeSet)vertexToEdges.get(other)).disconnect(vertex);
      }
      



      for (TypedEdge<T> e : edges) {
        updateTypeCounts(e.edgeType(), -1);
      }
      

      Iterator<WeakReference<UndirectedMultigraph<T>.Subgraph>> iter = subgraphs.iterator();
      while (iter.hasNext()) {
        Object ref = (WeakReference)iter.next();
        UndirectedMultigraph<T>.Subgraph s = (Subgraph)((WeakReference)ref).get();
        


        if (s == null) {
          iter.remove();



        }
        else if (vertexSubset.remove(vertex)) {
          Iterator<T> subgraphTypesIter = validTypes.iterator();
          while (subgraphTypesIter.hasNext()) {
            if (!typeCounts.containsKey(subgraphTypesIter.next()))
              subgraphTypesIter.remove();
          }
        }
      }
      return true;
    }
    return false;
  }
  


  public boolean remove(TypedEdge<T> edge)
  {
    SparseTypedEdgeSet<T> edges = (SparseTypedEdgeSet)vertexToEdges.get(edge.to());
    if ((edges != null) && (edges.remove(edge))) {
      ((SparseTypedEdgeSet)vertexToEdges.get(edge.from())).remove(edge);
      size -= 1;
      



      updateTypeCounts(edge.edgeType(), -1);
      
      if (!typeCounts.containsKey(edge.edgeType()))
      {
        Iterator<WeakReference<UndirectedMultigraph<T>.Subgraph>> sIt = subgraphs.iterator();
        while (sIt.hasNext()) {
          WeakReference<UndirectedMultigraph<T>.Subgraph> ref = (WeakReference)sIt.next();
          UndirectedMultigraph<T>.Subgraph s = (Subgraph)ref.get();
          


          if (s == null) {
            sIt.remove();
          }
          else {
            validTypes.remove(edge.edgeType());
          }
        }
      }
      return true;
    }
    return false;
  }
  


  public int size()
  {
    return size;
  }
  


  public UndirectedMultigraph<T> subgraph(Set<Integer> subset)
  {
    UndirectedMultigraph<T>.Subgraph sub = new Subgraph(typeCounts.keySet(), subset);
    subgraphs.add(new WeakReference(sub));
    return sub;
  }
  


  public UndirectedMultigraph<T> subgraph(Set<Integer> subset, Set<T> edgeTypes)
  {
    if (edgeTypes.isEmpty())
      throw new IllegalArgumentException("Must specify at least one type");
    if (!typeCounts.keySet().containsAll(edgeTypes)) {
      throw new IllegalArgumentException(
        "Cannot create subgraph with more types than exist");
    }
    UndirectedMultigraph<T>.Subgraph sub = new Subgraph(edgeTypes, subset);
    subgraphs.add(new WeakReference(sub));
    return sub;
  }
  



  public String toString()
  {
    return "{ vertices: " + vertices() + ", edges: " + edges() + "}";
  }
  


  private void updateTypeCounts(T type, int delta)
  {
    if (!typeCounts.containsKey(type)) {
      assert (delta > 0) : 
        "removing edge type that was not originally present";
      typeCounts.put(type, delta);
    }
    else {
      int curCount = typeCounts.get(type);
      int newCount = curCount + delta;
      assert (newCount >= 0) : 
        "removing edge type that was not originally present";
      if (newCount == 0) {
        typeCounts.remove(type);
      } else {
        typeCounts.put(type, newCount);
      }
    }
  }
  

  public IntSet vertices()
  {
    return PrimitiveCollections.unmodifiableSet(
      TroveIntSet.wrap(vertexToEdges.keySet()));
  }
  



  class EdgeView
    extends AbstractSet<TypedEdge<T>>
  {
    public EdgeView() {}
    



    public boolean add(TypedEdge<T> e)
    {
      return UndirectedMultigraph.this.add(e);
    }
    
    public boolean contains(Object o) {
      return ((o instanceof TypedEdge)) && 
        (contains((TypedEdge)o));
    }
    
    public Iterator<TypedEdge<T>> iterator() {
      return new EdgeIterator();
    }
    
    public boolean remove(Object o)
    {
      if ((o instanceof TypedEdge)) {
        TypedEdge<?> e = (TypedEdge)o;
        return 
          (typeCounts.containsKey(e.edgeType())) && 
          (remove((TypedEdge)o));
      }
      return false;
    }
    
    public int size() {
      return UndirectedMultigraph.this.size();
    }
    
    class EdgeIterator implements Iterator<TypedEdge<T>>
    {
      Iterator<SparseTypedEdgeSet<T>> edgeSets;
      Iterator<TypedEdge<T>> edges;
      TypedEdge<T> cur = null;
      
      public EdgeIterator() { edgeSets = vertexToEdges.valueCollection().iterator();
        advance();
      }
      
      private void advance() {
        while (((edges == null) || (!edges.hasNext())) && 
          (edgeSets.hasNext())) {
          SparseTypedEdgeSet<T> edgeSet = (SparseTypedEdgeSet)edgeSets.next();
          edges = edgeSet.uniqueIterator();
        }
      }
      
      public boolean hasNext() {
        return (edges != null) && (edges.hasNext());
      }
      
      public TypedEdge<T> next() {
        if (!hasNext())
          throw new NoSuchElementException();
        cur = ((TypedEdge)edges.next());
        advance();
        return cur;
      }
      
      public void remove() {
        if (cur == null)
          throw new IllegalStateException();
        remove(cur);
        cur = null;
      }
    }
  }
  



  class EdgeListWrapper
    extends SetDecorator<TypedEdge<T>>
  {
    private static final long serialVersionUID = 1L;
    


    public EdgeListWrapper()
    {
      super();
    }
    
    public boolean add(TypedEdge<T> e) {
      return UndirectedMultigraph.this.add(e);
    }
    
    public boolean addAll(Collection<? extends TypedEdge<T>> c) {
      boolean added = false;
      for (TypedEdge<T> e : c) {
        if (UndirectedMultigraph.this.add(e))
          added = true;
      }
      return added;
    }
    
    public boolean remove(Object o) {
      if ((o instanceof TypedEdge))
      {
        TypedEdge<T> e = (TypedEdge)o;
        return remove(e);
      }
      return false;
    }
    
    public boolean removeAll(Collection<?> c) {
      boolean removed = false;
      for (Object o : c) {
        if ((o instanceof TypedEdge))
        {
          TypedEdge<T> e = (TypedEdge)o;
          if (remove(e))
            removed = true;
        }
      }
      return removed;
    }
    
    public boolean retainAll(Collection<?> c) {
      throw new Error("FIXME");
    }
  }
  



  class Subgraph
    extends UndirectedMultigraph<T>
  {
    private static final long serialVersionUID = 1L;
    

    private final Set<T> validTypes;
    

    private final IntSet vertexSubset;
    


    public Subgraph(Set<Integer> validTypes)
    {
      this.validTypes = validTypes;
      this.vertexSubset = new TroveIntSet(vertexSubset);
    }
    


    public boolean add(int vertex)
    {
      if (vertexSubset.contains(vertex))
        return false;
      throw new UnsupportedOperationException(
        "Cannot add new vertex to subgraph");
    }
    


    public boolean add(TypedEdge<T> e)
    {
      if ((!vertexSubset.contains(e.from())) || 
        (!vertexSubset.contains(e.to())) || 
        (!validTypes.contains(e.edgeType()))) {
        throw new UnsupportedOperationException(
          "Cannot add new vertex to subgraph");
      }
      return UndirectedMultigraph.this.add(e);
    }
    


    public void clear()
    {
      for (Integer v : vertexSubset)
        UndirectedMultigraph.this.remove(v.intValue());
      vertexSubset.clear();
    }
    


    public void clearEdges()
    {
      throw new Error();
    }
    


    public void clearEdges(T edgeType)
    {
      throw new Error();
    }
    


    public boolean contains(int vertex)
    {
      return vertexSubset.contains(vertex);
    }
    


    public boolean contains(Edge e)
    {
      if ((e instanceof TypedEdge)) {
        TypedEdge<?> te = (TypedEdge)e;
        return (vertexSubset.contains(e.from())) && 
          (vertexSubset.contains(e.to())) && 
          (validTypes.contains(te.edgeType())) && 
          (UndirectedMultigraph.this.contains(e));
      }
      return false;
    }
    


    public boolean contains(int vertex1, int vertex2)
    {
      if ((!vertexSubset.contains(vertex1)) || 
        (!vertexSubset.contains(vertex2))) {
        return false;
      }
      Set<TypedEdge<T>> edges = 
        UndirectedMultigraph.this.getEdges(vertex1, vertex2);
      if (edges != null)
      {

        for (TypedEdge<T> e : edges)
          if (validTypes.contains(e.edgeType()))
            return true;
      }
      return false;
    }
    


    public boolean contains(int vertex1, int vertex2, T edgeType)
    {
      if ((!vertexSubset.contains(vertex1)) || 
        (!vertexSubset.contains(vertex2))) {
        return false;
      }
      return UndirectedMultigraph.this.contains(vertex1, vertex2, edgeType);
    }
    




    public UndirectedMultigraph<T> copy(Set<Integer> vertices)
    {
      if ((vertices.size() == order()) && (vertices.equals(vertices())))
        return new UndirectedMultigraph(this);
      UndirectedMultigraph<T> g = new UndirectedMultigraph();
      Iterator localIterator2; for (Iterator localIterator1 = vertices.iterator(); localIterator1.hasNext(); 
          



          localIterator2.hasNext())
      {
        int v = ((Integer)localIterator1.next()).intValue();
        if (!contains(v))
          throw new IllegalArgumentException(
            "Requested copy with non-existant vertex: " + v);
        g.add(v);
        localIterator2 = getAdjacencyList(v).iterator(); continue;TypedEdge<T> e = (TypedEdge)localIterator2.next();
        if ((vertices.contains(Integer.valueOf(e.from()))) && (vertices.contains(Integer.valueOf(e.to())))) {
          g.add(e);
        }
      }
      return g;
    }
    


    public int degree(int vertex)
    {
      return getNeighbors(vertex).size();
    }
    


    public Set<TypedEdge<T>> edges()
    {
      return new SubgraphEdgeView();
    }
    


    public Set<TypedEdge<T>> edges(T t)
    {
      throw new Error("implement me");
    }
    


    public Set<T> edgeTypes()
    {
      return Collections.unmodifiableSet(validTypes);
    }
    


    public boolean equals(Object o)
    {
      if ((o instanceof Graph)) {
        Graph<?> g = (Graph)o;
        return (g.order() == order()) && 
          (g.size() == size()) && 
          (g.vertices().equals(vertices())) && 
          (g.edges().equals(edges()));
      }
      return false;
    }
    


    public Set<TypedEdge<T>> getAdjacencyList(int vertex)
    {
      if (!vertexSubset.contains(vertex))
        return Collections.emptySet();
      Set<TypedEdge<T>> adj = 
        UndirectedMultigraph.this.getAdjacencyList(vertex);
      return adj.isEmpty() ? 
        Collections.emptySet() : 
        new SubgraphAdjacencyListView(vertex);
    }
    


    public Set<TypedEdge<T>> getEdges(int vertex1, int vertex2)
    {
      if ((!vertexSubset.contains(vertex1)) || 
        (!vertexSubset.contains(vertex2))) {
        return Collections.emptySet();
      }
      Set<TypedEdge<T>> edges = 
        getEdges(vertex1, vertex2, validTypes);
      return edges;
    }
    


    public IntSet getNeighbors(int vertex)
    {
      if (!vertexSubset.contains(vertex))
        return PrimitiveCollections.emptyIntSet();
      return new SubgraphNeighborsView(vertex);
    }
    


    public boolean hasCycles()
    {
      throw new Error();
    }
    


    public int hashCode()
    {
      return vertices().hashCode() ^ 
        validTypes.hashCode() * size();
    }
    




    private boolean hasEdge(int v1, int v2)
    {
      if ((vertexSubset.contains(v1)) && (vertexSubset.contains(v2))) {
        Set<TypedEdge<T>> edges = getEdges(v1, v2);
        return edges != null;
      }
      return false;
    }
    


    public int order()
    {
      return vertexSubset.size();
    }
    


    public boolean remove(int vertex)
    {
      throw new UnsupportedOperationException(
        "Cannot remove vertices from a subgraph");
    }
    


    public boolean remove(TypedEdge<T> e)
    {
      if ((!vertexSubset.contains(e.from())) || 
        (!vertexSubset.contains(e.to())) || 
        (!validTypes.contains(e.edgeType())))
        return false;
      return UndirectedMultigraph.this.remove(e);
    }
    


    public int size()
    {
      int size = 0;
      System.out.println("types: " + validTypes);
      
      for (Iterator localIterator1 = vertexSubset.iterator(); localIterator1.hasNext();) { int v1 = ((Integer)localIterator1.next()).intValue();
        for (Iterator localIterator2 = vertexSubset.iterator(); localIterator2.hasNext();) { int v2 = ((Integer)localIterator2.next()).intValue();
          if (v1 == v2) {
            break;
          }
          size = size + getEdges(v1, v2, validTypes).size();
          System.out.printf("%d -> %d had %d edges%n", new Object[] { Integer.valueOf(v1), Integer.valueOf(v2), 
          
            Integer.valueOf(getEdges(v1, v2, validTypes).size()) });
        }
      }
      return size;
    }
    


    public UndirectedMultigraph<T> subgraph(Set<Integer> verts)
    {
      if (!vertexSubset.containsAll(verts)) {
        throw new IllegalArgumentException("provided set is not a subset of the vertices of this graph");
      }
      return UndirectedMultigraph.this.subgraph(verts, validTypes);
    }
    


    public UndirectedMultigraph<T> subgraph(Set<Integer> verts, Set<T> edgeTypes)
    {
      if (!vertexSubset.containsAll(verts)) {
        throw new IllegalArgumentException("provided set is not a subset of the vertices of this graph");
      }
      if (!validTypes.containsAll(edgeTypes)) {
        throw new IllegalArgumentException("provided types is not a subset of the edge types of this graph");
      }
      return UndirectedMultigraph.this.subgraph(verts, edgeTypes);
    }
    


    public IntSet vertices()
    {
      return PrimitiveCollections.unmodifiableSet(vertexSubset);
    }
    



    private class SubgraphAdjacencyListView
      extends AbstractSet<TypedEdge<T>>
    {
      private final int root;
      


      public SubgraphAdjacencyListView(int root)
      {
        this.root = root;
      }
      
      public boolean add(TypedEdge<T> edge) {
        return ((edge.from() == root) || 
          (edge.to() == root)) && 
          (UndirectedMultigraph.Subgraph.this.add(edge));
      }
      
      public boolean contains(Object o) {
        if (!(o instanceof Edge))
          return false;
        Edge e = (Edge)o;
        return ((e.to() == root) || 
          (e.from() == root)) && 
          (contains(e));
      }
      
      public Iterator<TypedEdge<T>> iterator() {
        return new SubgraphAdjacencyListIterator();
      }
      
      public boolean remove(Object o) {
        if (!(o instanceof TypedEdge))
          return false;
        TypedEdge<?> e = (TypedEdge)o;
        if (!validTypes.contains(e.edgeType())) {
          return false;
        }
        TypedEdge<T> e2 = (TypedEdge)o;
        return ((e2.to() == root) || 
          (e2.from() == root)) && 
          (remove(e2));
      }
      
      public int size() {
        int sz = 0;
        for (Integer i : vertexSubset)
          sz += getEdges(root, i.intValue()).size();
        return sz;
      }
      


      private class SubgraphAdjacencyListIterator
        implements Iterator<TypedEdge<T>>
      {
        Iterator<TypedEdge<T>> edges;
        


        public SubgraphAdjacencyListIterator()
        {
          throw new Error();
        }
        
        public boolean hasNext() {
          return edges.hasNext();
        }
        
        public TypedEdge<T> next() {
          return (TypedEdge)edges.next();
        }
        
        public void remove() {
          edges.remove();
        }
      }
    }
    



    private class SubgraphEdgeView
      extends AbstractSet<TypedEdge<T>>
    {
      public SubgraphEdgeView() {}
      



      public boolean add(TypedEdge<T> e)
      {
        return UndirectedMultigraph.Subgraph.this.add(e);
      }
      
      public boolean contains(Object o) {
        if (!(o instanceof TypedEdge)) {
          return false;
        }
        TypedEdge<T> edge = (TypedEdge)o;
        return contains(edge);
      }
      
      public Iterator<TypedEdge<T>> iterator() {
        return new SubgraphEdgeIterator();
      }
      


      public boolean remove(Object o)
      {
        if (!(o instanceof TypedEdge)) {
          return false;
        }
        TypedEdge<T> edge = (TypedEdge)o;
        return remove(edge);
      }
      
      public int size() {
        return UndirectedMultigraph.Subgraph.this.size();
      }
      


      private class SubgraphEdgeIterator
        implements Iterator<TypedEdge<T>>
      {
        private final Queue<TypedEdge<T>> edgesToReturn;
        

        private final Queue<Integer> remainingVertices;
        
        private Iterator<Integer> possibleNeighbors;
        
        private Integer curVertex;
        

        public SubgraphEdgeIterator()
        {
          remainingVertices = 
            new ArrayDeque(vertices());
          edgesToReturn = new ArrayDeque();
          curVertex = null;
          advance();
        }
        

        private void advance()
        {
          if (!edgesToReturn.isEmpty()) {
            return;
            







            curVertex = ((Integer)remainingVertices.poll());
            possibleNeighbors = remainingVertices.iterator();
          }
          do
          {
            if ((possibleNeighbors == null) || 
              (!possibleNeighbors.hasNext())) {
              if (!remainingVertices.isEmpty()) {
                break;
              }
            }
            





            while ((edgesToReturn.isEmpty()) && 
              (possibleNeighbors.hasNext())) {
              Integer v = (Integer)possibleNeighbors.next();
              edgesToReturn.addAll(getEdges(curVertex.intValue(), v.intValue()));
            }
          } while ((edgesToReturn.isEmpty()) && 
            (!remainingVertices.isEmpty()));
        }
        
        public boolean hasNext() {
          return edgesToReturn.size() > 0;
        }
        
        public TypedEdge<T> next() {
          if (!hasNext())
            throw new NoSuchElementException();
          TypedEdge<T> n = (TypedEdge)edgesToReturn.poll();
          advance();
          return n;
        }
        
        public void remove() {
          throw new IllegalStateException();
        }
      }
    }
    



    private class SubgraphNeighborsView
      extends AbstractIntSet
    {
      private int root;
      



      public SubgraphNeighborsView(int root)
      {
        this.root = root;
      }
      



      public boolean add(Integer vertex)
      {
        throw new UnsupportedOperationException(
          "Cannot add vertices to subgraph");
      }
      
      public boolean contains(Object o) {
        if (!(o instanceof Integer))
          return false;
        Integer i = (Integer)o;
        return (vertexSubset.contains(i)) && (isNeighboringVertex(i));
      }
      
      public boolean contains(int i) {
        return (vertexSubset.contains(i)) && (isNeighboringVertex(Integer.valueOf(i)));
      }
      
      private boolean isNeighboringVertex(Integer i) {
        return contains(root, i.intValue());
      }
      
      public IntIterator iterator() {
        return new SubgraphNeighborsIterator();
      }
      
      public boolean remove(Object o) {
        throw new UnsupportedOperationException();
      }
      
      public int size() {
        int sz = 0;
        for (Integer v : vertexSubset) {
          if (isNeighboringVertex(v))
            sz++;
        }
        return sz;
      }
      


      private class SubgraphNeighborsIterator
        implements IntIterator
      {
        private final IntIterator iter;
        
        private int next;
        
        private boolean hasNext;
        

        public SubgraphNeighborsIterator()
        {
          iter = vertexSubset.iterator();
          advance();
        }
        


        private void advance()
        {
          hasNext = false;
          while ((iter.hasNext()) && (!hasNext)) {
            int v = iter.nextInt();
            if (UndirectedMultigraph.Subgraph.SubgraphNeighborsView.this.isNeighboringVertex(Integer.valueOf(v))) {
              next = v;
              hasNext = true;
            }
          }
        }
        
        public boolean hasNext() {
          return hasNext;
        }
        
        public Integer next() {
          return Integer.valueOf(nextInt());
        }
        
        public int nextInt() {
          if (!hasNext)
            throw new NoSuchElementException();
          int cur = next;
          advance();
          return cur;
        }
        


        public void remove()
        {
          throw new UnsupportedOperationException();
        }
      }
    }
  }
}
