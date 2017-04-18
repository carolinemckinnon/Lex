package edu.ucla.sspace.matrix;

import java.io.IOException;
































public class MatrixIOException
  extends IOException
{
  private static final long serialVersionUID = 1L;
  
  public MatrixIOException() {}
  
  public MatrixIOException(String message)
  {
    super(message);
  }
  




  public MatrixIOException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
