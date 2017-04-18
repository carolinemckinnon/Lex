package edu.ucla.sspace.graph;

import edu.ucla.sspace.util.primitive.IntSet;
import edu.ucla.sspace.util.primitive.TroveIntSet;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;













































public class SparseDirectedGraph
  extends AbstractGraph<DirectedEdge, SparseDirectedEdgeSet>
  implements DirectedGraph<DirectedEdge>
{
  private static final long serialVersionUID = 1L;
  
  public SparseDirectedGraph() {}
  
  public SparseDirectedGraph(Set<Integer> vertices)
  {
    super(vertices);
  }
  



  public SparseDirectedGraph(Graph<? extends DirectedEdge> g)
  {
    for (Integer v : g.vertices())
      add(v.intValue());
    for (DirectedEdge e : g.edges()) {
      add(e);
    }
  }
  



  public DirectedGraph<DirectedEdge> copy(Set<Integer> vertices)
  {
    if ((vertices.size() == order()) && (vertices.equals(vertices())))
      return new SparseDirectedGraph(this);
    SparseDirectedGraph g = new SparseDirectedGraph();
    Iterator localIterator2; for (Iterator localIterator1 = vertices.iterator(); localIterator1.hasNext(); 
        



        localIterator2.hasNext())
    {
      int v = ((Integer)localIterator1.next()).intValue();
      if (!contains(v))
        throw new IllegalArgumentException(
          "Requested copy with non-existant vertex: " + v);
      g.add(v);
      localIterator2 = getAdjacencyList(v).iterator(); continue;DirectedEdge e = (DirectedEdge)localIterator2.next();
      if ((vertices.contains(Integer.valueOf(e.from()))) && (vertices.contains(Integer.valueOf(e.to()))))
        g.add(e);
    }
    return g;
  }
  



  protected SparseDirectedEdgeSet createEdgeSet(int vertex)
  {
    return new SparseDirectedEdgeSet(vertex);
  }
  


  public int inDegree(int vertex)
  {
    SparseDirectedEdgeSet edges = (SparseDirectedEdgeSet)getEdgeSet(vertex);
    return edges == null ? 0 : edges.inEdges().size();
  }
  


  public Set<DirectedEdge> inEdges(int vertex)
  {
    SparseDirectedEdgeSet edges = (SparseDirectedEdgeSet)getEdgeSet(vertex);
    return edges == null ? 
      Collections.emptySet() : 
      new EdgeSetDecorator(edges.inEdges());
  }
  


  public int outDegree(int vertex)
  {
    SparseDirectedEdgeSet edges = (SparseDirectedEdgeSet)getEdgeSet(vertex);
    return edges == null ? 0 : edges.outEdges().size();
  }
  


  public Set<DirectedEdge> outEdges(int vertex)
  {
    SparseDirectedEdgeSet edges = (SparseDirectedEdgeSet)getEdgeSet(vertex);
    return edges == null ? 
      Collections.emptySet() : 
      new EdgeSetDecorator(edges.outEdges());
  }
  


  public IntSet predecessors(int vertex)
  {
    IntSet preds = new TroveIntSet();
    for (DirectedEdge e : inEdges(vertex))
      preds.add(e.from());
    return preds;
  }
  


  public DirectedGraph subgraph(Set<Integer> vertices)
  {
    Graph<DirectedEdge> subgraph = super.subgraph(vertices);
    return new SubgraphAdaptor(subgraph);
  }
  


  public IntSet successors(int vertex)
  {
    IntSet succs = new TroveIntSet();
    for (DirectedEdge e : outEdges(vertex))
      succs.add(e.to());
    return succs;
  }
  

  private class EdgeSetDecorator
    extends AbstractSet<DirectedEdge>
  {
    private final Set<DirectedEdge> edges;
    

    public EdgeSetDecorator()
    {
      this.edges = edges;
    }
    


    public boolean add(DirectedEdge e)
    {
      return add(e);
    }
    
    public boolean contains(Object o) {
      return edges.contains(o);
    }
    
    public Iterator<DirectedEdge> iterator() {
      return new EdgeSetIteratorDecorator();
    }
    
    public boolean remove(Object o) {
      if (!(o instanceof DirectedEdge)) {
        return false;
      }
      

      return remove((DirectedEdge)o);
    }
    
    public int size() {
      return edges.size();
    }
    
    private class EdgeSetIteratorDecorator
      implements Iterator<DirectedEdge>
    {
      private final Iterator<DirectedEdge> iter;
      private boolean alreadyRemoved;
      
      public EdgeSetIteratorDecorator()
      {
        iter = edges.iterator();
        alreadyRemoved = true;
      }
      
      public boolean hasNext() {
        return iter.hasNext();
      }
      
      public DirectedEdge next() {
        alreadyRemoved = false;
        return (DirectedEdge)iter.next();
      }
      







      public void remove()
      {
        throw new UnsupportedOperationException();
      }
    }
  }
  



  private class SubgraphAdaptor
    extends GraphAdaptor<DirectedEdge>
    implements DirectedGraph<DirectedEdge>, Serializable
  {
    private static final long serialVersionUID = 1L;
    



    public SubgraphAdaptor()
    {
      super();
    }
    




    public DirectedGraph<DirectedEdge> copy(Set<Integer> vertices)
    {
      if ((vertices.size() == order()) && (vertices.equals(vertices())))
        return new SparseDirectedGraph(this);
      SparseDirectedGraph g = new SparseDirectedGraph();
      Iterator localIterator2; for (Iterator localIterator1 = vertices.iterator(); localIterator1.hasNext(); 
          



          localIterator2.hasNext())
      {
        int v = ((Integer)localIterator1.next()).intValue();
        if (!contains(v))
          throw new IllegalArgumentException(
            "Requested copy with non-existant vertex: " + v);
        g.add(v);
        localIterator2 = getAdjacencyList(v).iterator(); continue;DirectedEdge e = (DirectedEdge)localIterator2.next();
        if ((vertices.contains(Integer.valueOf(e.from()))) && 
          (vertices.contains(Integer.valueOf(e.to()))))
          g.add(e);
      }
      return g;
    }
    


    public int inDegree(int vertex)
    {
      int degree = 0;
      Set<DirectedEdge> edges = getAdjacencyList(vertex);
      if (edges.isEmpty())
        return 0;
      for (DirectedEdge e : edges) {
        if (e.to() == vertex)
          degree++;
      }
      return degree;
    }
    





    public Set<DirectedEdge> inEdges(int vertex)
    {
      Set<DirectedEdge> edges = getAdjacencyList(vertex);
      if (edges.isEmpty()) {
        return Collections.emptySet();
      }
      Set<DirectedEdge> in = new HashSet();
      for (DirectedEdge e : edges) {
        if (e.to() == vertex)
          in.add(e);
      }
      return in;
    }
    


    public int outDegree(int vertex)
    {
      int degree = 0;
      Set<DirectedEdge> edges = getAdjacencyList(vertex);
      if (edges.isEmpty())
        return 0;
      for (DirectedEdge e : edges) {
        if (e.from() == vertex)
          degree++;
      }
      return degree;
    }
    





    public Set<DirectedEdge> outEdges(int vertex)
    {
      Set<DirectedEdge> edges = getAdjacencyList(vertex);
      if (edges.isEmpty())
        return Collections.emptySet();
      Set<DirectedEdge> out = new HashSet();
      for (DirectedEdge e : edges) {
        if (e.from() == vertex)
          out.add(e);
      }
      return out;
    }
    


    public IntSet predecessors(int vertex)
    {
      IntSet preds = new TroveIntSet();
      for (DirectedEdge e : inEdges(vertex))
        preds.add(e.from());
      return preds;
    }
    


    public IntSet successors(int vertex)
    {
      IntSet succs = new TroveIntSet();
      for (DirectedEdge e : outEdges(vertex))
        succs.add(e.to());
      return succs;
    }
    


    public DirectedGraph subgraph(Set<Integer> vertices)
    {
      Graph<DirectedEdge> g = super.subgraph(vertices);
      return new SubgraphAdaptor(SparseDirectedGraph.this, g);
    }
  }
}
