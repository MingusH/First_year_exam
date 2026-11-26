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

public abstract class Animal implements Actor, DynamicDisplayInformationProvider {
    // Status for dyr
    protected Boolean isSleeping;

    // Atributter for dyr
    protected int age; // Alder for dyr
    protected int maxEnergy; // Maksimal energi
    protected int energy; // Energi ved start
    protected int energyGain; // Når eat() kaldes
    protected int energyLoss; // Energi mistet ved age()
    protected boolean isRemoved; // er dyret fjernet fra map? f.eks. når det er i en hule.
    protected Location l; // Dyret lokation (opdateres ved act)

    protected Animal() {
        this(0);
    }

    public Animal(int maxEnergy) {
        this.age = 0; // Alle dyr har en alder på 0 ved instanstiering
        this.isSleeping = false;
        this.isRemoved = true;
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy; // Rabbit start på
    }

    // FÆLLES ACT METODE, SOM SKAL KALDER MED SUPER.ACT()
    public void act(World world) {

        // Kalder age for hvert act
        age();

        System.out.println(age);
        // Tjekker om dyret har mistet al energi - og derved dør
        if (energy <= 0) {
            die(world);
            return;
        }

        // Opdaterer lokationen for dyret ved vært act
        try {
            l = world.getLocation(this);
            isRemoved = false;
        } catch (IllegalArgumentException e) {
            isRemoved = true;
        }
        System.out.println("Energy: " + energy);
    }

    // FÆLLES METODER FOR ANIMALS

    //Age-metode, som eksekveres for hvert act-kald
    public void age() {
        age++;
        energy--;
        if (age % 20 == 0) {
            maxEnergy = maxEnergy - energyLoss;
            System.out.println(energyLoss);
        }
    }

    //
    public void die(World world) {
        world.delete(this);
    }

    public void eat(World world) {
        if (world.getNonBlocking(l) instanceof Grass) { // Tjekker om der er grass den kan spise.
            Object eatenGrass = world.getNonBlocking(l);
            world.delete(eatenGrass);
            if (energy < maxEnergy) { // Ændrer energiniveau for at spise.
                if (this.maxEnergy - this.energy > this.energyGain) {
                    this.energy = this.energy + this.energyGain;
                } else {
                    this.energy = this.maxEnergy;
                }
            }
        }
    }

    public void moveTo(World world, Location to) {

        // Bevægelse mod to
        ArrayList<Location> neighbours = new ArrayList<>(world.getEmptySurroundingTiles(l));

        double shortestDistance = Math.hypot(to.getX() - l.getX(), to.getY() - l.getY());
        Location closestLocation = l;
        for (Location loc : neighbours) {
            double distance = Math.hypot(to.getX() - loc.getX(), to.getY() - loc.getY());
            if (distance < shortestDistance) {
                shortestDistance = distance;
                closestLocation = loc;
            }
        }

        if (!closestLocation.equals(l)) {
            world.move(this, closestLocation);
        }
    }

    public void moveAround(World world) {
        Set<Location> neighbours = world.getEmptySurroundingTiles();
        List<Location> list = new ArrayList<>(neighbours);
        Random random = new Random();

        if (list.size() > 0) {
            l = list.get(random.nextInt(list.size())); // Vælger et tilfældigt tomt felt omkring kaninen
            world.move(this, l);
        }
    }

    public int getAge() {
        return age;
    }

    @Override
    public DisplayInformation getInformation() {
        return new DisplayInformation(null);
    }

}
