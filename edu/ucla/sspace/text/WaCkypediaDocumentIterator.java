package edu.ucla.sspace.text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.util.Iterator;
















































public class WaCkypediaDocumentIterator
  implements Iterator<Document>
{
  private final BufferedReader documentsReader;
  private String nextLine;
  
  public WaCkypediaDocumentIterator(String documentsFile)
  {
    try
    {
      documentsReader = new BufferedReader(new FileReader(documentsFile));
      nextLine = advance();
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  


  public boolean hasNext()
  {
    return nextLine != null;
  }
  
  private String advance() throws IOException {
    StringBuilder sb = new StringBuilder();
    String line = null;
    




    while (((line = documentsReader.readLine()) != null) && 
      (!line.equals("<s>"))) {}
    


    if (line == null) {
      return null;
    }
    

    while (((line = documentsReader.readLine()) != null) && 
      (!line.equals("</s>"))) {
      sb.append(line).append("\n");
    }
    return sb.toString();
  }
  


  public synchronized Document next()
  {
    Document next = new StringDocument(nextLine);
    try {
      nextLine = advance();
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
    return next;
  }
  


  public void remove()
  {
    throw new UnsupportedOperationException(
      "removing documents is not supported");
  }
}
