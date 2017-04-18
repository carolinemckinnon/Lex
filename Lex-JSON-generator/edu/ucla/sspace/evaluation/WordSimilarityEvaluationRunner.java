package edu.ucla.sspace.evaluation;

import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.common.Similarity.SimType;
import edu.ucla.sspace.vector.Vector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;















































public class WordSimilarityEvaluationRunner
{
  public WordSimilarityEvaluationRunner() {}
  
  public static WordSimilarityReport evaluate(SemanticSpace sspace, WordSimilarityEvaluation test, Similarity.SimType vectorComparisonType)
  {
    Collection<WordSimilarity> wordPairs = test.getPairs();
    int unanswerable = 0;
    




    List<Double> humanJudgements = new ArrayList(wordPairs.size());
    List<Double> sspaceJudgements = new ArrayList(wordPairs.size());
    
    double testRange = test.getMostSimilarValue() - 
      test.getLeastSimilarValue();
    


    for (WordSimilarity pair : wordPairs)
    {
      Vector firstVector = sspace.getVector(pair.getFirstWord());
      Vector secondVector = sspace.getVector(pair.getSecondWord());
      

      if ((firstVector == null) || (secondVector == null)) {
        unanswerable++;

      }
      else
      {

        double similarity = 
          Similarity.getSimilarity(vectorComparisonType, 
          firstVector, secondVector);
        double scaled = similarity * testRange + 
          test.getLeastSimilarValue();
        
        humanJudgements.add(Double.valueOf(pair.getSimilarity()));
        sspaceJudgements.add(Double.valueOf(scaled));
      }
    }
    

    double[] humanArr = new double[humanJudgements.size()];
    double[] sspaceArr = new double[humanJudgements.size()];
    
    for (int i = 0; i < humanArr.length; i++) {
      humanArr[i] = ((Double)humanJudgements.get(i)).doubleValue();
      sspaceArr[i] = ((Double)sspaceJudgements.get(i)).doubleValue();
    }
    
    double correlation = Similarity.correlation(humanArr, sspaceArr);
    
    return new SimpleReport(wordPairs.size(), correlation, unanswerable);
  }
  





  private static class SimpleReport
    implements WordSimilarityReport
  {
    private final int numWordPairs;
    




    private final double correlation;
    




    private final int unanswerable;
    




    public SimpleReport(int numWordPairs, double correlation, int unanswerable)
    {
      this.numWordPairs = numWordPairs;
      this.correlation = correlation;
      this.unanswerable = unanswerable;
    }
    


    public int numberOfWordPairs()
    {
      return numWordPairs;
    }
    


    public double correlation()
    {
      return correlation;
    }
    


    public int unanswerableQuestions()
    {
      return unanswerable;
    }
    



    public String toString()
    {
      return String.format("%.4f correlation; %d/%d unanswered", new Object[] {
        Double.valueOf(correlation), Integer.valueOf(unanswerable), Integer.valueOf(numWordPairs) });
    }
  }
}
