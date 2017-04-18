package edu.ucla.sspace.vector;

import edu.ucla.sspace.util.DoubleEntry;
import edu.ucla.sspace.util.IntegerEntry;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;










































public class VectorMath
{
  private VectorMath() {}
  
  public static Vector add(Vector vector1, Vector vector2)
  {
    if (vector2.length() != vector1.length())
      throw new IllegalArgumentException(
        "Vectors of different sizes cannot be added");
    if (((vector2 instanceof IntegerVector)) && 
      ((vector1 instanceof DoubleVector)))
      return add(vector1, Vectors.asDouble(vector2));
    if ((vector2 instanceof SparseVector)) {
      addSparseValues(vector1, vector2);
    } else {
      int length = vector2.length();
      for (int i = 0; i < length; i++) {
        double value = vector2.getValue(i).doubleValue() + 
          vector1.getValue(i).doubleValue();
        vector1.set(i, Double.valueOf(value));
      }
    }
    
    return vector1;
  }
  








  public static DoubleVector add(DoubleVector vector1, DoubleVector vector2)
  {
    if (vector2.length() != vector1.length()) {
      throw new IllegalArgumentException(
        "Vectors of different sizes cannot be added.  Lengths are: vector1: " + 
        vector1.length() + 
        ", vector2: " + vector2.length());
    }
    
    if ((vector2 instanceof SparseVector)) {
      addSparseValues(vector1, vector2);
    }
    else
    {
      int length = vector2.length();
      for (int i = 0; i < length; i++) {
        double value = vector2.get(i);
        
        if (value != 0.0D)
          vector1.add(i, value);
      }
    }
    return vector1;
  }
  








  public static IntegerVector add(IntegerVector vector1, IntegerVector vector2)
  {
    if (vector2.length() != vector1.length()) {
      throw new IllegalArgumentException(
        "Vectors of different sizes cannot be added");
    }
    
    if ((vector2 instanceof SparseVector)) {
      addSparseValues(vector1, vector2);
    } else if ((vector2 instanceof TernaryVector)) {
      addTernaryValues(vector1, (TernaryVector)vector2);
    }
    else
    {
      int length = vector2.length();
      for (int i = 0; i < length; i++) {
        int value = vector2.get(i);
        
        if (value != 0.0D)
          vector1.add(i, value);
      }
    }
    return vector1;
  }
  







  public static Vector addUnmodified(Vector vector1, Vector vector2)
  {
    if (vector2.length() != vector1.length())
      throw new IllegalArgumentException(
        "Vectors of different sizes cannot be added");
    return addUnmodified(Vectors.asDouble(vector1), 
      Vectors.asDouble(vector2));
  }
  








  public static DoubleVector addUnmodified(DoubleVector vector1, DoubleVector vector2)
  {
    if (vector2.length() != vector1.length())
      throw new IllegalArgumentException(
        "Vectors of different sizes cannot be added");
    DoubleVector finalVector = Vectors.copyOf(vector1);
    

    if ((vector2 instanceof SparseVector)) {
      addSparseValues(finalVector, vector2);
    }
    else
    {
      int length = vector2.length();
      for (int i = 0; i < length; i++) {
        double value = vector2.get(i);
        finalVector.add(i, value);
      }
    }
    return finalVector;
  }
  








  public static IntegerVector addUnmodified(IntegerVector vector1, IntegerVector vector2)
  {
    if (vector2.length() != vector1.length())
      throw new IllegalArgumentException(
        "Vectors of different sizes cannot be added");
    IntegerVector finalVector = Vectors.copyOf(vector1);
    

    if ((vector2 instanceof SparseVector)) {
      addSparseValues(finalVector, vector2);
    } else if ((vector2 instanceof TernaryVector)) {
      addTernaryValues(finalVector, (TernaryVector)vector2);
    }
    else
    {
      int length = vector2.length();
      for (int i = 0; i < length; i++) {
        int value = vector2.get(i);
        finalVector.add(i, value);
      }
    }
    return finalVector;
  }
  










