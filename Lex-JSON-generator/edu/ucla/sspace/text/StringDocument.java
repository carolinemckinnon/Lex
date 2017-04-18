package edu.ucla.sspace.text;

import java.io.BufferedReader;
import java.io.StringReader;

































public class StringDocument
  implements Document
{
  private final String text;
  
  public StringDocument(String docText)
  {
    text = docText;
  }
  



  public BufferedReader reader()
  {
    return new BufferedReader(new StringReader(text));
  }
  


  public String toString()
  {
    return text;
  }
}
