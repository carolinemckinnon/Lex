package edu.ucla.sspace.text.corpora;

import edu.ucla.sspace.text.DirectoryCorpusReader;
import edu.ucla.sspace.text.DirectoryCorpusReader.BaseFileIterator;
import edu.ucla.sspace.text.DocumentPreprocessor;
import edu.ucla.sspace.text.StringDocument;
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
  extends DirectoryCorpusReader<edu.ucla.sspace.text.Document>
{
  public ChildesCorpusReader() {}
  
  public ChildesCorpusReader(DocumentPreprocessor preprocessor)
  {
    super(preprocessor);
  }
  


  protected Iterator<edu.ucla.sspace.text.Document> corpusIterator(Iterator<File> files)
  {
    return new ChildesFileIterator(files);
  }
  



  public class ChildesFileIterator
    extends DirectoryCorpusReader<edu.ucla.sspace.text.Document>.BaseFileIterator
  {
    private NodeList utterances;
    


    private int currentNodeIndex;
    


    private boolean oneUtterancePerDoc;
    


    private final DocumentBuilder db;
    


    public ChildesFileIterator()
    {
      super(files);
      oneUtterancePerDoc = true;
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder d = null;
      try {
        d = dbf.newDocumentBuilder();
      } catch (ParserConfigurationException pce) {
        pce.printStackTrace();
      }
      db = d;
    }
    


    protected void setupCurrentDoc(File currentDocName)
    {
      try
      {
        org.w3c.dom.Document currentXmlDoc = db.parse(currentDocName);
        utterances = currentXmlDoc.getElementsByTagName("u");
        currentNodeIndex = 0;
      } catch (SAXException saxe) {
        saxe.printStackTrace();
      } catch (IOException ioe) {
        throw new IOError(ioe);
      }
    }
    



    protected edu.ucla.sspace.text.Document advanceInDoc()
    {
      if (currentNodeIndex >= utterances.getLength()) {
        return null;
      }
      StringBuilder utteranceBuilder = new StringBuilder();
      if (oneUtterancePerDoc) {
        addTextFromUtterance(
          (Element)utterances.item(currentNodeIndex++), 
          utteranceBuilder);
      } else {
        for (int i = 0; i < utterances.getLength(); i++) {
          addTextFromUtterance((Element)utterances.item(i), 
            utteranceBuilder);
          utteranceBuilder.append(". ");
        }
      }
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
