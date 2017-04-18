package edu.ucla.sspace.graph;

import edu.ucla.sspace.common.Statistics;
import edu.ucla.sspace.graph.isomorphism.IsomorphicGraphCounter;
import edu.ucla.sspace.graph.isomorphism.TypedIsomorphicGraphCounter;
import edu.ucla.sspace.util.Counter;
import edu.ucla.sspace.util.LoggerUtil;
import edu.ucla.sspace.util.WorkQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;


































































public class Fanmod
{
  private static final Logger LOGGER = Logger.getLogger(Fanmod.class.getName());
  



  private static final WorkQueue q = WorkQueue.getWorkQueue();
  













  public Fanmod() {}
  













  public <T, E extends TypedEdge<T>> Map<Multigraph<T, E>, Result> findMotifs(final Multigraph<T, E> g, final boolean findSimpleMotifs, final int motifSize, int numRandomGraphs, MotifFilter filter)
  {
    Counter<Multigraph<T, E>> inGraph = 
      new TypedIsomorphicGraphCounter();
    
    LoggerUtil.verbose(LOGGER, "Counting all the %smotifs of size %d in the input", new Object[] {
      findSimpleMotifs ? "simple " : "", Integer.valueOf(motifSize) });
    



    Iterator<Multigraph<T, E>> subgraphIter = findSimpleMotifs ? 
      new SimpleGraphIterator(g, motifSize) : 
      new SubgraphIterator(g, motifSize);
    

    int cnt = 0;
    long start = System.currentTimeMillis();
    while (subgraphIter.hasNext()) {
      inGraph.count((Multigraph)subgraphIter.next());
      cnt++; if (cnt % 10000 == 0) {
        long cur = System.currentTimeMillis();
        LoggerUtil.verbose(LOGGER, "Counted %d subgraphs in original graph (%d unique), %f subgraphs/sec", new Object[] {
          Integer.valueOf(cnt), 
          Integer.valueOf(inGraph.size()), Double.valueOf(cnt / ((cur - start) / 1000.0D)) });
      }
    }
    
    LoggerUtil.info(LOGGER, "Finished counting in original graph, computing null model", new Object[0]);
    




    final List<TypedIsomorphicGraphCounter<T, Multigraph<T, E>>> nullModelCounts = 
      new ArrayList();
    





    Set<Multigraph<T, E>> canonical = new HashSet();
    for (Multigraph<T, E> m : inGraph.items())
    {
      Multigraph<T, E> packed = (Multigraph)Graphs.pack(m);
      canonical.add(packed);
    }
    


    for (int j = 0; j < numRandomGraphs; j++)
    {

      nullModelCounts.add(
        TypedIsomorphicGraphCounter.asMotifs(canonical));
    }
    
    Object taskKey = q.registerTaskGroup(numRandomGraphs);
    
    for (int j = 0; j < numRandomGraphs; j++) {
      final int j_ = j;
      q.add(taskKey, new Runnable() {
        public void run() {
          LoggerUtil.verbose(Fanmod.LOGGER, "Computing random model %d", new Object[] { Integer.valueOf(j_) });
          

          TypedIsomorphicGraphCounter<T, Multigraph<T, E>> nullModelCounter = (TypedIsomorphicGraphCounter)nullModelCounts.get(j_);
          



          LoggerUtil.verbose(Fanmod.LOGGER, "Copying graph for null model %d", new Object[] { Integer.valueOf(j_) });
          Multigraph<T, E> nullModel = g.copy(g.vertices());
          

          LoggerUtil.verbose(Fanmod.LOGGER, "Shuffling edges of null model %d", new Object[] { Integer.valueOf(j_) });
          Graphs.shufflePreserveType(nullModel, 3);
          
          LoggerUtil.verbose(Fanmod.LOGGER, "Counting motifs of null model %d", new Object[] { Integer.valueOf(j_) });
          


          Iterator<Multigraph<T, E>> subgraphIter = 
            findSimpleMotifs ? 
            new SimpleGraphIterator(nullModel, motifSize) : 
            new SubgraphIterator(
            nullModel, motifSize);
          




          int count = 0;
          long start = System.currentTimeMillis();
          while (subgraphIter.hasNext()) {
            count++; if (count % 10000 == 0) {
              long cur = System.currentTimeMillis();
              LoggerUtil.verbose(Fanmod.LOGGER, 
                "Counted %d subgraphs in null model %d (%d unique), %f subgraphs/sec", new Object[] {
                
                Integer.valueOf(count), Integer.valueOf(j_), Integer.valueOf(nullModelCounter.size()), 
                Double.valueOf(count / ((cur - start) / 1000.0D)) });
            }
            
            Multigraph<T, E> sub = (Multigraph)subgraphIter.next();
            nullModelCounter.count(sub);
          }
        }
      });
    }
    
    q.await(taskKey);
    


    Object motifToResult = 
      new HashMap();
    
    LoggerUtil.info(LOGGER, "Building motif model counts", new Object[0]);
    




    Map<Multigraph<T, E>, List<Integer>> motifCounts = 
      new HashMap();
    int counterIndex = 0;
    for (TypedIsomorphicGraphCounter<T, Multigraph<T, E>> mc : nullModelCounts) {
      LoggerUtil.info(LOGGER, "Updating results for counter %d%n", new Object[] { Integer.valueOf(counterIndex++) });
      for (Map.Entry<Multigraph<T, E>, Integer> motifAndCount : mc) {
        Multigraph<T, E> motif = (Multigraph)motifAndCount.getKey();
        int count = ((Integer)motifAndCount.getValue()).intValue();
        List<Integer> counts = (List)motifCounts.get(motif);
        if (counts == null) {
          counts = new ArrayList(numRandomGraphs);
          motifCounts.put(motif, counts);
        }
        counts.add(Integer.valueOf(count));
      }
      assert (motifCounts.size() == inGraph.size()) : 
        "Null model has extra motif";
    }
    
    LoggerUtil.info(LOGGER, "Computing motif statistics", new Object[0]);
    

    for (Map.Entry<Multigraph<T, E>, Integer> motifAndCount : inGraph)
    {



      Multigraph<T, E> motif = (Multigraph)motifAndCount.getKey();
      
      Object packed = (Multigraph)Graphs.pack(motif);
      
      int count = ((Integer)motifAndCount.getValue()).intValue();
      

      List<Integer> counts = (List)motifCounts.get(packed);
      

      double mean = Statistics.mean(counts);
      double stddev = Statistics.stddev(counts);
      
      if (filter.accepts(count, mean, stddev)) {
        ((Map)motifToResult).put(packed, new Result(count, mean, stddev, 
          filter.getStatistic(count, mean, stddev)));
      }
    }
    LoggerUtil.info(LOGGER, "accepted %d motifs, rejected %d", new Object[] { Integer.valueOf(((Map)motifToResult).size()), 
      Integer.valueOf(inGraph.size() - ((Map)motifToResult).size()) });
    
    return motifToResult;
  }
  
















