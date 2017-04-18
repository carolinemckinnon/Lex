package edu.ucla.sspace.text;

import java.io.BufferedReader;
import java.io.StringReader;









































public class TemporalStringDocument
  implements TemporalDocument
{
  private final BufferedReader reader;
  private final long timeStamp;
  
  public TemporalStringDocument(String docText, long timeStamp)
  {
    reader = new BufferedReader(new StringReader(docText));
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
