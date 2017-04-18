package edu.ucla.sspace.evaluation;

import edu.ucla.sspace.common.SemanticSpace;
import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.util.Pair;
import edu.ucla.sspace.util.WorkerThread;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;


















































public abstract class AbstractWordAssociationTest
  implements WordAssociationTest
{
  protected final Map<Pair<String>, Double> wordPairToHumanJudgement;
  
  public AbstractWordAssociationTest(Map<Pair<String>, Double> wordPairToHumanJudgement)
  {
    this.wordPairToHumanJudgement = wordPairToHumanJudgement;
  }
  













  public WordAssociationReport evaluate(final SemanticSpace sspace)
  {
    int numThreads = Runtime.getRuntime().availableProcessors();
    
    BlockingQueue<Runnable> workQueue = 
      new LinkedBlockingQueue();
    for (int i = 0; i < numThreads; i++) {
      Thread t = new WorkerThread(workQueue);
      t.start();
    }
    Semaphore itemsProcessed = new Semaphore(0);
    


    final double testRange = getHighestScore() - getLowestScore();
    final AtomicInteger unanswered = new AtomicInteger();
    final AtomicInteger answered = new AtomicInteger();
    

    int numQuestions = wordPairToHumanJudgement.size();
    final double[] compScores = new double[numQuestions];
    double[] humanScores = new double[numQuestions];
    


    int question = 0;
    
    Iterator localIterator = wordPairToHumanJudgement.entrySet().iterator();
    while (localIterator.hasNext()) {
      Map.Entry<Pair<String>, Double> e = (Map.Entry)localIterator.next();
      

      final Pair<String> p = (Pair)e.getKey();
      final int index = question;
      double humanScore = ((Double)e.getValue()).doubleValue();
      question++;
      
      humanScores[index] = humanScore;
      

      workQueue.offer(new Runnable() {
        public void run() {
          Double association = computeAssociation(sspace, (String)px, (String)py);
          



          if (association == null) {
            unanswered.incrementAndGet();
            compScores[index] = Double.MIN_VALUE;
          } else {
            answered.incrementAndGet();
            


            compScores[index] = 
              (association.doubleValue() * testRange + getLowestScore());
          }
          val$itemsProcessed.release();
        }
      });
    }
    
    try
    {
      itemsProcessed.acquire(numQuestions);
    } catch (InterruptedException ie) {
      throw new Error(ie);
    }
    





    double[] finalHumanScores = new double[answered.get()];
    double[] finalCompScores = new double[answered.get()];
    int i = 0; for (int index = 0; i < numQuestions; i++) {
      if (compScores[i] != Double.MIN_VALUE)
      {
        finalHumanScores[index] = humanScores[i];
        finalCompScores[index] = compScores[i];
        index++;
      }
    }
    
    double score = computeScore(humanScores, compScores);
    
    return new SimpleWordAssociationReport(
      wordPairToHumanJudgement.size(), score, unanswered.get());
  }
  





  protected double computeScore(double[] humanScores, double[] compScores)
  {
    return Similarity.correlation(humanScores, compScores);
  }
  
  protected abstract double getLowestScore();
  
  protected abstract double getHighestScore();
  
  protected abstract Double computeAssociation(SemanticSpace paramSemanticSpace, String paramString1, String paramString2);
}
