package edu.ucla.sspace.tools;

import edu.ucla.sspace.basis.BasisMapping;
import edu.ucla.sspace.basis.StringBasisMapping;
import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.hal.LinearWeighting;
import edu.ucla.sspace.hal.WeightingFunction;
import edu.ucla.sspace.mains.GenericMain;
import edu.ucla.sspace.matrix.AtomicGrowingSparseHashMatrix;
import edu.ucla.sspace.matrix.NoTransform;
import edu.ucla.sspace.matrix.SparseMatrix;
import edu.ucla.sspace.matrix.Transform;
import edu.ucla.sspace.text.IteratorFactory;
import edu.ucla.sspace.util.BoundedSortedMap;
import edu.ucla.sspace.util.Pair;
import edu.ucla.sspace.util.ReflectionUtil;
import edu.ucla.sspace.vector.DenseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;





































public class BasisMaker
  extends GenericMain
{
  public BasisMaker() {}
  
  protected void addExtraOptions(ArgOptions options)
  {
    options.addOption('T', "matrixTransform", 
      "Specifies the matrix transform that should be applied to co-occurrence counts after they have been generated", 
      
      true, "CLASSNAME", "Optional");
    options.addOption('b', "basisSize", 
      "Specifies the total desired size of the basis (Default: 10000)", 
      
      true, "INT", "Optional");
    options.addOption('w', "windowSize", 
      "Specifies the sliding window size (Default: 5)", 
      true, "INT", "Optional");
    options.addOption('p', "printWeights", 
      "If true, each saved word and it's associated weight will be printed to standard out", 
      
      false, null, "Optional");
  }
  


  protected SemanticSpace getSpace()
  {
    Transform transform = null;
    if (argOptions.hasOption('T')) {
      transform = (Transform)ReflectionUtil.getObjectInstance(
        argOptions.getStringOption('T'));
    } else
      transform = new NoTransform();
    int bound = argOptions.getIntOption('b', 10000);
    int windowSize = argOptions.getIntOption('w', 5);
    return new OccurrenceCounter(transform, bound, windowSize);
  }
  



  protected void saveSSpace(SemanticSpace sspace, File outputFile)
    throws IOException
  {
    BasisMapping<String, String> savedTerms = new StringBasisMapping();
    for (String term : sspace.getWords()) {
      savedTerms.getDimension(term);
    }
    ObjectOutputStream ouStream = new ObjectOutputStream(
      new FileOutputStream(outputFile));
    ouStream.writeObject(savedTerms);
    ouStream.close();
  }
  





  public class OccurrenceCounter
    implements SemanticSpace
  {
    private final AtomicGrowingSparseHashMatrix cooccurrenceMatrix;
    




    private final WeightingFunction weighting;
    




    private final BasisMapping<String, String> basis;
    




    private final Map<String, Double> wordScores;
    




    private final Transform transform;
    



    private final int windowSize;
    




    public OccurrenceCounter(Transform transform, int bound, int windowSize)
    {
      cooccurrenceMatrix = new AtomicGrowingSparseHashMatrix();
      basis = new StringBasisMapping();
      wordScores = new BoundedSortedMap(bound);
      weighting = new LinearWeighting();
      
      this.transform = transform;
      this.windowSize = windowSize;
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
      

      while (!nextWords.isEmpty())
      {
        focus = (String)nextWords.remove();
        

        if (documentTokens.hasNext()) {
          nextWords.offer((String)documentTokens.next());
        }
        

        if (!focus.equals("")) {
          int focusIndex = basis.getDimension(focus);
          
          countOccurrences(nextWords, focusIndex, 
            1, matrixEntryToCount);
          countOccurrences(prevWords, focusIndex, 
            -prevWords.size(), matrixEntryToCount);
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
    







    private void countOccurrences(Queue<String> words, int focusIndex, int wordDistance, Map<Pair<Integer>, Double> entryCounts)
    {
      for (String term : words)
      {

        if (!term.equals("")) {
          int index = basis.getDimension(term);
          



          Pair<Integer> p = new Pair(Integer.valueOf(focusIndex), Integer.valueOf(index));
          double value = weighting.weight(wordDistance, windowSize);
          Double curCount = (Double)entryCounts.get(p);
          entryCounts.put(p, Double.valueOf(curCount == null ? value : value + curCount.doubleValue()));
        }
        wordDistance++;
      }
    }
    


    public Set<String> getWords()
    {
      return Collections.unmodifiableSet(wordScores.keySet());
    }
    


    public DoubleVector getVector(String word)
    {
      Double score = (Double)wordScores.get(word);
      return score == null ? 
        new DenseVector(new double[] { 0.0D }) : 
        new DenseVector(new double[] { score.doubleValue() });
    }
    


    public int getVectorLength()
    {
      return 1;
    }
    


    public void processSpace(Properties properties)
    {
      SparseMatrix cleanedMatrix = (SparseMatrix)transform.transform(
        cooccurrenceMatrix);
      for (String term : basis.keySet()) {
        int index = basis.getDimension(term);
        SparseDoubleVector sdv = cleanedMatrix.getRowVector(index);
        
        double score = 0.0D;
        for (int i : sdv.getNonZeroIndices()) {
          score += sdv.get(i);
        }
        wordScores.put(term, Double.valueOf(score));
      }
    }
    


    public String getSpaceName()
    {
      return "BasisMaker";
    }
  }
}
