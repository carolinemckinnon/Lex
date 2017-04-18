package edu.ucla.sspace.text.corpora;

import edu.ucla.sspace.text.CorpusReader;
import edu.ucla.sspace.text.Document;
import edu.ucla.sspace.text.StringDocument;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;



































public class PukWacDependencyCorpusReader
  implements CorpusReader<Document>
{
  public PukWacDependencyCorpusReader() {}
  
  public Iterator<Document> read(File file)
  {
    try
    {
      return read(new FileReader(file));
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  


  public Iterator<Document> read(Reader baseReader)
  {
    return new UkWacIterator(new BufferedReader(baseReader));
  }
  


  public class UkWacIterator
    implements Iterator<Document>
  {
    protected BufferedReader reader;
    

    private String next;
    

    public UkWacIterator(BufferedReader reader)
    {
      this.reader = reader;
      next = advance();
    }
    


    public boolean hasNext()
    {
      return next != null;
    }
    


    public Document next()
    {
      Document doc = new StringDocument(next);
      next = advance();
      return doc;
    }
    




    public void remove()
    {
      throw new UnsupportedOperationException(
        "Cannot remove documents from a CorpusReader");
    }
    




    protected String advance()
    {
      StringBuilder sb = new StringBuilder();
      try {
        for (String line = null; (line = reader.readLine()) != null;)
        {

          if ((!line.startsWith("<text")) && 
            (!line.startsWith("</text>")) && 
            (!line.startsWith("<s>")) && 
            (line.length() != 0))
          {


            if (line.startsWith("</s>")) {
              break;
            }
            


            sb.append(line).append("\n");
          } }
      } catch (IOException ioe) {
        throw new IOError(ioe);
      }
      


      if (sb.length() == 0) {
        return null;
      }
      return sb.toString();
    }
  }
}
