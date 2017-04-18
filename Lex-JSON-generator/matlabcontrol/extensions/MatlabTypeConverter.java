package matlabcontrol.extensions;

import java.io.Serializable;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxy.MatlabThreadCallable;
import matlabcontrol.MatlabProxy.MatlabThreadProxy;






































public class MatlabTypeConverter
{
  private final MatlabProxy _proxy;
  
  public MatlabTypeConverter(MatlabProxy proxy)
  {
    _proxy = proxy;
  }
  






  public MatlabNumericArray getNumericArray(String arrayName)
    throws MatlabInvocationException
  {
    ArrayInfo info = (ArrayInfo)_proxy.invokeAndWait(new GetArrayCallable(arrayName));
    
    return new MatlabNumericArray(real, imaginary, lengths);
  }
  
  private static class GetArrayCallable implements MatlabProxy.MatlabThreadCallable<MatlabTypeConverter.ArrayInfo>, Serializable
  {
    private final String _arrayName;
    
    public GetArrayCallable(String arrayName)
    {
      _arrayName = arrayName;
    }
    

    public MatlabTypeConverter.ArrayInfo call(MatlabProxy.MatlabThreadProxy proxy)
      throws MatlabInvocationException
    {
      Object realObject = proxy.returningEval("real(" + _arrayName + ");", 1)[0];
      double[] realValues = (double[])realObject;
      

      boolean isReal = ((boolean[])(boolean[])proxy.returningEval("isreal(" + _arrayName + ");", 1)[0])[0];
      double[] imaginaryValues = null;
      if (!isReal)
      {
        Object imaginaryObject = proxy.returningEval("imag(" + _arrayName + ");", 1)[0];
        imaginaryValues = (double[])imaginaryObject;
      }
      

      double[] size = (double[])proxy.returningEval("size(" + _arrayName + ");", 1)[0];
      int[] lengths = new int[size.length];
      for (int i = 0; i < size.length; i++)
      {
        lengths[i] = ((int)size[i]);
      }
      
      return new MatlabTypeConverter.ArrayInfo(realValues, imaginaryValues, lengths);
    }
  }
  
  private static class ArrayInfo implements Serializable
  {
    private final double[] real;
    private final double[] imaginary;
    private final int[] lengths;
    
    public ArrayInfo(double[] real, double[] imaginary, int[] lengths) {
      this.real = real;
      this.imaginary = imaginary;
      this.lengths = lengths;
    }
  }
  






  public void setNumericArray(String arrayName, MatlabNumericArray array)
    throws MatlabInvocationException
  {
    _proxy.invokeAndWait(new SetArrayCallable(arrayName, array, null));
  }
  
  private static class SetArrayCallable implements MatlabProxy.MatlabThreadCallable<Object>, Serializable
  {
    private final String _arrayName;
    private final double[] _realArray;
    private final double[] _imaginaryArray;
    private final int[] _lengths;
    
    private SetArrayCallable(String arrayName, MatlabNumericArray array) {
      _arrayName = arrayName;
      _realArray = array.getRealLinearArray();
      _imaginaryArray = array.getImaginaryLinearArray();
      _lengths = array.getLengths();
    }
    

    public Object call(MatlabProxy.MatlabThreadProxy proxy)
      throws MatlabInvocationException
    {
      String realArray = (String)proxy.returningEval("genvarname('" + _arrayName + "_real', who);", 1)[0];
      proxy.setVariable(realArray, _realArray);
      

      String imagArray = null;
      if (_imaginaryArray != null)
      {
        imagArray = (String)proxy.returningEval("genvarname('" + _arrayName + "_imag', who);", 1)[0];
        proxy.setVariable(imagArray, _imaginaryArray);
      }
      




      String evalStatement = _arrayName + " = reshape(" + realArray;
      if (_imaginaryArray != null)
      {
        evalStatement = evalStatement + " + " + imagArray + " * i";
      }
      for (int length : _lengths)
      {
        evalStatement = evalStatement + ", " + length;
      }
      evalStatement = evalStatement + ");";
      proxy.eval(evalStatement);
      

      proxy.eval("clear " + realArray + ";");
      proxy.eval("clear " + imagArray + ";");
      
      return null;
    }
  }
  







  public String toString()
  {
    return "[" + getClass().getName() + " proxy=" + _proxy + "]";
  }
}
