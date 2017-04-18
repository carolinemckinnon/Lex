package edu.ucla.sspace.text;

import org.tartarus.snowball.ext.germanStemmer;




























public class GermanStemmer
  implements Stemmer
{
  public GermanStemmer() {}
  
  public String stem(String token)
  {
    germanStemmer stemmer = new germanStemmer();
    stemmer.setCurrent(token);
    stemmer.stem();
    return stemmer.getCurrent();
  }
}
