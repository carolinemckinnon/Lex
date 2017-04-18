package edu.ucla.sspace.tools;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.statistics.ChiSquaredTest;
import edu.ucla.sspace.common.statistics.GTest;
import edu.ucla.sspace.common.statistics.PointwiseMutualInformationTest;
import edu.ucla.sspace.common.statistics.SignificanceTest;
import edu.ucla.sspace.text.BufferedFileListDocumentIterator;
import edu.ucla.sspace.text.Document;
import edu.ucla.sspace.text.OneLinePerDocumentIterator;
import edu.ucla.sspace.text.StringUtils;
import edu.ucla.sspace.text.WordIterator;
import edu.ucla.sspace.util.CombinedIterator;
import edu.ucla.sspace.util.Counter;
import edu.ucla.sspace.util.LoggerUtil;
import edu.ucla.sspace.util.ObjectCounter;
import edu.ucla.sspace.util.Pair;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


























































public class IterativeBigramExtractor
{
  private static final Logger LOGGER = Logger.getLogger(IterativeBigramExtractor.class.getName());
  
  public IterativeBigramExtractor() {}
  
  public static void main(String[] args) throws Exception { ArgOptions options = new ArgOptions();
    
    options.addOption('f', "fileList", "a list of document files", 
      true, "FILE[,FILE...]", "Required (at least one of)");
    options.addOption('d', "docFile", 
      "a file where each line is a document", true, 
      "FILE[,FILE...]", "Required (at least one of)");
    
    options.addOption('s', "stopWords", "A file containing a list of stop words that should be encluded from bigrams", 
    
      true, "FILE", "Program Options");
    

    options.addOption('h', "help", "Generates a help message and exits", 
      false, null, "Program Options");
    options.addOption('v', "verbose", "Turns on verbose output", 
      false, null, "Program Options");
    options.addOption('V', "veryVerbose", "Turns on *very* verbose output", 
      false, null, "Program Options");
    
    options.addOption('n', "numberOfTermsPerIteration", "Specifies the number of terms to compute the association between per iteration (default: all)", 
    

      true, "INT", "Runtime Options");
    options.addOption('F', "filterAssociationBelow", "Specifies the an association score below which the pair will not be reported", 
    

      true, "DOUBLE", "Runtime Options");
    
    options.parseOptions(args);
    

    if (options.hasOption('v'))
      LoggerUtil.setLevel(Level.FINE);
    if (options.hasOption('V'))
      LoggerUtil.setLevel(Level.FINER);
    if ((options.numPositionalArgs() < 3) || (options.hasOption("help"))) {
      usage(options);
      return;
    }
    
    File termsFile = new File(options.getPositionalArg(0));
    String outputPrefix = options.getPositionalArg(1);
    
    Set<String> terms = StringUtils.loadFileAsSet(termsFile);
    
    Set<String> stopWords = null;
    if (options.hasOption('s')) {
      stopWords = StringUtils.loadFileAsSet(
        new File(options.getStringOption('s')));
    }
    


    Map<SignificanceTest, Double> tests = 
      new HashMap();
    Map<SignificanceTest, PrintWriter> testWriters = 
      new HashMap();
    
    int numArgs = options.numPositionalArgs();
    for (int i = 2; i < numArgs; i++) {
      String testName = options.getPositionalArg(i);
      SignificanceTest test = getTest(testName);
      Double minWeight = null;
      if (i + 1 < numArgs)
      {
        String weightStr = options.getPositionalArg(i + 1);
        try
        {
          minWeight = Double.valueOf(Double.parseDouble(weightStr));
        } catch (NumberFormatException localNumberFormatException) {}
        i++;
      }
      tests.put(test, minWeight);
      PrintWriter pw = new PrintWriter(outputPrefix + testName + ".txt");
      testWriters.put(test, pw);
    }
    
    int termsToUsePerIteration = options.hasOption('n') ? 
      options.getIntOption('n') : 
      terms.size();
    
    Queue<String> termsToAssociate = new ArrayDeque(terms);
    int round = 0;
    Counter<Pair<String>> bigramCounts; Iterator<String> tokens; for (; termsToAssociate.size() > 0; 
        












































































        tokens.hasNext())
    {
      round++;
      
      Counter<String> termCounts = new ObjectCounter();
      bigramCounts = 
        new ObjectCounter();
      int allBigramCounts = 0;
      

      Set<String> curTerms = new HashSet();
      while ((curTerms.size() < termsToUsePerIteration) && 
        (!termsToAssociate.isEmpty())) {
        curTerms.add((String)termsToAssociate.poll());
      }
      
      LoggerUtil.info(LOGGER, "Finding associations between all %d terms and a %d term subset (%d remain)", new Object[] {
        Integer.valueOf(terms.size()), Integer.valueOf(curTerms.size()), 
        Integer.valueOf(termsToAssociate.size()) });
      
      int docNum = 0;
      long startTime = System.currentTimeMillis();
      Iterator<Document> docs = getDocuments(options);
      while (docs.hasNext()) {
        Document doc = (Document)docs.next();
        tokens = new WordIterator(doc.reader());
        String t1 = null;
        while (tokens.hasNext()) {
          String t2 = (String)tokens.next();
          

          if ((terms.contains(t2)) && (
            (stopWords == null) || (!stopWords.contains(t2))))
          {
            termCounts.count(t2);
            if (t1 != null) {
              allBigramCounts++;
              
              if (curTerms.contains(t1))
                bigramCounts.count(new Pair(t1, t2));
            }
          }
          t1 = t2;
        }
        

        docNum++; if (docNum % 1000 == 0) {
          double now = System.currentTimeMillis();
          double docsSec = docNum / ((now - startTime) / 1000.0D);
          LoggerUtil.verbose(LOGGER, "Processed document %d in round %d, docs/sec: %f", new Object[] {
            Integer.valueOf(docNum), Integer.valueOf(round), Double.valueOf(docsSec) });
        }
      }
      Iterator localIterator;
      for (tokens = tests.entrySet().iterator(); tokens.hasNext(); 
          



          localIterator.hasNext())
      {
        Map.Entry<SignificanceTest, Double> e = (Map.Entry)tokens.next();
        SignificanceTest test = (SignificanceTest)e.getKey();
        double minWeight = e.getValue() == null ? 0.0D : ((Double)e.getValue()).doubleValue();
        PrintWriter pw = (PrintWriter)testWriters.get(test);
        
        localIterator = bigramCounts.iterator(); continue;Map.Entry<Pair<String>, Integer> e2 = (Map.Entry)localIterator.next();
        Pair<String> bigram = (Pair)e2.getKey();
        int bigramCount = ((Integer)e2.getValue()).intValue();
        String t1 = (String)x;
        String t2 = (String)y;
        int t1Count = termCounts.getCount(t1);
        int t2Count = termCounts.getCount(t2);
        int t1butNotT2 = t1Count - bigramCount;
        int t2butNotT1 = t2Count - bigramCount;
        int neitherAppeared = 
          allBigramCounts - (t1Count + t2Count - bigramCount);
        
        double score = test.score(bigramCount, t1butNotT2, 
          t2butNotT1, neitherAppeared);
        if ((score > minWeight) && (!Double.isNaN(score)) && 
          (!Double.isInfinite(score))) {
          pw.println(t1 + "\t" + t2 + "\t" + score);
        }
      }
      
      tokens = testWriters.values().iterator(); continue;PrintWriter pw = (PrintWriter)tokens.next();
      pw.flush();
    }
    
    for (PrintWriter pw : testWriters.values()) {
      pw.close();
    }
  }
  
