package edu.ucla.sspace.common;

import edu.ucla.sspace.util.Counter;
import edu.ucla.sspace.util.ObjectCounter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.List;









































public class Statistics
{
  private Statistics() {}
  
  public static double entropy(int[] a)
  {
    double sum = sum(a);
    double entropy = 0.0D;
    int[] arrayOfInt = a;int j = a.length; for (int i = 0; i < j; i++) { int x = arrayOfInt[i];
      double p = x / sum;
      if (p != 0.0D)
        entropy += Math.log(p) * p;
    }
    return -entropy;
  }
  




  public static double entropy(double[] a)
  {
    double sum = sum(a);
    double entropy = 0.0D;
    double[] arrayOfDouble = a;int j = a.length; for (int i = 0; i < j; i++) { double x = arrayOfDouble[i];
      double p = x / sum;
      if (p != 0.0D)
        entropy += Math.log(p) * p;
    }
    return -entropy;
  }
  


  public static double log2(double d)
  {
    return Math.log(d) / Math.log(2.0D);
  }
  




  public static double log2_1p(double d)
  {
    return Math.log1p(d) / Math.log(2.0D);
  }
  


  public static double mean(Collection<? extends Number> values)
  {
    double sum = 0.0D;
    for (Number n : values)
      sum += n.doubleValue();
    return sum / values.size();
  }
  


  public static double mean(int[] values)
  {
    double sum = 0.0D;
    int[] arrayOfInt = values;int j = values.length; for (int i = 0; i < j; i++) { int i = arrayOfInt[i];
      sum += i; }
    return sum / values.length;
  }
  


  public static double mean(double[] values)
  {
    double sum = 0.0D;
    double[] arrayOfDouble = values;int j = values.length; for (int i = 0; i < j; i++) { double d = arrayOfDouble[i];
      sum += d; }
    return sum / values.length;
  }
  



  public static <T extends Number,  extends Comparable> T median(Collection<T> values)
  {
    if (values.isEmpty())
      throw new IllegalArgumentException(
        "No median in an empty collection");
    List<T> sorted = new ArrayList(values);
    
    Collections.sort(sorted);
    return (Number)sorted.get(sorted.size() / 2);
  }
  


  public static double median(int[] values)
  {
    if (values.length == 0)
      throw new IllegalArgumentException("No median in an empty array");
    int[] sorted = Arrays.copyOf(values, values.length);
    Arrays.sort(sorted);
    return sorted[(sorted.length / 2)];
  }
  


  public static double median(double[] values)
  {
    if (values.length == 0)
      throw new IllegalArgumentException("No median in an empty array");
    double[] sorted = Arrays.copyOf(values, values.length);
    Arrays.sort(sorted);
    return sorted[(sorted.length / 2)];
  }
  


  public static <T extends Number> T mode(Collection<T> values)
  {
    if (values.isEmpty())
      throw new IllegalArgumentException(
        "No mode in an empty collection");
    Counter<T> c = new ObjectCounter();
    for (T n : values)
      c.count(n);
    return (Number)c.max();
  }
  


  public static int mode(int[] values)
  {
    if (values.length == 0)
      throw new IllegalArgumentException("No mode in an empty array");
    Counter<Integer> c = new ObjectCounter();
    int[] arrayOfInt = values;int j = values.length; for (int i = 0; i < j; i++) { int i = arrayOfInt[i];
      c.count(Integer.valueOf(i)); }
    return ((Integer)c.max()).intValue();
  }
  


  public static double mode(double[] values)
  {
    if (values.length == 0)
      throw new IllegalArgumentException("No mode in an empty array");
    Counter<Double> c = new ObjectCounter();
    double[] arrayOfDouble = values;int j = values.length; for (int i = 0; i < j; i++) { double d = arrayOfDouble[i];
      c.count(Double.valueOf(d)); }
    return ((Double)c.max()).doubleValue();
  }
  







  public static BitSet randomDistribution(int valuesToSet, int range)
  {
    if ((valuesToSet < 0) || (range <= 0)) {
      throw new IllegalArgumentException("must specificy a positive range and non-negative number of values to set.");
    }
    if (valuesToSet > range)
      throw new IllegalArgumentException("too many values (" + valuesToSet + 
        ") for range " + range);
    BitSet values = new BitSet(range);
    

    if (valuesToSet < range / 2) {
      int set = 0;
      while (set < valuesToSet) {
        int i = (int)(Math.random() * range);
        if (!values.get(i)) {
          values.set(i, true);
          set++;
        }
        
      }
    }
    else
    {
      values.set(0, range, true);
      int set = range;
      while (set > valuesToSet) {
        int i = (int)(Math.random() * range);
        if (values.get(i)) {
          values.set(i, false);
          set--;
        }
      }
    }
    return values;
  }
  


  public static double stddev(Collection<? extends Number> values)
  {
    double mean = mean(values);
    double sum = 0.0D;
    for (Number n : values) {
      double d = n.doubleValue() - mean;
      sum += d * d;
    }
    return Math.sqrt(sum / values.size());
  }
  


  public static double stddev(int[] values)
  {
    double mean = mean(values);
    double sum = 0.0D;
    int[] arrayOfInt = values;int j = values.length; for (int i = 0; i < j; i++) { int i = arrayOfInt[i];
      double d = i - mean;
      sum += d * d;
    }
    return Math.sqrt(sum / values.length);
  }
  


  public static double stddev(double[] values)
  {
    double mean = mean(values);
    double sum = 0.0D;
    double[] arrayOfDouble = values;int j = values.length; for (int i = 0; i < j; i++) { double d = arrayOfDouble[i];
      double d2 = d - mean;
      sum += d2 * d2;
    }
    return Math.sqrt(sum / values.length);
  }
  


  public static double sum(Collection<? extends Number> values)
  {
    double sum = 0.0D;
    for (Number n : values)
      sum += n.doubleValue();
    return sum;
  }
  


  public static int sum(int[] values)
  {
    int sum = 0;
    int[] arrayOfInt = values;int j = values.length; for (int i = 0; i < j; i++) { int i = arrayOfInt[i];
      sum += i; }
    return sum;
  }
  


  public static double sum(double[] values)
  {
    double sum = 0.0D;
    double[] arrayOfDouble = values;int j = values.length; for (int i = 0; i < j; i++) { double d = arrayOfDouble[i];
      sum += d; }
    return sum;
  }
}
