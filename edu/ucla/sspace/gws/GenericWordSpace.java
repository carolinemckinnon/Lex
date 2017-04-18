package edu.ucla.sspace.gws;

import edu.ucla.sspace.basis.BasisMapping;
import edu.ucla.sspace.common.DimensionallyInterpretableSemanticSpace;
import edu.ucla.sspace.common.Filterable;
import edu.ucla.sspace.matrix.Matrices;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.Transform;
import edu.ucla.sspace.text.IteratorFactory;
import edu.ucla.sspace.util.Duple;
import edu.ucla.sspace.util.ReflectionUtil;
import edu.ucla.sspace.vector.CompactSparseIntegerVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseIntegerVector;
import edu.ucla.sspace.vector.Vector;
import edu.ucla.sspace.vector.Vectors;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;




































































































































































public class GenericWordSpace
  implements DimensionallyInterpretableSemanticSpace<String>, Filterable, Serializable
{
  private static final long serialVersionUID = 1L;
  public static final String GWS_SSPACE_NAME = "generic-word-space";
  private static final String PROPERTY_PREFIX = "edu.ucla.sspace.gws.GenericWordSpace";
  public static final String WINDOW_SIZE_PROPERTY = "edu.ucla.sspace.gws.GenericWordSpace.windowSize";
  public static final String USE_WORD_ORDER_PROPERTY = "edu.ucla.sspace.gws.GenericWordSpace.useWordOrder";
  public static final String TRANSFORM_PROPERTY = "edu.ucla.sspace.gws.GenericWordSpace.transform";
  public static final int DEFAULT_WINDOW_SIZE = 2;
  private final Map<String, SparseIntegerVector> wordToSemantics;
  private final int windowSize;
  private final Set<String> semanticFilter;
  private final BasisMapping<Duple<String, Integer>, String> basisMapping;
  private Map<String, DoubleVector> wordToTransformedVector;
  
  public GenericWordSpace()
  {
    this(System.getProperties());
  }
  




  public GenericWordSpace(Properties properties)
  {
    String windowSizeProp = properties.getProperty("edu.ucla.sspace.gws.GenericWordSpace.windowSize");
    windowSize = (windowSizeProp != null ? 
      Integer.parseInt(windowSizeProp) : 
      2);
    
    String useWordOrderProp = 
      properties.getProperty("edu.ucla.sspace.gws.GenericWordSpace.useWordOrder");
    boolean useWordOrder = useWordOrderProp != null ? 
      Boolean.parseBoolean(useWordOrderProp) : 
      false;
    
    basisMapping = (useWordOrder ? 
      new WordOrderBasisMapping() : 
      new WordBasisMapping());
    
    wordToSemantics = new HashMap(1024, 4.0F);
    wordToTransformedVector = null;
    semanticFilter = new HashSet();
  }
  



  public GenericWordSpace(int windowSize)
  {
    this(windowSize, new WordBasisMapping());
  }
  





  public GenericWordSpace(int windowSize, boolean useWordOrder)
  {
    this(windowSize, useWordOrder ? new WordOrderBasisMapping() : new WordBasisMapping());
  }
  








  public GenericWordSpace(int windowSize, BasisMapping<Duple<String, Integer>, String> basis)
  {
    if (windowSize < 1)
      throw new IllegalArgumentException("windowSize must be at least 1");
    if (basis == null) {
      throw new NullPointerException("basis cannot be null");
    }
    this.windowSize = windowSize;
    basisMapping = basis;
    wordToSemantics = new HashMap(1024, 4.0F);
    semanticFilter = new HashSet();
  }
  





  public void clearSemantics()
  {
    wordToSemantics.clear();
  }
  








  private SparseIntegerVector getSemanticVector(String word)
  {
    SparseIntegerVector v = (SparseIntegerVector)wordToSemantics.get(word);
    if (v == null)
    {

      synchronized (this)
      {

        v = (SparseIntegerVector)wordToSemantics.get(word);
        if (v == null) {
          v = new CompactSparseIntegerVector(Integer.MAX_VALUE);
          wordToSemantics.put(word, v);
        }
      }
    }
    return v;
  }
  


  public String getDimensionDescription(int dimension)
  {
    return (String)basisMapping.getDimensionDescription(dimension);
  }
  













  public Vector getVector(String word)
  {
    Vector v = wordToTransformedVector != null ? 
      (Vector)wordToTransformedVector.get(word) : 
      (Vector)wordToSemantics.get(word);
    
    if (v == null) {
      return null;
    }
    


    return Vectors.immutable(Vectors.subview(v, 0, getVectorLength()));
  }
  


  public String getSpaceName()
  {
    return 
      "generic-word-space-w-" + windowSize + "-" + basisMapping;
  }
  


  public int getVectorLength()
  {
    return basisMapping.numDimensions();
  }
  


  public Set<String> getWords()
  {
    return wordToTransformedVector != null ? 
      Collections.unmodifiableSet(wordToTransformedVector.keySet()) : 
      Collections.unmodifiableSet(wordToSemantics.keySet());
  }
  






  public void processDocument(BufferedReader document)
    throws IOException
  {
    if (wordToTransformedVector != null) {
      throw new IllegalStateException("Cannot add new documents to a GenericWordSpace whose vectors have been transformed");
    }
    

    Queue<String> prevWords = new ArrayDeque(windowSize);
    Queue<String> nextWords = new ArrayDeque(windowSize);
    
    Iterator<String> documentTokens = 
      IteratorFactory.tokenizeOrdered(document);
    
    String focusWord = null;
    

    int i = 0;
    do { nextWords.offer((String)documentTokens.next());i++;
      if (i >= windowSize) break; } while (documentTokens.hasNext());
    

    while (!nextWords.isEmpty()) {
      focusWord = (String)nextWords.remove();
      

      if (documentTokens.hasNext()) {
        String windowEdge = (String)documentTokens.next();
        nextWords.offer(windowEdge);
      }
      




      boolean calculateSemantics = 
        ((semanticFilter.isEmpty()) || (semanticFilter.contains(focusWord))) && 
        (!focusWord.equals(""));
      
      if (calculateSemantics) {
        SparseIntegerVector focusSemantics = 
          getSemanticVector(focusWord);
        


        int position = -prevWords.size();
        for (String word : prevWords)
        {




          if (word.equals("")) {
            position++;
          }
          else
          {
            int dimension = basisMapping.getDimension(
              new Duple(word, Integer.valueOf(position)));
            synchronized (focusSemantics) {
              focusSemantics.add(dimension, 1);
            }
            position++;
          }
        }
        
        position = 1;
        for (String word : nextWords)
        {




          if (word.equals("")) {
            position++;
          }
          else
          {
            int dimension = basisMapping.getDimension(
              new Duple(word, Integer.valueOf(position)));
            synchronized (focusSemantics) {
              focusSemantics.add(dimension, 1);
            }
            position++;
          }
        }
      }
      


      prevWords.offer(focusWord);
      if (prevWords.size() > windowSize) {
        prevWords.remove();
      }
    }
    
    document.close();
  }
  













  public void processSpace(Properties properties)
  {
    String transformClassName = properties.getProperty("edu.ucla.sspace.gws.GenericWordSpace.transform");
    if (transformClassName == null) {
      return;
    }
    
    Transform transform = (Transform)ReflectionUtil.getObjectInstance(transformClassName);
    processSpace(transform);
  }
  








  public synchronized void processSpace(Transform transform)
  {
    if (transform == null) {
      throw new NullPointerException("transform cannot be null");
    }
    


    Map<String, ? extends Vector> wordToVector = 
      wordToTransformedVector != null ? 
      wordToTransformedVector : 
      wordToSemantics;
    


    List<String> wordsInOrder = new ArrayList(wordToVector.size());
    List<DoubleVector> vectorsInOrder = 
      new ArrayList(wordToVector.size());
    
    Iterator localIterator = wordToVector.entrySet().iterator();
    while (localIterator.hasNext()) {
      Map.Entry<String, ? extends Vector> e = (Map.Entry)localIterator.next();
      wordsInOrder.add((String)e.getKey());
      

      vectorsInOrder.add(Vectors.asDouble(
        Vectors.subview((Vector)e.getValue(), 0, getVectorLength())));
    }
    Matrix m = Matrices.asMatrix(vectorsInOrder);
    

    Matrix transformed = transform.transform(m);
    

    wordToSemantics.clear();
    int n = wordsInOrder.size();
    wordToTransformedVector = new HashMap(n);
    for (int i = 0; i < n; i++) {
      wordToTransformedVector.put(
        (String)wordsInOrder.get(i), transformed.getRowVector(i));
    }
  }
  







  public void setSemanticFilter(Set<String> semanticsToRetain)
  {
    semanticFilter.clear();
    semanticFilter.addAll(semanticsToRetain);
  }
}
