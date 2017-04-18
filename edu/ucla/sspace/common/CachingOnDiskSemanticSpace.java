package edu.ucla.sspace.common;

import edu.ucla.sspace.vector.Vector;
import edu.ucla.sspace.vector.Vectors;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.logging.Logger;























































public class CachingOnDiskSemanticSpace
  implements SemanticSpace
{
  private static final Logger LOGGER = Logger.getLogger(CachingOnDiskSemanticSpace.class.getName());
  





  private final Map<String, Vector> wordToVector;
  




  private final SemanticSpace backingSpace;
  





  public CachingOnDiskSemanticSpace(String filename)
    throws IOException
  {
    this(new File(filename));
  }
  







  public CachingOnDiskSemanticSpace(File file)
    throws IOException
  {
    backingSpace = new OnDiskSemanticSpace(file);
    wordToVector = new WeakHashMap();
  }
  


  public String getSpaceName()
  {
    return backingSpace.getSpaceName();
  }
  


  public Set<String> getWords()
  {
    return backingSpace.getWords();
  }
  







  public synchronized Vector getVector(String word)
  {
    Vector vector = (Vector)wordToVector.get(word);
    if (vector != null) {
      return Vectors.immutable(vector);
    }
    Vector v = backingSpace.getVector(word);
    if (v != null)
      wordToVector.put(word, v);
    return v;
  }
  


  public int getVectorLength()
  {
    return backingSpace.getVectorLength();
  }
  




  public void processDocument(BufferedReader document)
  {
    throw new UnsupportedOperationException(
      "CachingOnDiskSemanticSpace instances cannot be updated");
  }
  




  public void processSpace(Properties props)
  {
    throw new UnsupportedOperationException(
      "CachingOnDiskSemanticSpace instances cannot be updated");
  }
}
