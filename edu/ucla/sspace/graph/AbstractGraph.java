package edu.ucla.sspace.graph;

import edu.ucla.sspace.util.CombinedIterator;
import edu.ucla.sspace.util.primitive.AbstractIntSet;
import edu.ucla.sspace.util.primitive.IntIterator;
import edu.ucla.sspace.util.primitive.IntSet;
import edu.ucla.sspace.util.primitive.PrimitiveCollections;
import edu.ucla.sspace.util.primitive.TroveIntSet;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.AbstractSet;
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














































































public abstract class AbstractGraph<T extends Edge, S extends EdgeSet<T>>
  implements Graph<T>, Serializable
{
  private static final long serialVersionUID = 1L;
  public static final int DEFAULT_INITIAL_VERTEX_CAPACITY = 16;
  private int numEdges;
  private final TIntObjectMap<S> vertexToEdges;
  private Collection<WeakReference<AbstractGraph<T, S>.Subgraph>> subgraphs;
  
  public AbstractGraph()
  {
    this(16);
  }
  



  public AbstractGraph(int capacity)
  {
    if (capacity < 0) {
      throw new IllegalArgumentException("Capcity must be positive");
    }
    vertexToEdges = new TIntObjectHashMap(capacity);
    subgraphs = new ArrayList();
  }
  


  public AbstractGraph(Set<Integer> vertices)
  {
    this(Math.max(vertices.size(), 16));
    for (Integer v : vertices) {
      vertexToEdges.put(v.intValue(), createEdgeSet(v.intValue()));
    }
  }
  


  private EdgeSet<T> addIfAbsent(int v)
  {
    S edges = getEdgeSet(v);
    if (edges == null) {
      edges = createEdgeSet(v);
      vertexToEdges.put(v, edges);
    }
    return edges;
  }
  


  public boolean add(int v)
  {
    S edges = getEdgeSet(v);
    if (edges == null) {
      edges = createEdgeSet(v);
      vertexToEdges.put(v, edges);
      return true;
    }
    return false;
  }
  


  public boolean add(T e)
  {
    EdgeSet<T> from = addIfAbsent(e.from());
    EdgeSet<T> to = addIfAbsent(e.to());
    








    boolean isNew = from.add(e);
    to.add(e);
    if (isNew) {
      numEdges += 1;
    }
    assert (from.contains(e)) : "error in EdgeSet contains logic";
    assert (to.contains(e)) : "error in EdgeSet contains logic";
    
    return isNew;
  }
  
  private void checkIndex(int vertex) {
    if (vertex < 0) {
      throw new IllegalArgumentException("vertices must be non-negative");
    }
  }
  

  public void clear()
  {
    vertexToEdges.clear();
    numEdges = 0;
  }
  


  public void clearEdges()
  {
    for (EdgeSet<T> e : vertexToEdges.valueCollection())
      e.clear();
    numEdges = 0;
  }
  


  public boolean contains(int vertex)
  {
    return vertexToEdges.containsKey(vertex);
  }
  


  public boolean contains(int vertex1, int vertex2)
  {
    EdgeSet<T> e1 = getEdgeSet(vertex1);
    return (e1 != null) && (e1.connects(vertex2));
  }
  








  public boolean contains(Edge e)
  {
    EdgeSet<T> e1 = getEdgeSet(e.from());
    return (e1 != null) && (e1.contains(e));
  }
  



  public abstract Graph<T> copy(Set<Integer> paramSet);
  



  protected abstract S createEdgeSet(int paramInt);
  



  public int degree(int vertex)
  {
    EdgeSet<T> e = getEdgeSet(vertex);
    return e == null ? 0 : e.size();
  }
  


  public Set<T> edges()
  {
    return new EdgeView();
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
  


  public Set<T> getAdjacencyList(int vertex)
  {
    EdgeSet<T> e = getEdgeSet(vertex);
    return e == null ? 
      Collections.emptySet() : 
      new AdjacencyListView(e);
  }
  


  public IntSet getNeighbors(int vertex)
  {
    EdgeSet<T> e = getEdgeSet(vertex);
    return e == null ? 
      PrimitiveCollections.emptyIntSet() : 
      PrimitiveCollections.unmodifiableSet(e.connected());
  }
  


  public Set<T> getEdges(int vertex1, int vertex2)
  {
    EdgeSet<T> e = getEdgeSet(vertex1);
    if (e == null)
      return Collections.emptySet();
    Set<T> edges = e.getEdges(vertex2);
    return edges.isEmpty() ? Collections.emptySet() : edges;
  }
  



  protected S getEdgeSet(int vertex)
  {
    return (EdgeSet)vertexToEdges.get(vertex);
  }
  


  public boolean hasCycles()
  {
    throw new UnsupportedOperationException("fix me");
  }
  


  public int hashCode()
  {
    return vertices().hashCode();
  }
  


  public Iterator<Integer> iterator()
  {
    return new VertexView().iterator();
  }
  


  public int order()
  {
    return vertexToEdges.size();
  }
  








  public boolean remove(Edge e)
  {
    EdgeSet<T> from = getEdgeSet(e.from());
    EdgeSet<T> to = getEdgeSet(e.to());
    
    int before = numEdges;
    if ((from != null) && (from.remove(e))) {
      numEdges -= 1;
      assert (to.contains(e)) : 
        ("Complementary edge set " + to + "  did not contain " + e);
      

      to.remove(e);
    }
    
    return before != numEdges;
  }
  


  public boolean remove(int vertex)
  {
    EdgeSet<T> edges = (EdgeSet)vertexToEdges.remove(vertex);
    if (edges == null) {
      return false;
    }
    
    removeInternal(vertex, edges);
    return true;
  }
  






  private void removeInternal(int vertex, EdgeSet<T> edges)
  {
    numEdges -= edges.size();
    


    for (Edge e : edges)
    {

      int otherVertex = e.from() == vertex ? e.to() : e.from();
      EdgeSet<T> otherEdges = (EdgeSet)vertexToEdges.get(otherVertex);
      assert (otherEdges.contains(e)) : 
        "Error in ensuring consistent from/to edge sets";
      otherEdges.remove(e);
    }
    


    Iterator<WeakReference<AbstractGraph<T, S>.Subgraph>> iter = subgraphs.iterator();
    while (iter.hasNext()) {
      Object ref = (WeakReference)iter.next();
      AbstractGraph<T, S>.Subgraph s = (Subgraph)((WeakReference)ref).get();
      


      if (s == null) {
        iter.remove();

      }
      else
      {
        vertexSubset.remove(vertex);
      }
    }
  }
  

  public int size()
  {
    return numEdges;
  }
  


  public Graph<T> subgraph(Set<Integer> vertices)
  {
    AbstractGraph<T, S>.Subgraph sub = new Subgraph(vertices);
    subgraphs.add(new WeakReference(sub));
    return sub;
  }
  



  public String toString()
  {
    return "{ vertices: " + vertices() + ", edges: " + edges() + "}";
  }
  


  public IntSet vertices()
  {
    return new VertexView();
  }
  



  private class VertexView
    extends AbstractIntSet
  {
    public VertexView() {}
    



    public boolean add(int vertex)
    {
      return AbstractGraph.this.add(vertex);
    }
    
    public boolean add(Integer vertex) {
      return AbstractGraph.this.add(vertex.intValue());
    }
    
    public boolean contains(int vertex) {
      return AbstractGraph.this.contains(vertex);
    }
    
    public boolean contains(Integer vertex) {
      return AbstractGraph.this.contains(vertex.intValue());
    }
    
    public IntIterator iterator() {
      return new VertexIterator();
    }
    
    public boolean remove(int i) {
      return AbstractGraph.this.remove(i);
    }
    
    public boolean remove(Integer i) {
      return AbstractGraph.this.remove(i.intValue());
    }
    
    public int size() {
      return order();
    }
    
    public class VertexIterator implements IntIterator
    {
      boolean alreadyRemoved = true;
      IntIterator iter;
      int cur;
      
      public VertexIterator() {
        iter = TroveIntSet.wrap(vertexToEdges.keySet()).iterator();
        cur = -1;
      }
      
      public boolean hasNext() {
        return iter.hasNext();
      }
      
      public int nextInt() {
        alreadyRemoved = false;
        cur = iter.nextInt();
        return cur;
      }
      
      public Integer next() {
        return Integer.valueOf(nextInt());
      }
      
      public void remove() {
        if (alreadyRemoved)
          throw new IllegalStateException();
        alreadyRemoved = true;
        AbstractGraph.this.remove(cur);
      }
    }
  }
  

  private class AdjacencyListView
    extends AbstractSet<T>
  {
    private final EdgeSet<T> adjacencyList;
    

    public AdjacencyListView()
    {
      this.adjacencyList = adjacencyList;
    }
    





    public boolean add(T edge)
    {
      if (adjacencyList.add(edge))
      {

        int otherVertex = edge.from() == adjacencyList.getRoot() ? 
          edge.to() : edge.from();
        if (!vertexToEdges.containsKey(otherVertex)) {
          add(otherVertex);
        }
        

        ((EdgeSet)vertexToEdges.get(otherVertex)).add(edge);
        numEdges += 1;
        return true;
      }
      return false;
    }
    
    public boolean contains(Object edge) {
      return adjacencyList.contains(edge);
    }
    
    public Iterator<T> iterator() {
      return new AdjacencyListIterator();
    }
    
    public boolean remove(Object o) {
      if (!(o instanceof Edge))
        return false;
      Edge edge = (Edge)o;
      

      if (adjacencyList.remove(edge))
      {
        int otherVertex = edge.from() == adjacencyList.getRoot() ? 
          edge.to() : edge.from();
        
        ((EdgeSet)vertexToEdges.get(otherVertex)).remove(edge);
        
        numEdges -= 1;
        return true;
      }
      return false;
    }
    
    public int size() {
      return adjacencyList.size();
    }
    

    private class AdjacencyListIterator
      implements Iterator<T>
    {
      private final Iterator<T> edges;
      

      public AdjacencyListIterator()
      {
        edges = adjacencyList.iterator();
      }
      
      public boolean hasNext() {
        return edges.hasNext();
      }
      
      public T next() {
        return (Edge)edges.next();
      }
      
      public void remove() {
        edges.remove();
        numEdges -= 1;
      }
    }
  }
  





  private class AdjacentVerticesView
    extends AbstractSet<Integer>
  {
    private final Set<Integer> adjacent;
    





    public AdjacentVerticesView()
    {
      this.adjacent = adjacent;
    }
    


    public boolean add(Integer vertex)
    {
      throw new UnsupportedOperationException("cannot create edges using an adjacenct vertices set; use add() instead");
    }
    
    public boolean contains(Object o)
    {
      return ((o instanceof Integer)) && 
        (adjacent.contains((Integer)o));
    }
    
    public boolean isEmpty() {
      return adjacent.isEmpty();
    }
    
    public Iterator<Integer> iterator() {
      return new AdjacentVerticesIterator();
    }
    
    public boolean remove(Object o) {
      throw new UnsupportedOperationException("cannot remove edges using an adjacenct vertices set; use remove() instead");
    }
    
    public int size()
    {
      return adjacent.size();
    }
    

    private class AdjacentVerticesIterator
      implements Iterator<Integer>
    {
      private final Iterator<Integer> vertices;
      

      public AdjacentVerticesIterator()
      {
        vertices = adjacent.iterator();
      }
      
      public boolean hasNext() {
        return vertices.hasNext();
      }
      
      public Integer next() {
        return (Integer)vertices.next();
      }
      
      public void remove() {
        throw new UnsupportedOperationException("cannot remove an edge to an adjacenct vertices using this iterator; use remove() instead");
      }
    }
  }
  


  private class EdgeView
    extends AbstractSet<T>
  {
    public EdgeView() {}
    


    public boolean add(T e)
    {
      return AbstractGraph.this.add(e);
    }
    
    public boolean contains(Object o) {
      return ((o instanceof Edge)) && (contains((Edge)o));
    }
    
    public Iterator<T> iterator() {
      return new EdgeViewIterator();
    }
    
    public boolean remove(Object o) {
      return ((o instanceof Edge)) && (remove((Edge)o));
    }
    
    public int size() {
      return numEdges;
    }
    





    private class EdgeViewIterator
      implements Iterator<T>
    {
      private final Iterator<S> vertices;
      




      private Iterator<T> edges;
      




      private Iterator<T> toRemoveFrom;
      



      private T next;
      



      private T cur;
      



      private int curRoot = -1;
      
      public EdgeViewIterator() {
        vertices = vertexToEdges.valueCollection().iterator();
        advance();
      }
      




      private void advance()
      {
        next = null;
        if (((edges == null) || (!edges.hasNext())) && (!vertices.hasNext())) {}
        do { return;
          

          while (((edges == null) || (!edges.hasNext())) && 
            (vertices.hasNext())) {
            S edgeSet = (EdgeSet)vertices.next();
            curRoot = edgeSet.getRoot();
            edges = edgeSet.iterator();
          }
          
          if ((edges == null) || (!edges.hasNext())) {
            return;
          }
          T e = (Edge)edges.next();
          





          if (((curRoot == e.from()) && (curRoot < e.to())) || (
            (curRoot == e.to()) && (curRoot < e.from())))
            next = e;
        } while (next == null);
      }
      
      public boolean hasNext() {
        return next != null;
      }
      
      public T next() {
        if (!hasNext())
          throw new NoSuchElementException();
        cur = next;
        

        if (toRemoveFrom != edges)
          toRemoveFrom = edges;
        advance();
        return cur;
      }
      
      public void remove() {
        if (cur == null)
          throw new IllegalStateException("no element to remove");
        remove(cur);
        cur = null;
      }
    }
  }
  






  protected class Subgraph
    implements Graph<T>
  {
    private final IntSet vertexSubset;
    






    public Subgraph()
    {
      vertexSubset = new TroveIntSet(vertices);
    }
    


    public boolean add(int v)
    {
      if (vertexSubset.contains(v))
        return false;
      throw new UnsupportedOperationException(
        "Cannot add a vertext to a subgraph");
    }
    


    public boolean add(T e)
    {
      return (vertexSubset.contains(e.from())) && 
        (vertexSubset.contains(e.to())) && 
        (AbstractGraph.this.add(e));
    }
    


    public void clear()
    {
      for (IntIterator it = vertexSubset.iterator(); it.hasNext();) {
        int v = it.nextInt();
        AbstractGraph.this.remove(v);
      }
      vertexSubset.clear();
    }
    


    public void clearEdges()
    {
      for (T e : edges()) {
        AbstractGraph.this.remove(e);
      }
    }
    

    public boolean contains(int vertex1, int vertex2)
    {
      return (vertexSubset.contains(vertex1)) && 
        (vertexSubset.contains(vertex2)) && 
        (AbstractGraph.this.contains(vertex1, vertex2));
    }
    


    public boolean contains(Edge e)
    {
      return (vertexSubset.contains(e.from())) && 
        (vertexSubset.contains(e.to())) && 
        (AbstractGraph.this.contains(e));
    }
    


    public boolean contains(int v)
    {
      return (vertexSubset.contains(v)) && 
        (AbstractGraph.this.contains(v));
    }
    
    public Graph<T> copy(Set<Integer> vertices) {
      Graph<T> g = 
        AbstractGraph.this.copy(Collections.emptySet());
      Iterator localIterator2; for (Iterator localIterator1 = vertices.iterator(); localIterator1.hasNext(); 
          



          localIterator2.hasNext())
      {
        int v = ((Integer)localIterator1.next()).intValue();
        if (!contains(v))
          throw new IllegalArgumentException(
            "Requested copy with non-existant vertex: " + v);
        g.add(v);
        localIterator2 = getAdjacencyList(v).iterator(); continue;T e = (Edge)localIterator2.next();
        if ((vertices.contains(Integer.valueOf(e.from()))) && 
          (vertices.contains(Integer.valueOf(e.to()))))
          g.add(e);
      }
      return g;
    }
    


    public int degree(int vertex)
    {
      int d = 0;
      for (Iterator localIterator = AbstractGraph.this.getNeighbors(vertex).iterator(); localIterator.hasNext();) { int n = ((Integer)localIterator.next()).intValue();
        if (vertexSubset.contains(n))
          d++; }
      return d;
    }
    


    public Set<T> edges()
    {
      return new SubgraphEdgeView();
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
    


    public Set<T> getAdjacencyList(int vertex)
    {
      Set<T> adjList = AbstractGraph.this.getAdjacencyList(vertex);
      return adjList.isEmpty() ? 
        adjList : 
        new SubgraphAdjacencyListView(vertex);
    }
    


    public IntSet getNeighbors(int vertex)
    {
      return !vertexSubset.contains(vertex) ? 
        PrimitiveCollections.emptyIntSet() : 
        new SubgraphNeighborsView(vertex);
    }
    














    public Set<T> getEdges(int vertex1, int vertex2)
    {
      return (vertexSubset.contains(vertex1)) && 
        (vertexSubset.contains(vertex2)) ? 
        AbstractGraph.this.getEdges(vertex1, vertex2) : 
        Collections.emptySet();
    }
    


    public boolean hasCycles()
    {
      throw new UnsupportedOperationException("fix me");
    }
    


    public int hashCode()
    {
      return vertices().hashCode();
    }
    


    public Iterator<Integer> iterator()
    {
      return vertices().iterator();
    }
    

    public int order()
    {
      return vertexSubset.size();
    }
    


    public boolean remove(Edge e)
    {
      return (vertexSubset.contains(e.from())) && 
        (vertexSubset.contains(e.to())) && 
        (AbstractGraph.this.remove(e));
    }
    


    public boolean remove(int vertex)
    {
      return (vertexSubset.contains(vertex)) && 
        (AbstractGraph.this.remove(vertex));
    }
    





    public int size()
    {
      int numEdges = 0;
      for (IntIterator it = vertexSubset.iterator(); it.hasNext();) {
        int v = it.nextInt();
        EdgeSet<T> edges = getEdgeSet(v);
        IntIterator it2 = vertexSubset.iterator();
        while (it2.hasNext()) {
          int v2 = it2.nextInt();
          if (v == v2)
            break;
          if (edges.connects(v2))
            numEdges++;
        }
      }
      return numEdges;
    }
    


    public Graph<T> subgraph(Set<Integer> vertices)
    {
      if (!vertexSubset.containsAll(vertices)) {
        throw new IllegalArgumentException("provided set is not a subset of the vertices of this graph");
      }
      return AbstractGraph.this.subgraph(vertices);
    }
    
    public String toString() {
      return "{ vertices: " + vertices() + ", edges: " + edges() + "}";
    }
    


    public IntSet vertices()
    {
      return PrimitiveCollections.unmodifiableSet(vertexSubset);
    }
    


    private class SubgraphAdjacencyListView
      extends AbstractSet<T>
    {
      private final int root;
      

      public SubgraphAdjacencyListView(int root)
      {
        this.root = root;
      }
      
      public boolean add(T edge) {
        return ((edge.from() == root) || 
          (edge.to() == root)) && 
          (AbstractGraph.Subgraph.this.add(edge));
      }
      
      public boolean contains(Object o) {
        if (!(o instanceof Edge))
          return false;
        Edge e = (Edge)o;
        return ((e.to() == root) || 
          (e.from() == root)) && 
          (contains(e));
      }
      
      public Iterator<T> iterator() {
        return new SubgraphAdjacencyListIterator();
      }
      
      public boolean remove(Object o) {
        if (!(o instanceof Edge))
          return false;
        Edge e = (Edge)o;
        return ((e.to() == root) || 
          (e.from() == root)) && 
          (remove(e));
      }
      
      public int size() {
        int sz = 0;
        for (IntIterator it = vertexSubset.iterator(); it.hasNext();) {
          int v = it.nextInt();
          if (v != root)
          {
            Set<T> edges = AbstractGraph.this.getEdges(v, root);
            sz += edges.size();
          } }
        return sz;
      }
      


      private class SubgraphAdjacencyListIterator
        implements Iterator<T>
      {
        private final Iterator<T> edges;
        


        public SubgraphAdjacencyListIterator()
        {
          List<Iterator<T>> iters = new LinkedList();
          
          IntIterator it = vertexSubset.iterator();
          while (it.hasNext()) {
            int v = it.nextInt();
            if (v != root)
            {
              Set<T> edges = AbstractGraph.this.getEdges(v, root);
              if (!edges.isEmpty())
                iters.add(edges.iterator());
            } }
          this.edges = new CombinedIterator(iters);
        }
        
        public boolean hasNext() {
          return edges.hasNext();
        }
        
        public T next() {
          return (Edge)edges.next();
        }
        
        public void remove() {
          throw new UnsupportedOperationException();
        }
      }
    }
    


    private class SubgraphEdgeView
      extends AbstractSet<T>
    {
      public SubgraphEdgeView() {}
      

      public boolean add(T e)
      {
        return AbstractGraph.Subgraph.this.add(e);
      }
      
      public boolean contains(T o) {
        return AbstractGraph.Subgraph.this.contains(o);
      }
      
      public Iterator<T> iterator() {
        return new SubgraphEdgeIterator();
      }
      
      public boolean remove(Object o) {
        return ((o instanceof Edge)) && 
          (remove((Edge)o));
      }
      
      public int size() {
        return AbstractGraph.Subgraph.this.size();
      }
      

      private class SubgraphEdgeIterator
        implements Iterator<T>
      {
        private final Queue<T> edgesToReturn;
        
        private final Queue<Integer> remainingVertices;
        
        private Iterator<Integer> possibleNeighbors;
        
        private Integer curVertex;
        
        public SubgraphEdgeIterator()
        {
          remainingVertices = new ArrayDeque(vertexSubset);
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
        
        public T next() {
          if (!hasNext())
            throw new NoSuchElementException();
          T n = (Edge)edgesToReturn.poll();
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
        return (vertexSubset.contains(vertex)) && (checkVertex(vertex));
      }
      
      public boolean contains(Object o) {
        if (!(o instanceof Integer))
          return false;
        Integer i = (Integer)o;
        return (vertexSubset.contains(i)) && (checkVertex(i.intValue()));
      }
      


      private boolean checkVertex(int i)
      {
        return AbstractGraph.this.contains(i, root);
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
        IntIterator it = vertexSubset.iterator();
        while (it.hasNext()) {
          int v = it.nextInt();
          if (checkVertex(v))
            sz++;
        }
        return sz;
      }
      


      private class SubgraphNeighborsIterator
        implements IntIterator
      {
        private final IntIterator iter;
        
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
            int v = ((Integer)iter.next()).intValue();
            if (AbstractGraph.Subgraph.SubgraphNeighborsView.this.checkVertex(v))
              next = Integer.valueOf(v);
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
