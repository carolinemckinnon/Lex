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










































































public class WeightedDirectedMultigraph<T>
  implements Multigraph<T, WeightedDirectedTypedEdge<T>>, DirectedGraph<WeightedDirectedTypedEdge<T>>, WeightedGraph<WeightedDirectedTypedEdge<T>>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final TObjectIntMap<T> typeCounts;
  private final TIntObjectMap<SparseWeightedDirectedTypedEdgeSet<T>> vertexToEdges;
  private Collection<WeakReference<WeightedDirectedMultigraph<T>.Subgraph>> subgraphs;
  private int size;
  
  public WeightedDirectedMultigraph()
  {
    typeCounts = new TObjectIntHashMap();
    vertexToEdges = new TIntObjectHashMap();
    subgraphs = new ArrayList();
    size = 0;
  }
  



  public WeightedDirectedMultigraph(Graph<? extends WeightedDirectedTypedEdge<T>> g)
  {
    this();
    for (Integer v : g.vertices())
      add(v.intValue());
    for (WeightedDirectedTypedEdge<T> e : g.edges()) {
      add(e);
    }
  }
  

  public boolean add(int vertex)
  {
    if (!vertexToEdges.containsKey(vertex)) {
      vertexToEdges.put(vertex, new SparseWeightedDirectedTypedEdgeSet(vertex));
      return true;
    }
    return false;
  }
  


  public boolean add(WeightedDirectedTypedEdge<T> e)
  {
    SparseWeightedDirectedTypedEdgeSet<T> from = (SparseWeightedDirectedTypedEdgeSet)vertexToEdges.get(e.from());
    if (from == null) {
      from = new SparseWeightedDirectedTypedEdgeSet(e.from());
      vertexToEdges.put(e.from(), from);
    }
    if (from.add(e)) {
      updateTypeCounts(e.edgeType(), 1);
      SparseWeightedDirectedTypedEdgeSet<T> to = (SparseWeightedDirectedTypedEdgeSet)vertexToEdges.get(e.to());
      if (to == null) {
        to = new SparseWeightedDirectedTypedEdgeSet(e.to());
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
      SparseWeightedDirectedTypedEdgeSet<T> edges = (SparseWeightedDirectedTypedEdgeSet)localIterator.next();
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
    SparseWeightedDirectedTypedEdgeSet<T> edges = (SparseWeightedDirectedTypedEdgeSet)vertexToEdges.get(e.to());
    return (edges != null) && (edges.contains(e));
  }
  


  public boolean contains(int vertex1, int vertex2)
  {
    SparseWeightedDirectedTypedEdgeSet<T> edges = (SparseWeightedDirectedTypedEdgeSet)vertexToEdges.get(vertex1);
    return (edges != null) && (edges.connects(vertex2));
  }
  


  public boolean contains(int vertex1, int vertex2, T edgeType)
  {
    SparseWeightedDirectedTypedEdgeSet<T> edges = (SparseWeightedDirectedTypedEdgeSet)vertexToEdges.get(vertex2);
    return (edges != null) && (edges.connects(vertex2, edgeType));
  }
  




  public WeightedDirectedMultigraph<T> copy(Set<Integer> toCopy)
  {
    if ((toCopy.size() == order()) && (toCopy.equals(vertices()))) {
      return new WeightedDirectedMultigraph(this);
    }
    WeightedDirectedMultigraph<T> g = new WeightedDirectedMultigraph();
    for (Iterator localIterator1 = toCopy.iterator(); localIterator1.hasNext();) { int v = ((Integer)localIterator1.next()).intValue();
      if (!vertexToEdges.containsKey(v))
        throw new IllegalArgumentException(
          "Request copy of non-present vertex: " + v);
      g.add(v);
      SparseWeightedDirectedTypedEdgeSet<T> edges = (SparseWeightedDirectedTypedEdgeSet)vertexToEdges.get(v);
      if (edges == null)
        throw new IllegalArgumentException();
      for (Iterator localIterator2 = toCopy.iterator(); localIterator2.hasNext();) { int v2 = ((Integer)localIterator2.next()).intValue();
        if (v == v2)
          break;
        if (edges.connects(v2))
          for (WeightedDirectedTypedEdge<T> e : edges.getEdges(v2))
            g.add(e);
      }
    }
    return g;
  }
  


  public int degree(int vertex)
  {
    SparseWeightedDirectedTypedEdgeSet<T> edges = (SparseWeightedDirectedTypedEdgeSet)vertexToEdges.get(vertex);
    return edges == null ? 0 : edges.size();
  }
  


  public Set<WeightedDirectedTypedEdge<T>> edges()
  {
    return new EdgeView();
  }
  


  public Set<WeightedDirectedTypedEdge<T>> edges(T t)
  {
    Set<WeightedDirectedTypedEdge<T>> edges = 
      new HashSet();
    
    Iterator localIterator = vertexToEdges.valueCollection().iterator();
    while (localIterator.hasNext()) {
      SparseWeightedDirectedTypedEdgeSet<T> set = (SparseWeightedDirectedTypedEdgeSet)localIterator.next();
      Set<WeightedDirectedTypedEdge<T>> edgesOfType = set.getEdges(t);
      if (!edgesOfType.isEmpty())
        edges.addAll(edgesOfType);
    }
    return edges;
  }
  


  public Set<T> edgeTypes()
  {
    return Collections.unmodifiableSet(typeCounts.keySet());
  }
  


  public boolean equals(Object o)
  {
    if ((o instanceof WeightedDirectedMultigraph)) {
      WeightedDirectedMultigraph<?> dm = (WeightedDirectedMultigraph)o;
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
  


  public Set<WeightedDirectedTypedEdge<T>> getAdjacencyList(int vertex)
  {
    SparseWeightedDirectedTypedEdgeSet<T> edges = (SparseWeightedDirectedTypedEdgeSet)vertexToEdges.get(vertex);
    return edges == null ? 
      Collections.emptySet() : 
      new EdgeListWrapper(edges);
  }
  


  public Set<WeightedDirectedTypedEdge<T>> getEdges(int vertex1, int vertex2)
  {
    SparseWeightedDirectedTypedEdgeSet<T> edges = (SparseWeightedDirectedTypedEdgeSet)vertexToEdges.get(vertex1);
    return edges == null ? 
      Collections.emptySet() : 
      edges.getEdges(vertex2);
  }
  




  public Set<WeightedDirectedTypedEdge<T>> getEdges(int vertex1, int vertex2, Set<T> types)
  {
    SparseWeightedDirectedTypedEdgeSet<T> edges = (SparseWeightedDirectedTypedEdgeSet)vertexToEdges.get(vertex1);
    return edges == null ? 
      Collections.emptySet() : 
      edges.getEdges(vertex2, types);
  }
  


  public IntSet getNeighbors(int vertex)
  {
    SparseWeightedDirectedTypedEdgeSet<T> edges = (SparseWeightedDirectedTypedEdgeSet)vertexToEdges.get(vertex);
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
    return vertexToEdges.keySet().hashCode() ^ typeCounts.hashCode() * size;
  }
  


  public int inDegree(int vertex)
  {
    SparseWeightedDirectedTypedEdgeSet<T> edges = (SparseWeightedDirectedTypedEdgeSet)vertexToEdges.get(vertex);
    return edges == null ? 
      0 : 
      edges.predecessors().size();
  }
  


  public Set<WeightedDirectedTypedEdge<T>> inEdges(int vertex)
  {
    SparseWeightedDirectedTypedEdgeSet<T> edges = (SparseWeightedDirectedTypedEdgeSet)vertexToEdges.get(vertex);
    return edges == null ? 
      Collections.emptySet() : 
      new EdgeListWrapper(edges.incoming());
  }
  


  public int order()
  {
    return vertexToEdges.size();
  }
  


  public int outDegree(int vertex)
  {
    SparseWeightedDirectedTypedEdgeSet<T> edges = (SparseWeightedDirectedTypedEdgeSet)vertexToEdges.get(vertex);
    return edges == null ? 
      0 : 
      edges.successors().size();
  }
  


  public Set<WeightedDirectedTypedEdge<T>> outEdges(int vertex)
  {
    SparseWeightedDirectedTypedEdgeSet<T> edges = (SparseWeightedDirectedTypedEdgeSet)vertexToEdges.get(vertex);
    return edges == null ? 
      Collections.emptySet() : 
      new EdgeListWrapper(edges.outgoing());
  }
  


  public IntSet predecessors(int vertex)
  {
    SparseWeightedDirectedTypedEdgeSet<T> edges = (SparseWeightedDirectedTypedEdgeSet)vertexToEdges.get(vertex);
    return edges == null ? 
      PrimitiveCollections.emptyIntSet() : 
      PrimitiveCollections.unmodifiableSet(edges.predecessors());
  }
  




  public boolean remove(int vertex)
  {
    SparseWeightedDirectedTypedEdgeSet<T> edges = (SparseWeightedDirectedTypedEdgeSet)vertexToEdges.remove(vertex);
    if (edges != null) {
      size -= edges.size();
      for (Iterator localIterator = edges.connected().iterator(); localIterator.hasNext();) { int other = ((Integer)localIterator.next()).intValue();
        ((SparseWeightedDirectedTypedEdgeSet)vertexToEdges.get(other)).disconnect(vertex);
      }
      



      for (WeightedDirectedTypedEdge<T> e : edges) {
        updateTypeCounts(e.edgeType(), -1);
      }
      

      Iterator<WeakReference<WeightedDirectedMultigraph<T>.Subgraph>> iter = subgraphs.iterator();
      while (iter.hasNext()) {
        Object ref = (WeakReference)iter.next();
        WeightedDirectedMultigraph<T>.Subgraph s = (Subgraph)((WeakReference)ref).get();
        


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
  


  public boolean remove(WeightedDirectedTypedEdge<T> edge)
  {
    SparseWeightedDirectedTypedEdgeSet<T> edges = (SparseWeightedDirectedTypedEdgeSet)vertexToEdges.get(edge.to());
    if ((edges != null) && (edges.remove(edge))) {
      ((SparseWeightedDirectedTypedEdgeSet)vertexToEdges.get(edge.from())).remove(edge);
      size -= 1;
      


      updateTypeCounts(edge.edgeType(), -1);
      
      if (!typeCounts.containsKey(edge.edgeType()))
      {
        Iterator<WeakReference<WeightedDirectedMultigraph<T>.Subgraph>> sIt = subgraphs.iterator();
        while (sIt.hasNext()) {
          WeakReference<WeightedDirectedMultigraph<T>.Subgraph> ref = (WeakReference)sIt.next();
          WeightedDirectedMultigraph<T>.Subgraph s = (Subgraph)ref.get();
          


          if (s == null) {
            sIt.remove();
          }
          else
            validTypes.remove(edge.edgeType());
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
  


  public double strength(int vertex)
  {
    SparseWeightedDirectedTypedEdgeSet<T> e = (SparseWeightedDirectedTypedEdgeSet)vertexToEdges.get(vertex);
    return e == null ? 0.0D : e.sum();
  }
  


  public IntSet successors(int vertex)
  {
    SparseWeightedDirectedTypedEdgeSet<T> edges = (SparseWeightedDirectedTypedEdgeSet)vertexToEdges.get(vertex);
    return edges == null ? 
      PrimitiveCollections.emptyIntSet() : 
      PrimitiveCollections.unmodifiableSet(edges.successors());
  }
  


  public WeightedDirectedMultigraph<T> subgraph(Set<Integer> subset)
  {
    WeightedDirectedMultigraph<T>.Subgraph sub = new Subgraph(typeCounts.keySet(), subset);
    subgraphs.add(new WeakReference(sub));
    return sub;
  }
  


  public WeightedDirectedMultigraph<T> subgraph(Set<Integer> subset, Set<T> edgeTypes)
  {
    if (edgeTypes.isEmpty())
      throw new IllegalArgumentException("Must specify at least one type");
    if (!typeCounts.keySet().containsAll(edgeTypes)) {
      throw new IllegalArgumentException(
        "Cannot create subgraph with more types than exist");
    }
    WeightedDirectedMultigraph<T>.Subgraph sub = new Subgraph(edgeTypes, subset);
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
    extends AbstractSet<WeightedDirectedTypedEdge<T>>
  {
    public EdgeView() {}
    



    public boolean add(WeightedDirectedTypedEdge<T> e)
    {
      return WeightedDirectedMultigraph.this.add(e);
    }
    
    public boolean contains(Object o) {
      return ((o instanceof WeightedDirectedTypedEdge)) && 
        (contains((WeightedDirectedTypedEdge)o));
    }
    
    public Iterator<WeightedDirectedTypedEdge<T>> iterator() {
      return new EdgeIterator();
    }
    
    public boolean remove(Object o)
    {
      if ((o instanceof WeightedDirectedTypedEdge)) {
        WeightedDirectedTypedEdge<?> e = (WeightedDirectedTypedEdge)o;
        return 
          (typeCounts.containsKey(e.edgeType())) && 
          (remove((WeightedDirectedTypedEdge)o));
      }
      return false;
    }
    
    public int size() {
      return WeightedDirectedMultigraph.this.size();
    }
    
    class EdgeIterator implements Iterator<WeightedDirectedTypedEdge<T>>
    {
      Iterator<SparseWeightedDirectedTypedEdgeSet<T>> edgeSets;
      Iterator<WeightedDirectedTypedEdge<T>> edges;
      WeightedDirectedTypedEdge<T> cur = null;
      
      public EdgeIterator() { edgeSets = vertexToEdges.valueCollection().iterator();
        advance();
      }
      
      private void advance() {
        while (((edges == null) || (!edges.hasNext())) && 
          (edgeSets.hasNext())) {
          SparseWeightedDirectedTypedEdgeSet<T> edgeSet = (SparseWeightedDirectedTypedEdgeSet)edgeSets.next();
          edges = edgeSet.uniqueIterator();
        }
      }
      
      public boolean hasNext() {
        return (edges != null) && (edges.hasNext());
      }
      
      public WeightedDirectedTypedEdge<T> next() {
        if (!hasNext())
          throw new NoSuchElementException();
        cur = ((WeightedDirectedTypedEdge)edges.next());
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
    extends SetDecorator<WeightedDirectedTypedEdge<T>>
  {
    private static final long serialVersionUID = 1L;
    


    public EdgeListWrapper()
    {
      super();
    }
    
    public boolean add(WeightedDirectedTypedEdge<T> e) {
      return WeightedDirectedMultigraph.this.add(e);
    }
    
    public boolean addAll(Collection<? extends WeightedDirectedTypedEdge<T>> c) {
      boolean added = false;
      for (WeightedDirectedTypedEdge<T> e : c) {
        if (WeightedDirectedMultigraph.this.add(e))
          added = true;
      }
      return added;
    }
    
    public boolean remove(Object o) {
      if ((o instanceof WeightedDirectedTypedEdge))
      {
        WeightedDirectedTypedEdge<T> e = (WeightedDirectedTypedEdge)o;
        return remove(e);
      }
      return false;
    }
    
    public boolean removeAll(Collection<?> c) {
      boolean removed = false;
      for (Object o : c) {
        if ((o instanceof WeightedDirectedTypedEdge))
        {
          WeightedDirectedTypedEdge<T> e = (WeightedDirectedTypedEdge)o;
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
    extends WeightedDirectedMultigraph<T>
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
    


    public boolean add(WeightedDirectedTypedEdge<T> e)
    {
      if ((!vertexSubset.contains(e.from())) || 
        (!vertexSubset.contains(e.to())) || 
        (!validTypes.contains(e.edgeType()))) {
        throw new UnsupportedOperationException(
          "Cannot add new vertex to subgraph");
      }
      return WeightedDirectedMultigraph.this.add(e);
    }
    


    public void clear()
    {
      for (Integer v : vertexSubset)
        WeightedDirectedMultigraph.this.remove(v.intValue());
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
      if ((e instanceof WeightedDirectedTypedEdge)) {
        WeightedDirectedTypedEdge<?> te = (WeightedDirectedTypedEdge)e;
        return (vertexSubset.contains(e.from())) && 
          (vertexSubset.contains(e.to())) && 
          (validTypes.contains(te.edgeType())) && 
          (WeightedDirectedMultigraph.this.contains(e));
      }
      return false;
    }
    


    public boolean contains(int vertex1, int vertex2)
    {
      if ((!vertexSubset.contains(vertex1)) || 
        (!vertexSubset.contains(vertex2))) {
        return false;
      }
      Set<WeightedDirectedTypedEdge<T>> edges = 
        WeightedDirectedMultigraph.this.getEdges(vertex1, vertex2);
      if (edges != null)
      {

        for (WeightedDirectedTypedEdge<T> e : edges)
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
      return WeightedDirectedMultigraph.this.contains(vertex1, vertex2, edgeType);
    }
    




    public WeightedDirectedMultigraph<T> copy(Set<Integer> vertices)
    {
      if ((vertices.size() == order()) && (vertices.equals(vertices())))
        return new WeightedDirectedMultigraph(this);
      WeightedDirectedMultigraph<T> g = new WeightedDirectedMultigraph();
      Iterator localIterator2; for (Iterator localIterator1 = vertices.iterator(); localIterator1.hasNext(); 
          



          localIterator2.hasNext())
      {
        int v = ((Integer)localIterator1.next()).intValue();
        if (!contains(v))
          throw new IllegalArgumentException(
            "Requested copy with non-existant vertex: " + v);
        g.add(v);
        localIterator2 = getAdjacencyList(v).iterator(); continue;WeightedDirectedTypedEdge<T> e = (WeightedDirectedTypedEdge)localIterator2.next();
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
    


    public Set<WeightedDirectedTypedEdge<T>> edges()
    {
      return new SubgraphEdgeView();
    }
    


    public Set<WeightedDirectedTypedEdge<T>> edges(T t)
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
    


    public Set<WeightedDirectedTypedEdge<T>> getAdjacencyList(int vertex)
    {
      if (!vertexSubset.contains(vertex))
        return Collections.emptySet();
      Set<WeightedDirectedTypedEdge<T>> adj = 
        WeightedDirectedMultigraph.this.getAdjacencyList(vertex);
      return adj.isEmpty() ? 
        Collections.emptySet() : 
        new SubgraphAdjacencyListView(vertex);
    }
    


    public Set<WeightedDirectedTypedEdge<T>> getEdges(int vertex1, int vertex2)
    {
      if ((!vertexSubset.contains(vertex1)) || 
        (!vertexSubset.contains(vertex2))) {
        return Collections.emptySet();
      }
      Set<WeightedDirectedTypedEdge<T>> edges = 
        getEdges(vertex1, vertex2, validTypes);
      return edges;
    }
    


    public IntSet getNeighbors(int vertex)
    {
      SparseWeightedDirectedTypedEdgeSet<T> edges = (SparseWeightedDirectedTypedEdgeSet)vertexToEdges.get(vertex);
      return edges == null ? 
        PrimitiveCollections.emptyIntSet() : 
        new SubgraphNeighborsView(vertex);
    }
    


    public boolean hasCycles()
    {
      throw new Error();
    }
    


    public int hashCode()
    {
      return vertices().hashCode() ^ validTypes.hashCode() * size();
    }
    




    private boolean hasEdge(int v1, int v2)
    {
      if ((vertexSubset.contains(v1)) && (vertexSubset.contains(v2))) {
        Set<WeightedDirectedTypedEdge<T>> edges = getEdges(v1, v2);
        return edges != null;
      }
      return false;
    }
    


    public int inDegree(int vertex)
    {
      int inDegree = 0;
      for (WeightedDirectedTypedEdge<T> e : getAdjacencyList(vertex)) {
        if (e.to() == vertex)
          inDegree++;
      }
      return inDegree;
    }
    





    public Set<WeightedDirectedTypedEdge<T>> inEdges(int vertex)
    {
      Set<WeightedDirectedTypedEdge<T>> edges = getAdjacencyList(vertex);
      if (edges.isEmpty()) {
        return Collections.emptySet();
      }
      Set<WeightedDirectedTypedEdge<T>> in = new HashSet();
      for (WeightedDirectedTypedEdge<T> e : edges) {
        if (e.to() == vertex)
          in.add(e);
      }
      return in;
    }
    


    public int order()
    {
      return vertexSubset.size();
    }
    


    public int outDegree(int vertex)
    {
      int outDegree = 0;
      for (WeightedDirectedTypedEdge<T> e : getAdjacencyList(vertex)) {
        if (e.from() == vertex)
          outDegree++;
      }
      return outDegree;
    }
    





    public Set<WeightedDirectedTypedEdge<T>> outEdges(int vertex)
    {
      Set<WeightedDirectedTypedEdge<T>> edges = getAdjacencyList(vertex);
      if (edges.isEmpty())
        return Collections.emptySet();
      Set<WeightedDirectedTypedEdge<T>> out = new HashSet();
      for (WeightedDirectedTypedEdge<T> e : edges) {
        if (e.from() == vertex)
          out.add(e);
      }
      return out;
    }
    


    public IntSet predecessors(int vertex)
    {
      SparseWeightedDirectedTypedEdgeSet<T> edges = (SparseWeightedDirectedTypedEdgeSet)vertexToEdges.get(vertex);
      return edges == null ? 
        PrimitiveCollections.emptyIntSet() : 
        PrimitiveCollections.unmodifiableSet(edges.predecessors());
    }
    


    public boolean remove(int vertex)
    {
      throw new UnsupportedOperationException(
        "Cannot remove vertices from a subgraph");
    }
    


    public boolean remove(WeightedDirectedTypedEdge<T> e)
    {
      if ((!vertexSubset.contains(e.from())) || 
        (!vertexSubset.contains(e.to())) || 
        (!validTypes.contains(e.edgeType())))
        return false;
      return WeightedDirectedMultigraph.this.remove(e);
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
    


    public IntSet successors(int vertex)
    {
      SparseWeightedDirectedTypedEdgeSet<T> edges = (SparseWeightedDirectedTypedEdgeSet)vertexToEdges.get(vertex);
      return edges == null ? 
        PrimitiveCollections.emptyIntSet() : 
        PrimitiveCollections.unmodifiableSet(edges.successors());
    }
    


    public WeightedDirectedMultigraph<T> subgraph(Set<Integer> verts)
    {
      if (!vertexSubset.containsAll(verts)) {
        throw new IllegalArgumentException("provided set is not a subset of the vertices of this graph");
      }
      return WeightedDirectedMultigraph.this.subgraph(verts, validTypes);
    }
    


    public WeightedDirectedMultigraph<T> subgraph(Set<Integer> verts, Set<T> edgeTypes)
    {
      if (!vertexSubset.containsAll(verts)) {
        throw new IllegalArgumentException("provided set is not a subset of the vertices of this graph");
      }
      if (!validTypes.containsAll(edgeTypes)) {
        throw new IllegalArgumentException("provided types is not a subset of the edge types of this graph");
      }
      return WeightedDirectedMultigraph.this.subgraph(verts, edgeTypes);
    }
    


    public IntSet vertices()
    {
      return PrimitiveCollections.unmodifiableSet(vertexSubset);
    }
    



    private class SubgraphAdjacencyListView
      extends AbstractSet<WeightedDirectedTypedEdge<T>>
    {
      private final int root;
      


      public SubgraphAdjacencyListView(int root)
      {
        this.root = root;
      }
      
      public boolean add(WeightedDirectedTypedEdge<T> edge) {
        return ((edge.from() == root) || 
          (edge.to() == root)) && 
          (WeightedDirectedMultigraph.Subgraph.this.add(edge));
      }
      
      public boolean contains(Object o) {
        if (!(o instanceof Edge))
          return false;
        Edge e = (Edge)o;
        return ((e.to() == root) || 
          (e.from() == root)) && 
          (contains(e));
      }
      
      public Iterator<WeightedDirectedTypedEdge<T>> iterator() {
        return new SubgraphAdjacencyListIterator();
      }
      
      public boolean remove(Object o) {
        if (!(o instanceof WeightedDirectedTypedEdge))
          return false;
        WeightedDirectedTypedEdge<?> e = (WeightedDirectedTypedEdge)o;
        if (!validTypes.contains(e.edgeType())) {
          return false;
        }
        WeightedDirectedTypedEdge<T> e2 = (WeightedDirectedTypedEdge)o;
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
        implements Iterator<WeightedDirectedTypedEdge<T>>
      {
        Iterator<WeightedDirectedTypedEdge<T>> edges;
        


        public SubgraphAdjacencyListIterator()
        {
          throw new Error();
        }
        
        public boolean hasNext() {
          return edges.hasNext();
        }
        
        public WeightedDirectedTypedEdge<T> next() {
          return (WeightedDirectedTypedEdge)edges.next();
        }
        
        public void remove() {
          edges.remove();
        }
      }
    }
    



    private class SubgraphEdgeView
      extends AbstractSet<WeightedDirectedTypedEdge<T>>
    {
      public SubgraphEdgeView() {}
      



      public boolean add(WeightedDirectedTypedEdge<T> e)
      {
        return WeightedDirectedMultigraph.Subgraph.this.add(e);
      }
      
      public boolean contains(Object o) {
        if (!(o instanceof WeightedDirectedTypedEdge)) {
          return false;
        }
        WeightedDirectedTypedEdge<T> edge = (WeightedDirectedTypedEdge)o;
        return contains(edge);
      }
      
      public Iterator<WeightedDirectedTypedEdge<T>> iterator() {
        return new SubgraphEdgeIterator();
      }
      


      public boolean remove(Object o)
      {
        if (!(o instanceof WeightedDirectedTypedEdge)) {
          return false;
        }
        WeightedDirectedTypedEdge<T> edge = (WeightedDirectedTypedEdge)o;
        return remove(edge);
      }
      
      public int size() {
        return WeightedDirectedMultigraph.Subgraph.this.size();
      }
      


      private class SubgraphEdgeIterator
        implements Iterator<WeightedDirectedTypedEdge<T>>
      {
        private final Queue<WeightedDirectedTypedEdge<T>> edgesToReturn;
        

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
        
        public WeightedDirectedTypedEdge<T> next() {
          if (!hasNext())
            throw new NoSuchElementException();
          WeightedDirectedTypedEdge<T> n = (WeightedDirectedTypedEdge)edgesToReturn.poll();
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
      
      public boolean add(int vertex) {
        throw new UnsupportedOperationException(
          "Cannot add vertices to subgraph");
      }
      
      public boolean add(Integer vertex) {
        throw new UnsupportedOperationException(
          "Cannot add vertices to subgraph");
      }
      
      public boolean contains(int vertex) {
        return (vertexSubset.contains(vertex)) && 
          (isNeighboringVertex(Integer.valueOf(vertex)));
      }
      
      public boolean contains(Object o) {
        if (!(o instanceof Integer))
          return false;
        Integer i = (Integer)o;
        return (vertexSubset.contains(i)) && (isNeighboringVertex(i));
      }
      
      private boolean isNeighboringVertex(Integer i) {
        return contains(root, i.intValue());
      }
      
      public IntIterator iterator() {
        return new SubgraphNeighborsIterator();
      }
      
      public boolean remove(int vertex) {
        throw new UnsupportedOperationException(
          "Cannot remove vertices from subgraph");
      }
      
      public boolean remove(Object vertex) {
        throw new UnsupportedOperationException(
          "Cannot remove vertices from subgraph");
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
        private final Iterator<Integer> iter;
        
        private Integer next;
        

        public SubgraphNeighborsIterator()
        {
          iter = vertexSubset.iterator();
          advance();
        }
        


        private void advance()
        {
          next = null;
          while ((iter.hasNext()) && (next == null)) {
            Integer v = (Integer)iter.next();
            if (WeightedDirectedMultigraph.Subgraph.SubgraphNeighborsView.this.isNeighboringVertex(v))
              next = v;
          }
        }
        
        public boolean hasNext() {
          return next != null;
        }
        
        public Integer next() {
          if (!hasNext())
            throw new NoSuchElementException();
          Integer cur = next;
          advance();
          return cur;
        }
        
        public int nextInt() {
          return next().intValue();
        }
        


        public void remove()
        {
          throw new UnsupportedOperationException();
        }
      }
    }
  }
}
