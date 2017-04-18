package edu.ucla.sspace.text;








public class PorterStemmer
  implements Stemmer
{
  private StringBuilder sb;
  






  private char[] b;
  






  private int j;
  






  private int k;
  






  public PorterStemmer() {}
  






  public synchronized String stem(String token)
  {
    sb = new StringBuilder(token);
    k = (sb.length() - 1);
    if (k > 1) {
      step1();
      step2();
      step3();
      step4();
      step5();
      step6();
    }
    sb.delete(k + 1, sb.length());
    return sb.toString();
  }
  


  private final boolean cons(int i)
  {
    switch (sb.charAt(i)) {
    case 'a': 
    case 'e': 
    case 'i': 
    case 'o': 
    case 'u': 
      return false;
    case 'y': 
      return i == 0;
    }
    return true;
  }
  











  private final int m()
  {
    int n = 0;
    for (int i = 0; 
        cons(i); i++) {
      if (i > j)
        return n;
    }
    i++;
    while (i <= j) {
      for (; !cons(i); i++) {
        if (i > j)
          return n;
      }
      i++;
      n++;
      
      if (i > j)
        return n;
      for (; cons(i); i++) {
        if (i > j)
          return n;
      }
      i++;
    }
    return n;
  }
  

  private final boolean vowelinstem()
  {
    for (int i = 0; i <= j; i++)
      if (!cons(i))
        return true;
    return false;
  }
  

  private final boolean doublec(int j)
  {
    if (j < 1)
      return false;
    if (sb.charAt(j) != sb.charAt(j - 1))
      return false;
    return cons(j);
  }
  







  private final boolean cvc(int i)
  {
    if ((i < 2) || (!cons(i)) || (cons(i - 1)) || (!cons(i - 2)))
      return false;
    char ch = sb.charAt(i);
    return (ch != 'w') && (ch != 'x') && (ch != 'y');
  }
  
  private final boolean ends(String s) {
    int l = s.length();
    int o = k - l + 1;
    if (o < 0)
      return false;
    for (int i = 0; i < l; i++)
      if (sb.charAt(o + i) != s.charAt(i))
        return false;
    j = (k - l);
    return true;
  }
  


  private final void setto(String s)
  {
    int l = s.length();
    int o = j + 1;
    for (int i = 0; i < l; i++)
      sb.setCharAt(o + i, s.charAt(i));
    k = (j + l);
  }
  

  private final void r(String s)
  {
    if (m() > 0) {
      setto(s);
    }
  }
  



















  private final void step1()
  {
    if (sb.charAt(k) == 's') {
      if (ends("sses")) {
        k -= 2;
      } else if (ends("ies")) {
        setto("i");
      } else if (sb.charAt(k - 1) != 's')
        k -= 1;
    }
    if (ends("eed")) {
      if (m() > 0)
        k -= 1;
    } else if (((ends("ed")) || (ends("ing"))) && (vowelinstem())) {
      k = j;
      if (ends("at")) {
        setto("ate");
      } else if (ends("bl")) {
        setto("ble");
      } else if (ends("iz")) {
        setto("ize");
      } else if (doublec(k)) {
        k -= 1;
        char ch = sb.charAt(k);
        if ((ch == 'l') || (ch == 's') || (ch == 'z'))
          k += 1;
      } else if ((m() == 1) && (cvc(k))) {
        setto("e");
      }
    }
  }
  
  private final void step2() {
    if ((ends("y")) && (vowelinstem())) {
      sb.setCharAt(k, 'i');
    }
  }
  

  private final void step3()
  {
    if (k == 0) {
      return;
    }
    switch (sb.charAt(k - 1)) {
    case 'a': 
      if (ends("ational")) {
        r("ate");
      } else if (ends("tional"))
        r("tion");
      break;
    case 'c': 
      if (ends("enci")) {
        r("ence");
      } else if (ends("anci"))
        r("ance");
      break;
    case 'e': 
      if (ends("izer"))
        r("ize");
      break;
    case 'l': 
      if (ends("bli")) {
        r("ble");
      } else if (ends("alli")) {
        r("al");
      } else if (ends("entli")) {
        r("ent");
      } else if (ends("eli")) {
        r("e");
      } else if (ends("ousli"))
        r("ous");
      break;
    case 'o': 
      if (ends("ization")) {
        r("ize");
      } else if (ends("ation")) {
        r("ate");
      } else if (ends("ator"))
        r("ate");
      break;
    case 's': 
      if (ends("alism")) {
        r("al");
      } else if (ends("iveness")) {
        r("ive");
      } else if (ends("fulness")) {
        r("ful");
      } else if (ends("ousness"))
        r("ous");
      break;
    case 't': 
      if (ends("aliti")) {
        r("al");
      } else if (ends("iviti")) {
        r("ive");
      } else if (ends("biliti"))
        r("ble");
      break;
    case 'g': 
      if (ends("logi")) {
        r("log");
      }
      break;
    }
  }
  
  private final void step4() {
    switch (sb.charAt(k)) {
    case 'e': 
      if (ends("icate")) {
        r("ic");
      } else if (ends("ative")) {
        r("");
      } else if (ends("alize"))
        r("al");
      break;
    case 'i': 
      if (ends("iciti"))
        r("ic");
      break;
    case 'l': 
      if (ends("ical")) {
        r("ic");
      } else if (ends("ful"))
        r("");
      break;
    case 's': 
      if (ends("ness")) { r("");
      }
      break;
    }
  }
  
  private final void step5()
  {
    if (k == 0) {
      return;
    }
    switch (sb.charAt(k - 1)) {
    case 'a': 
      if (!ends("al"))
        return;
      break;
    case 'c': 
      if (!ends("ance"))
      {
        if (!ends("ence"))
          return; }
      break;
    case 'e': 
      if (!ends("er"))
        return;
      break;
    case 'i': 
      if (!ends("ic"))
        return;
      break;
    case 'l': 
      if (!ends("able"))
      {
        if (!ends("ible"))
          return; }
      break;
    case 'n': 
      if (!ends("ant"))
      {
        if (!ends("ement"))
        {
          if (!ends("ment"))
          {

            if (!ends("ent"))
              return; } } }
      break;
    case 'o': 
      if ((!ends("ion")) || (j < 0) || ((b[j] != 's') && (b[j] != 't')))
      {

        if (!ends("ou"))
          return;
      }
      break;
    case 's': 
      if (!ends("ism"))
        return;
      break;
    case 't': 
      if (!ends("ate"))
      {
        if (!ends("iti"))
          return; }
      break;
    case 'u': 
      if (!ends("ous"))
        return;
      break;
    case 'v': 
      if (!ends("ive"))
        return;
      break;
    case 'z': 
      if (!ends("ize"))
        return;
      break;
    case 'b': case 'd': case 'f': case 'g': case 'h': case 'j': case 'k': case 'm': case 'p': case 'q': case 'r': case 'w': case 'x': case 'y': default: 
      return;
    }
    if (m() > 1) { k = j;
    }
  }
  
  private final void step6()
  {
    j = k;
    if (sb.charAt(k) == 'e') {
      int a = m();
      if ((a > 1) || ((a == 1) && (!cvc(k - 1))))
        k -= 1;
    }
    if ((sb.charAt(k) == 'l') && (doublec(k)) && (m() > 1)) {
      k -= 1;
    }
  }
}
