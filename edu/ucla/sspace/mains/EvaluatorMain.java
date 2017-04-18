package edu.ucla.sspace.mains;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.SemanticSpaceIO;
import edu.ucla.sspace.common.Similarity.SimType;
import edu.ucla.sspace.evaluation.NormedWordPrimingReport;
import edu.ucla.sspace.evaluation.NormedWordPrimingTest;
import edu.ucla.sspace.evaluation.WordChoiceEvaluation;
import edu.ucla.sspace.evaluation.WordChoiceEvaluationRunner;
import edu.ucla.sspace.evaluation.WordChoiceReport;
import edu.ucla.sspace.evaluation.WordPrimingReport;
import edu.ucla.sspace.evaluation.WordPrimingTest;
import edu.ucla.sspace.evaluation.WordSimilarityEvaluation;
import edu.ucla.sspace.evaluation.WordSimilarityEvaluationRunner;
import edu.ucla.sspace.evaluation.WordSimilarityReport;
import edu.ucla.sspace.text.WordIterator;
import edu.ucla.sspace.util.LoggerUtil;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

















































public class EvaluatorMain
{
  private static final Logger LOGGER = Logger.getLogger(EvaluatorMain.class.getName());
  




  private final ArgOptions argOptions;
  




  private Collection<WordChoiceEvaluation> wordChoiceTests;
  



  private Collection<WordSimilarityEvaluation> wordSimilarityTests;
  



  private Collection<WordPrimingTest> wordPrimingTests;
  



  private Collection<NormedWordPrimingTest> normedPrimingTests;
  



  private ResultReporter reporter;
  



  private PrintStream resultWriter;
  




  public EvaluatorMain()
  {
    argOptions = new ArgOptions();
    addOptions();
  }
  



  protected void addOptions()
  {
    argOptions.addOption('c', "wordChoice", 
      "a list of WordChoiceEvaluation class names and their data files", 
      
      true, "CLASS=FILE[=FILE2...][,CLASS=FILE...]", 
      "Required (at least one of)");
    argOptions.addOption('s', "wordSimilarity", 
      "a list of WordSimilarityEvaluation class names", 
      true, "CLASS=FILE[=FILE2...][,CLASS=FILE...]", 
      "Required (at least one of)");
    argOptions.addOption('p', "wordPriming", 
      "a list of WordPrimingTest class names", 
      true, "CLASS[=FILE][=FILE2...][,CLASS=FILE...]", 
      "Required (at least one of)");
    argOptions.addOption('n', "normedPriming", 
      "a list of NormedWordPrimingTest class names", 
      true, "CLASS[=FILE][=FILE2...][,CLASS=FILE...]", 
      "Required (at least one of)");
    argOptions.addOption('g', "testConfiguration", 
      "a file containing a list of test configurations to run", 
      
      true, "FILE", "Required (at least one of)");
    

    argOptions.addOption('l', "latexOutput", 
      "writes the results to a file in a latex table format", 
      
      false, null, "Program Options");
    
    argOptions.addOption('o', "outputFile", 
      "writes the results to this file", 
      true, "FILE", "Program Options");
    argOptions.addOption('t', "threads", 
      "the number of threads to use", 
      true, "INT", "Program Options");
    argOptions.addOption('v', "verbose", 
      "prints verbose output", 
      false, null, "Program Options");
  }
  
