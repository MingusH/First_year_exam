package classes;

import java.awt.Color;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.executable.Program;
import itumulator.world.NonBlocking;
import itumulator.simulator.Actor;
import itumulator.world.World;

public class Grass implements NonBlocking, Actor {

    DisplayInformation di = new DisplayInformation(Color.GREEN, "grass.png");


    @Override
    public void act(World world) {

        // Spreading of grass:

    }

}
