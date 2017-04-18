package edu.ucla.sspace.dependency;

import java.io.BufferedReader;
import java.io.IOException;

public abstract interface DependencyExtractor
{
  public abstract DependencyTreeNode[] readNextTree(BufferedReader paramBufferedReader)
    throws IOException;
}
