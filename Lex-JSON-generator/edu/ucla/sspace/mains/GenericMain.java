package edu.ucla.sspace.mains;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.SemanticSpaceIO;
import edu.ucla.sspace.common.SemanticSpaceIO.SSpaceFormat;
import edu.ucla.sspace.text.CorpusReader;
import edu.ucla.sspace.text.Document;
import edu.ucla.sspace.text.FileListDocumentIterator;
import edu.ucla.sspace.text.IteratorFactory;
import edu.ucla.sspace.text.OneLinePerDocumentIterator;
import edu.ucla.sspace.util.CombinedIterator;
import edu.ucla.sspace.util.LimitedIterator;
import edu.ucla.sspace.util.LoggerUtil;
import edu.ucla.sspace.util.ReflectionUtil;
import edu.ucla.sspace.util.WorkQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;



















































































public abstract class GenericMain
{
  public static final String EXT = ".sspace";
  private static final Logger LOGGER = Logger.getLogger(GenericMain.class.getName());
  



  protected boolean verbose;
  


  protected final ArgOptions argOptions;
  


  protected final boolean isMultiThreaded;
  



  public GenericMain()
  {
    this(true);
  }
  
  public GenericMain(boolean isMultiThreaded) {
    this.isMultiThreaded = isMultiThreaded;
    argOptions = setupOptions();
    verbose = false;
  }
  




  protected abstract SemanticSpace getSpace();
  




