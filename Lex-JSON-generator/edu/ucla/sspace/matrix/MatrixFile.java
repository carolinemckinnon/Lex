package edu.ucla.sspace.matrix;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;







































public class MatrixFile
  implements Iterable<MatrixEntry>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final File matrixFile;
  private final MatrixIO.Format format;
  
  public MatrixFile(File matrixFile, MatrixIO.Format format)
  {
    if (matrixFile == null)
      throw new NullPointerException("matrix file cannot be null");
    this.matrixFile = matrixFile;
    this.format = format;
  }
  
  public boolean equals(Object o) {
    if ((o instanceof MatrixFile)) {
      MatrixFile m = (MatrixFile)o;
      return (matrixFile.equals(matrixFile)) && 
        (format.equals(format));
    }
    return false;
  }
  


  public File getFile()
  {
    return matrixFile;
  }
  


  public MatrixIO.Format getFormat()
  {
    return format;
  }
  
  public int hashCode() {
    return matrixFile.hashCode() ^ format.hashCode();
  }
  



  public Iterator<MatrixEntry> iterator()
  {
    try
    {
      return MatrixIO.getMatrixFileIterator(matrixFile, format);
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  


  public Matrix load()
  {
    try
    {
      return MatrixIO.readMatrix(matrixFile, format);
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  
  public String toString() {
    return "Matrix[" + matrixFile + ":" + format + "]";
  }
}
