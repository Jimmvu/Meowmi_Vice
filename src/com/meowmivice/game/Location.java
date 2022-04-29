package com.meowmivice.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Location {
    private String name;
    private String description;
    private Map<String, String> directions;
    private Map<String, String> clue;
    private Map<String, String> item;
    private Map<String, String> npc;

    public Location(String name, String description, HashMap<String, String> directions) {
        this.name =name;
        this.description = description;
        this.directions = directions;

    }
    public Location(String name, String description, HashMap<String, String> directions,
                    HashMap<String, String> clue, HashMap<String, String> item, HashMap<String, String> npc) {
        this.name =name;
        this.description = description;
        this.directions = directions;
        this.clue = clue;
        this.item = item;
        this.npc = npc;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, String> getDirections() {
        return directions;
    }

    public Map<String, String> getClue() {
        return clue;
    }

    public Map<String, String> getItem() {
        return item;
    }

    public Map<String, String> getNpc() {
        return npc;
    }

    // TODO: toString

}