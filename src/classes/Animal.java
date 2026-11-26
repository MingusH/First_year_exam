package classes;

import itumulator.world.Location;
import itumulator.simulator.Actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Random;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.World;
import itumulator.executable.DisplayInformation;


public abstract class Animal implements Actor, DynamicDisplayInformationProvider{
    Boolean isSleeping;

    int age;
    int maxEnergy;
    int energy;
    int energyGain;
    int energyLoss;

    Location l;

    public Animal(){
        isSleeping = false;
        int age = 0;
    }

    public void act(World world){

    }

    public void age(){
        age++;
        energy--;
        if (age % 20 ==0){
            maxEnergy = maxEnergy - energyLoss;
        }
    }

    public void die(World world){
        world.delete(this);
    }

    public void eat(World world){

    }

    public void moveTo(World world, Location to){
        l = world.getLocation(this);

        //Bevægelse mod to
        ArrayList<Location> neighbours = new ArrayList<>(world.getEmptySurroundingTiles(l));

        double shortestDistance = Math.hypot(to.getX() - l.getX(), to.getY() - l.getY());
        Location closestLocation = l;
        for(Location loc : neighbours){
            double distance = Math.hypot(to.getX() - loc.getX(), to.getY() - loc.getY());
            if(distance < shortestDistance){
                shortestDistance = distance;
                closestLocation = loc;
            }
        }

        if(!closestLocation.equals(l)){
            world.move(this,closestLocation);
        }
    }

    public void moveAround(World world) {
        Set<Location> neighbours = world.getEmptySurroundingTiles();
        List<Location> list = new ArrayList<>(neighbours);
        Random random = new Random();

        if(list.size()>0){
            l = list.get(random.nextInt(list.size())); // Vælger et tilfældigt tomt felt omkring kaninen
            world.move(this,l);
        }
    }

    public int getAge(){
        return age;
    }

    

    @Override 
    public DisplayInformation getInformation(){
        return new DisplayInformation(null);
    }
    
}
