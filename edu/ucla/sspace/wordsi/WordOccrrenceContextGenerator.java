package edu.ucla.sspace.wordsi;

import edu.ucla.sspace.basis.BasisMapping;
import edu.ucla.sspace.hal.WeightingFunction;
import edu.ucla.sspace.vector.CompactSparseVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import java.util.Queue;
























































public class WordOccrrenceContextGenerator
  implements ContextGenerator
{
  private final BasisMapping<String, String> basis;
  private final WeightingFunction weighting;
  private final int windowSize;
  
  public WordOccrrenceContextGenerator(BasisMapping<String, String> basis, WeightingFunction weighting, int windowSize)
  {
    this.basis = basis;
    this.weighting = weighting;
    this.windowSize = windowSize;
  }
  



  public SparseDoubleVector generateContext(Queue<String> prevWords, Queue<String> nextWords)
  {
    SparseDoubleVector meaning = new CompactSparseVector();
    addContextTerms(meaning, prevWords, -1 * prevWords.size());
    addContextTerms(meaning, nextWords, 1);
    return meaning;
  }
  


  public int getVectorLength()
  {
    return basis.numDimensions();
  }
  


  public void setReadOnly(boolean readOnly)
  {
    basis.setReadOnly(readOnly);
  }
  







  protected void addContextTerms(SparseDoubleVector meaning, Queue<String> words, int distance)
  {
    for (String term : words) {
      if (!term.equals(""))
      {
        int dimension = basis.getDimension(term);
        if (dimension != -1)
        {



          meaning.set(dimension, weighting.weight(distance, windowSize));
          distance++;
        }
      }
    }
  }
}
