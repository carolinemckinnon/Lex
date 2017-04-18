package edu.ucla.sspace.mains;

import edu.ucla.sspace.basis.BasisMapping;
import edu.ucla.sspace.basis.StringBasisMapping;
import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.wordsi.DependencyContextGenerator;
import edu.ucla.sspace.wordsi.OccurrenceDependencyContextGenerator;
import edu.ucla.sspace.wordsi.OrderingDependencyContextGenerator;
import edu.ucla.sspace.wordsi.PartOfSpeechDependencyContextGenerator;



























































public class DVWCWordsiMain
  extends DVWordsiMain
{
  private BasisMapping<String, String> basis;
  
  public DVWCWordsiMain() {}
  
  public static void main(String[] args)
    throws Exception
  {
    DVWCWordsiMain main = new DVWCWordsiMain();
    main.run(args);
  }
  


  protected void addExtraOptions(ArgOptions options)
  {
    super.addExtraOptions(options);
    
    options.addOption('H', "usePartsOfSpeech", 
      "If provided, parts of speech will be used as part of the word occurrence features.", 
      
      false, null, "Optional");
    options.addOption('O', "useWordOrdering", 
      "If provided, parts of speech will be used as part of the word occurrence features.", 
      
      false, null, "Optional");
  }
  



  protected void handleExtraOptions()
  {
    if (argOptions.hasOption('L')) {
      basis = ((BasisMapping)loadObject(openLoadFile()));
      basis.setReadOnly(true);
    } else {
      basis = new StringBasisMapping();
    }
  }
  

  protected void postProcessing()
  {
    if (argOptions.hasOption('S')) {
      saveObject(openSaveFile(), basis);
    }
  }
  

  protected DependencyContextGenerator getContextGenerator()
  {
    if (argOptions.hasOption('H'))
      return new PartOfSpeechDependencyContextGenerator(
        basis, windowSize());
    if (argOptions.hasOption('O'))
      return new OrderingDependencyContextGenerator(
        basis, windowSize());
    return new OccurrenceDependencyContextGenerator(basis, windowSize());
  }
}
