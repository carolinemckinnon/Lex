package edu.ucla.sspace.util;

import edu.ucla.sspace.matrix.Matrix;
import java.util.SortedSet;
import java.util.TreeSet;




















































public class KrippendorffsAlpha
{
  public KrippendorffsAlpha() {}
  
  public static enum LevelOfMeasurement
  {
    NOMINAL, 
    ORDINAL, 
    INTERVAL;
  }
  























  public double compute(Matrix ratings, LevelOfMeasurement level)
  {
    SortedSet<Rating> values = new TreeSet();
    for (int r = 0; r < ratings.rows(); r++) {
      for (int c = 0; c < ratings.columns(); c++) {
        double d = ratings.get(r, c);
        if (!Double.isNaN(d)) {
          values.add(new Rating(d));
        }
      }
    }
    Indexer<Rating> valueRanks = new ObjectIndexer(values);
    

    double[][] coincidence = new double[values.size()][values.size()];
    int numPairableValues = 0;
    
    for (int item = 0; item < ratings.columns(); item++)
    {

      int m_u = 0;
      for (int c = 0; c < ratings.rows(); c++) {
        if (!Double.isNaN(ratings.get(c, item))) {
          m_u++;
        }
      }
      if (m_u > 1) {
        numPairableValues += m_u;
        


        for (int coder1 = 0; coder1 < ratings.rows(); coder1++) {
          double d1 = ratings.get(coder1, item);
          if (!Double.isNaN(d1))
          {
            Rating r1 = new Rating(d1);
            
            for (int coder2 = 0; coder2 < ratings.rows(); coder2++)
              if (coder1 != coder2)
              {
                double d2 = ratings.get(coder2, item);
                if (!Double.isNaN(d2))
                {
                  Rating r2 = new Rating(d2);
                  coincidence[valueRanks.index(r1)][valueRanks.index(r2)] += 1.0D / (m_u - 1);
                }
              }
          }
        } } }
    double[] ratingToSum = new double[valueRanks.size()];
    for (int i = 0; i < coincidence.length; i++) {
      for (int j = 0; j < coincidence[i].length; j++) {
        ratingToSum[i] += coincidence[i][j];
      }
    }
    
    double expectedDisagreementSum = 0.0D;
    for (int i = 0; i < coincidence.length; i++)
    {

      for (int j = i + 1; j < coincidence[i].length; j++)
      {
        expectedDisagreementSum = expectedDisagreementSum + ratingToSum[i] * ratingToSum[j] * diff(i, j, ratingToSum, valueRanks, level);
      }
    }
    

    double disagreementSum = 0.0D;
    for (int i = 0; i < coincidence.length; i++)
    {

      for (int j = i + 1; j < coincidence[i].length; j++) {
        double co = coincidence[i][j];
        if (co > 0.0D) {
          disagreementSum += co * diff(i, j, ratingToSum, valueRanks, level);
        }
      }
    }
    
    double expectedDisagreement = expectedDisagreementSum / (numPairableValues - 1);
    
    return 1.0D - disagreementSum / expectedDisagreement;
  }
  
  static double diff(int c, int k, double[] ratingToSum, Indexer<Rating> ranks, LevelOfMeasurement lom)
  {
    switch (lom) {
    case NOMINAL: 
      double sum = 0.0D;
      for (int g = c; g <= k; g++) {
        sum += ratingToSum[g];
      }
      sum -= (ratingToSum[c] + ratingToSum[k]) / 2.0D;
      return sum * sum;
    
    case INTERVAL: 
      double rating1 = lookupval;
      double rating2 = lookupval;
      return rating1 == rating2 ? 0 : 1;
    
    case ORDINAL: 
      double rating1 = lookupval;
      double rating2 = lookupval;
      double d = rating1 - rating2;
      return d * d;
    }
    
    throw new IllegalArgumentException(
      "Unsupported level of measurement: " + lom);
  }
  
  static class Rating implements Comparable<Rating>
  {
    final double val;
    
    public Rating(double val) {
      this.val = val;
    }
    
    public int compareTo(Rating r) {
      return Double.compare(val, val);
    }
    
    public boolean equals(Object o) {
      return ((o instanceof Rating)) && (val == val);
    }
    
    public int hashCode() {
      return (int)val;
    }
    
    public String toString() {
      return String.valueOf(val);
    }
  }
}