  public <E extends Edge> Map<Graph<E>, Result> findMotifs(final Graph<E> g, final int motifSize, int numRandomGraphs, MotifFilter filter)
  {
    Counter<Graph<E>> inGraph = new IsomorphicGraphCounter();
    



    Iterator<Graph<E>> subgraphIter = 
      new SubgraphIterator(g, motifSize);
    

    while (subgraphIter.hasNext()) {
      inGraph.count((Graph)subgraphIter.next());
    }
    



    final List<IsomorphicGraphCounter<Graph<E>>> nullModelCounts = 
      new ArrayList();
    


    for (int j = 0; j < numRandomGraphs; j++)
    {

      nullModelCounts.add(
        new IsomorphicGraphCounter(inGraph.items()));
    }
    
    Object taskKey = q.registerTaskGroup(numRandomGraphs);
    
    for (int j = 0; j < numRandomGraphs; j++) {
      final int j_ = j;
      q.add(taskKey, new Runnable() {
        public void run() {
          LoggerUtil.verbose(Fanmod.LOGGER, "Computing random model %d", new Object[] { Integer.valueOf(j_) });
          
          IsomorphicGraphCounter<Graph<E>> nullModelCounter = 
            (IsomorphicGraphCounter)nullModelCounts.get(j_);
          



          Graph<E> nullModel = g.copy(g.vertices());
          

          Graphs.shufflePreserve(nullModel, 3);
          


          Iterator<Graph<E>> subgraphIter = 
            new SubgraphIterator(nullModel, motifSize);
          




          int count = 0;
          long start = System.currentTimeMillis();
          while (subgraphIter.hasNext()) {
            count++; if (count % 100000 == 0) {
              long cur = System.currentTimeMillis();
              LoggerUtil.verbose(Fanmod.LOGGER, 
                "Counted %d subgraphs in null model (%d unique), %f subgraphs/sec", new Object[] {
                
                Integer.valueOf(count), Integer.valueOf(nullModelCounter.size()), 
                Double.valueOf(count / (cur - start / 1000L)) });
            }
            
            Graph<E> sub = (Graph)subgraphIter.next();
            nullModelCounter.count(sub);
          }
        }
      });
    }
    
    q.await(taskKey);
    


    Map<Graph<E>, Result> motifToResult = new HashMap();
    

    for (Map.Entry<Graph<E>, Integer> motifAndCount : inGraph) {
      Graph<E> motif = (Graph)motifAndCount.getKey();
      int count = ((Integer)motifAndCount.getValue()).intValue();
      int[] counts = new int[numRandomGraphs];
      int i = 0;
      for (IsomorphicGraphCounter<Graph<E>> mc : nullModelCounts) {
        counts[(i++)] = mc.getCount(motif);
      }
      double mean = Statistics.mean(counts);
      double stddev = Statistics.stddev(counts);
      
      if (filter.accepts(count, mean, stddev)) {
        motifToResult.put(motif, new Result(count, mean, stddev, 
          filter.getStatistic(count, mean, stddev)));
      }
    }
    
    return motifToResult;
  }
  





