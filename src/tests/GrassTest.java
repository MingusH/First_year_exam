package tests;

import classes.Grass;
import itumulator.world.Location;
import itumulator.world.World;
import java.util.Set;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GrassTest {
    // Tester at act ikke fejler, når alle nabofelter allerede indeholder græs.
    @Test
    void doesNotThrowWhenAllNeighboringTilesAlreadyContainGrass() {
        World world = new World(3);
        Location origin = new Location(1, 1);
        Grass grass = new Grass();
        world.setTile(origin, grass);
        world.setCurrentLocation(origin);
        Set<Location> neighbors = world.getSurroundingTiles(origin);
        for (Location neighbor : neighbors) {
            world.setTile(neighbor, new Grass());
        }

        assertDoesNotThrow(() -> grass.act(world));

        for (Location neighbor : neighbors) {
            assertTrue(world.containsNonBlocking(neighbor));
        }
    }

    // Tester at græsset bliver på sin plads, når verden kun har ét felt.
    @Test
    void doesNotThrowWhenWorldHasNoNeighboringTiles() {
        World world = new World(1);
        Location onlyLocation = new Location(0, 0);
        Grass grass = new Grass();
        world.setTile(onlyLocation, grass);
        world.setCurrentLocation(onlyLocation);

        assertDoesNotThrow(() -> grass.act(world));
        assertSame(grass, world.getNonBlocking(onlyLocation));
    }
}
