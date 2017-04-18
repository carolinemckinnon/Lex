package edu.ucla.sspace.matrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Logger;











































































public class LocalityPreservingProjection
{
  private static final Logger LOGGER = Logger.getLogger(LocalityPreservingProjection.class.getName());
  










































  private static final String SR_LPP_M = "%% This code requires the SR-LPP implementation by Deng Cai (dengcai AT\n%% gmail.com) available at\n%% http://www.zjucadcg.cn/dengcai/SR/index.html\n\n%% Load the data matrix from file\nTmp = load('%s','-ascii');\ndata = spconvert(Tmp);\n%% Remove the raw data file to save space\nclear Tmp;\n\n%% Load the affinity matrix from file\nTmp = load('%s','-ascii');\nW = spconvert(Tmp);\n%% Remove the raw data file to save space\nclear Tmp;\n\n%% If 0, all of the dimensions in the adj. matrix are used\nDim = %d\n\noptions = [];\noptions.W = W;\noptions.ReguAlpha = 0.01;\noptions.ReguType = 'Ridge';\noptions.ReducedDim = Dim;\n%% Call the SR code\n[eigvector] = SR_caller(options, data);\n[nSmp,nFea] = size(data);\nif size(eigvector,1) == nFea + 1\n    Projection = [data ones(nSmp,1)]*eigvector;\nelse\n    Projection = data*eigvector;\nend\n%% Save the projection as a matrix\n%s\nfprintf(1,'Finished\\n');\n\n";
  










































  private static final String LPP_M = "%% LPP code based on the Matlab implementation by Deng Cai (dengcai2 AT\n%% cs.uiuc.edu) available at\n%% http://www.cs.uiuc.edu/homes/dengcai2/Data/code/LPP.m\n\n%% Load the data matrix from file\nTmp = load('%s','-ascii');\ndata = spconvert(Tmp);\n%% Remove the raw data file to save space\nclear Tmp;\n[nSmp,nFea] = size(data);\n%% Subtract out the mean fromm the data.  See page 7 of the LPI paper\nprintf('Subtracting out the mean\\n');\nif issparse(data)\n    data = full(data);\nend\nsampleMean = mean(data);\ndata = (data - repmat(sampleMean,nSmp,1));\n\n%% Load the affinity matrix from file\nTmp = load('%s','-ascii');\nW = spconvert(Tmp);\n%% Remove the raw data file to save space\nclear Tmp;\n\n%% If 0, all of the dimensions in the adj. matrix are used\nDim = %d\n\noptions = [];\n\nD = full(sum(W,2));\n%%options.ReguAlpha = options.ReguAlpha*sum(D)/length(D);\nD = sparse(1:nSmp,1:nSmp,D,nSmp,nSmp);\n\nprintf('Computing D prime\\n');\nDPrime = data'*D*data;\nDPrime = max(DPrime,DPrime');\n\nprintf('Computing W prime\\n');\nWPrime = data'*W*data;\nWPrime = max(WPrime,WPrime');\n\ndimMatrix = size(WPrime,2);\n\nif Dim > dimMatrix\n    Dim = dimMatrix;\nend\n\n%% Before using eigs, check whether the affinity matrix is positive and definite\n%%printf('Testing if DPrime is pos. def.\\n');\n%%isposdef = true;\n%%for i=1:length(DPrime)\n%%     if ( det( DPrime(1:i, 1:i) ) <= 0 )\n%%          isposdef = false;\n%%          break;\n%%      end\n%%end\n\n%%if (isposdef & dimMatrix > 1000 & Dim < dimMatrix/10) | (dimMatrix > 500 & Dim < dimMatrix/20) | (dimMatrix > 250 & Dim < dimMatrix/30)\n%%    bEigs = 1;\n%%else\n    bEigs = 0;\n%%end\n\n\nprintf('Computing Eigenvectors\\n');\nif bEigs\n    %%disp('using eigs to speed up!');\n    [eigvector, eigvalue] = eigs(WPrime,DPrime,Dim,'la');\n    eigvalue = diag(eigvalue);\nelse\n    [eigvector, eigvalue] = eig(WPrime,DPrime);\n    eigvalue = diag(eigvalue);\n\n    [junk, index] = sort(-eigvalue);\n    eigvalue = eigvalue(index);\n    eigvector = eigvector(:,index);\n\n    if Dim < size(eigvector,2)\n        eigvector = eigvector(:, 1:Dim);\n        eigvalue = eigvalue(1:Dim);\n    end\nend\n\nfor i = 1:size(eigvector,2)\n    eigvector(:,i) = eigvector(:,i)./norm(eigvector(:,i));\nend\n\neigIdx = find(eigvalue < 1e-3);\neigvalue (eigIdx) = [];\neigvector(:,eigIdx) = [];\n\n%% Compute the projection\nprintf('Computing projection matrix\\n');\nprojection = data*eigvector;\n\n%% Save the projection as a matrix\n%s\nprintf('Finished\\n');\n";
  











































