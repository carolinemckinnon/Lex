package matlabcontrol;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;








































class JMIValidator
{
  private JMIValidator() {}
  
  static void validateJMIMethods()
    throws MatlabConnectionException
  {
    Class<?> matlabClass = getAndCheckClass("com.mathworks.jmi.Matlab");
    

    checkMethod(matlabClass, Object.class, "mtFevalConsoleOutput", new Class[] { String.class, [Ljava.lang.Object.class, Integer.TYPE }, new Class[] { Exception.class });
    



    checkMethod(matlabClass, Void.TYPE, "whenMatlabIdle", new Class[] { Runnable.class }, new Class[0]);
    




    Class<?> nativeMatlabClass = getAndCheckClass("com.mathworks.jmi.NativeMatlab");
    

    checkMethod(nativeMatlabClass, Boolean.TYPE, "nativeIsMatlabThread", new Class[0], new Class[0]);
  }
  

  private static Class<?> getAndCheckClass(String className)
    throws MatlabConnectionException
  {
    try
    {
      return Class.forName(className, false, JMIValidator.class.getClassLoader());
    }
    catch (ClassNotFoundException e)
    {
      throw new MatlabConnectionException("This version of MATLAB is missing a class required by matlabcontrol\nRequired: " + className, e);

    }
    catch (SecurityException e)
    {

      throw new MatlabConnectionException("Unable to verify if MATLAB has the method required by matlabcontrol", e);
    }
  }
  













  private static void checkMethod(Class<?> clazz, Class<?> requiredReturn, String methodName, Class<?>[] requiredParameters, Class<?>[] requiredExceptions)
    throws MatlabConnectionException
  {
    try
    {
      Method method = clazz.getDeclaredMethod(methodName, requiredParameters);
      int actualModifiers = method.getModifiers();
      Class<?> actualReturn = method.getReturnType();
      Class<?>[] actualExceptions = method.getExceptionTypes();
      

      boolean exceptionsEqual = doExceptionsMatch(requiredExceptions, actualExceptions);
      
      if ((!Modifier.isPublic(actualModifiers)) || (!Modifier.isStatic(actualModifiers)) || (!actualReturn.equals(requiredReturn)) || (!exceptionsEqual))
      {

        String required = buildMethodDescription(clazz, requiredReturn, methodName, requiredParameters, requiredExceptions);
        
        throw new MatlabConnectionException("This version of MATLAB is missing a method required by matlabcontrol\nRequired: " + required + "\n" + "Found:    " + method.toString());
      }
      

    }
    catch (NoSuchMethodException e)
    {
      String required = buildMethodDescription(clazz, requiredReturn, methodName, requiredParameters, requiredExceptions);
      
      throw new MatlabConnectionException("This version of MATLAB is missing a method required by matlabcontrol\nRequired: " + required);
    }
  }
  








  private static boolean doExceptionsMatch(Class<?>[] requiredExceptions, Class<?>[] actualExceptions)
  {
    HashSet<Class<?>> requiredSet = new HashSet();
    for (Class<?> excClass : requiredExceptions)
    {
      if (!RuntimeException.class.isAssignableFrom(excClass))
      {
        requiredSet.add(excClass);
      }
    }
    
    HashSet<Class<?>> actualSet = new HashSet();
    for (Class<?> excClass : actualExceptions)
    {
      if (!RuntimeException.class.isAssignableFrom(excClass))
      {
        actualSet.add(excClass);
      }
    }
    
    return requiredSet.equals(actualSet);
  }
  
















  private static String buildMethodDescription(Class<?> clazz, Class<?> requiredReturn, String methodName, Class<?>[] requiredParameters, Class<?>[] requiredExceptions)
  {
    String paramString = "";
    for (int i = 0; i < requiredParameters.length; i++)
    {
      paramString = paramString + requiredParameters[i].getCanonicalName();
      
      if (i < requiredParameters.length - 1)
      {
        paramString = paramString + ",";
      }
    }
    
    String throwsString = "";
    if (requiredExceptions.length > 0)
    {
      throwsString = " throws ";
      for (int i = 0; i < requiredExceptions.length; i++)
      {
        throwsString = throwsString + requiredExceptions[i].getCanonicalName();
        
        if (i < requiredExceptions.length - 1)
        {
          throwsString = throwsString + ",";
        }
      }
    }
    
    String desc = "public static " + requiredReturn.getCanonicalName() + " " + clazz.getCanonicalName() + "." + methodName + "(" + paramString + ")" + throwsString;
    

    return desc;
  }
}
