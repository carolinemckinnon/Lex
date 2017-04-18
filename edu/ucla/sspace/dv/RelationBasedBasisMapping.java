package edu.ucla.sspace.dv;

import edu.ucla.sspace.basis.AbstractBasisMapping;
import edu.ucla.sspace.dependency.DependencyPath;
import edu.ucla.sspace.dependency.DependencyTreeNode;




































public class RelationBasedBasisMapping
  extends AbstractBasisMapping<DependencyPath, String>
  implements DependencyPathBasisMapping
{
  private static final long serialVersionUID = 1L;
  
  public RelationBasedBasisMapping() {}
  
  public int getDimension(DependencyPath path)
  {
    String endToken = path.last().word();
    


    String relation = path.getRelation(path.length() - 1);
    return getDimensionInternal(endToken + "+" + relation);
  }
}