  private LocalityPreservingProjection() {}
  










































  public static MatrixFile project(MatrixFile inputMatrix, MatrixFile affinityMatrix, int dimensions)
  {
    try
    {
      File outputFile = File.createTempFile("lcc-output-matrix", ".dat");
      execute(inputMatrix.getFile(), affinityMatrix.getFile(), dimensions, 
        outputFile);
      return new MatrixFile(outputFile, MatrixIO.Format.DENSE_TEXT);
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  

















  public static Matrix project(Matrix m, MatrixFile affinityMatrix, int dimensions)
  {
    try
    {
      return execute(m, affinityMatrix, dimensions);
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  

















  public static Matrix project(Matrix m, Matrix affinityMatrix, int dimensions)
  {
    try
    {
      File affMatrixFile = 
        File.createTempFile("affinity-matrix", ".dat");
      MatrixIO.writeMatrix(affinityMatrix, affMatrixFile, 
        MatrixIO.Format.MATLAB_SPARSE);
      return execute(m, new MatrixFile(affMatrixFile, 
        MatrixIO.Format.MATLAB_SPARSE), 
        dimensions);
    } catch (IOException ioe) {
      throw new IOError(ioe);
    }
  }
  













  private static Matrix execute(Matrix dataMatrix, MatrixFile affMatrixFile, int dims)
    throws IOException
  {
    File mInput = File.createTempFile("lpp-input-data-matrix", ".dat");
    mInput.deleteOnExit();
    MatrixIO.writeMatrix(dataMatrix, mInput, MatrixIO.Format.MATLAB_SPARSE);
    


    File output = File.createTempFile("lpp-output-matrix", ".dat");
    

    execute(mInput, affMatrixFile.getFile(), dims, output);
    

    return MatrixIO.readMatrix(output, MatrixIO.Format.DENSE_TEXT);
  }
  

















  private static void execute(File dataMatrixFile, File affMatrixFile, int dims, File outputMatrix)
    throws IOException
  {
    if (isMatlabAvailable()) {
      invokeMatlab(dataMatrixFile, affMatrixFile, dims, outputMatrix);
    }
    else if (isOctaveAvailable()) {
      invokeOctave(dataMatrixFile, affMatrixFile, dims, outputMatrix);
    } else {
      throw new IllegalStateException(
        "Cannot find Matlab or Octave to invoke LPP");
    }
  }
  



  private static void invokeMatlab(File dataMatrixFile, File affMatrixFile, int dimensions, File outputFile)
    throws IOException
  {
    String commandLine = "matlab -nodisplay -nosplash -nojvm";
    LOGGER.fine(commandLine);
    Process matlab = Runtime.getRuntime().exec(commandLine);
    

    String outputStr = 
      "save " + outputFile.getAbsolutePath() + " projection -ASCII\n";
    

    String matlabProgram = String.format("%% This code requires the SR-LPP implementation by Deng Cai (dengcai AT\n%% gmail.com) available at\n%% http://www.zjucadcg.cn/dengcai/SR/index.html\n\n%% Load the data matrix from file\nTmp = load('%s','-ascii');\ndata = spconvert(Tmp);\n%% Remove the raw data file to save space\nclear Tmp;\n\n%% Load the affinity matrix from file\nTmp = load('%s','-ascii');\nW = spconvert(Tmp);\n%% Remove the raw data file to save space\nclear Tmp;\n\n%% If 0, all of the dimensions in the adj. matrix are used\nDim = %d\n\noptions = [];\noptions.W = W;\noptions.ReguAlpha = 0.01;\noptions.ReguType = 'Ridge';\noptions.ReducedDim = Dim;\n%% Call the SR code\n[eigvector] = SR_caller(options, data);\n[nSmp,nFea] = size(data);\nif size(eigvector,1) == nFea + 1\n    Projection = [data ones(nSmp,1)]*eigvector;\nelse\n    Projection = data*eigvector;\nend\n%% Save the projection as a matrix\n%s\nfprintf(1,'Finished\\n');\n\n", new Object[] {
      dataMatrixFile.getAbsolutePath(), 
      affMatrixFile.getAbsolutePath(), 
      Integer.valueOf(dimensions), outputStr });
    

    PrintWriter stdin = new PrintWriter(matlab.getOutputStream());
    BufferedReader stdout = new BufferedReader(
      new InputStreamReader(matlab.getInputStream()));
    BufferedReader stderr = new BufferedReader(
      new InputStreamReader(matlab.getErrorStream()));
    
    stdin.println(matlabProgram);
    stdin.close();
    



    StringBuilder output = new StringBuilder("Matlab LPP output:\n");
    for (String line = null; (line = stdout.readLine()) != null;) {
      output.append(line).append("\n");
      if (line.equals("Finished")) {
        matlab.destroy();
      }
    }
    LOGGER.fine(output.toString());
    
    int exitStatus = -1;
    try {
      exitStatus = matlab.waitFor();
    } catch (InterruptedException ie) {
      throw new Error(ie);
    }
    LOGGER.fine("Octave LPP exit status: " + exitStatus);
    


    if (exitStatus != 0) {
      StringBuilder sb = new StringBuilder();
      for (String line = null; (line = stderr.readLine()) != null;) {
        sb.append(line).append("\n");
      }
      throw new IllegalStateException(
        "Matlab LPP did not finish normally: " + sb);
    }
  }
  





  private static void invokeOctave(File dataMatrixFile, File affMatrixFile, int dimensions, File outputFile)
    throws IOException
  {
    File octaveFile = File.createTempFile("octave-LPP", ".m");
    
    String outputStr = 
      "save(\"-ascii\", \"" + outputFile.getAbsolutePath() + 
      "\", \"projection\");\n";
    
    String octaveProgram = null;
    try
    {
      octaveProgram = String.format("%% This code requires the SR-LPP implementation by Deng Cai (dengcai AT\n%% gmail.com) available at\n%% http://www.zjucadcg.cn/dengcai/SR/index.html\n\n%% Load the data matrix from file\nTmp = load('%s','-ascii');\ndata = spconvert(Tmp);\n%% Remove the raw data file to save space\nclear Tmp;\n\n%% Load the affinity matrix from file\nTmp = load('%s','-ascii');\nW = spconvert(Tmp);\n%% Remove the raw data file to save space\nclear Tmp;\n\n%% If 0, all of the dimensions in the adj. matrix are used\nDim = %d\n\noptions = [];\noptions.W = W;\noptions.ReguAlpha = 0.01;\noptions.ReguType = 'Ridge';\noptions.ReducedDim = Dim;\n%% Call the SR code\n[eigvector] = SR_caller(options, data);\n[nSmp,nFea] = size(data);\nif size(eigvector,1) == nFea + 1\n    Projection = [data ones(nSmp,1)]*eigvector;\nelse\n    Projection = data*eigvector;\nend\n%% Save the projection as a matrix\n%s\nfprintf(1,'Finished\\n');\n\n", new Object[] {
        dataMatrixFile.getAbsolutePath(), 
        affMatrixFile.getAbsolutePath(), 
        Integer.valueOf(dimensions), outputStr });
    } catch (Throwable t) {
      t.printStackTrace();
    }
    PrintWriter pw = new PrintWriter(octaveFile);
    pw.println(octaveProgram);
    pw.close();
    


    String commandLine = "octave " + octaveFile.getAbsolutePath();
    LOGGER.fine(commandLine);
    Process octave = Runtime.getRuntime().exec(commandLine);
    
    BufferedReader stdout = new BufferedReader(
      new InputStreamReader(octave.getInputStream()));
    BufferedReader stderr = new BufferedReader(
      new InputStreamReader(octave.getErrorStream()));
    

    StringBuilder output = new StringBuilder("Octave LPP output:\n");
    for (String line = null; (line = stdout.readLine()) != null;) {
      output.append(line).append("\n");
    }
    LOGGER.fine(output.toString());
    
    int exitStatus = -1;
    try {
      exitStatus = octave.waitFor();
    } catch (InterruptedException ie) {
      throw new Error(ie);
    }
    LOGGER.fine("Octave LPP exit status: " + exitStatus);
    

    if (exitStatus != 0) {
      StringBuilder sb = new StringBuilder();
      for (String line = null; (line = stderr.readLine()) != null;) {
        sb.append(line).append("\n");
      }
      throw new IllegalStateException(
        "Octave LPP did not finish normally: " + sb);
    }
  }
  


  private static boolean isOctaveAvailable()
  {
    return SVD.isOctaveAvailable();
  }
  


  private static boolean isMatlabAvailable()
  {
    return SVD.isMatlabAvailable();
  }
}
