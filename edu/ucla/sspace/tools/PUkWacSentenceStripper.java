package edu.ucla.sspace.tools;

import edu.ucla.sspace.dependency.CoNLLDependencyExtractor;
import edu.ucla.sspace.dependency.DependencyExtractor;
import edu.ucla.sspace.dependency.DependencyTreeNode;
import edu.ucla.sspace.text.Document;
import edu.ucla.sspace.text.UkWacDependencyFileIterator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;





























public class PUkWacSentenceStripper
{
  public PUkWacSentenceStripper() {}
  
  public static void main(String[] args)
    throws IOException
  {
    Iterator<Document> ukWacIter = new UkWacDependencyFileIterator(args[0]);
    
    PrintWriter writer = null;
    try {
      writer = new PrintWriter(args[1]);
      StringBuilder builder = new StringBuilder();
      DependencyExtractor extractor = new CoNLLDependencyExtractor();
      while (ukWacIter.hasNext()) {
        BufferedReader doc = ((Document)ukWacIter.next()).reader();
        DependencyTreeNode[] tree = null;
        int j; int i; for (; (tree = extractor.readNextTree(doc)) != null; 
            i < j) { DependencyTreeNode[] arrayOfDependencyTreeNode1; j = (arrayOfDependencyTreeNode1 = tree).length;i = 0; continue;DependencyTreeNode node = arrayOfDependencyTreeNode1[i];
          builder.append(node.word()).append(" ");i++;
        }
        
        writer.println(builder.toString());
        builder = new StringBuilder();
      }
    } finally {
      if (writer != null) writer.close();
    }
  }
}
