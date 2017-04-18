package comp6803.plainly;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

public class Corpus
{
  private int id;
  private String name;
  private File fileEntropy;
  private File fileTermDocumentIndex;
  private File fileU;
  private int dimensions;
  private File folder;
  private String transform;
  private String svd;
  private Boolean stemming;
  private int documents;
  private Set<String> terms;
  
  public Corpus(int id, String name, File fileEntropy, File fileTermDocumentIndex, File fileU, File terms, int dimensions, File folder, String transform, String svd, Boolean stemming, int documents)
  {
    this.id = id;
    this.name = name;
    this.fileEntropy = fileEntropy;
    this.fileTermDocumentIndex = fileTermDocumentIndex;
    this.fileU = fileU;
    this.dimensions = dimensions;
    this.folder = folder;
    this.transform = transform;
    this.svd = svd;
    this.stemming = stemming;
    this.documents = documents;
    
    String term = "";
    this.terms = new java.util.HashSet();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(terms));
      while ((term = reader.readLine()) != null) {
        this.terms.add(term);
      }
      reader.close();
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setDimensions(int dimensions) {
    this.dimensions = dimensions;
  }
  
  public void setDirectory(File folder) {
    this.folder = folder;
  }
  
  public int getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }
  
  public File getFileEntropy() {
    return fileEntropy;
  }
  
  public File getFileTermDocumentIndex() {
    return fileTermDocumentIndex;
  }
  
  public File getFileU() {
    return fileU;
  }
  
  public int getDimensions() {
    return dimensions;
  }
  
  public File getFolder() {
    return folder;
  }
  
  public String getTransform() {
    return transform;
  }
  
  public String getSvd() {
    return svd;
  }
  
  public Boolean getStemming() {
    return stemming;
  }
  
  public int getDocuments() {
    return documents;
  }
  
  public Set<String> getTerms() {
    return terms;
  }
}
