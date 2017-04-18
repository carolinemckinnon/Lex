package edu.ucla.sspace.tools;

import edu.ucla.sspace.text.DocumentPreprocessor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.PrintWriter;
































public class NsfAbstractCleaner
{
  public NsfAbstractCleaner() {}
  
  public static void main(String[] args)
    throws Exception
  {
    if (args.length != 2) {
      System.out.println(
        "usage: java NsfAbstractCleaner <abstract_dir> <out_file>");
      System.exit(1);
    }
    
    DocumentPreprocessor processor = new DocumentPreprocessor();
    PrintWriter pw = new PrintWriter(args[1]);
    
    File baseAbstractDir = new File(args[0]);
    
    for (File abstractYearDir : baseAbstractDir.listFiles())
    {


      if ((abstractYearDir.isDirectory()) && 
        (abstractYearDir.getName().startsWith("awards")))
      {



        for (File abstractPartDir : abstractYearDir.listFiles())
        {

          if (abstractPartDir.isDirectory())
          {


            for (File awardFile : abstractPartDir.listFiles()) {
              BufferedReader br = 
                new BufferedReader(new FileReader(awardFile));
              StringBuilder sb = new StringBuilder();
              boolean startedContent = false;
              


              for (String line = null; (line = br.readLine()) != null;) {
                if (startedContent)
                  sb.append(line).append(" ");
                if (line.startsWith("Abstract")) {
                  startedContent = true;
                }
              }
              
              sb.append("\n");
              String cleanedContent = processor.process(sb.toString());
              System.out.println(awardFile.getAbsolutePath());
              pw.printf("%s\n", new Object[] { cleanedContent });
              br.close();
            } } }
      }
    }
    pw.close();
  }
}
