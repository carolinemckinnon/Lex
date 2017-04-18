package edu.ucla.sspace.mains;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.grefenstette.Grefenstette;
import edu.ucla.sspace.text.Document;
import edu.ucla.sspace.text.OneLinePerDocumentIterator;
import edu.ucla.sspace.util.CombinedIterator;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
























































































public class GrefenstetteMain
  extends GenericMain
{
  private Properties props;
  
  private GrefenstetteMain() {}
  
  protected ArgOptions setupOptions()
  {
    ArgOptions options = new ArgOptions();
    options.addOption('s', "sentenceFile", 
      "a file where each line is a sentence", true, 
      "FILE[,FILE...]", "Required (at least one of)");
    
    options.addOption('o', "outputFormat", "the .sspace format to use", 
      true, "{text|binary}", "Program Options");
    

    options.addOption('w', "overwrite", "specifies whether to overwrite the existing output", 
      true, "BOOL", 
      "Program Options");
    options.addOption('t', "threads", "the number of threads to use", 
      true, "INT", "Program Options");
    options.addOption('v', "verbose", "prints verbose output", 
      false, null, "Program Options");
    addExtraOptions(options);
    return options;
  }
  


  protected void addExtraOptions(ArgOptions options) {}
  


  protected Iterator<Document> getDocumentIterator()
    throws IOException
  {
    Iterator<Document> docIter = null;
    
    String sentenceList = argOptions.hasOption("sentenceFile") ? 
      argOptions.getStringOption("sentenceFile") : 
      null;
    
    if (sentenceList == null) {
      throw new Error("must specify sentence file");
    }
    


    Collection<Iterator<Document>> docIters = 
      new LinkedList();
    
    String[] fileNames = sentenceList.split(",");
    

    for (String s : fileNames) {
      docIters.add(new OneLinePerDocumentIterator(s));
    }
    

    docIter = new CombinedIterator(docIters);
    return docIter;
  }
  



  public SemanticSpace getSpace()
  {
    return new Grefenstette();
  }
  
  public static void main(String[] args) {
    try {
      GrefenstetteMain main = new GrefenstetteMain();
      main.run(args);
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }
}
