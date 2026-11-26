package classes;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.awt.Color;


public class Grass implements NonBlocking, Actor, DynamicDisplayInformationProvider {

    @Override
    public void act(World world) {
        grow(world);
    }

    public void grow(World world){
                // Spredning af græs:

        Random r = new Random();
        if (r.nextInt(6) == 1) { // Sandsynligheden for at græs spreder sig pr. act er 1/6

            List<Location> empty_neighbors = new ArrayList<>(); // Laver en ny liste, som kan bruges til
            Set<Location> neighbors = world.getSurroundingTiles(); // Laver et set med alle omkringliggende tiles

            for (Location l : neighbors) {
                if (!world.containsNonBlocking(l)) { //Så længe at nabo-lokationen ikke har en non-blocking
                    empty_neighbors.add(l); //Tilføj grass til listen af tomme naboer
                }
            }

            if (empty_neighbors.size() == 0) {
                return; // Afslutter act hvis der ikke er nogen ledige felter
            }

            Location randomLocation = empty_neighbors.get(r.nextInt(empty_neighbors.size())); //Vælger en tilfældig lokation ud fra vores liste af tomme naboer

            world.setTile(randomLocation, new Grass()); //Sætter objektet (grass) på den tilfældige lokation
        }
    }

    @Override 
    public DisplayInformation getInformation(){
        return new DisplayInformation(Color.GREEN, "grass");
    }
}
