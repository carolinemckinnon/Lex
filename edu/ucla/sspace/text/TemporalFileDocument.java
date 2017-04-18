package edu.ucla.sspace.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;








































public class TemporalFileDocument
  implements TemporalDocument
{
  private final BufferedReader reader;
  private final long timeStamp;
  
  public TemporalFileDocument(String fileName)
    throws IOException
  {
    this(new File(fileName));
  }
  







  public TemporalFileDocument(File file)
    throws IOException
  {
    this(file, file.lastModified());
  }
  








  public TemporalFileDocument(String fileName, long timeStamp)
    throws IOException
  {
    this(new File(fileName), timeStamp);
  }
  







  public TemporalFileDocument(File file, long timeStamp)
    throws IOException
  {
    BufferedReader r = null;
    try {
      r = new BufferedReader(new FileReader(file));
    } catch (Throwable t) {
      t.printStackTrace();
    }
    reader = r;
    this.timeStamp = timeStamp;
  }
  


  public BufferedReader reader()
  {
    return reader;
  }
  


  public long timeStamp()
  {
    return timeStamp;
  }
}
