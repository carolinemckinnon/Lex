package edu.ucla.sspace.common;

import edu.ucla.sspace.text.IteratorFactory;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseVector;
import edu.ucla.sspace.vector.Vector;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
































































public class DocumentVectorBuilder
{
  private static final String PROPERTY_PREFIX = "edu.ucla.sspace.common.DocumentVectorBuilder";
  public static final String USE_TERM_FREQUENCIES_PROPERTY = "edu.ucla.sspace.common.DocumentVectorBuilder.usetf";
  private final SemanticSpace sspace;
  private final boolean useTermFreq;
  
  public DocumentVectorBuilder(SemanticSpace baseSpace)
  {
    this(baseSpace, System.getProperties());
  }
  



  public DocumentVectorBuilder(SemanticSpace baseSpace, Properties props)
  {
    sspace = baseSpace;
    useTermFreq = (props.getProperty("edu.ucla.sspace.common.DocumentVectorBuilder.usetf") != null);
  }
  

















  public DoubleVector buildVector(BufferedReader document, DoubleVector documentVector)
  {
    Map<String, Integer> termCounts = new HashMap();
    Iterator<String> articleTokens = IteratorFactory.tokenize(document);
    Integer count; while (articleTokens.hasNext()) {
      String term = (String)articleTokens.next();
      count = (Integer)termCounts.get(term);
      termCounts.put(term, 
        Integer.valueOf((count == null) || (!useTermFreq) ? 1 : count.intValue() + 1));
    }
    


    for (Map.Entry<String, Integer> entry : termCounts.entrySet()) {
      Vector termVector = sspace.getVector((String)entry.getKey());
      if (termVector != null)
      {
        add(documentVector, termVector, ((Integer)entry.getValue()).intValue());
      }
    }
    return documentVector;
  }
  



  void add(DoubleVector dest, Vector src, int factor)
  {
    if ((src instanceof SparseVector)) {
      int[] nonZeros = ((SparseVector)src).getNonZeroIndices();
      for (int i : nonZeros)
        dest.add(i, factor * src.getValue(i).doubleValue());
    } else {
      for (int i = 0; i < src.length(); i++) {
        dest.add(i, factor * src.getValue(i).doubleValue());
      }
    }
  }
}
