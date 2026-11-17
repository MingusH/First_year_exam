package classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class Rabbit implements Actor {
    @Override 
    public void act(World world) {
        Boolean hasEaten = false;
        Location l = world.getLocation(this);
        //Location holeLocation; //skal instansieres 

        if(world.getNonBlocking(l) instanceof Grass){ //Tjekker om der er grass den kan spise.
            Object eatenGrass = world.getTile(l);
            world.delete(eatenGrass);
            hasEaten = true;
        }
        if(world.getCurrentTime() <= 0 && world.getCurrentTime() >= 7){ //Hvis det er morgen eller eftermiddag, så går kaninen rundt
            Set<Location> neighbours = world.getEmptySurroundingTiles();
            List<Location> list = new ArrayList<>(neighbours);
            Random random = new Random();
            if(list.size()<0){
                l = list.get(random.nextInt(list.size())); // Vælger et tilfældigt tomt felt omkring kaninen
                world.move(this,l);
            }
        }
            
        
        
        
        
        if(world.isNight()){ // Tjekker om det er nat
            //Kaninen skal sove
        }
        
        

        if(!hasEaten && world.isNight()){
            die(world);
        }
    }

    public void die(World world) {
        world.delete(this);
    }

    public void eat(Grass grass) {
        // Implement the logic for the rabbit eating grass
        // This could involve removing the grass from the world and increasing the rabbit's energy or health.
    }

    public void age() {
        // Implement the logic for the rabbit aging
        // This could involve increasing the rabbit's age and checking if it has reached a certain threshold for death.
    }

    public void reproduce() {
        // Implement the logic for the rabbit reproducing
        // This could involve creating a new Rabbit instance and adding it to the world.
    }

    public void digHole() {
        // Implement the logic for the rabbit digging a hole
        // This could involve creating a new Hole instance and adding it to the world.
    }

    public void move(Location newLocation) {
        // Implement the logic for moving the rabbit to a new location
        // This could involve checking if the new location is valid and updating the rabbit's position in the world.
    }


}
