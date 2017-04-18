package edu.ucla.sspace.text.corpora;

import edu.ucla.sspace.text.DirectoryCorpusReader;
import edu.ucla.sspace.text.DirectoryCorpusReader.BaseFileIterator;
import edu.ucla.sspace.text.DocumentPreprocessor;
import edu.ucla.sspace.text.TemporalDocument;
import edu.ucla.sspace.text.TemporalStringDocument;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;






































public class TemporalUsenetCorpusReader
  extends DirectoryCorpusReader<TemporalDocument>
{
  private static final String END_OF_DOCUMENT = "---END.OF.DOCUMENT---";
  
  public TemporalUsenetCorpusReader() {}
  
  public TemporalUsenetCorpusReader(DocumentPreprocessor preprocessor)
  {
    super(preprocessor);
  }
  
  protected Iterator<TemporalDocument> corpusIterator(Iterator<File> files) {
    return new UseNetIterator(files);
  }
  


  public class UseNetIterator
    extends DirectoryCorpusReader<TemporalDocument>.BaseFileIterator
  {
    private BufferedReader usenetReader;
    

    private long curDocTimestamp;
    

    public UseNetIterator()
    {
      super(files);
    }
    


    protected void setupCurrentDoc(File currentDocName)
    {
      try
      {
        usenetReader = 
          new BufferedReader(new FileReader(currentDocName));
        




        String[] parsedName = currentDocName.getName().split("\\.");
        String date = parsedName[(parsedName.length - 2)];
        Calendar calendar = new GregorianCalendar(
          Integer.parseInt(date.substring(0, 4)), 
          Integer.parseInt(date.substring(4, 6)), 
          Integer.parseInt(date.substring(6, 8)));
        curDocTimestamp = calendar.getTimeInMillis();
      } catch (IOException ioe) {
        throw new IOError(ioe);
      }
    }
    



    protected TemporalDocument advanceInDoc()
    {
      String line = null;
      StringBuilder content = new StringBuilder();
      



      try
      {
        while ((line = usenetReader.readLine()) != null) {
          if (line.contains("---END.OF.DOCUMENT---")) {
            return new TemporalStringDocument(
              cleanDoc(content.toString()), curDocTimestamp);
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
