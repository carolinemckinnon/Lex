package edu.ucla.sspace.graph.io;

import edu.ucla.sspace.graph.DirectedEdge;
import edu.ucla.sspace.graph.DirectedGraph;
import edu.ucla.sspace.graph.DirectedMultigraph;
import edu.ucla.sspace.graph.DirectedTypedEdge;
import edu.ucla.sspace.graph.Edge;
import edu.ucla.sspace.graph.Graph;
import edu.ucla.sspace.graph.Multigraph;
import edu.ucla.sspace.graph.SimpleDirectedEdge;
import edu.ucla.sspace.graph.SimpleDirectedTypedEdge;
import edu.ucla.sspace.graph.SparseDirectedGraph;
import edu.ucla.sspace.graph.TypedEdge;
import edu.ucla.sspace.util.ColorGenerator;
import edu.ucla.sspace.util.Indexer;
import edu.ucla.sspace.util.LineReader;
import edu.ucla.sspace.util.MultiMap;
import edu.ucla.sspace.util.ObjectIndexer;
import edu.ucla.sspace.util.primitive.IntSet;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;































public class DotIO
{
  private final Indexer<String> indexer;
  private final Pattern UNDIRECTED_EDGE = Pattern.compile("\\s*([\\-\\w]+)\\s*--\\s*([\\-\\w]+)\\s*(\\[.*\\])\\s*;");
  
  private final Pattern DIRECTED_EDGE = Pattern.compile("\\s*([\\-\\w]+)\\s*->\\s*([\\-\\w]+)\\s*;");
  




  private static final Map<Object, Color> EDGE_COLORS = new HashMap();
  
  public DotIO()
  {
    this(new ObjectIndexer());
  }
  
  public DotIO(Indexer<String> indexer) {
    this.indexer = indexer;
  }
  
  public Graph readGraph(File dotFile) {
    return null;
  }
  
  public DirectedGraph<DirectedEdge> readDirectedGraph(File dotFile) {
    DirectedGraph<DirectedEdge> g = new SparseDirectedGraph();
    Matcher m;
    for (Iterator localIterator = new LineReader(dotFile).iterator(); localIterator.hasNext(); 
        

        m.find())
    {
      String line = (String)localIterator.next();
      
      m = DIRECTED_EDGE.matcher(line);
      continue;
      String from = m.group(1);
      String to = m.group(2);
      

      g.add(new SimpleDirectedEdge(indexer.index(from), 
        indexer.index(to)));
    }
    
    return g;
  }
  
  public DirectedMultigraph readDirectedMultigraph(File dotFile) {
    DirectedMultigraph<String> g = new DirectedMultigraph();
    Matcher m;
    for (Iterator localIterator = new LineReader(dotFile).iterator(); localIterator.hasNext(); 
        
        m.find())
    {
      String line = (String)localIterator.next();
      m = DIRECTED_EDGE.matcher(line);
      continue;
      String from = m.group(1);
      String to = m.group(2);
      String meta = m.group(3);
      Pattern type = Pattern.compile("label=\"(\\w+)\"");
      Matcher m2 = type.matcher(meta);
      if (!m2.find())
        throw new Error("could not find label: " + line);
      String label = m2.group(1);
      
      g.add(new SimpleDirectedTypedEdge(
        label, indexer.index(from), indexer.index(to)));
    }
    
    return g;
  }
  

  public <E extends Edge> void writeUndirectedGraph(Graph<E> g, File f)
    throws IOException
  {
    PrintWriter pw = new PrintWriter(f);
    pw.println("graph g {");
    
    for (Iterator localIterator = g.vertices().iterator(); localIterator.hasNext();) { int v = ((Integer)localIterator.next()).intValue();
      pw.println("  " + v + ";"); }
    pw.println();
    
    for (E e : g.edges()) {
      pw.printf("  %d -- %d;%n", new Object[] { Integer.valueOf(e.from()), Integer.valueOf(e.to()) });
    }
    pw.println("}");
    pw.close();
  }
  

  public <E extends Edge> void writeUndirectedGraph(Graph<E> g, File f, Indexer<String> vertexLabels)
    throws IOException
  {
    PrintWriter pw = new PrintWriter(f);
    pw.println("graph g {");
    
    for (Iterator localIterator = g.vertices().iterator(); localIterator.hasNext();) { int v = ((Integer)localIterator.next()).intValue();
      String label = (String)vertexLabels.lookup(v);
      if (label == null)
        label = String.valueOf(v);
      pw.println("  " + v + " [label=\"" + label + "\"];");
    }
    pw.println();
    
    for (E e : g.edges()) {
      pw.printf("  %d -- %d;%n", new Object[] { Integer.valueOf(e.from()), 
        Integer.valueOf(e.to()) });
    }
    pw.println("}");
    pw.close();
  }
  

  public <E extends DirectedEdge> void writeDirectedGraph(DirectedGraph<E> g, File f)
    throws IOException
  {
    PrintWriter pw = new PrintWriter(f);
    pw.println("digraph g {");
    
    for (Iterator localIterator = g.vertices().iterator(); localIterator.hasNext();) { int v = ((Integer)localIterator.next()).intValue();
      pw.println("  " + v + ";"); }
    pw.println();
    
    for (E e : g.edges()) {
      pw.printf("  %d -> %d;%n", new Object[] { Integer.valueOf(e.from()), Integer.valueOf(e.to()) });
    }
    pw.println("}");
    pw.close();
  }
  






