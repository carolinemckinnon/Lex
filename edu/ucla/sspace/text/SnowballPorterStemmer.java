package edu.ucla.sspace.text;

import org.tartarus.snowball.ext.porterStemmer;




























public class SnowballPorterStemmer
  implements Stemmer
{
  public SnowballPorterStemmer() {}
  
  public String stem(String token)
  {
    porterStemmer stemmer = new porterStemmer();
    stemmer.setCurrent(token);
    stemmer.stem();
    return stemmer.getCurrent();
  }
}
