package edu.ucla.sspace.text;

import java.io.File;
import java.io.Reader;
import java.util.Iterator;

public abstract interface CorpusReader<D extends Document>
{
  public abstract Iterator<D> read(File paramFile);
  
  public abstract Iterator<D> read(Reader paramReader);
}
