package edu.ucla.sspace.hal;

import edu.ucla.sspace.basis.BasisMapping;
import edu.ucla.sspace.basis.StringBasisMapping;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.matrix.AtomicGrowingSparseHashMatrix;
import edu.ucla.sspace.matrix.MatrixEntropy;
import edu.ucla.sspace.matrix.MatrixEntropy.EntropyStats;
import edu.ucla.sspace.matrix.SparseMatrix;
import edu.ucla.sspace.matrix.YaleSparseMatrix;
import edu.ucla.sspace.text.IteratorFactory;
import edu.ucla.sspace.util.BoundedSortedMultiMap;
import edu.ucla.sspace.util.MultiMap;
import edu.ucla.sspace.util.Pair;
import edu.ucla.sspace.vector.CompactSparseVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.Vector;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Logger;



































































































































public class HyperspaceAnalogueToLanguage
  implements SemanticSpace
{
  private static final Logger LOGGER = Logger.getLogger(HyperspaceAnalogueToLanguage.class.getName());
  




  public static final int DEFAULT_WINDOW_SIZE = 5;
  




  private final BasisMapping<String, String> termToIndex;
  




  private final int windowSize;
  




  private final double columnThreshold;
  




  private final int retainColumns;
  




  private final WeightingFunction weighting;
  




  private int wordIndexCounter;
  




  private AtomicGrowingSparseHashMatrix cooccurrenceMatrix;
  




  private SparseMatrix reduced;
  





  public HyperspaceAnalogueToLanguage()
  {
    this(new StringBasisMapping(), 5, new LinearWeighting(), -1.0D, -1);
  }
  































  public HyperspaceAnalogueToLanguage(BasisMapping<String, String> basis, int windowSize, WeightingFunction weightFunction, double columnThreshold, int retainColumns)
  {
    cooccurrenceMatrix = new AtomicGrowingSparseHashMatrix();
    termToIndex = basis;
    this.windowSize = windowSize;
    weighting = weightFunction;
    this.columnThreshold = columnThreshold;
    this.retainColumns = retainColumns;
    reduced = null;
    wordIndexCounter = 0;
    


    if (basis == null)
      throw new IllegalArgumentException(
        "basis mapping must not be null");
    if (weightFunction == null)
      throw new IllegalArgumentException(
        "weightFunction must not be null");
    if (windowSize <= 0)
      throw new IllegalArgumentException(
        "Window size must be a positive, non-negative integer.\nGiven: " + 
        windowSize);
    if ((columnThreshold > -1.0D) && (retainColumns > 0)) {
      throw new IllegalArgumentException(
        "columnThreshold and retainColumns cannot both be active.\ncolumnThreshold: " + 
        columnThreshold + "\n" + 
        "retainColumns: " + retainColumns + "\n");
    }
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
        nextWords.offer((String)documentTokens.next());
      }
      

      if (!focus.equals("")) {
        int focusIndex = termToIndex.getDimension(focus);
        

        if (focusIndex >= 0)
        {
          wordDistance = -windowSize + (windowSize - prevWords.size());
          addTokens(prevWords, focusIndex, wordDistance, matrixEntryToCount);
        }
      }
      


      prevWords.offer(focus);
      if (prevWords.size() > windowSize) {
        prevWords.remove();
      }
    }
    

    for (Map.Entry<Pair<Integer>, Double> e : matrixEntryToCount.entrySet()) {
      Pair<Integer> p = (Pair)e.getKey();
      cooccurrenceMatrix.addAndGet(((Integer)x).intValue(), ((Integer)y).intValue(), ((Double)e.getValue()).doubleValue());
    }
  }
  








  private void addTokens(Queue<String> words, int focusIndex, int distance, Map<Pair<Integer>, Double> matrixEntryToCount)
  {
    for (String word : words)
    {

      if (!word.equals("")) {
        int index = termToIndex.getDimension(word);
        if (index >= 0)
        {


          Pair<Integer> p = new Pair(Integer.valueOf(index), Integer.valueOf(focusIndex));
          double value = weighting.weight(distance, windowSize);
          Double curCount = (Double)matrixEntryToCount.get(p);
          matrixEntryToCount.put(p, 
            Double.valueOf(curCount == null ? value : value + curCount.doubleValue()));
        }
      }
      distance++;
    }
  }
  



  public Set<String> getWords()
  {
    return Collections.unmodifiableSet(termToIndex.keySet());
  }
  


  public Vector getVector(String word)
  {
    Integer index = Integer.valueOf(termToIndex.getDimension(word));
    if (index == null) {
      return null;
    }
    
    if (reduced == null)
    {



      SparseDoubleVector rowVec = index.intValue() < cooccurrenceMatrix.rows() ? 
        cooccurrenceMatrix.getRowVectorUnsafe(index.intValue()) : 
        new CompactSparseVector(termToIndex.numDimensions());
      SparseDoubleVector colVec = index.intValue() < cooccurrenceMatrix.columns() ? 
        cooccurrenceMatrix.getColumnVectorUnsafe(index.intValue()) : 
        new CompactSparseVector(termToIndex.numDimensions());
      
      return new ConcatenatedSparseDoubleVector(rowVec, colVec);
    }
    


    return reduced.getRowVector(index.intValue());
  }
  


  public int getVectorLength()
  {
    if (cooccurrenceMatrix != null)
      return cooccurrenceMatrix.columns() + cooccurrenceMatrix.rows();
    return reduced.columns();
  }
  





  public void processSpace(Properties properties)
  {
    if (cooccurrenceMatrix.get(termToIndex.numDimensions() - 1, 
      termToIndex.numDimensions() - 1) == 0.0D) {
      cooccurrenceMatrix.set(termToIndex.numDimensions() - 1, 
        termToIndex.numDimensions() - 1, 
        0.0D);
    }
    if (columnThreshold > -1.0D)
      thresholdColumns(columnThreshold);
    if (retainColumns > 0) {
      retainOnly(retainColumns);
    }
  }
  




  private void retainOnly(int columns)
  {
    LOGGER.info("Sorting the columns by entropy and computing the top " + 
      columns + " columns to retain");
    
    int words = termToIndex.numDimensions();
    MultiMap<Double, Integer> entropyToIndex = 
      new BoundedSortedMultiMap(
      columns, false, true, true);
    

    MatrixEntropy.EntropyStats stats = MatrixEntropy.entropy(cooccurrenceMatrix);
    



    for (int col = 0; col < words; col++)
      entropyToIndex.put(Double.valueOf(colEntropy[col]), Integer.valueOf(col));
    for (int row = 0; row < words; row++) {
      entropyToIndex.put(Double.valueOf(rowEntropy[row]), Integer.valueOf(row + words));
    }
    Set<Integer> indicesToKeep = 
      new HashSet(entropyToIndex.values());
    
    LOGGER.info("Reducing to " + columns + " highest entropy columns.");
    
    reduced = retainColumns(indicesToKeep);
    cooccurrenceMatrix = null;
  }
  






  private void thresholdColumns(double threshold)
  {
    LOGGER.info("Computing the columns which are equal to or above the specified threshold");
    

    int words = termToIndex.numDimensions();
    Set<Integer> colsToRetain = new HashSet();
    

    MatrixEntropy.EntropyStats stats = MatrixEntropy.entropy(cooccurrenceMatrix);
    




    for (int col = 0; col < words; col++) {
      if (colEntropy[col] >= threshold)
        colsToRetain.add(Integer.valueOf(col));
    }
    for (int row = 0; row < words; row++) {
      if (rowEntropy[row] >= threshold)
        colsToRetain.add(Integer.valueOf(row + words));
    }
    LOGGER.info("Retaining " + colsToRetain.size() + "/" + words * 2 + 
      " columns, which passed the threshold of " + threshold);
    
    reduced = retainColumns(colsToRetain);
    cooccurrenceMatrix = null;
  }
  



  private SparseMatrix retainColumns(Set<Integer> indicesToKeep)
  {
    int words = termToIndex.numDimensions();
    int cols = indicesToKeep.size();
    

    Map<Integer, Integer> indexMap = new HashMap();
    int newIndex = 0;
    for (Integer index : indicesToKeep) {
      indexMap.put(index, Integer.valueOf(newIndex++));
    }
    

    SparseMatrix reduced = new YaleSparseMatrix(
      words, indicesToKeep.size());
    

    for (int row = 0; row < words; row++) {
      SparseDoubleVector sv = cooccurrenceMatrix.getRowVector(row);
      for (int col : sv.getNonZeroIndices()) {
        double v = cooccurrenceMatrix.get(row, col);
        


        Integer newColIndex = (Integer)indexMap.get(Integer.valueOf(col));
        if (newColIndex != null) {
          reduced.set(row, newColIndex.intValue(), v);
        }
        


        newColIndex = (Integer)indexMap.get(Integer.valueOf(row + words));
        if (newColIndex != null) {
          reduced.set(col, newColIndex.intValue(), v);
        }
      }
    }
    return reduced;
  }
  


  public String getSpaceName()
  {
    return "hal-semantic-space";
  }
}
