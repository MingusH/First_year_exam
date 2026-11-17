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
            map.put(file.getName(), new Program(s.nextInt(), 800, 1000));
            World world = map.get(file.getName()).getWorld();
            while (s.hasNext()){
                String object = s.next();
                String amountStr = s.next();
                int amount = intervalHandler(amountStr);

                switch(object){
                    case "grass":{
                        for (int i = 0; i < amount; i++){
                            Location l = randomLocation(file.getName(), world, true);
                            world.setTile(l, new Grass());
                        }
                        break;
                    } default: {
                        System.out.println("hoy");
                    }
                }
            }
        } catch (FileNotFoundException e){
            System.out.println("File not found " + file.getName());
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
        DisplayInformation gdi = new DisplayInformation(Color.GREEN, "grass");

        for (String key: map.keySet()){
            map.get(key).setDisplayInformation(Grass.class, gdi);
        }
    }

    public int intervalHandler(String input){
        int amount;
        if(input.contains("-")){
            Random r = new Random();
            int min = Integer.parseInt(input.substring(0, input.indexOf("-"))); //Laver min værdien om til en integer
            int max = 1 + Integer.parseInt(input.substring(1 + input.indexOf("-"))); //Laver max værdien om til en integer

            amount = r.nextInt(min, max); //Finder en tilfældig integer i intervallet
        } else {
            amount = Integer.parseInt(input); //Laver amountString om til en integer
        }
        return amount;
    }
}