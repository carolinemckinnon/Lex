package edu.ucla.sspace.tools;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.text.IteratorFactory;
import edu.ucla.sspace.util.HashMultiMap;
import edu.ucla.sspace.util.MultiMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;











































































public class ChildesParser
{
  private PrintWriter writer;
  private PrintWriter posWriter;
  private MultiMap<String, String> posTags;
  private final boolean generateAugmented;
  private final boolean separateByPeriod;
  private final boolean appendPosTags;
  private final boolean generateOneDoc;
  
  public ChildesParser(String outFile, String posOutFile, boolean generateAugmented, boolean separateByPeriod, boolean appendPosTags, boolean generateOneDoc)
  {
    this.generateAugmented = generateAugmented;
    this.separateByPeriod = separateByPeriod;
    this.appendPosTags = appendPosTags;
    this.generateOneDoc = generateOneDoc;
    posTags = new HashMultiMap();
    try {
      writer = new PrintWriter(outFile);
      posWriter = new PrintWriter(posOutFile);
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  


  private synchronized void print(String output)
  {
    if (generateOneDoc) {
      writer.print(output);
    } else {
      writer.println(output);
    }
  }
  



  public void parseFile(File file, boolean utterancePerDoc)
  {
    try
    {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(file);
      

      NodeList utterances = doc.getElementsByTagName("u");
      StringBuilder fileBuilder = new StringBuilder();
      for (int i = 0; i < utterances.getLength(); i++)
      {
        Element item = (Element)utterances.item(i);
        NodeList words = item.getElementsByTagName("w");
        StringBuilder utteranceBuilder = new StringBuilder();
        

        List<String> wordsInUtterance = 
          new ArrayList(words.getLength());
        for (int j = 0; j < words.getLength(); j++)
        {
          Element wordNode = (Element)words.item(j);
          NodeList posNodeList = wordNode.getElementsByTagName("pos");
          String word = wordNode.getFirstChild().getNodeValue();
          if (posNodeList.getLength() > 0) {
            Node posNode = 
              posNodeList.item(0).getFirstChild().getFirstChild();
            String pos = posNode.getNodeValue();
            posTags.put(word, pos);
            if (appendPosTags)
              word = word + "-" + pos;
          }
          wordsInUtterance.add(word);
        }
        









        NodeList auxNodes = item.getElementsByTagName("a");
        List<String> augmentedUtterances = new LinkedList();
        String auxNodeType; if (generateAugmented) {
          for (int j = 0; j < auxNodes.getLength(); j++)
          {
            Node n = auxNodes.item(j);
            auxNodeType = n.getAttributes()
              .getNamedItem("type").getNodeValue();
            



            if ((auxNodeType.equals("action")) || 
              (auxNodeType.equals("actions")) || 
              (auxNodeType.equals("addressee")) || 
              (auxNodeType.equals("comments")) || 
              (auxNodeType.equals("explanation")) || 
              (auxNodeType.equals("gesture")) || 
              (auxNodeType.equals("happening")) || 
              (auxNodeType.equals("situation")))
            {
              String commentOnUtterance = 
                n.getFirstChild().getNodeValue();
              


              Iterator<String> tokenIter = 
                IteratorFactory.tokenize(
                new BufferedReader(
                new StringReader(
                commentOnUtterance)));
              Iterator localIterator;
              for (; 
                  

                  tokenIter.hasNext(); 
                  
                  localIterator.hasNext())
              {
                String token = (String)tokenIter.next();
                localIterator = wordsInUtterance.iterator(); continue;String word = (String)localIterator.next();
                augmentedUtterances.add(word + " " + 
                  token + "-GROUNDING");
              }
            }
          }
        }
        


        Iterator<String> it = wordsInUtterance.iterator();
        while (it.hasNext()) {
          utteranceBuilder.append((String)it.next());
          if (it.hasNext())
            utteranceBuilder.append(" ");
        }
        String utterance = utteranceBuilder.toString();
        if (utterancePerDoc) {
          print(utterance);
          

          for (String aug : augmentedUtterances) {
            print(aug);
          }
        } else {
          fileBuilder.append(utterance);
          if (separateByPeriod)
            fileBuilder.append(".");
          fileBuilder.append(" ");
          




          for (String aug : augmentedUtterances) {
            print(aug);
          }
        }
      }
      
      if (!utterancePerDoc)
        print(fileBuilder.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  


  public void finish()
  {
    for (Map.Entry<String, String> entry : posTags.entrySet()) {
      posWriter.println((String)entry.getKey() + " " + (String)entry.getValue());
    }
    posWriter.flush();
    posWriter.close();
    
    writer.flush();
    writer.close();
  }
  

  public static void main(String[] args)
  {
    ArgOptions options = new ArgOptions();
    options.addOption('p', "partOfSpeechTag", 
      "If set, each token will be appended with it's part of speech tag, such as cat-noun", 
      
      false, null, "Optional");
    options.addOption('S', "separateByPeriod", 
      "If set, seperates sentences by periods", 
      false, null, "Optional");
    options.addOption('U', "utterancePerDoc", 
      "If set, one utterance is considered a document, otherwise all uterances in a file will be considered a document", 
      

      false, null, "Optional");
    options.addOption('g', "generateOneDoc", 
      "If set, only one document will be generated for all the text processed", 
      
      false, null, "Optional");
    
    options.addOption('A', "augmentedUtterances", 
      "Generates augmented utterances from comments about the utterances", 
      false, null, "Augmented");
    options.addOption('F', "augmentedUtterancesFilter", 
      "Specifes a token filter for which tokens in comments are used to generate augmented utterances", 
      
      true, "SPEC", "Augmented");
    
    options.addOption('d', "baseChildesDirectory", 
      "The base childes directory.  XML files will be searched for recursively from this base.  Use of this overrides the fileList option.", 
      

      true, "DIRECTORY", "Required (At least one of)");
    options.addOption('f', "fileList", 
      "The list of files to process", 
      true, "FILE[,FILE]*", "Required (At least one of)");
    


    options.parseOptions(args);
    if (((!options.hasOption("fileList")) && 
      (!options.hasOption("baseChildesDirectory"))) || 
      (options.numPositionalArgs() != 2)) {
      System.out.println(
        "usage: java ChildesParser [options] <outfile> <pos-file>\n" + 
        
        options.prettyPrint());
      return;
    }
    


    boolean utterancePerDoc = false;
    utterancePerDoc = options.hasOption("utterancePerDoc");
    
    boolean genAugmented = options.hasOption("augmentedUtterances");
    if ((genAugmented) && (options.hasOption("augmentedUtterancesFilter"))) {
      String filterConf = 
        options.getStringOption("augmentedUtterancesFilter");
      Properties p = System.getProperties();
      p.setProperty("edu.ucla.sspace.text.TokenizerFactory.tokenFilter", filterConf);
      IteratorFactory.setProperties(p);
    }
    
    ChildesParser parser = new ChildesParser(options.getPositionalArg(0), 
      options.getPositionalArg(1), 
      genAugmented, 
      options.hasOption('S'), 
      options.hasOption('p'), 
      options.hasOption('g'));
    

    if (options.hasOption("fileList")) {
      String[] files = options.getStringOption("fileList").split(",");
      for (String file : files) {
        parser.parseFile(new File(file), utterancePerDoc);
      }
    } else {
      File baseDir = 
        new File(options.getStringOption("baseChildesDirectory"));
      findXmlFiles(parser, utterancePerDoc, baseDir);
    }
    
    parser.finish();
  }
  




  public static void findXmlFiles(ChildesParser parser, boolean utterancePerDoc, File directory)
  {
    File[] files = directory.listFiles();
    for (File file : files) {
      if (file.isDirectory()) {
        findXmlFiles(parser, utterancePerDoc, file);
      } else if ((file.isFile()) && (file.getPath().endsWith(".xml"))) {
        parser.parseFile(file, utterancePerDoc);
      }
    }
  }
}
