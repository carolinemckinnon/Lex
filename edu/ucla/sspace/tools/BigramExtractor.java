package edu.ucla.sspace.tools;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.text.IteratorFactory;
import edu.ucla.sspace.util.LoggerUtil;
import edu.ucla.sspace.util.TrieMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;














































public class BigramExtractor
{
  public static enum SignificanceTest
  {
    CHI_SQUARED, 
    FISHERS_EXACT, 
    BARNARDS, 
    PMI, 
    LOG_LIKELIHOOD;
  }
  




  private static final Logger LOGGER = Logger.getLogger(BigramExtractor.class.getName());
  



  private final Map<String, TokenStats> tokenCounts;
  



  private final Map<Long, Number> bigramCounts;
  



  private int tokenIndexCounter;
  



  private int numBigramsInCorpus;
  




  public BigramExtractor()
  {
    this(1000);
  }
  



  public BigramExtractor(int expectedNumBigrams)
  {
    tokenCounts = new TrieMap();
    
    bigramCounts = new HashMap(expectedNumBigrams);
    tokenIndexCounter = 0;
    numBigramsInCorpus = 0;
  }
  



  private boolean excludeToken(String token)
  {
    return token.equals("");
  }
  





  public void process(String text)
  {
    process(IteratorFactory.tokenizeOrdered(text));
  }
  





  public void process(BufferedReader text)
  {
    process(IteratorFactory.tokenizeOrdered(text));
  }
  



  public void process(Iterator<String> text)
  {
    String nextToken = null;String curToken = null;
    

    if (text.hasNext())
      nextToken = (String)text.next();
    while (text.hasNext()) {
      curToken = nextToken;
      nextToken = (String)text.next();
      

      if ((!excludeToken(curToken)) && (!excludeToken(nextToken))) {
        processBigram(curToken, nextToken);
      }
    }
  }
  





  private void processBigram(String left, String right)
  {
    TokenStats leftStats = getStatsFor(left);
    TokenStats rightStats = getStatsFor(right);
    

    count += 1;
    count += 1;
    

    leftCount += 1;
    rightCount += 1;
    

    numBigramsInCorpus += 1;
    



    long bigram = index << 32 | index;
    Number curBigramCount = (Number)bigramCounts.get(Long.valueOf(bigram));
    int i = curBigramCount == null ? 1 : 1 + curBigramCount.intValue();
    


    Number val = null;
    if (i < 127) {
      val = Byte.valueOf((byte)i);
    } else if (i < 32767) {
      val = Short.valueOf((short)i);
    } else {
      val = Integer.valueOf(i);
    }
    bigramCounts.put(Long.valueOf(bigram), val);
  }
  
  private TokenStats getStatsFor(String token)
  {
    TokenStats stats = (TokenStats)tokenCounts.get(token);
    if (stats == null) {
      stats = new TokenStats(tokenIndexCounter++);
      tokenCounts.put(token, stats);
    }
    return stats;
  }
  




































































  public void printBigrams(PrintWriter output, SignificanceTest test, int minOccurrencePerToken)
  {
    String[] indexToToken = new String[tokenCounts.size()];
    for (Map.Entry<String, TokenStats> e : tokenCounts.entrySet()) {
      indexToToken[getValueindex] = ((String)e.getKey()).toString();
    }
    LOGGER.info("Number of bigrams: " + bigramCounts.size());
    
    for (Map.Entry<Long, Number> e : bigramCounts.entrySet())
    {
      long bigram = ((Long)e.getKey()).longValue();
      int firstTokenIndex = (int)(bigram >>> 32);
      int secondTokenIndex = (int)(bigram & 0xFFFFFFFF);
      int bigramCount = ((Number)e.getValue()).intValue();
      


      TokenStats t1 = (TokenStats)tokenCounts.get(indexToToken[firstTokenIndex]);
      TokenStats t2 = (TokenStats)tokenCounts.get(indexToToken[secondTokenIndex]);
      


      if ((count >= minOccurrencePerToken) && 
        (count >= minOccurrencePerToken))
      {

        int[] contingencyTable = getContingencyTable(t1, t2, bigramCount);
        double score = getScore(contingencyTable, test);
        
        output.println(score + " " + indexToToken[firstTokenIndex] + 
          " " + indexToToken[secondTokenIndex]);
      }
    }
  }
  






  private double getScore(int[] contingencyTable, SignificanceTest test)
  {
    switch (test) {
    case LOG_LIKELIHOOD: 
      return pmi(contingencyTable);
    case BARNARDS: 
      return chiSq(contingencyTable);
    case PMI: 
      return logLikelihood(contingencyTable);
    }
    throw new Error(test + " not implemented yet");
  }
  





