package edu.ucla.sspace.common;

import edu.ucla.sspace.vector.Vector;
import java.io.BufferedReader;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
























































public class VectorMapSemanticSpace<T extends Vector>
  implements SemanticSpace, Serializable
{
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = Logger.getLogger(VectorMapSemanticSpace.class.getName());
  




  private final Map<String, T> wordSpace;
  



  private final int dimensions;
  



  private String spaceName;
  




  public VectorMapSemanticSpace(Map<String, T> wordSpace, String spaceName, int dimensions)
  {
    if (wordSpace == null)
      throw new IllegalArgumentException(
        "the wordSpace must be non-null");
    if (spaceName == null)
      throw new IllegalArgumentException(
        "the spaceName must be non-null");
    if (dimensions <= 0) {
      throw new IllegalArgumentException(
        "the VectorMapSemanticSpace must have more than 0 dimensions");
    }
    
    this.wordSpace = wordSpace;
    this.dimensions = dimensions;
    this.spaceName = spaceName;
  }
  


  public Set<String> getWords()
  {
    return Collections.unmodifiableSet(wordSpace.keySet());
  }
  


  public T getVector(String term)
  {
    return (Vector)wordSpace.get(term);
  }
  


  public String getSpaceName()
  {
    return spaceName;
  }
  


  public int getVectorLength()
  {
    return dimensions;
  }
  
  public void processDocument(BufferedReader document) {}
  
  public void processSpace(Properties props) {}
}
