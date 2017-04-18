package edu.ucla.sspace.tools;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.graph.Edge;
import edu.ucla.sspace.graph.Graph;
import edu.ucla.sspace.graph.GraphIO;
import edu.ucla.sspace.graph.LinkClustering;
import edu.ucla.sspace.util.Indexer;
import edu.ucla.sspace.util.LoggerUtil;
import edu.ucla.sspace.util.MultiMap;
import edu.ucla.sspace.util.ObjectIndexer;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;



















































public class LinkClusteringTool
{
  public LinkClusteringTool() {}
  
  private static final Logger LOGGER = Logger.getLogger(LinkClusteringTool.class.getName());
  
  public static void main(String[] args) {
    ArgOptions opts = new ArgOptions();
    
    opts.addOption('h', "help", "Generates a help message and exits", 
      false, null, "Program Options");
    opts.addOption('W', "minWeight", "Loads a weighted graph as unweighted, keeping only those edges with weight at least the specified value", 
    

      true, "Double", "Input Options");
    opts.addOption('v', "verbose", "Turns on verbose output", 
      false, null, "Program Options");
    opts.addOption('V', "verbVerbose", "Turns on very verbose output", 
      false, null, "Program Options");
    






















    opts.parseOptions(args);
    
    if ((opts.numPositionalArgs() < 2) || (opts.hasOption("help"))) {
      usage(opts);
      return;
    }
    




    if (opts.hasOption('v'))
      LoggerUtil.setLevel(Level.FINE);
    if (opts.hasOption('V')) {
      LoggerUtil.setLevel(Level.FINER);
    }
    
    try
    {
      LOGGER.info("Loading graph file");
      Indexer<String> vertexLabels = new ObjectIndexer();
      File f = new File(opts.getPositionalArg(0));
      Graph<Edge> graph = null;
      if (opts.hasOption('W')) {
        graph = GraphIO.readUndirectedFromWeighted(
          f, vertexLabels, opts.getDoubleOption('W'));
      } else {
        GraphIO.readUndirected(f, vertexLabels);
      }
      LinkClustering lc = new LinkClustering();
      MultiMap<Integer, Integer> clusterToVertices = 
        lc.cluster(graph, System.getProperties());
      
      PrintWriter pw = new PrintWriter(
        new BufferedOutputStream(new FileOutputStream(
        opts.getPositionalArg(1))));
      

      Iterator localIterator = clusterToVertices.asMap().entrySet().iterator();
      Iterator<Integer> iter;
      for (; localIterator.hasNext(); 
          








          iter.hasNext())
      {
        Map.Entry<Integer, Set<Integer>> e = (Map.Entry)localIterator.next();
        Integer clusterId = (Integer)e.getKey();
        Set<Integer> vertices = (Set)e.getValue();
        
        LoggerUtil.verbose(LOGGER, "Cluster %d had vertices: %s%n", new Object[] {
          clusterId, Integer.valueOf(vertices.size()) });
        
        iter = vertices.iterator();
        StringBuilder sb = new StringBuilder();
        continue;
        int v = ((Integer)iter.next()).intValue();
        sb.append((String)vertexLabels.lookup(v));
        if (iter.hasNext()) {
          sb.append(' ');
        } else {
          pw.println(sb);
        }
      }
      pw.close();
    }
    catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  




  private static void usage(ArgOptions options)
  {
    System.out.println(
      "Link Clustering 1.0, based on the community detection method of\n\n\tYong-Yeol Ahn, James P. Bagrow, and Sune Lehmann. 2010.\n\tLink communities reveal multiscale complexity in networks.\n\tNature, (466):761-764, August.\n\nusage: java -jar lc.jar [options] edge_file communities.txt \n\n" + 
      




      options.prettyPrint() + 
      "\nThe edge file format is:\n" + 
      "   vertex1 vertex2 [weight]\n" + 
      "where vertices may be named using any contiguous sequence of " + 
      "characters or\n" + 
      "numbers.  Weights may be any non-zero double value.  \n" + 
      "Lines beginning\n" + 
      "with '#' are treated as comments and skipped.\n\n" + 
      "Send bug reports or comments to <s-space-research-dev@googlegroups.com>.");
  }
}
