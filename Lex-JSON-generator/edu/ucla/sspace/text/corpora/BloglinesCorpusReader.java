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
import java.sql.Timestamp;
import java.util.Iterator;






































public class BloglinesCorpusReader
  extends DirectoryCorpusReader<Document>
{
  public BloglinesCorpusReader() {}
  
  public BloglinesCorpusReader(DocumentPreprocessor preprocessor)
  {
    super(preprocessor);
  }
  


  protected Iterator<Document> corpusIterator(Iterator<File> files)
  {
    return new BloglinesIterator(files);
  }
  
  public class BloglinesIterator extends DirectoryCorpusReader<Document>.BaseFileIterator {
    private BufferedReader bloglinesReader;
    
    public BloglinesIterator() { super(files); }
    








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
              extractedContent = cleanDoc(extractedContent);
              return new StringDocument(extractedContent);
            }
            


            content = new StringBuilder(line.substring(
              startIndex));
            inContent = true;
          } else {
            if (line.contains("</content>")) {
              inContent = false;
              


              int endIndex = line.lastIndexOf("<");
              content.append(line.substring(0, endIndex));
              
              return new StringDocument(cleanDoc(content.toString())); }
            if ((line.contains("<updated>")) && (content != null))
            {


              int startIndex = line.indexOf(">") + 1;
              int endIndex = line.lastIndexOf("<");
              String date = line.substring(startIndex, endIndex);
              long dateTime = date.equals("") ? 
                0L : 
                Timestamp.valueOf(date).getTime();
              String doc = String.format(
                "%d %s", new Object[] { Long.valueOf(dateTime), 
                cleanDoc(content.toString()) });
              return new StringDocument(doc); }
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
