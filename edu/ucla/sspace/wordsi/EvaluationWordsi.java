package edu.ucla.sspace.wordsi;

import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.vector.SparseDoubleVector;
import edu.ucla.sspace.vector.Vector;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;






























































public class EvaluationWordsi
  extends BaseWordsi
{
  private final SemanticSpace wordSpace;
  private final AssignmentReporter reporter;
  
  public EvaluationWordsi(Set<String> acceptedWords, ContextExtractor extractor, SemanticSpace sspace, AssignmentReporter reporter)
  {
    super(acceptedWords, extractor);
    wordSpace = sspace;
    this.reporter = reporter;
  }
  


  public Set<String> getWords()
  {
    return new HashSet();
  }
  


  public Vector getVector(String term)
  {
    return wordSpace.getVector(term);
  }
  





  public void handleContextVector(String focusKey, String secondaryKey, SparseDoubleVector context)
  {
    int senseNumber = 0;
    int bestSense = 0;
    double bestSimilarity = -1.0D;
    
    for (;;)
    {
      Vector wordSense = getVector(
      
        focusKey + "-" + senseNumber);
      

      if (wordSense == null) {
        break;
      }
      

      double similarity = Similarity.cosineSimilarity(wordSense, context);
      if (similarity > bestSimilarity) {
        bestSimilarity = similarity;
        bestSense = senseNumber;
      }
      
      senseNumber++;
    }
    

    if (reporter != null) {
      reporter.updateAssignment(focusKey, secondaryKey, bestSense);
    }
  }
  


  public void processSpace(Properties props)
  {
    if (reporter != null) {
      reporter.finalizeReport();
    }
  }
}
