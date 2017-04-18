package edu.ucla.sspace.graph.io;

import edu.ucla.sspace.graph.DirectedEdge;
import edu.ucla.sspace.graph.Edge;
import edu.ucla.sspace.graph.Graph;
import edu.ucla.sspace.graph.SimpleDirectedEdge;
import edu.ucla.sspace.graph.SparseDirectedGraph;
import edu.ucla.sspace.util.Indexer;
import edu.ucla.sspace.util.LineReader;
import edu.ucla.sspace.util.ObjectIndexer;
import edu.ucla.sspace.util.primitive.IntIterator;
import edu.ucla.sspace.util.primitive.IntSet;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;








































public class PajekIO
{
  private final Indexer<String> vertexIndex;
  
  public PajekIO()
  {
    this(new ObjectIndexer());
  }
  
  public PajekIO(Indexer<String> vertexIndex) {
    this.vertexIndex = vertexIndex;
  }
  
  public static Graph<DirectedEdge> readPajek(File f) throws IOException {
    Graph<DirectedEdge> g = new SparseDirectedGraph();
    int lineNo = 0;
    boolean seenVertices = false;
    boolean seenEdges = false;
    Map<String, Integer> labelToVertex = new HashMap();
    
    for (String line : new LineReader(f)) {
      lineNo++;
      
      if ((!line.matches("\\s*%.*")) && (!line.matches("\\s+")))
      {
        if (line.startsWith("*vertices")) {
          if (seenVertices) {
            throw new IOException("Duplicate vertices definiton on line " + 
              lineNo);
          }
          String[] arr = line.split("\\s+");
          if (arr.length < 2) {
            throw new IOException("Missing specification of how many vertices");
          }
          int numVertices = -1;
          try {
            numVertices = Integer.parseInt(arr[1]);
          } catch (NumberFormatException nfe) {
            throw new IOException("Invalid number of vertices: " + 
              arr[1], nfe);
          }
          if (numVertices < 1) {
            throw new IOException("Must have at least one vertex");
          }
          
          for (int i = 0; i < numVertices; i++) {
            g.add(i);
          }
          seenVertices = true;
        }
        else if ((line.startsWith("*edges")) || 
          (line.startsWith("*arcs"))) {
          if (!seenVertices)
            throw new IOException("Must specify vertices before edges");
          if (seenEdges)
            throw new IOException("Duplicate edges definition on line" + 
              lineNo);
          seenEdges = true;


        }
        else if (seenEdges) {
          String[] arr = line.split("\\s+");
          if (arr.length < 2)
            throw new IOException("Missing vertex declaration(s) for edge definition: " + 
              line);
          int v1 = -1;
          int v2 = -1;
          try {
            v1 = Integer.parseInt(arr[0]);
            v2 = Integer.parseInt(arr[1]);
          } catch (NumberFormatException nfe) {
            throw new IOException("Invalid vertex value: " + line, nfe);
          }
          g.add(new SimpleDirectedEdge(v1, v2));
        }
        else if (!seenVertices)
        {


          throw new IOException("Unknown line content type: " + line);
        } }
    }
    return g;
  }
  
  public <E extends Edge> void writeUndirectedGraph(Graph<E> g, File f)
    throws IOException
  {
    PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f)));
    Indexer<Integer> vertexMap = new ObjectIndexer();
    vertexMap.index(Integer.valueOf(-1));
    pw.println("*Vertices " + g.order());
    
    IntIterator iter = g.vertices().iterator();
    while (iter.hasNext()) {
      int v = ((Integer)iter.next()).intValue();
      pw.println(vertexMap.index(Integer.valueOf(v)) + " \"" + (String)vertexIndex.lookup(v) + "\"");
    }
    pw.println("*Edges " + g.size());
    

    for (E e : g.edges())
      pw.println(vertexMap.index(Integer.valueOf(e.from())) + " " + vertexMap.index(Integer.valueOf(e.to())));
    pw.close();
  }
}
