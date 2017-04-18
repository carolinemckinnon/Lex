package org.tartarus.snowball;

public abstract class SnowballStemmer
  extends SnowballProgram
{
  public SnowballStemmer() {}
  
  public abstract boolean stem();
}
