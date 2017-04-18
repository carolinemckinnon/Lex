package edu.ucla.sspace.text;

import edu.ucla.sspace.util.FileResourceFinder;
import edu.ucla.sspace.util.ResourceFinder;
import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;







































































public class TokenFilter
{
  private final Set<String> tokens;
  private final boolean excludeTokens;
  private TokenFilter parent;
  
  public TokenFilter(Set<String> tokens)
  {
    this(tokens, false, null);
  }
  










  public TokenFilter(Set<String> tokens, boolean excludeTokens)
  {
    this(tokens, excludeTokens, null);
  }
  













  public TokenFilter(Set<String> tokens, boolean excludeTokens, TokenFilter parent)
  {
    this.tokens = tokens;
    this.excludeTokens = excludeTokens;
    this.parent = parent;
  }
  







  public boolean accept(String token)
  {
    return ((parent == null) || (parent.accept(token))) && ((
      tokens.contains(token) ^ excludeTokens));
  }
  









  public TokenFilter combine(TokenFilter parent)
  {
    TokenFilter oldParent = parent;
    this.parent = parent;
    return oldParent;
  }
  




















  public static TokenFilter loadFromSpecification(String configuration)
  {
    return loadFromSpecification(configuration, new FileResourceFinder());
  }
  

























  public static TokenFilter loadFromSpecification(String configuration, ResourceFinder finder)
  {
    TokenFilter toReturn = null;
    

    String[] filters = configuration.split(",");
    
    for (String s : filters) {
      String[] optionAndFiles = s.split("=");
      if (optionAndFiles.length != 2) {
        throw new IllegalArgumentException(
          "Invalid number of filter parameters: " + s);
      }
      String behavior = optionAndFiles[0];
      boolean exclude = behavior.equals("exclude");
      
      if ((!exclude) && (!behavior.equals("include"))) {
        throw new IllegalArgumentException(
          "Invalid filter behavior: " + behavior);
      }
      String[] files = optionAndFiles[1].split(":");
      

      Set<String> words = new HashSet();
      try {
        for (String f : files) {
          BufferedReader br = finder.open(f);
          for (String line = null; (line = br.readLine()) != null;)
            words.add(line);
          br.close();
        }
      }
      catch (IOException ioe) {
        throw new IOError(ioe);
      }
      

      toReturn = new TokenFilter(words, exclude, toReturn);
    }
    
    return toReturn;
  }
}
