package edu.ucla.sspace.basis;

import java.util.Set;




























public class StringBasisMapping
  extends AbstractBasisMapping<String, String>
{
  private static final long serialVersionUID = 1L;
  
  public StringBasisMapping() {}
  
  public StringBasisMapping(Set<String> words)
  {
    for (String word : words) {
      getDimension(word);
    }
  }
  

  public int getDimension(String key)
  {
    return getDimensionInternal(key);
  }
}
