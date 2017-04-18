package edu.ucla.sspace.evaluation;







public class SimpleWordSimilarity
  implements WordSimilarity
{
  private final String first;
  





  private final String second;
  





  private final double sim;
  






  public SimpleWordSimilarity(String first, String second, double sim)
  {
    this.first = first;
    this.second = second;
    this.sim = sim;
  }
  


  public String getFirstWord()
  {
    return first;
  }
  


  public String getSecondWord()
  {
    return second;
  }
  


  public double getSimilarity()
  {
    return sim;
  }
}