  protected String getAlgorithmSpecifics()
  {
    return "";
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
      "\n" + "The compound word option specifies a file whose contents are compound tokens,\ne.g. white house.  Each compound token should be specified on its own line.\nCompound tokenization is greedy and will select the longest compound token\npresent.  For example if \"bar exam\" and \"California bar exam\" are both\ncompound tokens, the latter will always be returned as a single token, rather\nthan returning the two tokens \"California\" and \"bar exam\"." + 
      "\n\n" + "token configuration lists sets of files that contain tokens to be included or\nexcluded.  The behavior, \"include\" or \"exclude\" is specified\nfirst, followed by one or more file names, each separated by colons.\nMultiple behaviors may be specified one after the other using a ','\ncharacter to separate them.  For example, a typical configuration may\nlook like: include=top-tokens.txt:test-words.txt,exclude=stop-words.txt\nNote that behaviors are applied in the order they are presented on the command-line." + 
      "\n\n" + "Tokens can be stemmed for various languages using wrappers for the snowball\nstemming algorithms.  Each language has it's own stemmer, following a simple naming\n convention: LanguagenameStemmer." + 
      "\n\n" + "Semantic space files are stored in one of four formats: text, sparse_text, binary\nsparse_binary.  The sparse versions should be used if the algorithm produces\nsemantic vectors in which more than half of the values are 0.  The sparse\nversions are much more compact for these types of semantic spaces and will be\nboth faster to read and write as well as be much smaller on disk.  Text formats\nare human readable but may take up more space.  Binary formats offer\nsignificantly better I/O performance." + 
      "\n\n" + "Send bug reports or comments to <s-space-research-dev@googlegroups.com>.");
  }
  








  protected SemanticSpaceIO.SSpaceFormat getSpaceFormat()
  {
    return SemanticSpaceIO.SSpaceFormat.TEXT;
  }
  







  protected void addExtraOptions(ArgOptions options) {}
  







  protected void handleExtraOptions() {}
  







  protected void postProcessing() {}
  







  protected Properties setupProperties()
  {
    Properties props = System.getProperties();
    return props;
  }
  




  protected ArgOptions setupOptions()
  {
    ArgOptions options = new ArgOptions();
    

    options.addOption('f', "fileList", "a list of document files", 
      true, "FILE[,FILE...]", "Required (at least one of)");
    options.addOption('d', "docFile", 
      "a file where each line is a document", true, 
      "FILE[,FILE...]", "Required (at least one of)");
    options.addOption('R', "corpusReader", 
      "Specifies a CorpusReader which will automatically parse the document files that are not in the formats expected by -f and -d.", 
      

      true, "CLASSNAME,FILE[,FILE...]", 
      "Required (at least one of)");
    

    options.addOption('o', "outputFormat", "the .sspace format to use", 
      true, "FORMAT", 
      "Program Options");
    if (isMultiThreaded) {
      options.addOption('t', "threads", "the number of threads to use", 
        true, "INT", "Program Options");
    }
    options.addOption('w', "overwrite", "specifies whether to overwrite the existing output", 
      true, "BOOL", 
      "Program Options");
    options.addOption('v', "verbose", "prints verbose output", 
      false, null, "Program Options");
    

    options.addOption('Z', "stemmingAlgorithm", 
      "specifices the stemming algorithm to use on tokens while iterating.  (default: none)", 
      
      true, "CLASSNAME", "Tokenizing Options");
    options.addOption('F', "tokenFilter", "filters to apply to the input token stream", 
      true, "FILTER_SPEC", 
      "Tokenizing Options");
    options.addOption('C', "compoundWords", "a file where each line is a recognized compound word", 
      true, "FILE", 
      "Tokenizing Options");
    options.addOption('z', "wordLimit", "Set the maximum number of words a document can return", 
    
      true, "INT", "Tokenizing Options");
    
    addExtraOptions(options);
    return options;
  }
  








  protected Iterator<Document> getDocumentIterator()
    throws IOException
  {
    Collection<Iterator<Document>> docIters = 
      new LinkedList();
    
    if (argOptions.hasOption('R'))
      addCorpusReaderIterators(
        docIters, argOptions.getStringOption('R').split(","));
    if (argOptions.hasOption('f'))
      addFileIterators(
        docIters, argOptions.getStringOption('f').split(","));
    if (argOptions.hasOption('d')) {
      addDocIterators(
        docIters, argOptions.getStringOption('d').split(","));
    }
    if (docIters.size() == 0) {
      throw new Error("Must specify document sources");
    }
    
    Iterator<Document> docIter = new CombinedIterator(docIters);
    

    if (argOptions.hasOption("docLimit")) {
      return new LimitedIterator(
        docIter, argOptions.getIntOption("docLimit"));
    }
    
    return docIter;
  }
  




  protected void addCorpusReaderIterators(Collection<Iterator<Document>> docIters, String[] fileNames)
    throws IOException
  {
    CorpusReader<Document> reader = 
      (CorpusReader)ReflectionUtil.getObjectInstance(fileNames[0]);
    for (int i = 1; i < fileNames.length; i++) {
      docIters.add(reader.read(new File(fileNames[0])));
    }
  }
  


  protected void addFileIterators(Collection<Iterator<Document>> docIters, String[] fileNames)
    throws IOException
  {
    for (String s : fileNames) {
      docIters.add(new FileListDocumentIterator(s));
    }
  }
  


  protected void addDocIterators(Collection<Iterator<Document>> docIters, String[] fileNames)
    throws IOException
  {
    for (String s : fileNames) {
      docIters.add(new OneLinePerDocumentIterator(s));
    }
  }
  




  public void run(String[] args)
    throws Exception
  {
    if (args.length == 0) {
      usage();
      System.exit(1);
    }
    argOptions.parseOptions(args);
    
    if (argOptions.numPositionalArgs() == 0) {
      throw new IllegalArgumentException("must specify output path");
    }
    
    verbose = ((argOptions.hasOption('v')) || (argOptions.hasOption("verbose")));
    



    if (verbose) {
      LoggerUtil.setLevel(Level.FINE);
    }
    

    int numThreads = isMultiThreaded ? 
      Runtime.getRuntime().availableProcessors() : 
      1;
    if (argOptions.hasOption("threads")) {
      numThreads = argOptions.getIntOption("threads");
    }
    

    WorkQueue.getWorkQueue(numThreads);
    
    boolean overwrite = true;
    if (argOptions.hasOption("overwrite")) {
      overwrite = argOptions.getBooleanOption("overwrite");
    }
    
    handleExtraOptions();
    
    Properties props = setupProperties();
    


    if (argOptions.hasOption("tokenFilter")) {
      props.setProperty("edu.ucla.sspace.text.TokenizerFactory.tokenFilter", 
        argOptions.getStringOption("tokenFilter"));
    }
    

    if (argOptions.hasOption("stemmingAlgorithm")) {
      props.setProperty("edu.ucla.sspace.text.TokenizerFactory.stemmer", 
        argOptions.getStringOption("stemmingAlgorithm"));
    }
    if (argOptions.hasOption("compoundWords")) {
      props.setProperty("edu.ucla.sspace.text.TokenizerFactory.compoundTokens", 
        argOptions.getStringOption("compoundWords"));
    }
    if (argOptions.hasOption("wordLimit")) {
      props.setProperty("edu.ucla.sspace.text.TokenizerFactory.tokenCountLimit", 
        argOptions.getStringOption("wordLimit"));
    }
    
    SemanticSpaceIO.SSpaceFormat format = argOptions.hasOption("outputFormat") ? 
      SemanticSpaceIO.SSpaceFormat.valueOf(
      argOptions.getStringOption("outputFormat").toUpperCase()) : 
      getSpaceFormat();
    
    IteratorFactory.setProperties(props);
    



    SemanticSpace space = getSpace();
    

    Iterator<Document> docIter = getDocumentIterator();
    
    processDocumentsAndSpace(space, docIter, numThreads, props);
    
    File outputPath = new File(argOptions.getPositionalArg(0));
    File outputFile = null;
    

    if (outputPath.isDirectory()) {
      outputFile = overwrite ? 
        new File(outputPath, space.getSpaceName() + ".sspace") : 
        File.createTempFile(space.getSpaceName(), ".sspace", outputPath);




    }
    else if ((outputPath.exists()) && (!overwrite))
    {

      String name = outputPath.getName();
      int dotIndex = name.lastIndexOf(".");
      String extension = (dotIndex < 0) && (dotIndex + 1 < name.length()) ? 
        "" : name.substring(dotIndex);
      String baseName = name.substring(0, dotIndex);
      


      if (baseName.length() < 3)
        baseName = baseName + Math.abs(Math.random() * 32767.0D * 10.0D);
      File outputDir = outputPath.getParentFile();
      

      if (outputDir == null)
        outputDir = new File(".");
      verbose("base dir: " + outputDir);
      outputFile = File.createTempFile(baseName, extension, outputDir);
    }
    else {
      outputFile = outputPath;
    }
    
    System.out.println("output File: " + outputFile);
    
    long startTime = System.currentTimeMillis();
    saveSSpace(space, outputFile, format);
    long endTime = System.currentTimeMillis();
    verbose("printed space in %.3f seconds", new Object[] {
      Double.valueOf((endTime - startTime) / 1000.0D) });
    
    postProcessing();
  }
  




  protected void saveSSpace(SemanticSpace sspace, File outputFile, SemanticSpaceIO.SSpaceFormat format)
    throws IOException
  {
    SemanticSpaceIO.save(sspace, outputFile, format);
  }
  




  protected void processDocumentsAndSpace(SemanticSpace space, Iterator<Document> docIter, int numThreads, Properties props)
    throws Exception
  {
    parseDocumentsMultiThreaded(space, docIter, numThreads);
    
    long startTime = System.currentTimeMillis();
    space.processSpace(props);
    long endTime = System.currentTimeMillis();
    verbose("processed space in %.3f seconds", new Object[] {
      Double.valueOf((endTime - startTime) / 1000.0D) });
  }
  









  protected void parseDocumentsSingleThreaded(SemanticSpace sspace, Iterator<Document> docIter)
    throws IOException
  {
    long processStart = System.currentTimeMillis();
    int count = 0;
    
    while (docIter.hasNext()) {
      long startTime = System.currentTimeMillis();
      Document doc = (Document)docIter.next();
      count++;int docNumber = count;
      int terms = 0;
      sspace.processDocument(doc.reader());
      long endTime = System.currentTimeMillis();
      verbose("processed document #%d in %.3f seconds", new Object[] {
        Integer.valueOf(docNumber), Double.valueOf((endTime - startTime) / 1000.0D) });
    }
    
    verbose("Processed all %d documents in %.3f total seconds", new Object[] {
      Integer.valueOf(count), 
      Double.valueOf((System.currentTimeMillis() - processStart) / 1000.0D) });
  }
  












  protected void parseDocumentsMultiThreaded(final SemanticSpace sspace, final Iterator<Document> docIter, int numThreads)
    throws IOException, InterruptedException
  {
    Collection<Thread> threads = new LinkedList();
    
    final AtomicInteger count = new AtomicInteger(0);
    
    WorkQueue queue = WorkQueue.getWorkQueue(numThreads);
    Object key = queue.registerTaskGroup(numThreads);
    
    long processStart = System.currentTimeMillis();
    verbose("Beginning processing using %d threads", new Object[] { Integer.valueOf(numThreads) });
    for (int i = 0; i < numThreads; i++) {
      queue.add(key, new Runnable()
      {
        public void run()
        {
          while (docIter.hasNext()) {
            long startTime = System.currentTimeMillis();
            Document doc = (Document)docIter.next();
            int docNumber = count.incrementAndGet();
            int terms = 0;
            try {
              sspace.processDocument(doc.reader());
            } catch (Throwable t) {
              t.printStackTrace();
            }
            long endTime = System.currentTimeMillis();
            verbose("parsed document #%d in %.3f seconds", new Object[] {
              Integer.valueOf(docNumber), Double.valueOf((endTime - startTime) / 1000.0D) });
          }
        }
      });
    }
    
    queue.await(key);
    
    verbose("Processed all %d documents in %.3f total seconds", new Object[] {
      Integer.valueOf(count.get()), 
      Double.valueOf((System.currentTimeMillis() - processStart) / 1000.0D) });
  }
  




  protected static Set<String> loadValidTermSet(String validTermsFileName)
    throws IOException
  {
    Set<String> validTerms = new HashSet();
    BufferedReader br = new BufferedReader(
      new FileReader(validTermsFileName));
    
    for (String line = null; (line = br.readLine()) != null;) {
      validTerms.add(line);
    }
    
    br.close();
    
    return validTerms;
  }
  
  protected void verbose(String msg) {
    if (LOGGER.isLoggable(Level.FINE))
      LOGGER.logp(Level.FINE, getClass().getName(), "verbose", msg);
  }
  
  protected void verbose(String format, Object... args) {
    if (LOGGER.isLoggable(Level.FINE)) {
      LOGGER.logp(Level.FINE, getClass().getName(), "verbose", 
        String.format(format, args));
    }
  }
}
