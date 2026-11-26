package tests;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import classes.InputReader;
import classes.Grass;
import itumulator.world.Location;
import java.util.Random;
import java.io.File;
import itumulator.executable.Program;
import itumulator.world.World;


class InputReaderTest {
    @Test
    void testInputReaderConstructor() {
        InputReader ir = new InputReader(new File("input/week-1/t1-1a.txt"));
        
        assertTrue(ir.getProgram() instanceof Program);
        assertTrue(ir.getWorld() instanceof World);
        assertEquals(ir.getFile().getName(), "t1-1a.txt");

    }

    @Test
    void testIntervalHandlerStaysInRange() {
        InputReader ir = new InputReader(new File("input/week-1/t1-1a.txt"));
        Random r = new Random();
        int start = r.nextInt(1000);
        int end = r.nextInt(start, 1500);
        for (int i = 0; i < 1000; i++) {
            int result = ir.intervalHandler(start + "-" + end);
            assertTrue(result >= start && result <= end, "Out of range: " + result);
        }
    }

    @Test
    void testIntervalHandlerSingleNumber() {
        InputReader ir = new InputReader(new File("input/week-1/t1-1a.txt"));
        Random r = new Random();
        for (int i = 0; i < 1000; i++){
            assertEquals(ir.intervalHandler(i + ""), i);
        }
    }

    @Test
    void testRandomLocationStaysInsideWorld() {
        InputReader ir = new InputReader(new File("input/week-1/t1-1a.txt"));
        int size = ir.getProgram().getSize();
        World world = new World(size);
        for (int i = 0; i < 1000; i++) {
            Location blocking = world.getRandomLocation(false);
            Location nonBlocking = world.getRandomLocation(true);
            for (Location l : new Location[]{blocking, nonBlocking}) {
                assertTrue(l.getX() >= 0 && l.getX() < size, "x out of bounds: " + l.getX());
                assertTrue(l.getY() >= 0 && l.getY() < size, "y out of bounds: " + l.getY());
            }
        }
    }

    @Test
    void testRandomLocationNonBlockingFindsOnlyFreeTile() {
        InputReader ir = new InputReader(new File("input/week-1/t1-1a.txt"));
        World world = ir.getWorld();
        int size = ir.getProgram().getSize();
        Location free = new Location(size - 1, size - 1);

        // Fyld alle felter med græs undtagen ét
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Location l = new Location(x, y);
                if (!l.equals(free)) {
                    world.setTile(l, new Grass());
                }
            }
        }

        for (int i = 0; i < 100; i++) {
            Location result = world.getRandomLocation(true);
            assertEquals(free, result);
            assertFalse(world.containsNonBlocking(result));
        }
    }

    @Test
    void testRandomLocationBlockingFindsOnlyEmptyTile() {
        InputReader ir = new InputReader(new File("input/week-1/t1-1a.txt"));
        World world = ir.getWorld();
        int size = ir.getProgram().getSize();
        Location free = new Location(0, 0);

        // Fyld alle felter med blokerende objekter undtagen ét
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Location l = new Location(x, y);
                if (!l.equals(free)) {
                    world.setTile(l, new Object());
                }
            }
        }

        for (int i = 0; i < 100; i++) {
            Location result = world.getRandomLocation(false);
            assertEquals(free, result);
            assertTrue(world.isTileEmpty(result));
        }
    }
}