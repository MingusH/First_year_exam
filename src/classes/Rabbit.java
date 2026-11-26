package classes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

public class Rabbit extends Animal{
    private Burrow burrow;
    

    public Rabbit(){
        super();
        this.maxEnergy = 30;
        this.energy = 30;
        this.energyGain = 10;
        this.energyLoss = 5;
    }
    
    @Override 
    public void act(World world) {
        age(); // Kalder age metode => øger alder + tager energi hver 5. dag

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
            isSleeping = false;
            eat(world);
            occupyBurrow(world);
            moveAround(world);
        } else if (world.getCurrentTime() > 7 && world.getCurrentTime() < 12){
            eat(world);
            if(this.burrow != null){
                moveTo(world, burrow.getLocation(world));
            } else { // continues to move around and eat after 7 even if no burrow
                eat(world);
                occupyBurrow(world);
                moveAround(world);
            }
        }

            
        if(world.getCurrentTime() >= 12){ // Tjekker om det er nat
            isSleeping = true;
        }
    }

    public void die(World world) {
        if (burrow != null){
            burrow.remove(this);
        }
        super.die(world);
    }

    @Override
    public void eat(World world) {
        if(world.getNonBlocking(l) instanceof Grass){ //Tjekker om der er grass den kan spise.
            Object eatenGrass = world.getNonBlocking(l);
            world.delete(eatenGrass);
            if(energy < maxEnergy){ //Ændrer energiniveau for at spise.
                if(this.maxEnergy - this.energy > this.energyGain){
                    this.energy = this.energy + this.energyGain;
                } else {
                    this.energy = this.maxEnergy;
                } 
            }
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
            if(world.getTile(tile) instanceof Burrow b && !b.isFull()){
                burrow = b;
                b.addRabbit(this);
                break;
            }
        }
    }


    @Override 
    public DisplayInformation getInformation(){
        String imageKey = "rabbit";
        if (age <= 20){
            imageKey = imageKey + "-small";
        } 
        if (isSleeping){
            imageKey = imageKey + "-sleeping";
        }
        // add fungi when it comes


        return new DisplayInformation(Color.WHITE, imageKey);
    }
}
