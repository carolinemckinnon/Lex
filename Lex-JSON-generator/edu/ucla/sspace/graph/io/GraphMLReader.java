package edu.ucla.sspace.graph.io;

import edu.ucla.sspace.graph.SimpleWeightedDirectedTypedEdge;
import edu.ucla.sspace.graph.WeightedDirectedMultigraph;
import edu.ucla.sspace.graph.WeightedDirectedTypedEdge;
import edu.ucla.sspace.util.Indexer;
import edu.ucla.sspace.util.LoggerUtil;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.ParserAdapter;



































































public class GraphMLReader
  extends GraphReaderAdapter
  implements GraphReader
{
  private static final Logger LOGGER = Logger.getLogger(GraphMLReader.class.getName());
  
  public GraphMLReader() {}
  
  public WeightedDirectedMultigraph<String> readWeightedDirectedMultigraph(File f, Indexer<String> vertexLabels)
    throws IOException
  {
    try
    {
      SAXParserFactory spf = SAXParserFactory.newInstance();
      SAXParser sp = spf.newSAXParser();
      ParserAdapter pa = new ParserAdapter(sp.getParser());
      
      GraphMLParser parser = new GraphMLParser(vertexLabels);
      
      pa.setContentHandler(parser);
      pa.setErrorHandler(parser);
      pa.parse(new InputSource(new BufferedInputStream(new FileInputStream(f))));
      return g;
    } catch (SAXException saxe) {
      throw new IOException(saxe);
    } catch (ParserConfigurationException saxe) {
      throw new IOException(saxe);
    }
  }
  
  public WeightedDirectedMultigraph<String> readWeightedDirectedMultigraphFromDOM(File f, Indexer<String> vertexLabels) throws IOException
  {
    try
    {
      DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbfac.newDocumentBuilder();
      Document graphDoc = db.parse(f);
      LoggerUtil.verbose(LOGGER, "Finished parsing %s", new Object[] { f });
      





      NodeList graphElemList = graphDoc.getElementsByTagName("graph");
      if (graphElemList.getLength() == 0)
        throw new IOException("Missing <graph> element");
      if (graphElemList.getLength() > 1)
        LOGGER.warning(f + " has more than one <graph> element" + 
          "; returning only the first");
      Element graphElem = (Element)graphElemList.item(0);
      
      String weightKeyId = null;
      String typeKeyId = null;
      
      WeightedDirectedMultigraph<String> g = 
        new WeightedDirectedMultigraph();
      
      NodeList keyElemList = graphElem.getElementsByTagName("key");
      for (int i = 0; i < keyElemList.getLength(); i++) {
        Element key = (Element)keyElemList.item(i);
        if (key.getAttribute("for").equals("edge")) {
          if (key.getAttribute("attr.name").equals("weight")) {
            weightKeyId = key.getAttribute("id");
          } else if (key.getAttribute("attr.name").equals("type")) {
            typeKeyId = key.getAttribute("id");
          }
        }
      }
      NodeList nodeElemList = graphElem.getElementsByTagName("node");
      for (int i = 0; i < nodeElemList.getLength(); i++) {
        Element node = (Element)nodeElemList.item(i);
        String id = node.getAttribute("id");
        g.add(vertexLabels.index(id));
        if ((i + 1) % 1000 == 0)
          LoggerUtil.verbose(LOGGER, "Added %d vertices", new Object[] { Integer.valueOf(i) });
      }
      LoggerUtil.verbose(LOGGER, "Found %d total vertices", new Object[] { Integer.valueOf(g.order()) });
      
      NodeList edgeElemList = graphElem.getElementsByTagName("edge");
      for (int i = 0; i < edgeElemList.getLength(); i++) {
        Element edge = (Element)edgeElemList.item(i);
        String fromId = edge.getAttribute("source");
        String toId = edge.getAttribute("target");
        

        String weightStr = null;
        String type = null;
        
        NodeList dataElemList = edge.getElementsByTagName("data");
        for (int j = 0; j < dataElemList.getLength(); j++) {
          Element data = (Element)dataElemList.item(j);
          if (data.getAttribute("key").equals(weightKeyId)) {
            weightStr = data.getTextContent();
          } else if (data.getAttribute("key").equals(typeKeyId)) {
            type = data.getTextContent();
          }
        }
        if (weightStr == null)
          throw new IOException("No weight specified for edge " + 
            edge.getAttribute("id"));
        if (type == null) {
          throw new IOException("No type specified for edge " + 
            edge.getAttribute("id"));
        }
        int from = vertexLabels.find(fromId);
        if (from < 0)
          throw new IOException("Unknown source node for edge " + 
            edge.getAttribute("id") + ": " + fromId);
        int to = vertexLabels.find(toId);
        if (to < 0) {
          throw new IOException("Unknown target node for edge " + 
            edge.getAttribute("id") + ": " + toId);
        }
        double weight = 0.0D;
        try {
          weight = Double.parseDouble(weightStr);
        } catch (NumberFormatException nfe) {
          throw new IOException("Invalid weight for edge " + 
            edge.getAttribute("id") + ": " + weightStr);
        }
        
        WeightedDirectedTypedEdge<String> e = 
          new SimpleWeightedDirectedTypedEdge(
          type, from, to, weight);
        g.add(e);
        if ((i + 1) % 1000 == 0) {
          LoggerUtil.verbose(LOGGER, "Added %d edges", new Object[] { Integer.valueOf(i) });
        }
      }
      
      LoggerUtil.verbose(LOGGER, "Loaded a directed, weighted multigraph with %d vertices and %d edges", new Object[] {
        Integer.valueOf(g.order()), Integer.valueOf(g.size()) });
      return g;
    }
    catch (IOException ioe) {
      throw ioe;
    }
    catch (Exception e)
    {
      throw new IOException(e);
    }
  }
  

  public class GraphMLParser
    extends DefaultHandler
  {
    private int from;
    
    private int to;
    
    private String type;
    
    private double weight;
    
    private static final String NODE = "node";
    private static final String EDGE = "edge";
    private static final String DATA = "data";
    private final WeightedDirectedMultigraph<String> g;
    private final Indexer<String> vertexLabels;
    private String weightKeyId;
    private String typeKeyId;
    private String curDataKey = null;
    private String curData = null;
    
    GraphMLParser() {
      this.vertexLabels = vertexLabels;
      g = new WeightedDirectedMultigraph();
    }
    
    public void startDocument() {}
    
    public void endDocument() throws SAXException {
      LoggerUtil.verbose(GraphMLReader.LOGGER, "Loaded a directed, weighted multigraph with %d vertices and %d edges", new Object[] {
        Integer.valueOf(g.order()), Integer.valueOf(g.size()) });
    }
    
    public void startElement(String namespace, String localName, String qName, Attributes atts) throws SAXException
    {
      if (qName.equals("key")) {
        if (atts.getValue("for").equals("edge")) {
          if (atts.getValue("attr.name").equals("weight")) {
            weightKeyId = atts.getValue("id");
          } else if (atts.getValue("attr.name").equals("type")) {
            typeKeyId = atts.getValue("id");
          }
        }
      } else if (qName.equals("node")) {
        String id = atts.getValue("id");
        g.add(vertexLabels.index(id));
        if (g.order() % 1000 == 0) {
          LoggerUtil.verbose(GraphMLReader.LOGGER, "Added %d vertices", new Object[] { Integer.valueOf(g.order()) });
        }
      } else if (qName.equals("edge")) {
        String fromId = atts.getValue("source");
        String toId = atts.getValue("target");
        
        from = vertexLabels.find(fromId);
        if (from < 0)
          throw new SAXException("Unknown source node for edge " + 
            fromId);
        to = vertexLabels.find(toId);
        if (to < 0) {
          throw new SAXException("Unknown target node for edge " + 
            toId);
        }
      } else if (qName.equals("data")) {
        curDataKey = atts.getValue("key");
      }
    }
    
    public void characters(char[] ch, int start, int length) {
      curData = new String(ch, start, length);
    }
    
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
      if (qName.equals("edge")) {
        if (from == to)
          return;
        WeightedDirectedTypedEdge<String> e = 
          new SimpleWeightedDirectedTypedEdge(
          type, from, to, weight);
        g.add(e);
        if (g.size() % 1000 == 0) {
          LoggerUtil.verbose(GraphMLReader.LOGGER, "Added %d edges", new Object[] { Integer.valueOf(g.size()) });
        }
      }
      else if (qName.equals("data")) {
        if (curDataKey.equals(weightKeyId)) {
          try {
            weight = Double.parseDouble(curData);
          } catch (NumberFormatException nfe) {
            throw new SAXException("Invalid weight: " + curData);
          }
          
        } else if (curDataKey.equals(typeKeyId)) {
          type = curData;
        }
      }
    }
  }
}
