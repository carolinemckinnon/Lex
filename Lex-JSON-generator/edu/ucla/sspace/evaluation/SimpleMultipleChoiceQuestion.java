package edu.ucla.sspace.evaluation;

import java.util.List;








































public class SimpleMultipleChoiceQuestion
  implements MultipleChoiceQuestion
{
  private final String prompt;
  private final List<String> options;
  private final int correctAnswer;
  
  public SimpleMultipleChoiceQuestion(String prompt, List<String> options, int correctAnswer)
  {
    this.prompt = prompt;
    this.options = options;
    this.correctAnswer = correctAnswer;
  }
  


  public String getPrompt()
  {
    return prompt;
  }
  


  public List<String> getOptions()
  {
    return options;
  }
  


  public int getCorrectAnswer()
  {
    return correctAnswer;
  }
  


  public String toString()
  {
    StringBuffer sb = new StringBuffer(100);
    sb.append(prompt).append(":\n");
    int i = 0;
    for (String option : options) {
      sb.append(i).append(" - ").append(option);
      if (i == correctAnswer) {
        sb.append("\t(correct)");
      }
      sb.append("\n");
      i++;
    }
    return sb.toString();
  }
}
