package edu.ucla.sspace.text;

import org.tartarus.snowball.ext.englishStemmer;




























public class EnglishStemmer
  implements Stemmer
{
  public EnglishStemmer() {}
  
  public String stem(String token)
  {
    englishStemmer stemmer = new englishStemmer();
    stemmer.setCurrent(token);
    stemmer.stem();
    return stemmer.getCurrent();
  }
}
