package edu.ucla.sspace.text;

import edu.ucla.sspace.util.Duple;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;





























































public class CompoundWordIterator
  implements Iterator<String>
{
  private final Map<String, CompoundTokens> compoundTokens;
  private BufferedIterator tokenizer;
  
  public CompoundWordIterator(String str, Set<String> compoundWords)
  {
    this(new BufferedReader(new StringReader(str)), compoundWords);
  }
  




  public CompoundWordIterator(BufferedReader br, Set<String> compoundWords)
  {
    tokenizer = new BufferedIterator(br);
    compoundTokens = new LinkedHashMap();
    initializeMapping(compoundWords);
  }
  




  public CompoundWordIterator(Iterator<String> tokens, Set<String> compoundWords)
  {
    tokenizer = new BufferedIterator(tokens);
    compoundTokens = new LinkedHashMap();
    initializeMapping(compoundWords);
  }
  



  private void initializeMapping(Set<String> compoundWords)
  {
    for (String s : compoundWords) {
      String[] tokens = s.split("\\s+");
      
      if (tokens.length != 1)
      {

        CompoundTokens compounds = (CompoundTokens)compoundTokens.get(tokens[0]);
        if (compounds == null)
        {

          compounds = new CompoundTokens();
          compoundTokens.put(tokens[0], compounds);
        }
        


        List<String> tokenList = 
          Arrays.asList((String[])Arrays.copyOfRange(tokens, 1, tokens.length));
        compounds.addCompound(tokenList, s);
      }
    }
  }
  

  public boolean hasNext()
  {
    return tokenizer.hasNext();
  }
  






  public String next()
  {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    
    String token = tokenizer.next();
    


    CompoundTokens compounds = (CompoundTokens)compoundTokens.get(token);
    


    if (compounds != null) {
      List<String> nextTokens = tokenizer.peek(compounds.maxTokens());
      Duple<Integer, String> compound = compounds.findMatch(nextTokens);
      if (compound != null)
      {
        for (int i = 0; i < ((Integer)x).intValue(); i++)
          tokenizer.next();
        return (String)y;
      }
      return token;
    }
    


    return token;
  }
  


  public void remove()
  {
    throw new UnsupportedOperationException("remove is not supported");
  }
  




  public void reset(BufferedReader br)
  {
    tokenizer = new BufferedIterator(br);
  }
  



  public void reset(Iterator<String> tokens)
  {
    tokenizer = new BufferedIterator(tokens);
  }
  





  private static class CompoundTokens
  {
    private final Map<List<String>, String> compounds;
    




    private int maxTokens;
    




    public CompoundTokens()
    {
      maxTokens = 0;
      compounds = new LinkedHashMap();
    }
    



    public void addCompound(List<String> tokens, String compoundToken)
    {
      compounds.put(tokens, compoundToken);
      if (tokens.size() > maxTokens) {
        maxTokens = tokens.size();
      }
    }
    


    public int maxTokens()
    {
      return maxTokens;
    }
    




    public Duple<Integer, String> findMatch(List<String> tokens)
    {
      String match = null;
      int num = -1;
      for (Map.Entry<List<String>, String> e : compounds.entrySet())
      {
        if (tokens.size() >= ((List)e.getKey()).size())
        {

          List<String> ngram = (List)e.getKey();
          List<String> toCompare = tokens.subList(0, ngram.size());
          
          if (ngram.equals(toCompare))
          {

            if ((match == null) || (match.length() < ((String)e.getValue()).length())) {
              match = (String)e.getValue();
              num = ((List)e.getKey()).size();
            } }
        }
      }
      return match == null ? 
        null : new Duple(Integer.valueOf(num), match);
    }
  }
}
