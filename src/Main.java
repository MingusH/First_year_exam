import classes.InputReader;
import itumulator.executable.Program;
import java.io.File;

public class Main {

    public static void main(String[] args) {
        File inputFile = new File("input/week-1/t1-2a.txt");
        InputReader inputReader = new InputReader(inputFile);
        inputReader.build();

        Program p = inputReader.getProgram();
        p.show();

        
        for (int i = 0; i < 200; i++) {
            p.simulate();
        } // kører 200 runder, alts˚a kaldes 'act' 200 gange for alle placerede
    }
}