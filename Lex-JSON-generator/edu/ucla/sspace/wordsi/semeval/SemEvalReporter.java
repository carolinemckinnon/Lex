package edu.ucla.sspace.wordsi.semeval;

import edu.ucla.sspace.wordsi.AssignmentReporter;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
















































public class SemEvalReporter
  implements AssignmentReporter
{
  private final Map<String, List<Assignment>> assignmentMap;
  private PrintStream writer;
  
  public SemEvalReporter(OutputStream stream)
  {
    writer = new PrintStream(stream);
    assignmentMap = new HashMap();
  }
  





  public synchronized void updateAssignment(String primaryKey, String secondaryKey, int clusterId)
  {
    int splitIndex = secondaryKey.lastIndexOf(".");
    String wordPos = secondaryKey.substring(0, splitIndex);
    

    writer.printf("%s %s %s.%d\n", new Object[] {
      wordPos, secondaryKey, wordPos, Integer.valueOf(clusterId) });
  }
  


  public void finalizeReport()
  {
    writer.close();
  }
  





  public void assignContextToKey(String primaryKey, String secondaryKey, int contextId)
  {
    List<Assignment> primaryAssignments = (List)assignmentMap.get(primaryKey);
    if (primaryAssignments == null) {
      synchronized (this) {
        primaryAssignments = (List)assignmentMap.get(primaryKey);
        if (primaryAssignments == null) {
          primaryAssignments = Collections.synchronizedList(
            new ArrayList());
          assignmentMap.put(primaryKey, primaryAssignments);
        }
      }
    }
    
    primaryAssignments.add(new Assignment(secondaryKey, contextId));
  }
  



  public String[] contextLabels(String primaryKey)
  {
    List<Assignment> primaryAssignments = (List)assignmentMap.get(primaryKey);
    

    if (primaryAssignments == null) {
      return new String[0];
    }
    

    String[] labels = new String[primaryAssignments.size()];
    for (Assignment assignment : primaryAssignments)
      labels[id] = key;
    return labels;
  }
  

  private class Assignment
  {
    public String key;
    public int id;
    
    public Assignment(String key, int id)
    {
      this.key = key;
      this.id = id;
    }
  }
}
