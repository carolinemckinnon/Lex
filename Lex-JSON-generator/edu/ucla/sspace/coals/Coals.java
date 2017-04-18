package edu.ucla.sspace.coals;

import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.matrix.CellMaskedSparseMatrix;
import edu.ucla.sspace.matrix.Matrices;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.MatrixFactorization;
import edu.ucla.sspace.matrix.MatrixFile;
import edu.ucla.sspace.matrix.MatrixIO;
import edu.ucla.sspace.matrix.MatrixIO.Format;
import edu.ucla.sspace.matrix.SparseMatrix;
import edu.ucla.sspace.matrix.Transform;
import edu.ucla.sspace.text.IteratorFactory;
import edu.ucla.sspace.vector.CompactSparseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.SparseHashDoubleVector;
import edu.ucla.sspace.vector.Vector;
import edu.ucla.sspace.vector.VectorMath;
import edu.ucla.sspace.vector.Vectors;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

















































































































public class Coals
  implements SemanticSpace
{
  public static final String PROPERTY_PREFIX = "edu.ucla.sspace.coals.Coals";
  public static final String REDUCE_MATRIX_PROPERTY = "edu.ucla.sspace.coals.Coals.reduce";
  public static final String REDUCE_DIMENSION_PROPERTY = "edu.ucla.sspace.coals.Coals.dimension";
  public static final String MAX_DIMENSIONS_PROPERTY = "edu.ucla.sspace.coals.Coals.maxDimensions";
  public static final String MAX_WORDS_PROPERTY = "edu.ucla.sspace.coals.Coals.maxWords";
  public static final String DO_NOT_NORMALIZE_PROPERTY = "edu.ucla.sspace.coals.Coals.doNotNormalize";
  private static final int DEFAULT_REDUCE_DIMENSIONS = 800;
  private static final int DEFAULT_MAX_DIMENSIONS = 14000;
  private static final int DEFAULT_MAX_WORDS = 15000;
  public static final String COALS_SSPACE_NAME = "coals-semantic-space";
  private static final Logger COALS_LOGGER = Logger.getLogger(Coals.class.getName());
  




  private Map<String, SparseDoubleVector> wordToSemantics;
  



  private Map<String, Integer> termToIndex;
  



  private ConcurrentMap<String, AtomicInteger> totalWordFreq;
  



  private Matrix finalCorrelation;
  



  private final int reducedDimensions;
  



  private final int maxWords;
  



  private final int maxDimensions;
  



  private int wordIndexCounter;
  



  private final MatrixFactorization reducer;
  



  private final Transform transform;
  




  public Coals(Transform transform, MatrixFactorization reducer)
  {
    this(transform, reducer, 800, 15000, 14000);
  }
  






  public Coals(Transform transform, MatrixFactorization reducer, int reducedDimensions, int maxWords, int maxDimensions)
  {
    termToIndex = new HashMap();
    totalWordFreq = new ConcurrentHashMap();
    wordToSemantics = new HashMap(1024, 4.0F);
    finalCorrelation = null;
    this.transform = transform;
    this.reducer = reducer;
    this.reducedDimensions = (reducedDimensions == 0 ? 
      800 : 
      reducedDimensions);
    this.maxWords = (maxWords == 0 ? 
      15000 : 
      maxWords);
    this.maxDimensions = (maxDimensions == 0 ? 
      14000 : 
      maxDimensions);
  }
  


  public Set<String> getWords()
  {
    return termToIndex.keySet();
  }
  


  public Vector getVector(String term)
  {
    Integer index = (Integer)termToIndex.get(term);
    if (index == null)
      return null;
    return Vectors.immutable(
      finalCorrelation.getRowVector(index.intValue()));
  }
  
  public String getSpaceName() {
    String ret = "coals-semantic-space";
    if (reducer != null)
      ret = ret + "-svd-" + reducedDimensions;
    return ret;
  }
  
  public int getVectorLength() {
    return finalCorrelation.columns();
  }
  

  public void processDocument(BufferedReader document)
    throws IOException
  {
    Map<String, Integer> wordFreq = new HashMap();
    Map<String, SparseDoubleVector> wordDocSemantics = 
      new HashMap();
    


    Queue<String> prevWords = new ArrayDeque();
    Queue<String> nextWords = new ArrayDeque();
    
    Iterator<String> it = IteratorFactory.tokenizeOrdered(document);
    
    int i = 0;
    do { nextWords.offer((String)it.next());i++;
      if (i >= 4) break; } while (it.hasNext());
    



    while (!nextWords.isEmpty())
    {

      if (it.hasNext()) {
        nextWords.offer((String)it.next());
      }
      
      String focusWord = (String)nextWords.remove();
      if (!focusWord.equals("")) {
        getIndexFor(focusWord);
        

        focusFreq = (Integer)wordFreq.get(focusWord);
        wordFreq.put(focusWord, 
        
          Integer.valueOf(focusFreq == null ? 1 : 1 + focusFreq.intValue()));
        


        SparseDoubleVector focusSemantics = (SparseDoubleVector)wordDocSemantics.get(
          focusWord);
        if (focusSemantics == null) {
          focusSemantics = new SparseHashDoubleVector(
            Integer.MAX_VALUE);
          wordDocSemantics.put(focusWord, focusSemantics);
        }
        

        int offset = 4 - prevWords.size();
        for (String word : prevWords) {
          offset++;
          if (!word.equals(""))
          {
            int index = getIndexFor(word);
            focusSemantics.add(index, offset);
          }
        }
        
        offset = 5;
        for (String word : nextWords) {
          offset--;
          if (!word.equals(""))
          {
            int index = getIndexFor(word);
            focusSemantics.add(index, offset);
          }
        }
      }
      prevWords.offer(focusWord);
      if (prevWords.size() > 4) {
        prevWords.remove();
      }
    }
    


    Integer focusFreq = wordDocSemantics.entrySet().iterator();
    while (focusFreq.hasNext()) {
      Map.Entry<String, SparseDoubleVector> e = (Map.Entry)focusFreq.next();
      SparseDoubleVector focusSemantics = getSemanticVector(
        (String)e.getKey());
      

      focusSemantics.getNonZeroIndices();
      synchronized (focusSemantics) {
        VectorMath.add(focusSemantics, (DoubleVector)e.getValue());
      }
    }
    


    for (Map.Entry<String, Integer> entry : wordFreq.entrySet()) {
      int count = ((Integer)entry.getValue()).intValue();
      AtomicInteger freq = (AtomicInteger)totalWordFreq.putIfAbsent(
        (String)entry.getKey(), new AtomicInteger(count));
      if (freq != null) {
        freq.addAndGet(count);
      }
    }
  }
  







  private SparseDoubleVector getSemanticVector(String word)
  {
    SparseDoubleVector v = (SparseDoubleVector)wordToSemantics.get(word);
    if (v == null)
    {

      synchronized (this)
      {

        v = (SparseDoubleVector)wordToSemantics.get(word);
        if (v == null) {
          v = new CompactSparseVector();
          wordToSemantics.put(word, v);
        }
      }
    }
    return v;
  }
  




  private int getIndexFor(String word)
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
  


  public void processSpace(Properties props)
  {
    COALS_LOGGER.info("Droppring dimensions from co-occurrance matrix.");
    
    finalCorrelation = buildMatrix(maxWords, maxDimensions);
    COALS_LOGGER.info("Done dropping dimensions.");
    
    if (transform != null) {
      COALS_LOGGER.info("Normalizing co-occurrance matrix.");
      

      int wordCount = finalCorrelation.rows();
      finalCorrelation = transform.transform(finalCorrelation);
      COALS_LOGGER.info("Done normalizing co-occurrance matrix.");
    }
    
    if (reducer != null) {
      if (reducedDimensions > finalCorrelation.columns()) {
        throw new IllegalArgumentException(
          "Cannot reduce to more dimensions than exist");
      }
      COALS_LOGGER.info("Reducing using SVD.");
      try {
        File coalsMatrixFile = 
          File.createTempFile("coals-term-doc-matrix", "dat");
        coalsMatrixFile.deleteOnExit();
        MatrixIO.writeMatrix(finalCorrelation, 
          coalsMatrixFile, 
          MatrixIO.Format.SVDLIBC_SPARSE_BINARY);
        
        MatrixFile processedSpace = new MatrixFile(
          coalsMatrixFile, MatrixIO.Format.SVDLIBC_SPARSE_BINARY);
        reducer.factorize(processedSpace, reducedDimensions);
        
        finalCorrelation = reducer.dataClasses();
      } catch (IOException ioe) {
        throw new IOError(ioe);
      }
      COALS_LOGGER.info("Done reducing using SVD.");
    }
  }
  









  private Matrix buildMatrix(int maxWords, int maxDimensions)
  {
    SparseDoubleVector[] vectorList = 
      new SparseDoubleVector[wordToSemantics.size()];
    
    Iterator localIterator1 = wordToSemantics.entrySet().iterator();
    while (localIterator1.hasNext()) {
      Map.Entry<String, SparseDoubleVector> e = (Map.Entry)localIterator1.next();
      vectorList[getIndexFor((String)e.getKey())] = ((SparseDoubleVector)e.getValue()); }
    SparseMatrix matrix = Matrices.asSparseMatrix(
      Arrays.asList(vectorList));
    

    if ((maxWords == 0) || (maxWords > wordToSemantics.size())) {
      maxWords = wordToSemantics.size();
    }
    COALS_LOGGER.info("Forming the inverse mapping from terms to indices.");
    

    String[] indexToTerm = new String[termToIndex.size()];
    for (Map.Entry<String, Integer> entry : termToIndex.entrySet()) {
      indexToTerm[((Integer)entry.getValue()).intValue()] = ((String)entry.getKey());
    }
    COALS_LOGGER.info("Sorting the terms based on frequency.");
    

    ArrayList<Map.Entry<String, AtomicInteger>> wordCountList = 
      new ArrayList(
      totalWordFreq.entrySet());
    Collections.sort(wordCountList, new EntryComp(null));
    


    COALS_LOGGER.info("Generating the index masks.");
    

    int wordCount = wordCountList.size() > maxDimensions ? 
      maxDimensions : 
      wordCountList.size();
    
    int[] rowMask = new int[maxWords];
    int[] colMask = new int[wordCount];
    



    SparseDoubleVector[] newVectorList = new SparseDoubleVector[maxWords];
    



    int termCount = 0;
    for (Map.Entry<String, AtomicInteger> entry : wordCountList) {
      Integer oldIndex = (Integer)termToIndex.get(entry.getKey());
      

      if (oldIndex != null)
      {



        if (termCount < maxWords) {
          if (termCount < wordCount) {
            colMask[termCount] = oldIndex.intValue();
          }
          newVectorList[termCount] = vectorList[oldIndex.intValue()];
          

          rowMask[termCount] = termCount;
          termToIndex.put((String)entry.getKey(), Integer.valueOf(termCount));
          termCount++;
        }
        else
        {
          termToIndex.remove(entry.getKey());
        } }
    }
    wordToSemantics = null;
    matrix = Matrices.asSparseMatrix(Arrays.asList(newVectorList));
    
    return new CellMaskedSparseMatrix(matrix, rowMask, colMask);
  }
  
  private class EntryComp implements Comparator<Map.Entry<String, AtomicInteger>> {
    private EntryComp() {}
    
    public int compare(Map.Entry<String, AtomicInteger> o1, Map.Entry<String, AtomicInteger> o2) {
      int diff = ((AtomicInteger)o2.getValue()).get() - ((AtomicInteger)o1.getValue()).get();
      return diff != 0 ? diff : ((String)o2.getKey()).compareTo((String)o1.getKey());
    }
  }
}