  public static class Result
  {
    public final int count;
    




    public final double meanCountInNullModel;
    




    public final double stddevInNullModel;
    




    public final double statistic;
    




    public Result(int count, double meanCountInNullModel, double stddevInNullModel, double statistic)
    {
      this.count = count;
      this.meanCountInNullModel = meanCountInNullModel;
      this.stddevInNullModel = stddevInNullModel;
      this.statistic = statistic;
    }
  }
  











  public static class ZScoreFilter
    implements Fanmod.MotifFilter
  {
    private final double minZscore;
    











    public ZScoreFilter(double minZscore)
    {
      this.minZscore = minZscore;
    }
    
    public boolean accepts(int actualFrequency, double expectedValue, double standardDeviation)
    {
      double zScore = (actualFrequency - expectedValue) / 
        standardDeviation;
      return zScore >= minZscore;
    }
    
    public double getStatistic(int actualFrequency, double expectedValue, double standardDeviation)
    {
      return (actualFrequency - expectedValue) / standardDeviation;
    }
  }
  
  public static class FrequencyFilter implements Fanmod.MotifFilter
  {
    private final double minFrequency;
    
    public FrequencyFilter(double minFrequency) {
      this.minFrequency = minFrequency;
    }
    
    public boolean accepts(int actualFrequency, double expectedValue, double standardDeviation)
    {
      return actualFrequency >= minFrequency;
    }
    
    public double getStatistic(int actualFrequency, double expectedValue, double standardDeviation)
    {
      return actualFrequency;
    }
  }
  
  public static class FrequencyAndZScoreFilter implements Fanmod.MotifFilter
  {
    private final int minFrequency;
    private final double minZscore;
    
    public FrequencyAndZScoreFilter(int minFrequency, double minZscore)
    {
      this.minFrequency = minFrequency;
      this.minZscore = minZscore;
    }
    
    public boolean accepts(int actualFrequency, double expectedValue, double standardDeviation)
    {
      double zScore = (actualFrequency - expectedValue) / 
        standardDeviation;
      return (actualFrequency >= minFrequency) && (zScore >= minZscore);
    }
    
    public double getStatistic(int actualFrequency, double expectedValue, double standardDeviation)
    {
      return (actualFrequency - expectedValue) / standardDeviation;
    }
  }
  
  public static abstract interface MotifFilter
  {
    public abstract boolean accepts(int paramInt, double paramDouble1, double paramDouble2);
    
    public abstract double getStatistic(int paramInt, double paramDouble1, double paramDouble2);
  }
}
