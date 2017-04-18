package comp6803.plainly;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class CorpusManager extends JFrame
{
  private static CorpusManager manager = null;
  
  private JTable table;
  
  private static File corpusFolder = new File("corpus");
  private static File corpusSettingsFile = new File("corpora.txt");
  private static final int MIN_WIDTH = 700;
  private static final int MIN_HEIGHT = 200;
  
  public CorpusManager()
  {
    setTitle("Corpus Manager");
    
    setSize(new Dimension(700, 200));
    setVisible(true);
    setMinimumSize(new Dimension(700, 200));
    setSize(900, 200);
    

    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e) {
        CorpusManager.manager = null;
      }
      
    });
    JPanel panel = new JPanel(new java.awt.BorderLayout());
    add(panel);
    
    String[] columns = { "Id", "Name", "Dimensions", "Corpus Directory", "Documents", "Terms", "Transform", "Svd", "Stemming", "Delete" };
    Object[][] rows = new Object[PlainlyJsonGenerator.getInstance().getCorpora().size()][10];
    int i = 0;
    for (Corpus corpus : PlainlyJsonGenerator.getInstance().getCorpora()) {
      rows[i][0] = Integer.valueOf(corpus.getId());
      rows[i][1] = corpus.getName();
      rows[i][2] = Integer.valueOf(corpus.getDimensions());
      rows[i][3] = corpus.getFolder();
      rows[i][4] = Integer.valueOf(corpus.getDocuments());
      rows[i][5] = Integer.valueOf(corpus.getTerms().size());
      rows[i][6] = corpus.getTransform();
      rows[i][7] = corpus.getSvd();
      if (corpus.getStemming().booleanValue()) {
        rows[i][8] = "Yes";
      } else {
        rows[i][8] = "No";
      }
      rows[i][9] = "Delete";
      i++;
    }
    table = new JTable(rows, columns);
    table.setSelectionMode(0);
    table.setRowSelectionAllowed(false);
    table.getTableHeader().setReorderingAllowed(false);
    table.getColumnModel().getColumn(0).setMinWidth(20);
    table.getColumnModel().getColumn(0).setMaxWidth(20);
    table.getColumnModel().getColumn(1).setMinWidth(100);
    table.getColumnModel().getColumn(1).setMaxWidth(200);
    table.getColumnModel().getColumn(2).setMinWidth(70);
    table.getColumnModel().getColumn(2).setMaxWidth(70);
    table.getColumnModel().getColumn(4).setMinWidth(70);
    table.getColumnModel().getColumn(4).setMaxWidth(70);
    table.getColumnModel().getColumn(5).setMinWidth(60);
    table.getColumnModel().getColumn(5).setMaxWidth(60);
    table.getColumnModel().getColumn(6).setMinWidth(140);
    table.getColumnModel().getColumn(6).setMaxWidth(140);
    table.getColumnModel().getColumn(7).setMinWidth(50);
    table.getColumnModel().getColumn(7).setMaxWidth(50);
    table.getColumnModel().getColumn(8).setMinWidth(60);
    table.getColumnModel().getColumn(8).setMaxWidth(60);
    table.getColumnModel().getColumn(9).setMinWidth(70);
    table.getColumnModel().getColumn(9).setMaxWidth(70);
    table.setRowHeight(30);
    table.setDefaultEditor(table.getColumnClass(0), null);
    table.getColumnModel().getColumn(9).setCellRenderer(new ButtonRenderer());
    table.addMouseListener(new java.awt.event.MouseAdapter()
    {
      public void mouseClicked(MouseEvent event) {
        int row = table.rowAtPoint(event.getPoint());
        int column = table.columnAtPoint(event.getPoint());
        if (column == 9) {
          int delete = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the corpus \"" + 
            ((Corpus)PlainlyJsonGenerator.getInstance().getCorpora().get(row)).getName() + "\"", "Delete Corpus", 0);
          if (delete == 0) {
            System.out.println("Deleting corpus " + ((Corpus)PlainlyJsonGenerator.getInstance().getCorpora().get(row)).getName());
            ((Corpus)PlainlyJsonGenerator.getInstance().getCorpora().get(row)).getFileEntropy().delete();
            ((Corpus)PlainlyJsonGenerator.getInstance().getCorpora().get(row)).getFileTermDocumentIndex().delete();
            ((Corpus)PlainlyJsonGenerator.getInstance().getCorpora().get(row)).getFileU().delete();
            PlainlyJsonGenerator.getInstance().getCorpora().remove(row);
            createCorpusSettingsFile();
            System.out.println(event.getSource());
            ((Window)((Component)event.getSource()).getParent().getParent().getParent()
              .getParent().getParent().getParent().getParent()).dispose();
            CorpusManager.manager = null;
            CorpusManager.getInstance();
          }
        }
      }
    });
    panel.add(new javax.swing.JScrollPane(table));
  }
  
  public static File getCorpusFolder() {
    if (corpusFolder.exists()) {
      return corpusFolder;
    }
    corpusFolder.mkdirs();
    return corpusFolder;
  }
  
  public static File getCorpusSettingsFile()
  {
    if (corpusSettingsFile.exists()) {
      return corpusSettingsFile;
    }
    try {
      corpusSettingsFile.createNewFile();
      PrintWriter writer = new PrintWriter(corpusSettingsFile);
      writer.close();
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
    return corpusSettingsFile;
  }
  
  public void createCorpusSettingsFile()
  {
    try
    {
      PrintWriter writer = new PrintWriter(new FileWriter(corpusSettingsFile));
      for (Corpus corpus : PlainlyJsonGenerator.getInstance().getCorpora()) {
        writer.println(corpus.getId() + ";" + corpus.getName() + ";" + corpus.getDimensions() + 
          ";" + corpus.getFileEntropy() + ";" + corpus.getFileTermDocumentIndex() + ";" + 
          corpus.getFileU() + ";" + corpus.getFolder() + ";" + corpus.getTransform() + ";" + corpus.getSvd() + 
          ";" + corpus.getDocuments());
      }
      writer.close();
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
  
  public static CorpusManager getInstance() {
    if (manager == null) {
      manager = new CorpusManager();
    }
    return manager;
  }
}
