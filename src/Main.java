import java.awt.Color;

import classes.Grass;
import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;

public class Main {

    public static void main(String[] args) {

        int size = 15;
        int delay = 1000;
        int display_size = 800;

        Program p = new Program(size, display_size, delay); // opretter programmet ud fra de givne parametre.

        DisplayInformation grassDisplayInformation = new DisplayInformation(Color.GREEN, "grass.png");
        p.setDisplayInformation(Grass.class, grassDisplayInformation);

        World world = p.getWorld(); // hiver verdenen ud af det overstående program, så det kan bruges til at sætte

        Location l = new Location(2, 2);
        world.setTile(l, new Grass());

        p.show();

        for (int i = 0; i < 200; i++) {
            p.simulate();
        }

    }
}