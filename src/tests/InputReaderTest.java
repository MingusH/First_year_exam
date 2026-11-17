package tests;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import classes.InputReader;


class InputReaderTest {
    @Test
    void testInputReaderConstructor() {
        String testFilePath = "input/week-1";
        InputReader reader = new InputReader(testFilePath);
        String[] expectedFiles = {
                "t1-1a.txt", "t1-1b.txt", "t1-1c.txt",
                "t1-2a.txt", "t1-2b.txt", "t1-2cde.txt", "t1-2fg.txt",
                "t1-3a.txt", "t1-3b.txt",
                "tf1-1.txt"
        };
        
        Arrays.sort(expectedFiles);

        assertEquals(expectedFiles.length, reader.inputFiles.length);
        for (int i = 0; i < expectedFiles.length; i++) {
            assertEquals(expectedFiles[i], reader.inputFiles[i].getName());
        }
    }
}