package edu.ucla.sspace.util;

import java.io.BufferedReader;
import java.io.IOException;

public abstract interface ResourceFinder
{
  public abstract BufferedReader open(String paramString)
    throws IOException;
}
