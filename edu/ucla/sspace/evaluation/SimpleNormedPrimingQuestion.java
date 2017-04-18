package edu.ucla.sspace.evaluation;












public class SimpleNormedPrimingQuestion
  implements NormedPrimingQuestion
{
  private String cue;
  










  private String[] targets;
  









  private double[] strengths;
  










  public SimpleNormedPrimingQuestion(String cue, String[] targets, double[] strengths)
  {
    if (targets.length != strengths.length)
      throw new IllegalArgumentException(
        "The target and strength length must be equal");
    this.cue = cue;
    this.targets = targets;
    this.strengths = strengths;
  }
  

  public String getCue()
  {
    return cue;
  }
  


  public int numberOfTargets()
  {
    return targets.length;
  }
  


  public String getTarget(int i)
  {
    return targets[i];
  }
  


  public double getStrength(int i)
  {
    return strengths[i];
  }
}
