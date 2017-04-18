package comp6803.plainly;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;



public class PlainlyMenuBar
  extends JMenuBar
{
  private JMenu menuFile;
  private JMenu menuLsa;
  private JMenu menuCorpus;
  private JLabel labelCorpus;
  private JMenuItem menuLsaRun;
  private JMenuItem menuLsaDownload;
  private JMenu menuCorpusSelect;
  private ButtonGroup menuCorpusSelectGroup;
  
  public PlainlyMenuBar()
  {
    createMenus();
  }
  
  private void createMenus()
  {
    menuFile = new JMenu("File");
    add(menuFile);
    JMenuItem menuFileLoad = new JMenuItem("Load File");
    JMenuItem menuFileClear = new JMenuItem("Clear Text");
    JMenuItem menuFileExit = new JMenuItem("Exit");
    menuFileLoad.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        PlainlyJsonGenerator.getInstance().loadDocument();
      }
    });
    menuFileClear.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        PlainlyJsonGenerator.getInstance().clearDocument();
      }
    });
    menuFileExit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        System.out.println("Closing Application.");
        System.exit(0);
      }
      
    });
    ButtonGroup menuFileEncoding = new ButtonGroup();
    JRadioButtonMenuItem menuFileISO_8859_1 = new JRadioButtonMenuItem("ISO 8859-1");
    JRadioButtonMenuItem menuFileUS_ASCII = new JRadioButtonMenuItem("ASCII");
    JRadioButtonMenuItem menuFileUTF_8 = new JRadioButtonMenuItem("UTF-8");
    menuFileEncoding.add(menuFileISO_8859_1);
    menuFileEncoding.add(menuFileUS_ASCII);
    menuFileEncoding.add(menuFileUTF_8);
    menuFileISO_8859_1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        System.out.println("Using ISO_8859_1 Encoding.");
        PlainlyJsonGenerator.getInstance().setDocumentEncoding(StandardCharsets.ISO_8859_1);
      }
    });
    menuFileUS_ASCII.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        System.out.println("Using ASCII Encoding.");
        PlainlyJsonGenerator.getInstance().setDocumentEncoding(StandardCharsets.US_ASCII);
      }
    });
    menuFileUTF_8.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        System.out.println("Using UTF-8 Encoding.");
        PlainlyJsonGenerator.getInstance().setDocumentEncoding(StandardCharsets.UTF_8);
      }
    });
    menuFile.add(menuFileLoad);
    menuFile.add(menuFileClear);
    menuFile.addSeparator();
    menuFile.add(menuFileUS_ASCII);
    menuFile.add(menuFileISO_8859_1);
    menuFile.add(menuFileUTF_8);
    menuFileUS_ASCII.setSelected(true);
    menuFile.addSeparator();
    menuFile.add(menuFileExit);
    

    menuLsa = new JMenu("LSA");
    menuLsaRun = new JMenuItem("Run LSA");
    menuLsaDownload = new JMenuItem("Download Json File");
    ButtonGroup menuLsaType = new ButtonGroup();
    JRadioButtonMenuItem menuLsaRecurrence = new JRadioButtonMenuItem("Recurrence");
    JRadioButtonMenuItem menuLsaLexicalChain = new JRadioButtonMenuItem("Lexical Chain");
    menuLsaType.add(menuLsaRecurrence);
    menuLsaType.add(menuLsaLexicalChain);
    JMenuItem menuLsaSettings = new JMenuItem("Settings");
    
    menuLsaRun.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        new LatentSemanticAnalysisWorker().execute();
      }
    });
    menuLsaDownload.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        PlainlyJsonGenerator.getInstance().downloadJsonFile();
      }
    });
    menuLsaRecurrence.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        System.out.println("Recurrence.");
        PlainlyJsonGenerator.getInstance().setLsaType(Boolean.valueOf(true));
      }
    });
    menuLsaLexicalChain.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        System.out.println("Lexical Chain.");
        PlainlyJsonGenerator.getInstance().setLsaType(Boolean.valueOf(false));
      }
    });
    menuLsaSettings.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        PlainlySettings.getInstance();
      }
    });
    menuLsaRun.setAccelerator(KeyStroke.getKeyStroke(82, 2));
    menuLsaDownload.setAccelerator(KeyStroke.getKeyStroke(68, 2));
    menuLsa.add(menuLsaRun);
    menuLsa.add(menuLsaDownload);
    menuLsa.addSeparator();
    menuLsa.add(menuLsaRecurrence);
    menuLsa.add(menuLsaLexicalChain);
    menuLsaRecurrence.setSelected(true);
    menuLsa.addSeparator();
    menuLsa.add(menuLsaSettings);
    add(menuLsa);
    

    menuCorpus = new JMenu("Corpus");
    JMenuItem menuCorpusCreate = new JMenuItem("Create Corpus");
    JMenuItem menuCorpusManager = new JMenuItem("Manage Corpora");
    menuCorpusSelect = new JMenu("Select Corpus");
    menuCorpusSelectGroup = new ButtonGroup();
    menuCorpusCreate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        CorpusCreate.getInstance();
      }
    });
    menuCorpusManager.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        if (!PlainlyJsonGenerator.getInstance().getCorpora().isEmpty()) {
          CorpusManager.getInstance();
        } else {
          JOptionPane.showMessageDialog(null, "No corpora exist.");
        }
      }
    });
    menuCorpus.add(menuCorpusSelect);
    menuCorpus.addSeparator();
    menuCorpus.add(menuCorpusCreate);
    menuCorpus.add(menuCorpusManager);
    add(menuCorpus);
    
    labelCorpus = new JLabel("No Corpus Selected.");
    add(Box.createHorizontalGlue());
    add(labelCorpus);
  }
  
  public void setCorpusLabel(String string) {
    labelCorpus.setText(string);
    revalidate();
    repaint();
  }
  
  public void setLsaRunEnabled(Boolean value) {
    menuLsaRun.setEnabled(value.booleanValue());
    menuLsaDownload.setEnabled(value.booleanValue());
  }
  
  public void selectCorpusAdd(Corpus corpus) {
    JRadioButtonMenuItem select = new JRadioButtonMenuItem();
    select.setText(corpus.getName() + " (" + corpus.getDimensions() + " Dimensions)");
    select.setName(Integer.toString(corpus.getId()));
    select.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        Corpus corpus = getSelectedCorpus();
        setCorpusLabel(corpus.getName() + " (" + corpus.getDimensions() + " Dimensions)");
      }
    });
    menuCorpusSelectGroup.add(select);
    menuCorpusSelect.add(select);
  }
  
  public Corpus getSelectedCorpus() {
    Enumeration<AbstractButton> buttons = menuCorpusSelectGroup.getElements();
    while (buttons.hasMoreElements()) {
      JRadioButtonMenuItem item = (JRadioButtonMenuItem)buttons.nextElement();
      if (item.isSelected()) {
        for (Corpus corpus : PlainlyJsonGenerator.getInstance().getCorpora()) {
          if (corpus.getId() == Integer.parseInt(item.getName()))
          {




            return corpus;
          }
        }
      }
    }
    return null;
  }
  
  public void setSelectedCorpus(Corpus corpus) {
    Enumeration<AbstractButton> buttons = menuCorpusSelectGroup.getElements();
    while (buttons.hasMoreElements()) {
      JRadioButtonMenuItem item = (JRadioButtonMenuItem)buttons.nextElement();
      if (item.getName().equals(Integer.toString(corpus.getId()))) {
        item.setSelected(true);
      }
    }
  }
}
