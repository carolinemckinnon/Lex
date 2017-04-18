package edu.ucla.sspace.mains;

import edu.ucla.sspace.matrix.SVD.Algorithm;
















































































































public final class OptionDescriptions
{
  public static final String COMPOUND_WORDS_DESCRIPTION = "The compound word option specifies a file whose contents are compound tokens,\ne.g. white house.  Each compound token should be specified on its own line.\nCompound tokenization is greedy and will select the longest compound token\npresent.  For example if \"bar exam\" and \"California bar exam\" are both\ncompound tokens, the latter will always be returned as a single token, rather\nthan returning the two tokens \"California\" and \"bar exam\".";
  public static final String TOKEN_FILTER_DESCRIPTION = "token configuration lists sets of files that contain tokens to be included or\nexcluded.  The behavior, \"include\" or \"exclude\" is specified\nfirst, followed by one or more file names, each separated by colons.\nMultiple behaviors may be specified one after the other using a ','\ncharacter to separate them.  For example, a typical configuration may\nlook like: include=top-tokens.txt:test-words.txt,exclude=stop-words.txt\nNote that behaviors are applied in the order they are presented on the command-line.";
  public static final String TOKEN_STEMMING_DESCRIPTION = "Tokens can be stemmed for various languages using wrappers for the snowball\nstemming algorithms.  Each language has it's own stemmer, following a simple naming\n convention: LanguagenameStemmer.";
  public static final String FILE_FORMAT_DESCRIPTION = "Semantic space files are stored in one of four formats: text, sparse_text, binary\nsparse_binary.  The sparse versions should be used if the algorithm produces\nsemantic vectors in which more than half of the values are 0.  The sparse\nversions are much more compact for these types of semantic spaces and will be\nboth faster to read and write as well as be much smaller on disk.  Text formats\nare human readable but may take up more space.  Binary formats offer\nsignificantly better I/O performance.";
  public static final String HELP_DESCRIPTION = "Send bug reports or comments to <s-space-research-dev@googlegroups.com>.";
  public static final String SVD_DESCRIPTION = "The SVD implmentation may select from several options:\n  " + 
    SVD.Algorithm.values() + 
    "\nSVDLIBJ is included with the S-Space Package and provides a fully " + 
    "Java\n" + 
    "implementation of a sparse SVD.  The other options provide support " + 
    "from\n" + 
    "external libraries and programs that implement the SVD.  " + 
    "If external\n" + 
    "programs (svd, octave, matlab) are used, the program must " + 
    "be invokable\n" + 
    "using the current PATH environment variable.  If the library is " + 
    "Java-based\n" + 
    "(JAMA, COLT), the library must be present in the classpath.  " + 
    "However, for\n" + 
    "Java-based libraries, if the semantic space algorithm iteself is " + 
    "being invoked\n" + 
    "through a .jar, the classpath to the SVD library must be specified " + 
    "using an\n" + 
    "additional system property.  For JAMA, the path to the .jar file " + 
    "must be\n" + 
    "specified using the system property \"jama.path\".  To set this " + 
    "on the\n" + 
    "command-line, use -Djama.path=<.jar location>.  Similarly, if COLT " + 
    "is to be used\n" + 
    "when invoked from a .jar, then the \"colt.path\" property\n" + 
    "must be set.";
  
  private OptionDescriptions() {}
}
