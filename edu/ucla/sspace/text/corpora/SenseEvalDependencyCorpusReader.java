package edu.ucla.sspace.text.corpora;

import edu.ucla.sspace.text.CorpusReader;
import edu.ucla.sspace.text.StringDocument;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;











































public class SenseEvalDependencyCorpusReader
  implements CorpusReader<edu.ucla.sspace.text.Document>
{
  public SenseEvalDependencyCorpusReader() {}
  
  public Iterator<edu.ucla.sspace.text.Document> read(File file)
  {
    try
    {
      return read(new FileReader(file));
    } catch (FileNotFoundException fnfe) {
      throw new IOError(fnfe);
    }
  }
  

  public Iterator<edu.ucla.sspace.text.Document> read(Reader docReader)
  {
    try
    {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      org.w3c.dom.Document currentXmlDoc = db.parse(
        new InputSource(docReader));
      return new SenseEvalIterator(
        currentXmlDoc.getElementsByTagName("instance"));
    } catch (ParserConfigurationException pce) {
      pce.printStackTrace();
    } catch (SAXException saxe) {
      saxe.printStackTrace();
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
    return null;
  }
  



  public class SenseEvalIterator
    implements Iterator<edu.ucla.sspace.text.Document>
  {
    private NodeList instances;
    

    private int currentNodeIndex;
    

    private String currentDoc;
    


    public SenseEvalIterator(NodeList instances)
    {
      this.instances = instances;
      currentDoc = null;
      currentNodeIndex = 0;
    }
    


    public synchronized boolean hasNext()
    {
      return currentDoc != null;
    }
    


    public synchronized edu.ucla.sspace.text.Document next()
    {
      edu.ucla.sspace.text.Document doc = new StringDocument(currentDoc);
      currentDoc = advance();
      return doc;
    }
    


    public synchronized void remove()
    {
      throw new UnsupportedOperationException("Remove not permitted.");
    }
    



    protected String advance()
    {
      if (currentNodeIndex >= instances.getLength())
        return null;
      Element instance = (Element)instances.item(currentNodeIndex++);
      String instanceId = instance.getAttribute("id");
      String word = instance.getAttribute("name");
      String text = instance.getFirstChild().getNodeValue();
      
      return String.format("%s\n%s\n%s", new Object[] { instanceId, word, text });
    }
  }
}
