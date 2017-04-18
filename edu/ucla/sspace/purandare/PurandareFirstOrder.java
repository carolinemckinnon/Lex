package edu.ucla.sspace.purandare;

import edu.ucla.sspace.clustering.Assignment;
import edu.ucla.sspace.clustering.Assignments;
import edu.ucla.sspace.clustering.ClutoClustering;
import edu.ucla.sspace.clustering.ClutoClustering.Criterion;
import edu.ucla.sspace.clustering.ClutoClustering.Method;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.Statistics;
import edu.ucla.sspace.matrix.AtomicGrowingMatrix;
import edu.ucla.sspace.matrix.AtomicMatrix;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.SparseMatrix;
import edu.ucla.sspace.matrix.SparseRowMaskedMatrix;
import edu.ucla.sspace.matrix.YaleSparseMatrix;
import edu.ucla.sspace.text.IteratorFactory;
import edu.ucla.sspace.util.SparseArray;
import edu.ucla.sspace.util.SparseHashArray;
import edu.ucla.sspace.util.WorkerThread;
import edu.ucla.sspace.vector.CompactSparseVector;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.VectorMath;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;










































































public class PurandareFirstOrder
  implements SemanticSpace
{
  private static final Logger LOGGER = Logger.getLogger(PurandareFirstOrder.class.getName());
  




  private static final String PROPERTY_PREFIX = "edu.ucla.sspace.purandare.PurandareFirstOrder";
  




  public static final String MAX_CONTEXTS_PER_WORD = "edu.ucla.sspace.purandare.PurandareFirstOrder.maxContexts";
  




  private final Map<String, Integer> termToIndex;
  




  private final Map<String, DoubleVector> termToVector;
  




  private final int windowSize;
  




  private final int contextWindowSize;
  




  private final AtomicMatrix cooccurrenceMatrix;
  




  private final List<AtomicInteger> termCounts;
  




  private File compressedDocuments;
  




  private DataOutputStream compressedDocumentsWriter;
  



  private final AtomicInteger documentCounter;
  



  private final int maxContextsPerWord;
  



  private int wordIndexCounter;
  




  public PurandareFirstOrder()
  {
    this(System.getProperties());
  }
  



  public PurandareFirstOrder(Properties props)
  {
    cooccurrenceMatrix = new AtomicGrowingMatrix();
    termToIndex = new ConcurrentHashMap();
    termToVector = new ConcurrentHashMap();
    termCounts = new CopyOnWriteArrayList();
    windowSize = 5;
    contextWindowSize = 20;
    documentCounter = new AtomicInteger(0);
    String maxContextsProp = props.getProperty("edu.ucla.sspace.purandare.PurandareFirstOrder.maxContexts");
    if (maxContextsProp == null) {
      maxContextsPerWord = Integer.MAX_VALUE;
    } else {
      int i = Integer.parseInt(maxContextsProp);
      if (i <= 0)
        throw new IllegalArgumentException(
          "The number of contexts must be a positive number");
      maxContextsPerWord = i;
    }
    try {
      compressedDocuments = 
        File.createTempFile("petersen-documents", ".dat");
      compressedDocuments.deleteOnExit();
      compressedDocumentsWriter = new DataOutputStream(
        new BufferedOutputStream(
        new FileOutputStream(compressedDocuments)));
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  

  public void processDocument(BufferedReader document)
    throws IOException
  {
    documentCounter.getAndIncrement();
    Queue<String> nextWords = new ArrayDeque();
    Queue<String> prevWords = new ArrayDeque();
    
    Iterator<String> documentTokens = 
      IteratorFactory.tokenizeOrdered(document);
    
    String focus = null;
    
    ByteArrayOutputStream compressedDocument = 
      new ByteArrayOutputStream(4096);
    DataOutputStream dos = new DataOutputStream(compressedDocument);
    int tokens = 0;
    int unfilteredTokens = 0;
    
    int i = 0;
    do { nextWords.offer((String)documentTokens.next());i++;
      if (i >= windowSize) break; } while (documentTokens.hasNext());
    

    while (!nextWords.isEmpty()) {
      tokens++;
      

      focus = (String)nextWords.remove();
      

      if (documentTokens.hasNext()) {
        String windowEdge = (String)documentTokens.next();
        nextWords.offer(windowEdge);
      }
      


      if (focus.equals(""))
      {

        dos.writeInt(-1);
        
        prevWords.offer(focus);
        if (prevWords.size() > windowSize) {
          prevWords.remove();
        }
      }
      else {
        int focusIndex = getIndexFor(focus);
        

        dos.writeInt(focusIndex);
        
        ((AtomicInteger)termCounts.get(focusIndex)).incrementAndGet();
        unfilteredTokens++;
        

        for (String after : nextWords)
        {

          if (!after.equals("")) {
            int index = getIndexFor(after);
            cooccurrenceMatrix.addAndGet(focusIndex, index, 1.0D);
          }
        }
        
        for (String before : prevWords)
        {

          if (!before.equals("")) {
            int index = getIndexFor(before);
            cooccurrenceMatrix.addAndGet(focusIndex, index, 1.0D);
          }
        }
        


        prevWords.offer(focus);
        if (prevWords.size() > windowSize)
          prevWords.remove();
      }
    }
    dos.close();
    byte[] docAsBytes = compressedDocument.toByteArray();
    


    synchronized (compressedDocumentsWriter)
    {
      compressedDocumentsWriter.writeInt(tokens);
      compressedDocumentsWriter.writeInt(unfilteredTokens);
      compressedDocumentsWriter.write(docAsBytes, 0, docAsBytes.length);
    }
  }
  




  private final int getIndexFor(String word)
  {
    Integer index = (Integer)termToIndex.get(word);
    if (index == null) {
      synchronized (this)
      {
        index = (Integer)termToIndex.get(word);
        

        if (index == null) {
          int i = wordIndexCounter++;
          


          termCounts.add(new AtomicInteger(0));
          termToIndex.put(word, Integer.valueOf(i));
          return i;
        }
      }
    }
    return index.intValue();
  }
  



  public Set<String> getWords()
  {
    return Collections.unmodifiableSet(termToVector.keySet());
  }
  


  public DoubleVector getVector(String word)
  {
    return (DoubleVector)termToVector.get(word);
  }
  



  public void processSpace(Properties properties)
  {
    try
    {
      processSpace();
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  




  private void processSpace()
    throws IOException
  {
    compressedDocumentsWriter.close();
    


    String[] indexToTerm = new String[termToIndex.size()];
    for (Map.Entry<String, Integer> e : termToIndex.entrySet()) {
      indexToTerm[((Integer)e.getValue()).intValue()] = ((String)e.getKey());
    }
    

    int corpusSize = 0;
    for (AtomicInteger i : termCounts)
      corpusSize += i.get();
    final int uniqueTerms = cooccurrenceMatrix.rows();
    
    LOGGER.info("calculating term features");
    




    final BitSet[] termFeatures = new BitSet[wordIndexCounter];
    for (int termIndex = 0; termIndex < uniqueTerms; termIndex++) {
      String term = indexToTerm[termIndex];
      termFeatures[termIndex] = calculateTermFeatures(term, corpusSize);
    }
    
    LOGGER.info("reprocessing corpus to generate feature vectors");
    


    BlockingQueue<Runnable> workQueue = 
      new LinkedBlockingQueue();
    for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
      Thread t = new WorkerThread(workQueue);
      t.start();
    }
    final Semaphore termsProcessed = new Semaphore(0);
    
    for (int termIndex = 0; termIndex < uniqueTerms; termIndex++)
    {
      final String term = indexToTerm[termIndex];
      final int i = termIndex;
      workQueue.offer(new Runnable() {
        public void run() {
          try {
            PurandareFirstOrder.LOGGER.fine(String.format(
              "processing term %6d/%d: %s", new Object[] { Integer.valueOf(i), Integer.valueOf(uniqueTerms), term }));
            Matrix contexts = PurandareFirstOrder.this.getTermContexts(i, termFeatures[i]);
            PurandareFirstOrder.this.senseInduce(term, contexts);
          } catch (IOException ioe) {
            ioe.printStackTrace();
          } finally {
            termsProcessed.release();
          }
        }
      });
    }
    
    try
    {
      termsProcessed.acquire(uniqueTerms);
    } catch (InterruptedException ie) {
      throw new Error("interrupted while waiting for terms to finish reprocessing", 
        ie);
    }
    LOGGER.info("finished reprocessing all terms");
  }
  
  private BitSet calculateTermFeatures(String term, int corpusSize) {
    int termIndex = ((Integer)termToIndex.get(term)).intValue();
    LOGGER.fine(String.format("Calculating feature set for %6d/%d: %s", new Object[] {
      Integer.valueOf(termIndex), Integer.valueOf(cooccurrenceMatrix.rows()), term }));
    DoubleVector cooccurrences = cooccurrenceMatrix.getRowVector(termIndex);
    int termCount = ((AtomicInteger)termCounts.get(termIndex)).get();
    BitSet validFeatures = new BitSet(wordIndexCounter);
    



    for (int co = 0; co < cooccurrences.length(); co++)
    {


      double count = cooccurrences.get(co);
      
      if (count != 0.0D)
      {


        double a = count;
        
        double b = ((AtomicInteger)termCounts.get(co)).get() - count;
        
        double c = termCount - count;
        
        double d = corpusSize - (a + b + c);
        
        double logLikelihood = logLikelihood(a, b, c, d);
        if (logLikelihood > 3.841D)
          validFeatures.set(co);
      } }
    if (LOGGER.isLoggable(Level.FINE))
      LOGGER.fine(term + " had " + validFeatures.cardinality() + 
        " features");
    return validFeatures;
  }
  
















  private Matrix getTermContexts(int termIndex, BitSet termFeatures)
    throws IOException
  {
    DataInputStream corpusReader = new DataInputStream(
      new BufferedInputStream(new FileInputStream(compressedDocuments)));
    
    int documents = documentCounter.get();
    

    SparseMatrix contextsForCurTerm = new YaleSparseMatrix(
      ((AtomicInteger)termCounts.get(termIndex)).get(), termToIndex.size());
    int contextsSeen = 0;
    for (int d = 0; d < documents; d++) {
      int docId = d;
      
      int tokensInDoc = corpusReader.readInt();
      int unfilteredTokens = corpusReader.readInt();
      
      int[] doc = new int[tokensInDoc];
      for (int i = 0; i < tokensInDoc; i++) {
        doc[i] = corpusReader.readInt();
      }
      int contextsInDoc = 
        processIntDocument(termIndex, doc, contextsForCurTerm, 
        contextsSeen, termFeatures);
      contextsSeen += contextsInDoc;
    }
    corpusReader.close();
    


    if ((maxContextsPerWord < Integer.MAX_VALUE) && 
      (contextsForCurTerm.rows() > maxContextsPerWord)) {
      BitSet randomContexts = Statistics.randomDistribution(
        maxContextsPerWord, contextsForCurTerm.rows());
      contextsForCurTerm = 
        new SparseRowMaskedMatrix(contextsForCurTerm, randomContexts);
    }
    
    return contextsForCurTerm;
  }
  






  private void senseInduce(String term, Matrix contexts)
    throws IOException
  {
    LOGGER.fine("Clustering " + contexts.rows() + " contexts for " + term);
    


    int numClusters = Math.min(7, contexts.rows());
    




    if ((!term.matches("[a-zA-z]+")) || (numClusters <= 6)) {
      SparseDoubleVector meanSenseVector = 
        new CompactSparseVector(termToIndex.size());
      int rows = contexts.rows();
      for (int row = 0; row < rows; row++)
        VectorMath.add(meanSenseVector, contexts.getRowVector(row));
      termToVector.put(term, meanSenseVector);
      return;
    }
    
    Assignments clusterAssignment = 
      new ClutoClustering().cluster(contexts, numClusters, 
      ClutoClustering.Method.AGGLOMERATIVE, 
      ClutoClustering.Criterion.UPGMA);
    
    LOGGER.fine("Generative sense vectors for " + term);
    

    int[] clusterSize = new int[numClusters];
    


    SparseDoubleVector[] meanSenseVectors = 
      new CompactSparseVector[numClusters];
    
    for (int i = 0; i < meanSenseVectors.length; i++) {
      meanSenseVectors[i] = new CompactSparseVector(termToIndex.size());
    }
    

    for (int row = 0; row < clusterAssignment.size(); row++)
    {
      if (clusterAssignment.get(row).assignments().length != 0)
      {
        int assignment = clusterAssignment.get(row).assignments()[0];
        clusterSize[assignment] += 1;
        
        DoubleVector contextVector = contexts.getRowVector(row);
        VectorMath.add(meanSenseVectors[assignment], contextVector);
      }
    }
    


    int senseCounter = 0;
    for (int i = 0; i < numClusters; i++) {
      int size = clusterSize[i];
      if (size / contexts.rows() > 0.02D) {
        String termWithSense = 
          term + "-" + senseCounter;
        senseCounter++;
        termToVector.put(termWithSense, meanSenseVectors[i]);
      }
    }
    LOGGER.fine("Discovered " + senseCounter + " senses for " + term);
  }
  



















  private int processIntDocument(int termIndex, int[] document, Matrix contextMatrix, int rowStart, BitSet featuresForTerm)
  {
    int contexts = 0;
    for (int i = 0; i < document.length; i++)
    {
      int curToken = document[i];
      
      if (curToken == termIndex)
      {



        SparseArray<Integer> contextCounts = new SparseHashArray();
        

        for (int left = Math.max(i - contextWindowSize, 0); 
              left < i; left++)
        {

          int token = document[left];
          

          if ((token >= 0) && (featuresForTerm.get(token))) {
            Integer count = (Integer)contextCounts.get(token);
            contextCounts.set(token, Integer.valueOf(count == null ? 1 : count.intValue() + 1));
          }
        }
        

        int end = Math.min(i + contextWindowSize, document.length);
        for (int right = i + 1; right < end; right++) {
          int token = document[right];
          

          if ((token >= 0) && (featuresForTerm.get(token))) {
            count = (Integer)contextCounts.get(token);
            contextCounts.set(token, Integer.valueOf(count == null ? 1 : count.intValue() + 1));
          }
        }
        



        int curContext = rowStart + contexts;
        int[] arrayOfInt; Integer localInteger1 = (arrayOfInt = contextCounts.getElementIndices()).length; for (Integer count = 0; count < localInteger1; count++) { int feat = arrayOfInt[count];
          
          contextMatrix.set(curContext, feat, ((Integer)contextCounts.get(feat)).intValue());
        }
        

        contexts++;
      } }
    return contexts;
  }
  



  public int getVectorLength()
  {
    return termToIndex.size();
  }
  


  public String getSpaceName()
  {
    return "purandare-petersen";
  }
  







  private static double logLikelihood(double a, double b, double c, double d)
  {
    double col1sum = a + c;
    double col2sum = b + d;
    double row1sum = a + b;
    double row2sum = c + d;
    double sum = row1sum + row2sum;
    

    double aExp = row1sum / sum * col1sum;
    double bExp = row1sum / sum * col2sum;
    double cExp = row2sum / sum * col1sum;
    double dExp = row2sum / sum * col2sum;
    


    double aVal = a == 0.0D ? 0.0D : a * Math.log(a / aExp);
    double bVal = b == 0.0D ? 0.0D : b * Math.log(b / bExp);
    double cVal = c == 0.0D ? 0.0D : c * Math.log(c / cExp);
    double dVal = d == 0.0D ? 0.0D : d * Math.log(d / dExp);
    
    return 2.0D * (aVal + bVal + cVal + dVal);
  }
}
