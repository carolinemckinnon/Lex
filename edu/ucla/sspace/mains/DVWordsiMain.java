package edu.ucla.sspace.mains;

import edu.ucla.sspace.common.ArgOptions;
import edu.ucla.sspace.common.SemanticSpaceIO.SSpaceFormat;
import edu.ucla.sspace.dependency.CoNLLDependencyExtractor;
import edu.ucla.sspace.dependency.DependencyPathAcceptor;
import edu.ucla.sspace.dependency.DependencyPathWeight;
import edu.ucla.sspace.dependency.FlatPathWeight;
import edu.ucla.sspace.dependency.UniversalPathAcceptor;
import edu.ucla.sspace.dv.DependencyPathBasisMapping;
import edu.ucla.sspace.dv.WordBasedBasisMapping;
import edu.ucla.sspace.text.DependencyFileDocumentIterator;
import edu.ucla.sspace.text.Document;
import edu.ucla.sspace.util.ReflectionUtil;
import edu.ucla.sspace.wordsi.ContextExtractor;
import edu.ucla.sspace.wordsi.DependencyContextExtractor;
import edu.ucla.sspace.wordsi.DependencyContextGenerator;
import edu.ucla.sspace.wordsi.WordOccrrenceDependencyContextGenerator;
import edu.ucla.sspace.wordsi.psd.PseudoWordDependencyContextExtractor;
import edu.ucla.sspace.wordsi.semeval.SemEvalDependencyContextExtractor;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;























































public class DVWordsiMain
  extends GenericWordsiMain
{
  private DependencyPathBasisMapping basis;
  
  public DVWordsiMain() {}
  
  public static void main(String[] args)
    throws Exception
  {
    DVWordsiMain main = new DVWordsiMain();
    main.run(args);
  }
  


  protected void addExtraOptions(ArgOptions options)
  {
    super.addExtraOptions(options);
    
    options.removeOption('f');
    options.addOption('p', "pathAcceptor", 
      "Specifies the DependencyPathAcceptor to use when validating paths as features. (Default: Universal)", 
      
      true, "CLASSNAME", "Optional");
    options.addOption('G', "weightingFunction", 
      "Specifies the class that will weight dependency paths. (Default: None)", 
      
      true, "CLASSNAME", "Optional");
    options.addOption('B', "basisMapping", 
      "Specifies the class that deterine what aspect of a DependencyPath will as a feature in the word space. (Default: WordBasedBasisMapping)", 
      

      true, "CLASSNAME", "Optional");
  }
  




  protected void handleExtraOptions()
  {
    if (argOptions.hasOption('L')) {
      basis = ((DependencyPathBasisMapping)loadObject(openLoadFile()));
      basis.setReadOnly(true);
    } else if (argOptions.hasOption('B')) {
      basis = ((DependencyPathBasisMapping)ReflectionUtil.getObjectInstance(
        argOptions.getStringOption('B')));
    } else {
      basis = new WordBasedBasisMapping();
    }
  }
  

  protected void postProcessing()
  {
    if (argOptions.hasOption('S'))
      saveObject(openSaveFile(), basis);
  }
  
  protected DependencyPathWeight getWeighter() {
    DependencyPathWeight weight;
    DependencyPathWeight weight;
    if (argOptions.hasOption('G')) {
      weight = (DependencyPathWeight)ReflectionUtil.getObjectInstance(
        argOptions.getStringOption('G'));
    } else
      weight = new FlatPathWeight();
    return weight;
  }
  
  protected DependencyPathAcceptor getAcceptor() {
    DependencyPathAcceptor acceptor;
    DependencyPathAcceptor acceptor;
    if (argOptions.hasOption('p')) {
      acceptor = (DependencyPathAcceptor)ReflectionUtil.getObjectInstance(
        argOptions.getStringOption('p'));
    } else
      acceptor = new UniversalPathAcceptor();
    return acceptor;
  }
  
  protected DependencyContextGenerator getContextGenerator() {
    return new WordOccrrenceDependencyContextGenerator(
      basis, getWeighter(), getAcceptor(), windowSize());
  }
  


  protected ContextExtractor getExtractor()
  {
    DependencyContextGenerator generator = 
      getContextGenerator();
    

    if (argOptions.hasOption('e')) {
      generator.setReadOnly(true);
    }
    

    if (argOptions.hasOption('E')) {
      return new SemEvalDependencyContextExtractor(
        new CoNLLDependencyExtractor(), generator);
    }
    

    if (argOptions.hasOption('P')) {
      return new PseudoWordDependencyContextExtractor(
        new CoNLLDependencyExtractor(), 
        generator, getPseudoWordMap());
    }
    
    return new DependencyContextExtractor(
      new CoNLLDependencyExtractor(), generator, 
      argOptions.hasOption('h'));
  }
  


  protected SemanticSpaceIO.SSpaceFormat getSpaceFormat()
  {
    return SemanticSpaceIO.SSpaceFormat.SPARSE_BINARY;
  }
  


  protected void addFileIterators(Collection<Iterator<Document>> docIters, String[] fileNames)
    throws IOException
  {
    throw new UnsupportedOperationException(
      "A file based document iterator does not exist");
  }
  



  protected void addDocIterators(Collection<Iterator<Document>> docIters, String[] fileNames)
    throws IOException
  {
    for (String s : fileNames) {
      docIters.add(new DependencyFileDocumentIterator(s));
    }
  }
}