  public static Vector addWithScalars(Vector vector1, double weight1, Vector vector2, double weight2)
  {
    if (vector2.length() != vector1.length())
      throw new IllegalArgumentException(
        "Vectors of different sizes cannot be added");
    int length = vector2.length();
    for (int i = 0; i < length; i++) {
      double value = vector1.getValue(i).doubleValue() * weight1 + 
        vector2.getValue(i).doubleValue() * weight2;
      vector1.set(i, Double.valueOf(value));
    }
    return vector1;
  }
  











  public static Vector addWithScalars(DoubleVector vector1, double weight1, DoubleVector vector2, double weight2)
  {
    if (vector2.length() != vector1.length())
      throw new IllegalArgumentException(
        "Vectors of different sizes cannot be added");
    int length = vector2.length();
    for (int i = 0; i < length; i++) {
      double value = vector1.get(i) * weight1 + 
        vector2.get(i) * weight2;
      vector1.set(i, value);
    }
    return vector1;
  }
  











  public static Vector addWithScalars(IntegerVector vector1, int weight1, IntegerVector vector2, int weight2)
  {
    if (vector2.length() != vector1.length())
      throw new IllegalArgumentException(
        "Vectors of different sizes cannot be added");
    int length = vector2.length();
    for (int i = 0; i < length; i++) {
      double value = vector1.get(i) * weight1 + 
        vector2.get(i) * weight2;
      vector1.set(i, Double.valueOf(value));
    }
    return vector1;
  }
  












  public static double dotProduct(Vector x, Vector y)
  {
    if (((x instanceof IntegerVector)) && ((y instanceof IntegerVector))) {
      return dotProduct((IntegerVector)x, (IntegerVector)y);
    }
    

    return dotProduct(Vectors.asDouble(x), Vectors.asDouble(y));
  }
  












  public static double dotProduct(DoubleVector a, DoubleVector b)
  {
    if (a.length() != b.length()) {
      throw new IllegalArgumentException(
        "cannot compute dot product of vectors with different lengths");
    }
    double dotProduct = 0.0D;
    
    double bValue;
    
    if (((a instanceof Iterable)) && ((b instanceof Iterable)))
    {


      boolean useA = 
        ((a instanceof SparseVector)) && ((b instanceof SparseVector)) && (
        ((SparseVector)a).getNonZeroIndices().length < 
        ((SparseVector)b).getNonZeroIndices().length);
      




      if (useA) {
        for (DoubleEntry e : (Iterable)a) {
          int index = e.index();
          double aValue = e.value();
          double bValue = b.get(index);
          dotProduct += aValue * bValue;
        }
        
      } else {
        for (DoubleEntry e : (Iterable)b) {
          int index = e.index();
          double aValue = a.get(index);
          bValue = e.value();
          dotProduct += aValue * bValue;
        }
        
      }
      

    }
    else if (((a instanceof SparseVector)) && ((b instanceof SparseVector))) {
      SparseVector svA = (SparseVector)a;
      SparseVector svB = (SparseVector)b;
      int[] nzA = svA.getNonZeroIndices();
      int[] nzB = svB.getNonZeroIndices();
      

      int[] arrayOfInt1;
      
      if (nzA.length < nzB.length) {
        bValue = (arrayOfInt1 = nzA).length; for (double d1 = 0; d1 < bValue; d1++) { int nz = arrayOfInt1[d1];
          double aValue = a.get(nz);
          double bValue = b.get(nz);
          dotProduct += aValue * bValue;
        }
      }
      else {
        bValue = (arrayOfInt1 = nzB).length; for (double d2 = 0; d2 < bValue; d2++) { int nz = arrayOfInt1[d2];
          double aValue = a.get(nz);
          double bValue = b.get(nz);
          dotProduct += aValue * bValue;
        }
        
      }
    }
    else
    {
      for (int i = 0; i < b.length(); i++) {
        double aValue = a.get(i);
        double bValue = b.get(i);
        dotProduct += aValue * bValue;
      }
    }
    
    return dotProduct;
  }
  












