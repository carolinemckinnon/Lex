package edu.ucla.sspace.text;

import edu.ucla.sspace.dependency.DependencyTreeNode;

public abstract interface ParsedDocument
  extends Document
{
  public abstract DependencyTreeNode[] parsedDocument();
  
  public abstract String text();
  
  public abstract String prettyPrintText();
}
