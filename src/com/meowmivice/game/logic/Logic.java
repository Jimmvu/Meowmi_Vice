package com.meowmivice.game.logic;

import com.apps.util.Console;
import com.apps.util.Prompter;
import com.meowmivice.game.cast.*;
import com.meowmivice.game.controller.Game;
import com.meowmivice.game.reader.Audio;
import com.meowmivice.game.reader.FileReader;
import com.meowmivice.game.reader.SaveAndLoad;
import com.meowmivice.game.reader.TextParser;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.*;

public class Logic {
    LocationsLoader locLoader  = new LocationsLoader(); // loads from json
    Map<String, Location> mapLocations = locLoader.load(); // creates a map of all the rooms

    private Ascii art = new Ascii(); // used anytime ascii needs to be loaded
    private Player player = new Player(); // new player
    private static Prompter prompter; // prompts the player for input
    private static int count = 0; // counter to check failstate
    private static boolean checkCounter = false; // forces the player to have to check the item,clue before picking up

    private Location currentSpot = mapLocations.get(player.getCurrentLocation()); // the current location of the player

    private static String plug = ""; // used to dynamically display what the user does

    private CommandsLoader commandsLoader = new CommandsLoader(); // get the commands from the Loaderclass

    // Synonym List / prob can refactor the checks somewhere else
    private List<String> go = commandsLoader.verbsObj().get("go");
    private List<String> north = commandsLoader.directionsObj().get("north");
    private List<String> east = commandsLoader.directionsObj().get("east");
    private List<String> south = commandsLoader.directionsObj().get("south");
    private List<String> west = commandsLoader.directionsObj().get("west");

    private List<String> upstairs = commandsLoader.directionsObj().get("upstairs");
    private List<String> downstairs = commandsLoader.directionsObj().get("downstairs");
    private List<String> get = commandsLoader.verbsObj().get("get");
    private List<String> help = commandsLoader.verbsObj().get("help");
    private List<String> quit = commandsLoader.verbsObj().get("quit");
    private List<String> solve = commandsLoader.verbsObj().get("solve");
    private List<String> look = commandsLoader.verbsObj().get("look");
    private List<String> talk = commandsLoader.verbsObj().get("talk");
    private List<String> restart = commandsLoader.verbsObj().get("restart");
    private List<String> map = commandsLoader.verbsObj().get("map");
    private List<String> play = commandsLoader.verbsObj().get("play");
    private List<String> stop = commandsLoader.verbsObj().get("stop");
    private List<String> load = commandsLoader.verbsObj().get("load");
    private List<String> save = commandsLoader.verbsObj().get("save");

    // CONSTRUCTOR
    public Logic(Prompter var1) throws Exception {
        prompter = var1;
        this.player = player; // unnecessary now - remove
    }

    // BUSINESS LOGIC
    // basic logic
    public void logic() throws Exception {
        String input = prompter.prompt(">").trim().toLowerCase();
        Console.clear();
        List<String> textParser = TextParser.textParser(input); // pass the input to the text parser which validates it

        if (textParser.size()>=2 && go.contains(textParser.get(0))) { // checks size and get(0) is in go synonym list
            String direction = getDirection(textParser); // takes the input and passes to function that finds the synonym
            go(textParser, direction);
        }
        else if (look.contains(textParser.get(0))) { // checks look synonym list
            look(textParser);
        } else if (talk.contains(textParser.get(0))) { // checks talk synonym list
            talk(textParser);
        } else if(solve.contains(textParser.get(0))) { // checks solve synonym list
            solve();
        } else if (quit.contains(textParser.get(0))) { // checks quit synonym list
            Game.quit();
        } else if(help.contains(textParser.get(0))){ // checks help synonym list
            Game.help();
        } else if(restart.contains(textParser.get(0))){ // checks restart synonym list
            Game.restart();
        } else if (map.contains(textParser.get(0))){ // checks map synonym list
            art.displayLocation(prompter, player);
            //TODO fix save and load
        } else if (save.contains(textParser.get(0))){ // checks save synonym list
            SaveAndLoad.save();
        } else if (load.contains(textParser.get(0))){ // checks load synonym list
            SaveAndLoad.load();
        }else if (play.contains(textParser.get(0))){ // checks play synonym list / Audio
            Audio.audio();
        } else if (stop.contains(textParser.get(0))){ // checks stop synonym list / Audio
            Audio.stopAudio();
        } else if(textParser.get(0).equals("suspects")){ // checks if get(0) is suspects
            showSuspects();
        } else if(textParser.get(0).equals("inventory")){ // checks if get(0) is inventory
            showInventory();
        }
    }