  public static int dotProduct(IntegerVector a, IntegerVector b)
  {
    if (a.length() != b.length()) {
      throw new IllegalArgumentException(
        "cannot compute dot product of vectors with different lengths");
    }
    int dotProduct = 0;
    
    int bValue;
    
    if (((a instanceof Iterable)) && ((b instanceof Iterable)))
    {


      boolean useA = 
        ((a instanceof SparseVector)) && ((b instanceof SparseVector)) && (
        ((SparseVector)a).getNonZeroIndices().length < 
        ((SparseVector)b).getNonZeroIndices().length);
      



      if (useA) {
        for (IntegerEntry e : (Iterable)a) {
          int index = e.index();
          int aValue = e.value();
          int bValue = b.get(index);
          dotProduct += aValue * bValue;
        }
        
      } else {
        for (IntegerEntry e : (Iterable)b) {
          int index = e.index();
          int aValue = a.get(index);
          bValue = e.value();
          dotProduct += aValue * bValue;
        }
        
      }
      

    }
    else if (((a instanceof SparseVector)) && ((b instanceof SparseVector))) {
      SparseVector svA = (SparseVector)a;
      SparseVector svB = (SparseVector)b;
      int[] nzA = svA.getNonZeroIndices();
      int[] nzB = svB.getNonZeroIndices();
      



      if (nzA.length < nzB.length) {
        for (int nz : nzA) {
          int aValue = a.get(nz);
          int bValue = b.get(nz);
          dotProduct += aValue * bValue;
        }
        
      } else {
        for (int nz : nzB) {
          int aValue = a.get(nz);
          int bValue = b.get(nz);
          dotProduct += aValue * bValue;
        }
        
      }
    }
    else
    {
      for (int i = 0; i < b.length(); i++) {
        int aValue = a.get(i);
        int bValue = b.get(i);
        dotProduct += aValue * bValue;
      }
    }
    return dotProduct;
  }
  









  public static Vector multiply(Vector left, Vector right)
  {
    if (left.length() != right.length())
      throw new IllegalArgumentException(
        "Vectors of different sizes cannot be multiplied");
    int length = left.length();
    for (int i = 0; i < length; i++)
      left.set(i, 
        Double.valueOf(left.getValue(i).doubleValue() * right.getValue(i).doubleValue()));
    return left;
  }
  









  public static DoubleVector multiply(DoubleVector left, DoubleVector right)
  {
    if (left.length() != right.length()) {
      throw new IllegalArgumentException(
        "Vectors of different sizes cannot be multiplied");
    }
    if (((left instanceof SparseVector)) && ((right instanceof SparseVector))) {
      TIntSet lnz = new TIntHashSet(((SparseVector)left).getNonZeroIndices());
      for (int nz : ((SparseVector)right).getNonZeroIndices()) {
        if (lnz.contains(nz)) {
          left.set(nz, left.get(nz) * right.get(nz));
          lnz.remove(nz);
        }
      }
      



      TIntIterator iter = lnz.iterator();
      while (iter.hasNext()) {
        left.set(iter.next(), 0.0D);
      }
    } else {
      int length = left.length();
      for (int i = 0; i < length; i++)
        left.set(i, left.get(i) * right.get(i));
    }
    return left;
  }
  









