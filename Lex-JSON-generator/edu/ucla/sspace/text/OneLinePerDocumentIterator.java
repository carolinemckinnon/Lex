package edu.ucla.sspace.text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;














































public class OneLinePerDocumentIterator
  implements Iterator<Document>
{
  private final BufferedReader documentsReader;
  private String nextLine;
  
  public OneLinePerDocumentIterator(String documentsFile)
    throws IOException
  {
    documentsReader = new BufferedReader(new FileReader(documentsFile));
    nextLine = documentsReader.readLine();
  }
  


  public synchronized boolean hasNext()
  {
    return nextLine != null;
  }
  


  public synchronized Document next()
  {
    Document next = new StringDocument(nextLine);
    try {
      nextLine = documentsReader.readLine();
      

      if (nextLine == null) {
        documentsReader.close();
      }
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
