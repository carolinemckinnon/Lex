package comp6803.plainly;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.PrintStream;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

public class PlainlySettings extends JFrame
{
  private static PlainlySettings settings = null;
  private static File settingsFile = new File("settings.txt");
  
  private JCheckBox settingsIncludeParagraphs;
  
  private JCheckBox settingsIncludeWords;
  
  private JSpinner settingsTermThreshold;
  private JButton stopwords;
  private File settingsStopwords;
  private static final int MIN_WIDTH = 320;
  private static final int MIN_HEIGHT = 190;
  
  public PlainlySettings()
  {
    setTitle("Settings");
    
    setSize(new Dimension(320, 190));
    setVisible(true);
    setMinimumSize(new Dimension(320, 190));
    
    setResizable(false);
    

    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e) {
        PlainlySettings.settings = null;
      }
      

    });
    JPanel panel = new JPanel(new javax.swing.SpringLayout());
    add(panel);
    

    JLabel settingsIncludeParagraphsLabel = new JLabel("Include Paragraphs: ");
    settingsIncludeParagraphs = new JCheckBox();
    settingsIncludeParagraphsLabel.setLabelFor(settingsIncludeParagraphs);
    panel.add(settingsIncludeParagraphsLabel);
    panel.add(settingsIncludeParagraphs);
    

    JLabel settingsIncludeWordsLabel = new JLabel("Include Words: ");
    settingsIncludeWords = new JCheckBox();
    settingsIncludeWordsLabel.setLabelFor(settingsIncludeWords);
    settingsIncludeWords.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        if (settingsIncludeWords.isSelected()) {
          settingsTermThreshold.setEnabled(true);
        } else {
          settingsTermThreshold.setEnabled(false);
        }
      }
    });
    panel.add(settingsIncludeWordsLabel);
    panel.add(settingsIncludeWords);
    

    JLabel settingsTermThresholdLabel = new JLabel("Term Connection Threshold: ");
    settingsTermThreshold = new JSpinner(new javax.swing.SpinnerNumberModel(0.0D, 0.0D, 1.0D, 0.01D));
    settingsTermThresholdLabel.setLabelFor(settingsTermThreshold);
    settingsTermThreshold.setEnabled(false);
    panel.add(settingsTermThresholdLabel);
    panel.add(settingsTermThreshold);
    








    JLabel stopwordsLabel = new JLabel("Stopwords: ");
    stopwords = new JButton("Select File");
    stopwordsLabel.setLabelFor(stopwords);
    stopwords.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select Stopwords File");
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        if (chooser.showOpenDialog(null) == 0) {
          stopwords.setText(chooser.getSelectedFile().getName());
          settingsStopwords = chooser.getSelectedFile();
        }
      }
    });
    stopwords.setPreferredSize(new Dimension(200, 80));
    panel.add(stopwordsLabel);
    panel.add(stopwords);
    

    JButton settingsSave = new JButton("Save");
    JButton settingsCancel = new JButton("Cancel");
    settingsSave.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        PlainlyJsonGenerator.getInstance().setStopwordsFile(settingsStopwords);
        PlainlyJsonGenerator.getInstance().setIncludeParagraphs(Boolean.valueOf(settingsIncludeParagraphs.isSelected()));
        PlainlyJsonGenerator.getInstance().setIncludeWords(Boolean.valueOf(settingsIncludeWords.isSelected()));
        PlainlyJsonGenerator.getInstance().setTermThreshold(((Double)settingsTermThreshold.getValue()).doubleValue());
        
        PlainlySettings.getInstance().dispose();
        PlainlySettings.settings = null;
      }
    });
    settingsCancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        PlainlySettings.getInstance().dispose();
        PlainlySettings.settings = null;
      }
    });
    panel.add(settingsSave);
    panel.add(settingsCancel);
    
    comp6803.layout.SpringUtilities.makeCompactGrid(panel, 5, 2, 10, 10, 10, 10);
    

    settingsIncludeParagraphs.setSelected(PlainlyJsonGenerator.getInstance().getIncludeParagraphs().booleanValue());
    settingsIncludeWords.setSelected(PlainlyJsonGenerator.getInstance().getIncludeWords().booleanValue());
    settingsTermThreshold.setValue(Double.valueOf(PlainlyJsonGenerator.getInstance().getTermThreshold()));
    if (PlainlyJsonGenerator.getInstance().getIncludeWords().booleanValue()) {
      settingsTermThreshold.setEnabled(true);
    }
    
    if (PlainlyJsonGenerator.getInstance().getStopwordsFile() != null) {
      stopwords.setText(PlainlyJsonGenerator.getInstance().getStopwordsFile().getName());
    }
    

    revalidate();
    repaint();
    System.out.println("Settings initalised");
  }
  
  public static File getSettingsFile() {
    return settingsFile;
  }
  
  public static PlainlySettings getInstance() {
    if (settings == null) {
      settings = new PlainlySettings();
    }
    return settings;
  }
}
