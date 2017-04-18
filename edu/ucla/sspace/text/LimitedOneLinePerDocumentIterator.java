package edu.ucla.sspace.text;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

































































public class LimitedOneLinePerDocumentIterator
  implements Iterator<Document>
{
  private final Iterator<Document> iter;
  private final AtomicInteger docCount;
  private boolean isReset;
  private final int docLimit;
  private final boolean useMultipleResets;
  
  public LimitedOneLinePerDocumentIterator(Iterator<Document> iter, int docLimit, boolean useMultipleResets)
    throws IOException
  {
    this.iter = iter;
    this.docLimit = docLimit;
    this.useMultipleResets = useMultipleResets;
    docCount = new AtomicInteger();
    isReset = false;
  }
  


  public synchronized boolean hasNext()
  {
    return (docCount.get() < docLimit) && (iter.hasNext());
  }
  


  public synchronized Document next()
  {
    if ((!isReset) || (useMultipleResets))
      docCount.incrementAndGet();
    return (Document)iter.next();
  }
  


  public synchronized void reset()
  {
    docCount.set(0);
    isReset = true;
  }
  


  public void remove()
  {
    throw new UnsupportedOperationException(
      "removing documents is not supported");
  }
}
