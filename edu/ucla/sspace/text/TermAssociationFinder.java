package edu.ucla.sspace.text;

import edu.ucla.sspace.common.statistics.SignificanceTest;
import edu.ucla.sspace.util.Counter;
import edu.ucla.sspace.util.ObjectCounter;
import edu.ucla.sspace.util.Pair;
import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;











































public class TermAssociationFinder
  implements Iterable<Map.Entry<Pair<String>, Double>>, Serializable
{
  private static final long serialVersionUID = 1L;
  private final Counter<String> termCounts;
  private final Counter<Pair<String>> bigramCounts;
  private int contexts;
  private SignificanceTest test;
  private final Set<String> leftTerms;
  private final Set<String> rightTerms;
  
  public TermAssociationFinder(SignificanceTest test)
  {
    this(test, null, null);
  }
  






  public TermAssociationFinder(SignificanceTest test, Set<String> leftTerms, Set<String> rightTerms)
  {
    this.test = test;
    this.leftTerms = leftTerms;
    this.rightTerms = rightTerms;
    termCounts = new ObjectCounter();
    bigramCounts = new ObjectCounter();
    contexts = 0;
  }
  
  public void addContext(Set<String> tokens) {
    contexts += 1;
    for (String t1 : tokens) {
      termCounts.count(t1);
      for (String t2 : tokens) {
        if (t1.equals(t2)) {
          break;
        }
        
        String s2;
        String s1;
        String s2;
        if (t1.compareTo(t2) > 0) {
          String s1 = t1;
          s2 = t2;
        }
        else {
          s1 = t2;
          s2 = t1;
        }
        


        if ((leftTerms == null) || 
          ((rightTerms.contains(s1)) && (leftTerms.contains(s2))) || (
          (rightTerms.contains(s2)) && (leftTerms.contains(s1)))) {
          bigramCounts.count(new Pair(s1, s2));
        }
      }
    }
  }
  
  public double getAssociationScore(String t1, String t2) {
    int t1Count = termCounts.getCount(t1);
    int t2Count = termCounts.getCount(t2);
    if ((t1Count == 0) || (t2Count == 0))
      return 0.0D;
    int bothAppeared = bigramCounts.getCount(new Pair(t1, t2));
    int t1butNotT2 = t1Count - bothAppeared;
    int t2butNotT1 = t2Count - bothAppeared;
    int neitherAppeared = contexts - (t1Count + t2Count - bothAppeared);
    return test.score(bothAppeared, t1butNotT2, 
      t2butNotT1, neitherAppeared);
  }
  



  public Iterator<Map.Entry<Pair<String>, Double>> iterator()
  {
    return new ScoreIterator();
  }
  
  public SignificanceTest getTest() {
    return test;
  }
  
  public void setTest(SignificanceTest test) {
    if (test == null)
      throw new NullPointerException("test cannot be null");
    this.test = test;
  }
  
  private class ScoreIterator implements Iterator<Map.Entry<Pair<String>, Double>>
  {
    private final Iterator<Map.Entry<Pair<String>, Integer>> bigramIter;
    
    public ScoreIterator()
    {
      bigramIter = bigramCounts.iterator();
    }
    
    public boolean hasNext() {
      return bigramIter.hasNext();
    }
    
    public Map.Entry<Pair<String>, Double> next() {
      Map.Entry<Pair<String>, Integer> e = (Map.Entry)bigramIter.next();
      Pair<String> p = (Pair)e.getKey();
      String t1 = (String)x;
      String t2 = (String)y;
      
      int t1Count = termCounts.getCount(t1);
      int t2Count = termCounts.getCount(t2);
      int bothAppeared = ((Integer)e.getValue()).intValue();
      
      int t1butNotT2 = t1Count - bothAppeared;
      int t2butNotT1 = t2Count - bothAppeared;
      int neitherAppeared = 
        contexts - (t1Count + t2Count - bothAppeared);
      double score = test.score(bothAppeared, t1butNotT2, 
        t2butNotT1, neitherAppeared);
      return new AbstractMap.SimpleEntry(p, Double.valueOf(score));
    }
    
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
}
