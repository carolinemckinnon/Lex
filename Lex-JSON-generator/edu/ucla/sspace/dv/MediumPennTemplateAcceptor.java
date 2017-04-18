package edu.ucla.sspace.dv;

import edu.ucla.sspace.dependency.DependencyPath;
import edu.ucla.sspace.dependency.DependencyPathAcceptor;
import edu.ucla.sspace.dependency.DependencyTreeNode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;








































public class MediumPennTemplateAcceptor
  implements DependencyPathAcceptor
{
  static final Set<String> MEDIUM_TEMPLATES = new HashSet();
  





  static final Map<String, String> POS_TAG_TO_CLASS = new HashMap();
  
  static final Map<String, String> REL_TO_CLASS;
  

  static
  {
    for (String noun : PennTags.NOUN_POS_TAGS)
      POS_TAG_TO_CLASS.put(noun, "N");
    for (String adj : PennTags.ADJ_POS_TAGS)
      POS_TAG_TO_CLASS.put(adj, "J");
    for (String adv : PennTags.ADV_POS_TAGS)
      POS_TAG_TO_CLASS.put(adv, "R");
    for (String verb : PennTags.VERB_POS_TAGS) {
      POS_TAG_TO_CLASS.put(verb, "V");
    }
    





    REL_TO_CLASS = 
      new HashMap();
    


    for (String mod : PennTags.MODIFIERS) {
      REL_TO_CLASS.put(mod, "mod");
    }
    





    MEDIUM_TEMPLATES.add("J:nmod:N,N:amod:(null)");
    MEDIUM_TEMPLATES.add("J:nmod:N,N:vmod:(null)");
    MEDIUM_TEMPLATES.add("J:nmod:N,N:nmod:(null)");
    MEDIUM_TEMPLATES.add("J:nmod:N,N:nmod:N");
    MEDIUM_TEMPLATES.add("J:sbj:N,N:amod:(null)");
    MEDIUM_TEMPLATES.add("J:sbj:N,N:nmod:(null)");
    MEDIUM_TEMPLATES.add("J:sbj:N,N:vmod:(null)");
    MEDIUM_TEMPLATES.add("J:sbj:N,N:nmod:N");
    
    MEDIUM_TEMPLATES.add("R:nmod:N,N:amod:(null)");
    MEDIUM_TEMPLATES.add("R:nmod:N,N:vmod:(null)");
    MEDIUM_TEMPLATES.add("R:nmod:N,N:nmod:(null)");
    MEDIUM_TEMPLATES.add("R:nmod:N,N:nmod:N");
    MEDIUM_TEMPLATES.add("R:sbj:N,N:amod:(null)");
    MEDIUM_TEMPLATES.add("R:sbj:N,N:vmod:(null)");
    MEDIUM_TEMPLATES.add("R:sbj:N,N:nmod:(null)");
    MEDIUM_TEMPLATES.add("R:sbj:N,N:nmod:N");
    

    MEDIUM_TEMPLATES.add("N:coord:N,N:mod:(null)");
    MEDIUM_TEMPLATES.add("N:coord:N,N:nmod:N");
    MEDIUM_TEMPLATES.add("N:gen:N,N:nmod:(null)");
    MEDIUM_TEMPLATES.add("N:gen:N,N:nmod:N");
    MEDIUM_TEMPLATES.add("N:nmod:N,N:coord:N");
    MEDIUM_TEMPLATES.add("N:nmod:N,N:coord:N,N:nmod:N");
    MEDIUM_TEMPLATES.add("N:nmod:N,N:gen:N");
    MEDIUM_TEMPLATES.add("N:nmod:N,N:gen:N,N:nmod:N");
    MEDIUM_TEMPLATES.add("N:nmod:N,N:mod:A");
    MEDIUM_TEMPLATES.add("N:nmod:N,N:mod:TO");
    MEDIUM_TEMPLATES.add("N:nmod:N,N:obj:V");
    MEDIUM_TEMPLATES.add("N:nmod:N,N:prd:V");
    MEDIUM_TEMPLATES.add("N:nmod:N,N:sbj:A");
    MEDIUM_TEMPLATES.add("N:nmod:N,N:sbj:V");
    
    MEDIUM_TEMPLATES.add("(null):mod:N,N:coord:N");
    MEDIUM_TEMPLATES.add("(null):mod:N,N:coord:N,N:mod:(null)");
    MEDIUM_TEMPLATES.add("(null):mod:N,N:gen:N");
    MEDIUM_TEMPLATES.add("(null):mod:N,N:gen:N,N:mod:(null)");
    MEDIUM_TEMPLATES.add("(null):mod:N,N:amod:J");
    MEDIUM_TEMPLATES.add("(null):mod:N,N:adv:R");
    MEDIUM_TEMPLATES.add("(null):mod:N,N:pmod:TO");
    MEDIUM_TEMPLATES.add("(null):mod:N,N:obj:V");
    MEDIUM_TEMPLATES.add("(null):mod:N,N:prd:V");
    MEDIUM_TEMPLATES.add("(null):mod:N,N:sbj:J");
    MEDIUM_TEMPLATES.add("(null):mod:N,N:sbj:R");
    MEDIUM_TEMPLATES.add("(null):mod:N,N:sbj:V");
    
    MEDIUM_TEMPLATES.add("TO:mod:N,N:mod:(null)");
    MEDIUM_TEMPLATES.add("TO:mod:N,N:nmod:N");
    
    MEDIUM_TEMPLATES.add("V:obj:N,N:mod:(null)");
    MEDIUM_TEMPLATES.add("V:obj:N,N:nmod:N");
    MEDIUM_TEMPLATES.add("V:sbj:N,N:mod:(null)");
    MEDIUM_TEMPLATES.add("V:sbj:N,N:nmod:N");
    MEDIUM_TEMPLATES.add("V:prd:N,N:mod:(null)");
    MEDIUM_TEMPLATES.add("V:prd:N,N:nmod:N");
  }
  





  public MediumPennTemplateAcceptor() {}
  




  public boolean accepts(DependencyPath path)
  {
    return acceptsInternal(path);
  }
  










  static boolean acceptsInternal(DependencyPath path)
  {
    if (MinimumPennTemplateAcceptor.acceptsInternal(path)) {
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
      





      String class1 = (String)POS_TAG_TO_CLASS.get(firstPos);
      String class2 = (String)POS_TAG_TO_CLASS.get(secPos);
      
      if (class1 != null)
        firstPos = class1;
      if (class2 != null) {
        secPos = class2;
      }
      


      String relClass = (String)REL_TO_CLASS.get(rel);
      if (relClass != null) {
        rel = relClass;
      }
      

      nullStart.append(i == 1 ? "(null)" : firstPos);
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
    boolean match = (MEDIUM_TEMPLATES.contains(noNulls.toString())) || 
      (MEDIUM_TEMPLATES.contains(nullStart.toString())) || 
      (MEDIUM_TEMPLATES.contains(nullEnd.toString()));
    
    return match;
  }
  


  public int maxPathLength()
  {
    return 4;
  }
}
