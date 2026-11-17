package classes;

import java.util.Arrays;
import java.util.Comparator;
import java.io.File;

public class InputReader {
    public final String inputFilesPath;
    public File[] inputFiles;

    public InputReader(String inputFilesPath) {
        this.inputFilesPath = inputFilesPath;
        File folder = new File(inputFilesPath);
        this.inputFiles = folder.listFiles();

        if (this.inputFiles == null) {
            throw new IllegalArgumentException("Not a folder: " + inputFilesPath);
        }
        Arrays.sort(this.inputFiles, Comparator.comparing(File::getName));

    }
}