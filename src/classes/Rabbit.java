package classes;

import java.awt.Color;
import java.util.ArrayList;
import itumulator.executable.DisplayInformation;
import itumulator.world.Location;
import itumulator.world.World;

public class Rabbit extends Animal {

    private Burrow burrow; // Den hule som kaninen er tilknyttet

    // KONSTRUKTØR FOR RABBIT
    public Rabbit() {
        super(30);
        // this.maxEnergy = 30; //Maks energi for rabbit er 30
        this.energyGain = 10; // Bruges ved eat-metoden
        this.energyLoss = 5; // Den energi der mistes ved age() i Animal
    }

    // Act-Metode - override af Animal act
    @Override
    public void act(World world) {
        // Kalder animal act() og tjekker fælles ting
        super.act(world);

        // Stopper act hvis dyret er fjernet eller dødt
        if (energy <= 0 || isRemoved)
            return;

        if (world.getCurrentTime() >= 0 && world.getCurrentTime() <= 7) { // Hvis "dag" så eksekvér følgende:
            isSleeping = false; // Sover ikke
            eat(world); // Kalder superklassens eat
            occupyBurrow(world);
            moveAround(world);
        } else if (world.getCurrentTime() > 7 && world.getCurrentTime() < 12) {
            eat(world);
            if (this.burrow != null) {
                moveTo(world, burrow.getLocation(world));
            } else { // continues to move around and eat after 7 even if no burrow
                eat(world);
                occupyBurrow(world);
                moveAround(world);
            }
        }

        if (world.getCurrentTime() >= 12) { // Tjekker om det er nat
            isSleeping = true;
        }
    }

    // OBJEKTSPECIFIKKE METODER FOR RABBIT

    @Override //Overrider superklassen die() - rabbit skal fjernes fra burrow
    public void die(World world) {
        if (burrow != null) {
            burrow.remove(this);
        }
        super.die(world);
    }

    //Retunerer den Burrow
    public Burrow getBurrow(World world) {
        return burrow;
    }

    //Tager en plads i en burrow - hvis rabbit endnu ikke har en plads i en
    public void occupyBurrow(World world) {
        if (this.burrow != null) {
            return;
        }

        ArrayList<Location> list = new ArrayList<>(world.getEmptySurroundingTiles(world.getLocation(this)));
        for (Location tile : list) {
            if (world.getTile(tile) instanceof Burrow b && !b.isFull()) {
                burrow = b;
                b.addRabbit(this);
                break;
            }
        }
    }

    @Override
    public DisplayInformation getInformation() {
        String imageKey = "rabbit";
        if (age <= 20) {
            imageKey = imageKey + "-small";
        }
        if (isSleeping) {
            imageKey = imageKey + "-sleeping";
        }
        // add fungi when it comes

        return new DisplayInformation(Color.WHITE, imageKey);
    }
}
