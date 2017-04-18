package edu.ucla.sspace.dependency;

import edu.ucla.sspace.text.Stemmer;
import edu.ucla.sspace.text.TokenFilter;
import edu.ucla.sspace.util.Duple;
import edu.ucla.sspace.util.HashMultiMap;
import edu.ucla.sspace.util.MultiMap;
import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;




























































































public class CoNLLDependencyExtractor
  implements DependencyExtractor
{
  private final TokenFilter filter;
  private final Stemmer stemmer;
  private final int idIndex;
  private final int formIndex;
  private final int lemmaIndex;
  private final int posIndex;
  private final int parentIndex;
  private final int relationIndex;
  
  public CoNLLDependencyExtractor()
  {
    this(null, null);
  }
  




  public CoNLLDependencyExtractor(TokenFilter filter, Stemmer stemmer)
  {
    this.filter = filter;
    this.stemmer = stemmer;
    
    idIndex = 0;
    formIndex = 1;
    lemmaIndex = 2;
    posIndex = 3;
    parentIndex = 6;
    relationIndex = 7;
  }
  







  public CoNLLDependencyExtractor(TokenFilter filter, Stemmer stemmer, int idIndex, int formIndex, int lemmaIndex, int posIndex, int parentIndex, int relationIndex)
  {
    this.filter = filter;
    this.stemmer = stemmer;
    
    this.idIndex = idIndex;
    this.formIndex = formIndex;
    this.lemmaIndex = lemmaIndex;
    this.posIndex = posIndex;
    this.parentIndex = parentIndex;
    this.relationIndex = relationIndex;
  }
  




  public CoNLLDependencyExtractor(String configFile)
  {
    this(configFile, null, null);
  }
  






  public CoNLLDependencyExtractor(String configFile, TokenFilter filter, Stemmer stemmer)
  {
    this.filter = filter;
    this.stemmer = stemmer;
    

    int id = 0;
    int form = 1;
    int lemma = 2;
    int pos = 3;
    int head = 4;
    int rel = 5;
    
    try
    {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document currentXmlDoc = db.parse(configFile);
      

      NodeList columnList = currentXmlDoc.getElementsByTagName("column");
      for (int i = 0; i < columnList.getLength(); i++) {
        Element column = (Element)columnList.item(i);
        String name = column.getAttribute("name");
        



        if (name.equals("ID"))
          id = i;
        if (name.equals("FORM"))
          form = i;
        if (name.equals("LEMMA"))
          lemma = i;
        if (name.equals("POSTAG"))
          pos = i;
        if (name.equals("HEAD"))
          head = i;
        if (name.equals("DEPREL"))
          rel = i;
      }
    } catch (ParserConfigurationException pce) {
      pce.printStackTrace();
    } catch (SAXException saxe) {
      saxe.printStackTrace();
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
    

    idIndex = id;
    formIndex = form;
    lemmaIndex = lemma;
    posIndex = pos;
    parentIndex = head;
    relationIndex = rel;
  }
  




















  public DependencyTreeNode[] readNextTree(BufferedReader reader)
    throws IOException
  {
    List<SimpleDependencyTreeNode> nodes = 
      new ArrayList();
    





    MultiMap<Integer, Duple<Integer, String>> relationsToAdd = 
      new HashMultiMap();
    
    StringBuilder sb = new StringBuilder();
    


    int id = 0;
    int offset = 0;
    String[] nodeFeatures; for (String line = null; (line = reader.readLine()) != null;) {
      line = line.trim();
      


      if ((line.length() != 0) || (nodes.size() != 0))
      {




        if (line.length() == 0) {
          break;
        }
        sb.append(line).append("\n");
        

        nodeFeatures = line.split("\t");
        

        if (nodeFeatures.length - 1 < idIndex) {
          reader.close();
          throw new IllegalStateException("While parsing, found line with too few columns.  Missing node id columns.  Offending line:\n" + 
          
            line);
        }
        



        int realId = Integer.parseInt(nodeFeatures[idIndex]);
        if (((realId == 0) && (nodes.size() != offset)) || (
          (realId == 1) && 
          (nodes.size() != offset) && (nodes.size() != offset + 1))) {
          offset = nodes.size();
        }
        
        int parent = 
          Integer.parseInt(nodeFeatures[parentIndex]) - 1 + offset;
        
        String word = getWord(nodeFeatures);
        
        String lemma = getLemma(nodeFeatures, word);
        

        String pos = nodeFeatures[posIndex];
        

        String rel = nodeFeatures[relationIndex];
        

        SimpleDependencyTreeNode curNode = 
          new SimpleDependencyTreeNode(word, pos, lemma, id);
        



        if (parent - offset > 0)
        {

          if (parent < nodes.size()) {
            SimpleDependencyTreeNode parentNode = (SimpleDependencyTreeNode)nodes.get(parent);
            DependencyRelation r = new SimpleDependencyRelation(
              parentNode, rel, curNode);
            parentNode.addNeighbor(r);
            curNode.addNeighbor(r);

          }
          else
          {
            relationsToAdd.put(Integer.valueOf(id), 
              new Duple(Integer.valueOf(parent), rel));
          }
        }
        

        nodes.add(curNode);
        id++;
      }
    }
    if (nodes.size() == 0) {
      return null;
    }
    if (relationsToAdd.size() > 0)
    {


      nodeFeatures = relationsToAdd.entrySet().iterator();
      while (nodeFeatures.hasNext()) {
        Map.Entry<Integer, Duple<Integer, String>> parentAndRel = (Map.Entry)nodeFeatures.next();
        SimpleDependencyTreeNode dep = (SimpleDependencyTreeNode)nodes.get(((Integer)parentAndRel.getKey()).intValue());
        Duple<Integer, String> d = (Duple)parentAndRel.getValue();
        SimpleDependencyTreeNode head = (SimpleDependencyTreeNode)nodes.get(((Integer)x).intValue());
        DependencyRelation r = new SimpleDependencyRelation(
          head, (String)y, dep);
        head.addNeighbor(r);
        dep.addNeighbor(r);
      }
    }
    
    return (DependencyTreeNode[])nodes.toArray(
      new SimpleDependencyTreeNode[nodes.size()]);
  }
  







  private String getWord(String[] nodeFeatures)
  {
    String word = nodeFeatures[formIndex];
    
    if ((filter != null) && (!filter.accept(word)))
      return "";
    return word;
  }
  
  private String getLemma(String[] nodeFeatures, String word)
  {
    String lemma = nodeFeatures[lemmaIndex];
    if (lemma.equals("_"))
      return stemmer == null ? word : stemmer.stem(word);
    return lemma;
  }
}
