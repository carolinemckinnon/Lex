package edu.ucla.sspace.graph.io;

import edu.ucla.sspace.graph.DirectedEdge;
import edu.ucla.sspace.graph.DirectedGraph;
import edu.ucla.sspace.graph.DirectedMultigraph;
import edu.ucla.sspace.graph.Edge;
import edu.ucla.sspace.graph.Graph;
import edu.ucla.sspace.graph.UndirectedMultigraph;
import edu.ucla.sspace.graph.WeightedEdge;
import edu.ucla.sspace.graph.WeightedGraph;
import edu.ucla.sspace.util.HashIndexer;
import edu.ucla.sspace.util.Indexer;
import java.io.File;
import java.io.IOException;
































public class GraphReaderAdapter
  implements GraphReader
{
  public GraphReaderAdapter() {}
  
  public Graph<Edge> readUndirected(File f)
    throws IOException
  {
    return readUndirected(f, new HashIndexer());
  }
  


  public Graph<Edge> readUndirected(File f, Indexer<String> vertexLabels)
    throws IOException
  {
    throw new UnsupportedOperationException();
  }
  

  public WeightedGraph<WeightedEdge> readWeighted(File f)
    throws IOException
  {
    return readWeighted(f, new HashIndexer());
  }
  


  public WeightedGraph<WeightedEdge> readWeighted(File f, Indexer<String> vertexLabels)
    throws IOException
  {
    throw new UnsupportedOperationException();
  }
  



  public WeightedGraph<WeightedEdge> readWeighted(File f, Indexer<String> vertexLabels, double minWeight)
    throws IOException
  {
    throw new UnsupportedOperationException();
  }
  



  public Graph<Edge> readUndirectedFromWeighted(File f, Indexer<String> vertexLabels, double minWeight)
    throws IOException
  {
    throw new UnsupportedOperationException();
  }
  

  public DirectedGraph<DirectedEdge> readDirected(File f)
    throws IOException
  {
    return readDirected(f, new HashIndexer());
  }
  


  public DirectedGraph<DirectedEdge> readDirected(File f, Indexer<String> vertexLabels)
    throws IOException
  {
    throw new UnsupportedOperationException();
  }
  


  public DirectedMultigraph<String> readDirectedMultigraph(File f)
    throws IOException
  {
    return readDirectedMultigraph(f, new HashIndexer());
  }
  


  public DirectedMultigraph<String> readDirectedMultigraph(File f, Indexer<String> vertexLabels)
    throws IOException
  {
    throw new UnsupportedOperationException();
  }
  


  public UndirectedMultigraph<String> readUndirectedMultigraph(File f)
    throws IOException
  {
    return readUndirectedMultigraph(f, new HashIndexer());
  }
  
  public UndirectedMultigraph<String> readUndirectedMultigraph(File f, Indexer<String> vertexLabels) throws IOException
  {
    throw new UnsupportedOperationException();
  }
}
