package edu.ucla.sspace.dependency;

import java.util.HashMap;
import java.util.Map;












































public class DependencyExtractorManager
{
  private static DependencyExtractor defaultExtractor;
  private static Map<String, DependencyExtractor> nameToExtractor = new HashMap();
  








  private DependencyExtractorManager() {}
  







  public static synchronized void addExtractor(String name, DependencyExtractor extractor)
  {
    addExtractor(name, extractor, defaultExtractor == null);
  }
  















  public static synchronized void addExtractor(String name, DependencyExtractor extractor, boolean isDefault)
  {
    if (extractor == null)
      throw new NullPointerException("Extractor cannot be null" + name);
    if (name == null)
      throw new NullPointerException("Extractor cannot have null name");
    nameToExtractor.put(name, extractor);
    if (isDefault) {
      defaultExtractor = extractor;
    }
  }
  








  public static synchronized DependencyExtractor getExtractor(String name)
  {
    DependencyExtractor e = (DependencyExtractor)nameToExtractor.get(name);
    if (e == null)
      throw new IllegalArgumentException("No extactor with name " + name);
    return e;
  }
  




  public static synchronized DependencyExtractor getDefaultExtractor()
  {
    if (defaultExtractor == null)
      throw new IllegalStateException(
        "No DependencyExtractors available.");
    return defaultExtractor;
  }
}
