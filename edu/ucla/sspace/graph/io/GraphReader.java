package edu.ucla.sspace.graph.io;

import edu.ucla.sspace.graph.DirectedEdge;
import edu.ucla.sspace.graph.DirectedGraph;
import edu.ucla.sspace.graph.DirectedMultigraph;
import edu.ucla.sspace.graph.Edge;
import edu.ucla.sspace.graph.Graph;
import edu.ucla.sspace.graph.UndirectedMultigraph;
import edu.ucla.sspace.graph.WeightedEdge;
import edu.ucla.sspace.graph.WeightedGraph;
import edu.ucla.sspace.util.Indexer;
import java.io.File;
import java.io.IOException;

public abstract interface GraphReader
{
  public abstract Graph<Edge> readUndirected(File paramFile)
    throws IOException;
  
  public abstract Graph<Edge> readUndirected(File paramFile, Indexer<String> paramIndexer)
    throws IOException;
  
  public abstract WeightedGraph<WeightedEdge> readWeighted(File paramFile)
    throws IOException;
  
  public abstract WeightedGraph<WeightedEdge> readWeighted(File paramFile, Indexer<String> paramIndexer)
    throws IOException;
  
  public abstract WeightedGraph<WeightedEdge> readWeighted(File paramFile, Indexer<String> paramIndexer, double paramDouble)
    throws IOException;
  
  public abstract Graph<Edge> readUndirectedFromWeighted(File paramFile, Indexer<String> paramIndexer, double paramDouble)
    throws IOException;
  
  public abstract DirectedGraph<DirectedEdge> readDirected(File paramFile)
    throws IOException;
  
  public abstract DirectedGraph<DirectedEdge> readDirected(File paramFile, Indexer<String> paramIndexer)
    throws IOException;
  
  public abstract DirectedMultigraph<String> readDirectedMultigraph(File paramFile)
    throws IOException;
  
  public abstract DirectedMultigraph<String> readDirectedMultigraph(File paramFile, Indexer<String> paramIndexer)
    throws IOException;
  
  public abstract UndirectedMultigraph<String> readUndirectedMultigraph(File paramFile)
    throws IOException;
  
  public abstract UndirectedMultigraph<String> readUndirectedMultigraph(File paramFile, Indexer<String> paramIndexer)
    throws IOException;
}
