package edu.ucla.sspace.matrix;

import java.io.File;

public abstract interface FileTransformer
{
  public abstract File transform(File paramFile1, File paramFile2, GlobalTransform paramGlobalTransform);
}
