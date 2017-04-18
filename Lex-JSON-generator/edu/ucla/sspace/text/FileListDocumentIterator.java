package edu.ucla.sspace.text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;










































public class FileListDocumentIterator
  implements Iterator<Document>
{
  private final Queue<String> filesToProcess;
  
  public FileListDocumentIterator(String fileListName)
    throws IOException
  {
    filesToProcess = new ConcurrentLinkedQueue();
    

    BufferedReader br = new BufferedReader(new FileReader(fileListName));
    for (String line = null; (line = br.readLine()) != null;) {
      filesToProcess.offer(line.trim());
    }
    br.close();
  }
  


  public boolean hasNext()
  {
    return !filesToProcess.isEmpty();
  }
  


  public Document next()
  {
    String fileName = (String)filesToProcess.poll();
    if (fileName == null)
      return null;
    try {
      return new FileDocument(fileName);
    } catch (IOException ioe) {}
    return null;
  }
  



  public void remove()
  {
    throw new UnsupportedOperationException(
      "removing documents is not supported");
  }
}
