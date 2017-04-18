package edu.ucla.sspace.common;

import comp6803.plainly.CorpusCreate;
import comp6803.plainly.PlainlyJsonGenerator;
import edu.ucla.sspace.basis.BasisMapping;
import edu.ucla.sspace.basis.StringBasisMapping;
import edu.ucla.sspace.matrix.Matrices;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.MatrixBuilder;
import edu.ucla.sspace.matrix.MatrixFile;
import edu.ucla.sspace.matrix.Transform;
import edu.ucla.sspace.text.EnglishStemmer;
import edu.ucla.sspace.text.IteratorFactory;
import edu.ucla.sspace.text.Stemmer;
import edu.ucla.sspace.util.Counter;
import edu.ucla.sspace.util.LoggerUtil;
import edu.ucla.sspace.util.ObjectCounter;
import edu.ucla.sspace.util.SparseArray;
import edu.ucla.sspace.util.SparseIntHashArray;
import edu.ucla.sspace.vector.Vector;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
































































public abstract class GenericTermDocumentVectorSpace
  implements SemanticSpace, Serializable
{
  private static final long serialVersionUID = 1L;
  protected static final Logger LOG = Logger.getLogger(GenericTermDocumentVectorSpace.class.getName());
  





  protected BasisMapping<String, String> termToIndex;
  





  protected final AtomicInteger documentCounter;
  




  private transient MatrixBuilder termDocumentMatrixBuilder;
  




  private final boolean readHeaderToken;
  




  protected Matrix wordSpace;
  





  public GenericTermDocumentVectorSpace()
    throws IOException
  {
    this(false, new StringBasisMapping(), Matrices.getMatrixBuilderForSVD());
  }
  

















  public GenericTermDocumentVectorSpace(boolean readHeaderToken, BasisMapping<String, String> termToIndex, MatrixBuilder termDocumentMatrixBuilder)
    throws IOException
  {
    this.readHeaderToken = readHeaderToken;
    this.termToIndex = termToIndex;
    documentCounter = new AtomicInteger(0);
    
    this.termDocumentMatrixBuilder = termDocumentMatrixBuilder;
    
    System.out.println("Saving matrix using " + termDocumentMatrixBuilder);
    
    wordSpace = null;
  }
  

















  public void processDocument(BufferedReader document)
    throws IOException
  {
    Counter<String> termCounts = new ObjectCounter();
    Iterator<String> documentTokens = IteratorFactory.tokenize(document);
    

    int docCount = documentCounter.getAndAdd(1);
    

    if (readHeaderToken) {
      handleDocumentHeader(docCount, (String)documentTokens.next());
    }
    
    if (!documentTokens.hasNext()) {
      return;
    }
    

    while (documentTokens.hasNext()) {
      String word = ((String)documentTokens.next()).replaceAll("[\\p{P}&&[^']]", "").toLowerCase();
      
      if (!PlainlyJsonGenerator.getInstance().getStopwords().contains(word))
      {


        CorpusCreate.getInstance(); if (CorpusCreate.stemmingEnabled.booleanValue()) {
          Stemmer stemmer = new EnglishStemmer();
          System.out.print("Stemming: " + word);
          word = stemmer.stem(word);
          System.out.println(" -> " + word);
        }
        

        if (!word.equals(""))
        {



          termToIndex.getDimension(word);
          
          termCounts.count(word);
        }
      } }
    document.close();
    System.out.printf("Saw %d terms, %d unique%n", new Object[] { Integer.valueOf(termCounts.sum()), Integer.valueOf(termCounts.size()) });
    




    if (termCounts.size() == 0) {
      return;
    }
    

    int totalNumberOfUniqueWords = termToIndex.numDimensions();
    

    SparseArray<Integer> documentColumn = 
      new SparseIntHashArray(totalNumberOfUniqueWords);
    for (Map.Entry<String, Integer> e : termCounts) {
      documentColumn.set(
        termToIndex.getDimension((String)e.getKey()), (Integer)e.getValue());
    }
    System.out.println(this + " processing doc " + documentColumn);
    


    termDocumentMatrixBuilder.addColumn(documentColumn);
  }
  























  public Vector getDocumentVector(int documentNumber)
  {
    throw new UnsupportedOperationException(
      "Getting the document vector is not supported by this class.");
  }
  








  public int documentSpaceSize()
  {
    return 0;
  }
  


  public Set<String> getWords()
  {
    return Collections.unmodifiableSet(termToIndex.keySet());
  }
  



  public Vector getVector(String word)
  {
    int index = termToIndex.getDimension(word);
    
    return index < 0 ? null : wordSpace.getRowVector(index);
  }
  


  public int getVectorLength()
  {
    return wordSpace.columns();
  }
  









  protected MatrixFile processSpace(Transform transform)
  {
    try
    {
      termDocumentMatrixBuilder.finish();
      

      File termDocumentMatrix = termDocumentMatrixBuilder.getFile();
      

      if (transform != null) {
        LoggerUtil.info(LOG, "performing %s transform", new Object[] { transform });
        
        LoggerUtil.verbose(
          LOG, "stored term-document matrix in format %s at %s", new Object[] {
          termDocumentMatrixBuilder.getMatrixFormat(), 
          termDocumentMatrix.getAbsolutePath() });
        

        termDocumentMatrix = transform.transform(
          termDocumentMatrix, 
          termDocumentMatrixBuilder.getMatrixFormat());
        
        LoggerUtil.verbose(
          LOG, "transformed matrix to %s", new Object[] {
          termDocumentMatrix.getAbsolutePath() });
      }
      
      return new MatrixFile(
        termDocumentMatrix, 
        termDocumentMatrixBuilder.getMatrixFormat());
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  
  protected void handleDocumentHeader(int docIndex, String header) {}
}
