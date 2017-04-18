package edu.ucla.sspace.tools;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.text.DocumentPreprocessor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Logger;








































public class BlogPreProcessor
{
  private static final Logger LOGGER = Logger.getLogger(BlogPreProcessor.class.getName());
  private DocumentPreprocessor processor;
  private final PrintWriter pw;
  private boolean saveTS;
  private long beginTime;
  private long endTime;
  
  private BlogPreProcessor(File wordFile, File outFile, long begin, long end)
  {
    PrintWriter writer = null;
    beginTime = begin;
    endTime = end;
    try {
      writer = new PrintWriter(outFile);
      processor = new DocumentPreprocessor(wordFile);
    } catch (FileNotFoundException fnee) {
      fnee.printStackTrace();
      System.exit(1);
    } catch (IOException ioe) {
      ioe.printStackTrace();
      System.exit(1);
    }
    pw = writer;
  }
  


  public void processFile(File blogFile)
    throws IOException
  {
    BufferedReader br = new BufferedReader(new FileReader(blogFile));
    String line = null;
    String date = null;
    String id = null;
    StringBuilder content = new StringBuilder();
    boolean needMoreContent = false;
    while ((line = br.readLine()) != null)
      if (line.contains("<id>")) {
        int startIndex = line.indexOf(">") + 1;
        int endIndex = line.lastIndexOf("<");
        id = line.substring(startIndex, endIndex);
      } else if (line.contains("<content>"))
      {


        int startIndex = line.indexOf(">") + 1;
        int endIndex = line.lastIndexOf("<");
        content = new StringBuilder();
        if (endIndex > startIndex) {
          content.append(line.substring(startIndex, endIndex));
        } else {
          content.append(line.substring(startIndex));
          needMoreContent = true;
        }
      } else if (needMoreContent)
      {

        int endIndex = line.contains("</content>") ? line.lastIndexOf("<") : -1;
        if (endIndex > 0) {
          content.append(line.substring(0, endIndex));
          needMoreContent = false;
        } else {
          content.append(line);
        } } else if (line.contains("<updated>"))
      {
        int startIndex = line.indexOf(">") + 1;
        int endIndex = line.lastIndexOf("<");
        date = line.substring(startIndex, endIndex);
        if (date.equals(""))
          date = null;
      } else if ((content != null) && (date != null))
      {
        long dateTime = Timestamp.valueOf(date).getTime();
        if ((dateTime < beginTime) || (dateTime > endTime)) {
          needMoreContent = false;
          date = null;
        }
        else {
          String cleanedContent = processor.process(content.toString());
          if (!cleanedContent.equals("")) {
            synchronized (pw) {
              pw.format("%d %s\n", new Object[] { Long.valueOf(dateTime), cleanedContent });
              pw.flush();
            }
          }
          LOGGER.info(String.format("Processed blog %s with timestamp %d", new Object[] {
            id, Long.valueOf(dateTime) }));
          needMoreContent = false;
          date = null;
        }
      }
    br.close();
  }
  
  public static ArgOptions setupOptions() {
    ArgOptions opts = new ArgOptions();
    opts.addOption('d', "docFiles", "location of directory containing only blog files", 
      true, "FILE[,FILE,...]", "Required");
    opts.addOption('w', "wordlist", "Word List for cleaning documents", 
      true, "STRING", "Required");
    opts.addOption('s', "beginTime", "Earliest timestamp for any document", 
      true, "INTEGER", "Optional");
    opts.addOption('e', "endTime", "Latest timestamp for any document", 
      true, "INTEGER", "Optional");
    opts.addOption('h', "threads", "number of threads", true, "INT");
    return opts;
  }
  
  public static void main(String[] args) throws IOException, InterruptedException
  {
    ArgOptions options = setupOptions();
    options.parseOptions(args);
    
    if ((!options.hasOption("docFiles")) || 
      (!options.hasOption("wordlist")) || 
      (options.numPositionalArgs() != 1)) {
      System.out.println("usage: java BlogPreProcessor [options] <out_file> \n" + 
        options.prettyPrint());
      System.exit(1);
    }
    
    File outFile = new File(options.getPositionalArg(0));
    File wordFile = new File(options.getStringOption("wordlist"));
    

    long startTime = options.hasOption("beginTime") ? 
      options.getLongOption("beginTime") : 0L;
    long endTime = options.hasOption("endTime") ? 
      options.getLongOption("endTime") : Long.MAX_VALUE;
    
    final BlogPreProcessor blogCleaner = 
      new BlogPreProcessor(wordFile, outFile, startTime, endTime);
    String[] fileNames = options.getStringOption("docFiles").split(",");
    

    int numThreads = Runtime.getRuntime().availableProcessors();
    if (options.hasOption("threads")) {
      numThreads = options.getIntOption("threads");
    }
    Collection<File> blogFiles = new ArrayDeque();
    for (String fileName : fileNames) {
      blogFiles.add(new File(fileName));
    }
    
    Iterator<File> fileIter = blogFiles.iterator();
    
    Object threads = new LinkedList();
    
    for (int i = 0; i < numThreads; i++) {
      t = new Thread() {
        public void run() {
          while (hasNext()) {
            File currentFile = (File)next();
            try {
              BlogPreProcessor.LOGGER.info("processing: " + currentFile.getPath());
              blogCleaner.processFile(currentFile);
            } catch (IOException ioe) {
              ioe.printStackTrace();
            }
          }
        }
      };
      ((Collection)threads).add(t);
    }
    
    for (Object t = ((Collection)threads).iterator(); ((Iterator)t).hasNext();) { Thread t = (Thread)((Iterator)t).next();
      t.start(); }
    for (t = ((Collection)threads).iterator(); ((Iterator)t).hasNext();) { Thread t = (Thread)((Iterator)t).next();
      t.join();
    }
  }
}
