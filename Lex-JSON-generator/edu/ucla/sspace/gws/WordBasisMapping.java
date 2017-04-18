package edu.ucla.sspace.gws;

import edu.ucla.sspace.basis.AbstractBasisMapping;
import edu.ucla.sspace.util.Duple;






























public class WordBasisMapping
  extends AbstractBasisMapping<Duple<String, Integer>, String>
{
  private static final long serialVersionUID = 1L;
  
  public WordBasisMapping() {}
  
  public int getDimension(Duple<String, Integer> key)
  {
    return getDimensionInternal((String)x);
  }
}
