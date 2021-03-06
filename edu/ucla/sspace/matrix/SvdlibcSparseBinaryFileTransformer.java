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


























class SvdlibcSparseBinaryFileTransformer
  implements FileTransformer
{
  SvdlibcSparseBinaryFileTransformer() {}
  
  public File transform(File inputFile, File outFile, GlobalTransform transform)
  {
    try
    {
      DataInputStream dis = new DataInputStream(
        new BufferedInputStream(new FileInputStream(inputFile)));
      
      int rows = dis.readInt();
      int cols = dis.readInt();
      int nzEntriesInMatrix = dis.readInt();
      
      DataOutputStream dos = new DataOutputStream(
        new BufferedOutputStream(new FileOutputStream(outFile)));
      
      dos.writeInt(rows);
      dos.writeInt(cols);
      dos.writeInt(nzEntriesInMatrix);
      

      for (int col = 0; col < cols; col++) {
        int nzInCurCol = dis.readInt();
        dos.writeInt(nzInCurCol);
        
        for (int index = 0; index < nzInCurCol; index++) {
          int row = dis.readInt();
          double value = dis.readFloat();
          dos.writeInt(row);
          dos.writeFloat(
            (float)transform.transform(row, col, value));
        }
      }
      dos.close();
      return outFile;
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
}
