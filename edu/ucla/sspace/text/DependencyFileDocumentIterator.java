package edu.ucla.sspace.text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.util.Iterator;





















































public class DependencyFileDocumentIterator
  implements Iterator<Document>
{
  private final BufferedReader documentsReader;
  private final boolean ignoreHeader;
  private String nextLine;
  
  public DependencyFileDocumentIterator(String documentsFile)
    throws IOException
  {
    this(documentsFile, false);
  }
  












  public DependencyFileDocumentIterator(String documentsFile, boolean ignoreHeader)
    throws IOException
  {
    this.ignoreHeader = ignoreHeader;
    documentsReader = new BufferedReader(new FileReader(documentsFile));
    nextLine = advance();
  }
  


  public boolean hasNext()
  {
    return nextLine != null;
  }
  
  private String advance() throws IOException {
    StringBuilder sb = new StringBuilder();
    String line = null;
    

    while (((line = documentsReader.readLine()) != null) && 
      (line.length() == 0)) {}
    


    if (line == null) {
      return null;
    }
    
    if (!ignoreHeader) {
      sb.append(line).append("\n");
    }
    

    while (((line = documentsReader.readLine()) != null) && 
      (line.length() != 0))
      sb.append(line).append("\n");
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
