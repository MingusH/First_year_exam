package tests;

import classes.Grass;
import classes.InputReader;
import classes.Rabbit;
import itumulator.world.Location;
import itumulator.world.World;
import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KravTester {

    // Krav: Græs kan plantes når input-filerne beskriver det, og placeringen er tilfældig.
    @Test
    void grassIsPlantedFromInputFileAtRandomLocations() {
        // Fast antal græs (t1-1a: "grass 3", t1-1b: "grass 1")
        assertEquals(3, grassLocations(buildWorld("input/week-1/t1-1a.txt")).size());
        assertEquals(1, grassLocations(buildWorld("input/week-1/t1-1b.txt")).size());

        // Interval (t1-1c: "grass 20-30") - antallet skal ligge i intervallet og variere mellem kørsler
        Set<Integer> amountsSeen = new HashSet<>();
        for (int i = 0; i < 20; i++) {
            int amount = grassLocations(buildWorld("input/week-1/t1-1c.txt")).size();
            assertTrue(amount >= 20 && amount <= 30, "Antal græs uden for interval: " + amount);
            amountsSeen.add(amount);
        }
        assertTrue(amountsSeen.size() > 1, "Antallet af græs i et interval bør være tilfældigt");

        // Placeringen skal være tilfældig: samme fil bygget flere gange giver forskellige placeringer
        Set<Set<Location>> placementsSeen = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            Set<Location> locations = grassLocations(buildWorld("input/week-1/t1-1a.txt"));
            for (Location l : locations) {
                assertTrue(l.getX() >= 0 && l.getX() < 5 && l.getY() >= 0 && l.getY() < 5,
                        "Græs placeret uden for verden: " + l);
            }
            placementsSeen.add(locations);
        }
        assertTrue(placementsSeen.size() > 1, "Græsset bør placeres tilfældigt");
    }

    // Krav: Græs kan sprede sig til nabofelterne.
    @Test
    void grassCanSpread() {
        World world = new World(5);
        Location origin = new Location(2, 2);
        Grass grass = new Grass();
        world.setTile(origin, grass);

        // Græs spreder sig med en vis sandsynlighed pr. act, så vi kalder act mange gange
        for (int i = 0; i < 100; i++) {
            world.setCurrentLocation(origin);
            grass.act(world);
        }

        Set<Location> locations = grassLocations(world);
        assertTrue(locations.size() > 1, "Græsset har ikke spredt sig");

        // Nyt græs må kun dukke op på nabofelter til det oprindelige græs
        Set<Location> allowed = world.getSurroundingTiles(origin);
        allowed.add(origin);
        for (Location l : locations) {
            assertTrue(allowed.contains(l), "Græs spredt til et felt der ikke er nabo: " + l);
        }
    }

    // Krav: Dyr kan stå på græs, uden at der sker noget med græsset.
    @Test
    void animalCanStandOnGrassWithoutAffectingIt() {
        World world = new World(3);
        Location l = new Location(1, 1);
        Grass grass = new Grass();
        Rabbit rabbit = new Rabbit();

        world.setTile(l, grass);
        world.setTile(l, rabbit); // Kaninen sættes på samme felt som græsset

        // Begge er på feltet: kaninen som blokerende, græsset som non-blocking
        assertSame(rabbit, world.getTile(l));
        assertSame(grass, world.getNonBlocking(l));
        assertEquals(l, world.getLocation(grass));

        // Græsset kan stadig handle (sprede sig) mens kaninen står på det
        world.setCurrentLocation(l);
        assertDoesNotThrow(() -> grass.act(world));
        assertSame(grass, world.getNonBlocking(l));

        // Når kaninen går væk, ligger græsset stadig uændret
        Location away = new Location(0, 0);
        world.move(rabbit, away);
        assertSame(grass, world.getNonBlocking(l));
        assertEquals(l, world.getLocation(grass));
        assertEquals(away, world.getLocation(rabbit));
    }

    // Krav: Kaniner kan placeres når input-filerne beskriver det, og placeringen er tilfældig.
    @Test
    void rabbitsArePlacedFromInputFileAtRandomLocations() {
        // Fast antal kaniner (t1-2a: "rabbit 1", t1-2b: "rabbit 2", t1-2fg: "rabbit 4")
        assertEquals(1, locationsOf(buildWorld("input/week-1/t1-2a.txt"), Rabbit.class).size());
        assertEquals(2, locationsOf(buildWorld("input/week-1/t1-2b.txt"), Rabbit.class).size());
        assertEquals(4, locationsOf(buildWorld("input/week-1/t1-2fg.txt"), Rabbit.class).size());

        // Interval (t1-2cde: "rabbit 2-4") - antallet skal ligge i intervallet og variere mellem kørsler
        Set<Integer> amountsSeen = new HashSet<>();
        for (int i = 0; i < 20; i++) {
            int amount = locationsOf(buildWorld("input/week-1/t1-2cde.txt"), Rabbit.class).size();
            assertTrue(amount >= 2 && amount <= 4, "Antal kaniner uden for interval: " + amount);
            amountsSeen.add(amount);
        }
        assertTrue(amountsSeen.size() > 1, "Antallet af kaniner i et interval bør være tilfældigt");

        // Placeringen skal være tilfældig: samme fil bygget flere gange giver forskellige placeringer
        Set<Set<Location>> placementsSeen = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            Set<Location> locations = locationsOf(buildWorld("input/week-1/t1-2b.txt"), Rabbit.class);
            for (Location l : locations) {
                assertTrue(l.getX() >= 0 && l.getX() < 5 && l.getY() >= 0 && l.getY() < 5,
                        "Kanin placeret uden for verden: " + l);
            }
            placementsSeen.add(locations);
        }
        assertTrue(placementsSeen.size() > 1, "Kaninerne bør placeres tilfældigt");
    }

    // Krav: Kaniner kan dø.
    @Test
    void rabbitCanDie() {
        // Kaninen fjernes fra verden når den dør
        World world = new World(5);
        Rabbit rabbit = new Rabbit();
        world.setTile(new Location(2, 2), rabbit);
        rabbit.die(world);
        assertFalse(world.contains(rabbit), "Kaninen er stadig i verden efter den er død");

        // Kaninen dør af sult når den ikke har noget at spise
        World hungryWorld = new World(5); // Ingen græs i verden
        Rabbit hungryRabbit = new Rabbit();
        hungryWorld.setTile(new Location(2, 2), hungryRabbit);

        int acts = 0;
        while (hungryWorld.contains(hungryRabbit) && acts < 1000) {
            hungryWorld.setCurrentLocation(hungryWorld.getLocation(hungryRabbit));
            hungryRabbit.act(hungryWorld);
            acts++;
        }

        assertFalse(hungryWorld.contains(hungryRabbit), "Kaninen døde ikke af sult");
        assertTrue(acts > 1, "Kaninen døde med det samme i stedet for at sulte langsomt");
    }

    // Krav: Kaniner kan spise græs, og lever længere når de gør.
    @Test
    void rabbitCanEatGrassAndLivesLonger() {
        // Kaninen spiser græsset den står på
        World world = new World(5);
        Location l = new Location(2, 2);
        Grass grass = new Grass();
        Rabbit rabbit = new Rabbit();
        world.setTile(l, grass);
        world.setTile(l, rabbit);
        world.setCurrentLocation(l);
        rabbit.act(world);
        assertFalse(world.contains(grass), "Kaninen spiste ikke græsset den stod på");

        // En kanin der får græs lever længere end en kanin der ikke gør
        int withoutGrass = actsUntilDeath(false);
        int withGrass = actsUntilDeath(true);
        assertTrue(withGrass > withoutGrass,
                "Kaninen med græs (" + withGrass + " acts) levede ikke længere end uden (" + withoutGrass + " acts)");
    }

    // Krav: Kaninens alder bestemmer hvor meget energi den har.
    @Test
    void rabbitAgeDeterminesEnergy() {
        World world = new World(5);
        Rabbit rabbit = new Rabbit();
        world.setTile(new Location(2, 2), rabbit);

        int startMaxEnergy = rabbit.getMaxEnergy();
        int previousMaxEnergy = startMaxEnergy;

        // Kaninen fodres med græs hver act, så den kun dør af alderdom
        while (world.contains(rabbit) && rabbit.getAge() < 60) {
            Location current = world.getLocation(rabbit);
            if (!world.containsNonBlocking(current)) {
                world.setTile(current, new Grass());
            }
            world.setCurrentLocation(current);
            rabbit.act(world);

            // Max-energien må aldrig stige når kaninen bliver ældre
            assertTrue(rabbit.getMaxEnergy() <= previousMaxEnergy, "Max-energien steg med alderen");
            // Kaninen kan aldrig have mere energi end dens max-energi tillader
            assertTrue(rabbit.getEnergy() <= rabbit.getMaxEnergy(),
                    "Energi (" + rabbit.getEnergy() + ") over max (" + rabbit.getMaxEnergy() + ")");
            previousMaxEnergy = rabbit.getMaxEnergy();
        }

        assertTrue(rabbit.getAge() >= 40, "Kaninen døde før testen kunne se den blive gammel");
        assertTrue(rabbit.getMaxEnergy() < startMaxEnergy, "En gammel kanin har ikke mindre max-energi end en ung");
    }

    // Hjælpemetoder

    // Tæller hvor mange acts en kanin overlever. Med feedGrass lægges der græs under kaninen før hver act.
    private int actsUntilDeath(boolean feedGrass) {
        World world = new World(5);
        Rabbit rabbit = new Rabbit();
        world.setTile(new Location(2, 2), rabbit);

        int acts = 0;
        while (world.contains(rabbit) && acts < 1000) {
            Location current = world.getLocation(rabbit);
            if (feedGrass && !world.containsNonBlocking(current)) {
                world.setTile(current, new Grass());
            }
            world.setCurrentLocation(current);
            rabbit.act(world);
            acts++;
        }
        assertFalse(world.contains(rabbit), "Kaninen døde aldrig");
        return acts;
    }

    private World buildWorld(String path) {
        InputReader ir = new InputReader(new File(path));
        ir.build();
        return ir.getWorld();
    }

    // Finder lokationerne for alt græs der er placeret på kortet
    private Set<Location> grassLocations(World world) {
        return locationsOf(world, Grass.class);
    }

    // Finder lokationerne for alle objekter af en given type der er placeret på kortet
    private Set<Location> locationsOf(World world, Class<?> type) {
        Set<Location> locations = new HashSet<>();
        for (Map.Entry<Object, Location> entry : world.getEntities().entrySet()) {
            if (type.isInstance(entry.getKey()) && entry.getValue() != null) {
                locations.add(entry.getValue());
            }
        }
        return locations;
    }
}
