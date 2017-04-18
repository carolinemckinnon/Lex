package edu.ucla.sspace.tools;

import edu.ucla.sspace.basis.BasisMapping;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.MatrixIO;
import edu.ucla.sspace.matrix.MatrixIO.Format;
import edu.ucla.sspace.util.BoundedSortedMultiMap;
import edu.ucla.sspace.util.MultiMap;
import edu.ucla.sspace.util.SerializableUtil;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;




public class SelectTopKWords
{
  public SelectTopKWords() {}
  
  public static void main(String[] args)
    throws Exception
  {
    BasisMapping<String, String> basis = 
      (BasisMapping)SerializableUtil.load(new File(args[0]));
    

    List<MultiMap<Double, String>> topTerms = new ArrayList();
    Matrix m = MatrixIO.readMatrix(new File(args[1]), MatrixIO.Format.DENSE_TEXT);
    for (int c = 0; c < m.columns(); c++)
      topTerms.add(new BoundedSortedMultiMap(10));
    String term;
    for (int r = 0; r < m.rows(); r++) {
      term = (String)basis.getDimensionDescription(r);
      for (int c = 0; c < m.columns(); c++) {
        ((MultiMap)topTerms.get(c)).put(Double.valueOf(m.get(r, c)), term);
      }
    }
    for (MultiMap<Double, String> topicTerms : topTerms) {
      for (String term : topicTerms.values())
        System.out.printf("%s ", new Object[] { term });
      System.out.println();
    }
  }
}
