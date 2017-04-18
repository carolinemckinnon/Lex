package edu.ucla.sspace.wordsi;

public abstract interface AssignmentReporter
{
  public abstract void updateAssignment(String paramString1, String paramString2, int paramInt);
  
  public abstract void finalizeReport();
  
  public abstract void assignContextToKey(String paramString1, String paramString2, int paramInt);
  
  public abstract String[] contextLabels(String paramString);
}
