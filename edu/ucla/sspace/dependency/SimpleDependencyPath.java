package edu.ucla.sspace.dependency;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;





































public class SimpleDependencyPath
  implements DependencyPath
{
  final List<DependencyRelation> path;
  final List<DependencyTreeNode> nodes;
  
  public SimpleDependencyPath(List<DependencyRelation> path)
  {
    this(path, true);
  }
  




  public SimpleDependencyPath(List<DependencyRelation> path, boolean isHeadFirst)
  {
    if ((path == null) || (path.size() == 0))
      throw new IllegalArgumentException("Cannot provide empty path");
    this.path = new ArrayList(path);
    nodes = new ArrayList(path.size() + 1);
    DependencyTreeNode cur = isHeadFirst ? 
      ((DependencyRelation)path.get(0)).headNode() : ((DependencyRelation)path.get(0)).dependentNode();
    nodes.add(cur);
    for (DependencyRelation r : path) {
      DependencyTreeNode next = r.headNode();
      

      if (next.equals(cur))
        next = r.dependentNode();
      nodes.add(next);
      cur = next;
    }
  }
  


  public SimpleDependencyPath(DependencyPath path)
  {
    if ((path == null) || (path.length() == 0)) {
      throw new IllegalArgumentException("Cannot provide empty path");
    }
    int size = path.length();
    this.path = new ArrayList(size);
    nodes = new ArrayList(size + 1);
    DependencyTreeNode cur = path.first();
    nodes.add(cur);
    for (DependencyRelation r : path) {
      this.path.add(r);
      DependencyTreeNode next = r.headNode();
      

      if (next.equals(cur))
        next = r.dependentNode();
      nodes.add(next);
      next = cur;
    }
  }
  




  public SimpleDependencyPath(DependencyRelation relation, boolean startFromHead)
  {
    this();
    if (relation == null)
      throw new IllegalArgumentException("Cannot provide empty path");
    path.add(relation);
    if (startFromHead) {
      nodes.add(relation.headNode());
      nodes.add(relation.dependentNode());
    }
    else {
      nodes.add(relation.dependentNode());
      nodes.add(relation.headNode());
    }
  }
  


  public SimpleDependencyPath()
  {
    path = new ArrayList();
    nodes = new ArrayList();
  }
  


  public SimpleDependencyPath copy()
  {
    SimpleDependencyPath copy = new SimpleDependencyPath();
    path.addAll(path);
    nodes.addAll(nodes);
    return copy;
  }
  



  public SimpleDependencyPath extend(DependencyRelation relation)
  {
    SimpleDependencyPath copy = copy();
    

    DependencyTreeNode last = last();
    nodes.add(relation.headNode().equals(last) ? 
      relation.dependentNode() : relation.headNode());
    path.add(relation);
    return copy;
  }
  
  public boolean equals(Object o) {
    if ((o instanceof DependencyPath)) {
      DependencyPath p = (DependencyPath)o;
      if (p.length() != length())
        return false;
      DependencyTreeNode f = p.first();
      DependencyTreeNode n = first();
      if ((f != n) && ((f == null) || (!f.equals(n))))
        return false;
      Iterator<DependencyRelation> it1 = iterator();
      Iterator<DependencyRelation> it2 = p.iterator();
      while (it1.hasNext())
        if (!((DependencyRelation)it1.next()).equals(it2.next()))
          return false;
      return true;
    }
    return false;
  }
  


  public DependencyTreeNode first()
  {
    return nodes.isEmpty() ? null : (DependencyTreeNode)nodes.get(0);
  }
  


  public DependencyRelation firstRelation()
  {
    return path.isEmpty() ? null : (DependencyRelation)path.get(0);
  }
  


  public DependencyTreeNode getNode(int position)
  {
    if ((position < 0) || (position >= nodes.size()))
      throw new IndexOutOfBoundsException("Invalid node: " + position);
    return (DependencyTreeNode)nodes.get(position);
  }
  


  public String getRelation(int position)
  {
    if ((position < 0) || (position >= path.size()))
      throw new IndexOutOfBoundsException("Invalid relation: " + position);
    DependencyRelation r = (DependencyRelation)path.get(position);
    return r.relation();
  }
  


  public Iterator<DependencyRelation> iterator()
  {
    return path.iterator();
  }
  


  public DependencyTreeNode last()
  {
    return (DependencyTreeNode)nodes.get(nodes.size() - 1);
  }
  


  public DependencyRelation lastRelation()
  {
    return (DependencyRelation)path.get(path.size() - 1);
  }
  


  public int length()
  {
    return path.size();
  }
  


  public String toString()
  {
    int size = nodes.size();
    StringBuilder sb = new StringBuilder(8 * size);
    sb.append(((DependencyTreeNode)nodes.get(0)).word());
    for (int i = 1; i < size; i++)
    {

      sb.append(' ').append(((DependencyRelation)path.get(i - 1)).relation()).append(' ').append(((DependencyTreeNode)nodes.get(i)).word()); }
    return sb.toString();
  }
}
