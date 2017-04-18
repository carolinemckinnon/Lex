package edu.ucla.sspace.dv;

import java.util.HashSet;
import java.util.Set;



























public final class PennTags
{
  public static final Set<String> NOUN_POS_TAGS = new HashSet();
  public static final Set<String> VERB_POS_TAGS = new HashSet();
  public static final Set<String> ADJ_POS_TAGS = new HashSet();
  public static final Set<String> ADV_POS_TAGS = new HashSet();
  
  public static final Set<String> MODIFIERS = new HashSet();
  
  static {
    ADJ_POS_TAGS.add("JJ");
    ADJ_POS_TAGS.add("JJR");
    ADJ_POS_TAGS.add("JJS");
    
    ADV_POS_TAGS.add("RB");
    ADV_POS_TAGS.add("RBR");
    ADV_POS_TAGS.add("RBS");
    
    NOUN_POS_TAGS.add("NN");
    NOUN_POS_TAGS.add("NNS");
    NOUN_POS_TAGS.add("NP");
    NOUN_POS_TAGS.add("NPS");
    
    VERB_POS_TAGS.add("VB");
    VERB_POS_TAGS.add("VBD");
    VERB_POS_TAGS.add("VBG");
    VERB_POS_TAGS.add("VBN");
    VERB_POS_TAGS.add("VBP");
    VERB_POS_TAGS.add("VBZ");
    VERB_POS_TAGS.add("VH");
    VERB_POS_TAGS.add("VHD");
    VERB_POS_TAGS.add("VHG");
    VERB_POS_TAGS.add("VHN");
    VERB_POS_TAGS.add("VHP");
    VERB_POS_TAGS.add("VHZ");
    VERB_POS_TAGS.add("VV");
    VERB_POS_TAGS.add("VVD");
    VERB_POS_TAGS.add("VVG");
    VERB_POS_TAGS.add("VVN");
    VERB_POS_TAGS.add("VVP");
    VERB_POS_TAGS.add("VVZ");
  }
  

  public PennTags() {}
  
  public static boolean isAdjective(String posTag)
  {
    return ADJ_POS_TAGS.contains(posTag);
  }
  



  public static boolean isAdverb(String posTag)
  {
    return ADV_POS_TAGS.contains(posTag);
  }
  



  public static boolean isNoun(String posTag)
  {
    return NOUN_POS_TAGS.contains(posTag);
  }
  



  public static boolean isVerb(String posTag)
  {
    return VERB_POS_TAGS.contains(posTag);
  }
}
