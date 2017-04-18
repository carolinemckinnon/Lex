package edu.ucla.sspace.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;







































public class StringUtils
{
  private static final Map<String, String> HTML_CODES = new HashMap();
  

  private static final Map<String, String> LATIN1_CODES = new HashMap();
  
  static
  {
    HTML_CODES.put("&nbsp;", " ");
    HTML_CODES.put("&Agrave;", "À");
    HTML_CODES.put("&Aacute;", "Á");
    HTML_CODES.put("&Acirc;", "Â");
    HTML_CODES.put("&Atilde;", "Ã");
    HTML_CODES.put("&Auml;", "Ä");
    HTML_CODES.put("&Aring;", "Å");
    HTML_CODES.put("&AElig;", "Æ");
    HTML_CODES.put("&Ccedil;", "Ç");
    HTML_CODES.put("&Egrave;", "È");
    HTML_CODES.put("&Eacute;", "É");
    HTML_CODES.put("&Ecirc;", "Ê");
    HTML_CODES.put("&Euml;", "Ë");
    HTML_CODES.put("&Igrave;", "Ì");
    HTML_CODES.put("&Iacute;", "Í");
    HTML_CODES.put("&Icirc;", "Î");
    HTML_CODES.put("&Iuml;", "Ï");
    HTML_CODES.put("&ETH;", "Ð");
    HTML_CODES.put("&Ntilde;", "Ñ");
    HTML_CODES.put("&Ograve;", "Ò");
    HTML_CODES.put("&Oacute;", "Ó");
    HTML_CODES.put("&Ocirc;", "Ô");
    HTML_CODES.put("&Otilde;", "Õ");
    HTML_CODES.put("&Ouml;", "Ö");
    HTML_CODES.put("&Oslash;", "Ø");
    HTML_CODES.put("&Ugrave;", "Ù");
    HTML_CODES.put("&Uacute;", "Ú");
    HTML_CODES.put("&Ucirc;", "Û");
    HTML_CODES.put("&Uuml;", "Ü");
    HTML_CODES.put("&Yacute;", "Ý");
    HTML_CODES.put("&THORN;", "Þ");
    HTML_CODES.put("&szlig;", "ß");
    HTML_CODES.put("&agrave;", "à");
    HTML_CODES.put("&aacute;", "á");
    HTML_CODES.put("&acirc;", "â");
    HTML_CODES.put("&atilde;", "ã");
    HTML_CODES.put("&auml;", "ä");
    HTML_CODES.put("&aring;", "å");
    HTML_CODES.put("&aelig;", "æ");
    HTML_CODES.put("&ccedil;", "ç");
    HTML_CODES.put("&egrave;", "è");
    HTML_CODES.put("&eacute;", "é");
    HTML_CODES.put("&ecirc;", "ê");
    HTML_CODES.put("&euml;", "ë");
    HTML_CODES.put("&igrave;", "ì");
    HTML_CODES.put("&iacute;", "í");
    HTML_CODES.put("&icirc;", "î");
    HTML_CODES.put("&iuml;", "ï");
    HTML_CODES.put("&eth;", "ð");
    HTML_CODES.put("&ntilde;", "ñ");
    HTML_CODES.put("&ograve;", "ò");
    HTML_CODES.put("&oacute;", "ó");
    HTML_CODES.put("&ocirc;", "ô");
    HTML_CODES.put("&otilde;", "õ");
    HTML_CODES.put("&ouml;", "ö");
    HTML_CODES.put("&oslash;", "ø");
    HTML_CODES.put("&ugrave;", "ù");
    HTML_CODES.put("&uacute;", "ú");
    HTML_CODES.put("&ucirc;", "û");
    HTML_CODES.put("&uuml;", "ü");
    HTML_CODES.put("&yacute;", "ý");
    HTML_CODES.put("&thorn;", "þ");
    HTML_CODES.put("&yuml;", "ÿ");
    HTML_CODES.put("&lt;", "<");
    HTML_CODES.put("&gt;", ">");
    HTML_CODES.put("&quot;", "\"");
    HTML_CODES.put("&amp;", "&");
    
    LATIN1_CODES.put("&#039;", "'");
    LATIN1_CODES.put("&#160;", " ");
    LATIN1_CODES.put("&#162;", "¢");
    LATIN1_CODES.put("&#164;", "¤");
    LATIN1_CODES.put("&#166;", "¦");
    LATIN1_CODES.put("&#168;", "¨");
    LATIN1_CODES.put("&#170;", "ª");
    LATIN1_CODES.put("&#172;", "¬");
    LATIN1_CODES.put("&#174;", "®");
    LATIN1_CODES.put("&#176;", "°");
    LATIN1_CODES.put("&#178;", "²");
    LATIN1_CODES.put("&#180;", "´");
    LATIN1_CODES.put("&#182;", "¶");
    LATIN1_CODES.put("&#184;", "¸");
    LATIN1_CODES.put("&#186;", "º");
    LATIN1_CODES.put("&#188;", "¼");
    LATIN1_CODES.put("&#190;", "¾");
    LATIN1_CODES.put("&#192;", "À");
    LATIN1_CODES.put("&#194;", "Â");
    LATIN1_CODES.put("&#196;", "Ä");
    LATIN1_CODES.put("&#198;", "Æ");
    LATIN1_CODES.put("&#200;", "È");
    LATIN1_CODES.put("&#202;", "Ê");
    LATIN1_CODES.put("&#204;", "Ì");
    LATIN1_CODES.put("&#206;", "Î");
    LATIN1_CODES.put("&#208;", "Ð");
    LATIN1_CODES.put("&#210;", "Ò");
    LATIN1_CODES.put("&#212;", "Ô");
    LATIN1_CODES.put("&#214;", "Ö");
    LATIN1_CODES.put("&#216;", "Ø");
    LATIN1_CODES.put("&#218;", "Ú");
    LATIN1_CODES.put("&#220;", "Ü");
    LATIN1_CODES.put("&#222;", "Þ");
    LATIN1_CODES.put("&#224;", "à");
    LATIN1_CODES.put("&#226;", "â");
    LATIN1_CODES.put("&#228;", "ä");
    LATIN1_CODES.put("&#230;", "æ");
    LATIN1_CODES.put("&#232;", "è");
    LATIN1_CODES.put("&#234;", "ê");
    LATIN1_CODES.put("&#236;", "ì");
    LATIN1_CODES.put("&#238;", "î");
    LATIN1_CODES.put("&#240;", "ð");
    LATIN1_CODES.put("&#242;", "ò");
    LATIN1_CODES.put("&#244;", "ô");
    LATIN1_CODES.put("&#246;", "ö");
    LATIN1_CODES.put("&#248;", "ø");
    LATIN1_CODES.put("&#250;", "ú");
    LATIN1_CODES.put("&#252;", "ü");
    LATIN1_CODES.put("&#254;", "þ");
    LATIN1_CODES.put("&#34;", "\"");
    LATIN1_CODES.put("&#38;", "&");
    LATIN1_CODES.put("&#8217;", "'");
  }
  

