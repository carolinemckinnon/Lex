package edu.ucla.sspace.graph;

import edu.ucla.sspace.util.primitive.IntIterator;
import edu.ucla.sspace.util.primitive.IntSet;
import edu.ucla.sspace.util.primitive.TroveIntSet;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.procedure.TIntObjectProcedure;
import gnu.trove.set.hash.TIntHashSet;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;





























































public class SparseDirectedTypedEdgeSet<T>
  extends AbstractSet<DirectedTypedEdge<T>>
  implements EdgeSet<DirectedTypedEdge<T>>, Serializable
{
  private static final long serialVersionUID = 1L;
  private static final List<Object> TYPES = new ArrayList();
  




  private static final Map<Object, Integer> TYPE_INDICES = new HashMap();
  private final int rootVertex;
  private final TIntObjectHashMap<BitSet> inEdges;
  
  private static int index(Object o)
  {
    Integer i = (Integer)TYPE_INDICES.get(o);
    if (i == null) {
      synchronized (TYPE_INDICES)
      {
        i = (Integer)TYPE_INDICES.get(o);
        if (i != null) {
          return i.intValue();
        }
        int j = TYPE_INDICES.size();
        TYPE_INDICES.put(o, Integer.valueOf(j));
        TYPES.add(o);
        return j;
      }
    }
    
    return i.intValue();
  }
  

































  public SparseDirectedTypedEdgeSet(int rootVertex)
  {
    this.rootVertex = rootVertex;
    inEdges = new TIntObjectHashMap();
    outEdges = new TIntObjectHashMap();
    connected = new TIntHashSet();
    size = 0;
  }
  



  public boolean add(DirectedTypedEdge<T> e)
  {
    if (e.from() == rootVertex)
      return add(outEdges, e.to(), e.edgeType());
    if (e.to() == rootVertex)
      return add(inEdges, e.from(), e.edgeType());
    return false;
  }
  



  private boolean add(TIntObjectHashMap<BitSet> edges, int i, T type)
  {
    BitSet types = (BitSet)edges.get(i);
    

    if (types == null) {
      types = new BitSet();
      edges.put(i, types);
      types.set(index(type));
      connected.add(i);
      size += 1;
      return true;
    }
    

    int index = index(type);
    if (!types.get(index)) {
      types.set(index);
      connected.add(i);
      size += 1;
      return true;
    }
    

    return false;
  }
  


  public void clear()
  {
    inEdges.clear();
    outEdges.clear();
    connected.clear();
    size = 0;
  }
  


  public IntSet connected()
  {
    return TroveIntSet.wrap(connected);
  }
  


  public boolean connects(int vertex)
  {
    return connected.contains(vertex);
  }
  


  public boolean connects(int vertex, T type)
  {
    BitSet types = (BitSet)inEdges.get(vertex);
    if ((types != null) && (types.get(index(type))))
      return true;
    types = (BitSet)outEdges.get(vertex);
    return (types != null) && (types.get(index(type)));
  }
  


  public boolean contains(Object o)
  {
    if (!(o instanceof DirectedTypedEdge)) {
      return false;
    }
    DirectedTypedEdge<T> e = (DirectedTypedEdge)o;
    
    if (e.from() == rootVertex)
      return contains(outEdges, e.to(), e.edgeType());
    if (e.to() == rootVertex)
      return contains(inEdges, e.from(), e.edgeType());
    return false;
  }
  
  private boolean contains(TIntObjectHashMap<BitSet> edges, int i, T type) {
    BitSet types = (BitSet)edges.get(i);
    if (types == null)
      return false;
    int index = index(type);
    return types.get(index);
  }
  


  public SparseDirectedTypedEdgeSet<T> copy(IntSet vertices)
  {
    SparseDirectedTypedEdgeSet<T> copy = 
      new SparseDirectedTypedEdgeSet(rootVertex);
    if ((vertices.size() < inEdges.size()) && 
      (vertices.size() < outEdges.size()))
    {
      IntIterator iter = vertices.iterator();
      while (iter.hasNext()) {
        int v = iter.nextInt();
        if (inEdges.containsKey(v))
          inEdges.put(v, (BitSet)outEdges.get(v));
        if (outEdges.containsKey(v)) {
          inEdges.put(v, (BitSet)outEdges.get(v));
        }
      }
    } else {
      TIntObjectIterator<BitSet> iter = inEdges.iterator();
      while (iter.hasNext()) {
        iter.advance();
        int v = iter.key();
        if (vertices.contains(v))
          inEdges.put(v, (BitSet)iter.value());
      }
      iter = outEdges.iterator();
      while (iter.hasNext()) {
        iter.advance();
        int v = iter.key();
        if (vertices.contains(v))
          outEdges.put(v, (BitSet)iter.value());
      }
    }
    return copy;
  }
  










  private final TIntObjectHashMap<BitSet> outEdges;
  









  private final TIntHashSet connected;
  









  private int size;
  









  public int disconnect(int v)
  {
    if (connected.remove(v)) {
      int removed = 0;
      BitSet b = (BitSet)inEdges.remove(v);
      if (b != null) {
        int edges = b.cardinality();
        size -= edges;
        removed += edges;
      }
      b = (BitSet)outEdges.remove(v);
      if (b != null) {
        int edges = b.cardinality();
        size -= edges;
        removed += edges;
      }
      assert (removed > 0) : 
        "connected removed an edge that wasn't listed elsewhere";
      return removed;
    }
    return 0;
  }
  


  public Set<DirectedTypedEdge<T>> getEdges(final T type)
  {
    if (!TYPE_INDICES.containsKey(type))
      return Collections.emptySet();
    final int typeIndex = index(type);
    final Set<DirectedTypedEdge<T>> edges = new HashSet();
    inEdges.forEachEntry(new TIntObjectProcedure() {
      public boolean execute(int v, BitSet types) {
        if (types.get(typeIndex))
          edges.add(new SimpleDirectedTypedEdge(
            type, v, rootVertex));
        return true;
      }
    });
    outEdges.forEachEntry(new TIntObjectProcedure() {
      public boolean execute(int v, BitSet types) {
        if (types.get(typeIndex))
          edges.add(new SimpleDirectedTypedEdge(
            type, rootVertex, v));
        return true;
      }
    });
    return edges;
  }
  


  public Set<DirectedTypedEdge<T>> getEdges(int vertex)
  {
    return new EdgesForVertex(vertex);
  }
  




  public Set<DirectedTypedEdge<T>> getEdges(int vertex, Set<T> types)
  {
    Set<DirectedTypedEdge<T>> edges = new HashSet();
    for (DirectedTypedEdge<T> e : new EdgesForVertex(vertex))
      if (types.contains(e.edgeType()))
        edges.add(e);
    return edges;
  }
  


  public int getRoot()
  {
    return rootVertex;
  }
  



  public Set<DirectedTypedEdge<T>> incoming()
  {
    throw new Error();
  }
  


  public boolean isEmpty()
  {
    return connected.isEmpty();
  }
  


  public Iterator<DirectedTypedEdge<T>> iterator()
  {
    return new EdgeIterator();
  }
  



  public Set<DirectedTypedEdge<T>> outgoing()
  {
    throw new Error();
  }
  
  public IntSet predecessors() {
    return TroveIntSet.wrap(inEdges.keySet());
  }
  
  public IntSet successors() {
    return TroveIntSet.wrap(outEdges.keySet());
  }
  


  public boolean remove(Object o)
  {
    if (!(o instanceof DirectedTypedEdge)) {
      return false;
    }
    
    DirectedTypedEdge<T> e = (DirectedTypedEdge)o;
    

    if (e.from() == rootVertex)
      return remove(outEdges, e.to(), e.edgeType());
    if (e.to() == rootVertex)
      return remove(inEdges, e.from(), e.edgeType());
    return false;
  }
  
  private boolean remove(TIntObjectHashMap<BitSet> edges, int i, T type) {
    BitSet types = (BitSet)edges.get(i);
    if (types == null)
      return false;
    int index = index(type);
    

    if (types.get(index)) {
      types.set(index, false);
      
      if (types.cardinality() == 0) {
        edges.remove(i);
        size -= 1;
        

        TIntObjectHashMap<BitSet> other = edges == inEdges ? 
          outEdges : inEdges;
        if (!other.containsKey(i))
          connected.remove(i);
      }
      return true;
    }
    return false;
  }
  


  public int size()
  {
    return size;
  }
  


  public Set<T> types()
  {
    throw new Error();
  }
  


  public Iterator<DirectedTypedEdge<T>> uniqueIterator()
  {
    return new UniqueEdgeIterator();
  }
  
  private void writeObject(ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();
    



    out.writeObject(TYPE_INDICES);
  }
  
  private void readObject(ObjectInputStream in)
    throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    



    Map<Object, Integer> typeIndices = 
      (Map)Map.class.cast(in.readObject());
    boolean needToRemapIndices = true;
    Iterator localIterator; if (!TYPE_INDICES.equals(typeIndices)) {
      if (TYPE_INDICES.isEmpty()) {
        synchronized (TYPE_INDICES)
        {


          if (TYPE_INDICES.isEmpty()) {
            TYPE_INDICES.putAll(typeIndices);
            


            for (int i = 0; i < TYPE_INDICES.size(); i++) {
              TYPES.add(null);
            }
            localIterator = TYPE_INDICES.entrySet().iterator();
            while (localIterator.hasNext()) {
              Map.Entry<Object, Integer> e = (Map.Entry)localIterator.next();
              TYPES.set(((Integer)e.getValue()).intValue(), e.getKey());
            }
            needToRemapIndices = false;
          }
          
        }
        
      }
    }
    else
    {
      boolean foundMismatch = false;
      for (Map.Entry<Object, Integer> e : typeIndices.entrySet()) {
        Object o = e.getKey();
        int oldIndex = ((Integer)e.getValue()).intValue();
        Integer curIndex = (Integer)TYPE_INDICES.get(o);
        



        if (curIndex == null)
        {

          while (TYPES.size() <= oldIndex)
            TYPES.add(null);
          TYPES.set(oldIndex, o);
          TYPE_INDICES.put(o, Integer.valueOf(oldIndex));
        }
        else if (curIndex.intValue() != oldIndex) {
          foundMismatch = true;
        }
      }
      



      if (!foundMismatch) {
        needToRemapIndices = false;
      }
    }
    


    if (needToRemapIndices) {
      TIntIntMap typeRemapping = new TIntIntHashMap();
      for (Map.Entry<Object, Integer> e : typeIndices.entrySet()) {
        Object o = e.getKey();
        int oldIndex = ((Integer)e.getValue()).intValue();
        


        typeRemapping.put(oldIndex, index(o));
      }
      
      for (TIntObjectIterator<BitSet> it = inEdges.iterator(); it.hasNext();) {
        it.advance();
        int v = it.key();
        BitSet oldIndices = (BitSet)it.value();
        BitSet newIndices = new BitSet();
        for (int i = oldIndices.nextSetBit(0); i >= 0; 
            i = oldIndices.nextSetBit(i + 1)) {
          newIndices.set(typeRemapping.get(i));
        }
        it.setValue(newIndices);
      }
      
      for (TIntObjectIterator<BitSet> it = outEdges.iterator(); it.hasNext();) {
        it.advance();
        int v = it.key();
        BitSet oldIndices = (BitSet)it.value();
        BitSet newIndices = new BitSet();
        for (int i = oldIndices.nextSetBit(0); i >= 0; 
            i = oldIndices.nextSetBit(i + 1)) {
          newIndices.set(typeRemapping.get(i));
        }
        it.setValue(newIndices);
      }
    }
  }
  



  private class EdgesForVertex
    extends AbstractSet<DirectedTypedEdge<T>>
  {
    private final int otherVertex;
    



    public EdgesForVertex(int otherVertex)
    {
      this.otherVertex = otherVertex;
    }
    
    public boolean add(DirectedTypedEdge<T> e) {
      return ((e.to() == rootVertex) && (e.from() == otherVertex)) || (
        (e.from() == rootVertex) && (e.to() == otherVertex) && 
        (SparseDirectedTypedEdgeSet.this.add(e)));
    }
    
    public boolean contains(Object o) {
      if (!(o instanceof DirectedTypedEdge))
        return false;
      DirectedTypedEdge<?> e = (DirectedTypedEdge)o;
      return ((e.to() == rootVertex) && (e.from() == otherVertex)) || (
        (e.from() == rootVertex) && (e.to() == otherVertex) && 
        (SparseDirectedTypedEdgeSet.this.contains(e)));
    }
    
    public boolean isEmpty() {
      return !connects(otherVertex);
    }
    
    public Iterator<DirectedTypedEdge<T>> iterator() {
      return new SparseDirectedTypedEdgeSet.EdgesForVertexIterator(SparseDirectedTypedEdgeSet.this, otherVertex);
    }
    
    public boolean remove(Object o) {
      if (!(o instanceof DirectedTypedEdge))
        return false;
      DirectedTypedEdge<?> e = (DirectedTypedEdge)o;
      return ((e.to() == rootVertex) && (e.from() == otherVertex)) || (
        (e.from() == rootVertex) && (e.to() == otherVertex) && 
        (SparseDirectedTypedEdgeSet.this.remove(e)));
    }
    
    public int size() {
      BitSet in = (BitSet)inEdges.get(otherVertex);
      BitSet out = (BitSet)outEdges.get(otherVertex);
      return (in == null ? 0 : in.cardinality()) + (
        out == null ? 0 : out.cardinality());
    }
  }
  


  private class EdgesForVertexIterator
    implements Iterator<DirectedTypedEdge<T>>
  {
    private int curTypeIndex;
    

    private BitSet curTypes;
    

    private DirectedTypedEdge<T> next;
    

    boolean areInEdges;
    
    int otherVertex;
    

    public EdgesForVertexIterator(int otherVertex)
    {
      this.otherVertex = otherVertex;
      areInEdges = true;
      curTypeIndex = -1;
      curTypes = ((BitSet)inEdges.get(otherVertex));
      advance();
    }
    
    private void advance() {
      next = null;
      while (next == null) {
        if ((curTypes == null) && (areInEdges)) {
          curTypes = ((BitSet)outEdges.get(otherVertex));
          areInEdges = false;
          curTypeIndex = -1;
        }
        
        if (curTypes == null)
          break;
        curTypeIndex = curTypes.nextSetBit(curTypeIndex + 1);
        if (curTypeIndex >= 0)
        {

          T type = SparseDirectedTypedEdgeSet.TYPES.get(curTypeIndex);
          next = (areInEdges ? 
            new SimpleDirectedTypedEdge(
            type, otherVertex, rootVertex) : 
            new SimpleDirectedTypedEdge(
            type, rootVertex, otherVertex));

        }
        else
        {
          curTypes = null;
        }
      }
    }
    
    public boolean hasNext() { return next != null; }
    
    public DirectedTypedEdge<T> next()
    {
      if (next == null)
        throw new NoSuchElementException();
      DirectedTypedEdge<T> n = next;
      advance();
      return n;
    }
    
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
  



  private class EdgeIterator
    implements Iterator<DirectedTypedEdge<T>>
  {
    private TIntObjectIterator<BitSet> iter;
    


    private int curTypeIndex;
    

    private BitSet curTypes;
    

    private DirectedTypedEdge<T> next;
    

    boolean areInEdges;
    


    public EdgeIterator()
    {
      areInEdges = true;
      curTypeIndex = -1;
      iter = inEdges.iterator();
      advance();
    }
    
    private void advance() {
      next = null;
      while (next == null) {
        if (curTypes == null) {
          if (!iter.hasNext())
            break;
          iter.advance();
          curTypeIndex = -1;
          curTypes = ((BitSet)iter.value());
        }
        

        curTypeIndex = curTypes.nextSetBit(curTypeIndex + 1);
        

        if (curTypeIndex >= 0)
        {

          T type = SparseDirectedTypedEdgeSet.TYPES.get(curTypeIndex);
          next = (areInEdges ? 
            new SimpleDirectedTypedEdge(
            type, iter.key(), rootVertex) : 
            new SimpleDirectedTypedEdge(
            type, rootVertex, iter.key()));

        }
        else
        {
          curTypes = null;
        }
      }
      
      if ((next == null) && (areInEdges)) {
        areInEdges = false;
        iter = outEdges.iterator();
        curTypes = null;
        advance();
      }
    }
    
    public boolean hasNext() {
      return next != null;
    }
    
    public DirectedTypedEdge<T> next() {
      if (next == null)
        throw new NoSuchElementException();
      DirectedTypedEdge<T> n = next;
      advance();
      return n;
    }
    
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
  



  private class UniqueEdgeIterator
    implements Iterator<DirectedTypedEdge<T>>
  {
    private TIntObjectIterator<BitSet> iter;
    

    private int curTypeIndex;
    

    private BitSet curTypes;
    

    private DirectedTypedEdge<T> next;
    

    boolean areInEdges;
    


    public UniqueEdgeIterator()
    {
      areInEdges = true;
      iter = inEdges.iterator();
      advance();
    }
    
    private void advance() {
      next = null;
      while (next == null) {
        if (curTypes == null) {
          if (!iter.hasNext())
            break;
          iter.advance();
          curTypeIndex = -1;
          int otherVertex = iter.key();
          

          if (((!areInEdges) || (rootVertex >= otherVertex)) && (
            (areInEdges) || (rootVertex >= otherVertex))) {
            curTypes = null;
          }
          else
          {
            curTypes = ((BitSet)iter.value());
          }
        } else {
          curTypeIndex = curTypes.nextSetBit(curTypeIndex + 1);
          if (curTypeIndex >= 0)
          {

            T type = SparseDirectedTypedEdgeSet.TYPES.get(curTypeIndex);
            next = (areInEdges ? 
              new SimpleDirectedTypedEdge(
              type, iter.key(), rootVertex) : 
              new SimpleDirectedTypedEdge(
              type, rootVertex, iter.key()));

          }
          else
          {
            curTypes = null;
          }
        }
      }
      if ((next == null) && (areInEdges)) {
        areInEdges = false;
        iter = outEdges.iterator();
        curTypes = null;
        advance();
      }
    }
    
    public boolean hasNext() {
      return next != null;
    }
    
    public DirectedTypedEdge<T> next() {
      if (next == null)
        throw new NoSuchElementException();
      DirectedTypedEdge<T> n = next;
      
      advance();
      return n;
    }
    
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
  



  private class InEdgeIterator
    implements Iterator<DirectedTypedEdge<T>>
  {
    private final TIntObjectIterator<BitSet> iter;
    


    private int curTypeIndex;
    

    private BitSet curTypes;
    

    private DirectedTypedEdge<T> next;
    


    public InEdgeIterator()
    {
      iter = inEdges.iterator();
      advance();
    }
    
    private void advance() {
      next = null;
      while (next == null) {
        if (curTypes == null) {
          if (!iter.hasNext())
            break;
          iter.advance();
          curTypeIndex = -1;
          curTypes = ((BitSet)iter.value());
        }
        

        curTypeIndex = curTypes.nextSetBit(curTypeIndex + 1);
        if (curTypeIndex >= 0)
        {

          T type = SparseDirectedTypedEdgeSet.TYPES.get(curTypeIndex);
          next = new SimpleDirectedTypedEdge(
            type, iter.key(), rootVertex);

        }
        else
        {
          curTypes = null;
        }
      }
    }
    
    public boolean hasNext() { return next != null; }
    
    public DirectedTypedEdge<T> next()
    {
      if (next == null)
        throw new NoSuchElementException();
      DirectedTypedEdge<T> n = next;
      advance();
      return n;
    }
    
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
  



  private class OutEdgeIterator
    implements Iterator<DirectedTypedEdge<T>>
  {
    private final TIntObjectIterator<BitSet> iter;
    


    private BitSet curTypes;
    

    private int curTypeIndex;
    

    private DirectedTypedEdge<T> next;
    


    public OutEdgeIterator()
    {
      iter = outEdges.iterator();
      advance();
    }
    
    private void advance() {
      next = null;
      while (next == null) {
        if (curTypes == null) {
          if (!iter.hasNext())
            break;
          iter.advance();
          curTypeIndex = -1;
          curTypes = ((BitSet)iter.value());
        }
        

        curTypeIndex = curTypes.nextSetBit(curTypeIndex + 1);
        if (curTypeIndex >= 0)
        {

          T type = SparseDirectedTypedEdgeSet.TYPES.get(curTypeIndex);
          next = new SimpleDirectedTypedEdge(
            type, rootVertex, iter.key());

        }
        else
        {
          curTypes = null;
        }
      }
    }
    
    public boolean hasNext() { return next != null; }
    
    public DirectedTypedEdge<T> next()
    {
      if (next == null)
        throw new NoSuchElementException();
      DirectedTypedEdge<T> n = next;
      advance();
      return n;
    }
    
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
}
