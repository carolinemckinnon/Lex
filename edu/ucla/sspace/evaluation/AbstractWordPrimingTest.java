package edu.ucla.sspace.evaluation;

import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.util.Pair;
import java.util.HashSet;
import java.util.Set;







































public abstract class AbstractWordPrimingTest
  implements WordPrimingTest
{
  protected final Set<Pair<String>> primeTargetPairs;
  
  public AbstractWordPrimingTest(Set<Pair<String>> primeTargetPairs)
  {
    this.primeTargetPairs = primeTargetPairs;
  }
  










  public WordPrimingReport evaluate(SemanticSpace sspace)
  {
    Pair<double[]> scores = evaluateRelation(sspace, primeTargetPairs);
    

    int numItems = ((double[])x).length;
    double relatedSum = 0.0D;
    double unrelatedSum = 0.0D;
    for (int i = 0; i < numItems; i++) {
      relatedSum += ((double[])x)[i];
      unrelatedSum += ((double[])y)[i];
    }
    

    relatedSum /= numItems;
    unrelatedSum /= numItems;
    

    return new SimpleWordPrimingReport(numItems, relatedSum, unrelatedSum);
  }
  





  private Pair<double[]> evaluateRelation(SemanticSpace sspace, Set<Pair<String>> pairs)
  {
    Set<String> sspaceWords = sspace.getWords();
    



    Set<String> primes = new HashSet();
    for (Pair<String> primeTargetPair : pairs) {
      if ((sspaceWords.contains(x)) && 
        (sspaceWords.contains(y)))
        primes.add((String)x);
    }
    int numValidPairs = primes.size();
    

    double[] relatedScores = new double[numValidPairs];
    double[] unrelatedScores = new double[numValidPairs];
    





    int scoreIndex = 0;
    for (Pair<String> pair : pairs)
    {
      if ((sspaceWords.contains(x)) && 
        (sspaceWords.contains(y)))
      {




        double related = computePriming(sspace, (String)x, (String)y).doubleValue();
        



        double unrelated = related;
        for (String prime : primes) {
          if (!prime.equals(x))
          {
            unrelated += computePriming(sspace, prime, (String)y).doubleValue(); }
        }
        unrelated /= primes.size();
        

        relatedScores[scoreIndex] = related;
        unrelatedScores[scoreIndex] = unrelated;
        scoreIndex++;
      } }
    return new Pair(relatedScores, unrelatedScores);
  }
  






  protected abstract Double computePriming(SemanticSpace paramSemanticSpace, String paramString1, String paramString2);
  





  public class SimpleWordPrimingReport
    implements WordPrimingReport
  {
    private int numDataPoints;
    




    private double relatedScore;
    




    private double unrelatedScore;
    





    public SimpleWordPrimingReport(int numDataPoints, double relatedScore, double unrelatedScore)
    {
      this.numDataPoints = numDataPoints;
      this.relatedScore = relatedScore;
      this.unrelatedScore = unrelatedScore;
    }
    


    public int numberOfWordPairs()
    {
      return numDataPoints;
    }
    


    public double relatedPriming()
    {
      return relatedScore;
    }
    


    public double unrelatedPriming()
    {
      return unrelatedScore;
    }
    


    public double effect()
    {
      return relatedScore - unrelatedScore;
    }
    
    public String toString() {
      return String.format("Primed Pairs: %f\nUnrelated Pairs: %f\n", new Object[] {
        Double.valueOf(relatedScore), Double.valueOf(unrelatedScore) });
    }
  }
}
