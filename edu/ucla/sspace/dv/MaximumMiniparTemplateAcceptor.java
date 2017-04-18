package edu.ucla.sspace.dv;

import edu.ucla.sspace.dependency.DependencyPath;
import edu.ucla.sspace.dependency.DependencyPathAcceptor;
import edu.ucla.sspace.dependency.DependencyTreeNode;
import java.util.HashSet;
import java.util.Set;




































public class MaximumMiniparTemplateAcceptor
  implements DependencyPathAcceptor
{
  static final Set<String> MAXIMUM_TEMPLATES = new HashSet();
  
  static {
    MAXIMUM_TEMPLATES.add("A:mod:A,A:mod:N,N:lex-mod:(null)");
    MAXIMUM_TEMPLATES.add("A:mod:A,A:mod:N,N:nn:N");
    MAXIMUM_TEMPLATES.add("A:mod:Prep,Prep:pcomp-n:N,N:lex-mod:(null)");
    MAXIMUM_TEMPLATES.add("N:mod:Prep,Prep:pcomp-n:N,N:lex-mod:(null)");
    MAXIMUM_TEMPLATES.add("N:mod:Prep,Prep:pcomp-n:N,N:nn:N");
    MAXIMUM_TEMPLATES.add("N:nn:N,N:mod:A,A:mod:A");
    MAXIMUM_TEMPLATES.add("N:nn:N,N:mod:Prep,Prep:pcomp-n:N");
    MAXIMUM_TEMPLATES.add("N:nn:N,N:mod:Prep,Prep:pcomp-n:N,N:nn:N");
    MAXIMUM_TEMPLATES.add("N:nn:N,N:obj:V,V:subj:N");
    MAXIMUM_TEMPLATES.add("N:nn:N,N:obj:V,V:subj:N,N:nn:N");
    MAXIMUM_TEMPLATES.add("N:nn:N,N:pcomp-n:Prep");
    MAXIMUM_TEMPLATES.add("N:nn:N,N:pcomp-n:Prep,Prep:mod:N");
    MAXIMUM_TEMPLATES.add("N:nn:N,N:pcomp-n:Prep,Prep:mod:N,N:nn:N");
    MAXIMUM_TEMPLATES.add("N:nn:N,N:subj:V,V:obj:N");
    MAXIMUM_TEMPLATES.add("N:nn:N,N:subj:V,V:obj:N,N:nn:N");
    MAXIMUM_TEMPLATES.add("N:nn:N,V:s:C,C:fc:V");
    MAXIMUM_TEMPLATES.add("N:obj:V,V:subj:N,N:lex-mod:(null)");
    MAXIMUM_TEMPLATES.add("N:obj:V,V:subj:N,N:nn:N");
    MAXIMUM_TEMPLATES.add("N:pcomp-n:Prep,Prep:mod:N,N:lex-mod:(null)");
    MAXIMUM_TEMPLATES.add("N:pcomp-n:Prep,Prep:mod:N,N:nn:N");
    MAXIMUM_TEMPLATES.add("N:subj:V,V:obj:N,N:lex-mod:(null)");
    MAXIMUM_TEMPLATES.add("N:subj:V,V:obj:N,N:nn:N");
    MAXIMUM_TEMPLATES.add("(null):lex-mod:N,N:mod:A,A:mod:A");
    MAXIMUM_TEMPLATES.add("(null):lex-mod:N,N:mod:Prep,Prep:pcomp-n:N");
    MAXIMUM_TEMPLATES.add("(null):lex-mod:N,N:mod:Prep,Prep:pcomp-n:N,N:lex-mod:(null)");
    MAXIMUM_TEMPLATES.add("(null):lex-mod:N,N:obj:V,V:subj:N");
    MAXIMUM_TEMPLATES.add("(null):lex-mod:N,N:obj:V,V:subj:N,N:lex-mod:(null)");
    MAXIMUM_TEMPLATES.add("(null):lex-mod:N,N:pcomp-n:Pred,Prep:mod:A");
    MAXIMUM_TEMPLATES.add("(null):lex-mod:N,N:pcomp-n:Prep");
    MAXIMUM_TEMPLATES.add("(null):lex-mod:N,N:pcomp-n:Prep,Prep:mod:N");
    MAXIMUM_TEMPLATES.add("(null):lex-mod:N,N:pcomp-n:Prep,Prep:mod:N,N:lex-mod:(null)");
    MAXIMUM_TEMPLATES.add("(null):lex-mod:N,N:pcomp-n:Prep,Prep:mod:V");
    MAXIMUM_TEMPLATES.add("(null):lex-mod:N,N:rel:C,C:i:V");
    MAXIMUM_TEMPLATES.add("(null):lex-mod:N,N:subj:V,V:obj:N");
    MAXIMUM_TEMPLATES.add("(null):lex-mod:N,N:subj:V,V:obj:N,N:lex-mod:(null)");
    MAXIMUM_TEMPLATES.add("(null):lex-mod:N,V:s:C,C:fc:V");
    MAXIMUM_TEMPLATES.add("Prep:pcomp-n:N,N:lex-mod:(null)");
    MAXIMUM_TEMPLATES.add("Prep:pcomp-n:N,N:nn:N");
    MAXIMUM_TEMPLATES.add("V:fc:C,C:s:N,N:lex-mod:(null)");
    MAXIMUM_TEMPLATES.add("V:fc:C,C:s:N,N:nn:N");
    MAXIMUM_TEMPLATES.add("V:i:C,C:rel:N,N:lex-mod:(null)");
    MAXIMUM_TEMPLATES.add("V:mod:Prep,Prep:pcomp-n:N,N:lex-mod:(null)");
  }
  





  public MaximumMiniparTemplateAcceptor() {}
  




  public boolean accepts(DependencyPath path)
  {
    return acceptsInternal(path);
  }
  










  static boolean acceptsInternal(DependencyPath path)
  {
    if (MediumMiniparTemplateAcceptor.acceptsInternal(path)) {
      return true;
    }
    
    if (path.length() > 4) {
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
    return (MAXIMUM_TEMPLATES.contains(noNulls.toString())) || 
      (MAXIMUM_TEMPLATES.contains(nullStart.toString())) || 
      (MAXIMUM_TEMPLATES.contains(nullEnd.toString()));
  }
  


  public int maxPathLength()
  {
    return 5;
  }
}
