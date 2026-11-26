package classes;

import java.awt.Color;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.World;

public class Berry implements Actor, DynamicDisplayInformationProvider {
    Boolean fruitBearing;
    int growSpeed;
    int growthProcess;

    public Berry(){
        this.fruitBearing = true;
        this.growSpeed = 5;
        this.growthProcess = 0;
    }

    @Override
    public void act(World world) {
        if(!fruitBearing){
            this.growthProcess++;
        }

        if(this.growthProcess == this.growSpeed){
            this.growthProcess = 0;
            this.fruitBearing = true;
        }
    }
    
    public void getEaten(World world){
            this.fruitBearing = false;
    }

    public boolean getFruitbearing(){
        return this.fruitBearing;
    }
    
    @Override
    public DisplayInformation getInformation(){
        if(!fruitBearing){
            return new DisplayInformation(Color.GREEN, "bush");
        } else {
            return new DisplayInformation(Color.GREEN, "bush-berries");
        }
        
    }


    
    
}