package edu.ucla.sspace.tools;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.text.DocumentPreprocessor;
import edu.ucla.sspace.text.IteratorFactory;
import edu.ucla.sspace.text.StringUtils;
import edu.ucla.sspace.util.LoggerUtil;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;











































public class WikipediaCleaner
{
  public static enum CleanerOption
  {
    INCLUDE_TITLES,  INCLUDE_CAPTIONS,  INCLUDE_LINK_TEXT, 
    FILTER_TOKENS,  USE_PREPROCESSOR;
  }
  

  private static final Logger LOGGER = Logger.getLogger(WikipediaCleaner.class.getName());
  



  private PrintWriter processedArticleWriter;
  



  private final Set<CleanerOption> options;
  



  private final int minTokensPerArticle;
  




  public WikipediaCleaner(String outputFile, Set<CleanerOption> options, int minTokensPerArticle)
  {
    this.options = options;
    this.minTokensPerArticle = minTokensPerArticle;
    try {
      processedArticleWriter = new PrintWriter(
        new BufferedOutputStream(new FileOutputStream(outputFile)));
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  








  public void processDocument(WikiDoc doc)
  {
    String rawArticleName = name;
    String articleName = StringUtils.unescapeHTML(rawArticleName);
    articleName = articleName.trim().toLowerCase();
    


    if (!isArticleLink(articleName)) {
      LOGGER.fine("skipping non-article document: " + articleName);
      return;
    }
    

    if ((articleName.indexOf("#REDIRECT") >= 0) || 
      (text.indexOf("#REDIRECT") >= 0)) {
      LOGGER.fine("skipping redirect: " + articleName);
      return;
    }
    LOGGER.log(Level.FINE, "Procesing article {0} with {1} characters", 
      new Object[] { articleName, Integer.valueOf(text.length()) });
    

    StringBuilder rawArticleText = text;
    

    LOGGER.finer("extracting raw article text");
    extractArticle(rawArticleText);
    

    LOGGER.finer("removing tables");
    removeTables(rawArticleText);
    



    LOGGER.finer("removing {{text}} from article");
    removeDoubleBraceMarkup(rawArticleText);
    

    LOGGER.finer("removing [[wiki-link]] from article");
    removeWikiLinkMarkup(rawArticleText, articleName);
    


    LOGGER.finer("removing [external-link] from article");
    removeExternalLinkMarkup(rawArticleText);
    


    LOGGER.finer("unescaping HTML");
    StringUtils.unescapeHTML(rawArticleText);
    


    LOGGER.finer("removing HTML comments");
    removeHtmlComments(rawArticleText);
    
    String article = rawArticleText.toString();
    

    if (options.contains(CleanerOption.USE_PREPROCESSOR)) {
      LOGGER.finer("applying preprocessor");
      article = new DocumentPreprocessor().process(article);
    }
    if (options.contains(CleanerOption.FILTER_TOKENS)) {
      LOGGER.finer("filtering tokens");
      article = filterTokens(article);
    }
    


    int finalTokenCount = getTokenCount(article);
    if (finalTokenCount < minTokensPerArticle) {
      LOGGER.log(Level.FINE, "Document {0} contained only {1} tokens and was not printed", 
        new Object[] {
        articleName, Integer.valueOf(finalTokenCount) });
      return;
    }
    
    if (options.contains(CleanerOption.INCLUDE_TITLES)) {
      processedArticleWriter.print(articleName);
      processedArticleWriter.print(" ");
    }
    

    processedArticleWriter.println(article);
    processedArticleWriter.flush();
  }
  








  private void extractArticle(StringBuilder article)
  {
    int startOfTextTag = article.indexOf("<text");
    int endOfStart = article.indexOf(">", startOfTextTag);
    int closingTextTag = article.indexOf("</text");
    


    if (closingTextTag >= 0) {
      article.delete(closingTextTag, article.length());
    }
    article.delete(0, endOfStart + 1);
  }
  



  private String filterTokens(String article)
  {
    Iterator<String> filteredTokens = IteratorFactory.tokenize(article);
    StringBuilder sb = new StringBuilder(article.length());
    while (filteredTokens.hasNext()) {
      sb.append((String)filteredTokens.next());
      if (filteredTokens.hasNext())
        sb.append(" ");
    }
    return sb.toString();
  }
  





  private void removeDoubleBraceMarkup(StringBuilder article)
  {
    int braceStart = article.indexOf("{{");
    
    while (braceStart >= 0)
    {


      int braceEnd = article.indexOf("}}", braceStart);
      
      int nextBraceStart = article.indexOf("{{", braceStart + 1);
      


      while ((nextBraceStart > braceStart) && (nextBraceStart < braceEnd)) {
        removeEmbeddedBrace(article, nextBraceStart);
        

        braceEnd = article.indexOf("}}", braceStart);
        nextBraceStart = article.indexOf("{{", braceStart + 1);
      }
      
      if (braceEnd < 0) {
        break;
      }
      article.delete(braceStart, braceEnd + 2);
      
      braceStart = article.indexOf("{{", braceStart);
    }
  }
  



  private void removeEmbeddedBrace(StringBuilder article, int startOffset)
  {
    int braceStart = startOffset;
    


    int braceEnd = article.indexOf("}}", braceStart);
    int nextBraceStart = article.indexOf("{{", braceStart + 1);
    while ((nextBraceStart > braceStart) && (nextBraceStart < braceEnd)) {
      removeEmbeddedBrace(article, nextBraceStart);
      

      braceEnd = article.indexOf("}}", braceStart);
      nextBraceStart = article.indexOf("{{", braceStart + 1);
    }
    
    if (braceEnd < 0) {
      return;
    }
    article.delete(braceStart, braceEnd + 2);
  }
  




  private void removeTables(StringBuilder article)
  {
    int tableStart = article.indexOf("{|");
    
    while (tableStart >= 0)
    {


      int tableEnd = article.indexOf("|}", tableStart);
      if (tableEnd <= tableStart) break;
      article.delete(tableStart, tableEnd + 2);
      


      tableStart = article.indexOf("{|", tableStart);
    }
  }
  




  private void removeHtmlComments(StringBuilder article)
  {
    int htmlCommentStart = article.indexOf("<!--");
    

    while (htmlCommentStart >= 0)
    {


      int htmlCommentEnd = article.indexOf("-->", htmlCommentStart);
      if (htmlCommentEnd <= htmlCommentStart) break;
      article.delete(htmlCommentStart, htmlCommentEnd + 3);
      


      htmlCommentStart = article.indexOf("<!--", htmlCommentStart);
    }
  }
  







  public void removeWikiLinkMarkup(StringBuilder article, String title)
  {
    int bracketStart = article.indexOf("[[");
    boolean includeLinkText = 
      options.contains(CleanerOption.INCLUDE_LINK_TEXT);
    label133: while (bracketStart >= 0)
    {


      int bracketEnd = article.indexOf("]]", bracketStart);
      

      if (bracketEnd < 0) {
        break;
      }
      



      if (includeLinkText)
      {
        if (isArticleLink(article.substring(bracketStart + 2, bracketEnd), title))
        {


          int optionalLinkDescriptionStart = 
            article.indexOf("|", bracketStart);
          

          int linkTextStart = 
            (optionalLinkDescriptionStart >= 0) && 
            (optionalLinkDescriptionStart < bracketEnd) ? 
            optionalLinkDescriptionStart + 1 : 
            bracketStart + 2;
          
          String linkText = article.substring(linkTextStart, bracketEnd);
          
          article.replace(bracketStart, bracketEnd + 2, linkText);
          
          break label133;
        }
      }
      article.delete(bracketStart, bracketEnd + 2);
      
      bracketStart = article.indexOf("[[", bracketStart);
    }
  }
  





  public void removeExternalLinkMarkup(StringBuilder article)
  {
    int bracketStart = article.indexOf("[");
    boolean includeLinkText = 
      options.contains(CleanerOption.INCLUDE_LINK_TEXT);
    while (bracketStart >= 0) {
      int bracketEnd = article.indexOf("]", bracketStart);
      

      if (bracketEnd < 0) {
        break;
      }
      

      if (includeLinkText)
      {

        int optionalLinkDescriptionStart = 
          article.indexOf(" ", bracketStart);
        

        int linkTextStart = 
          (optionalLinkDescriptionStart >= 0) && 
          (optionalLinkDescriptionStart < bracketEnd) ? 
          optionalLinkDescriptionStart : 
          bracketStart + 1;
        
        String linkText = article.substring(linkTextStart, bracketEnd);
        
        article.replace(bracketStart, bracketEnd + 1, linkText);

      }
      else
      {
        article.delete(bracketStart, bracketEnd + 1);
      }
      bracketStart = article.indexOf("[", bracketStart);
    }
  }
  


  private int getTokenCount(String article)
  {
    Pattern notWhiteSpace = Pattern.compile("\\S+");
    Matcher matcher = notWhiteSpace.matcher(article);
    int tokens = 0;
    while (matcher.find())
      tokens++;
    return tokens;
  }
  
  public static void main(String[] args) {
    ArgOptions options = new ArgOptions();
    options.addOption('t', "includeTitles", 
      "Prints article and section titles as a part of the document", 
      
      false, null, "Document Processing");
    options.addOption('c', "includeCaptions", 
      "Prints image and table captions as a part of the document", 
      
      false, null, "Document Processing");
    options.addOption('w', "includeLinkText", 
      "Prints text in the Wikipedia links as a part of the document", 
      
      false, null, "Document Processing");
    options.addOption('F', "tokenFilter", 
      "Specifies a filter to remove or retain certain tokens", 
      
      true, "FILTER_SPEC", "Filtering");
    options.addOption('M', "minTokens", 
      "Records only those documents with at least the minimum number of tokens", 
      
      true, "INT", "Filtering");
    options.addOption('P', "applyPreprocessor", 
      "Applies the DocumentPreprocessor to the documents", 
      false, null, "Filtering");
    options.addOption('v', "verbose", 
      "Print verbose output about article cleaning", 
      false, null, "Optional");
    options.addOption('V', "veryVerbose", 
      "Print lots of verbose output about article cleaning", 
      false, null, "Optional");
    

    options.parseOptions(args);
    
    if (options.numPositionalArgs() != 2) {
      System.out.println("usage java [OPTIONS] <wikifile> <output-file>\n" + 
        options.prettyPrint());
      return;
    }
    




    Level logLevel = null;
    if (options.hasOption("verbose")) {
      logLevel = Level.FINE;
    } else if (options.hasOption("veryVerbose"))
      logLevel = Level.FINER;
    if (logLevel != null) {
      LoggerUtil.setLevel(logLevel);
    }
    
    Set<CleanerOption> cleanerOptions = EnumSet.noneOf(CleanerOption.class);
    if (options.hasOption("includeTitles"))
      cleanerOptions.add(CleanerOption.INCLUDE_TITLES);
    if (options.hasOption("includeCaptions"))
      cleanerOptions.add(CleanerOption.INCLUDE_CAPTIONS);
    if (options.hasOption("includeLinkText"))
      cleanerOptions.add(CleanerOption.INCLUDE_LINK_TEXT);
    if (options.hasOption("tokenFilter"))
    {
      Properties props = new Properties();
      props.setProperty("edu.ucla.sspace.text.TokenizerFactory.tokenFilter", 
        options.getStringOption("tokenFilter"));
      IteratorFactory.setProperties(props);
      cleanerOptions.add(CleanerOption.FILTER_TOKENS);
    }
    if (options.hasOption("applyPreprocessor")) {
      cleanerOptions.add(CleanerOption.USE_PREPROCESSOR);
    }
    int minTokens = options.hasOption("minTokens") ? 
      options.getIntOption("minTokens") : 
      0;
    try
    {
      DocumentBufferedQueue docQueue = 
        new DocumentBufferedQueue(options.getPositionalArg(0));
      
      String outFileName = options.getPositionalArg(1);
      WikipediaCleaner cleaner = 
        new WikipediaCleaner(outFileName, cleanerOptions, minTokens);
      
      while (docQueue.hasNext()) {
        cleaner.processDocument(docQueue.next());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  






  private static class DocumentBufferedQueue
  {
    private static final int DOCS_TO_CACHE = 100;
    




    private static final int TITLE_HTML_LENGTH = "    <title>".length();
    



    private final BufferedReader wikiReader;
    



    private final BlockingQueue<WikipediaCleaner.WikiDoc> cachedDocs;
    



    private final AtomicBoolean isReaderOpen;
    




    public DocumentBufferedQueue(String wikipediaFile)
      throws IOException
    {
      wikiReader = new BufferedReader(new FileReader(wikipediaFile));
      cachedDocs = new LinkedBlockingQueue();
      isReaderOpen = new AtomicBoolean(true);
      
      for (int i = 0; i < 100; i++) {
        WikipediaCleaner.WikiDoc d = cacheDoc();
        if (d != null) {
          cachedDocs.offer(d);
        }
      }
    }
    

    private synchronized WikipediaCleaner.WikiDoc cacheDoc()
      throws IOException
    {
      StringBuilder sb = new StringBuilder();
      String articleTitle = null;
      
      for (String line = null; (line = wikiReader.readLine()) != null;)
      {
        if (line.startsWith("</mediawiki>"))
        {
          isReaderOpen.set(false);
        } else if (line.startsWith("  <page>")) {
          try
          {
            String titleLine = wikiReader.readLine();
            

            String rem = titleLine.substring(TITLE_HTML_LENGTH);
            
            int index = rem.indexOf("<");
            if (index < 0) {
              throw new Error("Malformed title: " + line);
            }
            articleTitle = rem.substring(0, index);
            

            while (((line = wikiReader.readLine()) != null) && 
              (!line.startsWith("  </page>")))
            {



              sb.append(line).append(" ");
            }
            
            return new WikipediaCleaner.WikiDoc(articleTitle, sb);
          } catch (Throwable t) {
            t.printStackTrace();
          }
        }
      }
      
      return null;
    }
    


    public boolean hasNext()
    {
      return (cachedDocs.size() > 0) || (isReaderOpen.get());
    }
    













    public WikipediaCleaner.WikiDoc next()
      throws InterruptedException
    {
      new Thread()
      {
        public void run()
        {
          try
          {
            WikipediaCleaner.WikiDoc d = WikipediaCleaner.DocumentBufferedQueue.this.cacheDoc();
            if (d != null)
              cachedDocs.offer(d);
          } catch (IOException ioe) {
            ioe.printStackTrace();
          }
          
        }
      }.start();
      return (WikipediaCleaner.WikiDoc)cachedDocs.poll(600000L, TimeUnit.MILLISECONDS);
    }
  }
  




  private static class WikiDoc
  {
    public final String name;
    



    public final StringBuilder text;
    




    public WikiDoc(String name, StringBuilder text)
    {
      this.name = name;
      this.text = text;
    }
  }
  




  private static boolean isArticleLink(String linkedArticleTitle)
  {
    String s = linkedArticleTitle.toLowerCase();
    return (!s.startsWith("image:")) && 
      (!s.startsWith("wikipedia:")) && 
      (!s.startsWith("template:")) && 
      (!s.startsWith("category:")) && 
      (!s.startsWith("portal:")) && 
      (!s.contains("(disambiguation)"));
  }
  








  private static boolean isArticleLink(String linkedArticleTitle, String linkingArticleTitle)
  {
    if (isArticleLink(linkedArticleTitle)) {
      int colonIndex = linkedArticleTitle.indexOf(":");
      if (colonIndex >= 0)
      {
        if (Pattern.matches("[a-z]*", linkedArticleTitle.substring(0, colonIndex)))
          return false;
      }
      return !linkedArticleTitle.endsWith(":" + linkingArticleTitle);
    }
    return false;
  }
}
