package edu.ucla.sspace.graph;

import edu.ucla.sspace.util.primitive.CompactIntSet;
import edu.ucla.sspace.util.primitive.IntIterator;
import edu.ucla.sspace.util.primitive.IntSet;
import edu.ucla.sspace.util.primitive.TroveIntSet;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.procedure.TIntObjectProcedure;
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



























































public class CompactSparseTypedEdgeSet<T>
  extends AbstractSet<TypedEdge<T>>
  implements EdgeSet<TypedEdge<T>>, Serializable
{
  private static final long serialVersionUID = 1L;
  private static final List<Object> TYPES = new ArrayList();
  




  private static final Map<Object, Integer> TYPE_INDICES = new HashMap();
  private final int rootVertex;
  private final TIntObjectHashMap<BitSet> edges;
  private int size;
  private BitSet setTypes;
  
  private static int index(Object o) { Integer i = (Integer)TYPE_INDICES.get(o);
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
  






















  public CompactSparseTypedEdgeSet(int rootVertex)
  {
    this.rootVertex = rootVertex;
    edges = new TIntObjectHashMap();
    setTypes = new BitSet();
    size = 0;
  }
  



  public boolean add(TypedEdge<T> e)
  {
    if (e.from() == rootVertex)
      return add(edges, e.to(), e.edgeType());
    if (e.to() == rootVertex)
      return add(edges, e.from(), e.edgeType());
    return false;
  }
  



  private boolean add(TIntObjectHashMap<BitSet> edges, int i, T type)
  {
    BitSet types = (BitSet)edges.get(i);
    

    if (types == null) {
      types = new BitSet();
      edges.put(i, types);
      types.set(index(type));
      size += 1;
      return true;
    }
    

    int index = index(type);
    setTypes.set(index);
    if (!types.get(index)) {
      types.set(index);
      size += 1;
      return true;
    }
    

    return false;
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
    BitSet types = (BitSet)edges.get(vertex);
    return (types != null) && (types.get(index(type)));
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
  
  private boolean contains(TIntObjectHashMap<BitSet> edges, int i, T type) {
    BitSet types = (BitSet)edges.get(i);
    if (types == null)
      return false;
    int index = index(type);
    return types.get(index);
  }
  


  public CompactSparseTypedEdgeSet<T> copy(IntSet vertices)
  {
    CompactSparseTypedEdgeSet<T> copy = new CompactSparseTypedEdgeSet(rootVertex);
    
    if (vertices.size() < edges.size()) {
      IntIterator iter = vertices.iterator();
      while (iter.hasNext()) {
        int v = iter.nextInt();
        if (edges.containsKey(v)) {
          BitSet b = (BitSet)edges.get(v);
          BitSet b2 = new BitSet();
          b2.or(b);
          edges.put(v, b2);
        }
      }
    }
    else {
      TIntObjectIterator<BitSet> iter = edges.iterator();
      while (iter.hasNext()) {
        iter.advance();
        int v = iter.key();
        if (vertices.contains(v)) {
          BitSet b = (BitSet)iter.value();
          BitSet b2 = new BitSet();
          b2.or(b);
          edges.put(v, b2);
        }
      }
    }
    return copy;
  }
  


  public int disconnect(int v)
  {
    BitSet b = (BitSet)this.edges.remove(v);
    if (b != null) {
      int edges = b.cardinality();
      size -= edges;
      return edges;
    }
    return 0;
  }
  


  public Set<TypedEdge<T>> getEdges(final T type)
  {
    if (!TYPE_INDICES.containsKey(type))
      return Collections.emptySet();
    final int typeIndex = index(type);
    final Set<TypedEdge<T>> s = new HashSet();
    edges.forEachEntry(new TIntObjectProcedure() {
      public boolean execute(int v, BitSet types) {
        if (types.get(typeIndex))
          s.add(new SimpleTypedEdge(
            type, v, rootVertex));
        return true;
      }
    });
    return s;
  }
  


  public Set<TypedEdge<T>> getEdges(int vertex)
  {
    BitSet b = (BitSet)edges.get(vertex);
    if (b == null)
      return Collections.emptySet();
    Set<TypedEdge<T>> s = new HashSet();
    for (int i = b.nextSetBit(0); i >= 0; i = b.nextSetBit(i + 1))
    {
      T type = TYPES.get(i);
      s.add(new SimpleTypedEdge(type, vertex, rootVertex));
    }
    return s;
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
      return remove(edges, e.to(), e.edgeType());
    if (e.to() == rootVertex)
      return remove(edges, e.from(), e.edgeType());
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
    return new Types(null);
  }
  


  public Iterator<TypedEdge<T>> uniqueIterator()
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
      
      for (TIntObjectIterator<BitSet> it = edges.iterator(); it.hasNext();) {
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
  
  private class Types
    extends AbstractSet<T>
  {
    private Types() {}
    
    public boolean contains(Object o)
    {
      if (CompactSparseTypedEdgeSet.TYPE_INDICES.containsKey(o)) {
        Integer i = (Integer)CompactSparseTypedEdgeSet.TYPE_INDICES.get(o);
        return setTypes.get(i.intValue());
      }
      return false;
    }
    
    public Iterator<T> iterator() {
      return new TypeIter();
    }
    
    public int size() {
      return setTypes.cardinality();
    }
    
    private class TypeIter implements Iterator<T>
    {
      IntIterator typeIndices;
      
      public TypeIter() {
        typeIndices = CompactIntSet.wrap(setTypes).iterator();
      }
      
      public boolean hasNext() {
        return typeIndices.hasNext();
      }
      
      public T next() {
        if (!typeIndices.hasNext())
          throw new NoSuchElementException();
        int i = typeIndices.nextInt();
        
        T type = CompactSparseTypedEdgeSet.TYPES.get(i);
        return type;
      }
      
      public void remove() {
        throw new UnsupportedOperationException();
      }
    }
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
        (CompactSparseTypedEdgeSet.this.add(e)));
    }
    
    public boolean contains(Object o) {
      if (!(o instanceof TypedEdge))
        return false;
      TypedEdge<?> e = (TypedEdge)o;
      return ((e.to() == rootVertex) && (e.from() == otherVertex)) || (
        (e.from() == rootVertex) && (e.to() == otherVertex) && 
        (CompactSparseTypedEdgeSet.this.contains(e)));
    }
    
    public boolean isEmpty() {
      return !connects(otherVertex);
    }
    
    public Iterator<TypedEdge<T>> iterator() {
      return new CompactSparseTypedEdgeSet.EdgesForVertexIterator(CompactSparseTypedEdgeSet.this, otherVertex);
    }
    
    public boolean remove(Object o) {
      if (!(o instanceof TypedEdge))
        return false;
      TypedEdge<?> e = (TypedEdge)o;
      return ((e.to() == rootVertex) && (e.from() == otherVertex)) || (
        (e.from() == rootVertex) && (e.to() == otherVertex) && 
        (CompactSparseTypedEdgeSet.this.remove(e)));
    }
    
    public int size() {
      BitSet b = (BitSet)edges.get(otherVertex);
      return b == null ? 0 : b.cardinality();
    }
  }
  


  private class EdgesForVertexIterator
    implements Iterator<TypedEdge<T>>
  {
    private int curTypeIndex;
    

    private BitSet curTypes;
    

    private TypedEdge<T> next;
    

    int otherVertex;
    

    public EdgesForVertexIterator(int otherVertex)
    {
      this.otherVertex = otherVertex;
      curTypeIndex = -1;
      curTypes = ((BitSet)edges.get(otherVertex));
      advance();
    }
    
    private void advance() {
      next = null;
      while ((next == null) && (curTypes != null)) {
        if (curTypes == null) {
          curTypes = ((BitSet)edges.get(otherVertex));
          curTypeIndex = -1;
        }
        
        if (curTypes == null)
          break;
        curTypeIndex = curTypes.nextSetBit(curTypeIndex + 1);
        if (curTypeIndex >= 0)
        {

          T type = CompactSparseTypedEdgeSet.TYPES.get(curTypeIndex);
          next = new SimpleTypedEdge(type, otherVertex, rootVertex);

        }
        else
        {
          curTypes = null;
        }
      }
    }
    
    public boolean hasNext() { return next != null; }
    
    public TypedEdge<T> next()
    {
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
  



  private class EdgeIterator
    implements Iterator<TypedEdge<T>>
  {
    private TIntObjectIterator<BitSet> iter;
    


    private TypedEdge<T> next;
    


    private int curVertex;
    


    private IntIterator curVertexTypes;
    


    public EdgeIterator()
    {
      iter = edges.iterator();
      advance();
    }
    
    private void advance() {
      next = null;
      while (next == null)
      {

        if ((curVertexTypes == null) || (!curVertexTypes.hasNext()))
        {
          if (!iter.hasNext())
            break;
          iter.advance();
          curVertex = iter.key();
          curVertexTypes = CompactIntSet.wrap((BitSet)iter.value()).iterator();
        }
        
        if (curVertexTypes.hasNext()) {
          int typeIndex = curVertexTypes.nextInt();
          
          T type = CompactSparseTypedEdgeSet.TYPES.get(typeIndex);
          next = new SimpleTypedEdge(type, curVertex, rootVertex);
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
