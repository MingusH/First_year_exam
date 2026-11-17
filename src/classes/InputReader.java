package classes;


import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;


public class InputReader {
    private String inputFilesPath;
    public File[] inputFiles;
    private HashMap<String, Program> map;


    public InputReader(String inputFilesPath) {
        this.inputFilesPath = inputFilesPath;
        File folder = new File(inputFilesPath);
        this.inputFiles = folder.listFiles();
        this.map = new HashMap<>();

        if (this.inputFiles == null) {
            throw new IllegalArgumentException("Not a folder: " + inputFilesPath);
        }
        Arrays.sort(this.inputFiles, Comparator.comparing(File::getName));

    }

    public HashMap<String, Program> build(File file){    
        try (Scanner s = new Scanner(file)){
            map.put(file.getName(), new Program(s.nextInt(), 800, 75));
            World world = map.get(file.getName()).getWorld();
            while (s.hasNext()){
                String[] line = s.nextLine().split(" ");
                String object = line[0];
                //String amount = line[1];

                switch(object){
                    case "grass":{
                        Location l = randomLocation(file.getName(), world, true);
                        world.setTile(l, new Grass());
                    } default: {
                        System.out.println("hoy");
                    }
                }
            }
        } catch (FileNotFoundException e){
            System.out.println("File not found" + file.getName());
        }
        return map;
    }

    public Location randomLocation(String filename, World world, Boolean isNonBlocking){
        Random r = new Random();
        int size = map.get(filename).getSize();
        int x =  r.nextInt(size);
        int y = r.nextInt(size);

        Location l = new Location(x, y);

        if (isNonBlocking){
            while(world.containsNonBlocking(l)){
                x = r.nextInt(size); //finder tilfældig x værdi
                y = r.nextInt(size); //finder tilfældig y værdi
            
                l = new Location(x,y);
            } 
        } else {
            while(!world.isTileEmpty(l)){
                x = r.nextInt(size); //finder tilfældig x værdi
                y = r.nextInt(size); //finder tilfældig y værdi
            
                l = new Location(x,y);
            } 
        }
        return l;
    }

    public void setDisplayInformations(){
        DisplayInformation gdi = new DisplayInformation(Color.GREEN, "grass.png");

        for (String key: map.keySet()){
            map.get(key).setDisplayInformation(Grass.class, gdi);
        }
    }
}