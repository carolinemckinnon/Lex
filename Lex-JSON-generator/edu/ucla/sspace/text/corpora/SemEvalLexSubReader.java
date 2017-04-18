package edu.ucla.sspace.text.corpora;

import edu.ucla.sspace.text.CorpusReader;
import edu.ucla.sspace.text.Document;
import edu.ucla.sspace.text.StringDocument;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.helpers.DefaultHandler;




























public class SemEvalLexSubReader
  extends DefaultHandler
  implements CorpusReader<Document>
{
  public SemEvalLexSubReader() {}
  
  public Iterator<Document> read(Reader reader)
  {
    SAXParserFactory saxfac = SAXParserFactory.newInstance();
    saxfac.setValidating(false);
    
    try
    {
      saxfac.setFeature("http://xml.org/sax/features/validation", false);
      saxfac.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
      saxfac.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
      saxfac.setFeature("http://xml.org/sax/features/external-general-entities", false);
      saxfac.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
      SAXParser saxParser = saxfac.newSAXParser();
      SemEvalHandler handler = new SemEvalHandler();
      saxParser.parse(new InputSource(reader), handler);
      return contexts.iterator();
    } catch (IOException ioe) {
      throw new IOError(ioe);
    } catch (SAXNotRecognizedException e1) {
      throw new RuntimeException(e1);
    } catch (SAXNotSupportedException e1) {
      throw new RuntimeException(e1);
    } catch (ParserConfigurationException e1) {
      throw new RuntimeException(e1);
    } catch (SAXException e) {
      throw new RuntimeException(e);
    }
  }
  

  public Iterator<Document> read(File file)
  {
    try
    {
      return read(new FileReader(file));
    } catch (FileNotFoundException fnfe) {
      throw new IOError(fnfe);
    }
  }
  




  public class SemEvalHandler
    extends DefaultHandler
  {
    List<Document> contexts;
    


    private boolean inContext;
    


    private boolean inHead;
    


    private boolean inLexElement;
    


    private String instanceId;
    


    private String lexicalId;
    


    private StringBuilder context;
    



    public SemEvalHandler()
    {
      contexts = new ArrayList();
      inContext = false;
      inHead = false;
      inLexElement = false;
      context = new StringBuilder();
    }
    




    public void startElement(String uri, String localName, String name, Attributes atts)
      throws SAXException
    {
      if (name.equals("lexelt")) {
        inLexElement = true;
        lexicalId = atts.getValue("item");
      } else if (name.equals("instance")) {
        instanceId = (atts.getValue("id") + " ");
        context.append(lexicalId).append("_").append(instanceId);
      } else if (name.equals("context")) {
        inContext = true;
      } else if (name.equals("head")) {
        inHead = true;
      }
    }
    



    public void endElement(String uri, String localName, String name)
      throws SAXException
    {
      if (name.equals("head")) {
        inHead = false;
      } else if (name.equals("lexelt")) {
        inLexElement = false;
      } else if (name.equals("context")) {
        inContext = false;
        contexts.add(new StringDocument(context.toString()));
        context.setLength(0);
      }
    }
    



    public void characters(char[] ch, int start, int length)
      throws SAXException
    {
      if (inContext)
        context.append(new String(ch, start, length));
      if (inHead) {
        context.append(" |||| ").append(new String(ch, start, length));
      }
    }
  }
}