  public void run(String[] args) throws Exception {
    if (args.length == 0) {
      usage();
      System.exit(1);
    }
    argOptions.parseOptions(args);
    
    if (argOptions.hasOption("verbose")) {
      LoggerUtil.setLevel(Level.FINE);
    }
    
    String wcTests = argOptions.hasOption("wordChoice") ? 
      argOptions.getStringOption("wordChoice") : 
      null;
    
    String wsTests = argOptions.hasOption("wordSimilarity") ? 
      argOptions.getStringOption("wordSimilarity") : 
      null;
    
    String wpTests = argOptions.hasOption("wordPriming") ? 
      argOptions.getStringOption("wordPriming") : 
      null;
    
    String npTests = argOptions.hasOption("normedPriming") ? 
      argOptions.getStringOption("normedPriming") : 
      null;
    
    String configFile = argOptions.hasOption("testConfiguration") ? 
      argOptions.getStringOption("testConfiguration") : 
      null;
    

    if ((wcTests == null) && (wsTests == null) && 
      (wpTests == null) && (configFile == null)) {
      usage();
      System.out.println("no tests specified");
      System.exit(1);
    }
    

    wordChoiceTests = (wcTests == null ? 
      new LinkedList() : 
      loadWordChoiceEvaluations(wcTests));
    

    wordSimilarityTests = (wsTests == null ? 
      new LinkedList() : 
      loadWordSimilarityEvaluations(wsTests));
    
    resultWriter = (argOptions.hasOption("outputFile") ? 
      new PrintStream(argOptions.getStringOption("outputFile")) : 
      System.out);
    
    reporter = (argOptions.hasOption('l') ? 
      new LatexReporter() : 
      new DefaultReporter(null));
    

    wordPrimingTests = (wpTests == null ? 
      new LinkedList() : 
      loadWordPrimingTests(wpTests));
    

    normedPrimingTests = (npTests == null ? 
      new LinkedList() : 
      loadNormedPrimingTests(npTests));
    



    if (configFile != null) {
      WordIterator it = new WordIterator(new BufferedReader(
        new FileReader(configFile)));
      while (it.hasNext()) {
        String className = it.next();
        if (!it.hasNext()) {
          throw new Error("test is not matched with data file: " + 
            className);
        }
        String[] dataFiles = it.next().split(",");
        

        Class<?> clazz = Class.forName(className);
        Class[] constructorArgs = new Class[dataFiles.length];
        for (int i = 0; i < constructorArgs.length; i++)
          constructorArgs[i] = String.class;
        Constructor<?> c = clazz.getConstructor(constructorArgs);
        System.out.println(className);
        Object o = c.newInstance(dataFiles);
        

        if ((o instanceof WordChoiceEvaluation)) {
          wordChoiceTests.add((WordChoiceEvaluation)o);
          verbose("Loaded word choice test " + className);
        }
        else if ((o instanceof WordSimilarityEvaluation)) {
          wordSimilarityTests.add((WordSimilarityEvaluation)o);
          verbose("Loaded word similarity test " + className);
        }
        else if ((o instanceof WordPrimingTest)) {
          wordPrimingTests.add((WordPrimingTest)o);
          verbose("Loaded word priming test " + className);
        }
        else if ((o instanceof NormedWordPrimingTest)) {
          normedPrimingTests.add((NormedWordPrimingTest)o);
          verbose("Loaded normed word priming test " + className);
        }
        else {
          throw new IllegalStateException(
            "provided class is not an known Evaluation class type: " + 
            className);
        }
      }
    }
    


    Set<String> loadedSSpaces = new HashSet();
    int spaces = argOptions.numPositionalArgs();
    for (int i = 0; i < spaces; i++) {
      SemanticSpace sspace = null;
      String[] sspaceConfig = argOptions.getPositionalArg(i).split(",");
      String sspaceFileName = sspaceConfig[0];
      Similarity.SimType comparisonFunction = Similarity.SimType.COSINE;
      if (sspaceConfig.length > 1) {
        for (int j = 1; j < sspaceConfig.length; j++) {
          String setting = sspaceConfig[j];
          if (j > 2) {
            throw new IllegalStateException(
              "too many .sspace file arguments:" + 
              argOptions.getPositionalArg(i));
          }
          if (setting.startsWith("function")) {
            comparisonFunction = 
              Similarity.SimType.valueOf(setting.substring(10));
          }
          else {
            throw new IllegalArgumentException(
              "unknown sspace parameter: " + setting);
          }
        }
      }
      


      if (!loadedSSpaces.contains(sspace)) {
        verbose("Loading semantic space: " + sspaceFileName);
        sspace = SemanticSpaceIO.load(sspaceFileName);
        loadedSSpaces.add(sspaceFileName);
        verbose("Done loading.");
        
        verbose("Evaluating semantic space: " + sspaceFileName);
        evaluateSemanticSpace(sspace, comparisonFunction);
        verbose("Done evaluating.");
      }
    }
    reporter.printResults();
  }
  




  private void evaluateSemanticSpace(SemanticSpace sspace, Similarity.SimType similarity)
  {
    String[] results = new String[wordChoiceTests.size() + 
      wordSimilarityTests.size() + 
      wordPrimingTests.size() + 
      normedPrimingTests.size()];
    int resultIndex = 0;
    

    for (WordChoiceEvaluation wordChoice : wordChoiceTests) {
      WordChoiceReport report = WordChoiceEvaluationRunner.evaluate(
        sspace, wordChoice, similarity);
      verbose("Results for %s:%n%s%n", new Object[] { wordChoice, report });
      results[(resultIndex++)] = String.format("%4.3f", new Object[] { Double.valueOf(report.score()) });
    }
    


    ??? = wordSimilarityTests.iterator();
    while (???.hasNext()) {
      WordSimilarityEvaluation wordSimilarity = (WordSimilarityEvaluation)???.next();
      WordSimilarityReport report = 
        WordSimilarityEvaluationRunner.evaluate(
        sspace, wordSimilarity, similarity);
      verbose("Results for %s:%n%s%n", new Object[] { wordSimilarity, report });
      results[(resultIndex++)] = String.format(
        "%4.3f", new Object[] { Double.valueOf(report.correlation()) });
    }
    

    for (WordPrimingTest wordPrimingTest : wordPrimingTests) {
      WordPrimingReport report = wordPrimingTest.evaluate(sspace);
      verbose("Results for %s:%n%s%n", new Object[] { wordPrimingTest, report });
      results[(resultIndex++)] = String.format("%4.3f & %4.3f & %4.3f", new Object[] {
        Double.valueOf(report.relatedPriming()), 
        Double.valueOf(report.unrelatedPriming()), 
        Double.valueOf(report.effect()) });
    }
    

    for (NormedWordPrimingTest normedPrimingTest : normedPrimingTests) {
      NormedWordPrimingReport report = normedPrimingTest.evaluate(sspace);
      verbose("Results for %s:%n%s%n", new Object[] { normedPrimingTest, report });
      results[(resultIndex++)] = String.format(
        "%4.3f", new Object[] { Double.valueOf(report.averageCorrelation()) });
    }
    
    reporter.addResults(sspace.getSpaceName(), 
      similarity.toString(), results);
  }
  


