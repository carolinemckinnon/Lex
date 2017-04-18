package edu.ucla.sspace.common;

import edu.ucla.sspace.vector.Vector;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

public abstract interface SemanticSpace
{
  public abstract void processDocument(BufferedReader paramBufferedReader)
    throws IOException;
  
  public abstract Set<String> getWords();
  
  public abstract Vector getVector(String paramString);
  
  public abstract void processSpace(Properties paramProperties);
  
  public abstract String getSpaceName();
  
  public abstract int getVectorLength();
}
