package edu.ucla.sspace.util;


















public class ReflectionUtil
{
  private ReflectionUtil() {}
  
















  public static <T> T getObjectInstance(String className)
  {
    try
    {
      Class clazz = Class.forName(className);
      return clazz.newInstance();
    } catch (Exception e) {
      throw new Error(e);
    }
  }
}