  public <T, E extends TypedEdge<T>> void writeUndirectedMultigraph(Multigraph<T, E> g, File f)
    throws IOException
  {
    Map<T, Color> edgeColors = new HashMap();
    ColorGenerator cg = new ColorGenerator();
    for (T type : g.edgeTypes())
      edgeColors.put(type, cg.next());
    writeUndirectedMultigraph(g, f, edgeColors, null, false);
  }
  











  public <T, E extends TypedEdge<T>> void writeUndirectedMultigraph(Multigraph<T, E> g, File f, Map<T, Color> edgeColors)
    throws IOException
  {
    writeUndirectedMultigraph(g, f, edgeColors, null, false);
  }
  












  public <T, E extends TypedEdge<T>> void writeUndirectedMultigraph(Multigraph<T, E> g, File f, Map<T, Color> edgeColors, Indexer<String> vertexLabels)
    throws IOException
  {
    writeUndirectedMultigraph(g, f, edgeColors, vertexLabels, true);
  }
  









  private <T, E extends TypedEdge<T>> void writeUndirectedMultigraph(Multigraph<T, E> g, File f, Map<T, Color> edgeColors, Indexer<String> vertexLabels, boolean useLabels)
    throws IOException
  {
    PrintWriter pw = new PrintWriter(f);
    ColorGenerator cg = new ColorGenerator();
    pw.println("graph g {");
    
    for (Iterator localIterator = g.vertices().iterator(); localIterator.hasNext();) { int v = ((Integer)localIterator.next()).intValue();
      if (useLabels) {
        String label = (String)vertexLabels.lookup(v);
        if (label == null)
          label = String.valueOf(v);
        pw.println("  " + v + " [label=\"" + label + "\"];");
      }
      else {
        pw.println("  " + v + ";");
      } }
    for (E e : g.edges()) {
      Color c = (Color)edgeColors.get(e.edgeType());
      if (c == null) {
        c = cg.next();
        edgeColors.put(e.edgeType(), c);
      }
      String hexColor = Integer.toHexString(c.getRGB());
      hexColor = hexColor.substring(2, hexColor.length());
      pw.printf("  %d -- %d [label=\"%s\", color=\"#%s\"]%n", new Object[] { Integer.valueOf(e.from()), Integer.valueOf(e.to()), 
        e.edgeType(), hexColor });
    }
    




    pw.println("}");
    pw.close();
  }
  









  public <T, E extends TypedEdge<T>> void writeUndirectedMultigraph(Multigraph<T, E> g, File f, Map<T, Color> edgeColors, MultiMap<Integer, String> vertexMetadata)
    throws IOException
  {
    PrintWriter pw = new PrintWriter(f);
    ColorGenerator cg = new ColorGenerator();
    pw.println("graph g {");
    
    for (Iterator localIterator1 = g.vertices().iterator(); localIterator1.hasNext();) { int v = ((Integer)localIterator1.next()).intValue();
      StringBuilder sb = new StringBuilder();
      for (String attr : vertexMetadata.get(Integer.valueOf(v)))
        sb.append(attr).append(' ');
      pw.println("  " + v + " [" + sb + "];");
    }
    for (E e : g.edges()) {
      Color c = (Color)edgeColors.get(e.edgeType());
      if (c == null) {
        c = cg.next();
        edgeColors.put(e.edgeType(), c);
      }
      String hexColor = Integer.toHexString(c.getRGB());
      hexColor = hexColor.substring(2, hexColor.length());
      pw.printf("  %d -- %d [label=\"%s\", color=\"#%s\"]%n", new Object[] { Integer.valueOf(e.from()), Integer.valueOf(e.to()), 
        e.edgeType(), hexColor });
    }
    




    pw.println("}");
    pw.close();
  }
  







  public <T, E extends DirectedTypedEdge<T>> void writeDirectedMultigraph(Multigraph<T, E> g, File f)
    throws IOException
  {
    Map<T, Color> edgeColors = new HashMap();
    ColorGenerator cg = new ColorGenerator();
    for (T type : g.edgeTypes())
      edgeColors.put(type, cg.next());
    writeDirectedMultigraph(g, f, edgeColors);
  }
  

  public <T, E extends DirectedTypedEdge<T>> void writeDirectedMultigraph(Multigraph<T, E> g, File f, Map<T, Color> edgeColors)
    throws IOException
  {
    PrintWriter pw = new PrintWriter(f);
    pw.println("digraph g {");
    
    for (Iterator localIterator = g.vertices().iterator(); localIterator.hasNext();) { int v = ((Integer)localIterator.next()).intValue();
      pw.println("  " + v + ";");
    }
    for (E e : g.edges()) {
      String hexColor = Integer.toHexString(
        ((Color)edgeColors.get(e.edgeType())).getRGB());
      hexColor = hexColor.substring(2, hexColor.length());
      pw.printf("  %d -> %d [label=\"%s\", color=\"#%s\"]%n", new Object[] { Integer.valueOf(e.from()), Integer.valueOf(e.to()), 
        e.edgeType(), hexColor });
    }
    pw.println("}");
    pw.close();
  }
}
