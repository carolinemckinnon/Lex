package edu.ucla.sspace.wordsi;

import edu.ucla.sspace.dependency.DependencyPath;
import edu.ucla.sspace.dependency.DependencyPathAcceptor;
import edu.ucla.sspace.dependency.DependencyPermutationFunction;
import edu.ucla.sspace.dependency.DependencyTreeNode;
import edu.ucla.sspace.dependency.FilteredDependencyIterator;
import edu.ucla.sspace.vector.CompactSparseVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.TernaryVector;
import java.util.Iterator;
import java.util.Map;












































































public class RandomIndexingDependencyContextGenerator
  implements DependencyContextGenerator
{
  private final DependencyPermutationFunction<TernaryVector> permFunc;
  private final Map<String, TernaryVector> indexMap;
  private final int indexVectorLength;
  private final int pathLength;
  private final DependencyPathAcceptor acceptor;
  private boolean readOnly;
  
  public RandomIndexingDependencyContextGenerator(DependencyPermutationFunction<TernaryVector> permFunc, DependencyPathAcceptor acceptor, Map<String, TernaryVector> indexMap, int indexVectorLength, int pathLength)
  {
    this.permFunc = permFunc;
    this.acceptor = acceptor;
    this.indexMap = indexMap;
    this.indexVectorLength = indexVectorLength;
    this.pathLength = pathLength;
  }
  



  public SparseDoubleVector generateContext(DependencyTreeNode[] tree, int focusIndex)
  {
    DependencyTreeNode focusNode = tree[focusIndex];
    
    SparseDoubleVector meaning = new CompactSparseVector(indexVectorLength);
    
    Iterator<DependencyPath> paths = new FilteredDependencyIterator(
      focusNode, acceptor, pathLength);
    
    while (paths.hasNext()) {
      DependencyPath path = (DependencyPath)paths.next();
      if ((!readOnly) || (indexMap.containsKey(path.last().word())))
      {

        TernaryVector termVector = (TernaryVector)indexMap.get(path.last().word());
        if (permFunc != null)
          termVector = (TernaryVector)permFunc.permute(termVector, path);
        add(meaning, termVector);
      } }
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
  


  private void add(SparseDoubleVector dest, TernaryVector src)
  {
    for (int p : src.positiveDimensions())
      dest.add(p, 1.0D);
    for (int n : src.negativeDimensions()) {
      dest.add(n, -1.0D);
    }
  }
}
