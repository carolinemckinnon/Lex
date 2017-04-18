package edu.ucla.sspace.mains;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.svs.StructuredVectorSpace;
import edu.ucla.sspace.util.SerializableUtil;
import edu.ucla.sspace.wordsi.DependencyContextGenerator;
import edu.ucla.sspace.wordsi.SelPrefDependencyContextGenerator;
import java.io.File;







































public class SelPrefWordsiMain
  extends DVWordsiMain
{
  public SelPrefWordsiMain() {}
  
  public static void main(String[] args)
    throws Exception
  {
    SelPrefWordsiMain main = new SelPrefWordsiMain();
    main.run(args);
  }
  


  protected void addExtraOptions(ArgOptions options)
  {
    super.addExtraOptions(options);
    
    options.addOption('l', "selecitonalPreferenceSpace", 
      "A serialized SelecitonalPreference SemanticSpace", 
      true, "FILE", "Required");
  }
  


  protected DependencyContextGenerator getContextGenerator()
  {
    StructuredVectorSpace svs = (StructuredVectorSpace)SerializableUtil.load(new File(
      argOptions.getStringOption('l')));
    return new SelPrefDependencyContextGenerator(svs);
  }
}
