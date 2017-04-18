package comp6803.plainly;

import comp6803.layout.SpringUtilities;
import edu.ucla.sspace.basis.StringBasisMapping;
import edu.ucla.sspace.lsa.LatentSemanticAnalysis;
import edu.ucla.sspace.matrix.CorrelationTransform;
import edu.ucla.sspace.matrix.LogEntropyTransform;
import edu.ucla.sspace.matrix.LogLikelihoodTransform;
import edu.ucla.sspace.matrix.NoTransform;
import edu.ucla.sspace.matrix.PointWiseMutualInformationTransform;
import edu.ucla.sspace.matrix.TfIdfTransform;
import edu.ucla.sspace.matrix.Transform;
import edu.ucla.sspace.matrix.factorization.SingularValueDecompositionMatlab;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class CorpusCreate extends JFrame
{
  private static CorpusCreate create = null;
  public static Boolean stemmingEnabled = Boolean.valueOf(false);
  
  private static final int MIN_WIDTH = 400;
  
  private static final int MIN_HEIGHT = 250;
  
  private JTextField corpusName;
  private File corpus;
  private JSpinner corpusDimension;
  private JComboBox<String> corpusSvd;
  private JComboBox<String> corpusTransform;
  private JCheckBox corpusStemming;
  private String name = "";
  
  public CorpusCreate() {
    setTitle("Create Corpus");
    
    setSize(new Dimension(400, 250));
    setVisible(true);
    setMinimumSize(new Dimension(400, 250));
    setMaximumSize(new Dimension(400, 250));
    setResizable(false);
    

    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e) {
        CorpusCreate.create = null;
      }
      
    });
    JPanel panel = new JPanel(new javax.swing.SpringLayout());
    add(panel);
    

    JLabel corpusNameLabel = new JLabel("Corpus Name: ");
    corpusName = new JTextField();
    corpusNameLabel.setLabelFor(corpusName);
    panel.add(corpusNameLabel);
    panel.add(corpusName);
    

    JLabel corpusFolderLabel = new JLabel("Corpus Folder: ");
    JButton corpusFolder = new JButton("Select Folder");
    corpusFolderLabel.setLabelFor(corpusFolder);
    corpusFolder.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select Corpus Folder");
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        chooser.setFileSelectionMode(1);
        if (chooser.showOpenDialog(null) == 0) {
          System.out.println("Corpus set to: " + chooser.getSelectedFile().getName());
          corpus = chooser.getSelectedFile();
        }
        ((JButton)evt.getSource()).setText("Corpus: " + corpus.getName());
      }
    });
    panel.add(corpusFolderLabel);
    panel.add(corpusFolder);
    

    JLabel corpusDimensionLabel = new JLabel("Dimensions: ");
    corpusDimension = new JSpinner(new SpinnerNumberModel(300, 1, 500, 1));
    corpusDimensionLabel.setLabelFor(corpusDimension);
    panel.add(corpusDimensionLabel);
    panel.add(corpusDimension);
    

    JLabel corpusStemmingLabel = new JLabel("Enable Stemming: ");
    corpusStemming = new JCheckBox();
    corpusStemmingLabel.setLabelFor(corpusStemming);
    panel.add(corpusStemmingLabel);
    panel.add(corpusStemming);
    

    JLabel corpusSvdLabel = new JLabel("Singular Value Decomposition: ");
    String[] svd = { "JAMA", "Matlab", "SVDLibC" };
    corpusSvd = new JComboBox(svd);
    corpusSvd.setSelectedIndex(1);
    corpusSvdLabel.setLabelFor(corpusSvd);
    panel.add(corpusSvdLabel);
    panel.add(corpusSvd);
    

    JLabel corpusTransformLabel = new JLabel("Transform: ");
    String[] transformations = { "No Transform", "Log Entropy Transform", "Row Magnitude Transform", "Correlation Transform", 
      "Log Likelihood Transform", "Point Wise Mutual Information Transform", "TfIdf Transform", "TfIdf Doc Striped Transform" };
    corpusTransform = new JComboBox(transformations);
    corpusTransform.setSelectedIndex(1);
    corpusTransformLabel.setLabelFor(corpusTransform);
    corpusTransform.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt) {
        PlainlyJsonGenerator.getInstance().setTransform((String)((JComboBox)evt.getSource()).getSelectedItem());
      }
    });
    panel.add(corpusTransformLabel);
    panel.add(corpusTransform);
    
    JButton corpusSave = new JButton("Create");
    JButton corpusCancel = new JButton("Cancel");
    corpusSave.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        createCorpus(corpusName.getText(), corpus, ((Integer)corpusDimension.getValue()).intValue(), 
          (String)corpusSvd.getSelectedItem(), (String)corpusTransform.getSelectedItem(), 
          Boolean.valueOf(corpusStemming.isSelected()));
      }
    });
    corpusCancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        removeWindow();
      }
    });
    panel.add(corpusSave);
    panel.add(corpusCancel);
    
    SpringUtilities.makeCompactGrid(panel, 7, 2, 10, 10, 10, 10);
  }
  
  public void createCorpus(String name, File folder, int dimensions, String svd, String transform, Boolean stemming) {
    System.out.println("Creating Corpus " + name + " for " + folder + " with " + dimensions + 
      " dimensions using " + transform + " transform.");
    
    if ((name == null) || (name.equals(""))) {
      JOptionPane.showMessageDialog(null, "No Corpus Name.");
      return;
    }
    if (corpus == null) {
      JOptionPane.showMessageDialog(null, "No Corpus Selected.");
      return;
    }
    this.name = name;
    stemmingEnabled = stemming;
    try {
      setTitle("Processing Documents... (0%)");
      LatentSemanticAnalysis lsa = new LatentSemanticAnalysis(false, dimensions, stringToTransform(transform), 
        new SingularValueDecompositionMatlab(), false, new StringBasisMapping());
      processDocuments(lsa, corpus);
      setTitle("Processing Semantic Space... (50%)");
      lsa.processSpace(System.getProperties());
      
      Set<String> words = lsa.getWords();
      try {
        PrintWriter writer = new PrintWriter(new FileWriter("corpus/" + getName().replaceAll(" ", "-").toLowerCase() + 
          "-terms.dat"));
        for (String word : words) {
          writer.println(word);
        }
        writer.close();
      } catch (IOException e) {
        System.err.println(e.getMessage());
      }
      

      int id = PlainlyJsonGenerator.getInstance().getNewCorpusId();
      try {
        PrintWriter writer = new PrintWriter(new FileWriter(CorpusManager.getCorpusSettingsFile(), true));
        writer.println(id + ";" + name + ";" + dimensions + 
          ";" + new File(new StringBuilder("corpus/").append(getName().replaceAll(" ", "-").toLowerCase()).append("-entropy.dat").toString()) + 
          ";" + new File(new StringBuilder("corpus/").append(getName().replaceAll(" ", "-").toLowerCase()).append("-term-document-index.dat").toString()) + 
          ";" + new File(new StringBuilder("corpus/").append(getName().replaceAll(" ", "-").toLowerCase()).append("-u-times-sigma-inv.dat").toString()) + 
          ";" + new File(new StringBuilder("corpus/").append(getName().replaceAll(" ", "-").toLowerCase()).append("-terms.dat").toString()) + 
          ";" + corpus + ";" + transform + ";" + svd + ";" + corpus.list().length + ";" + stemming);
        writer.close();
      } catch (IOException e) {
        System.err.println(e.getMessage());
      }
      
      addCorpus(new Corpus(PlainlyJsonGenerator.getInstance().getNewCorpusId(), name, 
        new File("corpus/" + getName().replaceAll(" ", "-").toLowerCase() + "-entropy.dat"), 
        new File("corpus/" + getName().replaceAll(" ", "-").toLowerCase() + "-term-document-index.dat"), 
        new File("corpus/" + getName().replaceAll(" ", "-").toLowerCase() + "-u-times-sigma-inv.dat"), 
        new File("corpus/" + getName().replaceAll(" ", "-").toLowerCase() + "-terms.dat"), 
        dimensions, corpus, transform, svd, stemming, corpus.list().length));
      JOptionPane.showMessageDialog(null, "Corpus Created.");
      dispose();
    } catch (IOException e) {
      System.err.println(e.getMessage());
      JOptionPane.showMessageDialog(null, e.getMessage());
    } catch (IndexOutOfBoundsException e) {
      System.err.println(e.getMessage());
      JOptionPane.showMessageDialog(null, e.getMessage());
    }
  }
  
  public void addCorpus(Corpus corpus) {
    System.out.println("Add Corpus 1");
    PlainlyJsonGenerator.getInstance().addCorpus(corpus);
  }
  





  private Boolean processDocuments(LatentSemanticAnalysis lsa, File corpus)
  {
    System.out.println("Processing Documents in " + corpus.getName());
    File[] files = corpus.listFiles();
    for (File file : files) {
      try {
        System.out.println("Processing Document: " + file.getName());
        lsa.processDocument(new BufferedReader(new FileReader(file)));
      } catch (IOException e) {
        System.err.println(e.getMessage());
        JOptionPane.showMessageDialog(null, e.getMessage());
      }
    }
    return Boolean.valueOf(true);
  }
  
  public Transform stringToTransform(String transform) {
    if (transform.equals("No Transform"))
      return new NoTransform();
    if (transform.equals("Log Entropy Transform"))
      return new LogEntropyTransform();
    if (transform.equals("Row Magnitude Transform"))
      return new edu.ucla.sspace.matrix.RowMagnitudeTransform();
    if (transform.equals("Correlation Transform"))
      return new CorrelationTransform();
    if (transform.equals("Log Likelihood Transform"))
      return new LogLikelihoodTransform();
    if (transform.equals("Point Wise Mutual Information Transform"))
      return new PointWiseMutualInformationTransform();
    if (transform.equals("TfIdf Transform"))
      return new TfIdfTransform();
    if (transform.equals("TfIdf Doc Striped Transform")) {
      return new edu.ucla.sspace.matrix.TfIdfDocStripedTransform();
    }
    return new LogEntropyTransform();
  }
  
  public void removeWindow()
  {
    dispose();
    create = null;
  }
  
  public String getName() {
    return name;
  }
  
  public static CorpusCreate getInstance() {
    if (create == null) {
      create = new CorpusCreate();
    }
    return create;
  }
}
