package edu.ucla.sspace.text;



















public class LabeledStringDocument
  extends StringDocument
  implements LabeledDocument
{
  private final String label;
  


















  public LabeledStringDocument(String label, String docText)
  {
    super(docText);
    this.label = label;
  }
  


  public String label()
  {
    return label;
  }
}
