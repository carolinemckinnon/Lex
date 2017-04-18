package edu.ucla.sspace.mains;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.SemanticSpaceIO;
import edu.ucla.sspace.common.SemanticSpaceIO.SSpaceFormat;
import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.ri.IndexVectorUtil;
import edu.ucla.sspace.text.FileListTemporalDocumentIterator;
import edu.ucla.sspace.text.OneLinePerTemporalDocumentIterator;
import edu.ucla.sspace.text.TemporalDocument;
import edu.ucla.sspace.tri.FixedDurationTemporalRandomIndexing;
import edu.ucla.sspace.util.CombinedIterator;
import edu.ucla.sspace.util.MultiMap;
import edu.ucla.sspace.util.NearestNeighborFinder;
import edu.ucla.sspace.util.SimpleNearestNeighborFinder;
import edu.ucla.sspace.util.SortedMultiMap;
import edu.ucla.sspace.util.TimeSpan;
import edu.ucla.sspace.util.TreeMultiMap;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.Vector;
import edu.ucla.sspace.vector.Vectors;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;


























































public class FixedDurationTemporalRandomIndexingMain
{
  private static final String EXT = ".sspace";
  private static final Logger LOGGER = Logger.getLogger(FixedDurationTemporalRandomIndexingMain.class.getName());
  




  private final ArgOptions argOptions;
  




  private final Set<String> interestingWords;
  




  private boolean compareNeighbors;
  



  private SemanticSpaceIO.SSpaceFormat format;
  



  private int interestingWordNeighbors;
  



  private File outputDir;
  



  private boolean overwrite;
  



  private boolean printInterestingTokenShifts;
  



  private boolean savePartitions;
  



  private boolean printShiftRankings;
  



  private final Map<String, SortedMap<Long, double[]>> wordToTemporalSemantics;
  




  private FixedDurationTemporalRandomIndexingMain()
  {
    argOptions = createOptions();
    interestingWords = new HashSet();
    interestingWordNeighbors = 0;
    compareNeighbors = false;
    wordToTemporalSemantics = 
      new HashMap();
    savePartitions = false;
    printShiftRankings = false;
  }
  


  protected ArgOptions createOptions()
  {
    ArgOptions options = new ArgOptions();
    options.addOption('f', "fileList", "a list of document files", 
      true, "FILE[,FILE...]", "Required (at least one of)");
    options.addOption('d', "docFile", 
      "a file where each line is a document", true, 
      "FILE[,FILE...]", "Required (at least one of)");
    
    options.addOption('T', "timespan", "the timespan for each semantic partition", 
      true, "Date String", "Required");
    
    options.addOption('o', "outputFormat", "the .sspace format to use", 
      true, "{text|binary}", "Program Options");
    options.addOption('t', "threads", "the number of threads to use", 
      true, "INT", "Program Options");
    options.addOption('w', "overwrite", "specifies whether to overwrite the existing output", 
      true, "BOOL", 
      "Program Options");
    options.addOption('v', "verbose", "prints verbose output", 
      false, null, "Program Options");
    

    options.addOption('i', "vectorGenerator", "IndexVectorGenerator class to use", 
      true, 
      "CLASSNAME", "Algorithm Options");
    options.addOption('l', "vectorLength", "length of semantic vectors", 
      true, "INT", "Algorithm Options");
    options.addOption('n', "permutationFunction", "permutation function to use", 
      true, 
      "CLASSNAME", "Algorithm Options");
    options.addOption('p', "usePermutations", "whether to permute index vectors based on word order", 
      true, 
      "BOOL", "Algorithm Options");
    options.addOption('r', "useSparseSemantics", "use a sparse encoding of semantics to save memory", 
      true, 
      "BOOL", "Algorithm Options");
    options.addOption('s', "windowSize", "how many words to consider in each direction", 
      true, 
      "INT", "Algorithm Options");
    options.addOption('S', "saveVectors", "save word-to-IndexVector mapping after processing", 
      true, 
      "FILE", "Algorithm Options");
    options.addOption('L', "loadVectors", "load word-to-IndexVector mapping before processing", 
      true, 
      "FILE", "Algorithm Options");
    

    options.addOption('F', "tokenFilter", "filters to apply to the input token stream", 
      true, "FILTER_SPEC", 
      "Tokenizing Options");
    options.addOption('C', "compoundWords", "a file where each line is a recognized compound word", 
      true, "FILE", 
      "Tokenizing Options");
    
    options.addOption('W', "semanticFilter", "exclusive list of word", 
      true, "FILE", "Input Options");
    

    options.addOption('I', "interestingTokenList", "list of interesting words", 
      true, "FILE", "Output Options");
    options.addOption('K', "printShiftRankings", "print ranked list of semantic shifts for each interesting word", 
      false, 
      null, "Output Options");
    options.addOption('R', "savePartitions", "write semantic partitions as .sspace files to disk", 
      false, null, 
      "Output Options");
    options.addOption('P', "printInterestingTokenShifts", "prints the vectors for each interesting word", 
      false, null, 
      "Output Options");
    options.addOption('N', "printInterestingTokenNeighbors", "prints the nearest neighbors for each interesting word", 
      true, 
      "INT", "Output Options");
    options.addOption('Z', "printInterestingTokenNeighborComparison", 
      "prints the distances between each of thenearest neighbors for each interesting word", 
      
      false, null, "Output Options");
    
    return options;
  }
  
