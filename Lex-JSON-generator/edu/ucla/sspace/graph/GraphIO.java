package edu.ucla.sspace.graph;

import edu.ucla.sspace.util.Indexer;
import edu.ucla.sspace.util.LoggerUtil;
import edu.ucla.sspace.util.ObjectIndexer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Set;
import java.util.logging.Logger;











































public class GraphIO
{
  private static final Logger LOGGER = Logger.getLogger(GraphIO.class.getName());
  private GraphIO() {}
  
  public static enum GraphType { UNDIRECTED, 
    DIRECTED, 
    WEIGHTED, 
    MULTIGRAPH;
  }
  
  public static enum GraphFileFormat {
    PAJEK;
  }
  
  public static Graph<? extends Edge> read(File f, GraphType type)
    throws IOException
  {
    switch (type) {
    case DIRECTED: 
      return readUndirected(f);
    case MULTIGRAPH: 
      return readDirected(f);
    }
    throw new Error("Reading GraphType " + type + " is current unsupported");
  }
  
  public static Graph<Edge> readUndirected(File f) throws IOException
  {
    return readUndirected(f, new ObjectIndexer());
  }
  
  public static Graph<Edge> readUndirected(File f, Indexer<String> vertexIndexer) throws IOException
  {
    BufferedReader br = new BufferedReader(new FileReader(f));
    Graph<Edge> g = 
      new SparseUndirectedGraph();
    int lineNo = 0;
    for (String line = null; (line = br.readLine()) != null;) {
      lineNo++;
      line = line.trim();
      if (!line.startsWith("#"))
      {
        if (line.length() != 0)
        {
          String[] arr = line.split("\\s+");
          if (arr.length < 2) {
            throw new IOException("Missing vertex on line " + lineNo);
          }
          int v1 = vertexIndexer.index(arr[0]);
          int v2 = vertexIndexer.index(arr[1]);
          g.add(new LabeledEdge(v1, v2, arr[0], arr[1]));
          if (lineNo % 100000 == 0)
            LoggerUtil.verbose(LOGGER, "Read %d lines from %s", new Object[] { Integer.valueOf(lineNo), f });
        } } }
    LoggerUtil.verbose(LOGGER, "Read undirected graph with %d vertices and %d edges", new Object[] {
      Integer.valueOf(g.order()), Integer.valueOf(g.size()) });
    return g;
  }
  
  public static WeightedGraph<WeightedEdge> readWeighted(File f) throws IOException {
    return readWeighted(f, new ObjectIndexer());
  }
  
  public static WeightedGraph<WeightedEdge> readWeighted(File f, Indexer<String> vertexIndexer)
    throws IOException
  {
    BufferedReader br = new BufferedReader(new FileReader(f));
    WeightedGraph<WeightedEdge> g = new SparseWeightedGraph();
    int lineNo = 0;
    for (String line = null; (line = br.readLine()) != null;) {
      lineNo++;
      line = line.trim();
      if (!line.startsWith("#"))
      {
        if (line.length() != 0)
        {
          String[] arr = line.split("\\s+");
          if (arr.length < 2)
            throw new IOException("Missing vertex on line " + lineNo);
          if (arr.length < 3)
            throw new IOException("Missing edge weight on line " + lineNo);
          int v1 = vertexIndexer.index(arr[0]);
          int v2 = vertexIndexer.index(arr[1]);
          double weight = Double.parseDouble(arr[2]);
          g.add(new SimpleWeightedEdge(v1, v2, weight));
          if (lineNo % 1000 == 0) {
            System.out.printf("Read %d lines from %s%n", new Object[] { Integer.valueOf(lineNo), f });
          }
          if (lineNo % 100000 == 0)
            LoggerUtil.veryVerbose(LOGGER, "Read %d lines from %s", new Object[] { Integer.valueOf(lineNo), f });
        } } }
    LoggerUtil.verbose(LOGGER, "Read directed graph with %d vertices and %d edges", new Object[] {
      Integer.valueOf(g.order()), Integer.valueOf(g.size()) });
    return g;
  }
  
  public static WeightedGraph<WeightedEdge> readWeighted(File f, Indexer<String> vertexIndexer, double minWeight)
    throws IOException
  {
    BufferedReader br = new BufferedReader(new FileReader(f));
    WeightedGraph<WeightedEdge> g = new SparseWeightedGraph();
    int lineNo = 0;
    for (String line = null; (line = br.readLine()) != null;) {
      lineNo++;
      line = line.trim();
      if (!line.startsWith("#"))
      {
        if (line.length() != 0)
        {
          String[] arr = line.split("\\s+");
          if (arr.length < 2)
            throw new IOException("Missing vertex on line " + lineNo);
          if (arr.length < 3)
            throw new IOException("Missing edge weight on line " + lineNo);
          int v1 = vertexIndexer.index(arr[0]);
          int v2 = vertexIndexer.index(arr[1]);
          double weight = Double.parseDouble(arr[2]);
          if (weight >= minWeight) {
            g.add(new SimpleWeightedEdge(v1, v2, weight));
          }
          if (lineNo % 100000 == 0)
            LoggerUtil.veryVerbose(LOGGER, "Read %d lines from %s", new Object[] { Integer.valueOf(lineNo), f });
        } } }
    LoggerUtil.verbose(LOGGER, "Read directed graph with %d vertices and %d edges", new Object[] {
      Integer.valueOf(g.order()), Integer.valueOf(g.size()) });
    return g;
  }
  





