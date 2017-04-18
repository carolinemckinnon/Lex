package edu.ucla.sspace.wordsi.psd;

import edu.ucla.sspace.wordsi.AssignmentReporter;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;





















































public class PseudoWordReporter
  implements AssignmentReporter
{
  private PrintStream writer;
  private final Map<String, Map<String, BitSet>> contextAssignments;
  private Map<String, Map<String, List<Integer>>> clusterCounts;
  
  public PseudoWordReporter(OutputStream stream)
  {
    writer = new PrintStream(stream);
    clusterCounts = new HashMap();
    contextAssignments = new HashMap();
  }
  




  public synchronized void updateAssignment(String primaryKey, String secondaryKey, int clusterId)
  {
    if (primaryKey.equals(secondaryKey)) {
      return;
    }
    
    Map<String, List<Integer>> secondaryCounts = (Map)clusterCounts.get(
      primaryKey);
    if (secondaryCounts == null) {
      secondaryCounts = new HashMap();
      clusterCounts.put(primaryKey, secondaryCounts);
    }
    

    List<Integer> counts = (List)secondaryCounts.get(secondaryKey);
    if (counts == null) {
      counts = new ArrayList(clusterId);
      secondaryCounts.put(secondaryKey, counts);
    }
    

    while (clusterId >= counts.size()) {
      counts.add(Integer.valueOf(0));
    }
    
    counts.set(clusterId, Integer.valueOf(((Integer)counts.get(clusterId)).intValue() + 1));
  }
  






  public void finalizeReport()
  {
    Iterator localIterator1 = clusterCounts.entrySet().iterator();
    Iterator localIterator2;
    for (; localIterator1.hasNext(); 
        

        localIterator2.hasNext())
    {
      Map.Entry<String, Map<String, List<Integer>>> e = (Map.Entry)localIterator1.next();
      String firstKey = (String)e.getKey();
      localIterator2 = ((Map)e.getValue()).entrySet().iterator(); continue;Map.Entry<String, List<Integer>> f = (Map.Entry)localIterator2.next();
      String secondKey = (String)f.getKey();
      List<Integer> counts = (List)f.getValue();
      for (int i = 0; i < counts.size(); i++) {
        if (((Integer)counts.get(i)).intValue() > 0)
          writer.printf("%s %s %d %d\n", new Object[] {
            firstKey, secondKey, Integer.valueOf(i), counts.get(i) });
      }
    }
    writer.close();
  }
  






  public void assignContextToKey(String primaryKey, String secondaryKey, int contextId)
  {
    Map<String, BitSet> termContexts = (Map)contextAssignments.get(primaryKey);
    if (termContexts == null) {
      synchronized (this) {
        termContexts = (Map)contextAssignments.get(primaryKey);
        if (termContexts == null) {
          termContexts = new HashMap();
          contextAssignments.put(primaryKey, termContexts);
        }
      }
    }
    

    BitSet contextIds = (BitSet)termContexts.get(secondaryKey);
    if (contextIds == null) {
      synchronized (this) {
        contextIds = (BitSet)termContexts.get(secondaryKey);
        if (contextIds == null) {
          contextIds = new BitSet();
          termContexts.put(secondaryKey, contextIds);
        }
      }
    }
    

    synchronized (contextIds) {
      contextIds.set(contextId);
    }
  }
  



  public String[] contextLabels(String primaryKey)
  {
    Map<String, BitSet> termContexts = (Map)contextAssignments.get(primaryKey);
    if (termContexts == null) {
      return new String[0];
    }
    
    int totalAssignments = 0;
    for (Map.Entry<String, BitSet> entry : termContexts.entrySet()) {
      totalAssignments = Math.max(
        totalAssignments, ((BitSet)entry.getValue()).length());
    }
    

    String[] contextLabels = new String[totalAssignments];
    int contextId; for (Iterator localIterator2 = termContexts.entrySet().iterator(); localIterator2.hasNext(); 
        
        contextId >= 0)
    {
      Object entry = (Map.Entry)localIterator2.next();
      BitSet contextIds = (BitSet)((Map.Entry)entry).getValue();
      contextId = contextIds.nextSetBit(0); continue;
      
      contextLabels[contextId] = ((String)((Map.Entry)entry).getKey());contextId = contextIds.nextSetBit(contextId + 1);
    }
    
    return contextLabels;
  }
}
