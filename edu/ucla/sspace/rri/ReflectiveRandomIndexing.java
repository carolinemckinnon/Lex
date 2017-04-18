package edu.ucla.sspace.rri;

import edu.ucla.sspace.common.Filterable;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.index.RandomIndexVectorGenerator;
import edu.ucla.sspace.text.IteratorFactory;
import edu.ucla.sspace.util.WorkerThread;
import edu.ucla.sspace.vector.CompactSparseIntegerVector;
import edu.ucla.sspace.vector.DenseIntVector;
import edu.ucla.sspace.vector.IntegerVector;
import edu.ucla.sspace.vector.TernaryVector;
import edu.ucla.sspace.vector.VectorMath;
import edu.ucla.sspace.vector.Vectors;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;



























































































































public class ReflectiveRandomIndexing
  implements SemanticSpace, Filterable
{
  private static final String PROPERTY_PREFIX = "edu.ucla.sspace.ri.ReflectiveRandomIndexing";
  public static final String VECTOR_LENGTH_PROPERTY = "edu.ucla.sspace.ri.ReflectiveRandomIndexing.vectorLength";
  public static final String USE_SPARSE_SEMANTICS_PROPERTY = "edu.ucla.sspace.ri.ReflectiveRandomIndexing.sparseSemantics";
  public static final int DEFAULT_VECTOR_LENGTH = 4000;
  private static final String RRI_SSPACE_NAME = "reflective-random-indexing";
  private static final Logger LOGGER = Logger.getLogger(ReflectiveRandomIndexing.class.getName());
  




  private final Map<Integer, IntegerVector> docToVector;
  




  private final Map<String, IntegerVector> termToReflectiveSemantics;
  




  private final Map<String, TernaryVector> termToIndexVector;
  




  private final Map<String, Integer> termToIndex;
  




  private final AtomicInteger documentCounter;
  




  private final int vectorLength;
  




  private final boolean useSparseSemantics;
  




  private final Set<String> semanticFilter;
  




  private final RandomIndexVectorGenerator indexVectorGenerator;
  




  private File compressedDocuments;
  




  private DataOutputStream compressedDocumentsWriter;
  




  private int termIndexCounter;
  




  private String[] indexToTerm;
  





  public ReflectiveRandomIndexing()
  {
    this(System.getProperties());
  }
  



  public ReflectiveRandomIndexing(Properties properties)
  {
    String vectorLengthProp = 
      properties.getProperty("edu.ucla.sspace.ri.ReflectiveRandomIndexing.vectorLength");
    vectorLength = (vectorLengthProp != null ? 
      Integer.parseInt(vectorLengthProp) : 
      4000);
    
    String useSparseProp = 
      properties.getProperty("edu.ucla.sspace.ri.ReflectiveRandomIndexing.sparseSemantics");
    useSparseSemantics = (useSparseProp != null ? 
      Boolean.parseBoolean(useSparseProp) : 
      true);
    
    indexVectorGenerator = 
      new RandomIndexVectorGenerator(vectorLength, properties);
    


    termToIndexVector = new ConcurrentHashMap();
    docToVector = new ConcurrentHashMap();
    termToReflectiveSemantics = 
      new ConcurrentHashMap();
    termToIndex = new ConcurrentHashMap();
    
    documentCounter = new AtomicInteger();
    semanticFilter = new HashSet();
    

    try
    {
      compressedDocuments = 
        File.createTempFile("reflective-ri-documents", ".dat");
      compressedDocuments.deleteOnExit();
      compressedDocumentsWriter = new DataOutputStream(
        new BufferedOutputStream(
        new FileOutputStream(compressedDocuments)));
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  



  private IntegerVector createVector()
  {
    return useSparseSemantics ? 
      new CompactSparseIntegerVector(vectorLength) : 
      new DenseIntVector(vectorLength);
  }
  







  private TernaryVector getTermIndexVector(String term)
  {
    TernaryVector iv = (TernaryVector)termToIndexVector.get(term);
    if (iv == null)
    {
      synchronized (this)
      {

        iv = (TernaryVector)termToIndexVector.get(term);
        if (iv == null)
        {

          termToIndex.put(term, Integer.valueOf(termIndexCounter++));
          

          termToReflectiveSemantics.put(term, createVector());
          
          iv = indexVectorGenerator.generate();
          termToIndexVector.put(term, iv);
        }
      }
    }
    return iv;
  }
  


  public IntegerVector getVector(String word)
  {
    IntegerVector v = (IntegerVector)termToReflectiveSemantics.get(word);
    if (v == null) {
      return null;
    }
    return Vectors.immutable(v);
  }
  


  public String getSpaceName()
  {
    return "reflective-random-indexing-" + vectorLength + "v";
  }
  


  public int getVectorLength()
  {
    return vectorLength;
  }
  


  public Set<String> getWords()
  {
    return Collections.unmodifiableSet(termToReflectiveSemantics.keySet());
  }
  



  public void processDocument(BufferedReader document)
    throws IOException
  {
    int docIndex = documentCounter.getAndIncrement();
    
    Iterator<String> documentTokens = 
      IteratorFactory.tokenizeOrdered(document);
    



    ByteArrayOutputStream compressedDocument = 
      new ByteArrayOutputStream(4096);
    DataOutputStream dos = new DataOutputStream(compressedDocument);
    int tokens = 0;
    int unfilteredTokens = 0;
    
    IntegerVector docVector = createVector();
    docToVector.put(Integer.valueOf(docIndex), docVector);
    
    while (documentTokens.hasNext()) {
      tokens++;
      String focusWord = (String)documentTokens.next();
      




      boolean calculateSemantics = 
        (semanticFilter.isEmpty()) || ((semanticFilter.contains(focusWord)) && 
        (!focusWord.equals("")));
      


      if (calculateSemantics)
      {




        unfilteredTokens++;
        add(docVector, getTermIndexVector(focusWord));
        





        int focusIndex = ((Integer)termToIndex.get(focusWord)).intValue();
        


        dos.writeInt(focusIndex);
      }
    }
    document.close();
    
    dos.close();
    byte[] docAsBytes = compressedDocument.toByteArray();
    


    synchronized (compressedDocumentsWriter)
    {
      compressedDocumentsWriter.writeInt(unfilteredTokens);
      compressedDocumentsWriter.write(docAsBytes, 0, docAsBytes.length);
    }
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
    LOGGER.info("generating reflective vectors");
    compressedDocumentsWriter.close();
    int numDocuments = documentCounter.get();
    termToIndexVector.clear();
    indexToTerm = new String[termToIndex.size()];
    for (Map.Entry<String, Integer> e : termToIndex.entrySet()) {
      indexToTerm[((Integer)e.getValue()).intValue()] = ((String)e.getKey());
    }
    

    DataInputStream corpusReader = new DataInputStream(
      new BufferedInputStream(new FileInputStream(compressedDocuments)));
    


    Object workQueue = 
      new LinkedBlockingQueue();
    for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
      Thread t = new WorkerThread((BlockingQueue)workQueue);
      t.start();
    }
    final Semaphore documentsRerocessed = new Semaphore(0);
    
    for (int d = 0; d < numDocuments; d++) {
      final int docId = d;
      


      int tokensInDoc = corpusReader.readInt();
      
      final int[] doc = new int[tokensInDoc];
      for (int i = 0; i < tokensInDoc; i++) {
        doc[i] = corpusReader.readInt();
      }
      ((BlockingQueue)workQueue).offer(new Runnable()
      {

        public void run()
        {
          ReflectiveRandomIndexing.LOGGER.fine("reprocessing doc #" + docId);
          ReflectiveRandomIndexing.this.processIntDocument((IntegerVector)docToVector.get(Integer.valueOf(docId)), doc);
          documentsRerocessed.release();
        }
      });
    }
    corpusReader.close();
    
    try
    {
      documentsRerocessed.acquire(numDocuments);
    } catch (InterruptedException ie) {
      throw new Error("interrupted while waiting for documents to finish reprocessing", 
        ie);
    }
    LOGGER.fine("finished reprocessing all documents");
  }
  













  private void processIntDocument(IntegerVector docVector, int[] document)
  {
    for (int termIndex : document) {
      IntegerVector reflectiveVector = 
        (IntegerVector)termToReflectiveSemantics.get(indexToTerm[termIndex]);
      

      synchronized (reflectiveVector) {
        VectorMath.add(reflectiveVector, docVector);
      }
    }
  }
  






  public void setSemanticFilter(Set<String> semanticsToRetain)
  {
    semanticFilter.clear();
    semanticFilter.addAll(semanticsToRetain);
  }
  







  private static void add(IntegerVector semantics, TernaryVector index)
  {
    synchronized (semantics) {
      for (int p : index.positiveDimensions())
        semantics.add(p, 1);
      for (int n : index.negativeDimensions()) {
        semantics.add(n, -1);
      }
    }
  }
}
