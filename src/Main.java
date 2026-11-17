import classes.InputReader;
import itumulator.executable.Program;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        String filePath = "input/week-1";
        InputReader inputReader = new InputReader(filePath);
        
        HashMap<String, Program> programs = inputReader.build(inputReader.inputFiles[0]);
        inputReader.setDisplayInformations();

        Program p = programs.get("t1-1a.txt");
        p.show();

        
        for (int i = 0; i < 200; i++) {
            p.simulate();
        } // kører 200 runder, alts˚a kaldes 'act' 200 gange for alle placerede
    }
}