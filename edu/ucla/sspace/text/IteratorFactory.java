package edu.ucla.sspace.text;

import edu.ucla.sspace.util.FileResourceFinder;
import edu.ucla.sspace.util.LimitedIterator;
import edu.ucla.sspace.util.ReflectionUtil;
import edu.ucla.sspace.util.ResourceFinder;
import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;





















































































































































public class IteratorFactory
{
  public static final String EMPTY_TOKEN = "";
  private static final String PROPERTY_PREFIX = "edu.ucla.sspace.text.TokenizerFactory";
  public static final String TOKEN_FILTER_PROPERTY = "edu.ucla.sspace.text.TokenizerFactory.tokenFilter";
  public static final String STEMMER_PROPERTY = "edu.ucla.sspace.text.TokenizerFactory.stemmer";
  public static final String COMPOUND_TOKENS_FILE_PROPERTY = "edu.ucla.sspace.text.TokenizerFactory.compoundTokens";
  public static final String TOKEN_REPLACEMENT_FILE_PROPERTY = "edu.ucla.sspace.text.TokenizerFactory.replacementTokens";
  public static final String TOKEN_COUNT_LIMIT_PROPERTY = "edu.ucla.sspace.text.TokenizerFactory.tokenCountLimit";
  public static final Set<String> ITERATOR_FACTORY_PROPERTIES = new HashSet();
  private static TokenFilter filter;
  
  static {
    ITERATOR_FACTORY_PROPERTIES.add(
      "edu.ucla.sspace.text.TokenizerFactory.tokenFilter");
    ITERATOR_FACTORY_PROPERTIES.add(
      "edu.ucla.sspace.text.TokenizerFactory.stemmer");
    ITERATOR_FACTORY_PROPERTIES.add(
      "edu.ucla.sspace.text.TokenizerFactory.compoundTokens");
    ITERATOR_FACTORY_PROPERTIES.add(
      "edu.ucla.sspace.text.TokenizerFactory.replacementTokens");
    ITERATOR_FACTORY_PROPERTIES.add(
      "edu.ucla.sspace.text.TokenizerFactory.tokenCountLimit");
  }
  










  private static ResourceFinder resourceFinder = new FileResourceFinder();
  





  private static Stemmer stemmer;
  





  private static int wordLimit;
  




  private static Map<String, String> replacementMap;
  




  private static final Map<Thread, CompoundWordIterator> compoundIterators = new HashMap();
  




  private static Set<String> compoundTokens = null;
  



  private IteratorFactory() {}
  



  public static synchronized void setProperties(Properties props)
  {
    wordLimit = Integer.parseInt(
      props.getProperty("edu.ucla.sspace.text.TokenizerFactory.tokenCountLimit", "0"));
    
    String filterProp = 
      props.getProperty("edu.ucla.sspace.text.TokenizerFactory.tokenFilter");
    filter = filterProp != null ? 
      TokenFilter.loadFromSpecification(filterProp, resourceFinder) : 
      null;
    


    String stemmerProp = props.getProperty("edu.ucla.sspace.text.TokenizerFactory.stemmer");
    if (stemmerProp != null) {
      stemmer = (Stemmer)ReflectionUtil.getObjectInstance(stemmerProp);
    }
    String compoundTokensProp = 
      props.getProperty("edu.ucla.sspace.text.TokenizerFactory.compoundTokens");
    if (compoundTokensProp != null)
    {
      compoundTokens = new LinkedHashSet();
      try {
        BufferedReader br = resourceFinder.open(compoundTokensProp);
        for (String line = null; (line = br.readLine()) != null;) {
          compoundTokens.add(line);
        }
        


        Iterator localIterator = compoundIterators.entrySet().iterator();
        while (localIterator.hasNext()) {
          Map.Entry<Thread, CompoundWordIterator> e = (Map.Entry)localIterator.next();
          

          BufferedReader dummyBuffer = 
            new BufferedReader(new StringReader(""));
          e.setValue(new CompoundWordIterator(
            dummyBuffer, compoundTokens));
        }
      }
      catch (IOException ioe) {
        throw new IOError(ioe);
      }
    }
    else
    {
      compoundTokens = null;
    }
    
    String replacementProp = 
      props.getProperty("edu.ucla.sspace.text.TokenizerFactory.replacementTokens");
    if (replacementProp != null) {
      try {
        BufferedReader br = resourceFinder.open(replacementProp);
        replacementMap = new HashMap();
        String line = null;
        while ((line = br.readLine()) != null) {
          String[] termReplacement = line.split("\\s+");
          replacementMap.put(termReplacement[0], termReplacement[1]);
        }
      } catch (IOException ioe) {
        throw new IOError(ioe);
      }
    } else {
      replacementMap = null;
    }
  }
  








  public static void setResourceFinder(ResourceFinder finder)
  {
    resourceFinder = finder;
  }
  









  public static Iterator<String> tokenize(BufferedReader reader)
  {
    return getBaseIterator(reader, false);
  }
  









  public static Iterator<String> tokenize(String str)
  {
    return tokenize(new BufferedReader(new StringReader(str)));
  }
  














  public static Iterator<String> tokenizeOrdered(BufferedReader reader)
  {
    return getBaseIterator(reader, true);
  }
  














  public static Iterator<String> tokenizeOrdered(String str)
  {
    return tokenizeOrdered(new BufferedReader(new StringReader(str)));
  }
  












  public static Iterator<String> tokenizeOrderedWithReplacement(BufferedReader reader)
  {
    Iterator<String> baseIterator = tokenizeOrdered(reader);
    return replacementMap == null ? 
      baseIterator : 
      new WordReplacementIterator(baseIterator, replacementMap);
  }
  












  private static Iterator<String> getBaseIterator(BufferedReader reader, boolean keepOrdering)
  {
    Iterator<String> finalIterator = new WordIterator(reader);
    

    if (replacementMap != null) {
      finalIterator = 
        new WordReplacementIterator(finalIterator, replacementMap);
    }
    
    if (compoundTokens != null)
    {



      CompoundWordIterator cwi = 
        (CompoundWordIterator)compoundIterators.get(Thread.currentThread());
      if (cwi == null) {
        cwi = new CompoundWordIterator(finalIterator, compoundTokens);
        compoundIterators.put(Thread.currentThread(), cwi);


      }
      else
      {

        cwi.reset(finalIterator);
      }
      finalIterator = cwi;
    }
    

    if (wordLimit > 0) {
      finalIterator = new LimitedIterator(
        finalIterator, wordLimit);
    }
    
    if (filter != null) {
      finalIterator = keepOrdering ? 
        new OrderPreservingFilteredIterator(finalIterator, filter) : 
        new FilteredIterator(finalIterator, filter);
    }
    

    if (stemmer != null) {
      finalIterator = new StemmingIterator(finalIterator, stemmer);
    }
    return finalIterator;
  }
}
