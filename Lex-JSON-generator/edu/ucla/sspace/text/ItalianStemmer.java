package edu.ucla.sspace.text;

import org.tartarus.snowball.ext.italianStemmer;




























public class ItalianStemmer
  implements Stemmer
{
  public ItalianStemmer() {}
  
  public String stem(String token)
  {
    italianStemmer stemmer = new italianStemmer();
    stemmer.setCurrent(token);
    stemmer.stem();
    return stemmer.getCurrent();
  }
}
