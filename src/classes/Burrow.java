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

        if (breedRabbits() && world.getCurrentTime() == 15){
            birthRabbit();
        }
    }

    void checkForRabbits(World world){
        Set<Location> set = world.getSurroundingTiles(world.getLocation(this));
        if (world.getTile(world.getLocation(this)) instanceof Rabbit r){
            if (rabbits.contains(r)){
                currentRabbits.add(r);
                world.remove(r);
            }
        }

        for (Location tile: set){
            if (world.getTile(tile) instanceof Rabbit r){
                if (rabbits.contains(r)){
                    currentRabbits.add(r);
                    world.remove(r);
                }
            }
        }
    }

    Boolean breedRabbits(){
        return currentRabbits.size() >= 2;
    }

    void birthRabbit(){
        currentRabbits.add(new Rabbit());
    }

    void releaseRabbits(World world){
        Random r = new Random();
        int num;
        ArrayList<Location> neighbours = new ArrayList<>(world.getEmptySurroundingTiles(world.getLocation(this)));
        for (Rabbit rabbit: currentRabbits){
            int size = neighbours.size();
            if (size > 0){
                System.out.println(size);
                num = r.nextInt(size);
                world.setTile(neighbours.get(num), rabbit);
                neighbours.remove(num);
            }
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

    public void remove(Rabbit rabbit){
        rabbits.remove(rabbit);
    }
}
