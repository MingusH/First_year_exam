package classes;

import java.util.ArrayList;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import java.util.ArrayList;
import itumulator.world.Location;
import java.util.Set;
import java.util.Random;
import java.awt.Button;
import java.awt.Color;


public class Burrow implements NonBlocking, Actor, DynamicDisplayInformationProvider{
    ArrayList<Rabbit> rabbits;
    ArrayList<Rabbit> currentRabbits;
    int maxSize;
    int timeEmpty;
    ArrayList<Burrow> tunnel;

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

        if (isCurrentlyFull() && world.getCurrentTime() == 19){
            digBurrow(world);
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

    void digBurrow(World world){
        if (tunnel == null){
            tunnel = new ArrayList<>();
            tunnel.add(this);
        }

        Location l = world.getRandomLocation(true);
        world.setTile(l, new Burrow());
        if (world.getNonBlocking(l) instanceof Burrow b){
            tunnel.add(b);
            b.connectTunnel(tunnel);
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
        Burrow randomBurrow = this;
        ArrayList<Location> neighbours = new ArrayList<>();

        for (Rabbit rabbit: currentRabbits){
            if (tunnel != null){
                randomBurrow = tunnel.get(r.nextInt(tunnel.size()));
            }

            neighbours = new ArrayList<>(world.getEmptySurroundingTiles(world.getLocation(randomBurrow)));
            int size = neighbours.size();

            if (size > 0){
                num = r.nextInt(size);
                world.setTile(neighbours.get(num), rabbit);
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

    public void connectTunnel(ArrayList<Burrow> tunnel){
        this.tunnel = tunnel;
    }

    public void remove(Rabbit rabbit){
        rabbits.remove(rabbit);
    }

    public Boolean isFull(){
        return rabbits.size() >= maxSize;
    }

    public Boolean isCurrentlyFull(){
        return currentRabbits.size() >= maxSize;
    }

    public DisplayInformation getInformation(){
        if (rabbits.size() > 2){
            return new DisplayInformation(Color.BLACK, "hole");
        }
        return new DisplayInformation(Color.BLACK, "hole-small");
    }
}
