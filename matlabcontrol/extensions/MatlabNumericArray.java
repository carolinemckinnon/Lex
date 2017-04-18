package matlabcontrol.extensions;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;









































































































public final class MatlabNumericArray
{
  private final double[] _realValues;
  private final double[] _imaginaryValues;
  private final int[] _lengths;
  private final DoubleArrayType<?> _arrayType;
  private final boolean _fromMatlab;
  private final boolean _isReal;
  
  MatlabNumericArray(double[] real, double[] imaginary, int[] lengths)
  {
    _fromMatlab = true;
    
    _realValues = real;
    _imaginaryValues = imaginary;
    _isReal = (imaginary == null);
    
    _lengths = lengths;
    _arrayType = DoubleArrayType.getInstance(lengths.length);
  }
  



















  public <T> MatlabNumericArray(DoubleArrayType<T> type, T real, T imaginary)
  {
    if (type == null)
    {
      throw new NullPointerException("The type of the arrays may not be null.");
    }
    if (real == null)
    {
      throw new NullPointerException("Real array may not be null.");
    }
    
    _fromMatlab = false;
    _isReal = (imaginary == null);
    

    _arrayType = type;
    

    _lengths = new int[type.getDimensions()];
    int[] realLengths = computeBoundingLengths(real);
    for (int i = 0; i < realLengths.length; i++)
    {
      _lengths[i] = Math.max(_lengths[i], realLengths[i]);
    }
    if (imaginary != null)
    {
      int[] imaginaryLengths = computeBoundingLengths(imaginary);
      for (int i = 0; i < imaginaryLengths.length; i++)
      {
        _lengths[i] = Math.max(_lengths[i], imaginaryLengths[i]);
      }
    }
    

    _realValues = linearize(real, _lengths);
    if (imaginary != null)
    {
      _imaginaryValues = linearize(imaginary, _lengths);
    }
    else
    {
      _imaginaryValues = null;
    }
  }
  







  public MatlabNumericArray(double[][] real, double[][] imaginary)
  {
    this(DoubleArrayType.DIM_2, real, imaginary);
  }
  







  public MatlabNumericArray(double[][][] real, double[][][] imaginary)
  {
    this(DoubleArrayType.DIM_3, real, imaginary);
  }
  







  public MatlabNumericArray(double[][][][] real, double[][][][] imaginary)
  {
    this(DoubleArrayType.DIM_4, real, imaginary);
  }
  






  private static int getTotalSize(int[] lengths)
  {
    int size = 0;
    
    for (int length : lengths)
    {
      if (size == 0)
      {
        size = length;
      }
      else if (length != 0)
      {
        size *= length;
      }
    }
    
    return size;
  }
  








  public double getRealValue(int linearIndex)
  {
    return _realValues[linearIndex];
  }
  









  public double getImaginaryValue(int linearIndex)
  {
    if (_isReal)
    {
      throw new IllegalStateException("array is real");
    }
    
    return _imaginaryValues[linearIndex];
  }
  









  public double getRealValue(int... indices)
  {
    return getValue(_realValues, indices);
  }
  










  public double getImaginaryValue(int... indices)
  {
    if (_isReal)
    {
      throw new IllegalStateException("array is real");
    }
    
    return getValue(_imaginaryValues, indices);
  }
  
  private double getValue(double[] values, int... indices) throws MatlabNumericArray.ArrayDimensionException, ArrayIndexOutOfBoundsException
  {
    double value;
    if (indices.length == getDimensions())
    {

      for (int i = 0; i < indices.length; i++)
      {
        if (indices[i] >= _lengths[i])
        {
          throw new IndexOutOfBoundsException("[" + indices[i] + "] is out of bounds for dimension " + i + " where the length is " + _lengths[i]);
        }
      }
      


      value = values[multidimensionalIndicesToLinearIndex(_lengths, indices)];
    }
    else
    {
      throw new ArrayDimensionException(_arrayType.getDimensions(), indices.length);
    }
    double value;
    return value;
  }
  





  public int getDimensions()
  {
    return _arrayType.getDimensions();
  }
  








