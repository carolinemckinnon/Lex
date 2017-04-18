package edu.ucla.sspace.tools;

import edu.ucla.sspace.text.DocumentPreprocessor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.PrintWriter;

































public class TwentyNewsGroupsCleaner
{
  public TwentyNewsGroupsCleaner() {}
  
  public static void main(String[] args)
    throws Exception
  {
    if (args.length != 2) {
      System.out.println(
        "usage: java TwentyNewsGroupCleaner <ng_dir> <out_file>");
      System.exit(1);
    }
    
    DocumentPreprocessor processor = new DocumentPreprocessor();
    PrintWriter pw = new PrintWriter(args[1]);
    
    File baseNGDir = new File(args[0]);
    
    for (File newsGroupDir : baseNGDir.listFiles())
    {

      if (newsGroupDir.isDirectory())
      {


        for (File newsGroupEntry : newsGroupDir.listFiles()) {
          BufferedReader br = 
            new BufferedReader(new FileReader(newsGroupEntry));
          StringBuilder sb = new StringBuilder();
          boolean startedContent = false;
          


          for (String line = null; (line = br.readLine()) != null;) {
            if (startedContent)
              sb.append(line).append(" ");
            if (line.startsWith("Lines:")) {
              startedContent = true;
            }
          }
          
          sb.append("\n");
          String cleanedContent = processor.process(sb.toString());
          System.out.println(newsGroupEntry.getAbsolutePath());
          pw.printf("%s\n", new Object[] { cleanedContent });
          br.close();
        } }
    }
    pw.close();
  }
}
