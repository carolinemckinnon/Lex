package edu.ucla.sspace.nonlinear;

import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.hal.WeightingFunction;
import edu.ucla.sspace.matrix.AffinityMatrixCreator;
import edu.ucla.sspace.matrix.AtomicMatrix;
import edu.ucla.sspace.matrix.GrowingSparseMatrix;
import edu.ucla.sspace.matrix.LocalityPreservingProjection;
import edu.ucla.sspace.matrix.Matrices;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.MatrixFile;
import edu.ucla.sspace.matrix.SparseMatrix;
import edu.ucla.sspace.text.IteratorFactory;
import edu.ucla.sspace.util.Pair;
import edu.ucla.sspace.util.ReflectionUtil;
import edu.ucla.sspace.vector.Vector;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;




























































































public class LocalityPreservingCooccurrenceSpace
  implements SemanticSpace
{
  private static final String PROPERTY_PREFIX = "edu.ucla.sspace.lpsa.LocalityPreservingCooccurrenceSpace";
  public static final String ENTROPY_THRESHOLD_PROPERTY = "edu.ucla.sspace.lpsa.LocalityPreservingCooccurrenceSpace.threshold";
  public static final String WINDOW_SIZE_PROPERTY = "edu.ucla.sspace.lpsa.LocalityPreservingCooccurrenceSpace.windowSize";
  public static final String WEIGHTING_FUNCTION_PROPERTY = "edu.ucla.sspace.lpsa.LocalityPreservingCooccurrenceSpace.weighting";
  public static final String LPCS_DIMENSIONS_PROPERTY = "edu.ucla.sspace.lpsa.LocalityPreservingCooccurrenceSpace.dimensions";
  public static final int DEFAULT_WINDOW_SIZE = 5;
  public static final String DEFAULT_WEIGHTING = "edu.ucla.sspace.hal.EvenWeighting";
  private static final Logger LOGGER = Logger.getLogger(LocalityPreservingCooccurrenceSpace.class.getName());
  




  private final Map<String, Integer> termToIndex;
  




  private final int windowSize;
  



  private final WeightingFunction weighting;
  



  private int wordIndexCounter;
  



  private SparseMatrix cooccurrenceMatrix;
  



  private AtomicMatrix atomicMatrix;
  



  private Matrix reduced;
  



  private AffinityMatrixCreator affinityCreator;
  




  public LocalityPreservingCooccurrenceSpace(AffinityMatrixCreator creator)
  {
    this(creator, System.getProperties());
  }
  




  public LocalityPreservingCooccurrenceSpace(AffinityMatrixCreator creator, Properties properties)
  {
    affinityCreator = creator;
    cooccurrenceMatrix = new GrowingSparseMatrix();
    atomicMatrix = Matrices.synchronizedMatrix(cooccurrenceMatrix);
    reduced = null;
    termToIndex = new ConcurrentHashMap();
    
    wordIndexCounter = 0;
    
    String windowSizeProp = properties.getProperty("edu.ucla.sspace.lpsa.LocalityPreservingCooccurrenceSpace.windowSize");
    windowSize = (windowSizeProp != null ? 
      Integer.parseInt(windowSizeProp) : 
      5);
    
    weighting = ((WeightingFunction)ReflectionUtil.getObjectInstance(
      properties.getProperty(
      "edu.ucla.sspace.lpsa.LocalityPreservingCooccurrenceSpace.weighting", "edu.ucla.sspace.hal.EvenWeighting")));
  }
  

  public void processDocument(BufferedReader document)
    throws IOException
  {
    Queue<String> nextWords = new ArrayDeque();
    Queue<String> prevWords = new ArrayDeque();
    
    Iterator<String> documentTokens = 
      IteratorFactory.tokenizeOrdered(document);
    
    String focus = null;
    




    Map<Pair<Integer>, Double> matrixEntryToCount = 
      new HashMap();
    

    int i = 0;
    do { nextWords.offer((String)documentTokens.next());i++;
      if (i >= windowSize) break; } while (documentTokens.hasNext());
    
    int wordDistance;
    while (!nextWords.isEmpty())
    {

      focus = (String)nextWords.remove();
      

      if (documentTokens.hasNext()) {
        String windowEdge = (String)documentTokens.next();
        nextWords.offer(windowEdge);
      }
      


      if (focus.equals(""))
      {
        prevWords.offer(focus);
        if (prevWords.size() > windowSize) {
          prevWords.remove();
        }
      }
      else {
        int focusIndex = getIndexFor(focus);
        

        wordDistance = 1;
        for (String after : nextWords)
        {

          if (!after.equals("")) {
            int index = getIndexFor(after);
            



            Pair<Integer> p = new Pair(Integer.valueOf(focusIndex), Integer.valueOf(index));
            double value = weighting.weight(wordDistance, windowSize);
            Double curCount = (Double)matrixEntryToCount.get(p);
            matrixEntryToCount.put(p, 
              Double.valueOf(curCount == null ? value : value + curCount.doubleValue()));
          }
          
          wordDistance++;
        }
        
        wordDistance = -1;
        for (String before : prevWords)
        {

          if (!before.equals("")) {
            int index = getIndexFor(before);
            



            Pair<Integer> p = new Pair(Integer.valueOf(index), Integer.valueOf(focusIndex));
            double value = weighting.weight(wordDistance, windowSize);
            Double curCount = (Double)matrixEntryToCount.get(p);
            matrixEntryToCount.put(p, 
              Double.valueOf(curCount == null ? value : value + curCount.doubleValue()));
          }
          wordDistance--;
        }
        


        prevWords.offer(focus);
        if (prevWords.size() > windowSize) {
          prevWords.remove();
        }
      }
    }
    
    for (Map.Entry<Pair<Integer>, Double> e : matrixEntryToCount.entrySet()) {
      Pair<Integer> p = (Pair)e.getKey();
      atomicMatrix.addAndGet(((Integer)x).intValue(), ((Integer)y).intValue(), ((Double)e.getValue()).doubleValue());
    }
  }
  




  private final int getIndexFor(String word)
  {
    Integer index = (Integer)termToIndex.get(word);
    if (index == null) {
      synchronized (this)
      {
        index = (Integer)termToIndex.get(word);
        

        if (index == null) {
          int i = wordIndexCounter++;
          termToIndex.put(word, Integer.valueOf(i));
          return i;
        }
      }
    }
    return index.intValue();
  }
  



  public Set<String> getWords()
  {
    return Collections.unmodifiableSet(termToIndex.keySet());
  }
  


  public Vector getVector(String word)
  {
    Integer index = (Integer)termToIndex.get(word);
    if (index == null) {
      return null;
    }
    

    return reduced.getRowVector(index.intValue());
  }
  


  public int getVectorLength()
  {
    return reduced.columns();
  }
  




  public void processSpace(Properties properties)
  {
    int dimensions = 300;
    

    String dimensionsProp = 
      properties.getProperty("edu.ucla.sspace.lpsa.LocalityPreservingCooccurrenceSpace.dimensions");
    if (dimensionsProp != null) {
      try {
        dimensions = Integer.parseInt(dimensionsProp);
      } catch (NumberFormatException nfe) {
        throw new IllegalArgumentException(
          "edu.ucla.sspace.lpsa.LocalityPreservingCooccurrenceSpace.dimensions is not an integer: " + 
          dimensionsProp);
      }
    }
    try
    {
      LOGGER.info("reducing to " + dimensions + " dimensions");
      File tiMap = new File("lpcs-term-index." + Math.random() + ".map");
      PrintWriter pw = new PrintWriter(tiMap);
      for (Map.Entry<String, Integer> e : termToIndex.entrySet())
        pw.println((String)e.getKey() + "\t" + e.getValue());
      pw.close();
      LOGGER.info("wrote term-index map to " + tiMap);
    } catch (Throwable t) {
      t.printStackTrace();
    }
    

    MatrixFile affinityMatrix = affinityCreator.calculate(
      cooccurrenceMatrix);
    


    reduced = LocalityPreservingProjection.project(
      cooccurrenceMatrix, affinityMatrix, dimensions);
  }
  


  public String getSpaceName()
  {
    return "nws-semantic-space";
  }
}