  public static void main(String[] args) {
    try {
      FixedDurationTemporalRandomIndexingMain main = 
        new FixedDurationTemporalRandomIndexingMain();
      main.run(args);
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }
  
  public void run(String[] args) throws Exception {
    if (args.length == 0) {
      usage();
      System.exit(1);
    }
    argOptions.parseOptions(args);
    
    if (argOptions.numPositionalArgs() == 0) {
      throw new IllegalArgumentException("must specify output directory");
    }
    
    outputDir = new File(argOptions.getPositionalArg(0));
    if (!outputDir.isDirectory()) {
      throw new IllegalArgumentException(
        "output directory is not a directory: " + outputDir);
    }
    
    if (!argOptions.hasOption("timespan")) {
      throw new IllegalArgumentException(
        "must specify a timespan duration for the semantic partition");
    }
    

    String timespanStr = argOptions.getStringOption("timespan");
    TimeSpan timeSpan = new TimeSpan(timespanStr);
    
    if ((argOptions.hasOption('v')) || (argOptions.hasOption("verbose")))
    {
      Logger appRooLogger = Logger.getLogger("edu.ucla.sspace");
      Handler verboseHandler = new ConsoleHandler();
      verboseHandler.setLevel(Level.FINE);
      appRooLogger.addHandler(verboseHandler);
      appRooLogger.setLevel(Level.FINE);
      appRooLogger.setUseParentHandlers(false);
    }
    

    Iterator<TemporalDocument> docIter = null;
    String fileList = argOptions.hasOption("fileList") ? 
      argOptions.getStringOption("fileList") : 
      null;
    
    String docFile = argOptions.hasOption("docFile") ? 
      argOptions.getStringOption("docFile") : 
      null;
    if ((fileList == null) && (docFile == null)) {
      throw new Error("must specify document sources");
    }
    


    Collection<Iterator<TemporalDocument>> docIters = 
      new LinkedList();
    
    if (fileList != null) {
      String[] fileNames = fileList.split(",");
      

      for (String s : fileNames) {
        docIters.add(new FileListTemporalDocumentIterator(s));
      }
    }
    if (docFile != null) {
      String[] fileNames = docFile.split(",");
      

      for (String s : fileNames) {
        docIters.add(new OneLinePerTemporalDocumentIterator(s));
      }
    }
    

    docIter = new CombinedIterator(docIters);
    
    int numThreads = Runtime.getRuntime().availableProcessors();
    if (argOptions.hasOption("threads")) {
      numThreads = argOptions.getIntOption("threads");
    }
    
    overwrite = true;
    if (argOptions.hasOption("overwrite")) {
      overwrite = argOptions.getBooleanOption("overwrite");
    }
    


    if (argOptions.hasOption("interestingTokenList")) {
      String fileName = argOptions.getStringOption("interestingTokenList");
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      int m; int k; for (String line = null; (line = br.readLine()) != null; 
          k < m) { String[] arrayOfString2; m = (arrayOfString2 = line.split("\\s+")).length;k = 0; continue;String s = arrayOfString2[k];
        interestingWords.add(s);
        wordToTemporalSemantics.put(s, new TreeMap());k++;
      }
      


      LOGGER.info("loaded " + interestingWords.size() + 
        " interesting words");
    }
    

    if (argOptions.hasOption("savePartitions")) {
      savePartitions = true;
    }
    

    if (argOptions.hasOption("printShiftRankings")) {
      printShiftRankings = true;



    }
    else if (interestingWords.isEmpty()) {
      throw new IllegalArgumentException(
        "Must specify some form of output as either a non-empty setof interesting words and/or writing the semantic partition .sspacefiles to disk");
    }
    



    if (argOptions.hasOption("printInterestingTokenNeighbors")) {
      interestingWordNeighbors = 
        argOptions.getIntOption("printInterestingTokenNeighbors");
    }
    if (argOptions.hasOption("printInterestingTokenShifts")) {
      printInterestingTokenShifts = true;
      LOGGER.info("Recording interesting token shifts");
    }
    if (argOptions.hasOption("printInterestingTokenNeighborComparison")) {
      compareNeighbors = true;
    }
    



    Properties props = setupProperties();
    

    FixedDurationTemporalRandomIndexing fdTri = 
      new FixedDurationTemporalRandomIndexing(props);
    



    if (argOptions.hasOption("semanticFilter")) {
      String fileName = argOptions.getStringOption("semanticFilter");
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      Object wordsToCompute = new HashSet();
      int i1; int n; for (String line = null; (line = br.readLine()) != null; 
          n < i1) { String[] arrayOfString3; i1 = (arrayOfString3 = line.split("\\s+")).length;n = 0; continue;String s = arrayOfString3[n];
        ((Set)wordsToCompute).add(s);n++;
      }
      

      LOGGER.info("computing semantics for only " + ((Set)wordsToCompute).size() + 
        " words");
      
      fdTri.setSemanticFilter((Set)wordsToCompute);
    }
    

    if (argOptions.hasOption("loadVectors")) {
      String fileName = argOptions.getStringOption("loadVectors");
      LOGGER.info("loading index vectors from " + fileName);
      Object wordToIndexVector = 
        IndexVectorUtil.load(new File(fileName));
      fdTri.setWordToIndexVector((Map)wordToIndexVector);
    }
    
    String formatName = argOptions.hasOption("outputFormat") ? 
      argOptions.getStringOption("outputFormat").toUpperCase() : 
      "TEXT";
    
    format = SemanticSpaceIO.SSpaceFormat.valueOf(formatName.toUpperCase());
    
    parseDocumentsMultiThreaded(fdTri, docIter, timeSpan, numThreads);
    
    long startTime = System.currentTimeMillis();
    fdTri.processSpace(props);
    long endTime = System.currentTimeMillis();
    LOGGER.info(String.format("processed space in %.3f seconds%n", new Object[] {
      Double.valueOf((endTime - startTime) / 1000.0D) }));
    

    if (argOptions.hasOption("saveVectors")) {
      String fileName = argOptions.getStringOption("saveVectors");
      LOGGER.info("saving index vectors to " + fileName);
      IndexVectorUtil.save(fdTri.getWordToIndexVector(), 
        new File(fileName));
    }
  }
  


  private void printSpace(SemanticSpace sspace, String tag)
  {
    try
    {
      String EXT = ".sspace";
      File output = overwrite ? 
        new File(outputDir, sspace.getSpaceName() + tag + EXT) : 
        File.createTempFile(sspace.getSpaceName() + tag, EXT, 
        outputDir);
      
      long startTime = System.currentTimeMillis();
      SemanticSpaceIO.save(sspace, output, format);
      long endTime = System.currentTimeMillis();
      verbose("printed space in %.3f seconds%n", new Object[] {
        Double.valueOf((endTime - startTime) / 1000.0D) });
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  











  private void updateTemporalSemantics(long currentSemanticPartitionStartTime, SemanticSpace semanticPartition)
  {
    double[] zeroVector = new double[semanticPartition.getVectorLength()];
    
    for (String word : interestingWords)
    {
      SortedMap<Long, double[]> temporalSemantics = 
        (SortedMap)wordToTemporalSemantics.get(word);
      Vector v = semanticPartition.getVector(word);
      

      double[] semantics = v == null ? 
        zeroVector : 
        Vectors.asDouble(v).toArray();
      
      temporalSemantics.put(Long.valueOf(currentSemanticPartitionStartTime), 
        semantics);
    }
  }
  






  private void printSemanticShifts(String dateString)
    throws IOException
  {
    LOGGER.fine("Writing semantic shifts for " + dateString);
    



    Iterator localIterator = wordToTemporalSemantics.entrySet().iterator();
    while (localIterator.hasNext()) {
      Map.Entry<String, SortedMap<Long, double[]>> e = (Map.Entry)localIterator.next();
      
      String word = (String)e.getKey();
      SortedMap<Long, double[]> timeStampToSemantics = (SortedMap)e.getValue();
      Iterator<Map.Entry<Long, double[]>> it = 
        timeStampToSemantics.entrySet().iterator();
      
      PrintWriter pw = new PrintWriter(new File(outputDir, 
        word + "." + dateString + ".temporal-changes.txt"));
      


      pw.println("#time\ttime-delay\tcosineSim\tcosineAngle\tEuclidean\tchange-in-magnitde\tmagnitde\tprev-magnitude");
      
      Map.Entry<Long, double[]> last = null;
      while (it.hasNext()) {
        Map.Entry<Long, double[]> cur = (Map.Entry)it.next();
        if (last != null) {
          long timeDelay = ((Long)cur.getKey()).longValue() - ((Long)last.getKey()).longValue();
          double euclideanDist = 
            Similarity.euclideanDistance((double[])cur.getValue(), (double[])last.getValue());
          double cosineSim = 
            Similarity.cosineSimilarity((double[])cur.getValue(), (double[])last.getValue());
          double cosineAngle = Math.acos(cosineSim);
          
          double oldMag = getMagnitude((double[])last.getValue());
          double newMag = getMagnitude((double[])cur.getValue());
          
          pw.println(cur.getKey() + "\t" + timeDelay + "\t" + 
            cosineSim + "\t" + cosineAngle + "\t" + 
            euclideanDist + "\t" + (newMag - oldMag) + "\t" + 
            newMag + "\t" + oldMag);
        }
        last = cur;
      }
      pw.close();
    }
  }
  










  private void printShiftRankings(String dateString, long startOfMostRecentPartition, TimeSpan partitionDuration)
    throws IOException
  {
    SortedMultiMap<Double, String> shiftToWord = 
      new TreeMultiMap();
    



    TimeSpan twoPartitions = new TimeSpan(partitionDuration.getYears() * 2, 
      partitionDuration.getMonths() * 2, 
      partitionDuration.getWeeks() * 2, 
      partitionDuration.getDays() * 2, 
      partitionDuration.getHours() * 2);
    



    Iterator localIterator = wordToTemporalSemantics.entrySet().iterator();
    String word;
    while (localIterator.hasNext()) {
      Map.Entry<String, SortedMap<Long, double[]>> e = (Map.Entry)localIterator.next();
      word = (String)e.getKey();
      SortedMap<Long, double[]> m = (SortedMap)e.getValue();
      

      if (m.size() >= 2)
      {



        NavigableMap<Long, double[]> timestampToVector = 
          (e instanceof NavigableMap) ? 
          (NavigableMap)m : 
          new TreeMap(m);
        
        Map.Entry<Long, double[]> mostRecent = timestampToVector.lastEntry();
        

        if (((Long)mostRecent.getKey()).equals(Long.valueOf(startOfMostRecentPartition)))
        {

          Map.Entry<Long, double[]> secondMostRecent = 
            timestampToVector.lowerEntry((Long)mostRecent.getKey());
          



          if (twoPartitions.insideRange(((Long)secondMostRecent.getKey()).longValue(), ((Long)mostRecent.getKey()).longValue()))
          {



            shiftToWord.put(Double.valueOf(Similarity.cosineSimilarity(
              (double[])secondMostRecent.getValue(), 
              (double[])mostRecent.getValue())), word); }
        }
      } }
    PrintWriter pw = new PrintWriter(new File(outputDir, 
      "shift-ranks-for." + dateString + ".txt"));
    for (Object e : shiftToWord.entrySet()) {
      pw.println(((Map.Entry)e).getKey() + "\t" + (String)((Map.Entry)e).getValue());
    }
    pw.close();
  }
  













  private void printWordNeighbors(String dateString, SemanticSpace semanticPartition)
    throws IOException
  {
    LOGGER.info("printing the most similar words for the semantic partition starting at: " + 
      dateString);
    
    NearestNeighborFinder nnf = 
      new SimpleNearestNeighborFinder(semanticPartition);
    

    for (String toExamine : interestingWords) {
      SortedMultiMap<Double, String> mostSimilar = 
        nnf.getMostSimilar(toExamine, interestingWordNeighbors);
      
      if (mostSimilar != null) {
        File neighborFile = 
          new File(outputDir, toExamine + "-" + dateString + ".txt");
        neighborFile.createNewFile();
        
        File neighborComparisonFile = new File(outputDir, 
          toExamine + "_neighbor-comparisons_" + dateString + ".txt");
        neighborComparisonFile.createNewFile();
        

        PrintWriter pw = new PrintWriter(neighborFile);
        for (String similar : mostSimilar.values()) {
          pw.println(similar);
        }
        pw.close();
        
        if (compareNeighbors)
        {


          writeNeighborComparison(neighborComparisonFile, 
            mostSimilar, semanticPartition);
        }
      }
    }
  }
  












  private static void writeNeighborComparison(File neighborFile, MultiMap<Double, String> mostSimilar, SemanticSpace sspace)
    throws IOException
  {
    PrintWriter pw = new PrintWriter(neighborFile);
    

    StringBuffer sb = new StringBuffer(mostSimilar.size() * 10);
    Iterator<String> it = mostSimilar.values().iterator();
    while (it.hasNext()) {
      sb.append((String)it.next());
      if (it.hasNext())
        sb.append(" ");
    }
    pw.println(sb.toString());
    


    for (String word : mostSimilar.values()) {
      sb = new StringBuffer(mostSimilar.size() * 10);
      sb.append(word).append(" ");
      

      for (String other : mostSimilar.values())
      {
        double similarity = Similarity.cosineSimilarity(
          sspace.getVector(word), 
          sspace.getVector(other));
        sb.append(similarity).append(" ");
      }
      pw.println(sb.toString());
    }
    
    pw.close();
  }
  


  protected Properties setupProperties()
  {
    Properties props = System.getProperties();
    



    if (argOptions.hasOption("usePermutations")) {
      props.setProperty(
        "edu.ucla.sspace.tri.OrderedTemporalRandomIndexing.usePermutations", 
        argOptions.getStringOption("usePermutations"));
    }
    
    if (argOptions.hasOption("permutationFunction")) {
      props.setProperty(
        "edu.ucla.sspace.tri.OrderedTemporalRandomIndexing.permutationFunction", 
        argOptions.getStringOption("permutationFunction"));
    }
    
    if (argOptions.hasOption("windowSize")) {
      props.setProperty(
        "edu.ucla.sspace.tri.OrderedTemporalRandomIndexing.windowSize", 
        argOptions.getStringOption("windowSize"));
    }
    
    if (argOptions.hasOption("vectorLength")) {
      props.setProperty(
        "edu.ucla.sspace.tri.OrderedTemporalRandomIndexing.vectorLength", 
        argOptions.getStringOption("vectorLength"));
    }
    
    if (argOptions.hasOption("useSparseSemantics")) {
      props.setProperty(
        "edu.ucla.sspace.tri.OrderedTemporalRandomIndexing.sparseSemantics", 
        argOptions.getStringOption("useSparseSemantics"));
    }
    
    if (argOptions.hasOption("partitionDuration")) {
      props.setProperty("edu.ucla.sspace.tri.FixedDurationTemporalRandomIndexing.partitionDuration", 
      
        argOptions.getStringOption("partitionDuration"));
    }
    


    if (argOptions.hasOption("tokenFilter")) {
      props.setProperty("edu.ucla.sspace.text.TokenizerFactory.tokenFilter", 
        argOptions.getStringOption("tokenFilter"));
    }
    
    if (argOptions.hasOption("compoundTokens")) {
      props.setProperty("edu.ucla.sspace.text.TokenizerFactory.compoundTokens", 
        argOptions.getStringOption("compoundTokens"));
    }
    
    return props;
  }
  
  private static double getMagnitude(double[] arr)
  {
    double mag = 0.0D;
    double[] arrayOfDouble = arr;int j = arr.length; for (int i = 0; i < j; i++) { double d = arrayOfDouble[i];
      if (d != 0.0D)
        mag += d * d;
    }
    return Math.sqrt(mag);
  }
  













  protected void parseDocumentsMultiThreaded(final FixedDurationTemporalRandomIndexing fdTri, final Iterator<TemporalDocument> docIter, final TimeSpan timeSpan, int numThreads)
    throws IOException, InterruptedException
  {
    Collection<Thread> processingThreads = new LinkedList();
    
    final AtomicInteger count = new AtomicInteger(0);
    
    final AtomicLong curSSpaceStartTime = new AtomicLong();
    Object calendarLock = new Object();
    final DateFormat df = new SimpleDateFormat("yyyy_MM_ww_dd_hh");
    
    AtomicLong lastWriteTime = new AtomicLong();
    


    final AtomicBoolean startBarrier = new AtomicBoolean(false);
    



    final Queue<Long> futureStartTimes = new ConcurrentLinkedQueue();
    

    final boolean writeSemanticPartitions = savePartitions;
    final boolean writeSemanticShifts = printInterestingTokenShifts;
    boolean writeInterestingWordNeighbors = 
      interestingWordNeighbors > 0;
    final boolean writeShiftRankings = printShiftRankings;
    




    Runnable serializeTimeSpan = new Runnable() {
      public void run() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(curSSpaceStartTime.get());
        String dateString = df.format(c.getTime());
        



        try
        {
          if (writeSemanticPartitions) {
            FixedDurationTemporalRandomIndexingMain.LOGGER.info("writing semantic partition starting at: " + 
              dateString);
            
            FixedDurationTemporalRandomIndexingMain.this.printSpace(fdTri, "-" + dateString);
          }
          


          FixedDurationTemporalRandomIndexingMain.this.updateTemporalSemantics(curSSpaceStartTime.get(), 
            fdTri);
          if (writeSemanticShifts) {
            FixedDurationTemporalRandomIndexingMain.this.printSemanticShifts(dateString);
          }
          if (writeShiftRankings) {
            FixedDurationTemporalRandomIndexingMain.this.printShiftRankings(dateString, 
              curSSpaceStartTime.get(), 
              timeSpan);
          }
          

          if (interestingWordNeighbors > 0) {
            FixedDurationTemporalRandomIndexingMain.this.printWordNeighbors(dateString, fdTri);
          }
        }
        catch (IOException ioe) {
          throw new IOError(ioe);
        }
        


        if ((!FixedDurationTemporalRandomIndexingMain.$assertionsDisabled) && (futureStartTimes.size() <= 0)) throw new AssertionError();
        Long ssStart = (Long)new TreeSet(futureStartTimes).first();
        futureStartTimes.clear();
        

        curSSpaceStartTime.set(ssStart.longValue());


      }
      


    };
    final CyclicBarrier exceededTimeSpanBarrier = 
      new CyclicBarrier(numThreads, serializeTimeSpan);
    

    for (int i = 0; i < numThreads; i++)
    {
      Thread processingThread = new Thread()
      {

        public void run()
        {
          while (docIter.hasNext())
          {
            TemporalDocument doc = (TemporalDocument)docIter.next();
            int docNumber = count.incrementAndGet();
            long docTime = doc.timeStamp();
            

            if (docNumber == 1) {
              curSSpaceStartTime.set(docTime);
              startBarrier.set(true);
            }
            





            while (!startBarrier.get()) {}
            









            while (!timeSpan.insideRange(curSSpaceStartTime.get(), docTime))
            {


              try
              {



                futureStartTimes.offer(Long.valueOf(docTime));
                exceededTimeSpanBarrier.await();
              } catch (InterruptedException ex) {
                return;
              } catch (BrokenBarrierException ex) {
                return;
              }
            }
            try
            {
              fdTri.processDocument(doc.reader());
            }
            catch (IOException ioe) {
              throw new IOError(ioe);
            }
            FixedDurationTemporalRandomIndexingMain.LOGGER.fine("parsed document #" + docNumber);
          }
        }
      };
      processingThreads.add(processingThread);
    }
    
    long threadStart = System.currentTimeMillis();
    

    for (Thread t : processingThreads) {
      t.start();
    }
    verbose("Beginning processing using %d threads", new Object[] { Integer.valueOf(numThreads) });
    

    for (Thread t : processingThreads) {
      t.join();
    }
    verbose("parsed %d document in %.3f total seconds)%n", new Object[] {
      Integer.valueOf(count.get()), 
      Double.valueOf((System.currentTimeMillis() - threadStart) / 1000.0D) });
  }
  


  protected void usage()
  {
    System.out.println(
      "usage: java FixedDurationTemporalRandomIndexingMain [options] <output-dir>\n\n" + 
      
      argOptions.prettyPrint() + 
      
      "\nFixed-Duration TRI provides four main output options:\n\n" + 
      
      "  1) Outputting each semantic partition as a separate .sspace file.  " + 
      "Each file\n     is named using the yyyy_MM_ww_dd_hh format to " + 
      "indicate it start date.\n     This is the most expensive of the " + 
      "operations due to I/O overhead.\n\n" + 
      
      "  The remaining options require the use of the -I " + 
      "--interestingTokenList option to\n  specify a set of word for use" + 
      " in tracking temporal changes.\n\n  2) For each of the interesting" + 
      "words, -P, --printInterestingTokenShifts will track\n" + 
      "     the semantics" + 
      " through time and report the semantic shift along with other\n" + 
      "     distance statistics.\n\n" + 
      "  3) For each of the interesting words, -N, " + 
      "--printInterestingTokenNeighbors\n     will print the nearest " + 
      "neighbor for each in the semantic space.  The\n     number " + 
      "of neighbors to print should be specified.\n\n" + 
      
      "  4) For each of the interesting words, generate the list of " + 
      "similar\n     neighbors using the --printInterestingTokenNeighbors" + 
      " and then compare\n     those neighbors with each other using " + 
      "the\n     --printInterestingTokenNeighborComparison option.  " + 
      "This creates a file\n     with the pair-wise cosine similarities " + 
      "for all neighbors.  Note that this\n     option requires both " + 
      "flags to be specified.\n\n" + 
      
      "Semantic filters limit the set of tokens for which the " + 
      "semantics are kept.\nThis limits the potential memory overhead " + 
      "for calculating semantics for a\nlarge set of words." + 
      
      "\n\n" + "The compound word option specifies a file whose contents are compound tokens,\ne.g. white house.  Each compound token should be specified on its own line.\nCompound tokenization is greedy and will select the longest compound token\npresent.  For example if \"bar exam\" and \"California bar exam\" are both\ncompound tokens, the latter will always be returned as a single token, rather\nthan returning the two tokens \"California\" and \"bar exam\"." + 
      "\n\n" + "token configuration lists sets of files that contain tokens to be included or\nexcluded.  The behavior, \"include\" or \"exclude\" is specified\nfirst, followed by one or more file names, each separated by colons.\nMultiple behaviors may be specified one after the other using a ','\ncharacter to separate them.  For example, a typical configuration may\nlook like: include=top-tokens.txt:test-words.txt,exclude=stop-words.txt\nNote that behaviors are applied in the order they are presented on the command-line." + 
      "\n\n" + "Semantic space files are stored in one of four formats: text, sparse_text, binary\nsparse_binary.  The sparse versions should be used if the algorithm produces\nsemantic vectors in which more than half of the values are 0.  The sparse\nversions are much more compact for these types of semantic spaces and will be\nboth faster to read and write as well as be much smaller on disk.  Text formats\nare human readable but may take up more space.  Binary formats offer\nsignificantly better I/O performance." + 
      "\n\n" + "Send bug reports or comments to <s-space-research-dev@googlegroups.com>.");
  }
  
  protected void verbose(String msg) {
    LOGGER.fine(msg);
  }
  
  protected void verbose(String format, Object... args) {
    if (LOGGER.isLoggable(Level.FINE)) {
      LOGGER.fine(String.format(format, args));
    }
  }
}
