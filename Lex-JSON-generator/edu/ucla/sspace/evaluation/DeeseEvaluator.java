package edu.ucla.sspace.evaluation;

import edu.ucla.sspace.common.SemanticSpace;

public class DeeseEvaluator {
  public DeeseEvaluator() {}
  
  public static void main(String[] args) throws Exception {
    DeeseAntonymEvaluation evaluator = new DeeseAntonymEvaluation();
    String[] arrayOfString = args;int j = args.length; for (int i = 0; i < j; i++) { String file = arrayOfString[i];
      SemanticSpace sspace = edu.ucla.sspace.common.SemanticSpaceIO.load(file);
      WordAssociationReport report = evaluator.evaluate(sspace);
      System.out.printf("%s: %.3f\n", new Object[] { file, Double.valueOf(report.correlation()) });
    }
  }
}
