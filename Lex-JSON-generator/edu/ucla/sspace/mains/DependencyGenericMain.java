package edu.ucla.sspace.mains;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.dependency.CoNLLDependencyExtractor;
import edu.ucla.sspace.dependency.DependencyExtractor;
import edu.ucla.sspace.dependency.DependencyExtractorManager;
import edu.ucla.sspace.dependency.WaCKyDependencyExtractor;
import edu.ucla.sspace.text.DependencyFileDocumentIterator;
import edu.ucla.sspace.text.Document;
import edu.ucla.sspace.text.Stemmer;
import edu.ucla.sspace.text.TokenFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;


















































public abstract class DependencyGenericMain
  extends GenericMain
{
  static final String DEPENDENCY_EXTRACTOR_DESCRIPTION = "This semantic space algorithm operates only on dependency parsed corpora.  The\ncorpora must be formated in way recognized by one of extractors.  The currently\nsupported dependency extractors are CoNLL and WaCKy.  One of these may be\nspecifed with the -D, --dependencyParseFormat option.  The CoNLL extractor\nsupports optional configuration with the -G, --configFile option to indicate the\norder of the fields.";
  
  public DependencyGenericMain() {}
  
  public void addExtraOptions(ArgOptions options)
  {
    options.addOption('G', "configFile", 
      "XML configuration file for the format of a dependency parse", 
      
      true, "FILE", 
      "Advanced Dependency Parsing");
    options.addOption('D', "dependencyParseFormat", 
      "the name of the dependency parsed format for the corpus (defalt: CoNLL)", 
      
      true, "STR", 
      "Advanced Dependency Parsing");
    options.addOption('H', "discardHeaderLines", 
      "If true, the first line in every dependency parse document will be discarded.  This is useful if the first line corresponds to a document or instance identifier and not acually part of the parsed text.  (Default: false)", 
      



      false, null, "Advanced Dependency Parsing");
  }
  






  protected void setupDependencyExtractor()
  {
    TokenFilter filter = argOptions.hasOption("tokenFilter") ? 
      TokenFilter.loadFromSpecification(argOptions.getStringOption('F')) : 
      null;
    
    Stemmer stemmer = (Stemmer)argOptions.getObjectOption("stemmingAlgorithm", null);
    String format = argOptions.getStringOption(
      "dependencyParseFormat", "CoNLL");
    
    if (format.equals("CoNLL")) {
      DependencyExtractor e = argOptions.hasOption('G') ? 
        new CoNLLDependencyExtractor(argOptions.getStringOption('G'), 
        filter, stemmer) : 
        new CoNLLDependencyExtractor(filter, stemmer);
      DependencyExtractorManager.addExtractor("CoNLL", e, true);
    } else if (format.equals("WaCKy")) {
      if (argOptions.hasOption('G'))
        throw new IllegalArgumentException(
          "WaCKy does not support configuration with -G");
      DependencyExtractor e = 
        new WaCKyDependencyExtractor(filter, stemmer);
      DependencyExtractorManager.addExtractor("WaCKy", e, true);
    } else {
      throw new IllegalArgumentException(
        "Unrecognized dependency parsed format: " + format);
    }
  }
  

  protected void addFileIterators(Collection<Iterator<Document>> docIters, String[] fileNames)
    throws IOException
  {
    throw new UnsupportedOperationException(
      "A file based document iterator does not exist");
  }
  



  protected void addDocIterators(Collection<Iterator<Document>> docIters, String[] fileNames)
    throws IOException
  {
    boolean removeHeader = argOptions.hasOption('H');
    for (String s : fileNames) {
      docIters.add(
        new DependencyFileDocumentIterator(s, removeHeader));
    }
  }
  



  protected void usage()
  {
    String specifics = getAlgorithmSpecifics();
    System.out.println(
      "usage: java " + 
      getClass().getName() + 
      " [options] <output-dir>\n" + 
      argOptions.prettyPrint() + (
      specifics.length() == 0 ? "" : new StringBuilder("\n").append(specifics).toString()) + 
      "\n\n" + "token configuration lists sets of files that contain tokens to be included or\nexcluded.  The behavior, \"include\" or \"exclude\" is specified\nfirst, followed by one or more file names, each separated by colons.\nMultiple behaviors may be specified one after the other using a ','\ncharacter to separate them.  For example, a typical configuration may\nlook like: include=top-tokens.txt:test-words.txt,exclude=stop-words.txt\nNote that behaviors are applied in the order they are presented on the command-line." + 
      "\n\n" + "Tokens can be stemmed for various languages using wrappers for the snowball\nstemming algorithms.  Each language has it's own stemmer, following a simple naming\n convention: LanguagenameStemmer." + 
      "\n\n" + "Semantic space files are stored in one of four formats: text, sparse_text, binary\nsparse_binary.  The sparse versions should be used if the algorithm produces\nsemantic vectors in which more than half of the values are 0.  The sparse\nversions are much more compact for these types of semantic spaces and will be\nboth faster to read and write as well as be much smaller on disk.  Text formats\nare human readable but may take up more space.  Binary formats offer\nsignificantly better I/O performance." + 
      "\n\n" + "This semantic space algorithm operates only on dependency parsed corpora.  The\ncorpora must be formated in way recognized by one of extractors.  The currently\nsupported dependency extractors are CoNLL and WaCKy.  One of these may be\nspecifed with the -D, --dependencyParseFormat option.  The CoNLL extractor\nsupports optional configuration with the -G, --configFile option to indicate the\norder of the fields." + 
      "\n\n" + "Send bug reports or comments to <s-space-research-dev@googlegroups.com>.");
  }
}
