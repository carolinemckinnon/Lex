package edu.ucla.sspace.graph.io;

import edu.ucla.sspace.graph.Edge;
import edu.ucla.sspace.graph.Graph;
import edu.ucla.sspace.graph.Multigraph;
import edu.ucla.sspace.graph.TypedEdge;
import edu.ucla.sspace.graph.WeightedEdge;
import edu.ucla.sspace.graph.WeightedGraph;
import edu.ucla.sspace.util.ColorGenerator;
import edu.ucla.sspace.util.Indexer;
import edu.ucla.sspace.util.primitive.IntIterator;
import edu.ucla.sspace.util.primitive.IntSet;
import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;







































public class GexfIO
{
  public GexfIO() {}
  
  public void write(Graph<? extends Edge> g, File gexfFile)
    throws IOException
  {
    write(g, gexfFile, null);
  }
  




  public void write(Graph<? extends Edge> g, File gexfFile, Indexer<String> vertexLabels)
    throws IOException
  {
    DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = null;
    try {
      docBuilder = dbfac.newDocumentBuilder();
    } catch (ParserConfigurationException pce) {
      throw new IOError(new IOException(pce));
    }
    Document doc = docBuilder.newDocument();
    
    Element root = doc.createElement("gexf");
    root.setAttribute("xmlns", "http://www.gexf.net/1.2draft");
    root.setAttribute("xmlns:viz", "http://www.gexf.net/1.2draft/viz");
    root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
    root.setAttribute("version", "1.2");
    root.setAttribute("xsi:schemaLocation", "http://www.gexf.net/1.2draft http://www.gexf.net/1.2draft/gexf.xsd");
    doc.appendChild(root);
    
    Element graph = doc.createElement("graph");
    graph.setAttribute("defaultedgetype", "undirected");
    root.appendChild(graph);
    
    Element nodes = doc.createElement("nodes");
    graph.appendChild(nodes);
    Element edges = doc.createElement("edges");
    graph.appendChild(edges);
    
    IntIterator vIter = g.vertices().iterator();
    String vLabel; while (vIter.hasNext()) {
      int vertex = ((Integer)vIter.next()).intValue();
      Element node = doc.createElement("node");
      vLabel = vertexLabels == null ? 
        String.valueOf(vertex) : 
        (String)vertexLabels.lookup(vertex);
      if (vLabel == null)
        vLabel = String.valueOf(vertex);
      node.setAttribute("id", vLabel);
      node.setAttribute("label", vLabel);
      nodes.appendChild(node);
    }
    
    int edgeId = 0;
    for (Edge e : g.edges()) {
      Element edge = doc.createElement("edge");
      edges.appendChild(edge);
      edge.setAttribute("id", edgeId++);
      
      String sourceLabel = vertexLabels == null ? 
        String.valueOf(e.from()) : 
        (String)vertexLabels.lookup(e.from());
      if (sourceLabel == null) {
        sourceLabel = String.valueOf(e.from());
      }
      String targetLabel = vertexLabels == null ? 
        String.valueOf(e.to()) : 
        (String)vertexLabels.lookup(e.to());
      if (targetLabel == null) {
        targetLabel = String.valueOf(e.to());
      }
      edge.setAttribute("source", sourceLabel);
      edge.setAttribute("target", targetLabel);
    }
    
    try
    {
      TransformerFactory transfac = TransformerFactory.newInstance();
      Transformer trans = transfac.newTransformer();
      trans.setOutputProperty("omit-xml-declaration", "yes");
      trans.setOutputProperty("indent", "yes");
      trans.setOutputProperty(
        "{http://xml.apache.org/xslt}indent-amount", "2");
      

      BufferedOutputStream bos = 
        new BufferedOutputStream(new FileOutputStream(gexfFile));
      StreamResult result = new StreamResult(bos);
      DOMSource source = new DOMSource(doc);
      trans.transform(source, result);
      bos.close();
    } catch (TransformerException te) {
      throw new IOError(new IOException(te));
    }
  }
  






