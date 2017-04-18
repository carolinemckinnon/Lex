package edu.ucla.sspace.dv;

import edu.ucla.sspace.dependency.DependencyPath;
import edu.ucla.sspace.dependency.DependencyPathAcceptor;
import edu.ucla.sspace.dependency.DependencyTreeNode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;







































public class MinimumPennTemplateAcceptor
  implements DependencyPathAcceptor
{
  static final Set<String> MINIMUM_TEMPLATES = new HashSet();
  





  static final Map<String, String> POS_TAG_TO_CLASS = new HashMap();
  



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
    





    MINIMUM_TEMPLATES.add(toPattern("R", "VMOD", "V"));
    MINIMUM_TEMPLATES.add(toPattern("R", "AMOD", "J"));
    MINIMUM_TEMPLATES.add(toPattern("R", "PROD", "V"));
    MINIMUM_TEMPLATES.add(toPattern("R", "PMOD", "V"));
    MINIMUM_TEMPLATES.add(toPattern("R", "ADV", "V"));
    MINIMUM_TEMPLATES.add(toPattern("R", "ADV", "N"));
    MINIMUM_TEMPLATES.add(toPattern("R", "ADV", "J"));
    
    MINIMUM_TEMPLATES.add(toPattern("J", "NMOD", "N"));
    MINIMUM_TEMPLATES.add(toPattern("J", "NMOD", "TO"));
    MINIMUM_TEMPLATES.add(toPattern("J", "PMOD", "TO"));
    MINIMUM_TEMPLATES.add(toPattern("J", "SBJ", "N"));
    
    MINIMUM_TEMPLATES.add(toPattern("N", "COORD", "N"));
    MINIMUM_TEMPLATES.add(toPattern("N", "PROD", "N"));
    MINIMUM_TEMPLATES.add(toPattern("N", "NMOD", "J"));
    MINIMUM_TEMPLATES.add(toPattern("N", "NMOD", "R"));
    MINIMUM_TEMPLATES.add(toPattern("N", "NMOD", "TO"));
    MINIMUM_TEMPLATES.add(toPattern("N", "NMOD", "N"));
    MINIMUM_TEMPLATES.add(toPattern("N", "OBJ", "V"));
    MINIMUM_TEMPLATES.add(toPattern("N", "SBJ", "J"));
    MINIMUM_TEMPLATES.add(toPattern("N", "SBJ", "R"));
    MINIMUM_TEMPLATES.add(toPattern("N", "SBJ", "N"));
    MINIMUM_TEMPLATES.add(toPattern("N", "SBJ", "V"));
    MINIMUM_TEMPLATES.add(toPattern("N", "ADV", "N"));
    




    MINIMUM_TEMPLATES.add(toPattern("TO", "AMOD", "J"));
    MINIMUM_TEMPLATES.add(toPattern("TO", "NMOD", "N"));
    MINIMUM_TEMPLATES.add(toPattern("TO", "VMOD", "V"));
    
    MINIMUM_TEMPLATES.add(toPattern("TO", "PMOD", "N"));
    MINIMUM_TEMPLATES.add(toPattern("TO", "ADV", "N"));
    MINIMUM_TEMPLATES.add(toPattern("TO", "ADV", "V"));
    MINIMUM_TEMPLATES.add(toPattern("TO", "ADV", "R"));
    MINIMUM_TEMPLATES.add(toPattern("TO", "ADV", "J"));
    

    MINIMUM_TEMPLATES.add(toPattern("IN", "AMOD", "J"));
    MINIMUM_TEMPLATES.add(toPattern("IN", "NMOD", "N"));
    MINIMUM_TEMPLATES.add(toPattern("IN", "VMOD", "V"));
    
    MINIMUM_TEMPLATES.add(toPattern("IN", "PMOD", "N"));
    MINIMUM_TEMPLATES.add(toPattern("IN", "ADV", "N"));
    MINIMUM_TEMPLATES.add(toPattern("IN", "ADV", "V"));
    MINIMUM_TEMPLATES.add(toPattern("IN", "ADV", "R"));
    MINIMUM_TEMPLATES.add(toPattern("IN", "ADV", "J"));
    

    MINIMUM_TEMPLATES.add(toPattern("V", "AMOD", "R"));
    MINIMUM_TEMPLATES.add(toPattern("V", "VMOD", "R"));
    
    MINIMUM_TEMPLATES.add(toPattern("V", "VMOD", "J"));
    MINIMUM_TEMPLATES.add(toPattern("V", "AMOD", "J"));
    MINIMUM_TEMPLATES.add(toPattern("V", "PMOD", "TO"));
    MINIMUM_TEMPLATES.add(toPattern("V", "OBJ", "N"));
    MINIMUM_TEMPLATES.add(toPattern("V", "SBJ", "N"));
    MINIMUM_TEMPLATES.add(toPattern("V", "ADV", "N"));
    MINIMUM_TEMPLATES.add(toPattern("V", "ADV", "R"));
    MINIMUM_TEMPLATES.add(toPattern("V", "ADV", "V"));
    MINIMUM_TEMPLATES.add(toPattern("V", "ADV", "J"));
  }
  





  public MinimumPennTemplateAcceptor() {}
  




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
    





    String class1 = (String)POS_TAG_TO_CLASS.get(pos1);
    String class2 = (String)POS_TAG_TO_CLASS.get(pos2);
    String pattern = toPattern(class1 == null ? pos1 : class1, rel, 
      class2 == null ? pos2 : class2);
    return MINIMUM_TEMPLATES.contains(pattern);
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
