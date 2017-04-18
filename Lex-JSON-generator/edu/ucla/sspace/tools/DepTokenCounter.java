package edu.ucla.sspace.tools;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.dependency.CoNLLDependencyExtractor;
import edu.ucla.sspace.dependency.DependencyExtractor;
import edu.ucla.sspace.dependency.DependencyTreeNode;
import edu.ucla.sspace.dependency.WaCKyDependencyExtractor;
import edu.ucla.sspace.text.DependencyFileDocumentIterator;
import edu.ucla.sspace.text.Document;
import edu.ucla.sspace.text.Stemmer;
import edu.ucla.sspace.text.TokenFilter;
import edu.ucla.sspace.util.LoggerUtil;
import edu.ucla.sspace.util.TrieMap;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;



















































public class DepTokenCounter
{
  private static final int UPDATE_INTERVAL = 10000;
  private static final Logger LOGGER = Logger.getLogger(DepTokenCounter.class.getName());
  




  private final Map<String, Integer> tokenToCount;
  




  private final boolean doLowerCasing;
  




  private final boolean doPos;
  




  private final DependencyExtractor extractor;
  





  public DepTokenCounter(boolean doLowerCasing, boolean doPos, DependencyExtractor extractor)
  {
    this.doLowerCasing = doLowerCasing;
    this.doPos = doPos;
    this.extractor = extractor;
    
    tokenToCount = new TrieMap();
  }
  


  public Map<String, Integer> getTokenCounts()
  {
    return Collections.unmodifiableMap(tokenToCount);
  }
  

  private void process(Iterator<Document> docs)
    throws IOException
  {
    long numTokens = 0L;
    int j; int i; for (; docs.hasNext(); 
        

        i < j)
    {
      Document doc = (Document)docs.next();
      DependencyTreeNode[] nodes = extractor.readNextTree(doc.reader());
      DependencyTreeNode[] arrayOfDependencyTreeNode1; j = (arrayOfDependencyTreeNode1 = nodes).length;i = 0; continue;DependencyTreeNode node = arrayOfDependencyTreeNode1[i];
      String token = node.word();
      if (doLowerCasing)
        token = token.toLowerCase();
      if (doPos) {
        token = token + "-" + node.pos();
      }
      Integer count = (Integer)tokenToCount.get(token);
      tokenToCount.put(token, Integer.valueOf(count == null ? 1 : 1 + count.intValue()));
      numTokens += 1L;
      
      if (numTokens % 10000L == 0L) {
        LOGGER.fine(
          "Processed " + numTokens + " tokens.  Currently " + 
          tokenToCount.size() + " unique tokens");
      }
      i++;
    }
  }
  














  public static void main(String[] args)
    throws Exception
  {
    ArgOptions options = new ArgOptions();
    options.addOption('Z', "stemmingAlgorithm", 
      "specifices the stemming algorithm to use on tokens while iterating.  (default: none)", 
      
      true, "CLASSNAME", "Tokenizing Options");
    options.addOption('F', "tokenFilter", "filters to apply to the input token stream", 
      true, "FILTER_SPEC", 
      "Tokenizing Options");
    options.addOption('L', "lowerCase", "lower-cases each token after all other filtering has been applied", 
      false, null, 
      "Tokenizing Options");
    options.addOption('P', "partOfSpeech", 
      "use part of speech tags for each token.", 
      false, null, "Tokenizing Options");
    options.addOption('H', "discardHeader", 
      "If true, the first line of each dependency document will be discarded.", 
      
      false, null, "Tokenizing Options");
    options.addOption('v', "verbose", 
      "Print verbose output about counting status", 
      false, null, "Optional");
    options.addOption('D', "dependencyParseFormat", 
      "the name of the dependency parsed format for the corpus (defalt: CoNLL)", 
      
      true, "STR", 
      "Advanced Dependency Parsing");
    

    options.parseOptions(args);
    if (options.numPositionalArgs() < 2) {
      System.out.println(
        "usage: java DepTokenCounter [options] <output-file> <input-file> [<input-file>]*\n" + 
        
        options.prettyPrint() + 
        "\n\n" + "token configuration lists sets of files that contain tokens to be included or\nexcluded.  The behavior, \"include\" or \"exclude\" is specified\nfirst, followed by one or more file names, each separated by colons.\nMultiple behaviors may be specified one after the other using a ','\ncharacter to separate them.  For example, a typical configuration may\nlook like: include=top-tokens.txt:test-words.txt,exclude=stop-words.txt\nNote that behaviors are applied in the order they are presented on the command-line.");
      return;
    }
    

    if (options.hasOption("verbose")) {
      LoggerUtil.setLevel(Level.FINE);
    }
    
    boolean doLowerCasing = options.hasOption("lowerCase");
    boolean doPos = options.hasOption("partOfSpeech");
    boolean discardHeader = options.hasOption('H');
    
    TokenFilter filter = options.hasOption("tokenFilter") ? 
      TokenFilter.loadFromSpecification(options.getStringOption('F')) : 
      null;
    
    Stemmer stemmer = (Stemmer)options.getObjectOption("stemmingAlgorithm", null);
    
    String format = options.getStringOption(
      "dependencyParseFormat", "CoNLL");
    

    DependencyExtractor e = null;
    if (format.equals("CoNLL")) {
      e = new CoNLLDependencyExtractor(filter, stemmer);
    } else if (format.equals("WaCKy")) {
      e = new WaCKyDependencyExtractor(filter, stemmer);
    }
    DepTokenCounter counter = new DepTokenCounter(doLowerCasing, doPos, e);
    

    for (int i = 1; i < options.numPositionalArgs(); i++) {
      counter.process(new DependencyFileDocumentIterator(
        options.getPositionalArg(i), discardHeader));
    }
    
    PrintWriter pw = new PrintWriter(options.getPositionalArg(0));
    
    Iterator localIterator = tokenToCount.entrySet().iterator();
    while (localIterator.hasNext()) {
      Map.Entry<String, Integer> entry = (Map.Entry)localIterator.next();
      pw.printf("%s %d\n", new Object[] { entry.getKey(), entry.getValue() }); }
    pw.close();
  }
}
