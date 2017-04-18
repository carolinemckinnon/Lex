package edu.ucla.sspace.text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;




























public class PatPho
{
  private static final Map<String, double[]> PHONEME_VALUES = new HashMap();
  



  private static final Set<String> VOWELS = new HashSet();
  



  private static final Set<String> CONSONANTS = new HashSet();
  



  private final int[] consonantIndices;
  



  private final int[] vowelIndices;
  




  public PatPho()
  {
    this(true);
  }
  






  public PatPho(boolean useSixSyllables)
  {
    int numConsonants = (useSixSyllables ? 18 : 9) + 3;
    


    int numVowels = useSixSyllables ? 12 : 6;
    


    consonantIndices = new int[numConsonants];
    int vectorOffset = 0;
    for (int i = 0; i < numConsonants; vectorOffset++) {
      if (i % 3 == 0)
        vectorOffset += 2;
      consonantIndices[i] = vectorOffset;i++;
    }
    


    vowelIndices = new int[numVowels];
    vectorOffset = 4;
    for (int i = 0; i < numVowels; vectorOffset++) {
      if (i % 2 == 0)
        vectorOffset += 3;
      vowelIndices[i] = vectorOffset;i++;
    }
  }
  









  public double[] vectorize(String phoneme)
  {
    return Arrays.copyOfRange((double[])PHONEME_VALUES.get(phoneme), 0, 3);
  }
  













  public double[] vectorize(List<String> phonemes)
  {
    int nextConsonantIndex = 0;
    int nextVowelIndex = 0;
    double[] result = new double[(vowelIndices.length + 
      consonantIndices.length) * 3];
    int i; for (Iterator localIterator = phonemes.iterator(); localIterator.hasNext(); 
        





        i < 3)
    {
      String phoneme = (String)localIterator.next();
      int offset = 3;
      if (VOWELS.contains(phoneme)) {
        offset *= vowelIndices[(nextVowelIndex++)];
      } else
        offset *= consonantIndices[(nextConsonantIndex++)];
      double[] values = (double[])PHONEME_VALUES.get(phoneme);
      i = 0; continue;
      result[(i + offset)] = values[i];i++;
    }
    
    return result;
  }
  



  static
  {
    PHONEME_VALUES.put("i", new double[] { 0.1D, 0.1D, 0.1D });
    PHONEME_VALUES.put("I", new double[] { 0.1D, 0.1D, 0.185D });
    PHONEME_VALUES.put("e", new double[] { 0.1D, 0.1D, 0.27D });
    PHONEME_VALUES.put("E", new double[] { 0.1D, 0.1D, 0.355D });
    PHONEME_VALUES.put("&", new double[] { 0.1D, 0.1D, 0.444D });
    PHONEME_VALUES.put("@", new double[] { 0.1D, 0.175D, 0.185D });
    PHONEME_VALUES.put("3", new double[] { 0.1D, 0.175D, 0.27D });
    PHONEME_VALUES.put("V", new double[] { 0.1D, 0.175D, 0.355D });
    PHONEME_VALUES.put("a", new double[] { 0.1D, 0.175D, 0.444D });
    PHONEME_VALUES.put("u", new double[] { 0.1D, 0.25D, 0.1D });
    PHONEME_VALUES.put("U", new double[] { 0.1D, 0.25D, 0.185D });
    PHONEME_VALUES.put("O", new double[] { 0.1D, 0.25D, 0.27D });
    PHONEME_VALUES.put("Q", new double[] { 0.1D, 0.25D, 0.355D });
    PHONEME_VALUES.put("A", new double[] { 0.1D, 0.25D, 0.444D });
    

    VOWELS.add("i");
    VOWELS.add("I");
    VOWELS.add("e");
    VOWELS.add("E");
    VOWELS.add("&");
    VOWELS.add("@");
    VOWELS.add("3");
    VOWELS.add("V");
    VOWELS.add("a");
    VOWELS.add("u");
    VOWELS.add("U");
    VOWELS.add("O");
    VOWELS.add("Q");
    VOWELS.add("A");
    

    PHONEME_VALUES.put("p", new double[] { 1.0D, 0.45D, 0.733D });
    PHONEME_VALUES.put("t", new double[] { 1.0D, 0.684D, 0.733D });
    PHONEME_VALUES.put("k", new double[] { 1.0D, 0.921D, 0.733D });
    PHONEME_VALUES.put("b", new double[] { 0.75D, 0.45D, 0.733D });
    PHONEME_VALUES.put("d", new double[] { 0.75D, 0.684D, 0.733D });
    PHONEME_VALUES.put("g", new double[] { 0.75D, 0.921D, 0.733D });
    PHONEME_VALUES.put("m", new double[] { 0.75D, 0.45D, 0.644D });
    PHONEME_VALUES.put("n", new double[] { 0.75D, 0.684D, 0.644D });
    PHONEME_VALUES.put("N", new double[] { 0.75D, 0.921D, 0.644D });
    PHONEME_VALUES.put("l", new double[] { 0.75D, 0.684D, 1.0D });
    PHONEME_VALUES.put("r", new double[] { 0.75D, 0.684D, 0.911D });
    PHONEME_VALUES.put("f", new double[] { 1.0D, 0.528D, 0.822D });
    PHONEME_VALUES.put("v", new double[] { 0.75D, 0.528D, 0.822D });
    PHONEME_VALUES.put("s", new double[] { 1.0D, 0.684D, 0.822D });
    PHONEME_VALUES.put("z", new double[] { 0.75D, 0.684D, 0.822D });
    PHONEME_VALUES.put("S", new double[] { 1.0D, 0.792D, 0.822D });
    PHONEME_VALUES.put("Z", new double[] { 0.75D, 0.792D, 0.822D });
    PHONEME_VALUES.put("j", new double[] { 0.75D, 0.841D, 0.911D });
    PHONEME_VALUES.put("h", new double[] { 1.0D, 1.0D, 0.911D });
    PHONEME_VALUES.put("w", new double[] { 0.75D, 0.921D, 0.911D });
    PHONEME_VALUES.put("T", new double[] { 1.0D, 0.606D, 0.822D });
    PHONEME_VALUES.put("D", new double[] { 0.75D, 0.606D, 0.822D });
    PHONEME_VALUES.put("C", new double[] { 1.0D, 0.841D, 0.822D });
    PHONEME_VALUES.put("J", new double[] { 0.75D, 0.841D, 0.822D });
    

    CONSONANTS.add("p");
    CONSONANTS.add("t");
    CONSONANTS.add("k");
    CONSONANTS.add("b");
    CONSONANTS.add("d");
    CONSONANTS.add("g");
    CONSONANTS.add("m");
    CONSONANTS.add("n");
    CONSONANTS.add("N");
    CONSONANTS.add("l");
    CONSONANTS.add("r");
    CONSONANTS.add("f");
    CONSONANTS.add("v");
    CONSONANTS.add("s");
    CONSONANTS.add("z");
    CONSONANTS.add("S");
    CONSONANTS.add("Z");
    CONSONANTS.add("j");
    CONSONANTS.add("h");
    CONSONANTS.add("w");
    CONSONANTS.add("T");
    CONSONANTS.add("D");
    CONSONANTS.add("C");
    CONSONANTS.add("J");
  }
}