  public int[] getLengths()
  {
    int[] lengthsCopy = new int[_lengths.length];
    System.arraycopy(_lengths, 0, lengthsCopy, 0, _lengths.length);
    
    return lengthsCopy;
  }
  





  public int getLength()
  {
    return _realValues.length;
  }
  
  private <T> T getAsJavaArray(DoubleArrayType<T> type, double[] values)
  {
    if (type.getDimensions() != _arrayType.getDimensions())
    {
      throw new ArrayDimensionException(_arrayType.getDimensions(), type.getDimensions());
    }
    
    return multidimensionalize(values, _arrayClass, _lengths);
  }
  








  public <T> T getRealArray(DoubleArrayType<T> type)
  {
    return getAsJavaArray(type, _realValues);
  }
  






  public double[][] getRealArray2D()
  {
    return (double[][])getRealArray(DoubleArrayType.DIM_2);
  }
  






  public double[][][] getRealArray3D()
  {
    return (double[][][])getRealArray(DoubleArrayType.DIM_3);
  }
  






  public double[][][][] getRealArray4D()
  {
    return (double[][][][])getRealArray(DoubleArrayType.DIM_4);
  }
  









  public <T> T getImaginaryArray(DoubleArrayType<T> type)
  {
    if (_isReal)
    {
      throw new IllegalStateException("array is real");
    }
    
    return getAsJavaArray(type, _imaginaryValues);
  }
  







  public double[][] getImaginaryArray2D()
  {
    return (double[][])getImaginaryArray(DoubleArrayType.DIM_2);
  }
  







  public double[][][] getImaginaryArray3D()
  {
    return (double[][][])getImaginaryArray(DoubleArrayType.DIM_3);
  }
  







  public double[][][][] getImaginaryArray4D()
  {
    return (double[][][][])getImaginaryArray(DoubleArrayType.DIM_4);
  }
  






  public boolean isReal()
  {
    return _isReal;
  }
  





  double[] getRealLinearArray()
  {
    return _realValues;
  }
  





  double[] getImaginaryLinearArray()
  {
    return _imaginaryValues;
  }
  







  public String toString()
  {
    return "[" + getClass() + " dimensions=" + getDimensions() + "," + " linearLength=" + getLength() + "," + " lengths=" + Arrays.toString(_lengths) + "," + " fromMATLAB=" + _fromMatlab + "]";
  }
  











  private static int[] linearIndexToMultidimensionalIndices(int[] lengths, int linearIndex)
  {
    int[] indices = new int[lengths.length];
    
    if (lengths.length == 1)
    {
      indices[0] = linearIndex;
    }
    else
    {
      int pageSize = lengths[0] * lengths[1];
      int pageNumber = linearIndex / pageSize;
      

      int indexInPage = linearIndex % pageSize;
      indices[0] = (indexInPage % lengths[0]);
      indices[1] = (indexInPage / lengths[0]);
      

      int accumSize = 1;
      for (int dim = 2; dim < lengths.length; dim++)
      {
        indices[dim] = (pageNumber / accumSize % lengths[dim]);
        accumSize *= lengths[dim];
      }
    }
    
    return indices;
  }
  








  private static int multidimensionalIndicesToLinearIndex(int[] lengths, int[] indices)
  {
    if (lengths.length != indices.length)
    {
      throw new IllegalArgumentException("There must be an equal number of lengths [" + lengths.length + "] " + "and indices [" + indices.length + "]");
    }
    

    int linearIndex = 0;
    
    int accumSize = 1;
    for (int i = 0; i < lengths.length; i++)
    {
      linearIndex += accumSize * indices[i];
      accumSize *= lengths[i];
    }
    
    return linearIndex;
  }
  










  private static <T> T multidimensionalize(double[] linearArray, Class<T> outputArrayType, int[] lengths)
  {
    return multidimensionalize_internal(linearArray, outputArrayType, lengths, 0, new int[0]);
  }
  














