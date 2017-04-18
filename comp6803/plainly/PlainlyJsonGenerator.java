package comp6803.plainly;

import edu.ucla.sspace.basis.StringBasisMapping;
import edu.ucla.sspace.lsa.LatentSemanticAnalysis;
import edu.ucla.sspace.matrix.CorrelationTransform;
import edu.ucla.sspace.matrix.LogEntropyTransform;
import edu.ucla.sspace.matrix.LogLikelihoodTransform;
import edu.ucla.sspace.matrix.NoTransform;
import edu.ucla.sspace.matrix.PointWiseMutualInformationTransform;
import edu.ucla.sspace.matrix.RowMagnitudeTransform;
import edu.ucla.sspace.matrix.TfIdfDocStripedTransform;
import edu.ucla.sspace.matrix.TfIdfTransform;
import edu.ucla.sspace.matrix.Transform;
import edu.ucla.sspace.matrix.factorization.AbstractSvd;
import edu.ucla.sspace.matrix.factorization.SingularValueDecompositionJAMA;
import edu.ucla.sspace.text.EnglishStemmer;
import edu.ucla.sspace.text.Stemmer;
import edu.ucla.sspace.text.StringDocument;
import edu.ucla.sspace.vector.DoubleVector;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.BreakIterator;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.Document;

public class PlainlyJsonGenerator extends JFrame
{
  private static PlainlyJsonGenerator plainly = null;
  private LatentSemanticAnalysis lsa;
  private File corpus;
  private String json = "";
  private Boolean lsaTypeRecurrence = Boolean.valueOf(true);
  private String documentFileName = "";
  private Charset documentEncoding = StandardCharsets.US_ASCII;
  private String matlabDirectory = "";
  private List<Corpus> corpora = new ArrayList();
  private Corpus selectedCorpus;
  private Set<String> stopwords = new HashSet();
  

  private int settingsDimensions = 2;
  private String settingsTransform = "Log Entropy Transform";
  private AbstractSvd settingsSvd = new SingularValueDecompositionJAMA();
  private Boolean settingsIncludeParagraphs = Boolean.valueOf(false);
  private Boolean settingsIncludeWords = Boolean.valueOf(false);
  private double settingsTermThreshold = 0.0D;
  
  private File settingsStopwords;
  private JPanel panel;
  private JPanel panelEditor;
  private JPanel panelResult;
  private JTextArea editor;
  private JTextArea result;
  private PlainlyMenuBar menuBar;
  private static final int MIN_WIDTH = 1000;
  private static final int MIN_HEIGHT = 600;
  
