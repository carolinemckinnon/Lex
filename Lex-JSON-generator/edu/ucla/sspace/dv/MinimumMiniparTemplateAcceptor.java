package edu.ucla.sspace.dv;

import edu.ucla.sspace.dependency.DependencyPath;
import edu.ucla.sspace.dependency.DependencyPathAcceptor;
import edu.ucla.sspace.dependency.DependencyTreeNode;
import java.util.HashSet;
import java.util.Set;



































public class MinimumMiniparTemplateAcceptor
  implements DependencyPathAcceptor
{
  static final Set<String> MINIMUM_TEMPLATES = new HashSet();
  
  static {
    MINIMUM_TEMPLATES.add(toPattern("A", "amod", "V"));
    MINIMUM_TEMPLATES.add(toPattern("A", "amod", "A"));
    MINIMUM_TEMPLATES.add(toPattern("A", "mod", "A"));
    MINIMUM_TEMPLATES.add(toPattern("A", "mod", "N"));
    MINIMUM_TEMPLATES.add(toPattern("A", "mod", "Prep"));
    MINIMUM_TEMPLATES.add(toPattern("A", "mod", "V"));
    MINIMUM_TEMPLATES.add(toPattern("A", "subj", "N"));
    MINIMUM_TEMPLATES.add(toPattern("N", "conj", "N"));
    MINIMUM_TEMPLATES.add(toPattern("N", "gen", "N"));
    MINIMUM_TEMPLATES.add(toPattern("N", "mod", "A"));
    MINIMUM_TEMPLATES.add(toPattern("N", "mod", "Prep"));
    MINIMUM_TEMPLATES.add(toPattern("N", "nn", "N"));
    MINIMUM_TEMPLATES.add(toPattern("N", "obj", "V"));
    MINIMUM_TEMPLATES.add(toPattern("N", "pcomp-n", "Prep"));
    MINIMUM_TEMPLATES.add(toPattern("N", "subj", "A"));
    MINIMUM_TEMPLATES.add(toPattern("N", "subj", "N"));
    MINIMUM_TEMPLATES.add(toPattern("N", "subj", "V"));
    MINIMUM_TEMPLATES.add(toPattern(null, "lex-mod", "V"));
    MINIMUM_TEMPLATES.add(toPattern("Prep", "mod", "A"));
    MINIMUM_TEMPLATES.add(toPattern("Prep", "mod", "N"));
    MINIMUM_TEMPLATES.add(toPattern("Prep", "mod", "V"));
    MINIMUM_TEMPLATES.add(toPattern("Prep", "pcomp-n", "N"));
    MINIMUM_TEMPLATES.add(toPattern("V", "amod", "A"));
    MINIMUM_TEMPLATES.add(toPattern("V", "lex-mod", null));
    MINIMUM_TEMPLATES.add(toPattern("V", "mod", "A"));
    MINIMUM_TEMPLATES.add(toPattern("V", "mod", "Prep"));
    MINIMUM_TEMPLATES.add(toPattern("V", "obj", "N"));
    MINIMUM_TEMPLATES.add(toPattern("V", "subj", "N"));
  }
  





  public MinimumMiniparTemplateAcceptor() {}
  




  public boolean accepts(DependencyPath path)
  {
    return acceptsInternal(path);
  }
  









  static boolean acceptsInternal(DependencyPath path)
  {
    if (path.length() != 2) {
      return false;
    }
    


    if ((path.getNode(0).word().equals("")) || 
      (path.getNode(0).word().equals(""))) {
      return false;
    }
    String pos1 = path.getNode(0).pos();
    String rel = path.getRelation(0);
    String pos2 = path.getNode(1).pos();
    
    return MINIMUM_TEMPLATES.contains(toPattern(pos1, rel, pos2));
  }
  


  public int maxPathLength()
  {
    return 2;
  }
  


  static String toPattern(String pos1, String rel, String pos2)
  {
    return pos1 + ":" + rel + ":" + pos2;
  }
}
