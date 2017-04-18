package edu.ucla.sspace.isa;

import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.index.PermutationFunction;
import edu.ucla.sspace.index.RandomIndexVectorGenerator;
import edu.ucla.sspace.index.TernaryPermutationFunction;
import edu.ucla.sspace.text.IteratorFactory;
import edu.ucla.sspace.util.GeneratorMap;
import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.SparseHashDoubleVector;
import edu.ucla.sspace.vector.TernaryVector;
import edu.ucla.sspace.vector.Vector;
import edu.ucla.sspace.vector.Vectors;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;























































































































































































































































































public class IncrementalSemanticAnalysis
  implements SemanticSpace
{
  private static final String PROPERTY_PREFIX = "edu.ucla.sspace.isa.IncrementalSemanticAnalysis";
  public static final String HISTORY_DECAY_RATE_PROPERTY = "edu.ucla.sspace.isa.IncrementalSemanticAnalysis.historyDecayRate";
  public static final String IMPACT_RATE_PROPERTY = "edu.ucla.sspace.isa.IncrementalSemanticAnalysis.impactRate";
  public static final String PERMUTATION_FUNCTION_PROPERTY = "edu.ucla.sspace.isa.IncrementalSemanticAnalysis.permutationFunction";
  public static final String USE_PERMUTATIONS_PROPERTY = "edu.ucla.sspace.isa.IncrementalSemanticAnalysis.usePermutations";
  public static final String VECTOR_LENGTH_PROPERTY = "edu.ucla.sspace.isa.IncrementalSemanticAnalysis.vectorLength";
  public static final String WINDOW_SIZE_PROPERTY = "edu.ucla.sspace.isa.IncrementalSemanticAnalysis.windowSize";
  public static final String USE_SPARSE_SEMANTICS_PROPERTY = "edu.ucla.sspace.isa.IncrementalSemanticAnalysis.sparseSemantics";
  public static final double DEFAULT_HISTORY_DECAY_RATE = 100.0D;
  public static final double DEFAULT_IMPACT_RATE = 0.003D;
  public static final int DEFAULT_VECTOR_LENGTH = 1800;
  public static final int DEFAULT_WINDOW_SIZE = 5;
  private final double historyDecayRate;
  private final double impactRate;
  private final PermutationFunction<TernaryVector> permutationFunc;
  private final boolean usePermutations;
  private final boolean useSparseSemantics;
  private final int vectorLength;
  private final int windowSize;
  private final Map<String, TernaryVector> wordToIndexVector;
  private final Map<String, SemanticVector> wordToMeaning;
  private final Map<String, Integer> wordToOccurrences;
  
  public IncrementalSemanticAnalysis()
  {
    this(System.getProperties());
  }
  






  public IncrementalSemanticAnalysis(Properties properties)
  {
    String vectorLengthProp = 
      properties.getProperty("edu.ucla.sspace.isa.IncrementalSemanticAnalysis.vectorLength");
    vectorLength = (vectorLengthProp != null ? 
      Integer.parseInt(vectorLengthProp) : 
      1800);
    
    String windowSizeProp = properties.getProperty("edu.ucla.sspace.isa.IncrementalSemanticAnalysis.windowSize");
    windowSize = (windowSizeProp != null ? 
      Integer.parseInt(windowSizeProp) : 
      5);
    
    String usePermutationsProp = 
      properties.getProperty("edu.ucla.sspace.isa.IncrementalSemanticAnalysis.usePermutations");
    usePermutations = (usePermutationsProp != null ? 
      Boolean.parseBoolean(usePermutationsProp) : 
      false);
    
    String permutationFuncProp = 
      properties.getProperty("edu.ucla.sspace.isa.IncrementalSemanticAnalysis.permutationFunction");
    permutationFunc = (permutationFuncProp != null ? 
      loadPermutationFunction(permutationFuncProp) : 
      new TernaryPermutationFunction());
    
    RandomIndexVectorGenerator indexVectorGenerator = 
      new RandomIndexVectorGenerator(vectorLength, properties);
    
    String useSparseProp = 
      properties.getProperty("edu.ucla.sspace.isa.IncrementalSemanticAnalysis.sparseSemantics");
    useSparseSemantics = (useSparseProp != null ? 
      Boolean.parseBoolean(useSparseProp) : 
      false);
    
    String decayRateProp = 
      properties.getProperty("edu.ucla.sspace.isa.IncrementalSemanticAnalysis.historyDecayRate");
    historyDecayRate = (decayRateProp != null ? 
      Double.parseDouble(decayRateProp) : 
      100.0D);
    
    String impactRateProp = 
      properties.getProperty("edu.ucla.sspace.isa.IncrementalSemanticAnalysis.impactRate");
    impactRate = (impactRateProp != null ? 
      Double.parseDouble(impactRateProp) : 
      0.003D);
    
    wordToIndexVector = 
      new GeneratorMap(indexVectorGenerator);
    wordToMeaning = new HashMap();
    wordToOccurrences = new HashMap();
  }
  









  private static PermutationFunction<TernaryVector> loadPermutationFunction(String className)
  {
    try
    {
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
  








  private SemanticVector getSemanticVector(String word)
  {
    SemanticVector v = (SemanticVector)wordToMeaning.get(word);
    if (v == null) {
      v = useSparseSemantics ? 
        new SparseSemanticVector(vectorLength) : 
        new DenseSemanticVector(vectorLength);
      wordToMeaning.put(word, v);
    }
    return v;
  }
  


  public String getSpaceName()
  {
    return 
    
      "IncrementSemanticAnalysis--" + vectorLength + "v-" + windowSize + "w-" + (usePermutations ? 
      permutationFunc.toString() : 
      "noPermutations");
  }
  


  public Vector getVector(String word)
  {
    SemanticVector v = (SemanticVector)wordToMeaning.get(word);
    if (v == null) {
      return null;
    }
    return Vectors.immutable(v);
  }
  


  public int getVectorLength()
  {
    return vectorLength;
  }
  








  public Map<String, TernaryVector> getWordToIndexVector()
  {
    return Collections.unmodifiableMap(wordToIndexVector);
  }
  


  public Set<String> getWords()
  {
    return Collections.unmodifiableSet(wordToMeaning.keySet());
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
    

    while (!nextWords.isEmpty())
    {
      focusWord = (String)nextWords.remove();
      

      if (documentTokens.hasNext()) {
        String windowEdge = (String)documentTokens.next();
        nextWords.offer(windowEdge);
      }
      


      if (!focusWord.equals("")) {
        SemanticVector focusMeaning = getSemanticVector(focusWord);
        



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
            
            updateSemantics(focusMeaning, word, iv);
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
            
            updateSemantics(focusMeaning, word, iv);
          }
        }
      }
      


      prevWords.offer(focusWord);
      


      Integer count = (Integer)wordToOccurrences.get(focusWord);
      wordToOccurrences.put(focusWord, Integer.valueOf(count == null ? 1 : count.intValue() + 1));
      
      if (prevWords.size() > windowSize) {
        prevWords.remove();
      }
    }
    
    document.close();
  }
  







  public void processSpace(Properties properties) {}
  







  public void setWordToIndexVector(Map<String, TernaryVector> m)
  {
    wordToIndexVector.clear();
    wordToIndexVector.putAll(m);
  }
  












  private void updateSemantics(SemanticVector toUpdate, String cooccurringWord, TernaryVector iv)
  {
    SemanticVector prevWordSemantics = getSemanticVector(cooccurringWord);
    
    Integer occurrences = (Integer)wordToOccurrences.get(cooccurringWord);
    if (occurrences == null)
      occurrences = Integer.valueOf(0);
    double semanticWeight = 
      1.0D / Math.exp(occurrences.intValue() / historyDecayRate);
    




    add(toUpdate, iv, impactRate * (1.0D - semanticWeight));
    toUpdate.addVector(prevWordSemantics, impactRate * semanticWeight);
  }
  











  private static void add(DoubleVector semantics, TernaryVector index, double percentage)
  {
    for (int p : index.positiveDimensions())
      semantics.add(p, percentage);
    for (int n : index.negativeDimensions()) {
      semantics.add(n, -percentage);
    }
  }
  





  private class DenseSemanticVector
    extends DenseVector
    implements IncrementalSemanticAnalysis.SemanticVector<DenseVector>
  {
    private static final long serialVersionUID = 1L;
    





    public DenseSemanticVector(int vectorLength)
    {
      super();
    }
    
    public void addVector(DenseVector v, double percentage) {
      int length = v.length();
      for (int i = 0; i < length; i++)
        add(i, percentage * v.get(i));
    }
  }
  
  private static abstract interface SemanticVector<T extends DoubleVector> extends DoubleVector {
    public abstract void addVector(T paramT, double paramDouble);
  }
  
  private class SparseSemanticVector extends SparseHashDoubleVector implements IncrementalSemanticAnalysis.SemanticVector<SparseDoubleVector> { private static final long serialVersionUID = 1L;
    
    public SparseSemanticVector(int vectorLength) { super(); }
    
    public void addVector(SparseDoubleVector v, double percentage)
    {
      for (int n : v.getNonZeroIndices()) {
        add(n, percentage * v.get(n));
      }
    }
  }
}
