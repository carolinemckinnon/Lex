package edu.ucla.sspace.evaluation;












public class SimpleWordAssociationReport
  implements WordAssociationReport
{
  private final int numWordPairs;
  










  private final double correlation;
  









  private final int unanswerable;
  










  public SimpleWordAssociationReport(int numWordPairs, double correlation, int unanswerable)
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
