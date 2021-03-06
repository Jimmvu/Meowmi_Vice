package com.meowmivice.game.cast;

// author mm
// Item object
// item is within location in locations2.json

public class Item {

    private String name;
    private String description;
    private Clue clue;

    // an item has a name, description and a Clue object
    Item(String name, String description, Clue clue) {
        setName(name);
        setDescription(description);
        setClue(clue);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Clue getClue() {
        return clue;
    }

    public void setClue(Clue clue) {
        this.clue = clue;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": name= " + getName()
                + ", description= " + getDescription()
                + ", clue= " + getClue().toString();
    }
}