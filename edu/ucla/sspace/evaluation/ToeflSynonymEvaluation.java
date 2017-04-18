package edu.ucla.sspace.evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
















































public class ToeflSynonymEvaluation
  implements WordChoiceEvaluation
{
  private final Collection<MultipleChoiceQuestion> questions;
  
  public ToeflSynonymEvaluation(String questionFileName, String answerFileName)
  {
    this(new File(questionFileName), new File(answerFileName));
  }
  






  public ToeflSynonymEvaluation(File questionsFile, File answerFile)
  {
    questions = parseTestFile(questionsFile, answerFile);
  }
  





  private static Collection<MultipleChoiceQuestion> parseTestFile(File questionFile, File answerFile)
  {
    try
    {
      BufferedReader br = new BufferedReader(new FileReader(answerFile));
      List<Integer> answers = new ArrayList();
      for (String line = null; (line = br.readLine()) != null;)
      {
        if (line.length() != 0)
        {




          answers.add(Integer.valueOf(line.split("\\s+")[3].charAt(0) - 'a')); }
      }
      br.close();
      

      int question = 0;
      br = new BufferedReader(new FileReader(questionFile));
      Collection<MultipleChoiceQuestion> questions = 
        new LinkedList();
      for (String line = null; (line = br.readLine()) != null;)
      {
        if (line.length() != 0)
        {


          String prompt = line.split("\\s+")[1];
          String[] options = new String[4];
          for (int i = 0; i < options.length; i++)
            options[i] = br.readLine().split("\\s+")[1];
          List<String> optionsAsList = Arrays.asList(options);
          int correctOption = ((Integer)answers.get(question)).intValue();
          questions.add(new SimpleMultipleChoiceQuestion(
            prompt, optionsAsList, correctOption));
          question++;
        } }
      br.close();
      
      return questions;
    }
    catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  


  public Collection<MultipleChoiceQuestion> getQuestions()
  {
    return questions;
  }
  
  public String toString() {
    return "TOEFL Synonym Test";
  }
}