    // go method
    private void go(List<String> input, String direction) {
        // Allows the user to just go back to previous room
        if (input.get(1).equals("back")){
            String temp = player.getCurrentLocation(); // store the currentlocation to temp
            player.setCurrentLocation(player.getPreviousLocation()); // set currentlocation to prev
            player.setPreviousLocation(temp); // store temp into prev
            checkCounter = false; // prob can remove
        }
        // Move rooms
        else if (currentSpot.getDirections().containsKey(direction)) { // checks if the currentroom contains that direction
            player.setPreviousLocation(player.getCurrentLocation()); // set currentroom to prev
            player.setCurrentLocation(currentSpot.getDirections().get(direction)); //set currentrooom to new room
            checkCounter = false; // prob can remove
        }else {
            plug = "That is an invalid direction to go!";
        }
        currentSpot = mapLocations.get(player.getCurrentLocation()); // get the current spot
        checkCounter = false; // check counter is for the get method, just resets it when they move to a new room
    }

    // get directions / take that synonym and return the base word
    private String getDirection(List<String> input) {
        String direction = "null";
        if(north.contains(input.get(1))) direction = "north";
        else if(east.contains(input.get(1))) direction = "east";
        else if(south.contains(input.get(1))) direction = "south";
        else if(west.contains(input.get(1))) direction = "west";
        else if(upstairs.contains(input.get(1))) direction = "upstairs";
        else if(downstairs.contains(input.get(1))) direction = "downstairs";
        return direction;
    }

    // get item
    private void get(Item currentItem)throws Exception{
        Clue clue = currentItem.getClue();
        System.out.println(currentItem.getDescription());
        System.out.println("What do you want to do?");

        String input = prompter.prompt("> ").toLowerCase().trim();
        List<String> textParser = TextParser.textParser(input); // parse the input

        // mini logic
        if(checkCounter && get.contains(textParser.get(0)) && textParser.get(1).equals("clue")){ //only when checkCounter is true
            player.getInventory().add(currentItem.getClue().getName()); // add to player inventory
            mapLocations.get(player.getCurrentLocation()).setItem(null); // remove from the map

            player.getClues().put(clue.getName(), // add to player clues
                    "Name: " + clue.getName() +
                            "\nDescription: " + clue.getDescription() +
                            "\nObtained from: " + currentItem.getName() +
                            "\nFound in: " + player.getCurrentLocation());
            checkCounter = false; // reset checkCounter
        }
        else if (look.contains(textParser.get(0)) && textParser.get(1).equals("clue")){ // when they look clue
            plug = currentItem.getClue().getDescription();
            checkCounter = true; // set checkCounter to true
            get(currentItem);
        }
        else {
            plug = "Invalid command";
        }
    }

    // look
    private void look(List<String> input) throws Exception {
        NPC currentNpc = currentSpot.getNpc(); // get currentNPC
        Item currentItem = currentSpot.getItem(); // get currentItem

        if (input.size() == 1){ // if they just pass look print out a different statement depending on the contents of the room
            if (currentNpc!=null && currentItem != null) { // if there is an npc and an item
                plug = currentNpc.getName() + " and a " + currentItem.getName() + " are at this location";
            } else if (currentNpc!=null && currentItem==null){ // npc and no item
                plug = currentNpc.getName() + " is at this location.";
            } else if(currentNpc==null && currentItem!=null){ // item and no npc
                plug = "There is a " + currentItem.getName() + " in this location.";
            } else {
                plug = "There is nothing in this location to look at.";
            }
        } else if (input.get(1).equals("item") && currentItem!=null){ // if user looks item
            get(currentItem);
        }
        else {
            plug = "Can't look there";
        }
    }

    // status
    public void showStatus() throws IOException {
        art.ascii(player.getCurrentLocation()); // call ascii to display room ascii of currentlocation
        System.out.println(plug); // print plug
        plug = "";// resets plug
        System.out.println("\033[1;36m===========================");
        System.out.println("You are in the " + player.getCurrentLocation()); // currentlocation
        System.out.println(currentSpot.getDescription());// location description
        System.out.println("Inventory:" +"\033[37m" + player.getInventory() + "\033[1;36m"); // player inventory
        System.out.println("Enter help to see a list of available commands");
        System.out.println("===========================");
        System.out.println("Directions you can go: " +"\033[37m" +
                showDirections(player.getCurrentLocation()) + "\033[0m"); // get directions
    }

    // show directions
    private String showDirections(String currentLocation) {
        Map<String,String> directionsMap =  currentSpot.getDirections(); // create a new map
        return directionsMap.keySet().toString(); // can prob nix this method and display currentSpot.getDirections().toString() in the show status
    }

