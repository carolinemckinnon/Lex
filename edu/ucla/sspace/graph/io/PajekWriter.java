package edu.ucla.sspace.graph.io;

import edu.ucla.sspace.graph.WeightedDirectedMultigraph;
import edu.ucla.sspace.graph.WeightedEdge;
import edu.ucla.sspace.util.Indexer;
import edu.ucla.sspace.util.primitive.IntIterator;
import edu.ucla.sspace.util.primitive.IntSet;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
















































public class PajekWriter
{
  public PajekWriter() {}
  
  public <T> void write(WeightedDirectedMultigraph<T> g, File f)
    throws IOException
  {
    write(g, f, null);
  }
  












  public <T> void write(WeightedDirectedMultigraph<T> g, File f, Indexer<String> vertexLabels)
    throws IOException
  {
    PrintWriter pw = new PrintWriter(f);
    pw.println("*Vertices " + g.order());
    if (vertexLabels != null) {
      IntIterator iter = g.vertices().iterator();
      while (iter.hasNext()) {
        int v = iter.nextInt();
        String label = (String)vertexLabels.lookup(v);
        if (label != null) {
          pw.printf("%d \"%s\"%n", new Object[] { Integer.valueOf(v), label });
        }
      }
    }
    


    pw.println("*Edges");
    IntIterator iter = g.vertices().iterator();
    IntIterator iter2; for (; iter.hasNext(); 
        

        iter2.hasNext())
    {
      int v1 = iter.nextInt();
      iter2 = g.getNeighbors(v1).iterator();
      continue;
      int v2 = iter2.nextInt();
      if (v1 >= v2)
      {
        Set<? extends WeightedEdge> edges = g.getEdges(v1, v2);
        double fromWeight = 0.0D;
        double toWeight = 0.0D;
        for (WeightedEdge e : edges) {
          if (e.from() == v1) {
            fromWeight += e.weight();
          } else
            toWeight += e.weight();
        }
        if (fromWeight != 0.0D)
          pw.printf("%d %d %f%n", new Object[] { Integer.valueOf(v1), Integer.valueOf(v2), Double.valueOf(fromWeight) });
        if (toWeight != 0.0D) {
          pw.printf("%d %d %f%n", new Object[] { Integer.valueOf(v2), Integer.valueOf(v1), Double.valueOf(toWeight) });
        }
      }
    }
    pw.close();
  }
}