  public PlainlyJsonGenerator()
  {
    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (ClassNotFoundException e1) {
      e1.printStackTrace();
    }
    catch (InstantiationException e1) {
      e1.printStackTrace();
    }
    catch (IllegalAccessException e1) {
      e1.printStackTrace();
    }
    catch (UnsupportedLookAndFeelException e1) {
      e1.printStackTrace();
    }
    
    if (!CorpusManager.getCorpusFolder().exists()) {
      CorpusManager.getCorpusFolder().mkdirs();
    }
    try {
      if (!CorpusManager.getCorpusSettingsFile().exists()) {
        CorpusManager.getCorpusSettingsFile().createNewFile();
      }
      if (!PlainlySettings.getSettingsFile().exists()) {
        PlainlySettings.getSettingsFile().createNewFile();
      }
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
    

    menuBar = new PlainlyMenuBar();
    setJMenuBar(menuBar);
    setSize(new Dimension(1000, 600));
    setVisible(true);
    setMinimumSize(new Dimension(1000, 600));
    setMaximumSize(new Dimension(1000, 600));
    
    setTitle("Plainly JSON Generator");
    

    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e) {
        System.out.println("Closing Application.");
        System.exit(0);
      }
      
    });
    panel = new JPanel();
    panel.setLayout(new FlowLayout());
    add(panel);
    
    panelEditor = new JPanel();
    panel.add(panelEditor, "West");
    editor = new JTextArea(28, 55);
    editor.setLineWrap(true);
    panelEditor.add(new JScrollPane(editor, 
      22, 31));
    
    panelResult = new JPanel();
    panel.add(panelResult, "East");
    result = new JTextArea(28, 55);
    result.setLineWrap(true);
    result.setEditable(false);
    panelResult.add(new JScrollPane(result, 
      22, 31));
    
    revalidate();
    repaint();
    populateCorpus();
    try
    {
      loadStopwords(new File("stopwords.txt"));
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
    System.out.println("Application initalised");
  }
  
  public void loadDocument() {
    JFileChooser chooser = new JFileChooser();
    chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
    if (chooser.showOpenDialog(null) == 0)
    {
      try
      {

        editor.setText(new String(Files.readAllBytes(Paths.get(chooser.getSelectedFile().getPath(), new String[0])), 
          getDocumentEncoding()));
        documentFileName = chooser.getSelectedFile().getName();
        System.out.println(chooser.getSelectedFile().getName() + " loaded.");
      } catch (IOException e) {
        System.err.println(e.getMessage());
        JOptionPane.showMessageDialog(null, e.getMessage());
      }
    }
  }
  
  public void clearDocument() {
    System.out.println("Document cleared.");
    editor.setText("");
    result.setText("");
    documentFileName = "";
    json = "";
  }
  


  public void setCorpus()
  {
    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("Select Corpus Folder");
    chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
    chooser.setFileSelectionMode(1);
    if (chooser.showOpenDialog(null) == 0) {
      System.out.println("Corpus set to: " + chooser.getSelectedFile().getName());
      corpus = chooser.getSelectedFile();
      menuBar.setCorpusLabel("Corpus: " + corpus.getAbsolutePath());
    }
  }
  



  public void clearCorpus()
  {
    System.out.println("Corpus Cleared.");
    menuBar.setCorpusLabel("No Corpus Selected.");
    corpus = null;
  }
  


  public void runLsa()
  {
    System.out.println("Running Latent Semantic Analysis...");
    menuBar.setCorpusLabel("Running Latent Semantic Analysis...");
    if ((editor.getText().trim() == null) || (editor.getText().trim().equals(""))) {
      JOptionPane.showMessageDialog(null, "No text.", "Error", 0);
      return;
    }
    result.setText("");
    List<String> paragraphs = new ArrayList(Arrays.asList(editor.getText().split("\\r?\\n", -1)));
    paragraphs.removeAll(Collections.singleton(null));
    paragraphs.removeAll(Collections.singleton(""));
    paragraphs.removeAll(Collections.singleton("\\n"));
    paragraphs.removeAll(Collections.singleton("\\r"));
    setCorpus(getMenu().getSelectedCorpus());
    System.out.println("Corpus " + getCorpus().getId() + " " + getCorpus().getName());
    System.out.println("Dimensions: " + getCorpus().getDimensions());
    getResultsText().append("Using " + getCorpus().getName() + " corpus with " + 
      getCorpus().getDocuments() + " documents using " + getCorpus().getDimensions() + " dimensions and " + 
      getCorpus().getTransform() + " transform.\n");
    try
    {
      lsa = new LatentSemanticAnalysis(false, getCorpus().getDimensions(), stringToTransform(settingsTransform), 
        new edu.ucla.sspace.matrix.factorization.SingularValueDecompositionSaved(), false, new StringBasisMapping());
    } catch (IOException e) {
      System.err.println(e.getMessage());
      JOptionPane.showMessageDialog(null, e.getMessage(), "Error", 0);
    }
    result.append("Processing Corpus...\n");
    menuBar.setCorpusLabel("Processing Documents...  20% ");
    result.setCaretPosition(result.getDocument().getLength());
    corpus = new File("corpora/example");
    
    System.out.println("Processing Space");
    menuBar.setCorpusLabel("Processing Semantic Space... 40% ");
    result.append("Processing Semantic Space...\n");
    result.setCaretPosition(result.getDocument().getLength());
    lsa.processTransform(getCorpus().getFileEntropy(), getCorpus().getFileTermDocumentIndex(), getCorpus().getFileU());
    menuBar.setCorpusLabel("Projecting Document onto Corpus... 60% ");
    result.append("Projecting onto Corpus...\n");
    

    List<String> sentences = new ArrayList();
    List<Integer> sentencesParagraphs = new ArrayList();
    for (int i = 0; i < paragraphs.size(); i++) {
      BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
      String text = (String)paragraphs.get(i);
      iterator.setText(text);
      start = iterator.first();
      for (int end = iterator.next(); end != -1; end = iterator.next()) {
        if ((!text.substring(start, end).equals("")) && (!text.substring(start, end).equals("\n")))
        {


          sentences.add(text.substring(start, end).trim());
          sentencesParagraphs.add(Integer.valueOf(i));
        }
        start = end;
      }
    }
    






    Set<String> words = new HashSet();
    Stemmer stemmer = new EnglishStemmer();
    StringTokenizer tokenizer; for (int start = sentences.iterator(); start.hasNext(); 
        
        tokenizer.hasMoreTokens())
    {
      String sentence = (String)start.next();
      tokenizer = new StringTokenizer(sentence);
      continue;
      if (getCorpus().getStemming().booleanValue()) {
        words.add(stemmer.stem(tokenizer.nextToken().replaceAll("[\\p{P}&&[^']]", "").toLowerCase()));
      } else {
        words.add(tokenizer.nextToken().replaceAll("[\\p{P}&&[^']]", "").toLowerCase());
      }
    }
    
    words.removeAll(getCorpus().getTerms());
    result.append(words.size() + " Terms Not Found In Corpus.\n");
    String message = words.size() + " Terms Not Found In Corpus.\n";
    for (String word : words) {
      result.append(word + " ");
      message = message + word + " ";
    }
    result.append(".\n");
    if (words.size() > 0) {
      JOptionPane.showMessageDialog(null, message, "Error", 0);
    }
    

    menuBar.setCorpusLabel("Generating Json... 80% ");
    result.append("Generating Json...\n");
    result.setCaretPosition(result.getDocument().getLength());
    try {
      json = generateJsonString(lsa, sentences, sentencesParagraphs, paragraphs);
    } catch (Exception e) {
      System.err.println(e.getMessage());
      System.err.println(e.getStackTrace().toString());
    }
    result.setText(json);
    
    if (!getCorpora().isEmpty()) {
      getMenu().setCorpusLabel(getCorpus().getName() + " (" + getCorpus().getDimensions() + " Dimensions)");
    }
  }
  






  private String generateJsonString(LatentSemanticAnalysis lsa, List<String> sentences, List<Integer> paragraphNumbers, List<String> paragraphs)
    throws Exception
  {
    String json = "";
    String results = "";
    

    json = json + "{\"nodes\":[";
    for (int i = 0; i < sentences.size(); i++) {
      json = 
        json + "{\"id\":" + i + ",\"name\":\"" + (String)sentences.get(i) + "\",\"group\":" + paragraphNumbers.get(i) + "},";
    }
    json = json.substring(0, json.length() - 1);
    
    DoubleVector[] documentProjections = new DoubleVector[sentences.size()];
    if (getCorpus().getStemming().booleanValue()) {
      Stemmer stemmer = new EnglishStemmer();
      System.out.println("Performing Stemming...");
      for (int i = 0; i < sentences.size(); i++) {
        sentences.set(i, stemmer.stem((String)sentences.get(i)));
      }
    }
    

    if (getLsaType().booleanValue())
    {
      json = json + "],\"links\":[";
      for (int i = 0; i < sentences.size(); i++) {
        for (int n = 0; n <= i; n++) {
          if (documentProjections[i] == null) {
            documentProjections[i] = lsa.project(new StringDocument((String)sentences.get(i)));
          }
          DoubleVector a = documentProjections[i];
          if (documentProjections[n] == null) {
            documentProjections[n] = lsa.project(new StringDocument((String)sentences.get(n)));
          }
          DoubleVector b = documentProjections[n];
          double similarity = Math.abs(cosineSimilarity(a, b));
          if (Double.isNaN(similarity)) {
            similarity = 0.0D;
          }
          System.out.println(new DecimalFormat("####0.00").format(similarity) + " : " + similarity + " : " + (String)sentences.get(i) + " | " + (String)sentences.get(n));
          results = results + new DecimalFormat("####0.00").format(similarity) + " ";
          json = json + "{\"source\":" + n + 
            ",\"target\":" + i + 
            ",\"value\":" + new DecimalFormat("####0.00").format(similarity) + "},";
        }
        results = results + "\n";
      }
    }
    else {
      json = json + "],\"links\":[";
      for (int i = 0; i < sentences.size(); i++) {
        if (i + 1 >= sentences.size()) {
          break;
        }
        if (documentProjections[i] == null) {
          documentProjections[i] = lsa.project(new StringDocument((String)sentences.get(i)));
        }
        DoubleVector a = documentProjections[i];
        if (documentProjections[i] == null) {
          documentProjections[(i + 1)] = lsa.project(new StringDocument((String)sentences.get(i + 1)));
        }
        DoubleVector b = documentProjections[(i + 1)];
        double similarity = Math.abs(cosineSimilarity(a, b));
        results = results + "(" + i + ", " + (i + 1) + "): " + similarity + "\n";
        json = json + "{\"source\":" + i + ",\"target\":" + (i + 1) + 
          ",\"value\":" + new DecimalFormat("####0.00").format(similarity) + "},";
      }
    }
    json = json.substring(0, json.length() - 1);
    
    System.out.println("============================================");
    System.out.println(results);
    if (getIncludeParagraphs().booleanValue()) {
      json = json + generateJsonParagraphs(lsa, paragraphs);
    }
    if (getIncludeWords().booleanValue()) {
      json = json + generateJsonWords(lsa, sentences, paragraphNumbers);
    }
    

    json = json + "]}";
    return json;
  }
  
  public String generateJsonParagraphs(LatentSemanticAnalysis lsa, List<String> paragraphs) {
    String json = "],\"paragraphs\":[";
    
    result.append("Calculating Paragraph Recurrence... (" + paragraphs.size() + " Paragraphs)\n");
    DoubleVector[] documentProjections = new DoubleVector[paragraphs.size()];
    for (int a = 0; a < paragraphs.size(); a++) {
      for (int b = 0; b <= a; b++) {
        if (documentProjections[a] == null) {
          documentProjections[a] = lsa.project(new StringDocument((String)paragraphs.get(a)));
        }
        if (documentProjections[b] == null) {
          documentProjections[b] = lsa.project(new StringDocument((String)paragraphs.get(b)));
        }
        double similarity = Math.abs(cosineSimilarity(documentProjections[a], documentProjections[b]));
        if (Double.isNaN(similarity)) {
          similarity = 0.0D;
        }
        json = 
          json + "{\"source\":" + b + ",\"target\":" + a + ",\"value\":" + new DecimalFormat("####0.00").format(similarity) + "},";
      }
    }
    
    json = json.substring(0, json.length() - 1);
    return json;
  }
  
  public String generateJsonWords(LatentSemanticAnalysis lsa, List<String> sentences, List<Integer> paragraphNumbers) {
    String json = "],\"terms\":[";
    Set<String> words = new HashSet();
    StringTokenizer tokenizer; for (Iterator localIterator1 = sentences.iterator(); localIterator1.hasNext(); 
        
        tokenizer.hasMoreTokens())
    {
      String sentence = (String)localIterator1.next();
      tokenizer = new StringTokenizer(sentence);
      continue;
      words.add(tokenizer.nextToken().replaceAll("[\\p{P}&&[^']]", "").toLowerCase());
    }
    
    int wordsSize = words.size();
    words.removeAll(stopwords);
    result.append("Calculating Word Recurrence... (" + words.size() + " Unique Terms Found, " + (
      wordsSize - words.size()) + " Removed)\n");
    
    Object map = new HashMap();
    if (getCorpus().getStemming().booleanValue()) {
      Stemmer stemmer = new EnglishStemmer();
      for (Iterator localIterator2 = words.iterator(); localIterator2.hasNext();) { word = (String)localIterator2.next();
        json = json + "{\"term\":\"" + word + "\"},";
        ((HashMap)map).put(word, lsa.project(new StringDocument(stemmer.stem(word))));
      }
    } else {
      for (String word : words) {
        json = json + "{\"term\":\"" + word + "\"},";
        ((HashMap)map).put(word, lsa.project(new StringDocument(word)));
      }
    }
    

    result.append("Creating Json...\n");
    json = json.substring(0, json.length() - 1);
    json = json + "],\"terms_links\":[";
    Iterator localIterator3; for (String word = words.iterator(); word.hasNext(); 
        
        localIterator3.hasNext())
    {
      String a = (String)word.next();
      System.out.println("A: " + a);
      localIterator3 = words.iterator(); continue;String b = (String)localIterator3.next();
      double similarity = Math.abs(cosineSimilarity((DoubleVector)((HashMap)map).get(a), (DoubleVector)((HashMap)map).get(b)));
      if (Double.isNaN(similarity)) {
        similarity = 0.0D;
      }
      if ((similarity >= getTermThreshold()) && (similarity != 0.0D)) {
        json = 
          json + "{\"source\":\"" + a + "\",\"target\":\"" + b + "\",\"value\":" + new DecimalFormat("####0.00").format(similarity) + "},";
      }
    }
    
    json = json.substring(0, json.length() - 1);
    return json;
  }
  






  private double cosineSimilarity(DoubleVector vectorA, DoubleVector vectorB)
  {
    double dotProduct = 0.0D;
    double normA = 0.0D;
    double normB = 0.0D;
    for (int i = 0; i < vectorA.length(); i++) {
      dotProduct += vectorA.get(i) * vectorB.get(i);
      normA += Math.pow(vectorA.get(i), 2.0D);
      normB += Math.pow(vectorB.get(i), 2.0D);
    }
    return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
  }
  


  public void downloadJsonFile()
  {
    if ((json == null) || (json.equals(""))) {
      JOptionPane.showMessageDialog(null, "No results to copy to json file.", "Error", 0);
      return;
    }
    
    JFileChooser chooser = new JFileChooser();
    chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Json Object", new String[] { "json" }));
    chooser.setSelectedFile(new File(System.getProperty("user.dir") + "\\" + documentFileName.toLowerCase() + 
      "_" + lsaTypeToString(lsaTypeRecurrence) + "_results.json"));
    if (chooser.showSaveDialog(null) == 0) {
      File file = chooser.getSelectedFile();
      BufferedWriter writer = null;
      
      try
      {
        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), getDocumentEncoding()));
        

        writer.write(json);
      }
      catch (IOException e) {
        System.err.println(e.getMessage());
        JOptionPane.showMessageDialog(null, e.getMessage(), "Error", 0);
        
        if (writer != null) {
          try {
            writer.close();
            JOptionPane.showMessageDialog(null, "Json file downloaded to " + file.getAbsolutePath());
          } catch (IOException e) {
            System.err.println(e.getMessage());
          }
        }
      }
      finally
      {
        if (writer != null) {
          try {
            writer.close();
            JOptionPane.showMessageDialog(null, "Json file downloaded to " + file.getAbsolutePath());
          } catch (IOException e) {
            System.err.println(e.getMessage());
          }
        }
      }
    }
    System.out.println("Completed writing json file");
  }
  
  private String lsaTypeToString(Boolean type) {
    if (type.booleanValue()) {
      return "recurrence";
    }
    return "lexical_chain";
  }
  

  public Boolean getLsaType() { return lsaTypeRecurrence; }
  public int getDimensions() { return settingsDimensions; }
  public String getTransform() { return settingsTransform; }
  public Charset getDocumentEncoding() { return documentEncoding; }
  public AbstractSvd getSvd() { return settingsSvd; }
  public Boolean getIncludeParagraphs() { return settingsIncludeParagraphs; }
  public Boolean getIncludeWords() { return settingsIncludeWords; }
  public double getTermThreshold() { return settingsTermThreshold; }
  public File getStopwordsFile() { return settingsStopwords; }
  public PlainlyMenuBar getMenu() { return menuBar; }
  
  public void setLsaType(Boolean type) { lsaTypeRecurrence = type; }
  public void setDimensions(int dimensions) { settingsDimensions = dimensions; }
  public void setTransform(String transform) { settingsTransform = transform; }
  public void setDocumentEncoding(Charset encoding) { documentEncoding = encoding; }
  public void setSvd(AbstractSvd svd) { settingsSvd = svd; }
  public void setIncludeParagraphs(Boolean checked) { settingsIncludeParagraphs = checked; }
  public void setIncludeWords(Boolean checked) { settingsIncludeWords = checked; }
  public void setTermThreshold(double threshold) { settingsTermThreshold = threshold; }
  public void setStopwordsFile(File stopwords) { settingsStopwords = stopwords; }
  
  public JTextArea getResultsText() {
    return result;
  }
  
  public Set<String> getStopwords() {
    return stopwords;
  }
  
  public void loadStopwords(File file) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(file));
    String line = "";
    while ((line = reader.readLine()) != null) {
      stopwords.add(line);
    }
    reader.close();
  }
  
  public void setMatlabDirectory(String directory) { matlabDirectory = directory; }
  
  public String getMatlabDirectory() { if (matlabDirectory.isEmpty()) {
      return "";
    }
    return matlabDirectory + "\\";
  }
  
  public void addCorpus(Corpus corpus)
  {
    corpora.add(corpus);
    getMenu().selectCorpusAdd(corpus);
  }
  
  public List<Corpus> getCorpora() {
    return corpora;
  }
  
  public void setCorpus(Corpus corpus) {
    selectedCorpus = corpus;
  }
  
  public Corpus getCorpus() {
    return selectedCorpus;
  }
  
  public Boolean getCorpusIdExists(int id) {
    for (Corpus corpus : getCorpora()) {
      if (corpus.getId() == id) {
        return Boolean.valueOf(true);
      }
    }
    return Boolean.valueOf(false);
  }
  
  public int getNewCorpusId() {
    for (int i = 0; i < 1000; i++) {
      if (!getCorpusIdExists(i).booleanValue()) {
        return i;
      }
    }
    return 0;
  }
  
  public void populateCorpus() {
    String line = "";
    String[] corpusLine;
    try { BufferedReader reader = new BufferedReader(new FileReader(CorpusManager.getCorpusSettingsFile()));
      while ((line = reader.readLine()) != null) {
        corpusLine = line.split(";");
        Boolean stemming = Boolean.valueOf(false);
        if (corpusLine.length >= 12) {
          stemming = Boolean.valueOf(Boolean.parseBoolean(corpusLine[11]));
        }
        getCorpora().add(new Corpus(Integer.parseInt(corpusLine[0]), corpusLine[1], new File(corpusLine[3]), 
          new File(corpusLine[4]), new File(corpusLine[5]), new File(corpusLine[6]), Integer.parseInt(corpusLine[2]), 
          new File(corpusLine[7]), corpusLine[8], corpusLine[9], stemming, Integer.parseInt(corpusLine[10])));
      }
      reader.close();
    } catch (FileNotFoundException e) {
      System.err.println(e.getMessage());
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
    Collections.sort(getCorpora(), new Comparator()
    {
      public int compare(Corpus obj1, Corpus obj2) {
        return obj1.getName().compareTo(obj2.getName());
      }
    });
    for (Corpus corpus : getCorpora()) {
      getMenu().selectCorpusAdd(corpus);
    }
    if (!getCorpora().isEmpty()) {
      getMenu().setSelectedCorpus((Corpus)getCorpora().get(0));
      selectedCorpus = ((Corpus)getCorpora().get(0));
      getMenu().setCorpusLabel(((Corpus)getCorpora().get(0)).getName() + " (" + ((Corpus)getCorpora().get(0)).getDimensions() + " Dimensions)");
    }
  }
  
  public Transform stringToTransform(String transform)
  {
    if (transform.equals("No Transform"))
      return new NoTransform();
    if (transform.equals("Log Entropy Transform"))
      return new LogEntropyTransform();
    if (transform.equals("Row Magnitude Transform"))
      return new RowMagnitudeTransform();
    if (transform.equals("Correlation Transform"))
      return new CorrelationTransform();
    if (transform.equals("Log Likelihood Transform"))
      return new LogLikelihoodTransform();
    if (transform.equals("Point Wise Mutual Information Transform"))
      return new PointWiseMutualInformationTransform();
    if (transform.equals("TfIdf Transform"))
      return new TfIdfTransform();
    if (transform.equals("TfIdf Doc Striped Transform")) {
      return new TfIdfDocStripedTransform();
    }
    return new LogEntropyTransform();
  }
  
  public static PlainlyJsonGenerator getInstance()
  {
    if (plainly == null) {
      plainly = new PlainlyJsonGenerator();
    }
    return plainly;
  }
  
  public static void main(String[] args) {
    getInstance();
  }
}
