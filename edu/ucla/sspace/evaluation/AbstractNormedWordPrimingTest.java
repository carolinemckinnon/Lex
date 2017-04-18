package edu.ucla.sspace.evaluation;

import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.Similarity;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;




































public abstract class AbstractNormedWordPrimingTest
  implements NormedWordPrimingTest
{
  protected final Set<NormedPrimingQuestion> normedWordQuestions;
  
  public AbstractNormedWordPrimingTest(Set<NormedPrimingQuestion> primes)
  {
    normedWordQuestions = primes;
  }
  










  public NormedWordPrimingReport evaluate(SemanticSpace sspace)
  {
    Set<String> sspaceWords = sspace.getWords();
    

    int totalQuestions = normedWordQuestions.size();
    int unanswered = 0;
    double totalCorrelation = 0.0D;
    


    for (NormedPrimingQuestion question : normedWordQuestions) {
      if (sspaceWords.contains(question.getCue())) {
        Double correl = answerQuestion(sspace, sspaceWords, question);
        if (correl == null) {
          unanswered++;
        }
        else {
          totalCorrelation += correl.doubleValue();
        }
      } else {
        unanswered++;
      }
    }
    double averageCorrelation = 
      totalCorrelation / (totalQuestions - unanswered);
    return new SimpleNormedWordPrimingReport(
      totalQuestions, unanswered, averageCorrelation);
  }
  







  private Double answerQuestion(SemanticSpace sspace, Set<String> sspaceWords, NormedPrimingQuestion question)
  {
    List<Double> knownStrengthsList = new ArrayList();
    List<Double> computedStrengthsList = new ArrayList();
    


    String cue = question.getCue();
    for (int i = 0; i < question.numberOfTargets(); i++) {
      String target = question.getTarget(i);
      double strength = question.getStrength(i);
      if (sspaceWords.contains(target))
      {

        knownStrengthsList.add(Double.valueOf(strength));
        computedStrengthsList.add(Double.valueOf(computeStrength(sspace, cue, target)));
      }
    }
    if (knownStrengthsList.size() < 2) {
      return null;
    }
    
    double[] knownStrengths = new double[knownStrengthsList.size()];
    double[] computedStrengths = new double[knownStrengthsList.size()];
    for (int i = 0; i < knownStrengths.length; i++) {
      knownStrengths[i] = ((Double)knownStrengthsList.get(i)).doubleValue();
      computedStrengths[i] = ((Double)computedStrengthsList.get(i)).doubleValue();
    }
    

    return Double.valueOf(Similarity.spearmanRankCorrelationCoefficient(
      knownStrengths, computedStrengths));
  }
  






  protected abstract double computeStrength(SemanticSpace paramSemanticSpace, String paramString1, String paramString2);
  






  public class SimpleNormedWordPrimingReport
    implements NormedWordPrimingReport
  {
    private int numberOfCues;
    





    private int numberOfUnanswerableCues;
    





    private double averageCorrelation;
    





    public SimpleNormedWordPrimingReport(int numberOfCues, int numberOfUnanswerableCues, double averageCorrelation)
    {
      this.numberOfCues = numberOfCues;
      this.numberOfUnanswerableCues = numberOfUnanswerableCues;
      this.averageCorrelation = averageCorrelation;
    }
    


    public double averageCorrelation()
    {
      return averageCorrelation;
    }
    


    public int numberOfCues()
    {
      return numberOfCues;
    }
    


    public int numberOfUnanswerableCues()
    {
      return numberOfUnanswerableCues;
    }
    
    public String toString() {
      return String.format("Primed cues: %s\nUnanswered cues: %s\nAverage Correlation: %f\n", new Object[] {
      

        Integer.valueOf(numberOfCues), Integer.valueOf(numberOfUnanswerableCues), 
        Double.valueOf(averageCorrelation) });
    }
  }
}
