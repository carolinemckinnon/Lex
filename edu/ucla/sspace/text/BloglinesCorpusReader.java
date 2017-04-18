package edu.ucla.sspace.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.Iterator;




































public class BloglinesCorpusReader
  extends DirectoryCorpusReader<Document>
{
  private final boolean useTimestamps;
  
  public BloglinesCorpusReader()
  {
    this(false);
  }
  



  public BloglinesCorpusReader(boolean includeTimeStamps)
  {
    useTimestamps = includeTimeStamps;
  }
  
  protected Iterator<Document> corpusIterator(Iterator<File> fileIter) {
    return new InnerIterator(fileIter);
  }
  

  public class InnerIterator
    extends DirectoryCorpusReader<Document>.BaseFileIterator
  {
    private BufferedReader bloglinesReader;
    
    public InnerIterator()
    {
      super(files);
    }
    


    protected void setupCurrentDoc(File currentDocName)
    {
      try
      {
        bloglinesReader = 
          new BufferedReader(new FileReader(currentDocName));
      } catch (IOException ioe) {
        throw new IOError(ioe);
      }
    }
    



    protected Document advanceInDoc()
    {
      String line = null;
      StringBuilder content = null;
      boolean inContent = false;
      
      try
      {
        while ((line = bloglinesReader.readLine()) != null)
        {

          if (line.contains("<content>"))
          {


            int startIndex = line.indexOf(">") + 1;
            int endIndex = line.lastIndexOf("<");
            

            if (endIndex > startIndex) {
              String extractedContent = 
                line.substring(startIndex, endIndex);
              if (!useTimestamps) {
                return new StringDocument(cleanDoc(extractedContent));
              }
              content = new StringBuilder(extractedContent);

            }
            else
            {
              content = new StringBuilder(line.substring(startIndex));
              inContent = true;
            }
          } else if (line.contains("</content>")) {
            inContent = false;
            

            int endIndex = line.lastIndexOf("<");
            content.append(line.substring(0, endIndex));
            


            if (useTimestamps) {
              System.out.println("TIMESTAMPS");
            }
            else
              return new StringDocument(cleanDoc(content.toString()));
          } else { if ((line.contains("<updated>")) && (content != null))
            {


              int startIndex = line.indexOf(">") + 1;
              int endIndex = line.lastIndexOf("<");
              String date = line.substring(startIndex, endIndex);
              long dateTime = date.equals("") ? 
                0L : 
                Timestamp.valueOf(date).getTime();
              return new StringDocument(String.format("%d %s", new Object[] {
                Long.valueOf(dateTime), 
                cleanDoc(content.toString()) })); }
            if ((inContent) && (content != null))
            {

              content.append(line); }
          }
        }
      } catch (IOException ioe) {
        throw new IOError(ioe);
      }
      
      return null;
    }
  }
}