  public static Graph<Edge> readUndirectedFromWeighted(File f, Indexer<String> vertexIndexer, double minWeight)
    throws IOException
  {
    BufferedReader br = new BufferedReader(new FileReader(f));
    Graph<Edge> g = new SparseUndirectedGraph();
    
    int lineNo = 0;
    for (String line = null; (line = br.readLine()) != null;) {
      lineNo++;
      line = line.trim();
      if (!line.startsWith("#"))
      {
        if (line.length() != 0)
        {
          String[] arr = line.split("\\s+");
          if (arr.length < 2)
            throw new IOException("Missing vertex on line " + lineNo);
          if (arr.length < 3)
            throw new IOException("Missing edge weight on line " + lineNo);
          int v1 = vertexIndexer.index(arr[0]);
          int v2 = vertexIndexer.index(arr[1]);
          double weight = Double.parseDouble(arr[2]);
          if (weight >= minWeight) {
            g.add(new SimpleEdge(v1, v2));
          }
          if (lineNo % 100000 == 0)
            LoggerUtil.veryVerbose(LOGGER, "Read %d lines from %s, kept %d edges", new Object[] {
              Integer.valueOf(lineNo), f, Integer.valueOf(g.size()) });
        } } }
    LoggerUtil.verbose(LOGGER, "Read directed graph with %d vertices and %d edges", new Object[] {
      Integer.valueOf(g.order()), Integer.valueOf(g.size()) });
    return g;
  }
  


  public static void writeUndirected(File f, Graph<? extends Edge> g, Indexer<String> vertexLabels)
    throws IOException
  {
    PrintWriter pw = new PrintWriter(f);
    for (Edge e : g.edges()) {
      String v1 = (String)vertexLabels.lookup(e.from());
      if (v1 == null)
        v1 = String.valueOf(e.from());
      String v2 = (String)vertexLabels.lookup(e.to());
      if (v2 == null) {
        v2 = String.valueOf(e.to());
      }
      pw.printf("%s\t%s%n", new Object[] { v1, v2 });
    }
    pw.close();
  }
  
  public static DirectedGraph<DirectedEdge> readDirected(File f) throws IOException {
    return readDirected(f, new ObjectIndexer());
  }
  
  public static DirectedGraph<DirectedEdge> readDirected(File f, Indexer<String> vertexIndexer)
    throws IOException
  {
    BufferedReader br = new BufferedReader(new FileReader(f));
    DirectedGraph<DirectedEdge> g = new SparseDirectedGraph();
    int lineNo = 0;
    for (String line = null; (line = br.readLine()) != null;) {
      lineNo++;
      line = line.trim();
      if (!line.startsWith("#"))
      {
        if (line.length() != 0)
        {
          String[] arr = line.split("\\s+");
          if (arr.length < 2) {
            throw new IOException("Missing vertex on line " + lineNo);
          }
          int v1 = vertexIndexer.index(arr[0]);
          int v2 = vertexIndexer.index(arr[1]);
          g.add(new SimpleDirectedEdge(v1, v2));
          if (lineNo % 100000 == 0)
            LoggerUtil.veryVerbose(LOGGER, "read %d lines from %s", new Object[] { Integer.valueOf(lineNo), f });
        } } }
    LoggerUtil.verbose(LOGGER, "Read directed graph with %d vertices and %d edges", new Object[] {
      Integer.valueOf(g.order()), Integer.valueOf(g.size()) });
    return g;
  }
  
  public static DirectedMultigraph<String> readDirectedMultigraph(File f) throws IOException
  {
    return readDirectedMultigraph(f, new ObjectIndexer());
  }
  
