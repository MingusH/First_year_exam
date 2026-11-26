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

    // Hjælpemetoder

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
