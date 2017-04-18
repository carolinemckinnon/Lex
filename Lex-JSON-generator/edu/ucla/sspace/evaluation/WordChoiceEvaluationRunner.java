package edu.ucla.sspace.evaluation;

import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.common.Similarity.SimType;
import edu.ucla.sspace.vector.Vector;
import java.util.Collection;














































public class WordChoiceEvaluationRunner
{
  public WordChoiceEvaluationRunner() {}
  
  public static WordChoiceReport evaluate(SemanticSpace sspace, WordChoiceEvaluation test, Similarity.SimType vectorComparisonType)
  {
    Collection<MultipleChoiceQuestion> questions = test.getQuestions();
    int correct = 0;
    int unanswerable = 0;
    



    for (MultipleChoiceQuestion question : questions) {
      String promptWord = question.getPrompt();
      

      Vector promptVector = sspace.getVector(promptWord);
      

      if (promptVector == null) {
        unanswerable++;
      }
      else
      {
        int answerIndex = 0;
        double closestOption = Double.MIN_VALUE;
        




        int optionIndex = 0;
        for (String optionWord : question.getOptions())
        {

          Vector optionVector = sspace.getVector(optionWord);
          

          if (optionVector == null) {
            unanswerable++;
            break;
          }
          
          double similarity = Similarity.getSimilarity(
            vectorComparisonType, 
            promptVector, optionVector);
          
          if (similarity > closestOption) {
            answerIndex = optionIndex;
            closestOption = similarity;
          }
          optionIndex++;
        }
        

        if (answerIndex == question.getCorrectAnswer()) {
          correct++;
        }
      }
    }
    return new SimpleReport(questions.size(), correct, unanswerable);
  }
  





  private static class SimpleReport
    implements WordChoiceReport
  {
    private final int numQuestions;
    



    private final int correct;
    



    private final int unanswerable;
    




    public SimpleReport(int numQuestions, int correct, int unanswerable)
    {
      this.numQuestions = numQuestions;
      this.correct = correct;
      this.unanswerable = unanswerable;
    }
    


    public int numberOfQuestions()
    {
      return numQuestions;
    }
    


    public int correctAnswers()
    {
      return correct;
    }
    


    public int unanswerableQuestions()
    {
      return unanswerable;
    }
    


    public double score()
    {
      return unanswerable == numQuestions ? 
        0.0D : 
        100.0D * correct / (numQuestions - unanswerable);
    }
    



    public String toString()
    {
      return String.format("%.2f correct; %d/%d unanswered", new Object[] {
        Double.valueOf(score()), 
        Integer.valueOf(unanswerable), Integer.valueOf(numQuestions) });
    }
  }
}
