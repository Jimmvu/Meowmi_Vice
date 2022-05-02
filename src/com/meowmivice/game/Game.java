package com.meowmivice.game;
import com.apps.util.Console;
import com.apps.util.Prompter;
import org.json.simple.JSONObject;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;

class Game {
    private static Prompter prompter;
    private static int count = 0;
    private static String currentLocation = "Kitchen";
    private List<String> inventory = new ArrayList<>();
    private JSONObject locations = TextParser.locations();
    private Map directions = ((Map) locations.get(currentLocation));
    private List<String> audio = TextParser.validAudio();
    private List<String> go = TextParser.validGo();
    private List<String> get = TextParser.validGet();
    private List<String> help = TextParser.validHelp();
    private List<String> quit = TextParser.validQuit();
    private List<String> solve = TextParser.validSolve();
    private List<String> look = TextParser.validLook();
    private List<String> talk = TextParser.validTalk();
    private List<String> restart = TextParser.validRestart();

    // CONSTRUCTOR
    Game(Prompter var1) throws Exception {
        prompter = var1;
    }

    void execute() throws Exception {
        boolean runGame = true;
        Audio.audio();
        welcome();
        createPlayer();
        promptToPlay();
        instructions();
        while (runGame) {
            showStatus();
            logic(directions);
        }
    }

    private void logic(Map area) throws Exception {
        String input = prompter.prompt(">").trim().toLowerCase();
        Console.clear();
        List<String> textParser = TextParser.textParser(input);

        if (go.contains(textParser.get(0))) {
            go(area, textParser);
        } else if (get.contains(textParser.get(0))) {
            get(area, textParser);
        } else if (look.contains(textParser.get(0))) {
            look(area,textParser);
        } else if (talk.contains(textParser.get(0))) {
            talk(area);
        } else if(solve.contains(textParser.get(0))) {
            solve();
        } else if (quit.contains(textParser.get(0))) {
            quit();
        } else if(help.contains(textParser.get(0))){
            help();
        } else if(restart.contains(textParser.get(0))){
            restart();
        } else if(audio.contains(textParser.get(0))){
            audio(textParser.get(0));
        }
    }

