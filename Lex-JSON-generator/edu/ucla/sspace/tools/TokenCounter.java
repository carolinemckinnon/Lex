package edu.ucla.sspace.tools;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.text.IteratorFactory;
import edu.ucla.sspace.util.LoggerUtil;
import edu.ucla.sspace.util.TrieMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
















































public class TokenCounter
{
  private static final int UPDATE_INTERVAL = 10000;
  private static final Logger LOGGER = Logger.getLogger(TokenCounter.class.getName());
  



  private final Map<String, Integer> tokenToCount;
  



  private final boolean doLowerCasing;
  



  public TokenCounter()
  {
    this(false);
  }
  





  public TokenCounter(boolean doLowerCasing)
  {
    this.doLowerCasing = doLowerCasing;
    tokenToCount = new TrieMap();
  }
  


  public Map<String, Integer> getTokenCounts()
  {
    return Collections.unmodifiableMap(tokenToCount);
  }
  

  public void processFile(String fileName)
    throws IOException
  {
    process(new BufferedReader(new FileReader(fileName)));
  }
  

  public void processFile(File file)
    throws IOException
  {
    process(new BufferedReader(new FileReader(file)));
  }
  


  public void process(BufferedReader br)
  {
    process(IteratorFactory.tokenize(br));
  }
  


  public void process(String tokens)
  {
    process(IteratorFactory.tokenize(tokens));
  }
  





  private void process(Iterator<String> tokens)
  {
    long numTokens = 0L;
    while (tokens.hasNext()) {
      String token = (String)tokens.next();
      if (doLowerCasing)
        token = token.toLowerCase();
      if (token.matches("[0-9]+"))
        token = "<NUM>";
      if (!token.matches("[^\\w\\s;:\\(\\)\\[\\]'!/&?\",\\.<>]"))
      {

        Integer count = (Integer)tokenToCount.get(token);
        tokenToCount.put(token, Integer.valueOf(count == null ? 1 : 1 + count.intValue()));
        numTokens += 1L;
        if ((numTokens % 10000L == 0L) && 
          (LOGGER.isLoggable(Level.FINE)))
          LOGGER.fine("Processed " + numTokens + " tokens.  Currently " + 
            tokenToCount.size() + " unique tokens");
      }
    }
  }
  
  public static void main(String[] args) { ArgOptions options = new ArgOptions();
    options.addOption('Z', "stemmingAlgorithm", 
      "specifices the stemming algorithm to use on tokens while iterating.  (default: none)", 
      
      true, "CLASSNAME", "Tokenizing Options");
    options.addOption('F', "tokenFilter", "filters to apply to the input token stream", 
      true, "FILTER_SPEC", 
      "Tokenizing Options");
    options.addOption('C', "compoundWords", "a file where each line is a recognized compound word", 
      true, "FILE", 
      "Tokenizing Options");
    options.addOption('L', "lowerCase", "lower-cases each token after all other filtering has been applied", 
      false, null, 
      "Tokenizing Options");
    options.addOption('z', "wordLimit", "Set the maximum number of words a document can return", 
    
      true, "INT", "Tokenizing Options");
    options.addOption('v', "verbose", 
      "Print verbose output about counting status", 
      false, null, "Optional");
    options.parseOptions(args);
    if (options.numPositionalArgs() < 2) {
      System.out.println(
        "usage: java TokenCounter [options] <output-file> <input-file> [<input-file>]*\n" + 
        
        options.prettyPrint() + 
        "\n" + "The compound word option specifies a file whose contents are compound tokens,\ne.g. white house.  Each compound token should be specified on its own line.\nCompound tokenization is greedy and will select the longest compound token\npresent.  For example if \"bar exam\" and \"California bar exam\" are both\ncompound tokens, the latter will always be returned as a single token, rather\nthan returning the two tokens \"California\" and \"bar exam\"." + 
        "\n\n" + "token configuration lists sets of files that contain tokens to be included or\nexcluded.  The behavior, \"include\" or \"exclude\" is specified\nfirst, followed by one or more file names, each separated by colons.\nMultiple behaviors may be specified one after the other using a ','\ncharacter to separate them.  For example, a typical configuration may\nlook like: include=top-tokens.txt:test-words.txt,exclude=stop-words.txt\nNote that behaviors are applied in the order they are presented on the command-line.");
      return;
    }
    
    if (options.hasOption("verbose")) {
      LoggerUtil.setLevel(Level.FINE);
    }
    
    boolean doLowerCasing = options.hasOption("lowerCase");
    
    Properties props = System.getProperties();
    

    if (options.hasOption("tokenFilter")) {
      props.setProperty("edu.ucla.sspace.text.TokenizerFactory.tokenFilter", 
        options.getStringOption("tokenFilter"));
    }
    if (options.hasOption("stemmingAlgorithm")) {
      props.setProperty("edu.ucla.sspace.text.TokenizerFactory.stemmer", 
        options.getStringOption("stemmingAlgorithm"));
    }
    if (options.hasOption("compoundWords"))
      props.setProperty("edu.ucla.sspace.text.TokenizerFactory.compoundTokens", 
        options.getStringOption("compoundWords"));
    if (options.hasOption("wordLimit")) {
      props.setProperty("edu.ucla.sspace.text.TokenizerFactory.tokenCountLimit", 
        options.getStringOption("wordLimit"));
    }
    IteratorFactory.setProperties(props);
    try
    {
      TokenCounter counter = new TokenCounter(doLowerCasing);
      
      for (int i = 1; i < options.numPositionalArgs(); i++) {
        counter.processFile(options.getPositionalArg(i));
      }
      PrintWriter pw = new PrintWriter(options.getPositionalArg(0));
      
      Iterator localIterator = tokenToCount.entrySet().iterator();
      while (localIterator.hasNext()) {
        Map.Entry<String, Integer> e = (Map.Entry)localIterator.next();
        pw.println((String)e.getKey() + " " + e.getValue()); }
      pw.close();
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }
}
