package classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.awt.Color;

import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

public class Wolf extends Hunter {
    WolfPack pack;
    Location leaderLocation;
    double maxDistanceFromLeader;

    public Wolf(){
        super(60);
        this.energyGain = 10; // Af kød
        this.energyLoss = 5;
        this.maxDistanceFromLeader = 2.5;
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

        if(world.isDay()){ //Hvis det er morgen eller eftermiddag, så går kaninen rundt
            moveAround(world);
            isSleeping = false;
        } else if (world.isNight()){
            isSleeping = true;
        }
        
        

    }


    @Override
    public void moveAround(World world) {
        if(pack.getLeader() == this){ //Hvis denne ulv er packLeader
            super.moveAround(world);

        } else { //Hvis denne ulv ikke er packLeader
            leaderLocation = world.getLocation(pack.getLeader());
            if(Math.hypot(l.getX() - leaderLocation.getX(), l.getY() - leaderLocation.getY()) >= this.maxDistanceFromLeader){ //Hvis den er for langt væk fra pack leader
               moveTo(world, leaderLocation); 

            } else { //hvis den er inden for godkendt radius
                Set<Location> neighbours = world.getEmptySurroundingTiles();
                List<Location> list = new ArrayList<>(neighbours);
                if(list.size()==0){
                    return;
                }

                List<Location> inTerritory = new ArrayList<>();

                for(Location location : list ){
                    if(Math.hypot(location.getX() - leaderLocation.getX(), location.getY() - leaderLocation.getY()) <= this.maxDistanceFromLeader){ //Tjekker om locationen er for langt væk fra territory 
                        inTerritory.add(location);
                    }
                }

                Random random = new Random();
                
                if(inTerritory.size()>0){
                    Location l = inTerritory.get(random.nextInt(inTerritory.size())); // Vælger et tilfældigt tomt felt omkring ulven
                    world.move(this,l);
                }
            }
           
        }

        

    }

    public void setPack(WolfPack pack){
        this.pack = pack;
    }

    @Override 
    public DisplayInformation getInformation(){
        String imageKey = "wolf";
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