  public <T, E extends TypedEdge<T>> void write(Multigraph<T, E> g, File f)
    throws IOException
  {
    write(g, f, null, false, null, false);
  }
  











  public <T, E extends TypedEdge<T>> void write(Multigraph<T, E> g, File f, Map<T, Color> edgeColors)
    throws IOException
  {
    write(g, f, edgeColors, true, null, false);
  }
  










  public <T, E extends TypedEdge<T>> void write(Multigraph<T, E> g, File f, Indexer<String> vertexLabels)
    throws IOException
  {
    write(g, f, null, false, vertexLabels, true);
  }
  














  public <T, E extends TypedEdge<T>> void write(Multigraph<T, E> g, File f, Map<T, Color> edgeColors, Indexer<String> vertexLabels)
    throws IOException
  {
    write(g, f, edgeColors, true, vertexLabels, true);
  }
  






  private <T, E extends TypedEdge<T>> void write(Multigraph<T, E> g, File f, Map<T, Color> edgeColors, boolean useColors, Indexer<String> vertexLabels, boolean useLabels)
    throws IOException
  {
    DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = null;
    try {
      docBuilder = dbfac.newDocumentBuilder();
    } catch (ParserConfigurationException pce) {
      throw new IOError(new IOException(pce));
    }
    Document doc = docBuilder.newDocument();
    
    Element root = doc.createElement("gexf");
    root.setAttribute("xmlns", "http://www.gexf.net/1.2draft");
    root.setAttribute("xmlns:viz", "http://www.gexf.net/1.2draft/viz");
    root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
    root.setAttribute("version", "1.2");
    root.setAttribute("xsi:schemaLocation", "http://www.gexf.net/1.2draft http://www.gexf.net/1.2draft/gexf.xsd");
    doc.appendChild(root);
    
    Element graph = doc.createElement("graph");
    graph.setAttribute("defaultedgetype", "undirected");
    root.appendChild(graph);
    
    Element nodes = doc.createElement("nodes");
    graph.appendChild(nodes);
    Element edges = doc.createElement("edges");
    graph.appendChild(edges);
    
    IntIterator vIter = g.vertices().iterator();
    while (vIter.hasNext()) {
      int vertex = ((Integer)vIter.next()).intValue();
      Element node = doc.createElement("node");
      node.setAttribute("id", String.valueOf(vertex));
      if (useLabels) {
        node.setAttribute("label", (String)vertexLabels.lookup(vertex));
      } else
        node.setAttribute("label", String.valueOf(vertex));
      nodes.appendChild(node);
    }
    
    ColorGenerator cg = null;
    if (useColors) {
      cg = new ColorGenerator();
    }
    int edgeId = 0;
    for (E e : g.edges()) {
      Element edge = doc.createElement("edge");
      edges.appendChild(edge);
      edge.setAttribute("id", edgeId++);
      edge.setAttribute("source", String.valueOf(e.from()));
      edge.setAttribute("target", String.valueOf(e.to()));
      edge.setAttribute("label", String.valueOf(e.edgeType()));
      if (useColors) {
        Element cEdge = doc.createElement("viz:color");
        edge.appendChild(cEdge);
        Color c = (Color)edgeColors.get(e.edgeType());
        if (c == null) {
          c = cg.next();
          edgeColors.put(e.edgeType(), c);
        }
        cEdge.setAttribute("r", String.valueOf(c.getRed()));
        cEdge.setAttribute("g", String.valueOf(c.getGreen()));
        cEdge.setAttribute("b", String.valueOf(c.getBlue()));
      }
    }
    
    try
    {
      TransformerFactory transfac = TransformerFactory.newInstance();
      Transformer trans = transfac.newTransformer();
      trans.setOutputProperty("omit-xml-declaration", "yes");
      trans.setOutputProperty("indent", "yes");
      trans.setOutputProperty(
        "{http://xml.apache.org/xslt}indent-amount", "2");
      

      BufferedOutputStream bos = 
        new BufferedOutputStream(new FileOutputStream(f));
      StreamResult result = new StreamResult(bos);
      DOMSource source = new DOMSource(doc);
      trans.transform(source, result);
      bos.close();
    } catch (TransformerException te) {
      throw new IOError(new IOException(te));
    }
  }
  


