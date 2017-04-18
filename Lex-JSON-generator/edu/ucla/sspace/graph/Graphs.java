package edu.ucla.sspace.graph;

import edu.ucla.sspace.util.HashIndexer;
import edu.ucla.sspace.util.Indexer;
import edu.ucla.sspace.util.LoggerUtil;
import edu.ucla.sspace.util.primitive.IntIterator;
import edu.ucla.sspace.util.primitive.IntSet;
import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import java.lang.reflect.Array;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;






































public final class Graphs
{
  private static final Logger LOGGER = Logger.getLogger(Graphs.class.getName());
  
  private Graphs() {}
  
  public static <E extends DirectedEdge> DirectedGraph<E> asDirectedGraph(Graph<E> g) {
    if (g == null)
      throw new NullPointerException();
    return (g instanceof DirectedGraph) ? 
      (DirectedGraph)g : 
      new DirectedGraphAdaptor(g);
  }
  
  public static <E extends WeightedEdge> WeightedGraph<E> asWeightedGraph(Graph<E> g) {
    throw new Error();
  }
  
  public static <T, E extends TypedEdge<T>> Multigraph<T, E> asMultigraph(Graph<E> g) {
    if (g == null)
      throw new NullPointerException();
    if ((g instanceof Multigraph))
    {
      Multigraph<T, E> m = (Multigraph)g;
      return m;
    }
    
    return new MultigraphAdaptor(g);
  }
  




  public static <E extends Edge> Graph<E> pack(Graph<E> g)
  {
    int order = g.order();
    boolean isContiguous = true;
    for (Iterator localIterator1 = g.vertices().iterator(); localIterator1.hasNext();) { int i = ((Integer)localIterator1.next()).intValue();
      if (i >= order) {
        isContiguous = false;
        break;
      }
    }
    if (isContiguous) {
      return g;
    }
    
    TIntIntMap vMap = new TIntIntHashMap(g.order());
    int j = 0;
    for (Iterator localIterator2 = g.vertices().iterator(); localIterator2.hasNext();) { int i = ((Integer)localIterator2.next()).intValue();
      vMap.put(i, j++);
    }
    Graph<E> copy = g.copy(Collections.emptySet());
    for (int i = 0; i < order; i++)
      copy.add(i);
    for (Object e : g.edges()) {
      copy.add(((Edge)e).clone(vMap.get(((Edge)e).from()), vMap.get(((Edge)e).to())));
    }
    return copy;
  }
  




















  public static <E extends Edge> int shufflePreserve(Graph<E> g, int shufflesPerEdge)
  {
    if (shufflesPerEdge < 1)
      throw new IllegalArgumentException("must shuffle at least once");
    return shuffleInternal(g, g.edges(), shufflesPerEdge, new Random());
  }
  






















  public static <E extends Edge> int shufflePreserve(Graph<E> g, int shufflesPerEdge, Random rnd)
  {
    if (shufflesPerEdge < 1)
      throw new IllegalArgumentException("must shuffle at least once");
    return shuffleInternal(g, g.edges(), shufflesPerEdge, rnd);
  }
  






