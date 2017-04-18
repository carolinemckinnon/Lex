package edu.ucla.sspace.tri;

import edu.ucla.sspace.common.Filterable;
import edu.ucla.sspace.ri.RandomIndexing;
import edu.ucla.sspace.temporal.TemporalSemanticSpace;
import edu.ucla.sspace.vector.TernaryVector;
import edu.ucla.sspace.vector.Vector;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.logging.Logger;
































































































































































public abstract class OrderedTemporalRandomIndexing
  implements TemporalSemanticSpace, Filterable
{
  private static final String PROPERTY_PREFIX = "edu.ucla.sspace.tri.OrderedTemporalRandomIndexing";
  public static final String PERMUTATION_FUNCTION_PROPERTY = "edu.ucla.sspace.tri.OrderedTemporalRandomIndexing.permutationFunction";
  public static final String USE_PERMUTATIONS_PROPERTY = "edu.ucla.sspace.tri.OrderedTemporalRandomIndexing.usePermutations";
  public static final String USE_SPARSE_SEMANTICS_PROPERTY = "edu.ucla.sspace.tri.OrderedTemporalRandomIndexing.sparseSemantics";
  public static final String VECTOR_LENGTH_PROPERTY = "edu.ucla.sspace.tri.OrderedTemporalRandomIndexing.vectorLength";
  public static final String WINDOW_SIZE_PROPERTY = "edu.ucla.sspace.tri.OrderedTemporalRandomIndexing.windowSize";
  public static final int DEFAULT_VECTOR_LENGTH = 10000;
  public static final int DEFAULT_WINDOW_SIZE = 4;
  private static final Logger LOGGER = Logger.getLogger(OrderedTemporalRandomIndexing.class.getName());
  




  protected final Collection<Runnable> partitionHooks;
  




  protected final RandomIndexing currentSlice;
  



  protected Long endTime;
  



  protected Long startTime;
  




  public OrderedTemporalRandomIndexing()
  {
    this(System.getProperties());
  }
  






  public OrderedTemporalRandomIndexing(Properties props)
  {
    partitionHooks = new ArrayList();
    

    Properties riProps = new Properties();
    


    String prop = null;
    if ((prop = props.getProperty("edu.ucla.sspace.tri.OrderedTemporalRandomIndexing.vectorLength")) != null) {
      riProps.put("edu.ucla.sspace.ri.RandomIndexing.vectorLength", prop);
    }
    if ((prop = props.getProperty("edu.ucla.sspace.tri.OrderedTemporalRandomIndexing.windowSize")) != null) {
      riProps.put("edu.ucla.sspace.ri.RandomIndexing.windowSize", prop);
    }
    if ((prop = props.getProperty("edu.ucla.sspace.tri.OrderedTemporalRandomIndexing.sparseSemantics")) != null) {
      riProps.put("edu.ucla.sspace.ri.RandomIndexing.sparseSemantics", prop);
    }
    currentSlice = new RandomIndexing(riProps);
  }
  







  public void addPartitionHook(Runnable hook)
  {
    partitionHooks.add(hook);
  }
  




  protected void clear()
  {
    currentSlice.clearSemantics();
    


    startTime = null;
    endTime = null;
  }
  

  public void processDocument(BufferedReader document)
    throws IOException
  {
    processDocument(document, System.currentTimeMillis());
  }
  



  public void processDocument(BufferedReader document, long timeStamp)
    throws IOException
  {
    if ((startTime != null) && (shouldPartitionSpace(timeStamp))) {
      Iterator<Runnable> it = partitionHooks.iterator();
      while (it.hasNext()) {
        Runnable r = (Runnable)it.next();
        
        try
        {
          r.run();
        } catch (Throwable t) {
          LOGGER.warning("Partition hook " + r + " caused the " + 
            "following exception during its operations" + 
            t + " and is being removed");
          it.remove();
        }
      }
      clear();
    }
    

    if (startTime == null) {
      startTime = Long.valueOf(timeStamp);
      endTime = Long.valueOf(timeStamp);
    }
    else if (endTime.longValue() < timeStamp) {
      timeStamp = endTime.longValue();
    }
    currentSlice.processDocument(document);
  }
  








  public void setSemanticFilter(Set<String> semanticsToRetain)
  {
    currentSlice.setSemanticFilter(semanticsToRetain);
  }
  








  protected abstract boolean shouldPartitionSpace(long paramLong);
  







  public Long startTime()
  {
    return startTime;
  }
  


  public Long endTime()
  {
    return endTime;
  }
  





  public abstract String getSpaceName();
  




  public SortedSet<Long> getTimeSteps(String word)
  {
    throw new UnsupportedOperationException(
      "getTimeSteps is not supported");
  }
  







  public Vector getVectorAfter(String word, long startTime)
  {
    throw new UnsupportedOperationException(
      "getVectorAfter is not supported");
  }
  







  public Vector getVectorBefore(String word, long endTime)
  {
    throw new UnsupportedOperationException(
      "getVectorBefore is not supported");
  }
  









  public Vector getVectorBetween(String word, long startTime, long endTime)
  {
    throw new UnsupportedOperationException(
      "getVectorBetween is not supported");
  }
  


  public Vector getVector(String word)
  {
    return currentSlice.getVector(word);
  }
  


  public int getVectorLength()
  {
    return currentSlice.getVectorLength();
  }
  





  public Set<String> getWords()
  {
    return currentSlice.getWords();
  }
  








  public Map<String, TernaryVector> getWordToIndexVector()
  {
    return currentSlice.getWordToIndexVector();
  }
  







  public void processSpace(Properties props) {}
  






  public void setWordToIndexVector(Map<String, TernaryVector> m)
  {
    currentSlice.setWordToIndexVector(m);
  }
}
