package edu.ucla.sspace.evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
















































public class RubensteinGoodenoughWordSimilarityEvaluation
  implements WordSimilarityEvaluation
{
  private final Collection<WordSimilarity> pairs;
  private final String dataFileName;
  
  public RubensteinGoodenoughWordSimilarityEvaluation(String rbSimFileName)
  {
    this(new File(rbSimFileName));
  }
  



  public RubensteinGoodenoughWordSimilarityEvaluation(File rbSimFile)
  {
    pairs = parse(rbSimFile);
    dataFileName = rbSimFile.getName();
  }
  



  private Collection<WordSimilarity> parse(File word353file)
  {
    Collection<WordSimilarity> pairs = new LinkedList();
    try
    {
      BufferedReader br = new BufferedReader(new FileReader(word353file));
      
      br.readLine();
      for (String line = null; (line = br.readLine()) != null;)
      {

        if ((!line.startsWith("#")) && (line.length() != 0))
        {


          String[] wordsAndNum = line.split("\\s+");
          if (wordsAndNum.length != 3) {
            throw new Error("Unexpected line formatting: " + line);
          }
          pairs.add(new SimpleWordSimilarity(
            wordsAndNum[0], wordsAndNum[1], 
            Double.parseDouble(wordsAndNum[2])));
        }
      }
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
    
    return pairs;
  }
  


  public Collection<WordSimilarity> getPairs()
  {
    return pairs;
  }
  


  public double getMostSimilarValue()
  {
    return 10.0D;
  }
  


  public double getLeastSimilarValue()
  {
    return 0.0D;
  }
  
  public String toString() {
    return 
      "Rubenstein & Goodenough Word Similarity Test [" + dataFileName + "]";
  }
}
