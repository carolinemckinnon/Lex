package edu.ucla.sspace.temporal;

import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.vector.Vector;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.SortedSet;

public abstract interface TemporalSemanticSpace
  extends SemanticSpace
{
  public abstract void processDocument(BufferedReader paramBufferedReader)
    throws IOException;
  
  public abstract void processDocument(BufferedReader paramBufferedReader, long paramLong)
    throws IOException;
  
  public abstract Long startTime();
  
  public abstract Long endTime();
  
  public abstract SortedSet<Long> getTimeSteps(String paramString);
  
  public abstract Vector getVectorAfter(String paramString, long paramLong);
  
  public abstract Vector getVectorBefore(String paramString, long paramLong);
  
  public abstract Vector getVectorBetween(String paramString, long paramLong1, long paramLong2);
  
  public abstract Vector getVector(String paramString);
}