  public static SparseDoubleVector multiplyUnmodified(SparseDoubleVector a, SparseDoubleVector b)
  {
    SparseDoubleVector result = new CompactSparseVector();
    int[] nonZerosA = a.getNonZeroIndices();
    int[] nonZerosB = b.getNonZeroIndices();
    if ((nonZerosA.length == 0) || (nonZerosB.length == 0)) {
      return result;
    }
    if (nonZerosA[(nonZerosA.length - 1)] > nonZerosB[(nonZerosB.length - 1)]) {
      SparseDoubleVector t = b;
      b = a;
      a = t;
    }
    nonZerosA = a.getNonZeroIndices();
    
    for (int index : nonZerosA) {
      double v = a.get(index);
      double w = b.get(index);
      if (w != 0.0D)
        result.set(index, v * w);
    }
    return result;
  }
  










  public static IntegerVector multiply(IntegerVector left, IntegerVector right)
  {
    if (left.length() != right.length())
      throw new IllegalArgumentException(
        "Vectors of different sizes cannot be multiplied");
    int length = left.length();
    for (int i = 0; i < length; i++)
      left.set(i, left.get(i) * right.get(i));
    return left;
  }
  










  public static DoubleVector multiplyUnmodified(DoubleVector left, DoubleVector right)
  {
    if (left.length() != right.length())
      throw new IllegalArgumentException(
        "Vectors of different sizes cannot be multiplied");
    DoubleVector result;
    DoubleVector result; if (((left instanceof SparseVector)) || 
      ((right instanceof SparseVector))) {
      result = new CompactSparseVector(left.length());
    } else {
      result = new DenseVector(left.length());
    }
    int length = left.length();
    for (int i = 0; i < length; i++)
      result.set(i, left.get(i) * right.get(i));
    return result;
  }
  







  public static Vector subtract(Vector vector1, Vector vector2)
  {
    if (vector2.length() != vector1.length())
      throw new IllegalArgumentException(
        "Vectors of different sizes cannot be added");
    if (((vector2 instanceof IntegerVector)) && 
      ((vector1 instanceof DoubleVector)))
      return subtract(vector1, Vectors.asDouble(vector2));
    if ((vector2 instanceof SparseVector)) {
      subtractSparseValues(vector1, vector2);
    } else {
      for (int i = 0; i < vector2.length(); i++) {
        double value = vector1.getValue(i).doubleValue() - 
          vector2.getValue(i).doubleValue();
        vector1.set(i, Double.valueOf(value));
      }
    }
    
    return vector1;
  }
  








  public static DoubleVector subtract(DoubleVector vector1, DoubleVector vector2)
  {
    if (vector2.length() != vector1.length()) {
      throw new IllegalArgumentException(
        "Vectors of different sizes cannot be added");
    }
    
    if ((vector2 instanceof SparseVector)) {
      subtractSparseValues(vector1, vector2);
    }
    else
    {
      for (int i = 0; i < vector2.length(); i++) {
        double value = vector2.get(i);
        
        if (value != 0.0D)
          vector1.add(i, -1.0D * value);
      }
    }
    return vector1;
  }
  








  public static IntegerVector subtract(IntegerVector vector1, IntegerVector vector2)
  {
    if (vector2.length() != vector1.length()) {
      throw new IllegalArgumentException(
        "Vectors of different sizes cannot be added");
    }
    
    if ((vector2 instanceof SparseVector)) {
      subtractSparseValues(vector1, vector2);
    } else if ((vector2 instanceof TernaryVector)) {
      subtractTernaryValues(vector1, (TernaryVector)vector2);
    }
    else
    {
      for (int i = 0; i < vector2.length(); i++) {
        int value = vector2.get(i);
        
        if (value != 0.0D)
          vector1.add(i, -1 * value);
      }
    }
    return vector1;
  }
  


  public static double sum(Vector v)
  {
    return sum(Vectors.asDouble(v));
  }
  


  public static double sum(DoubleVector v)
  {
    double sum = 0.0D;
    if ((v instanceof SparseVector)) {
      for (int nz : ((SparseVector)v).getNonZeroIndices()) {
        sum += v.get(nz);
      }
    } else {
      int len = v.length();
      for (int i = 0; i < len; i++)
        sum += v.get(i);
    }
    return sum;
  }
  


