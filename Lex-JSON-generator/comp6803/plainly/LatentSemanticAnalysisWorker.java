package comp6803.plainly;

import javax.swing.SwingWorker;

public class LatentSemanticAnalysisWorker extends SwingWorker<Integer, Integer> {
  public LatentSemanticAnalysisWorker() {}
  
  protected Integer doInBackground() throws Exception {
    PlainlyJsonGenerator.getInstance().getMenu().setLsaRunEnabled(Boolean.valueOf(false));
    PlainlyJsonGenerator.getInstance().runLsa();
    return null;
  }
  
  protected void done()
  {
    PlainlyJsonGenerator.getInstance().getMenu().setLsaRunEnabled(Boolean.valueOf(true));
  }
}
