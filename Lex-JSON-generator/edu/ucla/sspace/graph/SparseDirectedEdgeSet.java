package edu.ucla.sspace.graph;

import edu.ucla.sspace.util.primitive.AbstractIntSet;
import edu.ucla.sspace.util.primitive.IntIterator;
import edu.ucla.sspace.util.primitive.IntSet;
import edu.ucla.sspace.util.primitive.TroveIntSet;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;














































public class SparseDirectedEdgeSet
  extends AbstractSet<DirectedEdge>
  implements EdgeSet<DirectedEdge>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final int rootVertex;
  private final IntSet outEdges;
  private final IntSet inEdges;
  
  public SparseDirectedEdgeSet(int rootVertex)
  {
    this.rootVertex = rootVertex;
    outEdges = new TroveIntSet();
    inEdges = new TroveIntSet();
  }
  



  public boolean add(DirectedEdge e)
  {
    if (e.from() == rootVertex) {
      return outEdges.add(e.to());
    }
    if (e.to() == rootVertex) {
      return inEdges.add(e.from());
    }
    return false;
  }
  


  public IntSet connected()
  {
    return new CombinedSet(null);
  }
  


  public boolean connects(int vertex)
  {
    return (inEdges.contains(vertex)) || (outEdges.contains(vertex));
  }
  


  public boolean contains(Object o)
  {
    if ((o instanceof DirectedEdge)) {
      DirectedEdge e = (DirectedEdge)o;
      if (e.to() == rootVertex)
        return inEdges.contains(e.from());
      if (e.from() == rootVertex)
        return outEdges.contains(e.to());
    }
    return false;
  }
  



  public SparseDirectedEdgeSet copy(IntSet vertices)
  {
    SparseDirectedEdgeSet copy = new SparseDirectedEdgeSet(rootVertex);
    if ((vertices.size() < inEdges.size()) && 
      (vertices.size() < outEdges.size()))
    {
      IntIterator iter = vertices.iterator();
      while (iter.hasNext()) {
        int v = iter.nextInt();
        if (inEdges.contains(v))
          inEdges.add(v);
        if (outEdges.contains(v)) {
          inEdges.add(v);
        }
      }
    } else {
      IntIterator iter = inEdges.iterator();
      while (iter.hasNext()) {
        int v = iter.nextInt();
        if (vertices.contains(v))
          inEdges.add(v);
      }
      iter = outEdges.iterator();
      while (iter.hasNext()) {
        int v = iter.nextInt();
        if (vertices.contains(v))
          outEdges.add(v);
      }
    }
    return copy;
  }
  


  public int disconnect(int vertex)
  {
    int removed = 0;
    if (inEdges.remove(vertex))
      removed++;
    if (outEdges.remove(vertex))
      removed++;
    return removed;
  }
  


  public Set<DirectedEdge> getEdges(int vertex)
  {
    return new VertexEdgeSet(vertex);
  }
  


  public int getRoot()
  {
    return rootVertex;
  }
  




  public Set<DirectedEdge> inEdges()
  {
    return new EdgeSetWrapper(inEdges, true);
  }
  


  public boolean isEmpty()
  {
    return (inEdges().isEmpty()) && (outEdges.isEmpty());
  }
  


  public Iterator<DirectedEdge> iterator()
  {
    return new DirectedEdgeIterator();
  }
  




  public Set<DirectedEdge> outEdges()
  {
    return new EdgeSetWrapper(outEdges, false);
  }
  


  public boolean remove(Object o)
  {
    if ((o instanceof DirectedEdge)) {
      DirectedEdge e = (DirectedEdge)o;
      if (e.to() == rootVertex)
        return inEdges.remove(e.from());
      if (e.from() == rootVertex)
        return outEdges.remove(e.to());
    }
    return false;
  }
  


  public int size()
  {
    return inEdges.size() + outEdges.size();
  }
  



  private class VertexEdgeSet
    extends AbstractSet<DirectedEdge>
  {
    private final int otherVertex;
    


    public VertexEdgeSet(int otherVertex)
    {
      this.otherVertex = otherVertex;
    }
    
    public boolean add(DirectedEdge e) {
      return ((e.to() == rootVertex) && (e.from() == otherVertex)) || (
        (e.from() == rootVertex) && (e.to() == otherVertex) && 
        (SparseDirectedEdgeSet.this.add(e)));
    }
    
    public boolean contains(Object o) {
      if (!(o instanceof DirectedEdge))
        return false;
      DirectedEdge e = (DirectedEdge)o;
      return ((e.to() == rootVertex) && (e.from() == otherVertex)) || (
        (e.from() == rootVertex) && (e.to() == otherVertex) && 
        (SparseDirectedEdgeSet.this.contains(e)));
    }
    
    public Iterator<DirectedEdge> iterator() {
      return new EdgeIterator();
    }
    
    public boolean remove(Object o) {
      if (!(o instanceof DirectedEdge))
        return false;
      DirectedEdge e = (DirectedEdge)o;
      return ((e.to() == rootVertex) && (e.from() == otherVertex)) || (
        (e.from() == rootVertex) && (e.to() == otherVertex) && 
        (SparseDirectedEdgeSet.this.remove(e)));
    }
    
    public int size() {
      int size = 0;
      if (inEdges.contains(otherVertex))
        size++;
      if (outEdges.contains(otherVertex))
        size++;
      return size;
    }
    



    class EdgeIterator
      implements Iterator<DirectedEdge>
    {
      DirectedEdge next;
      

      DirectedEdge cur;
      

      boolean returnedIn;
      

      boolean returnedOut;
      


      public EdgeIterator()
      {
        advance();
      }
      
      private void advance() {
        next = null;
        if ((inEdges.contains(otherVertex)) && (!returnedIn)) {
          next = new SimpleDirectedEdge(otherVertex, rootVertex);
          returnedIn = true;
        }
        else if ((next == null) && (!returnedOut) && 
          (outEdges.contains(otherVertex))) {
          next = new SimpleDirectedEdge(rootVertex, otherVertex);
          returnedOut = true;
        }
      }
      
      public boolean hasNext() {
        return next != null;
      }
      
      public DirectedEdge next() {
        cur = next;
        advance();
        return cur;
      }
      
      public void remove() {
        if (cur == null)
          throw new IllegalStateException();
        SparseDirectedEdgeSet.this.remove(cur);
        cur = null;
      }
    }
  }
  






  private class EdgeSetWrapper
    extends AbstractSet<DirectedEdge>
  {
    private final Set<Integer> vertices;
    




    private final boolean areInEdges;
    





    public EdgeSetWrapper(boolean vertices)
    {
      this.vertices = vertices;
      this.areInEdges = areInEdges;
    }
    
    public boolean add(DirectedEdge e) {
      if (areInEdges) {
        return vertices.add(Integer.valueOf(e.from()));
      }
      
      assert (e.from() == rootVertex) : "incorrect edge set view";
      return vertices.add(Integer.valueOf(e.to()));
    }
    
    public boolean contains(Object o)
    {
      if (!(o instanceof DirectedEdge))
        return false;
      DirectedEdge e = (DirectedEdge)o;
      if (areInEdges) {
        return vertices.contains(Integer.valueOf(e.from()));
      }
      
      assert (e.from() == rootVertex) : "incorrect edge set view";
      return vertices.contains(Integer.valueOf(e.to()));
    }
    
    public Iterator<DirectedEdge> iterator()
    {
      return new EdgeSetWrapperIterator();
    }
    
    public boolean remove(Object o) {
      if (!(o instanceof DirectedEdge))
        return false;
      DirectedEdge e = (DirectedEdge)o;
      if (areInEdges) {
        return vertices.remove(Integer.valueOf(e.from()));
      }
      
      assert (e.from() == rootVertex) : "incorrect edge set view";
      return vertices.remove(Integer.valueOf(e.to()));
    }
    
    public int size()
    {
      return vertices.size();
    }
    
    public class EdgeSetWrapperIterator implements Iterator<DirectedEdge>
    {
      private final Iterator<Integer> iter;
      
      public EdgeSetWrapperIterator() {
        iter = vertices.iterator();
      }
      
      public boolean hasNext() {
        return iter.hasNext();
      }
      
      public DirectedEdge next() {
        return areInEdges ? 
          new SimpleDirectedEdge(((Integer)iter.next()).intValue(), rootVertex) : 
          new SimpleDirectedEdge(rootVertex, ((Integer)iter.next()).intValue());
      }
      
      public void remove() {
        iter.remove();
      }
    }
  }
  


  private class DirectedEdgeIterator
    implements Iterator<DirectedEdge>
  {
    private Iterator<Integer> inVertices;
    

    private Iterator<Integer> outVertices;
    
    private Iterator<Integer> toRemoveFrom;
    

    public DirectedEdgeIterator()
    {
      inVertices = inEdges.iterator();
      outVertices = outEdges.iterator();
      toRemoveFrom = null;
    }
    
    public boolean hasNext() {
      return (inVertices.hasNext()) || (outVertices.hasNext());
    }
    
    public DirectedEdge next() {
      if (!hasNext())
        throw new NoSuchElementException();
      int from = -1;int to = -1;
      if (inVertices.hasNext()) {
        from = ((Integer)inVertices.next()).intValue();
        to = rootVertex;
        toRemoveFrom = inVertices;
      }
      else {
        assert (outVertices.hasNext()) : "bad iterator logic";
        from = rootVertex;
        to = ((Integer)outVertices.next()).intValue();
        toRemoveFrom = outVertices;
      }
      return new SimpleDirectedEdge(from, to);
    }
    
    public void remove() {
      if (toRemoveFrom == null)
        throw new IllegalStateException("No element to remove");
      toRemoveFrom.remove();
    }
  }
  
  private class CombinedSet extends AbstractIntSet {
    private CombinedSet() {}
    
    public boolean contains(int i) { return (inEdges.contains(i)) || (outEdges.contains(i)); }
    
    public boolean contains(Object o)
    {
      if (!(o instanceof Integer))
        return false;
      int i = ((Integer)o).intValue();
      return (inEdges.contains(i)) || (outEdges.contains(i));
    }
    

    public IntIterator iterator() { return new SparseDirectedEdgeSet.CombinedIterator(SparseDirectedEdgeSet.this); }
    
    public int size() { IntSet larger;
      IntSet smaller;
      IntSet larger;
      if (inEdges.size() < outEdges.size()) {
        IntSet smaller = inEdges;
        larger = outEdges;
      }
      else {
        smaller = outEdges;
        larger = inEdges;
      }
      int size = larger.size();
      IntIterator iter = smaller.iterator();
      while (iter.hasNext()) {
        int i = iter.nextInt();
        if (!larger.contains(i))
          size++;
      }
      return size;
    }
  }
  
  private class CombinedIterator implements IntIterator
  {
    private IntIterator largerIter;
    private IntIterator smallerIter;
    private IntSet larger;
    int next;
    boolean hasNext;
    
    public CombinedIterator()
    {
      if (inEdges.size() < outEdges.size()) {
        smallerIter = inEdges.iterator();
        largerIter = outEdges.iterator();
        larger = outEdges;
      }
      else {
        smallerIter = outEdges.iterator();
        largerIter = inEdges.iterator();
        larger = inEdges;
      }
      advance();
    }
    
    private void advance() {
      hasNext = false;
      if (largerIter.hasNext()) {
        next = largerIter.nextInt();
        hasNext = true;
      }
      else {
        while (smallerIter.hasNext()) {
          int i = smallerIter.nextInt();
          if (!larger.contains(i)) {
            next = i;
            hasNext = true;
            break;
          }
        }
      }
    }
    
    public boolean hasNext() {
      return hasNext;
    }
    
    public int nextInt() {
      if (!hasNext())
        throw new NoSuchElementException();
      int n = next;
      advance();
      return n;
    }
    
    public Integer next() {
      return Integer.valueOf(nextInt());
    }
    
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
}
