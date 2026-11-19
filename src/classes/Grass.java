package classes;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Grass implements NonBlocking, Actor {

    @Override
    public void act(World world) {

        // SPREADING OF GRASS

        Random r = new Random();
        if (r.nextInt(8)==1) {
            List<Location> empty_neighbors = new ArrayList();
            Set<Location> neighbors = world.getSurroundingTiles();
            for (Location l: neighbors){
                if (!world.containsNonBlocking(l)){
                    empty_neighbors.add(l);
                }
            }

            if (empty_neighbors.size() == 0) {
                return; // Afslutter act hvis der ikke er nogen ledige felter
            }

            Location randomLocation = empty_neighbors.get(r.nextInt(empty_neighbors.size()));

            world.setTile(randomLocation, new Grass());
        }
    }
}
