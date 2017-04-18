package edu.ucla.sspace.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;




































public class FileResourceFinder
  implements ResourceFinder
{
  public FileResourceFinder() {}
  
  public BufferedReader open(String fileName)
    throws IOException
  {
    return new BufferedReader(new FileReader(fileName));
  }
}
