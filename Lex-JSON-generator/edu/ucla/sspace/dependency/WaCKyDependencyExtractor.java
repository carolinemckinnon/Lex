package edu.ucla.sspace.dependency;

import edu.ucla.sspace.text.Stemmer;
import edu.ucla.sspace.text.TokenFilter;















































































public class WaCKyDependencyExtractor
  extends CoNLLDependencyExtractor
{
  public WaCKyDependencyExtractor()
  {
    this(null, null);
  }
  




  public WaCKyDependencyExtractor(TokenFilter filter, Stemmer stemmer)
  {
    super(filter, stemmer, 3, 0, 1, 2, 4, 5);
  }
}
