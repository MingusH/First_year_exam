package classes;


import java.io.File;


public class InputReader {
    public final String inputFilesPath;
    public File[] inputFiles;

    public InputReader(String inputFilesPath) {
        this.inputFilesPath = inputFilesPath;
        File folder = new File(inputFilesPath);
        this.inputFiles = folder.listFiles();
    }
}