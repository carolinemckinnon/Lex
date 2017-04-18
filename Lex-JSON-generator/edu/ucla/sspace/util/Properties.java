package edu.ucla.sspace.util;



















public class Properties
{
  private final java.util.Properties props;
  

















  public Properties()
  {
    this(System.getProperties());
  }
  


  public Properties(java.util.Properties props)
  {
    this.props = props;
  }
  


  public String getProperty(String propName)
  {
    return props.getProperty(propName);
  }
  



  public String getProperty(String propName, String defaultValue)
  {
    return props.getProperty(propName, defaultValue);
  }
  



  public int getProperty(String propName, int defaultValue)
  {
    String propValue = props.getProperty(propName);
    return propValue == null ? defaultValue : Integer.parseInt(propValue);
  }
  



  public double getProperty(String propName, double defaultValue)
  {
    String propValue = props.getProperty(propName);
    return propValue == null ? defaultValue : Double.parseDouble(propValue);
  }
  





  public <T> T getProperty(String propName, T defaultValue)
  {
    String propValue = props.getProperty(propName);
    if (propValue == null)
      return defaultValue;
    return ReflectionUtil.getObjectInstance(propValue);
  }
  




  public static int getInt(java.util.Properties properties, String prop, int defaultVal)
  {
    String propVal = properties.getProperty(prop);
    return propVal != null ? 
      Integer.parseInt(propVal) : 
      defaultVal;
  }
  




  public static long getLong(java.util.Properties properties, String prop, long defaultVal)
  {
    String propVal = properties.getProperty(prop);
    return propVal != null ? 
      Long.parseLong(propVal) : 
      defaultVal;
  }
  





  public static boolean getBoolean(java.util.Properties properties, String prop, boolean defaultVal)
  {
    String propVal = properties.getProperty(prop);
    return propVal != null ? 
      Boolean.parseBoolean(propVal) : 
      defaultVal;
  }
}
