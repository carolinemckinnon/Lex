package edu.ucla.sspace.evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;












































public class ESLSynonymEvaluation
  implements WordChoiceEvaluation
{
  private final Collection<MultipleChoiceQuestion> questions;
  private final String dataFileName;
  
  public ESLSynonymEvaluation(String eslQuestionsFileName)
  {
    this(new File(eslQuestionsFileName));
  }
  


  public ESLSynonymEvaluation(File eslQuestionsFile)
  {
    questions = parseTestFile(eslQuestionsFile);
    dataFileName = eslQuestionsFile.getName();
  }
  


  private static Collection<MultipleChoiceQuestion> parseTestFile(File f)
  {
    try
    {
      BufferedReader br = new BufferedReader(new FileReader(f));
      Collection<MultipleChoiceQuestion> questions = 
        new LinkedList();
      for (String line = null; (line = br.readLine()) != null;)
      {

        if ((!line.startsWith("#")) && (line.length() != 0))
        {





          String[] promptAndOptions = line.split("\\|");
          


          for (int i = 0; i < promptAndOptions.length; i++) {
            promptAndOptions[i] = promptAndOptions[i].trim();
          }
          
          String prompt = promptAndOptions[0];
          String[] options = new String[promptAndOptions.length - 1];
          List<String> optionsAsList = Arrays.asList((String[])Arrays.copyOfRange(
            promptAndOptions, 1, promptAndOptions.length - 1));
          questions.add(
            new SimpleMultipleChoiceQuestion(prompt, optionsAsList, 0));
        }
      }
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
    return "Word Choice [" + dataFileName + "]";
  }
}
