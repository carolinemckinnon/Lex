package edu.ucla.sspace.graph;

import edu.ucla.sspace.util.Indexer;
import edu.ucla.sspace.util.ObjectIndexer;
import edu.ucla.sspace.util.primitive.IntSet;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;


































public class LabeledGraph<L, E extends Edge>
  extends GraphAdaptor<E>
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private final Graph<E> graph;
  private final Indexer<L> vertexLabels;
  
  public LabeledGraph(Graph<E> graph)
  {
    this(graph, new ObjectIndexer());
  }
  
  public LabeledGraph(Graph<E> graph, Indexer<L> vertexLabels) {
    super(graph);
    this.graph = graph;
    this.vertexLabels = vertexLabels;
  }
  
  public boolean add(L vertexLabel) {
    return add(vertexLabels.index(vertexLabel));
  }
  





  public boolean add(int vertex)
  {
    if (vertexLabels.lookup(vertex) == null) {
      throw new IllegalArgumentException("Cannot add a vertex without a label");
    }
    
    return super.add(vertex);
  }
  


  public LabeledGraph<L, E> copy(Set<Integer> vertices)
  {
    Graph<E> g = super.copy(vertices);
    


    Indexer<L> labels = new ObjectIndexer(vertexLabels);
    return new LabeledGraph(g, labels);
  }
  
  public boolean contains(L vertexLabel) {
    return contains(vertexLabels.index(vertexLabel));
  }
  
  public boolean remove(L vertexLabel) {
    return remove(vertexLabels.index(vertexLabel));
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder(order() * 4 + size() * 10);
    sb.append("vertices: [");
    for (Iterator localIterator = vertices().iterator(); localIterator.hasNext();) { int v = ((Integer)localIterator.next()).intValue();
      sb.append(vertexLabels.lookup(v)).append(',');
    }
    sb.setCharAt(sb.length() - 1, ']');
    sb.append(" edges: [");
    for (E e : edges()) {
      L from = vertexLabels.lookup(e.from());
      L to = vertexLabels.lookup(e.to());
      String edge = (e instanceof DirectedEdge) ? "->" : "--";
      sb.append('(').append(from).append(edge).append(to);
      if ((e instanceof TypedEdge)) {
        TypedEdge<?> t = (TypedEdge)e;
        sb.append(':').append(t.edgeType());
      }
      if ((e instanceof WeightedEdge)) {
        WeightedEdge w = (WeightedEdge)e;
        sb.append(", ").append(w.weight());
      }
      sb.append("), ");
    }
    sb.setCharAt(sb.length() - 2, ']');
    sb.deleteCharAt(sb.length() - 1);
    return sb.toString();
  }
}
