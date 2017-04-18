package edu.ucla.sspace.text.corpora;

import edu.ucla.sspace.text.DirectoryCorpusReader;
import edu.ucla.sspace.text.DirectoryCorpusReader.BaseFileIterator;
import edu.ucla.sspace.text.Document;
import edu.ucla.sspace.text.DocumentPreprocessor;
import edu.ucla.sspace.text.StringDocument;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.util.Iterator;








































public class UsenetCorpusReader
  extends DirectoryCorpusReader<Document>
{
  private static final String END_OF_DOCUMENT = "---END.OF.DOCUMENT---";
  
  public UsenetCorpusReader() {}
  
  public UsenetCorpusReader(DocumentPreprocessor preprocessor)
  {
    super(preprocessor);
  }
  
  protected Iterator<Document> corpusIterator(Iterator<File> files) {
    return new UseNetIterator(files);
  }
  


  public class UseNetIterator
    extends DirectoryCorpusReader<Document>.BaseFileIterator
  {
    private BufferedReader usenetReader;
    

    private final boolean useTimestamps;
    


    public UseNetIterator()
    {
      super(files);
      useTimestamps = false;
    }
    


    protected void setupCurrentDoc(File currentDocName)
    {
      try
      {
        usenetReader = 
          new BufferedReader(new FileReader(currentDocName));
      } catch (IOException ioe) {
        throw new IOError(ioe);
      }
    }
    



    protected Document advanceInDoc()
    {
      String line = null;
      StringBuilder content = new StringBuilder();
      



      try
      {
        while ((line = usenetReader.readLine()) != null) {
          if (line.contains("---END.OF.DOCUMENT---")) {
            return new StringDocument(cleanDoc(content.toString()));
          }
          int lineStart = 0;
          

          char c = line.charAt(lineStart);
          while ((lineStart < line.length()) && (
            (c == '>') || (c == ' '))) {
            c = line.charAt(++lineStart);
          }
          



          content.append(line.substring(lineStart)).append(" ");
        }
      }
      catch (IOException ioe) {
        throw new IOError(ioe);
      }
      
      return null;
    }
  }
}
