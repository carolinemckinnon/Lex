package edu.ucla.sspace.ri;

import edu.ucla.sspace.common.Filterable;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.index.PermutationFunction;
import edu.ucla.sspace.index.RandomIndexVectorGenerator;
import edu.ucla.sspace.index.TernaryPermutationFunction;
import edu.ucla.sspace.text.IteratorFactory;
import edu.ucla.sspace.util.GeneratorMap;
import edu.ucla.sspace.vector.CompactSparseIntegerVector;
import edu.ucla.sspace.vector.DenseIntVector;
import edu.ucla.sspace.vector.IntegerVector;
import edu.ucla.sspace.vector.TernaryVector;
import edu.ucla.sspace.vector.Vector;
import edu.ucla.sspace.vector.Vectors;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;



































































































































































































































public class RandomIndexing
  implements SemanticSpace, Filterable
{
  public static final String RI_SSPACE_NAME = "random-indexing";
  private static final String PROPERTY_PREFIX = "edu.ucla.sspace.ri.RandomIndexing";
  public static final String VECTOR_LENGTH_PROPERTY = "edu.ucla.sspace.ri.RandomIndexing.vectorLength";
  public static final String WINDOW_SIZE_PROPERTY = "edu.ucla.sspace.ri.RandomIndexing.windowSize";
  public static final String USE_PERMUTATIONS_PROPERTY = "edu.ucla.sspace.ri.RandomIndexing.usePermutations";
  public static final String PERMUTATION_FUNCTION_PROPERTY = "edu.ucla.sspace.ri.RandomIndexing.permutationFunction";
  public static final String USE_SPARSE_SEMANTICS_PROPERTY = "edu.ucla.sspace.ri.RandomIndexing.sparseSemantics";
  public static final String RANDOM_SEED_PROPERTY = "edu.ucla.sspace.ri.RandomIndexing.randomSeed";
  public static final int DEFAULT_WINDOW_SIZE = 2;
  public static final int DEFAULT_VECTOR_LENGTH = 4000;
  static final Random RANDOM = new Random();
  




  private final Map<String, TernaryVector> wordToIndexVector;
  




  private final Map<String, IntegerVector> wordToMeaning;
  




  private final int vectorLength;
  




  private final int windowSize;
  



  private final boolean usePermutations;
  



  private final PermutationFunction<TernaryVector> permutationFunc;
  



  private final boolean useSparseSemantics;
  



  private final Set<String> semanticFilter;
  




  public RandomIndexing()
  {
    this(System.getProperties());
  }
  



  public RandomIndexing(java.util.Properties properties, Object o)
  {
    String vectorLengthProp = 
      properties.getProperty("edu.ucla.sspace.ri.RandomIndexing.vectorLength");
    vectorLength = (vectorLengthProp != null ? 
      Integer.parseInt(vectorLengthProp) : 
      4000);
    
    String windowSizeProp = properties.getProperty("edu.ucla.sspace.ri.RandomIndexing.windowSize");
    windowSize = (windowSizeProp != null ? 
      Integer.parseInt(windowSizeProp) : 
      2);
    
    String usePermutationsProp = 
      properties.getProperty("edu.ucla.sspace.ri.RandomIndexing.usePermutations");
    usePermutations = (usePermutationsProp != null ? 
      Boolean.parseBoolean(usePermutationsProp) : 
      false);
    
    String permutationFuncProp = 
      properties.getProperty("edu.ucla.sspace.ri.RandomIndexing.permutationFunction");
    permutationFunc = (permutationFuncProp != null ? 
      loadPermutationFunction(permutationFuncProp) : 
      new TernaryPermutationFunction());
    
    RandomIndexVectorGenerator indexVectorGenerator = 
      new RandomIndexVectorGenerator(vectorLength, properties);
    
    String useSparseProp = 
      properties.getProperty("edu.ucla.sspace.ri.RandomIndexing.sparseSemantics");
    useSparseSemantics = (useSparseProp != null ? 
      Boolean.parseBoolean(useSparseProp) : 
      true);
    
    long randomSeed = 
      properties.getProperty("edu.ucla.sspace.ri.RandomIndexing.randomSeed") != null ? 
      Integer.parseInt(properties.getProperty("edu.ucla.sspace.ri.RandomIndexing.randomSeed")) : 
      System.currentTimeMillis();
    

    wordToIndexVector = new GeneratorMap(
      indexVectorGenerator);
    wordToMeaning = new ConcurrentHashMap();
    semanticFilter = new HashSet();
  }
  











  public RandomIndexing(java.util.Properties properties)
  {
    this(edu.ucla.sspace.util.Properties.getInt(properties, "edu.ucla.sspace.ri.RandomIndexing.vectorLength", 4000), edu.ucla.sspace.util.Properties.getInt(properties, "edu.ucla.sspace.ri.RandomIndexing.windowSize", 2), edu.ucla.sspace.util.Properties.getBoolean(properties, "edu.ucla.sspace.ri.RandomIndexing.usePermutations", false), loadPermutationFunction(properties.getProperty("edu.ucla.sspace.ri.RandomIndexing.permutationFunction")), edu.ucla.sspace.util.Properties.getBoolean(properties, "edu.ucla.sspace.ri.RandomIndexing.sparseSemantics", true), edu.ucla.sspace.util.Properties.getLong(properties, "edu.ucla.sspace.ri.RandomIndexing.randomSeed", System.currentTimeMillis()), properties);
  }
  
























  public RandomIndexing(int vectorLength, int windowSize, boolean usePermutations, PermutationFunction permutationFunc, boolean useSparseSemantics, long randomSeed, java.util.Properties otherProps)
  {
    if (permutationFunc == null) {
      throw new NullPointerException("permutationFunc cannot be null");
    }
    this.vectorLength = vectorLength;
    this.windowSize = windowSize;
    this.usePermutations = usePermutations;
    this.permutationFunc = permutationFunc;
    this.useSparseSemantics = useSparseSemantics;
    RANDOM.setSeed(randomSeed);
    
    RandomIndexVectorGenerator indexVectorGenerator = 
      new RandomIndexVectorGenerator(vectorLength, otherProps);
    wordToIndexVector = new GeneratorMap(
      indexVectorGenerator);
    wordToMeaning = new ConcurrentHashMap();
    semanticFilter = new HashSet();
  }
  









  private static PermutationFunction<TernaryVector> loadPermutationFunction(String className)
  {
    if (className == null)
      return new TernaryPermutationFunction();
    try {
      Class clazz = Class.forName(className);
      return (PermutationFunction)clazz.newInstance();
    }
    catch (Exception e) {
      throw new Error(e);
    }
  }
  





  public void clearSemantics()
  {
    wordToMeaning.clear();
  }
  








  private IntegerVector getSemanticVector(String word)
  {
    IntegerVector v = (IntegerVector)wordToMeaning.get(word);
    if (v == null)
    {

      synchronized (this)
      {

        v = (IntegerVector)wordToMeaning.get(word);
        if (v == null) {
          v = useSparseSemantics ? 
            new CompactSparseIntegerVector(vectorLength) : 
            new DenseIntVector(vectorLength);
          wordToMeaning.put(word, v);
        }
      }
    }
    return v;
  }
  


  public Vector getVector(String word)
  {
    IntegerVector v = (IntegerVector)wordToMeaning.get(word);
    if (v == null) {
      return null;
    }
    return Vectors.immutable(v);
  }
  


  public String getSpaceName()
  {
    return 
      "random-indexing-" + vectorLength + "v-" + windowSize + "w-" + (usePermutations ? 
      permutationFunc.toString() : 
      "noPermutations");
  }
  


  public int getVectorLength()
  {
    return vectorLength;
  }
  


  public Set<String> getWords()
  {
    return Collections.unmodifiableSet(wordToMeaning.keySet());
  }
  








  public Map<String, TernaryVector> getWordToIndexVector()
  {
    return Collections.unmodifiableMap(wordToIndexVector);
  }
  



  public void processDocument(BufferedReader document)
    throws IOException
  {
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
        (semanticFilter.isEmpty()) || ((semanticFilter.contains(focusWord)) && 
        (!focusWord.equals("")));
      
      if (calculateSemantics) {
        IntegerVector focusMeaning = getSemanticVector(focusWord);
        



        int permutations = -prevWords.size();
        for (String word : prevWords)
        {




          if (word.equals("")) {
            permutations++;
          }
          else
          {
            TernaryVector iv = (TernaryVector)wordToIndexVector.get(word);
            if (usePermutations) {
              iv = (TernaryVector)permutationFunc.permute(iv, permutations);
              permutations++;
            }
            
            add(focusMeaning, iv);
          }
        }
        
        permutations = 1;
        for (String word : nextWords)
        {




          if (word.equals("")) {
            permutations++;
          }
          else
          {
            TernaryVector iv = (TernaryVector)wordToIndexVector.get(word);
            if (usePermutations) {
              iv = (TernaryVector)permutationFunc.permute(iv, permutations);
              permutations++;
            }
            
            add(focusMeaning, iv);
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
  







  public void processSpace(java.util.Properties properties) {}
  







  public void setWordToIndexVector(Map<String, TernaryVector> m)
  {
    wordToIndexVector.clear();
    wordToIndexVector.putAll(m);
  }
  






  public void setSemanticFilter(Set<String> semanticsToRetain)
  {
    semanticFilter.clear();
    semanticFilter.addAll(semanticsToRetain);
  }
  







  private static void add(IntegerVector semantics, TernaryVector index)
  {
    synchronized (semantics) {
      for (int p : index.positiveDimensions())
        semantics.add(p, 1);
      for (int n : index.negativeDimensions()) {
        semantics.add(n, -1);
      }
    }
  }
}
