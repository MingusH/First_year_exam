import classes.InputReader;
import itumulator.executable.Program;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        String filePath = "input/week-1";
        InputReader inputReader = new InputReader(filePath);

        inputReader.setDisplayInformations();

        HashMap<String, Program> programs = inputReader.buildAll();

        // w.setTile(new Location(0, 0), new <MyClass>());

        // p.setDisplayInformation(<MyClass>.class, new DisplayInformation(<Color>, "<ImageName>"));

        programs.get("t1-1a.txt").show();
    }
}