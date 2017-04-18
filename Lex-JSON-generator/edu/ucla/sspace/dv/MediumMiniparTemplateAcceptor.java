package edu.ucla.sspace.dv;

import edu.ucla.sspace.dependency.DependencyPath;
import edu.ucla.sspace.dependency.DependencyPathAcceptor;
import edu.ucla.sspace.dependency.DependencyTreeNode;
import java.util.HashSet;
import java.util.Set;





































public class MediumMiniparTemplateAcceptor
  implements DependencyPathAcceptor
{
  static final Set<String> MEDIUM_TEMPLATES = new HashSet();
  
  static {
    MEDIUM_TEMPLATES.add("A:mod:N,N:lex-mod:(null)");
    MEDIUM_TEMPLATES.add("A:mod:N,N:nn:N");
    MEDIUM_TEMPLATES.add("A:subj:N,N:lex-mod:(null)");
    MEDIUM_TEMPLATES.add("A:subj:N,N:nn:N");
    MEDIUM_TEMPLATES.add("N:conj:N,N:lex-mod:(null)");
    MEDIUM_TEMPLATES.add("N:conj:N,N:nn:N");
    MEDIUM_TEMPLATES.add("N:gen:N,N:lex-mod:(null)");
    MEDIUM_TEMPLATES.add("N:gen:N,N:nn:N");
    MEDIUM_TEMPLATES.add("N:nn:N,N:conj:N");
    MEDIUM_TEMPLATES.add("N:nn:N,N:conj:N,N:nn:N");
    MEDIUM_TEMPLATES.add("N:nn:N,N:gen:N");
    MEDIUM_TEMPLATES.add("N:nn:N,N:gen:N,N:nn:N");
    MEDIUM_TEMPLATES.add("N:nn:N,N:mod:A");
    MEDIUM_TEMPLATES.add("N:nn:N,N:mod:Pred");
    MEDIUM_TEMPLATES.add("N:nn:N,N:obj:V");
    MEDIUM_TEMPLATES.add("N:nn:N,N:subj:A");
    MEDIUM_TEMPLATES.add("N:nn:N,N:subj:V");
    MEDIUM_TEMPLATES.add("(null):lex-mod:N,N:conj:N");
    MEDIUM_TEMPLATES.add("(null):lex-mod:N,N:conj:N,N:lex-mod:(null)");
    MEDIUM_TEMPLATES.add("(null):lex-mod:N,N:gen:N");
    MEDIUM_TEMPLATES.add("(null):lex-mod:N,N:gen:N,N:lex-mod:(null)");
    MEDIUM_TEMPLATES.add("(null):lex-mod:N,N:mod:A");
    MEDIUM_TEMPLATES.add("(null):lex-mod:N,N:mod:Pred");
    MEDIUM_TEMPLATES.add("(null):lex-mod:N,N:obj:V");
    MEDIUM_TEMPLATES.add("(null):lex-mod:N,N:subj:A");
    MEDIUM_TEMPLATES.add("(null):lex-mod:N,N:subj:V");
    MEDIUM_TEMPLATES.add("Prep:mod:N,N:lex-mod:(null)");
    MEDIUM_TEMPLATES.add("Prep:mod:N,N:nn:N");
    MEDIUM_TEMPLATES.add("V:obj:N,N:lex-mod:(null)");
    MEDIUM_TEMPLATES.add("V:obj:N,N:nn:N");
    MEDIUM_TEMPLATES.add("V:subj:N,N:lex-mod:(null)");
    MEDIUM_TEMPLATES.add("V:subj:N,N:nn:N");
  }
  





  public MediumMiniparTemplateAcceptor() {}
  




  public boolean accepts(DependencyPath path)
  {
    return acceptsInternal(path);
  }
  










  static boolean acceptsInternal(DependencyPath path)
  {
    if (MinimumMiniparTemplateAcceptor.acceptsInternal(path)) {
      return true;
    }
    
    if (path.length() > 3) {
      return false;
    }
    int pathLength = path.length();
    




    StringBuilder nullStart = new StringBuilder(pathLength * 16);
    StringBuilder nullEnd = new StringBuilder(pathLength * 16);
    StringBuilder noNulls = new StringBuilder(pathLength * 16);
    


    DependencyTreeNode first = path.first();
    for (int i = 1; i < pathLength; i++) {
      DependencyTreeNode second = path.getNode(i);
      


      if (first.word().equals("")) {
        return false;
      }
      
      String rel = path.getRelation(i - 1);
      String firstPos = first.pos();
      String secPos = second.pos();
      
      nullStart.append(i == 0 ? "(null)" : firstPos);
      nullStart.append(":").append(rel).append(":").append(secPos);
      
      nullEnd.append(firstPos).append(":").append(rel).append(":");
      nullEnd.append(i + 1 == pathLength ? "(null)" : secPos);
      
      noNulls.append(firstPos).append(":").append(rel)
        .append(":").append(secPos);
      

      if (i + 1 < pathLength) {
        nullStart.append(",");
        nullEnd.append(",");
        noNulls.append(",");
      }
      

      first = second;
    }
    

    if (first.word().equals("")) {
      return false;
    }
    return (MEDIUM_TEMPLATES.contains(noNulls.toString())) || 
      (MEDIUM_TEMPLATES.contains(nullStart.toString())) || 
      (MEDIUM_TEMPLATES.contains(nullEnd.toString()));
  }
  


  public int maxPathLength()
  {
    return 4;
  }
}
