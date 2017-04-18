package edu.ucla.sspace.evaluation;

import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.Similarity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;































public class OneNormedQuestionPerLineTest
  extends AbstractNormedWordPrimingTest
{
  public OneNormedQuestionPerLineTest(String primingFile)
  {
    this(new File(primingFile));
  }
  


  public OneNormedQuestionPerLineTest(File primingFile)
  {
    super(prepareQuestionSet(primingFile));
  }
  




  private static Set<NormedPrimingQuestion> prepareQuestionSet(File primingFile)
  {
    Set<NormedPrimingQuestion> questions = 
      new HashSet();
    try {
      BufferedReader br = new BufferedReader(new FileReader(primingFile));
      



      for (String line = null; (line = br.readLine()) != null;) {
        String[] cueAndTargets = line.split("\\|");
        String[] targets = new String[cueAndTargets.length - 1];
        double[] strengths = new double[cueAndTargets.length - 1];
        


        for (int i = 1; i < cueAndTargets.length; i++) {
          String[] targetAndStrength = cueAndTargets[i].split(",");
          targets[(i - 1)] = targetAndStrength[0].trim();
          strengths[(i - 1)] = Double.parseDouble(targetAndStrength[1]);
        }
        

        questions.add(new SimpleNormedPrimingQuestion(
          cueAndTargets[0].trim(), targets, strengths));
      }
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
    return questions;
  }
  




  public double computeStrength(SemanticSpace sspace, String word1, String word2)
  {
    return Similarity.cosineSimilarity(sspace.getVector(word1), 
      sspace.getVector(word2));
  }
}
