package edu.ucla.sspace.text;

import edu.ucla.sspace.dependency.DependencyExtractor;
import edu.ucla.sspace.dependency.DependencyTreeNode;
import java.io.IOError;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




























public class LabeledParsedStringDocument
  extends LabeledStringDocument
  implements LabeledParsedDocument
{
  private final DependencyTreeNode[] nodes;
  
  public LabeledParsedStringDocument(String label, DependencyExtractor extractor, String parse)
  {
    super(label, parse);
    try {
      nodes = extractor.readNextTree(reader());

    }
    catch (IOException ioe)
    {
      throw new IOError(ioe);
    }
  }
  


  public DependencyTreeNode[] parsedDocument()
  {
    return nodes;
  }
  


  public String text()
  {
    StringBuilder sb = new StringBuilder(nodes.length * 8);
    for (int i = 0; i < nodes.length; i++) {
      String token = nodes[i].word();
      sb.append(token);
      if (i + 1 < nodes.length)
        sb.append(' ');
    }
    return sb.toString();
  }
  


  public String prettyPrintText()
  {
    Pattern punctuation = Pattern.compile("[!,-.:;?`]");
    StringBuilder sb = new StringBuilder(nodes.length * 8);
    boolean evenSingleQuote = false;
    boolean evenDoubleQuote = false;
    
    boolean skipSpace = false;
    for (int i = 0; i < nodes.length; i++) {
      String token = nodes[i].word();
      
      if (i == 0) {
        sb.append(token);




      }
      else if ((punctuation.matcher(nodes[i].pos()).matches()) || 
        (punctuation.matcher(token).matches()) || 
        (token.equals(".")) || 
        (token.equals("n't")) || 
        (token.equals("'m")) || 
        (token.equals("'ll")) || 
        (token.equals("'re")) || 
        (token.equals("'ve")) || 
        (token.equals("'s"))) {
        sb.append(token);
      } else if (token.equals("'")) {
        if (evenSingleQuote) {
          sb.append(token);
        } else {
          sb.append(' ').append(token);
          skipSpace = true;
        }
        evenSingleQuote = !evenSingleQuote;
      }
      else if (token.equals("\"")) {
        if (evenDoubleQuote) {
          sb.append(token);
        } else {
          sb.append(' ').append(token);
          skipSpace = true;
        }
        evenDoubleQuote = !evenDoubleQuote;
      }
      else if (token.equals("$")) {
        sb.append(' ').append(token);
        skipSpace = true;


      }
      else if (skipSpace) {
        sb.append(token);
        skipSpace = false;
      }
      else {
        sb.append(' ').append(token);
      }
    }
    
    return sb.toString();
  }
}
