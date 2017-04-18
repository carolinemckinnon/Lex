package edu.ucla.sspace.graph.io;

import edu.ucla.sspace.graph.DirectedEdge;
import edu.ucla.sspace.graph.DirectedGraph;
import edu.ucla.sspace.graph.DirectedMultigraph;
import edu.ucla.sspace.graph.Edge;
import edu.ucla.sspace.graph.Graph;
import edu.ucla.sspace.graph.SimpleDirectedEdge;
import edu.ucla.sspace.graph.SimpleDirectedTypedEdge;
import edu.ucla.sspace.graph.SimpleEdge;
import edu.ucla.sspace.graph.SimpleTypedEdge;
import edu.ucla.sspace.graph.SimpleWeightedEdge;
import edu.ucla.sspace.graph.SparseDirectedGraph;
import edu.ucla.sspace.graph.SparseUndirectedGraph;
import edu.ucla.sspace.graph.SparseWeightedGraph;
import edu.ucla.sspace.graph.UndirectedMultigraph;
import edu.ucla.sspace.graph.WeightedEdge;
import edu.ucla.sspace.graph.WeightedGraph;
import edu.ucla.sspace.util.HashIndexer;
import edu.ucla.sspace.util.Indexer;
import edu.ucla.sspace.util.LoggerUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Set;
import java.util.logging.Logger;



































public class EdgeListReader
  extends GraphReaderAdapter
  implements GraphReader
{
  private static final Logger LOGGER = Logger.getLogger(EdgeListReader.class.getName());
  
  public EdgeListReader() {}
  
  public Graph<Edge> readUndirected(File f) throws IOException
  {
    return readUndirected(f, new HashIndexer());
  }
  
  public Graph<Edge> readUndirected(File f, Indexer<String> vertexIndexer) throws IOException
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
          g.add(new SimpleEdge(v1, v2));
          if (lineNo % 100000 == 0)
            LoggerUtil.verbose(LOGGER, "Read %d lines from %s", new Object[] { Integer.valueOf(lineNo), f });
        } } }
    LoggerUtil.verbose(LOGGER, "Read undirected graph with %d vertices and %d edges", new Object[] {
      Integer.valueOf(g.order()), Integer.valueOf(g.size()) });
    return g;
  }
  
  public WeightedGraph<WeightedEdge> readWeighted(File f) throws IOException {
    return readWeighted(f, new HashIndexer());
  }
  
  public WeightedGraph<WeightedEdge> readWeighted(File f, Indexer<String> vertexIndexer)
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
          
          if (lineNo % 10000 == 0)
            LoggerUtil.veryVerbose(LOGGER, "Read %d lines from %s", new Object[] { Integer.valueOf(lineNo), f });
        } } }
    LoggerUtil.verbose(LOGGER, "Read directed graph with %d vertices and %d edges", new Object[] {
      Integer.valueOf(g.order()), Integer.valueOf(g.size()) });
    return g;
  }
  
  public WeightedGraph<WeightedEdge> readWeighted(File f, Indexer<String> vertexIndexer, double minWeight)
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
  





  public Graph<Edge> readUndirectedFromWeighted(File f, Indexer<String> vertexIndexer, double minWeight)
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
            LoggerUtil.veryVerbose(LOGGER, "Read %d lines from %s", new Object[] { Integer.valueOf(lineNo), f });
        } } }
    LoggerUtil.verbose(LOGGER, "Read directed graph with %d vertices and %d edges", new Object[] {
      Integer.valueOf(g.order()), Integer.valueOf(g.size()) });
    return g;
  }
  
  public DirectedGraph<DirectedEdge> readDirected(File f) throws IOException
  {
    return readDirected(f, new HashIndexer());
  }
  
  public DirectedGraph<DirectedEdge> readDirected(File f, Indexer<String> vertexIndexer)
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
  
  public DirectedMultigraph<String> readDirectedMultigraph(File f) throws IOException
  {
    return readDirectedMultigraph(f, new HashIndexer());
  }
  
  public DirectedMultigraph<String> readDirectedMultigraph(File f, Indexer<String> vertexIndexer)
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
  
  public UndirectedMultigraph<String> readUndirectedMultigraph(File f) throws IOException {
    return readUndirectedMultigraph(f, new HashIndexer());
  }
  
  public UndirectedMultigraph<String> readUndirectedMultigraph(File f, Indexer<String> vertexIndexer) throws IOException
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
}
