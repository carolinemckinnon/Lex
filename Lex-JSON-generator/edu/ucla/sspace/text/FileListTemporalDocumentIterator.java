package edu.ucla.sspace.text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;














































public class FileListTemporalDocumentIterator
  implements Iterator<TemporalDocument>
{
  private final Queue<NameAndTime> filesToProcess;
  
  public FileListTemporalDocumentIterator(String fileListName)
    throws IOException
  {
    filesToProcess = new ConcurrentLinkedQueue();
    

    BufferedReader br = new BufferedReader(new FileReader(fileListName));
    for (String line = null; (line = br.readLine()) != null;)
      if ((line.length() != 0) && (!line.startsWith("#")))
      {




        String[] s = line.split("\\s+");
        String fileName = s[0];
        long timeStamp = s.length > 1 ? Long.parseLong(s[1]) : -1L;
        filesToProcess.offer(new NameAndTime(fileName, timeStamp));
      }
    br.close();
  }
  


  public boolean hasNext()
  {
    return !filesToProcess.isEmpty();
  }
  



  public TemporalDocument next()
  {
    NameAndTime n = (NameAndTime)filesToProcess.poll();
    if (n == null)
      return null;
    try {
      return n.hasTimeStamp() ? 
        new TemporalFileDocument(fileName, timeStamp) : 
        new TemporalFileDocument(fileName);
    } catch (IOException ioe) {}
    return null;
  }
  



  public void remove()
  {
    throw new UnsupportedOperationException(
      "removing documents is not supported");
  }
  

  private static class NameAndTime
  {
    public final String fileName;
    
    public final long timeStamp;
    
    public NameAndTime(String fileName, long timeStamp)
    {
      this.fileName = fileName;
      this.timeStamp = timeStamp;
    }
    
    public boolean hasTimeStamp() {
      return timeStamp >= 0L;
    }
  }
}
