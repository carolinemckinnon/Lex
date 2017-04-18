package edu.ucla.sspace.graph.io;

import edu.ucla.sspace.graph.DirectedEdge;
import edu.ucla.sspace.graph.DirectedGraph;
import edu.ucla.sspace.graph.Edge;
import edu.ucla.sspace.graph.Graph;
import edu.ucla.sspace.graph.Multigraph;
import edu.ucla.sspace.graph.TypedEdge;
import edu.ucla.sspace.util.Indexer;
import edu.ucla.sspace.util.primitive.IntSet;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;




























public class DotWriter
{
  private final Map<Integer, String> vertexLabels;
  
  public DotWriter()
  {
    vertexLabels = null;
  }
  
  public DotWriter(Indexer<String> indexer) {
    this(indexer.mapping());
  }
  
  public DotWriter(Map<Integer, String> vertexLabels) {
    this.vertexLabels = vertexLabels;
  }
  
  public void write(Graph<? extends Edge> g, File f) throws IOException {
    PrintWriter pw = new PrintWriter(f);
    pw.println("graph g {");
    String vertexLabel = null;
    for (Iterator localIterator = g.vertices().iterator(); localIterator.hasNext();) { int v = ((Integer)localIterator.next()).intValue();
      pw.print("\t" + v);
      if ((vertexLabels != null) && ((vertexLabel = (String)vertexLabels.get(Integer.valueOf(v))) != null)) {
        pw.printf(" [label=\"%s\"]", new Object[] { vertexLabel });
      }
      pw.println(';');
    }
    for (Edge e : g.edges()) {
      pw.printf("\t%d -- %d\n", new Object[] { Integer.valueOf(e.from()), Integer.valueOf(e.to()) });
    }
    pw.println("}");
    pw.close();
  }
  
  public void write(DirectedGraph<? extends DirectedEdge> g, File f) throws IOException
  {
    write(g, f, Collections.emptySet());
  }
  
  public void write(DirectedGraph<? extends DirectedEdge> g, File f, Collection<Set<Integer>> groups) throws IOException
  {
    PrintWriter pw = new PrintWriter(f);
    pw.println("digraph g {");
    String vertexLabel = null;
    for (Iterator localIterator1 = g.vertices().iterator(); localIterator1.hasNext();) { int v = ((Integer)localIterator1.next()).intValue();
      pw.print("\t" + v);
      if ((vertexLabels != null) && ((vertexLabel = (String)vertexLabels.get(Integer.valueOf(v))) != null)) {
        pw.printf(" [label=\"%s\"]", new Object[] { vertexLabel });
      }
      pw.println(';');
    }
    for (DirectedEdge e : g.edges()) {
      pw.printf("\t%d -> %d\n", new Object[] { Integer.valueOf(e.from()), Integer.valueOf(e.to()) });
    }
    if (!groups.isEmpty()) {
      for (Set<Integer> group : groups) {
        pw.print("\t{ rank=same; ");
        for (Integer i : group) {
          pw.print(i);
          pw.print(' ');
        }
        pw.println('}');
      }
    }
    pw.println("}");
    pw.close();
  }
  
  public <T> void write(Multigraph<T, ? extends TypedEdge<T>> g, File f) throws IOException
  {
    PrintWriter pw = new PrintWriter(f);
    pw.println("graph g {");
    String vertexLabel = null;
    for (Iterator localIterator = g.vertices().iterator(); localIterator.hasNext();) { int v = ((Integer)localIterator.next()).intValue();
      pw.print("\t" + v);
      if ((vertexLabels != null) && ((vertexLabel = (String)vertexLabels.get(Integer.valueOf(v))) != null)) {
        pw.printf(" [label=%s]", new Object[] { vertexLabel });
      }
      pw.println(';');
    }
    for (TypedEdge<T> e : g.edges()) {
      pw.printf("\t%d -- %d [label=\"%s\"]\n", new Object[] { Integer.valueOf(e.from()), Integer.valueOf(e.to()), e.edgeType() });
    }
    pw.println("}");
    pw.close();
  }
}
