package classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class Rabbit implements Actor {
    Burrow burrow;
    Location l;

    //Age & Energy systems
    int age; //Alder i steps
    int maxEnergy;
    int energy;
    int energyGained; //Mængde af energy modtaget af at spise
    

    public Rabbit(){
        this.age = 0;
        this.maxEnergy = 30;
        this.energy = 30;
        this.energyGained = 15;
    }
    
    @Override 
    public void act(World world) {
        age();

        if(energy <= 0){
            die(world);
            return;
        }
        
        try{
            l = world.getLocation(this);
        }catch(IllegalArgumentException e){
            return;
        }
        

        if(world.getCurrentTime() >= 0 && world.getCurrentTime() <= 7){ //Hvis det er morgen eller eftermiddag, så går kaninen rundt
            eat(world);
            occupyBurrow(world);
            move(world);
        } else if (world.getCurrentTime() > 7 && world.getCurrentTime() < 12){
            eat(world);
            if(this.burrow != null){
                moveToBurrow(world, burrow.getLocation(world));
            }
        }

            
        if(world.isNight()){ // Tjekker om det er nat
            //Kaninen skal sove
        }
    }

    public void age(){ //Age & energy system
        this.age++;
        this.energy--;
        if(age % 20*5 == 0);{//Hver femte dag
            this.maxEnergy--;
        } 
    }

    public void die(World world) {
        world.delete(this);
    }

    public void eat(World world) {
        if(world.getNonBlocking(l) instanceof Grass){ //Tjekker om der er grass den kan spise.
            Object eatenGrass = world.getNonBlocking(l);
            world.delete(eatenGrass);
            if(energy < maxEnergy){ //Ændrer energiniveau for at spise.
                if(this.maxEnergy - this.energy > this.energyGained){
                    this.energy = this.energy + this.energyGained;
                } else {
                    this.energy = this.maxEnergy;
                }
            }
        }
    }


    public void moveToBurrow(World world, Location burrowLocation){
        Location l = world.getLocation(this);
        System.out.println(burrowLocation);
        if(l.equals(burrowLocation)){ //hvis kaninen allerede står på sit hul.
            //Call enter burrow method
            return;
        }

        //Bevægelse mod sit hul
        ArrayList<Location> neighbours = new ArrayList<>(world.getEmptySurroundingTiles(l));

        double shortestDistance = Math.hypot(burrowLocation.getX() - l.getX(), burrowLocation.getY() - l.getY());
        Location closestLocation = l;
        for(Location loc : neighbours){
            double distance = Math.hypot(burrowLocation.getX() - loc.getX(), burrowLocation.getY() - loc.getY());
            if(distance < shortestDistance){
                shortestDistance = distance;
                closestLocation = loc;
            }
        }
        if(!closestLocation.equals(l)){
            world.move(this,closestLocation);
        }
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

    public Burrow getBurrow(World world) {
        return burrow;
    }

    public void occupyBurrow(World world) {
        if (this.burrow != null) {
            return; 
        }

        ArrayList<Location> list = new ArrayList<>(world.getEmptySurroundingTiles(world.getLocation(this)));
        for(Location tile : list){
            if(world.getTile(tile) instanceof Burrow b){
                burrow = b;
                b.addRabbit(this);
                break;
            }
        }
    }
}
