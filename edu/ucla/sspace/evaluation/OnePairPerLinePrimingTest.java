package edu.ucla.sspace.evaluation;

import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.util.Pair;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;











































public class OnePairPerLinePrimingTest
  extends AbstractWordPrimingTest
{
  private final String dataFileName;
  
  public OnePairPerLinePrimingTest(String testPairFileName)
  {
    this(new File(testPairFileName));
  }
  


  public OnePairPerLinePrimingTest(File testPairFile)
  {
    super(prepareRelationMap(testPairFile));
    dataFileName = testPairFile.getName();
  }
  



  public static Set<Pair<String>> prepareRelationMap(File testPairFile)
  {
    Set<Pair<String>> wordPairSet = new HashSet();
    try {
      BufferedReader br = 
        new BufferedReader(new FileReader(testPairFile));
      


      for (String line = null; (line = br.readLine()) != null;)
        if ((line.length() != 0) && (!line.startsWith("#")))
        {
          String[] pair = line.split("\\s+");
          wordPairSet.add(new Pair(pair[0], pair[1]));
        }
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
    return wordPairSet;
  }
  



  protected Double computePriming(SemanticSpace sspace, String word1, String word2)
  {
    return Double.valueOf(Similarity.cosineSimilarity(
      sspace.getVector(word1), sspace.getVector(word2)));
  }
  
  public String toString() {
    return "Priming Relation: " + dataFileName;
  }
}
