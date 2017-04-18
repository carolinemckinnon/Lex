package edu.ucla.sspace.text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;















































public class OneLinePerTemporalDocumentIterator
  implements Iterator<TemporalDocument>
{
  private final BufferedReader documentsReader;
  private String nextLine;
  
  public OneLinePerTemporalDocumentIterator(String documentsFile)
    throws IOException
  {
    documentsReader = new BufferedReader(new FileReader(documentsFile));
    nextLine = documentsReader.readLine();
  }
  


  public synchronized boolean hasNext()
  {
    return nextLine != null;
  }
  



  public synchronized TemporalDocument next()
  {
    int firstSpace = nextLine.indexOf(" ");
    String timeStr = nextLine.substring(0, firstSpace);
    String doc = nextLine.substring(firstSpace);
    TemporalDocument next = 
      new TemporalStringDocument(doc, Long.parseLong(timeStr));
    try {
      nextLine = documentsReader.readLine();
    } catch (Throwable t) {
      t.printStackTrace();
      nextLine = null;
    }
    return next;
  }
  


  public void remove()
  {
    throw new UnsupportedOperationException(
      "removing documents is not supported");
  }
}
