package edu.ucla.sspace.tools;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.dependency.CoNLLDependencyExtractor;
import edu.ucla.sspace.dependency.DependencyExtractor;
import edu.ucla.sspace.dependency.DependencyTreeNode;
import edu.ucla.sspace.text.DependencyFileDocumentIterator;
import edu.ucla.sspace.text.Document;
import edu.ucla.sspace.text.TokenFilter;
import edu.ucla.sspace.util.LoggerUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;




















































public class DepPsdTokenCounter
{
  private final Set<String> foundTokens;
  private final DependencyExtractor extractor;
  
  public DepPsdTokenCounter(DependencyExtractor extractor)
  {
    this.extractor = extractor;
    foundTokens = new HashSet();
  }
  
  public Set<String> getTokens() {
    return foundTokens;
  }
  

  private void process(Iterator<Document> docs)
    throws IOException
  {
    long numTokens = 0L;
    int index; int i; for (; docs.hasNext(); 
        









        i < index)
    {
      Document doc = (Document)docs.next();
      BufferedReader br = doc.reader();
      String header = br.readLine();
      String[] pieces = header.split("\\s+");
      index = Integer.parseInt(pieces[3]);
      
      DependencyTreeNode[] nodes = extractor.readNextTree(br);
      foundTokens.add(nodes[index].word().toLowerCase());
      for (int i = index + 1; (i < index + 10) && (i < nodes.length); i++)
        foundTokens.add(nodes[i].word().toLowerCase());
      i = Math.max(0, index - 10); continue;
      foundTokens.add(nodes[i].word().toLowerCase());i++;
    }
  }
  
  public static void main(String[] args)
    throws Exception
  {
    ArgOptions options = new ArgOptions();
    options.addOption('F', "tokenFilter", "filters to apply to the input token stream", 
      true, "FILTER_SPEC", 
      "Tokenizing Options");
    options.addOption('v', "verbose", 
      "Print verbose output about counting status", 
      false, null, "Optional");
    

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
    TokenFilter filter = options.hasOption("tokenFilter") ? 
      TokenFilter.loadFromSpecification(options.getStringOption('F')) : 
      null;
    

    DependencyExtractor e = new CoNLLDependencyExtractor(filter, null);
    DepPsdTokenCounter counter = new DepPsdTokenCounter(e);
    

    for (int i = 1; i < options.numPositionalArgs(); i++) {
      counter.process(new DependencyFileDocumentIterator(
        options.getPositionalArg(i)));
    }
    
    PrintWriter pw = new PrintWriter(options.getPositionalArg(0));
    for (String term : counter.getTokens())
      pw.println(term);
    pw.close();
  }
}