  private double pmi(int[] contingencyTable)
  {
    int[] t = contingencyTable;
    
    double probOfBigram = t[0] / numBigramsInCorpus;
    double probOfFirstTok = (t[0] + t[2]) / numBigramsInCorpus;
    double probOfSecondTok = (t[0] + t[1]) / numBigramsInCorpus;
    
    return probOfBigram / (probOfFirstTok * probOfSecondTok);
  }
  



  private double chiSq(int[] contingencyTable)
  {
    int[] t = contingencyTable;
    int col1sum = t[0] + t[2];
    int col2sum = t[1] + t[3];
    int row1sum = t[0] + t[1];
    int row2sum = t[2] + t[3];
    double sum = row1sum + row2sum;
    

    double aExp = row1sum / sum * col1sum;
    double bExp = row1sum / sum * col2sum;
    double cExp = row2sum / sum * col1sum;
    double dExp = row2sum / sum * col2sum;
    

    return 
      (t[0] - aExp) * (t[0] - aExp) / aExp + 
      (t[1] - bExp) * (t[1] - bExp) / bExp + 
      (t[2] - cExp) * (t[2] - cExp) / cExp + 
      (t[3] - dExp) * (t[3] - dExp) / dExp;
  }
  



  private double logLikelihood(int[] contingencyTable)
  {
    int[] t = contingencyTable;
    int col1sum = t[0] + t[2];
    int col2sum = t[1] + t[3];
    int row1sum = t[0] + t[1];
    int row2sum = t[2] + t[3];
    double sum = row1sum + row2sum;
    

    double aExp = row1sum / sum * col1sum;
    double bExp = row1sum / sum * col2sum;
    double cExp = row2sum / sum * col1sum;
    double dExp = row2sum / sum * col2sum;
    
    return 2.0D * (
      t[0] * Math.log(t[0] - aExp) + 
      t[1] * Math.log(t[1] - bExp) + 
      t[2] * Math.log(t[2] - cExp) + 
      t[3] * Math.log(t[3] - dExp));
  }
  













  private int[] getContingencyTable(TokenStats leftTokenStats, TokenStats rightTokenStats, int bigramCount)
  {
    int leftTokenOnLeftInAnyBigram = leftCount;
    int rightTokenOnRightInAnyBigram = rightCount;
    
    int a = bigramCount;
    

    int b = rightTokenOnRightInAnyBigram - a;
    

    int c = leftTokenOnLeftInAnyBigram - a;
    

    int d = numBigramsInCorpus - (b + c + a);
    return new int[] { a, b, c, d };
  }
  
  public static void main(String[] args)
  {
    ArgOptions options = new ArgOptions();
    options.addOption('F', "tokenFilter", "filters to apply to the input token stream", 
      true, "FILTER_SPEC", 
      "Tokenizing Options");
    options.addOption('M', "minFreq", "minimum frequency of the reported bigrams", 
      true, "INT", 
      "Bigram Options");
    options.addOption('v', "verbose", 
      "Print verbose output about counting status", 
      false, null, "Program Options");
    options.parseOptions(args);
    
    if (options.numPositionalArgs() < 3) {
      System.out.println("usage: java BigramExtractor [options] <OutputFile> <SignificanceTest> <InputFile> [<InputFile>...]\n significance test options: " + 
      



        SignificanceTest.values() + "\n" + 
        options.prettyPrint());
      return;
    }
    
    if (options.hasOption("verbose")) {
      LoggerUtil.setLevel(Level.FINE);
    }
    
    Properties props = System.getProperties();
    

    if (options.hasOption("tokenFilter"))
      props.setProperty("edu.ucla.sspace.text.TokenizerFactory.tokenFilter", 
        options.getStringOption("tokenFilter"));
    IteratorFactory.setProperties(props);
    try
    {
      BigramExtractor be = new BigramExtractor(1000000);
      String testStr = options.getPositionalArg(1).toUpperCase();
      SignificanceTest test = SignificanceTest.valueOf(testStr);
      PrintWriter output = new PrintWriter(options.getPositionalArg(0));
      int numArgs = options.numPositionalArgs();
      
      for (int i = 2; i < numArgs; i++) {
        String inputFile = options.getPositionalArg(i);
        BufferedReader br = new BufferedReader(
          new FileReader(inputFile));
        
        int lineNo = 0;
        for (String line = null; (line = br.readLine()) != null;) {
          be.process(line);
          lineNo++; if (lineNo % 10000 == 0)
            LOGGER.fine(inputFile + 
              ": processed document " + lineNo);
        }
        br.close();
      }
      
      int minFreq = options.hasOption("minFreq") ? 
        options.getIntOption("minFreq") : 
        0;
      be.printBigrams(output, test, minFreq);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  






  private static class TokenStats
  {
    public int index;
    




    public int count;
    




    public int leftCount;
    




    public int rightCount;
    





    public TokenStats(int index)
    {
      this.index = index;
      count = 0;
      leftCount = 0;
      rightCount = 0;
    }
  }
}
