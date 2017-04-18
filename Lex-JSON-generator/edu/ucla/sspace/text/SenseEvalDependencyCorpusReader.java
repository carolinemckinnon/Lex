package edu.ucla.sspace.text;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
























public class SenseEvalDependencyCorpusReader
  extends DirectoryCorpusReader<Document>
{
  public SenseEvalDependencyCorpusReader() {}
  
  protected Iterator<Document> corpusIterator(Iterator<File> fileIter)
  {
    return new InnerIterator(fileIter);
  }
  


  public class InnerIterator
    extends DirectoryCorpusReader<Document>.BaseFileIterator
  {
    private NodeList instances;
    

    private int currentNodeIndex;
    

    public InnerIterator()
    {
      super(files);
    }
    

    protected void setupCurrentDoc(File currentDocName)
    {
      try
      {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        System.out.println(currentDocName);
        org.w3c.dom.Document currentXmlDoc = db.parse(currentDocName);
        instances = currentXmlDoc.getElementsByTagName("instance");
        currentNodeIndex = 0;
      } catch (ParserConfigurationException pce) {
        pce.printStackTrace();
      } catch (SAXException saxe) {
        saxe.printStackTrace();
      } catch (IOException ioe) {
        throw new IOError(ioe);
      }
    }
    



    protected Document advanceInDoc()
    {
      if (currentNodeIndex >= instances.getLength())
        return null;
      Element instance = (Element)instances.item(currentNodeIndex++);
      String instanceId = instance.getAttribute("id");
      String word = instance.getAttribute("name");
      String text = instance.getFirstChild().getNodeValue();
      
      return new StringDocument(String.format("%s\n%s\n%s", new Object[] { instanceId, word, text }));
    }
  }
}
