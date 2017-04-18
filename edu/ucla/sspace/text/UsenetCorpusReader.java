package edu.ucla.sspace.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;














































public class UsenetCorpusReader
  extends DirectoryCorpusReader<Document>
{
  private static final String END_OF_DOCUMENT = "---END.OF.DOCUMENT---";
  private final boolean useTimestamps;
  
  public UsenetCorpusReader()
  {
    this(false);
  }
  




  public UsenetCorpusReader(boolean includeTimestamps)
  {
    useTimestamps = includeTimestamps;
  }
  
  protected Iterator<Document> corpusIterator(Iterator<File> fileIter) {
    return new InnerIterator(fileIter);
  }
  


  public class InnerIterator
    extends DirectoryCorpusReader<Document>.BaseFileIterator
  {
    private BufferedReader usenetReader;
    

    private long curDocTimestamp;
    

    public InnerIterator()
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
    



    protected Document advanceInDoc()
    {
      String line = null;
      StringBuilder content = new StringBuilder();
      

      if (useTimestamps) {
        content.append(curDocTimestamp).append(" ");
      }
      

      try
      {
        while ((line = usenetReader.readLine()) != null) {
          if (line.contains("---END.OF.DOCUMENT---")) {
            return new StringDocument(cleanDoc(content.toString()));
          }
          int lineStart = 0;
          

          char c = line.charAt(lineStart);
          while ((lineStart < line.length()) && ((c == '>') || (c == ' '))) {
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
