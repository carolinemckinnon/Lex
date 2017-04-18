package edu.ucla.sspace.vector;

import java.lang.reflect.Constructor;
import java.util.Arrays;















































public class Vectors
{
  private Vectors() {}
  
  public static DoubleVector asDouble(Vector v)
  {
    if (v == null) {
      throw new NullPointerException("Cannot re-type a null vector");
    }
    
    if ((v instanceof SparseIntegerVector))
      return new IntAsSparseDoubleVector((SparseIntegerVector)v);
    if ((v instanceof SparseDoubleVector))
      return (SparseDoubleVector)v;
    if ((v instanceof IntegerVector))
      return new IntAsDoubleVector((IntegerVector)v);
    if ((v instanceof DoubleVector)) {
      return (DoubleVector)v;
    }
    

    return new ViewVectorAsDoubleVector(v);
  }
  
  public static SparseDoubleVector asDouble(SparseIntegerVector v) {
    if (v == null)
      throw new NullPointerException("Cannot re-type a null vector");
    return new IntAsSparseDoubleVector(v);
  }
  








  public static DoubleVector asVector(double[] array)
  {
    if (array == null)
      throw new NullPointerException("Cannot wrap a null array");
    return new DoubleArrayAsVector(array);
  }
  








  public static IntegerVector asVector(int[] array)
  {
    if (array == null)
      throw new NullPointerException("Cannot wrap a null array");
    return new IntArrayAsVector(array);
  }
  





  public static boolean equals(DoubleVector v1, DoubleVector v2)
  {
    if (v1.length() == v2.length()) {
      int length = v1.length();
      for (int i = 0; i < length; i++) {
        if (v1.get(i) != v2.get(i))
          return false;
      }
      return true;
    }
    return false;
  }
  





  public static boolean equals(IntegerVector v1, IntegerVector v2)
  {
    if (v1.length() == v2.length()) {
      int length = v1.length();
      for (int i = 0; i < length; i++) {
        if (v1.get(i) != v2.get(i))
          return false;
      }
      return true;
    }
    return false;
  }
  






  public static boolean equals(Vector v1, Vector v2)
  {
    if (v1.length() == v2.length()) {
      int length = v1.length();
      for (int i = 0; i < length; i++) {
        Number n1 = v1.getValue(i);
        Number n2 = v2.getValue(i);
        if (!n1.equals(n2))
          return false;
      }
      return true;
    }
    return false;
  }
  





  public static DoubleVector immutable(DoubleVector vector)
  {
    if (vector == null) {
      throw new NullPointerException("Cannot create an immutable null vector");
    }
    return new DoubleVectorView(vector, true);
  }
  





  public static SparseDoubleVector immutable(SparseDoubleVector vector)
  {
    if (vector == null) {
      throw new NullPointerException("Cannot create an immutable null vector");
    }
    return new ViewDoubleAsDoubleSparseVector(vector, true);
  }
  





  public static IntegerVector immutable(IntegerVector vector)
  {
    if (vector == null) {
      throw new NullPointerException("Cannot create an immutable null vector");
    }
    return new IntegerVectorView(vector, true);
  }
  





  public static SparseIntegerVector immutable(SparseIntegerVector vector)
  {
    if (vector == null) {
      throw new NullPointerException("Cannot create an immutable null vector");
    }
    return new ViewIntegerAsIntegerSparseVector(vector, true);
  }
  





  public static Vector immutable(Vector vector)
  {
    if (vector == null) {
      throw new NullPointerException("Cannot create an immutable null vector");
    }
    return new VectorView(vector, true);
  }
  







  public static DoubleVector atomic(DoubleVector vector)
  {
    if (vector == null) {
      throw new NullPointerException("Cannot create an atomic null vector");
    }
    return new AtomicVector(vector);
  }
  







  public static DoubleVector synchronizedVector(DoubleVector vector)
  {
    if (vector == null) {
      throw new NullPointerException("Cannot create an synchronized null vector");
    }
    return new SynchronizedVector(vector);
  }
  
  public static DoubleVector scaleByMagnitude(DoubleVector vector) {
    if ((vector instanceof SparseDoubleVector))
      return scaleByMagnitude((SparseDoubleVector)vector);
    return new ScaledDoubleVector(vector, 1.0D / vector.magnitude());
  }
  
  public static DoubleVector scaleByMagnitude(SparseDoubleVector vector) {
    return new ScaledSparseDoubleVector(vector, 1.0D / vector.magnitude());
  }
  
















  public static Vector subview(Vector vector, int offset, int length)
  {
    if ((vector instanceof IntegerVector)) {
      return subview((IntegerVector)vector, offset, length);
    }
    return subview(asDouble(vector), offset, length);
  }
  















  public static DoubleVector subview(DoubleVector vector, int offset, int length)
  {
    if (vector == null) {
      throw new NullPointerException("Cannot create view of a null vector");
    }
    return new DoubleVectorView(vector, offset, length);
  }
  















