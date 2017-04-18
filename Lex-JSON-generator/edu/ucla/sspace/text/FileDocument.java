package edu.ucla.sspace.text;

import edu.ucla.sspace.util.LineReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.io.StringReader;










































public class FileDocument
  implements Document
{
  private final String fileName;
  private final String contents;
  
  public FileDocument(String fileName)
    throws IOException
  {
    this(fileName, false);
  }
  







  public FileDocument(String fileName, boolean cacheContents)
    throws IOException
  {
    this.fileName = fileName;
    if (cacheContents) {
      StringBuilder sb = new StringBuilder();
      for (String line : new LineReader(new File(fileName)))
        sb.append(line).append('\n');
      contents = sb.toString();
    }
    else {
      contents = null;
    }
  }
  

  public BufferedReader reader()
  {
    try
    {
      return contents == null ? 
        new BufferedReader(new FileReader(fileName)) : 
        new BufferedReader(new StringReader(contents));
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
}
