package edu.ucla.sspace.wordsi;

import java.io.BufferedReader;

public abstract interface ContextExtractor
{
  public abstract void processDocument(BufferedReader paramBufferedReader, Wordsi paramWordsi);
  
  public abstract int getVectorLength();
}