    private void audio(String input) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        if ("stop".equals(input) || "pause".equals(input)){
            Audio.pauseAudio();
        } else if("play".equals(input) || "unpause".equals(input)) {
            Audio.resumeAudio();
        }
    }

    private void createPlayer() {
        System.out.println();
        String name = prompter.prompt("Please enter player's name: ").toUpperCase(Locale.ROOT);
        System.out.println();
        System.out.println("Welcome to Meowmi Vice " +  name);
        System.out.println();
    }

    private void go(Map area, List<String> input) {
        if (area.containsKey(input.get(1))) {
            currentLocation = area.get(input.get(1)).toString();
            directions = ((Map) locations.get(currentLocation));
        }else {
            System.out.println("That is an invalid input!");
        }
    }

    private void get(Map area, List<String> input){
        if (area.containsKey("isClue")) {
            inventory.add(area.get("name").toString());
            directions.remove("item");
            System.out.println("You added " + input.get(1));

        } else if (!(area.containsKey("isClue"))){
            System.out.println("There is no clue here");
        } else{
            System.out.println("One must look at the item first to find the clue");
        }
    }

    private void help() throws IOException {
        String help = Files.readString(Path.of("resources/Text/commands.txt"));
        System.out.println(help);
    }

    private void instructions() throws IOException {
        String banner = Files.readString(Path.of("resources/Text/instructions.txt"));
        System.out.println(banner);
    }

    private void look(Map area, List<String> input) throws Exception {
        Map item = ((Map) directions.get("item"));
        Map npc = ((Map) directions.get("npc"));
        if (input.size() == 1){
            if (area.containsKey("npc") && area.containsKey("item")) {
                System.out.println(npc.get("name") + " and a " + item.get("name") + " are at this location.");
            } else if (area.containsKey("npc") && !(area.containsKey("item"))){
                System.out.println(npc.get("name") + " is at this location.");
            } else if(area.containsKey("item") && !(area.containsKey("npc"))){
                System.out.println("There is a " + item.get("name") + " in this location.");
            } else {
                System.out.println("There is nothing in this location to look at.");
            }
            // if user looks at an item, recall the logic so that user can interact with it
        } else if (area.containsKey(input.get(1))){
            Map itemInput = ((Map) area.get(input.get(1)));
            System.out.println(itemInput.get("description"));
            showStatus();
            logic(itemInput);
        }
        else {
            System.out.println("Cant look there");
        }
    }

    private void playAgain() throws Exception {
        boolean validInput = false;
        while (!validInput) {
            String play = prompter.prompt("Please enter [P] to play again or [Q] to exit the game: ","p|q|P|Q","\nThat is not a valid input!\n");
            System.out.println();
            validInput = true;
            if ("P".equals(play) || "p".equals(play)) {
                Console.clear();
                execute();
            } else {
                quit();
            }
        }
    }

    private void promptToPlay() throws InterruptedException {
        boolean validInput = false;
        while (!validInput) {
            String play = prompter.prompt("Please enter [S] to start the game or [Q] to exit the game: ","s|q|S|Q","\nThat is not a valid input!\n");
            Console.clear();
            validInput = true;
            if ("S".equals(play) || "s".equals(play)) {
                continue;

            } else {
                quit();
            }
        }
    }

    private void quit() throws InterruptedException {
        System.out.println("Are you you sure you want to quit? (Y|N)");
        String confirm = prompter.prompt(">").strip().toLowerCase(Locale.ROOT);
        if ("yes".equals(confirm) || "y".equals(confirm)){
            System.out.println("Exiting the game...");
            TimeUnit.SECONDS.sleep(2);
            System.exit(0);
        }
    }

    private void restart() throws Exception {
        System.out.println("Are you you sure you want to restart? (Y|N)");
        String confirm = prompter.prompt(">").strip().toLowerCase(Locale.ROOT);
        if ("yes".equals(confirm) || "y".equals(confirm)){
            System.out.println("Restarting the game...");
            TimeUnit.SECONDS.sleep(2);
            execute();
        }
    }

    private void showStatus(){
        System.out.println("===========================");
        System.out.println("You are in the " + currentLocation);
        System.out.println(directions.get("description"));
        System.out.println("Inventory:" + inventory);
        System.out.println("Enter help to see a list of available commands");
        System.out.println("===========================");
    }

    private void solve() throws Exception {
        if (inventory.size() > 1){
            System.out.println("Who do you think is guilty?");
            String solve = prompter.prompt(">").strip().toLowerCase(Locale.ROOT);
            if ("hamione granger".equals(solve)) {
                System.out.println("Congratulations! You have solved the mystery!");
                playAgain();
            } else {
                count++;
                if (count > 1) {
                    System.out.println("You Lost. The culprit got away!");
                    System.out.println("Exiting the game...");
                    TimeUnit.SECONDS.sleep(2);
                    System.exit(0);
                }
                System.out.println("Sorry please collect more clues or try again.");
            }
        } else {
            int remaining = 2-inventory.size();
            if (remaining == 1){
                System.out.println("Please collect " + remaining + " more clue to try to solve.");
            } else{
                System.out.println("Please collect " + remaining + " more clues to try to solve.");
            }

        }
    }

    private void talk(Map area){
        if (area.containsKey("npc")) {
            Map npc = ((Map) area.get("npc"));
            System.out.println(npc.get("name") + ": " + npc.get("dialogue"));
        } else {
            System.out.println("There is no one to talk to");
        }
    }

    private void welcome() throws IOException {
        String banner = Files.readString(Path.of("resources/Text/splashbanner.txt"));
        System.out.println(banner);
    }
}