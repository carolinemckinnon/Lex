package edu.ucla.sspace.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
















































public class DirectoryWalker
  implements Iterable<File>
{
  private final File baseDir;
  private final FileFilter filter;
  
  public DirectoryWalker(File baseDir)
  {
    this(baseDir, "");
  }
  





  public DirectoryWalker(File baseDir, String suffix)
  {
    this(baseDir, new FileFilter() {
      public boolean accept(File f) {
        return f.getName().endsWith(DirectoryWalker.this);
      }
    });
  }
  









  public DirectoryWalker(File baseDir, FileFilter filter)
  {
    this.baseDir = baseDir;
    this.filter = filter;
    if (!baseDir.isDirectory()) {
      throw new IllegalArgumentException(baseDir + " is not a directory");
    }
    if (filter == null) {
      throw new NullPointerException();
    }
  }
  


  public Iterator<File> iterator()
  {
    return new FileIterator();
  }
  

  private class FileIterator
    implements Iterator<File>
  {
    private final Queue<File> files;
    
    private File next;
    
    public FileIterator()
    {
      files = new ArrayDeque();
      for (File f : baseDir.listFiles(filter)) {
        files.offer(f);
      }
      advance();
    }
    
    private void advance() {
      next = null;
      while ((next == null) && (!files.isEmpty())) {
        File f = (File)files.poll();
        

        if (f.isDirectory()) {
          files.addAll(Arrays.asList(f.listFiles(filter)));
        } else if (filter.accept(f))
          next = f;
      }
    }
    
    public boolean hasNext() {
      return next != null;
    }
    
    public File next() {
      if (next == null)
        throw new NoSuchElementException();
      File f = next;
      advance();
      return f;
    }
    
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
}
