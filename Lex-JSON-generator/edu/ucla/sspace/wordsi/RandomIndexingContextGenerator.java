package edu.ucla.sspace.wordsi;

import edu.ucla.sspace.index.PermutationFunction;
import edu.ucla.sspace.vector.CompactSparseVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.TernaryVector;
import java.util.Map;
import java.util.Queue;



































































public class RandomIndexingContextGenerator
  implements ContextGenerator
{
  private final Map<String, TernaryVector> indexMap;
  private final PermutationFunction<TernaryVector> permFunc;
  private final int indexVectorLength;
  private boolean readOnly;
  
  public RandomIndexingContextGenerator(Map<String, TernaryVector> indexMap, PermutationFunction<TernaryVector> perm, int indexVectorLength)
  {
    this.indexMap = indexMap;
    permFunc = perm;
    this.indexVectorLength = indexVectorLength;
  }
  



  public SparseDoubleVector generateContext(Queue<String> prevWords, Queue<String> nextWords)
  {
    SparseDoubleVector meaning = new CompactSparseVector(indexVectorLength);
    addContextTerms(meaning, prevWords, -1 * prevWords.size());
    addContextTerms(meaning, nextWords, 1);
    return meaning;
  }
  


  public int getVectorLength()
  {
    return indexVectorLength;
  }
  


  public void setReadOnly(boolean readOnly)
  {
    this.readOnly = readOnly;
  }
  







  protected void addContextTerms(SparseDoubleVector meaning, Queue<String> words, int distance)
  {
    for (String term : words) {
      if (!term.equals(""))
      {


        if ((!readOnly) || (indexMap.containsKey(term)))
        {


          TernaryVector termVector = (TernaryVector)indexMap.get(term);
          if (termVector != null)
          {


            if (permFunc != null) {
              termVector = (TernaryVector)permFunc.permute(termVector, distance);
            }
            
            add(meaning, termVector);
            distance++;
          }
        }
      }
    }
  }
  
  private void add(SparseDoubleVector dest, TernaryVector src)
  {
    for (int p : src.positiveDimensions())
      dest.add(p, 1.0D);
    for (int n : src.negativeDimensions()) {
      dest.add(n, -1.0D);
    }
  }
}
