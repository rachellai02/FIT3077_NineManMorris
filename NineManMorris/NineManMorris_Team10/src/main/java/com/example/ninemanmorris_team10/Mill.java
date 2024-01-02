package com.example.ninemanmorris_team10;

import java.util.ArrayList;

public class Mill {
    private ArrayList<Position> mill = new ArrayList<>();

    public Mill(ArrayList<Position> posList) {
        for (Position pos: posList){
            addToMillObj(pos);
        }
    }

    public ArrayList<Position> getMill() {
        return mill;
    }

    public void addToMillObj(Position position){
        mill.add(position);
    }

}
