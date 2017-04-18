package edu.ucla.sspace.text;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



























public class ChildesCorpusReader
  extends DirectoryCorpusReader<Document>
{
  public ChildesCorpusReader() {}
  
  protected Iterator<Document> corpusIterator(Iterator<File> fileIter)
  {
    return new InnerIterator(fileIter);
  }
  


  public class InnerIterator
    extends DirectoryCorpusReader<Document>.BaseFileIterator
  {
    private NodeList utterances;
    

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
        org.w3c.dom.Document currentXmlDoc = db.parse(currentDocName);
        utterances = currentXmlDoc.getElementsByTagName("u");
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
      if (currentNodeIndex >= utterances.getLength()) {
        return null;
      }
      StringBuilder utteranceBuilder = new StringBuilder();
      addTextFromUtterance((Element)utterances.item(currentNodeIndex++), 
        utteranceBuilder);
      return new StringDocument(utteranceBuilder.toString());
    }
    



    private void addTextFromUtterance(Element utterance, StringBuilder utteranceBuilder)
    {
      NodeList words = utterance.getElementsByTagName("w");
      
      for (int j = 0; j < words.getLength(); j++) {
        Element wordNode = (Element)words.item(j);
        String word = wordNode.getFirstChild().getNodeValue();
        utteranceBuilder.append(word).append(" ");
      }
    }
  }
}
