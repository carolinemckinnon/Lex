package edu.ucla.sspace.evaluation;

import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.util.Pair;
import edu.ucla.sspace.vector.Vector;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

































public class DeeseAntonymEvaluation
  extends AbstractWordAssociationTest
{
  private static final Set<Pair<String>> DEESE_ANTONYMS = new LinkedHashSet();
  
  static {
    DEESE_ANTONYMS.add(new Pair("active", "passive"));
    DEESE_ANTONYMS.add(new Pair("bad", "good"));
    DEESE_ANTONYMS.add(new Pair("high", "low"));
    DEESE_ANTONYMS.add(new Pair("right", "wrong"));
    DEESE_ANTONYMS.add(new Pair("big", "little"));
    DEESE_ANTONYMS.add(new Pair("empty", "full"));
    DEESE_ANTONYMS.add(new Pair("narrow", "wide"));
    DEESE_ANTONYMS.add(new Pair("strong", "weak"));
    DEESE_ANTONYMS.add(new Pair("cold", "hot"));
    DEESE_ANTONYMS.add(new Pair("heavy", "light"));
    DEESE_ANTONYMS.add(new Pair("pretty", "ugly"));
    DEESE_ANTONYMS.add(new Pair("alone", "together"));
    DEESE_ANTONYMS.add(new Pair("few", "many"));
    DEESE_ANTONYMS.add(new Pair("dark", "light"));
    DEESE_ANTONYMS.add(new Pair("bottom", "top"));
    DEESE_ANTONYMS.add(new Pair("long", "short"));
    DEESE_ANTONYMS.add(new Pair("sour", "sweet"));
    DEESE_ANTONYMS.add(new Pair("clean", "dirty"));
    DEESE_ANTONYMS.add(new Pair("hard", "soft"));
    DEESE_ANTONYMS.add(new Pair("rich", "poor"));
    DEESE_ANTONYMS.add(new Pair("back", "front"));
    DEESE_ANTONYMS.add(new Pair("dry", "wet"));
    DEESE_ANTONYMS.add(new Pair("left", "right"));
    DEESE_ANTONYMS.add(new Pair("short", "tall"));
    DEESE_ANTONYMS.add(new Pair("far", "near"));
    DEESE_ANTONYMS.add(new Pair("easy", "hard"));
    DEESE_ANTONYMS.add(new Pair("happy", "sad"));
    DEESE_ANTONYMS.add(new Pair("old", "young"));
    DEESE_ANTONYMS.add(new Pair("alive", "dead"));
    DEESE_ANTONYMS.add(new Pair("deep", "shallow"));
    DEESE_ANTONYMS.add(new Pair("large", "small"));
    DEESE_ANTONYMS.add(new Pair("rough", "smooth"));
    DEESE_ANTONYMS.add(new Pair("black", "white"));
    DEESE_ANTONYMS.add(new Pair("fast", "slow"));
    DEESE_ANTONYMS.add(new Pair("new", "old"));
    DEESE_ANTONYMS.add(new Pair("thin", "thick"));
    DEESE_ANTONYMS.add(new Pair("first", "last"));
    DEESE_ANTONYMS.add(new Pair("single", "married"));
    DEESE_ANTONYMS.add(new Pair("inside", "outside"));
  }
  
  public DeeseAntonymEvaluation() {
    super(createMap());
  }
  
  private static Map<Pair<String>, Double> createMap() {
    Map<Pair<String>, Double> antonymToSimilarity = 
      new HashMap();
    for (Pair<String> p : DEESE_ANTONYMS)
      antonymToSimilarity.put(p, Double.valueOf(1.0D));
    return antonymToSimilarity;
  }
  


  protected double getLowestScore()
  {
    return 0.0D;
  }
  


  protected double getHighestScore()
  {
    return 1.0D;
  }
  






  protected Double computeAssociation(SemanticSpace sspace, String word1, String word2)
  {
    Vector v1 = sspace.getVector(word1);
    Vector v2 = sspace.getVector(word2);
    if ((v1 == null) || (v2 == null)) {
      return null;
    }
    
    double rank1 = findRank(sspace, word1, word2);
    double rank2 = findRank(sspace, word2, word1);
    return Double.valueOf(2.0D / (rank1 + rank2));
  }
  


  protected double computeScore(double[] humanScores, double[] compScores)
  {
    double average = 0.0D;
    for (double score : compScores)
      average += score;
    return average / compScores.length;
  }
  




  private int findRank(SemanticSpace sspace, String target, String other)
  {
    Vector v1 = sspace.getVector(target);
    Vector v2 = sspace.getVector(other);
    
    double baseSim = Similarity.cosineSimilarity(v1, v2);
    int rank = 0;
    

    for (String word : sspace.getWords()) {
      Vector v = sspace.getVector(word);
      double sim = Similarity.cosineSimilarity(v1, v);
      if (sim > baseSim)
        rank++;
    }
    return rank;
  }
}