  private StringUtils() {}
  
  public static List<String> loadFileAsList(File f)
  {
    try
    {
      List<String> s = new ArrayList();
      BufferedReader br = new BufferedReader(new FileReader(f));
      for (String line = null; (line = br.readLine()) != null;)
        s.add(line);
      br.close();
      return s;
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  




  public static Set<String> loadFileAsSet(File f)
  {
    try
    {
      Set<String> s = new HashSet();
      BufferedReader br = new BufferedReader(new FileReader(f));
      for (String line = null; (line = br.readLine()) != null;)
        s.add(line);
      br.close();
      return s;
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  






  public static final String unescapeHTML(String source)
  {
    StringBuilder sb = new StringBuilder(source.length());
    

    int start = -1;int end = -1;
    

    int last = 0;
    
    start = source.indexOf("&");
    end = source.indexOf(";", start);
    
    while ((start > -1) && (end > start)) {
      String encoded = source.substring(start, end + 1);
      String decoded = (String)HTML_CODES.get(encoded);
      


      if (decoded == null) {
        decoded = (String)LATIN1_CODES.get(encoded);
      }
      
      if (decoded != null)
      {

        String s = source.substring(last, start);
        sb.append(s).append(decoded);
        last = end + 1;
      }
      
      start = source.indexOf("&", end);
      end = source.indexOf(";", start);
    }
    
    if (sb.length() == 0) {
      return source;
    }
    

    sb.append(source.substring(last));
    return sb.toString();
  }
  







  public static final void unescapeHTML(StringBuilder source)
  {
    int start = -1;int end = -1;
    

    int last = 0;
    
    start = source.indexOf("&");
    end = source.indexOf(";", start);
    
    while ((start > -1) && (end > start)) {
      String encoded = source.substring(start, end + 1);
      String decoded = (String)HTML_CODES.get(encoded);
      


      if (decoded == null) {
        decoded = (String)LATIN1_CODES.get(encoded);
      }
      


      if (decoded != null) {
        source.replace(start, end + 1, decoded);
      }
      



      start = source.indexOf("&", start + 1);
      end = source.indexOf(";", start);
    }
  }
}
