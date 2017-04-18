package edu.ucla.sspace.dependency;

import java.util.Iterator;
import java.util.NoSuchElementException;





























































public class FilteredDependencyIterator
  implements Iterator<DependencyPath>
{
  private final DependencyPathAcceptor acceptor;
  private final BreadthFirstPathIterator iterator;
  private DependencyPath next;
  
  public FilteredDependencyIterator(DependencyTreeNode startNode, DependencyPathAcceptor acceptor)
  {
    this(startNode, acceptor, Integer.MAX_VALUE);
  }
  













  public FilteredDependencyIterator(DependencyTreeNode startNode, DependencyPathAcceptor acceptor, int maxPathLength)
  {
    iterator = new BreadthFirstPathIterator(startNode, maxPathLength);
    this.acceptor = acceptor;
    advance();
  }
  



  private void advance()
  {
    next = null;
    


    while ((iterator.hasNext()) && (next == null)) {
      DependencyPath p = iterator.next();
      if (acceptor.accepts(p)) {
        next = p;
      }
    }
  }
  


  public boolean hasNext()
  {
    return next != null;
  }
  




  public DependencyPath next()
  {
    if (next == null)
      throw new NoSuchElementException("No further paths to return");
    DependencyPath p = next;
    advance();
    return p;
  }
  


  public void remove()
  {
    throw new UnsupportedOperationException("Removal is not supported");
  }
}
