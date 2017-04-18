package edu.ucla.sspace.grefenstette;

import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.matrix.GrowingSparseMatrix;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.util.Pair;
import edu.ucla.sspace.vector.Vector;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;















































public class Grefenstette
  implements SemanticSpace
{
  private static final Logger LOGGER = Logger.getLogger(Grefenstette.class.getName());
  




  private final File wordRelations;
  




  private final PrintWriter wordRelationsWriter;
  




  private final Map<String, Integer> objectTable;
  




  private final Map<String, Integer> attributeTable;
  




  private final Matrix syntacticCooccurrence;
  




  private final AtomicInteger objectCounter;
  




  private final AtomicInteger attributeCounter;
  




  public Grefenstette()
  {
    try
    {
      wordRelations = File.createTempFile("word-relation-list", "txt");
      wordRelationsWriter = new PrintWriter(wordRelations);
      
      objectTable = new HashMap();
      attributeTable = new HashMap();
      
      syntacticCooccurrence = new GrowingSparseMatrix();
      
      objectCounter = new AtomicInteger(0);
      attributeCounter = new AtomicInteger(0);
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  


  public void processDocument(BufferedReader document)
  {
    ArrayList<Pair<String>> wordsInPhrase = new ArrayList();
    
    String nounPhrase = "";
    String lastNoun = "";
    String lastVerb = "";
    String secondPrevPhrase = "";
    String prevPhrase = "";
    try
    {
      nounPhrase = document.readLine();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    for (String tag = getNextTag(nounPhrase); 
        tag != null; tag = getNextTag(nounPhrase))
    {

      int startOfTag = nounPhrase.indexOf(tag);
      
      nounPhrase = nounPhrase.substring(startOfTag);
      wordsInPhrase.clear();
      
      if (tag.equals("NP")) {
        while (nounPhrase.charAt(0) != ')')
        {

          tag = getNextTag(nounPhrase);
          
          if ((isPhraseOrClause(tag)) || (isPreposition(tag))) {
            nounPhrase = 
              nounPhrase.substring(nounPhrase.indexOf(tag) + tag.length());
            
            break; }
          if ((inStartSet(tag)) || (inReceiveSet(tag))) {
            try
            {
              String word = nounPhrase
                .substring(nounPhrase.indexOf(" ", 
                nounPhrase.indexOf(tag)) + 1, 
                nounPhrase.indexOf(")"));
              
              wordsInPhrase.add(new Pair(tag, word));
              
              nounPhrase = nounPhrase
                .substring(nounPhrase.indexOf(")", 
                nounPhrase.indexOf(word)) + 1);
            } catch (StringIndexOutOfBoundsException e) {
              nounPhrase = nounPhrase.substring(nounPhrase.indexOf(")"));
            }
            
          } else {
            nounPhrase = nounPhrase.substring(nounPhrase.indexOf(")") + 1);
          }
        }
        


        if (!wordsInPhrase.isEmpty())
        {
          String headNoun = (String)getsize1y;
          

          if ((prevPhrase.equals("PP")) && (secondPrevPhrase.equals("NP")) && 
            (lastNoun.length() != 0)) {
            wordRelationsWriter.println(lastNoun + " " + headNoun);
            
            addRelation(lastNoun, headNoun);
          }
          

          if ((prevPhrase.equals("PP")) && (secondPrevPhrase.equals("VP")) && 
            (lastVerb.length() != 0))
          {
            wordRelationsWriter.println(lastVerb + " " + headNoun);
            
            addRelation(lastVerb, headNoun);
          } else if (prevPhrase.equals("VP"))
          {
            wordRelationsWriter.println(lastVerb + " " + headNoun);
            
            addRelation(lastVerb, headNoun);
          }
          
          lastNoun = headNoun;
        }
        

        if (nounPhrase.charAt(0) == ')')
        {

          processWordsInNP(wordsInPhrase);
          
          if (!"NP".equals(prevPhrase)) {
            secondPrevPhrase = prevPhrase;
            prevPhrase = "NP";
          }
        }
      }
      else if (tag.equals("VP")) {
        while ((tag != null) && (tag.startsWith("V")))
        {
          if (tag.startsWith("VB")) {
            String word = nounPhrase.substring(nounPhrase.indexOf(" ", 
              nounPhrase.indexOf(tag)) + 1, nounPhrase.indexOf(")"));
            lastVerb = word;
          }
          
          nounPhrase = nounPhrase.substring(nounPhrase.indexOf(tag) + 1);
          tag = getNextTag(nounPhrase);
        }
        

        if ((prevPhrase.equals("NP")) && (lastNoun.length() != 0))
        {
          wordRelationsWriter.println(lastNoun + " " + lastVerb);
          
          addRelation(lastNoun, lastVerb);
        }
        
        if (!prevPhrase.equals("VP")) {
          secondPrevPhrase = prevPhrase;
          prevPhrase = "VP";
        }
      }
      else if ((isPhraseOrClause(tag)) || (isPreposition(tag))) {
        nounPhrase = nounPhrase.substring(nounPhrase.indexOf(tag) + 
          tag.length());
        if (!tag.equals(prevPhrase)) {
          secondPrevPhrase = prevPhrase;
          prevPhrase = tag;
        }
      }
      else {
        nounPhrase = nounPhrase.substring(nounPhrase.indexOf(tag) + 
          tag.length());
      }
    }
  }
  






  private void addRelation(String object, String attribute)
  {
    object = object.toLowerCase();
    attribute = attribute.toLowerCase();
    int row;
    int row;
    if (objectTable.containsKey(object))
    {
      row = ((Integer)objectTable.get(object)).intValue();
    }
    else {
      row = Integer.valueOf(objectCounter.getAndIncrement()).intValue();
      
      objectTable.put(object, Integer.valueOf(row));
      System.out.println(object + " " + row);
    }
    int col;
    int col;
    if (attributeTable.containsKey(attribute)) {
      col = ((Integer)attributeTable.get(attribute)).intValue();
    } else {
      col = Integer.valueOf(attributeCounter.getAndIncrement()).intValue();
      attributeTable.put(attribute, Integer.valueOf(col));
    }
    


    if ((row < syntacticCooccurrence.rows()) && 
      (col < syntacticCooccurrence.columns()))
    {

      double val = syntacticCooccurrence.get(row, col);
      
      syntacticCooccurrence.set(row, col, val + 1.0D);
    }
    else {
      syntacticCooccurrence.set(row, col, 1.0D);
    }
  }
  


  private void processWordsInNP(ArrayList<Pair<String>> wordsInPhrase)
  {
    if (wordsInPhrase.size() > 1)
    {
      for (int i = 0; i < wordsInPhrase.size() - 1; i++)
      {
        if (inStartSet((String)getx))
        {
          for (int j = i + 1; j < wordsInPhrase.size(); j++)
          {
            if (inReceiveSet((String)getx))
            {

              wordRelationsWriter.println((String)gety + " " + 
                (String)gety);
              



              addRelation((String)gety, 
                (String)gety);
            }
          }
        }
      }
    }
  }
  







  private boolean inStartSet(String tag)
  {
    if (!tag.startsWith("NN"))
    {
      if (!tag.startsWith("JJ"))
      {
        if (!tag.startsWith("RB"))
        {
          if (!tag.startsWith("CD"))
            return false; } } } return true;
  }
  











  private boolean inReceiveSet(String tag)
  {
    return 
      (tag.startsWith("NN")) || 
      (tag.startsWith("VB"));
  }
  



  private boolean isPreposition(String tag)
  {
    return tag.startsWith("PP");
  }
  





  private boolean isPhraseOrClause(String tag)
  {
    if (((tag.equals("SYM")) || 
      (!tag.startsWith("S"))) && 
      (!tag.equals("ADJP")) && 
      (!tag.equals("ADVP")) && 
      (!tag.equals("CONJP")) && 
      (!tag.equals("FRAG")) && 
      (!tag.equals("INTJ")) && 
      (!tag.equals("LST")) && 
      (!tag.equals("NAC")) && 
      (!tag.equals("NP")) && 
      (!tag.equals("NX")) && 
      (!tag.equals("PP")) && 
      (!tag.equals("PRN")))
    {
      if ((!tag.equals("PRT")) && 
        (!tag.equals("QP")) && 
        (!tag.equals("RRC")) && 
        (!tag.equals("UCP")) && 
        (!tag.equals("VP")) && 
        (!tag.startsWith("WH")) && 
        (!tag.equals("X")))
        return false; } return true;
  }
  


























  private String getNextTag(String str)
  {
    int tagIndex = str.indexOf("(");
    
    if (tagIndex < 0) {
      return null;
    }
    

    int endIndex = str.indexOf(" ", tagIndex);
    
    if (endIndex < 0) {
      return null;
    }
    
    String tag = str.substring(tagIndex + 1, endIndex);
    
    if (tag.length() > 0) {
      return tag;
    }
    str = str.substring(tagIndex + 1);
    return getNextTag(str);
  }
  



  public Set<String> getWords()
  {
    return Collections.unmodifiableSet(objectTable.keySet());
  }
  


  public Vector getVector(String word)
  {
    word = word.toLowerCase();
    if (objectTable.containsKey(word)) {
      int wordIndex = ((Integer)objectTable.get(word)).intValue();
      if (wordIndex < syntacticCooccurrence.rows()) {
        return syntacticCooccurrence.getRowVector(wordIndex);
      }
    }
    


    return null;
  }
  



  public void processSpace(Properties properties) {}
  



  public String getSpaceName()
  {
    return "grefenstette-syntatic-analysis";
  }
  


  public int getVectorLength()
  {
    return syntacticCooccurrence.columns();
  }
}
