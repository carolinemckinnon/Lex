package edu.ucla.sspace.text;

public abstract interface AnnotatedDocument
  extends Document
{
  public abstract long creationDate();
  
  public abstract String label();
}