  public static DirectedMultigraph<String> readDirectedMultigraph(File f, Indexer<String> vertexIndexer)
    throws IOException
  {
    BufferedReader br = new BufferedReader(new FileReader(f));
    DirectedMultigraph<String> g = new DirectedMultigraph();
    int lineNo = 0;
    for (String line = null; (line = br.readLine()) != null;) {
      lineNo++;
      line = line.trim();
      if (!line.startsWith("#"))
      {
        if (line.length() != 0)
        {
          String[] arr = line.split("\\s+");
          if (arr.length < 3) {
            throw new IOException("Missing vertex or type on line " + lineNo);
          }
          int v1 = vertexIndexer.index(arr[0]);
          int v2 = vertexIndexer.index(arr[1]);
          String type = arr[2];
          g.add(new SimpleDirectedTypedEdge(type, v1, v2));
          
          if (lineNo % 100000 == 0)
            LoggerUtil.veryVerbose(LOGGER, "read %d lines from %s, graph now has %d vertices, %d edges, and %d types", new Object[] {
              Integer.valueOf(lineNo), f, 
              Integer.valueOf(g.order()), Integer.valueOf(g.size()), Integer.valueOf(g.edgeTypes().size()) });
        } }
    }
    LoggerUtil.verbose(LOGGER, "Read directed multigraph with %d vertices, %d edges, and %d types", new Object[] {
      Integer.valueOf(g.order()), Integer.valueOf(g.size()), Integer.valueOf(g.edgeTypes().size()) });
    return g;
  }
  
  public static UndirectedMultigraph<String> readUndirectedMultigraph(File f) throws IOException {
    return readUndirectedMultigraph(f, new ObjectIndexer());
  }
  
  public static UndirectedMultigraph<String> readUndirectedMultigraph(File f, Indexer<String> vertexIndexer) throws IOException
  {
    BufferedReader br = new BufferedReader(new FileReader(f));
    UndirectedMultigraph<String> g = new UndirectedMultigraph();
    int lineNo = 0;
    for (String line = null; (line = br.readLine()) != null;) {
      lineNo++;
      line = line.trim();
      if (!line.startsWith("#"))
      {
        if (line.length() != 0)
        {
          String[] arr = line.split("\\s+");
          if (arr.length < 3) {
            throw new IOException("Missing vertex or type on line " + lineNo);
          }
          if (arr[0].equals(arr[1])) {
            System.out.println("skipping self edge: " + line);
          }
          else {
            int v1 = vertexIndexer.index(arr[0]);
            int v2 = vertexIndexer.index(arr[1]);
            String type = arr[2];
            g.add(new SimpleTypedEdge(type, v1, v2));
            
            if (lineNo % 100000 == 0)
              LoggerUtil.veryVerbose(LOGGER, "read %d lines from %s, graph now has %d vertices, %d edges, and %d types", new Object[] {
                Integer.valueOf(lineNo), f, 
                Integer.valueOf(g.order()), Integer.valueOf(g.size()), Integer.valueOf(g.edgeTypes().size()) });
          }
        } } }
    if (g.order() != vertexIndexer.highestIndex() + 1) {
      System.out.printf("%d != %d%n", new Object[] { Integer.valueOf(g.order()), Integer.valueOf(vertexIndexer.highestIndex()) });
      throw new Error();
    }
    LoggerUtil.verbose(LOGGER, "Read undirected multigraph with %d vertices, %d edges, and %d types", new Object[] {
      Integer.valueOf(g.order()), Integer.valueOf(g.size()), Integer.valueOf(g.edgeTypes().size()) });
    return g;
  }
  
  public static <T> void writeUndirectedMultigraph(Multigraph<T, TypedEdge<T>> g, File f) throws IOException
  {
    PrintWriter pw = new PrintWriter(f);
    for (TypedEdge<T> e : g.edges()) {
      pw.printf("%d %d %f%n", new Object[] { Integer.valueOf(e.from()), Integer.valueOf(e.to()), e.edgeType() });
    }
    pw.close();
  }
  
  public static <T> void writeUndirectedMultigraph(Multigraph<T, TypedEdge<T>> g, File f, Indexer<String> vertexLabels)
    throws IOException
  {
    PrintWriter pw = new PrintWriter(f);
    for (TypedEdge<T> e : g.edges()) {
      pw.printf("%d %d %f%n", new Object[] { vertexLabels.lookup(e.from()), 
        vertexLabels.lookup(e.to()), e.edgeType() });
    }
    pw.close();
  }
  

  public static void writeWeighted(File f, WeightedGraph<? extends WeightedEdge> g)
    throws IOException
  {
    PrintWriter pw = new PrintWriter(f);
    for (WeightedEdge e : g.edges()) {
      pw.printf("%d %d %f%n", new Object[] { Integer.valueOf(e.from()), Integer.valueOf(e.to()), Double.valueOf(e.weight()) });
    }
    pw.close();
  }
  

  public static void writeWeighted(File f, WeightedGraph<? extends WeightedEdge> g, Indexer<String> vertexLabels)
    throws IOException
  {
    PrintWriter pw = new PrintWriter(f);
    for (WeightedEdge e : g.edges()) {
      String v1 = (String)vertexLabels.lookup(e.from());
      if (v1 == null)
        v1 = String.valueOf(e.from());
      String v2 = (String)vertexLabels.lookup(e.to());
      if (v2 == null) {
        v2 = String.valueOf(e.to());
      }
      pw.printf("%s\t%s\t%f%n", new Object[] { v1, v2, Double.valueOf(e.weight()) });
    }
    pw.close();
  }
}
