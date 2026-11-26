package classes;

import java.util.ArrayList;


public class WolfPack {
    Wolf packLeader;
    ArrayList<Wolf> pack;

    public WolfPack(){
        pack = new ArrayList<>();
    }

    public void addWolf(Wolf wolf){
        this.pack.add(wolf);
        if (packLeader == null) { // Første ulv der tilføjes bliver leder
            packLeader = wolf;
        }
        wolf.setPack(this); //Sætter ulvens pack
    }

    public Wolf getLeader(){
        return packLeader;
    }

    public ArrayList<Wolf> getPack(){
        return pack;
    }
}