  protected void verbose(String msg)
  {
    LOGGER.fine(msg);
  }
  


  protected void verbose(String format, Object... args)
  {
    LOGGER.fine(String.format(format, args));
  }
  


  public void usage()
  {
    System.out.println(
      "java EvaluatorMain " + 
      argOptions.prettyPrint() + 
      "<sspace-file>[,format=SSpaceFormat[,function=SimType]] " + 
      "[<sspace-file>...]\n\n" + 
      "The .sspace file arguments may have option specifications that " + 
      "indicate\n what vector " + 
      "comparison method\n" + 
      "should be used (default COSINE).  Users should specify the name " + 
      "of a\n" + 
      "Similarity.SimType.  A single .sspace can be evaluated with " + 
      "multiple\n" + 
      "comparison functions by specifying the file multiple times on " + 
      "the command\n" + 
      "line.  The .sspace file will be loaded only once.\n\n" + 
      "A test configuration file is a series of fully qualified class " + 
      "names of evaluations\nthat should be run followed by the data" + 
      "files that contains\nthe test information, comma separated");
  }
  

  public static void main(String[] args)
  {
    try
    {
      new EvaluatorMain().run(args);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  




  private Collection<WordChoiceEvaluation> loadWordChoiceEvaluations(String wcTests)
  {
    String[] testsAndFiles = wcTests.split(",");
    Collection<WordChoiceEvaluation> wordChoiceTests = 
      new LinkedList();
    try {
      for (String s : testsAndFiles) {
        String[] testAndFile = s.split("=");
        Class<?> clazz = Class.forName(testAndFile[0]);
        


        Class[] constructorArgs = new Class[testAndFile.length - 1];
        for (int i = 0; i < constructorArgs.length; i++)
          constructorArgs[i] = String.class;
        Constructor<?> c = clazz.getConstructor(constructorArgs);
        Object[] args = new String[testAndFile.length - 1];
        for (int i = 1; i < testAndFile.length; i++)
          args[(i - 1)] = testAndFile[i];
        WordChoiceEvaluation eval = 
          (WordChoiceEvaluation)c.newInstance(args);
        
        verbose("Loaded word choice test " + testAndFile[0]);
        wordChoiceTests.add(eval);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return wordChoiceTests;
  }
  




  private Collection<WordSimilarityEvaluation> loadWordSimilarityEvaluations(String wcTests)
  {
    String[] testsAndFiles = wcTests.split(",");
    Collection<WordSimilarityEvaluation> wordSimTests = 
      new LinkedList();
    try {
      for (String s : testsAndFiles) {
        String[] testAndFile = s.split("=");
        Class<?> clazz = Class.forName(testAndFile[0]);
        

        Class[] constructorArgs = new Class[testAndFile.length - 1];
        for (int i = 0; i < constructorArgs.length; i++)
          constructorArgs[i] = String.class;
        Constructor<?> c = clazz.getConstructor(constructorArgs);
        Object[] args = new String[testAndFile.length - 1];
        for (int i = 1; i < testAndFile.length; i++)
          args[(i - 1)] = testAndFile[i];
        WordSimilarityEvaluation eval = 
          (WordSimilarityEvaluation)c.newInstance(args);
        verbose("Loaded word similarity test " + testAndFile[0]);
        wordSimTests.add(eval);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return wordSimTests;
  }
  











  private class DefaultReporter
    implements EvaluatorMain.ResultReporter
  {
    private DefaultReporter() {}
    











    public void addResults(String sspaceName, String simType, String[] results)
    {
      int index = 0;
      for (WordChoiceEvaluation choice : wordChoiceTests) {
        resultWriter.printf(
          "Result for sspace %s-%s on synonymy test %s: %s\n", new Object[] {
          sspaceName, simType, choice, results[(index++)] });
      }
      for (WordSimilarityEvaluation similarity : wordSimilarityTests) {
        resultWriter.printf(
          "Result for sspace %s-%s on similarity test %s: %s\n", new Object[] {
          sspaceName, simType, similarity, results[(index++)] });
      }
      for (WordPrimingTest priming : wordPrimingTests) {
        resultWriter.printf(
          "Result for sspace %s-%s on priming test %s: %s\n", new Object[] {
          sspaceName, simType, priming, results[(index++)] });
      }
      for (NormedWordPrimingTest priming : normedPrimingTests) {
        resultWriter.printf(
          "Result for sspace %s-%s on priming test %s: %s\n", new Object[] {
          sspaceName, simType, priming, results[(index++)] });
      }
    }
    

    public void printResults()
    {
      resultWriter.close();
    }
  }
  




  private class LatexReporter
    implements EvaluatorMain.ResultReporter
  {
    private List<String> titleList;
    



    private List<String[]> resultList;
    



    public LatexReporter()
    {
      titleList = new ArrayList();
      resultList = new ArrayList();
    }
    




    public void addResults(String sspaceName, String simType, String[] results)
    {
      titleList.add(sspaceName + "-" + simType);
      resultList.add(results);
    }
    



    public void printResults()
    {
      StringBuilder sb = new StringBuilder();
      sb.append("SSpace Name  ");
      for (WordChoiceEvaluation choice : wordChoiceTests)
        sb.append("  &  ").append(choice.toString());
      for (WordSimilarityEvaluation similarity : wordSimilarityTests)
        sb.append("  &  ").append(similarity.toString());
      for (WordPrimingTest priming : wordPrimingTests)
        sb.append("  &  ").append(priming.toString());
      for (NormedWordPrimingTest priming : normedPrimingTests)
        sb.append("  &  ").append(priming.toString());
      sb.append("  \\");
      resultWriter.println(sb.toString());
      

      for (int i = 0; i < titleList.size(); i++) {
        resultWriter.printf("%s ", new Object[] { titleList.get(i) });
        String[] results = (String[])resultList.get(i);
        for (String result : results)
          resultWriter.printf("  &  %s", new Object[] { result });
        resultWriter.printf("  \\\\\n", new Object[0]);
      }
      resultWriter.close();
    }
  }
  



  private Collection<WordPrimingTest> loadWordPrimingTests(String wpTests)
  {
    String[] testsAndFiles = wpTests.split(",");
    Collection<WordPrimingTest> wordPrimingTests = 
      new LinkedList();
    try {
      for (String s : testsAndFiles) {
        String[] testAndFile = s.split("=");
        Class<?> clazz = Class.forName(testAndFile[0]);
        

        Class[] constructorArgs = new Class[testAndFile.length - 1];
        for (int i = 0; i < constructorArgs.length; i++)
          constructorArgs[i] = String.class;
        Constructor<?> c = clazz.getConstructor(constructorArgs);
        Object[] args = new String[testAndFile.length - 1];
        for (int i = 1; i < testAndFile.length; i++)
          args[(i - 1)] = testAndFile[i];
        WordPrimingTest eval = (WordPrimingTest)c.newInstance(args);
        verbose("Loaded word priming test " + testAndFile[0]);
        wordPrimingTests.add(eval);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return wordPrimingTests;
  }
  




  private Collection<NormedWordPrimingTest> loadNormedPrimingTests(String wpTests)
  {
    String[] testsAndFiles = wpTests.split(",");
    Collection<NormedWordPrimingTest> wordPrimingTests = 
      new LinkedList();
    try {
      for (String s : testsAndFiles) {
        String[] testAndFile = s.split("=");
        Class<?> clazz = Class.forName(testAndFile[0]);
        

        Class[] constructorArgs = new Class[testAndFile.length - 1];
        for (int i = 0; i < constructorArgs.length; i++)
          constructorArgs[i] = String.class;
        Constructor<?> c = clazz.getConstructor(constructorArgs);
        Object[] args = new String[testAndFile.length - 1];
        for (int i = 1; i < testAndFile.length; i++)
          args[(i - 1)] = testAndFile[i];
        NormedWordPrimingTest eval = 
          (NormedWordPrimingTest)c.newInstance(args);
        verbose("Loaded normed word priming test " + testAndFile[0]);
        wordPrimingTests.add(eval);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return wordPrimingTests;
  }
  
  private static abstract interface ResultReporter
  {
    public abstract void addResults(String paramString1, String paramString2, String[] paramArrayOfString);
    
    public abstract void printResults();
  }
}
