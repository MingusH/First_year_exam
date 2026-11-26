package classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.awt.Color;

import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

public class Bear extends Hunter {
    int energyGainBerry;
    Location territory;
    double territorySize;

    public Bear(){
        super(40);
        this.energyGain = 10; // Af kød
        this.energyLoss = 5;
        this.energyGainBerry = 5;
        this.territorySize = 2.5;
    }
    
    @Override 
    public void act(World world) {
        super.age();

        if(energy <= 0){
            die(world);
            return;
        }

        try{
            l = world.getLocation(this);
        }catch(IllegalArgumentException e){
            return;
        }

        if(this.territory == null){
            this.territory = l;
        } 

        if(world.isDay()){ //Hvis det er morgen eller eftermiddag, så går kaninen rundt
            isSleeping = false;
            eatBerry(world);
            moveAround(world);
        } else if (world.isNight()){
            isSleeping = true;
        }
        
        
    }

    @Override
    public void moveAround(World world) {
        Set<Location> neighbours = world.getEmptySurroundingTiles();
        List<Location> list = new ArrayList<>(neighbours);
        if(list.size()==0){
            return;
        }

        List<Location> inTerritory = new ArrayList<>();

        for(Location location : list ){
            if(Math.hypot(location.getX() - territory.getX(), location.getY() - territory.getY()) <= this.territorySize){ //Tjekker om locationen er for langt væk fra territory 
                inTerritory.add(location);
            }
        }

        Random random = new Random();
        
        if(inTerritory.size()>0){
            Location l = inTerritory.get(random.nextInt(inTerritory.size())); // Vælger et tilfældigt tomt felt omkring bjørnen
            world.move(this,l);
        }

    }

    public void eatBerry(World world) {
        Set<Location> neighbours = world.getSurroundingTiles(l);
        List<Location> list = new ArrayList<>(neighbours);

        for(Location location : list){
            if(world.getTile(location) instanceof Berry eatenBerry && eatenBerry.getFruitbearing()){
                eatenBerry.getEaten(world); //Opdaterer berry bush til ikke at have bær
                if(energy < maxEnergy){ //Ændrer energiniveau for at spise.
                    if(this.maxEnergy - this.energy > this.energyGainBerry){
                        this.energy = this.energy + this.energyGainBerry;
                    } else {
                        this.energy = this.maxEnergy;
                    } 
                }
                return;
            }
        }
    }

    @Override 
    public DisplayInformation getInformation(){
        String imageKey = "bear";
        if (age <= 40){
            imageKey = imageKey + "-small";
        } 
        if (isSleeping){
            imageKey = imageKey + "-sleeping";
        }
        // add fungi when it comes


        return new DisplayInformation(Color.WHITE, imageKey);
    }
}