  public static SparseDoubleVector subview(SparseDoubleVector vector, int offset, int length)
  {
    if (vector == null) {
      throw new NullPointerException("Cannot create view of a null vector");
    }
    return new ViewDoubleAsDoubleSparseVector(vector, offset, length);
  }
  














  public static IntegerVector subview(IntegerVector vector, int offset, int length)
  {
    if (vector == null) {
      throw new NullPointerException("Cannot create view of a null vector");
    }
    return new IntegerVectorView(vector, offset, length);
  }
  














  public static SparseIntegerVector subview(SparseIntegerVector vector, int offset, int length)
  {
    if (vector == null) {
      throw new NullPointerException("Cannot create view of a null vector");
    }
    return new SparseIntegerVectorView(vector, offset, length);
  }
  













  public static Vector copy(Vector dest, Vector source)
  {
    for (int i = 0; i < source.length(); i++)
      dest.set(i, Double.valueOf(source.getValue(i).doubleValue()));
    return dest;
  }
  







  public static DoubleVector copyOf(DoubleVector source)
  {
    if (source == null)
      return null;
    DoubleVector result = null;
    
    if ((source instanceof SparseDoubleVector)) {
      result = new CompactSparseVector(source.length());
      copyFromSparseVector(result, source);
    } else if (((source instanceof DenseVector)) || 
      ((source instanceof ScaledDoubleVector))) {
      result = new DenseVector(source.length());
      for (int i = 0; i < source.length(); i++)
        result.set(i, source.get(i));
    } else if ((source instanceof AmortizedSparseVector)) {
      result = new AmortizedSparseVector(source.length());
      copyFromSparseVector(result, source);
    } else { if ((source instanceof DoubleVectorView)) {
        DoubleVectorView view = (DoubleVectorView)source;
        return copyOf(view.getOriginalVector());
      }
      



      Class<? extends DoubleVector> sourceClazz = source.getClass();
      try {
        Constructor<? extends DoubleVector> constructor = 
          sourceClazz.getConstructor(new Class[] { DoubleVector.class });
        result = (DoubleVector)constructor.newInstance(new Object[] { source });
      }
      catch (NoSuchMethodException nsme)
      {
        try
        {
          Constructor<? extends DoubleVector> constructor = 
            sourceClazz.getConstructor(new Class[] { Integer.TYPE });
          int length = source.length();
          result = (DoubleVector)constructor.newInstance(new Object[] {
            Integer.valueOf(length) });
          
          for (int i = 0; i < length; i++) {
            result.set(i, source.get(i));
          }
        } catch (Exception e) {
          throw new UnsupportedOperationException(
            "Could not find applicalble way to copy a vector of type " + 
            source.getClass(), e);
        }
      }
      catch (Exception e) {
        throw new UnsupportedOperationException(
          "Could not find applicalble way to copy a vector of type " + 
          source.getClass(), e);
      }
    }
    return result;
  }
  






  public static Vector copyOf(Vector source)
  {
    if ((source instanceof DoubleVector))
      return copyOf((DoubleVector)source);
    if ((source instanceof IntegerVector)) {
      return copyOf((IntegerVector)source);
    }
    Vector result = new DenseVector(source.length());
    for (int i = 0; i < source.length(); i++)
      result.set(i, source.getValue(i));
    return result;
  }
  







  public static IntegerVector copyOf(IntegerVector source)
  {
    IntegerVector result = null;
    
    if ((source instanceof TernaryVector)) {
      TernaryVector v = (TernaryVector)source;
      int[] pos = v.positiveDimensions();
      int[] neg = v.negativeDimensions();
      result = new TernaryVector(source.length(), 
        Arrays.copyOf(pos, pos.length), 
        Arrays.copyOf(neg, neg.length));
    } else if ((source instanceof SparseVector)) {
      result = new SparseHashIntegerVector(source.length());
      copyFromSparseVector(result, source);
    } else {
      result = new DenseIntVector(source.length());
      for (int i = 0; i < source.length(); i++)
        result.set(i, source.get(i));
    }
    return result;
  }
  















  public static <T extends Vector> T instanceOf(T vector)
  {
    if ((vector instanceof CompactSparseIntegerVector)) {
      return new CompactSparseIntegerVector(vector.length());
    }
    

    try
    {
      Class<T> clazz = vector.getClass();
      Constructor<T> c = clazz.getConstructor(new Class[] { Integer.TYPE });
      return (Vector)c.newInstance(new Object[] { Integer.valueOf(vector.length()) });
    }
    catch (Exception e) {
      throw new IllegalArgumentException("Cannot instantiate a vector of type " + 
        vector.getClass(), e);
    }
  }
  







  private static void copyFromSparseVector(DoubleVector destination, DoubleVector source)
  {
    int[] nonZeroIndices = ((SparseVector)source).getNonZeroIndices();
    for (int index : nonZeroIndices) {
      destination.set(index, source.get(index));
    }
  }
  





  private static void copyFromSparseVector(IntegerVector destination, IntegerVector source)
  {
    int[] nonZeroIndices = ((SparseVector)source).getNonZeroIndices();
    for (int index : nonZeroIndices) {
      destination.set(index, source.get(index));
    }
  }
}
