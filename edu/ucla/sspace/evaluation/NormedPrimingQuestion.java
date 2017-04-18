package edu.ucla.sspace.evaluation;

public abstract interface NormedPrimingQuestion
{
  public abstract String getCue();
  
  public abstract int numberOfTargets();
  
  public abstract String getTarget(int paramInt);
  
  public abstract double getStrength(int paramInt);
}