  private static <E extends Edge> int shuffleInternal(Graph<E> g, Set<E> edges, int shufflesPerEdge, Random rand)
  {
    int totalShuffles = 0;
    int origSize = g.size();
    int numEdges = edges.size();
    if (numEdges < 2) {
      return 0;
    }
    



    E tmp = (Edge)edges.iterator().next();
    
    Edge[] edgeArray = (Edge[])Array.newInstance(tmp.getClass(), 1);
    edgeArray = (Edge[])edges.toArray(edgeArray);
    
    for (int i = 0; i < numEdges; i++)
    {
      for (int swap = 0; swap < shufflesPerEdge; swap++)
      {
        int j = i;
        while (i == j) {
          j = rand.nextInt(numEdges);
        }
        E e1 = edgeArray[i];
        E e2 = edgeArray[j];
        



        if ((!(e1 instanceof DirectedEdge)) && (rand.nextDouble() < 0.5D))
          e1 = e1.flip();
        if ((!(e2 instanceof DirectedEdge)) && (rand.nextDouble() < 0.5D)) {
          e2 = e2.flip();
        }
        
        E swapped1 = e1.clone(e1.from(), e2.to());
        E swapped2 = e2.clone(e2.from(), e1.to());
        


        if ((!g.contains(swapped1)) && (!g.contains(swapped2)))
        {
          if ((swapped1.from() != swapped1.to()) && 
            (swapped2.from() != swapped2.to()))
          {

            totalShuffles++;
            

            boolean r1 = g.remove(edgeArray[i]);
            boolean r2 = g.remove(edgeArray[j]);
            

            g.add(swapped1);
            g.add(swapped2);
            


            edgeArray[i] = swapped1;
            edgeArray[j] = swapped2;
            assert (g.size() == origSize) : 
              ("Added an extra edge of either " + swapped1 + " or " + swapped2);
          } }
      } }
    return totalShuffles;
  }
  



























  public static <T, E extends TypedEdge<T>> int shufflePreserveType(Multigraph<T, E> g, int shufflesPerEdge)
  {
    return shufflePreserveType(g, shufflesPerEdge, new Random());
  }
  




























  public static <T, E extends TypedEdge<T>> int shufflePreserveType(Multigraph<T, E> g, int shufflesPerEdge, Random rnd)
  {
    if (shufflesPerEdge < 1) {
      throw new IllegalArgumentException("must shuffle at least once");
    }
    int totalShuffles = 0;
    int order = g.order();
    int size = g.size();
    





    Set<T> types = new HashSet(g.edgeTypes());
    for (T type : types) {
      Set<E> edges = g.edges(type);
      
      int shuffles = shuffleInternal(g, edges, shufflesPerEdge, rnd);
      totalShuffles += shuffles;
      LoggerUtil.verbose(LOGGER, "Made %d shuffles for %d edges of type %s", new Object[] {
        Integer.valueOf(shuffles), Integer.valueOf(edges.size()), type });
    }
    
    assert (order == g.order()) : "Changed the number of vertices";
    assert (size == g.size()) : "Changed the number of edges";
    return totalShuffles;
  }
  



  public static <T extends Edge> Graph<T> synchronizedGraph(Graph<T> g)
  {
    throw new Error();
  }
  








  public static <E extends Edge, G extends Graph<E>> Graph<Edge> toLineGraph(G graph)
  {
    return toLineGraph(graph, new HashIndexer());
  }
  











  public static <E extends Edge, G extends Graph<E>> Graph<Edge> toLineGraph(G graph, Indexer<E> edgeIndices)
  {
    Graph<Edge> lineGraph = new SparseUndirectedGraph();
    IntIterator verts = graph.vertices().iterator();
    Iterator localIterator1; for (; verts.hasNext(); 
        



        localIterator1.hasNext())
    {
      int v = verts.nextInt();
      Set<E> adjacent = graph.getAdjacencyList(v);
      

      localIterator1 = adjacent.iterator(); continue;E e1 = (Edge)localIterator1.next();
      int e1vertex = edgeIndices.index(e1);
      for (E e2 : adjacent) {
        if (e1.equals(e2))
          break;
        lineGraph.add(
          new SimpleEdge(e1vertex, edgeIndices.index(e2)));
      }
    }
    

    return lineGraph;
  }
  



  public static String toAdjacencyMatrixString(Graph<?> g)
  {
    StringBuilder sb = new StringBuilder(g.order() * (g.order() + 1));
    for (Integer from : g.vertices()) {
      for (Integer to : g.vertices()) {
        Edge e = new SimpleDirectedEdge(from.intValue(), to.intValue());
        if (g.contains(e)) {
          sb.append('1');
        } else
          sb.append('0');
      }
      sb.append('\n');
    }
    return sb.toString();
  }
  


  public static <T extends Edge> Graph<T> unmodifiable(Graph<T> g)
  {
    throw new Error();
  }
}
