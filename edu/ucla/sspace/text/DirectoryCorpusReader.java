package edu.ucla.sspace.text;

import edu.ucla.sspace.util.DirectoryWalker;
import java.io.File;
import java.io.Reader;
import java.util.Iterator;






















































public abstract class DirectoryCorpusReader<D extends Document>
  implements CorpusReader<D>
{
  private final DocumentPreprocessor processor;
  
  public DirectoryCorpusReader()
  {
    this(null);
  }
  



  public DirectoryCorpusReader(DocumentPreprocessor processor)
  {
    this.processor = processor;
  }
  







  public Iterator<D> read(File dir)
  {
    return corpusIterator(new DirectoryWalker(dir).iterator());
  }
  


  public Iterator<D> read(Reader reader)
  {
    throw new UnsupportedOperationException(
      "The DirectoryCorpusReader cannot convert a reader to a directory structur.");
  }
  





  protected abstract Iterator<D> corpusIterator(Iterator<File> paramIterator);
  





  public void initialize(Reader baseReader)
  {
    throw new UnsupportedOperationException(
      "Cannot form a DirectoryCorpusReader from a Reader instance");
  }
  



  public abstract class BaseFileIterator
    implements Iterator<D>
  {
    private Iterator<File> filesToExplore;
    


    private D nextDoc;
    


    public BaseFileIterator()
    {
      this.filesToExplore = filesToExplore;
      nextDoc = null;
    }
    


    public boolean hasNext()
    {
      return nextDoc != null;
    }
    


    public D next()
    {
      D doc = nextDoc;
      nextDoc = advance();
      return doc;
    }
    


    public void remove()
    {
      throw new UnsupportedOperationException("Remove not permitted.");
    }
    





    protected abstract D advanceInDoc();
    




    protected abstract void setupCurrentDoc(File paramFile);
    




    protected String cleanDoc(String document)
    {
      return processor != null ? processor.process(document) : document;
    }
    




    protected D advance()
    {
      D newDoc = advanceInDoc();
      if (newDoc == null)
      {

        if (!filesToExplore.hasNext()) {
          return null;
        }
        
        setupCurrentDoc((File)filesToExplore.next());
        


        return advance();
      }
      return newDoc;
    }
  }
}
