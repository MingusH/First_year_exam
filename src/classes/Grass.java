package classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Random;

import itumulator.simulator.Actor;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import itumulator.world.Location;

public class Grass implements NonBlocking, Actor {

    @Override
    public void act(World world) {

        // SPREADING OF GRASS
        Set<Location> neighbors = world.getEmptySurroundingTiles();

        if (neighbors.size() == 0) {
            return; // Afslutter act hvis der ikke er nogen ledige felter
        }

        List<Location> list = new ArrayList<>(neighbors); // laver en Liste af dem

        Location randomLocation = list.get(new Random().nextInt(list.size()));

        world.setTile(randomLocation, this);

    }

}
