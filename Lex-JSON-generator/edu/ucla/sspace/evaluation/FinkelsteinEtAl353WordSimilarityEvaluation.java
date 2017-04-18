package edu.ucla.sspace.evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;





















































public class FinkelsteinEtAl353WordSimilarityEvaluation
  implements WordSimilarityEvaluation
{
  private final Collection<WordSimilarity> pairs;
  private final String dataFileName;
  
  public FinkelsteinEtAl353WordSimilarityEvaluation(String word353fileName)
  {
    this(new File(word353fileName));
  }
  



  public FinkelsteinEtAl353WordSimilarityEvaluation(File word353file)
  {
    pairs = parse(word353file);
    dataFileName = word353file.getName();
  }
  





  private Collection<WordSimilarity> parse(File word353file)
  {
    String delimeter = word353file.getName().endsWith(".csv") ? 
      "," : "\\s";
    
    Collection<WordSimilarity> pairs = new LinkedList();
    try
    {
      BufferedReader br = new BufferedReader(new FileReader(word353file));
      
      br.readLine();
      for (String line = null; (line = br.readLine()) != null;)
      {
        String[] wordsAndNum = line.split(delimeter);
        if (wordsAndNum.length != 3) {
          throw new Error("Unexpected line formatting: " + line);
        }
        pairs.add(new SimpleWordSimilarity(
          wordsAndNum[0], wordsAndNum[1], 
          Double.parseDouble(wordsAndNum[2])));
      }
    }
    catch (IOException ioe) {
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
    return "Finkelstein et al. Word Similarity Test [" + dataFileName + "]";
  }
}
