package edu.ucla.sspace.wordsi;

import edu.ucla.sspace.basis.BasisMapping;
import edu.ucla.sspace.dependency.DependencyTreeNode;
import edu.ucla.sspace.vector.CompactSparseVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import java.util.ArrayDeque;
import java.util.Queue;






















































public abstract class AbstractOccurrenceDependencyContextGenerator
  implements DependencyContextGenerator
{
  private final BasisMapping<String, String> basis;
  private final int windowSize;
  
  public AbstractOccurrenceDependencyContextGenerator(BasisMapping<String, String> basis, int windowSize)
  {
    this.basis = basis;
    this.windowSize = windowSize;
  }
  



  public SparseDoubleVector generateContext(DependencyTreeNode[] tree, int focusIndex)
  {
    Queue<String> prevWords = new ArrayDeque();
    for (int i = Math.max(0, focusIndex - windowSize - 1); i < focusIndex; i++) {
      prevWords.add(getFeature(tree[i], i - focusIndex));
    }
    Queue<String> nextWords = new ArrayDeque();
    for (int i = focusIndex + 1; 
          i < Math.min(focusIndex + windowSize + 1, tree.length); i++) {
      nextWords.add(getFeature(tree[i], i - focusIndex));
    }
    SparseDoubleVector focusMeaning = new CompactSparseVector();
    addContextTerms(focusMeaning, prevWords, -1 * prevWords.size());
    addContextTerms(focusMeaning, nextWords, 1);
    return focusMeaning;
  }
  






  protected abstract String getFeature(DependencyTreeNode paramDependencyTreeNode, int paramInt);
  





  protected void addContextTerms(SparseDoubleVector meaning, Queue<String> words, int distance)
  {
    for (String term : words) {
      if (!term.equals(""))
      {
        int dimension = basis.getDimension(term);
        if (dimension != -1)
        {



          meaning.set(dimension, 1.0D);
          distance++;
        }
      }
    }
  }
  

  public int getVectorLength()
  {
    return basis.numDimensions();
  }
  


  public void setReadOnly(boolean readOnly)
  {
    basis.setReadOnly(readOnly);
  }
}
