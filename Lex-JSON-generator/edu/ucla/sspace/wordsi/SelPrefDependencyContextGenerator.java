package edu.ucla.sspace.wordsi;

import edu.ucla.sspace.dependency.DependencyPath;
import edu.ucla.sspace.dependency.DependencyPathAcceptor;
import edu.ucla.sspace.dependency.DependencyRelation;
import edu.ucla.sspace.dependency.DependencyTreeNode;
import edu.ucla.sspace.dependency.FilteredDependencyIterator;
import edu.ucla.sspace.dependency.UniversalPathAcceptor;
import edu.ucla.sspace.svs.StructuredVectorSpace;
import edu.ucla.sspace.vector.CompactSparseVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.VectorMath;
import java.util.Iterator;




































public class SelPrefDependencyContextGenerator
  implements DependencyContextGenerator
{
  public static final String EMPTY_STRING = "";
  private final StructuredVectorSpace svs;
  private final DependencyPathAcceptor acceptor;
  
  public SelPrefDependencyContextGenerator(StructuredVectorSpace svs)
  {
    this.svs = svs;
    acceptor = new UniversalPathAcceptor();
  }
  



  public SparseDoubleVector generateContext(DependencyTreeNode[] tree, int focusIndex)
  {
    Iterator<DependencyPath> paths = new FilteredDependencyIterator(
      tree[focusIndex], acceptor, 1);
    



    if (!paths.hasNext())
      return new CompactSparseVector();
    SparseDoubleVector focusMeaning = contextualize((DependencyPath)paths.next());
    


    if (!paths.hasNext())
      return focusMeaning;
    SparseDoubleVector secondMeaning = contextualize((DependencyPath)paths.next());
    



    return VectorMath.multiplyUnmodified(focusMeaning, secondMeaning);
  }
  
  private SparseDoubleVector contextualize(DependencyPath path) {
    DependencyRelation rel = (DependencyRelation)path.iterator().next();
    String relation = rel.relation();
    String focusTerm = path.first().word();
    String otherTerm = path.last().word();
    

    if (otherTerm.equals(""))
      return null;
    boolean isFocusHead = !rel.headNode().word().equals(focusTerm);
    return svs.contextualize(focusTerm, relation, otherTerm, isFocusHead);
  }
  



  public void setReadOnly(boolean readOnly) {}
  



  public int getVectorLength()
  {
    return svs.getVectorLength();
  }
}