  public void write(WeightedGraph<? extends WeightedEdge> g, File gexfFile)
    throws IOException
  {
    write(g, gexfFile, null);
  }
  




  public void write(WeightedGraph<? extends WeightedEdge> g, File gexfFile, Indexer<String> vertexLabels)
    throws IOException
  {
    DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = null;
    try {
      docBuilder = dbfac.newDocumentBuilder();
    } catch (ParserConfigurationException pce) {
      throw new IOError(new IOException(pce));
    }
    Document doc = docBuilder.newDocument();
    
    Element root = doc.createElement("gexf");
    root.setAttribute("xmlns", "http://www.gexf.net/1.2draft");
    root.setAttribute("xmlns:viz", "http://www.gexf.net/1.2draft/viz");
    root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
    root.setAttribute("version", "1.2");
    root.setAttribute("xsi:schemaLocation", "http://www.gexf.net/1.2draft http://www.gexf.net/1.2draft/gexf.xsd");
    doc.appendChild(root);
    
    Element graph = doc.createElement("graph");
    graph.setAttribute("defaultedgetype", "undirected");
    root.appendChild(graph);
    
    Element nodes = doc.createElement("nodes");
    graph.appendChild(nodes);
    Element edges = doc.createElement("edges");
    graph.appendChild(edges);
    
    IntIterator vIter = g.vertices().iterator();
    String vLabel; while (vIter.hasNext()) {
      int vertex = ((Integer)vIter.next()).intValue();
      Element node = doc.createElement("node");
      vLabel = vertexLabels == null ? 
        String.valueOf(vertex) : 
        (String)vertexLabels.lookup(vertex);
      if (vLabel == null)
        vLabel = String.valueOf(vertex);
      node.setAttribute("id", vLabel);
      node.setAttribute("label", vLabel);
      nodes.appendChild(node);
    }
    
    int edgeId = 0;
    for (WeightedEdge e : g.edges()) {
      Element edge = doc.createElement("edge");
      edges.appendChild(edge);
      edge.setAttribute("id", edgeId++);
      
      String sourceLabel = vertexLabels == null ? 
        String.valueOf(e.from()) : 
        (String)vertexLabels.lookup(e.from());
      if (sourceLabel == null) {
        sourceLabel = String.valueOf(e.from());
      }
      String targetLabel = vertexLabels == null ? 
        String.valueOf(e.to()) : 
        (String)vertexLabels.lookup(e.to());
      if (targetLabel == null) {
        targetLabel = String.valueOf(e.to());
      }
      edge.setAttribute("source", sourceLabel);
      edge.setAttribute("target", targetLabel);
      edge.setAttribute("weight", String.valueOf(e.weight()));
    }
    
    try
    {
      TransformerFactory transfac = TransformerFactory.newInstance();
      Transformer trans = transfac.newTransformer();
      trans.setOutputProperty("omit-xml-declaration", "yes");
      trans.setOutputProperty("indent", "yes");
      trans.setOutputProperty(
        "{http://xml.apache.org/xslt}indent-amount", "2");
      

      BufferedOutputStream bos = 
        new BufferedOutputStream(new FileOutputStream(gexfFile));
      StreamResult result = new StreamResult(bos);
      DOMSource source = new DOMSource(doc);
      trans.transform(source, result);
      bos.close();
    } catch (TransformerException te) {
      throw new IOError(new IOException(te));
    }
  }
}
