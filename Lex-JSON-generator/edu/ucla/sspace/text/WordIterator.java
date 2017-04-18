package edu.ucla.sspace.text;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;































public class WordIterator
  implements Iterator<String>
{
  private static final Pattern notWhiteSpace = Pattern.compile("\\S+");
  



  private final BufferedReader br;
  



  private String next;
  



  private Matcher matcher;
  


  String curLine;
  



  public WordIterator(String str)
  {
    this(new BufferedReader(new StringReader(str)));
  }
  



  public WordIterator(BufferedReader br)
  {
    this.br = br;
    curLine = null;
    advance();
  }
  




  private void advance()
  {
    try
    {
      do
      {
        if ((curLine != null) && (matcher.find()))
          break;
        String line = br.readLine();
        


        if (line == null) {
          next = null;
          br.close();
          return;
        }
        

        matcher = notWhiteSpace.matcher(line);
        curLine = line;

      }
      while (!matcher.find());
      


      next = curLine.substring(matcher.start(), matcher.end());
    }
    catch (IOException ioe)
    {
      throw new IOError(ioe);
    }
  }
  


  public boolean hasNext()
  {
    return next != null;
  }
  


  public String next()
  {
    if (next == null) {
      throw new NoSuchElementException();
    }
    String s = next;
    advance();
    return s;
  }
  


  public void remove()
  {
    throw new UnsupportedOperationException("remove is not supported");
  }
}
