package edu.ucla.sspace.dv;

import edu.ucla.sspace.basis.AbstractBasisMapping;
import edu.ucla.sspace.dependency.DependencyPath;
import edu.ucla.sspace.dependency.DependencyTreeNode;




































public class WordBasedBasisMapping
  extends AbstractBasisMapping<DependencyPath, String>
  implements DependencyPathBasisMapping
{
  private static final long serialVersionUID = 1L;
  
  public WordBasedBasisMapping() {}
  
  public int getDimension(DependencyPath path)
  {
    return getDimensionInternal(path.last().word());
  }
}
