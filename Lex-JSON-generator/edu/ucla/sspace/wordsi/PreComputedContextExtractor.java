package edu.ucla.sspace.wordsi;

import edu.ucla.sspace.basis.BasisMapping;
import edu.ucla.sspace.basis.StringBasisMapping;
import edu.ucla.sspace.vector.CompactSparseVector;
import edu.ucla.sspace.vector.SparseDoubleVector;
import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;

























public class PreComputedContextExtractor
  implements ContextExtractor
{
  private final BasisMapping<String, String> basis;
  
  public PreComputedContextExtractor()
  {
    this(new StringBasisMapping());
  }
  



  public PreComputedContextExtractor(BasisMapping<String, String> basis)
  {
    this.basis = basis;
  }
  



  public void processDocument(BufferedReader document, Wordsi wordsi)
  {
    try
    {
      line = document.readLine();
    } catch (IOException ioe) { String line;
      throw new IOError(ioe);
    }
    
    String line;
    
    String[] headerRest = line.split("\\s+", 2);
    

    if (!wordsi.acceptWord(headerRest[0])) {
      return;
    }
    SparseDoubleVector context = new CompactSparseVector();
    


    for (String item : headerRest[1].split("\\|")) {
      String[] featureScore = item.split(",", 2);
      double score = Double.parseDouble(featureScore[0]);
      int dimension = basis.getDimension(featureScore[1]);
      if (dimension >= 0)
        context.set(dimension, score);
    }
    wordsi.handleContextVector(headerRest[0], headerRest[0], context);
  }
  


  public int getVectorLength()
  {
    return basis.numDimensions();
  }
}
