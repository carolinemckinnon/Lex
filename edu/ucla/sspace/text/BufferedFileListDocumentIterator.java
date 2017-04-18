package edu.ucla.sspace.text;

import edu.ucla.sspace.util.LineReader;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;






















































public class BufferedFileListDocumentIterator
  implements Iterator<Document>
{
  private static final int DEFAULT_BUFFER_SIZE = 100;
  private final Queue<String> filesToProcess;
  private final BlockingQueue<Document> documentsToReturn;
  private final AtomicInteger remaining;
  private volatile RuntimeException bufferError;
  
  public BufferedFileListDocumentIterator(String fileListName)
    throws IOException
  {
    this(fileListName, 100);
  }
  








  public BufferedFileListDocumentIterator(String fileListName, int bufferSize)
    throws IOException
  {
    filesToProcess = new ArrayDeque();
    documentsToReturn = new ArrayBlockingQueue(bufferSize);
    

    for (String line : new LineReader(new File(fileListName))) {
      filesToProcess.offer(line.trim());
    }
    remaining = new AtomicInteger(filesToProcess.size());
    bufferError = null;
    
    Thread bufferingThread = new Thread(new Bufferer(), 
      "BufferingThread for " + fileListName);
    bufferingThread.setDaemon(true);
    bufferingThread.start();
  }
  


  public boolean hasNext()
  {
    return remaining.get() > 0;
  }
  


  public Document next()
  {
    if (!hasNext())
      throw new NoSuchElementException("No further documents");
    if (bufferError != null)
      throw bufferError;
    try {
      return (Document)documentsToReturn.take();
    } catch (InterruptedException ie) {
      throw new IOError(ie);
    }
  }
  


  public void remove()
  {
    throw new UnsupportedOperationException(
      "removing documents is not supported");
  }
  
  class Bufferer implements Runnable { Bufferer() {}
    
    public void run() { while (!filesToProcess.isEmpty()) {
        try {
          String file = (String)filesToProcess.poll();
          documentsToReturn.put(new FileDocument(file, true));
          remaining.decrementAndGet();
        } catch (Exception e) {
          bufferError = new RuntimeException(e);
          throw new IOError(e);
        }
      }
    }
  }
}
