package edu.ucla.sspace.beagle;

import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.fft.FastFourierTransform;
import edu.ucla.sspace.text.IteratorFactory;
import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.VectorMath;
import edu.ucla.sspace.vector.Vectors;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;
















































public class Beagle
  implements SemanticSpace
{
  public static final int CONTEXT_SIZE = 6;
  public static final String BEAGLE_SSPACE_NAME = "beagle-semantic-space";
  
  public static enum SemanticType
  {
    CONTEXT, 
    ORDERING, 
    COMPOSITE;
  }
  
















  private static final Logger LOGGER = Logger.getLogger(Beagle.class.getName());
  



  private final Map<String, DoubleVector> vectorMap;
  



  private final ConcurrentMap<String, DoubleVector> termHolographs;
  



  private final int indexVectorSize;
  



  private int prevSize;
  



  private int nextSize;
  


  private DoubleVector placeHolder;
  


  private int[] permute1;
  


  private int[] permute2;
  


  private final SemanticType semanticType;
  



  public Beagle(int vectorSize, Map<String, DoubleVector> vectorMap)
  {
    this(vectorSize, SemanticType.COMPOSITE, vectorMap);
  }
  

  public Beagle(int vectorSize, SemanticType semanticType, Map<String, DoubleVector> vectorMap)
  {
    indexVectorSize = vectorSize;
    this.vectorMap = vectorMap;
    termHolographs = new ConcurrentHashMap();
    this.semanticType = semanticType;
    
    placeHolder = ((DoubleVector)vectorMap.get(""));
    

    permute1 = new int[indexVectorSize];
    permute2 = new int[indexVectorSize];
    randomPermute(permute1);
    randomPermute(permute2);
    
    prevSize = 1;
    nextSize = 5;
  }
  


  public Set<String> getWords()
  {
    return termHolographs.keySet();
  }
  


  public DoubleVector getVector(String term)
  {
    return Vectors.immutable((DoubleVector)termHolographs.get(term));
  }
  


  public String getSpaceName()
  {
    return 
    
      "beagle-semantic-space-" + indexVectorSize + "-" + semanticType.toString();
  }
  


  public int getVectorLength()
  {
    return indexVectorSize;
  }
  

  public void processDocument(BufferedReader document)
    throws IOException
  {
    Queue<String> prevWords = new ArrayDeque();
    Queue<String> nextWords = new ArrayDeque();
    
    Iterator<String> it = IteratorFactory.tokenizeOrdered(document);
    Map<String, DoubleVector> documentVectors = 
      new HashMap();
    


    for (int i = 0; (i < nextSize) && (it.hasNext()); i++)
      nextWords.offer(((String)it.next()).intern());
    prevWords.offer("");
    
    String focusWord = null;
    while (!nextWords.isEmpty()) {
      focusWord = (String)nextWords.remove();
      
      if (it.hasNext()) {
        nextWords.offer(((String)it.next()).intern());
      }
      if (!focusWord.equals(""))
      {


        DoubleVector meaning = (DoubleVector)termHolographs.get(focusWord);
        if (meaning == null) {
          meaning = new DenseVector(indexVectorSize);
          documentVectors.put(focusWord, meaning);
        }
        updateMeaning(meaning, prevWords, nextWords);
      }
      
      prevWords.offer(focusWord);
      if (prevWords.size() > 1) {
        prevWords.remove();
      }
    }
    

    Iterator localIterator = documentVectors.entrySet().iterator();
    while (localIterator.hasNext()) {
      Map.Entry<String, DoubleVector> entry = (Map.Entry)localIterator.next();
      synchronized ((String)entry.getKey())
      {



        DoubleVector existingVector = 
          (DoubleVector)termHolographs.get(entry.getKey());
        if (existingVector == null) {
          termHolographs.put((String)entry.getKey(), (DoubleVector)entry.getValue());
        } else {
          VectorMath.add(existingVector, (DoubleVector)entry.getValue());
        }
      }
    }
  }
  








  public void processSpace(Properties properties) {}
  







  private void updateMeaning(DoubleVector meaning, Queue<String> prevWords, Queue<String> nextWords)
  {
    if ((semanticType == SemanticType.COMPOSITE) || 
      (semanticType == SemanticType.CONTEXT)) {
      DoubleVector context = new DenseVector(indexVectorSize);
      

      for (String term : prevWords) {
        if (!term.equals(""))
        {
          VectorMath.add(context, (DoubleVector)vectorMap.get(term));
        }
      }
      
      for (String term : nextWords) {
        if (!term.equals(""))
        {
          VectorMath.add(context, (DoubleVector)vectorMap.get(term));
        }
      }
      
      normalize(context);
      VectorMath.add(meaning, context);
    }
    


    if ((semanticType == SemanticType.COMPOSITE) || 
      (semanticType == SemanticType.ORDERING)) {
      DoubleVector order = groupConvolution(prevWords, nextWords);
      

      normalize(order);
      VectorMath.add(meaning, order);
    }
  }
  



  private void normalize(DoubleVector v)
  {
    double magnitude = 0.0D;
    for (int i = 0; i < v.length(); i++)
      magnitude += Math.pow(v.get(i), 2.0D);
    if (magnitude == 0.0D) {
      return;
    }
    magnitude = Math.sqrt(magnitude);
    for (int i = 0; i < v.length(); i++) {
      v.set(i, v.get(i) / magnitude);
    }
  }
  










  private DoubleVector groupConvolution(Queue<String> prevWords, Queue<String> nextWords)
  {
    DoubleVector result = new DenseVector(indexVectorSize);
    

    String prevWord = (String)prevWords.peek();
    
    if (!prevWord.equals("")) {
      DoubleVector tempConvolution = 
        convolute((DoubleVector)vectorMap.get(prevWords.peek()), placeHolder);
      VectorMath.add(result, tempConvolution);
    } else {
      tempConvolution = placeHolder;
    }
    
    for (String term : nextWords) {
      if (!term.equals(""))
      {

        tempConvolution = convolute(tempConvolution, (DoubleVector)vectorMap.get(term));
        VectorMath.add(result, tempConvolution);
      }
    }
    DoubleVector tempConvolution = placeHolder;
    

    for (String term : nextWords)
      if (!term.equals(""))
      {

        tempConvolution = convolute(tempConvolution, (DoubleVector)vectorMap.get(term));
        VectorMath.add(result, tempConvolution);
      }
    return result;
  }
  



  private void randomPermute(int[] permute)
  {
    for (int i = 0; i < indexVectorSize; i++)
      permute[i] = i;
    for (int i = indexVectorSize - 1; i > 0; i--) {
      int w = (int)Math.floor(Math.random() * (i + 1));
      int temp = permute[w];
      permute[w] = permute[i];
      permute[i] = permute[w];
    }
  }
  
  private DoubleVector convolute(DoubleVector left, DoubleVector right)
  {
    left = changeVector(left, permute1);
    right = changeVector(right, permute2);
    

    FastFourierTransform.transform(left);
    FastFourierTransform.transform(right);
    

    DoubleVector result = VectorMath.multiply(left, right);
    

    FastFourierTransform.backtransform(result);
    return result;
  }
  








  private DoubleVector changeVector(DoubleVector data, int[] orderVector)
  {
    DoubleVector result = new DenseVector(indexVectorSize);
    for (int i = 0; i < indexVectorSize; i++)
      result.set(i, data.get(orderVector[i]));
    return result;
  }
}
