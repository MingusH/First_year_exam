package classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class Rabbit implements Actor {
    Boolean hasEaten;
    int age;
    //Hole hole;
    int energy;
    Location l;

    public Rabbit(){
        this.age = 0;
        this.hasEaten = false;
        this.energy = 20;
    }
    
    @Override 
    public void act(World world) {
        l = world.getLocation(this);

        if(world.getCurrentTime() >= 0 && world.getCurrentTime() <= 7){ //Hvis det er morgen eller eftermiddag, så går kaninen rundt
            eat(world);
            move(world);
        } else if (world.getCurrentTime() > 7 && world.isDay()){
            System.out.println("ehj");
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

    public void eat(World world) {
        if(world.getNonBlocking(l) instanceof Grass){ //Tjekker om der er grass den kan spise.
            Object eatenGrass = world.getNonBlocking(l);
            world.delete(eatenGrass);
            hasEaten = true;
        }
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

    public void move(World world) {
        Set<Location> neighbours = world.getEmptySurroundingTiles();
        List<Location> list = new ArrayList<>(neighbours);
        Random random = new Random();

        if(list.size()>0){
            l = list.get(random.nextInt(list.size())); // Vælger et tilfældigt tomt felt omkring kaninen
            world.move(this,l);
        }
    }
}