  private static <T> T multidimensionalize_internal(double[] linearArray, Class<T> outputArrayType, int[] lengths, int indexIntoLengths, int[] currIndices)
  {
    Class<?> arrayType = outputArrayType.getComponentType();
    int arrayLength = lengths[indexIntoLengths];
    T array = Array.newInstance(arrayType, arrayLength);
    

    if (arrayType.isArray())
    {

      if (arrayType.equals([D.class))
      {

        int[] primitiveArrayIndices = new int[currIndices.length + 2];
        System.arraycopy(currIndices, 0, primitiveArrayIndices, 0, currIndices.length);
        

        for (int i = 0; i < arrayLength; i++)
        {
          primitiveArrayIndices[(primitiveArrayIndices.length - 2)] = i;
          

          double[] primitiveArray = new double[lengths[(lengths.length - 1)]];
          

          for (int j = 0; j < primitiveArray.length; j++)
          {
            primitiveArrayIndices[(primitiveArrayIndices.length - 1)] = j;
            int linearIndex = multidimensionalIndicesToLinearIndex(lengths, primitiveArrayIndices);
            primitiveArray[j] = linearArray[linearIndex];
          }
          

          Array.set(array, i, primitiveArray);
        }
        
      }
      else
      {
        for (int i = 0; i < arrayLength; i++)
        {
          int[] nextIndices = new int[currIndices.length + 1];
          System.arraycopy(currIndices, 0, nextIndices, 0, currIndices.length);
          nextIndices[(nextIndices.length - 1)] = i;
          
          Object innerArray = multidimensionalize_internal(linearArray, arrayType, lengths, indexIntoLengths + 1, nextIndices);
          
          Array.set(array, i, innerArray);
        }
        
      }
      
    }
    else {
      System.arraycopy(linearArray, 0, array, 0, arrayLength);
    }
    
    return array;
  }
  






  private static int[] computeBoundingLengths(Object array)
  {
    DoubleArrayType<?> type = DoubleArrayType.getInstanceUnsafe(array.getClass());
    int[] maxLengths = new int[type.getDimensions()];
    

    int arrayLength = Array.getLength(array);
    maxLengths[0] = arrayLength;
    

    if (!array.getClass().getComponentType().equals(Double.TYPE))
    {

      for (int i = 0; i < arrayLength; i++)
      {

        int[] childLengths = computeBoundingLengths(Array.get(array, i));
        for (int j = 0; j < childLengths.length; j++)
        {
          maxLengths[(j + 1)] = Math.max(maxLengths[(j + 1)], childLengths[j]);
        }
      }
    }
    
    return maxLengths;
  }
  










  private static double[] linearize(Object array, int[] lengths)
  {
    double[] linearArray = new double[getTotalSize(lengths)];
    

    linearize_internal(linearArray, array, lengths, new int[0]);
    
    return linearArray;
  }
  









  private static void linearize_internal(double[] linearArray, Object array, int[] lengths, int[] currIndices)
  {
    if (array.getClass().equals([D.class))
    {
      int[] doubleArrayIndices = new int[currIndices.length + 1];
      System.arraycopy(currIndices, 0, doubleArrayIndices, 0, currIndices.length);
      

      double[] doubleArray = (double[])array;
      for (int i = 0; i < doubleArray.length; i++)
      {
        doubleArrayIndices[(doubleArrayIndices.length - 1)] = i;
        int linearIndex = multidimensionalIndicesToLinearIndex(lengths, doubleArrayIndices);
        linearArray[linearIndex] = doubleArray[i];
      }
    }
    else
    {
      int arrayLength = Array.getLength(array);
      for (int i = 0; i < arrayLength; i++)
      {
        int[] nextIndices = new int[currIndices.length + 1];
        System.arraycopy(currIndices, 0, nextIndices, 0, currIndices.length);
        nextIndices[(nextIndices.length - 1)] = i;
        
        linearize_internal(linearArray, Array.get(array, i), lengths, nextIndices);
      }
    }
  }
  

  public static class ArrayDimensionException
    extends RuntimeException
  {
    private static final long serialVersionUID = 50176L;
    
    private final int _actualNumberOfDimensions;
    
    private final int _usedAsNumberOfDimensions;
    
    ArrayDimensionException(int actualNumDim, int usedAsNumDim)
    {
      super();
      

      _actualNumberOfDimensions = actualNumDim;
      _usedAsNumberOfDimensions = usedAsNumDim;
    }
    





    public int getActualNumberOfDimensions()
    {
      return _actualNumberOfDimensions;
    }
    





    public int getUsedNumberOfDimensions()
    {
      return _usedAsNumberOfDimensions;
    }
  }
  











  public static final class DoubleArrayType<T>
  {
    private static final Map<Class<?>, DoubleArrayType> CLASS_TO_ARRAY_TYPE = new ConcurrentHashMap();
    




    public static final DoubleArrayType<double[][]> DIM_2 = getInstance([[D.class);
    



    public static final DoubleArrayType<double[][][]> DIM_3 = getInstance([[[D.class);
    



    public static final DoubleArrayType<double[][][][]> DIM_4 = getInstance([[[[D.class);
    



    public static final DoubleArrayType<double[][][][][]> DIM_5 = getInstance([[[[[D.class);
    



    public static final DoubleArrayType<double[][][][][][]> DIM_6 = getInstance([[[[[[D.class);
    



    public static final DoubleArrayType<double[][][][][][][]> DIM_7 = getInstance([[[[[[[D.class);
    



    public static final DoubleArrayType<double[][][][][][][][]> DIM_8 = getInstance([[[[[[[[D.class);
    



    public static final DoubleArrayType<double[][][][][][][][][]> DIM_9 = getInstance([[[[[[[[[D.class);
    




    private final Class<T> _arrayClass;
    




    private final int _numDimensions;
    




    private DoubleArrayType(Class<T> arrayClass)
    {
      if (!isDoubleArrayType(arrayClass))
      {
        throw new IllegalArgumentException(arrayClass + " does not hold doubles");
      }
      
      _arrayClass = arrayClass;
      _numDimensions = getNumberOfDimensions(arrayClass);
    }
    
















    public static <T> DoubleArrayType<T> getInstance(Class<T> arrayType)
    {
      if (arrayType.equals([D.class))
      {
        throw new IllegalArgumentException(arrayType + " not supported, must be 2 or more dimensions");
      }
      
      return getInstanceUnsafe(arrayType);
    }
    




    static <T> DoubleArrayType<T> getInstanceUnsafe(Class<T> arrayType)
    {
      if (!CLASS_TO_ARRAY_TYPE.containsKey(arrayType))
      {
        DoubleArrayType<T> type = new DoubleArrayType(arrayType);
        CLASS_TO_ARRAY_TYPE.put(arrayType, type);
      }
      
      return (DoubleArrayType)CLASS_TO_ARRAY_TYPE.get(arrayType);
    }
    



    static DoubleArrayType<?> getInstance(int dimensions)
    {
      String className = "";
      for (int i = 0; i < dimensions; i++)
      {
        className = className + "[";
      }
      className = className + "D";
      
      DoubleArrayType<?> type;
      try
      {
        type = getInstanceUnsafe(Class.forName(className));
      }
      catch (ClassNotFoundException e)
      {
        type = null;
      }
      
      return type;
    }
    





    public int getDimensions()
    {
      return _numDimensions;
    }
    





    public Class<T> getArrayClass()
    {
      return _arrayClass;
    }
    


    private static boolean isDoubleArrayType(Class<?> type)
    {
      boolean isType;
      

      boolean isType;
      

      if (type.isArray())
      {
        while (type.isArray())
        {
          type = type.getComponentType();
        }
        
        isType = type.equals(Double.TYPE);
      }
      else
      {
        isType = false;
      }
      
      return isType;
    }
    







    private static int getNumberOfDimensions(Class<?> type)
    {
      int numDim = 0;
      while (type.isArray())
      {
        numDim++;
        type = type.getComponentType();
      }
      
      return numDim;
    }
    







    public String toString()
    {
      return "[" + getClass().getName() + " class=" + _arrayClass + ", dimensions=" + _numDimensions + "]";
    }
  }
}