  public static int sum(IntegerVector v)
  {
    int sum = 0;
    if ((v instanceof SparseVector)) {
      for (int nz : ((SparseVector)v).getNonZeroIndices()) {
        sum += v.get(nz);
      }
    } else {
      int len = v.length();
      for (int i = 0; i < len; i++)
        sum += v.get(i);
    }
    return sum;
  }
  







  private static void addSparseValues(DoubleVector destination, DoubleVector source)
  {
    int[] otherIndices = ((SparseVector)source).getNonZeroIndices();
    for (int index : otherIndices) {
      destination.add(index, source.get(index));
    }
  }
  






  private static void addSparseValues(IntegerVector destination, IntegerVector source)
  {
    int[] otherIndices = ((SparseVector)source).getNonZeroIndices();
    for (int index : otherIndices) {
      destination.add(index, source.get(index));
    }
  }
  







  private static void addSparseValues(Vector destination, Vector source)
  {
    int[] otherIndices = ((SparseVector)source).getNonZeroIndices();
    for (int index : otherIndices) {
      double value = destination.getValue(index).doubleValue() + 
        source.getValue(index).doubleValue();
      destination.set(index, Double.valueOf(value));
    }
  }
  







  private static void addTernaryValues(IntegerVector destination, TernaryVector source)
  {
    for (int p : source.positiveDimensions())
      destination.add(p, 1);
    for (int n : source.negativeDimensions()) {
      destination.add(n, -1);
    }
  }
  






  private static void addTernaryValues(DoubleVector destination, TernaryVector source)
  {
    for (int p : source.positiveDimensions())
      destination.add(p, 1.0D);
    for (int n : source.negativeDimensions()) {
      destination.add(n, -1.0D);
    }
  }
  






  private static void addTernaryValues(Vector destination, TernaryVector source)
  {
    for (int p : source.positiveDimensions())
      destination.set(p, Double.valueOf(1.0D + destination.getValue(p).doubleValue()));
    for (int n : source.negativeDimensions()) {
      destination.set(n, Double.valueOf(-1.0D + destination.getValue(n).doubleValue()));
    }
  }
  






  private static void subtractSparseValues(DoubleVector destination, DoubleVector source)
  {
    int[] otherIndices = ((SparseVector)source).getNonZeroIndices();
    for (int index : otherIndices) {
      destination.add(index, -1.0D * source.get(index));
    }
  }
  






  private static void subtractSparseValues(IntegerVector destination, IntegerVector source)
  {
    int[] otherIndices = ((SparseVector)source).getNonZeroIndices();
    for (int index : otherIndices) {
      destination.add(index, -1 * source.get(index));
    }
  }
  







  private static void subtractSparseValues(Vector destination, Vector source)
  {
    int[] otherIndices = ((SparseVector)source).getNonZeroIndices();
    for (int index : otherIndices) {
      double value = destination.getValue(index).doubleValue() - 
        source.getValue(index).doubleValue();
      destination.set(index, Double.valueOf(value));
    }
  }
  







  private static void subtractTernaryValues(IntegerVector destination, TernaryVector source)
  {
    for (int p : source.positiveDimensions())
      destination.add(p, -1);
    for (int n : source.negativeDimensions()) {
      destination.add(n, 1);
    }
  }
  






  private static void subtractTernaryValues(DoubleVector destination, TernaryVector source)
  {
    for (int p : source.positiveDimensions())
      destination.add(p, -1.0D);
    for (int n : source.negativeDimensions()) {
      destination.add(n, 1.0D);
    }
  }
  






  private static void subtractTernaryValues(Vector destination, TernaryVector source)
  {
    for (int p : source.positiveDimensions())
      destination.set(p, Double.valueOf(destination.getValue(p).doubleValue() - 1.0D));
    for (int n : source.negativeDimensions()) {
      destination.set(n, Double.valueOf(destination.getValue(n).doubleValue() + 1.0D));
    }
  }
}
