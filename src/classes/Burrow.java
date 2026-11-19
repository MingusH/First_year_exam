package classes;

import java.util.ArrayList;

import itumulator.simulator.Actor;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import java.util.ArrayList;
import itumulator.world.Location;
import java.util.Set;
import java.util.Random;

public class Burrow implements NonBlocking, Actor{
    ArrayList<Rabbit> rabbits;
    ArrayList<Rabbit> currentRabbits;
    int maxSize;
    int timeEmpty;

    public Burrow(){
        this.rabbits = new ArrayList<>();
        this.currentRabbits = new ArrayList<>();
        this.maxSize = 4;
        this.timeEmpty = 0;
    }


    @Override     
    public void act(World world){
        if (world.getCurrentTime() > 7){
            checkForRabbits(world);
        }

        if (world.getCurrentTime() == 0){
            releaseRabbits(world);
        }
    }

    void checkForRabbits(World world){
        Set<Location> set = world.getSurroundingTiles(world.getLocation(this));
        for (Location tile: set){
            if (world.getTile(tile) instanceof Rabbit r){
                if (rabbits.contains(r)){
                    currentRabbits.add(r);
                    world.remove(r);
                }
            }
        }
    }

    void releaseRabbits(World world){
        Random r = new Random();
        ArrayList<Location> lst = new ArrayList<>(world.getEmptySurroundingTiles(world.getLocation(this)));
        for (Rabbit rabbit: currentRabbits){
            world.setTile(lst.get(r.nextInt(lst.size())), rabbit);
        }
        currentRabbits.clear();
    }

    Location getLocation(World world){
        return world.getLocation(this);
    }

    public void addRabbit(Rabbit rabbit){
        if (rabbits.size() < maxSize){
            rabbits.add(rabbit);
        }
    }
}
