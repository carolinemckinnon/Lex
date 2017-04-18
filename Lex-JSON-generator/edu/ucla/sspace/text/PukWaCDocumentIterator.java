package edu.ucla.sspace.text;

import edu.ucla.sspace.dependency.DependencyExtractor;
import edu.ucla.sspace.dependency.WaCKyDependencyExtractor;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;




































public class PukWaCDocumentIterator
  implements Iterator<LabeledParsedDocument>
{
  private static final DependencyExtractor extractor = new WaCKyDependencyExtractor();
  





  private final BufferedReader documentsReader;
  





  private String currentSource;
  




  private LabeledParsedDocument nextDoc;
  





  public PukWaCDocumentIterator(String documentsFile)
  {
    try
    {
      documentsReader = new BufferedReader(new FileReader(documentsFile));
      advance();
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  
  private void advance() throws IOException {
    nextDoc = null;
    StringBuilder sb = new StringBuilder();
    String line = null;
    
    while ((line = documentsReader.readLine()) != null)
    {

      if (line.contains("<text")) {
        int start = line.indexOf("\"");
        if (start >= 0)
        {
          int end = line.indexOf("\"", start + 1);
          if (end >= 0)
          {
            currentSource = line.substring(start + 1, end);
          }
        }
      }
      else if (line.equals("<s>")) {
        while (((line = documentsReader.readLine()) != null) && 
          (!line.equals("</s>")))
        {



          if (line.contains("<text")) {
            int start = line.indexOf("\"");
            int end = line.indexOf("\"", start + 1);
            if (end < 0)
            {


              sb.setLength(0);
              break;
            }
            currentSource = line.substring(start + 1, end);
          }
          else if (!line.contains("</text>")) {
            sb.append(line).append("\n");
          }
        }
        nextDoc = new LabeledParsedStringDocument(
          currentSource, extractor, sb.toString());
        break;
      }
    }
  }
  


  public boolean hasNext()
  {
    return nextDoc != null;
  }
  



  public LabeledParsedDocument next()
  {
    if (nextDoc == null)
      throw new NoSuchElementException("No further documents");
    LabeledParsedDocument next = nextDoc;
    for (;;) {
      try {
        advance();

      }
      catch (IOException ioe)
      {
        throw new IOError(ioe);
      }
      catch (Exception localException) {}
    }
    





    return next;
  }
  


  public void remove()
  {
    throw new UnsupportedOperationException(
      "removing documents is not supported");
  }
}
