package edu.ucla.sspace.matrix;

import edu.ucla.sspace.vector.DoubleVector;
import java.io.File;
import java.io.IOException;

public abstract interface Transform
{
  public abstract DoubleVector transform(DoubleVector paramDoubleVector);
  
  public abstract File transform(File paramFile, MatrixIO.Format paramFormat)
    throws IOException;
  
  public abstract void transform(File paramFile1, MatrixIO.Format paramFormat, File paramFile2)
    throws IOException;
  
  public abstract Matrix transform(Matrix paramMatrix);
  
  public abstract Matrix transform(Matrix paramMatrix1, Matrix paramMatrix2);
}
