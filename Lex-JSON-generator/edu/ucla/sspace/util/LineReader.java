package edu.ucla.sspace.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

























































public class LineReader
  implements Iterable<String>
{
  private final File f;
  
  public LineReader(File f)
  {
    this.f = f;
  }
  


  public Iterator<String> iterator()
  {
    return new LineIterator();
  }
  



  private class LineIterator
    implements Iterator<String>
  {
    private final BufferedReader br;
    


    private String next;
    


    public LineIterator()
    {
      try
      {
        br = new BufferedReader(new FileReader(f));
        advance();
      } catch (IOException ioe) {
        throw new IOError(ioe);
      }
    }
    
    private void advance() {
      try {
        next = br.readLine();
        
        if (next == null) {
          br.close();
        }
      } catch (IOException ioe) {
        throw new IOError(ioe);
      }
    }
    
    public boolean hasNext() {
      return next != null;
    }
    
    public String next() {
      if (next == null)
        throw new NoSuchElementException();
      String n = next;
      advance();
      return n;
    }
    
    public void remove() {
      throw new UnsupportedOperationException(
        "Cannot remove line from file");
    }
  }
}
