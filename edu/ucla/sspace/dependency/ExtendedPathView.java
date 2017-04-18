package edu.ucla.sspace.dependency;

import edu.ucla.sspace.util.CombinedIterator;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;











































class ExtendedPathView
  implements DependencyPath
{
  private final DependencyPath original;
  private final DependencyRelation extension;
  private final int length;
  
  public ExtendedPathView(DependencyPath original, DependencyRelation extension)
  {
    this.original = original;
    this.extension = extension;
    



    length = (original.length() + 1);
  }
  


  public DependencyTreeNode first()
  {
    return original.first();
  }
  


  public DependencyRelation firstRelation()
  {
    return original.firstRelation();
  }
  







  private DependencyTreeNode getNextNode(DependencyRelation prev, DependencyRelation cur)
  {
    return (prev.headNode() == cur.headNode()) || 
      (prev.dependentNode() == cur.headNode()) ? 
      cur.dependentNode() : 
      cur.headNode();
  }
  


  public DependencyTreeNode getNode(int position)
  {
    if (position < length - 1) {
      return original.getNode(position);
    }
    if (position > length) {
      throw new IllegalArgumentException("invalid node: " + position);
    }
    return last();
  }
  


  public String getRelation(int position)
  {
    if (position < length - 1) {
      return original.getRelation(position);
    }
    if (position >= length) {
      throw new IllegalArgumentException("invalid relation: " + position);
    }
    return extension.relation();
  }
  









  public Iterator<DependencyRelation> iterator()
  {
    Iterator<DependencyRelation> it = 
      new CombinedIterator(new Iterator[] { original.iterator(), 
      Collections.singleton(extension).iterator() });
    return it;
  }
  


  public DependencyTreeNode last()
  {
    return getNextNode(original.lastRelation(), extension);
  }
  


  public DependencyRelation lastRelation()
  {
    return extension;
  }
  


  public int length()
  {
    return length;
  }
  


  public String toString()
  {
    StringBuilder sb = new StringBuilder(8 * length);
    sb.append('[');
    for (int i = 0; i < length; i++) {
      sb.append(getNode(i).word());
      if (i < length - 1)
        sb.append(' ').append(getRelation(i)).append(' ');
    }
    return ']';
  }
}
