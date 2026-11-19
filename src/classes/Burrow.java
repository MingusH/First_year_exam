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
    ArrayList<Object> rabbits;
    ArrayList<Rabbit> current_rabits;
    int maxSize;
    int timeEmpty;

    public Burrow(){
        this.rabbits = new ArrayList<>();
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
            if (world.getTile(tile) instanceof Rabbit){
                if (world.getTile(tile).getBurrow() == this){
                    rabbits.add(world.getTile(tile));
                    world.remove(world.getTile(tile));
                }
            }
        }
    }

    void releaseRabbits(World world){
        Random r = new Random();
        ArrayList<Location> lst = new ArrayList<>(world.getEmptySurroundingTiles(world.getLocation(this)));
        for (Object rabbit: rabbits){
            world.setTile(lst.get(r.nextInt(lst.size())), rabbit);
        }
    }

    Location getLocation(World world){
        return world.getLocation(this);
    }
}
