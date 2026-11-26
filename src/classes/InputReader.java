package classes;


import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.util.Random;
import java.util.Scanner;
import classes.Rabbit;


public class InputReader {

    //FELTER
    private File inputFile; // Vores input fil (Fra tema-ugen)
    private Program p;
    private World world;


    //KONSTRUKTØR
    public InputReader(File file) {
        this.inputFile = file; //Sætter fil til den tildelte input-fil

        try (Scanner s = new Scanner(file)){
            this.p = new Program(s.nextInt(), 800, 500); //Sætter feltet p (Program) til size (fra fil) med display størrelse + delay
            this.world = p.getWorld(); //Sætter vores world til at hive programmet ud
        } catch (FileNotFoundException e){
            System.out.println("no such file: " + file.getName()); //Hvis der rejses en undtagelse, så printes dette
        }
    }

    //BUILD-KLASSE
    public void build(){  //Bygger vores verden ud fra filens oplysninger
        try (Scanner s = new Scanner(inputFile)){ 
            s.nextLine(); // Vi skal jo starte fra linje 2 => springer linje 1 over

            while (s.hasNext()){

                String object = s.next(); //Finder det objekt vi ønsker oprettet
                String amountStr = s.next(); //Sætter antallet vi ønsker
                int amount = intervalHandler(amountStr); //Bruges hvis filen angiver interval

                String className = "classes." + object.substring(0, 1).toUpperCase() + object.substring(1); //Opretter en "sti" til oprettelse af de ønskede objekter 

                try {
                    Class myClass = Class.forName(className); // finder klassen ud fra navnet (className)
                    Constructor constructor = myClass.getConstructor(); //Erklærer og initialiserer "constructor" til den offentlige konstruktør for myClass
                    boolean isNonBlocking = NonBlocking.class.isAssignableFrom(myClass); //Tjekker om klassen implementerer nonblocking
                    for (int i = 0; i < amount; i++){ //Opretter objektet i world (på en tile) amount-gange, ift. hvor
                        Location l = world.getRandomLocation(isNonBlocking);
                        world.setTile(l, constructor.newInstance());
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                
            }
        } catch (FileNotFoundException e){
            System.out.println("File not found " + inputFile.getName());
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

    public Program getProgram(){
        return p;
    }

    public World getWorld(){
        return world;
    }

    public File getFile(){
        return inputFile;
    }


}