  private static Iterator<Document> getDocuments(ArgOptions argOptions) throws IOException
  {
    Collection<Iterator<Document>> docIters = 
      new LinkedList();
    
    if (argOptions.hasOption('f')) {
      for (String s : argOptions.getStringOption('f').split(","))
        docIters.add(new BufferedFileListDocumentIterator(s));
    }
    if (argOptions.hasOption('d')) {
      for (String s : argOptions.getStringOption('d').split(",")) {
        docIters.add(new OneLinePerDocumentIterator(s));
      }
    }
    if (docIters.size() == 0) {
      throw new IllegalStateException(
        "Must specify at least one document source");
    }
    
    Iterator<Document> docIter = new CombinedIterator(docIters);
    
    return docIter;
  }
  





  private static Set<String> clean(String document, Set<String> validContext)
  {
    Set<String> tokens = new HashSet();
    String[] arr = document.split("\\s+");
    


    if (validContext.isEmpty()) {
      tokens.addAll(Arrays.asList(arr));
      return tokens;
    }
    
    for (String token : arr) {
      if (validContext.contains(token))
        tokens.add(token);
    }
    return tokens;
  }
  
  private static SignificanceTest getTest(String testName) {
    if (testName.equals("g-test"))
      return new GTest();
    if (testName.equals("chi-squared"))
      return new ChiSquaredTest();
    if (testName.equals("pmi")) {
      return new PointwiseMutualInformationTest();
    }
    throw new IllegalArgumentException(
      "No such significance test: " + testName);
  }
  




  private static void usage(ArgOptions options)
  {
    System.out.println(
      "IterativeBigramExtractor version 1.0\nusage: java IterativeBigramExtractor [options] terms.txt output-prefix test-name [min-weight] [test2-name [min-weight]]\n\n" + 
      


      options.prettyPrint());
  }
}