    // solve
    private void solve() throws Exception {
        // So the player needs at least one clue to solve
        if(player.getInventory().size() == 0){
            FileReader.fileReaderWhite("/Ascii/pdog3.txt");
            prompter.prompt("Press enter to continue");
            Console.clear();
            return;
        }
        //print the ascii of the dog
        FileReader.fileReaderWhite("/Ascii/pdog.txt"); // who dun it

        String culprit = prompter.prompt(">").strip().toLowerCase(); // choose a culprit, prob create a way to display all suspects
        Console.clear();
        FileReader.fileReaderWhite("/Ascii/pdog2.txt"); // whatcha got?
        Set<String> evidence = new HashSet<>(getEvidence()); // user picks out the evidence to provide

        CulpritLoader culpLoader = new CulpritLoader(); // loader class for culprit / move to top
        Culprit reqCulprit = culpLoader.load(); // get the culprit / move to top

        // If you provided all the correct items and guessed the right suspect
        if(culprit.equals(reqCulprit.getName()) && evidence.equals(reqCulprit.getEvidence())){
            System.out.println("Congratulations you solved the mystery!");
            Game.playAgain();
            System.exit(0);
        }
        else {
            count++;
            if (count > 2) {
                System.out.println("You Lost. The culprit got away!");
                Game.playAgain();
                System.exit(0);
            }
            System.out.println("Sorry please collect more clues or try again.");
        }
    }

    // Method to return evidence Set for solving
    private ArrayList<String> getEvidence() {

        ArrayList<String> evidence = new ArrayList<>(); // return evidence
        // create a copy to allow us to add, remove, without affecting the actual inventory
        ArrayList<String> copy = new ArrayList<>(player.getInventory());

        boolean isDone = false;
        // As long as they don't specify to quit, loop continues
        while(!isDone){
            System.out.println("Current Collected Evidence: ");
            int i = 1;
            for (String item: copy) { // prints out the copy
                System.out.println(i + " " + item);
                i++;
            }

            System.out.println("\nEvidence to provide: " + evidence.toString()); // current evidence

            System.out.println("What would you like to do?");
            System.out.println("Add, Remove, Solve, Inventory");

            String choice = prompter.prompt(">").strip().toLowerCase();
            try{
                // Add to the evidence
                if (choice.equals("a") || choice.equals("add")){
                    System.out.println("What index item would you like to add?");
                    String input = prompter.prompt(">").strip().toLowerCase();
                    int index = Integer.parseInt(input) - 1;
                    if(!evidence.contains(copy.get(index))) { // if evidence doesn't contain that item
                        evidence.add(copy.get(index)); // add the item
                        copy.remove(copy.get(index)); // remove it from the clues
                    }
                }
                // Remove evidence
                else if (choice.equals("r") || choice.equals("remove")){
                    System.out.println("What item do you want to remove?");
                    String input = prompter.prompt(">").strip().toLowerCase();
                    if (evidence.contains(input)){ // if evidence contains the input
                        copy.add(input); // add the input to copy
                        evidence.remove(input); // remove input from evidence
                    }
                }
                //TODO bug
                else if (choice.equals("i") || choice.equals(("inventory"))){
                    showInventory();
                }
                // Exit loop
                else if (choice.equals("s") || choice.equals("solve")){
                    isDone = true;
                }
            }
            catch (Exception e){
                System.out.println("Invalid command");
            }
            finally {
                Console.clear();
                System.out.println(plug+"\n");
            }
        }
        return evidence;
    }

    // talk
    private void talk(List<String> input){
        NPC npc = currentSpot.getNpc();
        if (npc!=null && input.size()>=2 && input.get(1).equals("npc")) { //if there is an npc and input is "talk npc"
            ArrayList<String> randDialogueList = npc.getRandDialogue(); // list from obj value
            int rand = new Random().nextInt(randDialogueList.size()); // make random int from size of list
            // TODO bug / only added rand diag
            String randDiag = randDialogueList.get(rand);
            plug = npc.getName() + ": " + randDiag;
            addDialogue(npc.getName(),randDiag); // adds the dialogue to the suspectsList
        }
        else if (npc!=null){
            plug = "Talk to who?";
        }
        else {
            plug = "There is no one to talk to";
        }
    }

    // add dialogue / suspect
    private void addDialogue(String name, String dialogue) {
        if(!player.getSuspects().containsKey(name)){ // if suspectlist doesn't contain the suspect
            player.getSuspects().put(name,dialogue); // add it
        }
    }

    // show current suspects
    private void showSuspects(){
        if(player.getSuspects().size() == 0){
            plug = "No suspects at this time";
        }
        else{
            // user specifies suspect to review
            System.out.println("Who do you want to talk to?");
            System.out.println(player.getSuspects().keySet().toString());
            String input = prompter.prompt(">").trim();
            plug =  player.getSuspects().getOrDefault(input, "Don't know who that is");
        }
    }

    // show current inventory
    private String showInventory() {
        String plug;
        if(player.getClues().size() == 0){
            plug = "Currently no clues collected";
        }
        else {
            // user specifies what clue to look at
            System.out.println("What clue would you like to review?");
            System.out.println(player.getClues().keySet());
            String input = prompter.prompt("> ").trim();
            plug = player.getClues().getOrDefault(input, "Invalid clue");
        }
        return plug;
    }
}