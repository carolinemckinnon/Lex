package edu.ucla.sspace.tools;

import edu.ucla.sspace.text.DocumentPreprocessor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;



















public class OneLineDocumentCleaner
{
  public OneLineDocumentCleaner() {}
  
  public static void main(String[] args)
  {
    try
    {
      if (args.length != 2) {
        usage();
        return;
      }
      DocumentPreprocessor processor = new DocumentPreprocessor();
      BufferedReader br = new BufferedReader(new FileReader(args[0]));
      BufferedWriter bw = new BufferedWriter(new FileWriter(args[1]));
      for (String line = null; (line = br.readLine()) != null;) {
        String cleaned = processor.process(line);
        if (!cleaned.equals("")) {
          bw.write(cleaned);
          bw.newLine();
        }
      }
      bw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  private static void usage() {
    System.out.println(
      "java OneLineDocumentCleaner word-file input-file output-file\n  input-file: a file with one document per line\n  output-file: a file where the cleaned documents will be put");
  }
}
