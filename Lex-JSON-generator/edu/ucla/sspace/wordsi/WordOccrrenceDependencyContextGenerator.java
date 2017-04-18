package edu.ucla.sspace.wordsi;

import edu.ucla.sspace.dependency.DependencyPath;
import edu.ucla.sspace.dependency.DependencyPathAcceptor;
import edu.ucla.sspace.dependency.DependencyPathWeight;
import edu.ucla.sspace.dependency.DependencyTreeNode;
import edu.ucla.sspace.dependency.FilteredDependencyIterator;
import edu.ucla.sspace.dv.DependencyPathBasisMapping;
import edu.ucla.sspace.vector.CompactSparseVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import java.util.Iterator;































































public class WordOccrrenceDependencyContextGenerator
  implements DependencyContextGenerator
{
  private final DependencyPathBasisMapping basisMapping;
  private final DependencyPathWeight weighter;
  private final int pathLength;
  private final DependencyPathAcceptor acceptor;
  
  public WordOccrrenceDependencyContextGenerator(DependencyPathBasisMapping basisMapping, DependencyPathWeight weighter, DependencyPathAcceptor acceptor, int pathLength)
  {
    this.basisMapping = basisMapping;
    this.weighter = weighter;
    this.acceptor = acceptor;
    this.pathLength = pathLength;
  }
  



  public SparseDoubleVector generateContext(DependencyTreeNode[] tree, int focusIndex)
  {
    DependencyTreeNode focusNode = tree[focusIndex];
    
    SparseDoubleVector focusMeaning = new CompactSparseVector();
    
    Iterator<DependencyPath> paths = new FilteredDependencyIterator(
      focusNode, acceptor, pathLength);
    




    while (paths.hasNext()) {
      DependencyPath path = (DependencyPath)paths.next();
      


      int dimension = basisMapping.getDimension(path);
      if (dimension >= 0)
      {

        double weight = weighter.scorePath(path);
        focusMeaning.add(dimension, weight);
      } }
    return focusMeaning;
  }
  


  public int getVectorLength()
  {
    return basisMapping.numDimensions();
  }
  


  public void setReadOnly(boolean readOnly)
  {
    basisMapping.setReadOnly(readOnly);
  }
}
