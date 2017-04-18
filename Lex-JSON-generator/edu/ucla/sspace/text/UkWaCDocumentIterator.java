package edu.ucla.sspace.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.util.Iterator;












































public class UkWaCDocumentIterator
  implements Iterator<LabeledDocument>
{
  private final BufferedReader lineReader;
  private LabeledDocument nextDoc;
  
  public UkWaCDocumentIterator(File documentsFile)
    throws IOException
  {
    lineReader = new BufferedReader(new FileReader(documentsFile));
    nextDoc = null;
    advance();
  }
  







  public UkWaCDocumentIterator(String documentsFile)
    throws IOException
  {
    this(new File(documentsFile));
  }
  


  public boolean hasNext()
  {
    return nextDoc != null;
  }
  
  private void advance() throws IOException {
    String header = lineReader.readLine();
    if (header == null) {
      lineReader.close();
    }
    else {
      String doc = lineReader.readLine();
      assert (doc != null);
      nextDoc = new LabeledStringDocument(header, doc);
    }
  }
  


  public LabeledDocument next()
  {
    LabeledDocument next = nextDoc;
    try {
      advance();
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
