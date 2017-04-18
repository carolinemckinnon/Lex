package edu.ucla.sspace.matrix;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;





























class SvdlibcDenseBinaryFileTransformer
  implements FileTransformer
{
  SvdlibcDenseBinaryFileTransformer() {}
  
  public File transform(File inputFile, File outFile, GlobalTransform transform)
  {
    try
    {
      DataInputStream dis = new DataInputStream(
        new BufferedInputStream(new FileInputStream(inputFile)));
      
      int rows = dis.readInt();
      int cols = dis.readInt();
      
      DataOutputStream dos = new DataOutputStream(
        new BufferedOutputStream(new FileOutputStream(outFile)));
      
      dos.writeInt(rows);
      dos.writeInt(cols);
      

      for (int row = 0; row < rows; row++) {
        for (int col = 0; col < cols; col++) {
          double val = dis.readFloat();
          dos.writeFloat((float)transform.transform(row, col, val));
        }
      }
      
      dos.close();
      return outFile;
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
}
