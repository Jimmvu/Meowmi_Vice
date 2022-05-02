package com.meowmivice.game;
import java.io.IOException;
import java.util.*;
import java.io.FileReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

class TextParser {
    static JSONObject locations() throws IOException, ParseException {

        Object obj = new JSONParser().parse(new FileReader("resources/Json/locations.json"));
        // typecasting obj to JSONObject
        return (JSONObject) obj;
    }

    private static void filter(List<String> userInput) {
        List<String> filterWords = new ArrayList<>(Arrays.asList("the", "to", "with"));
        for (String item : filterWords) {
            userInput.remove(item);
        }
    }

    static List<String> validGo(){
        return new ArrayList<>(Arrays.asList("go", "move", "walk", "run"));
    }

    static List<String> validGet(){
        return new ArrayList<>(Arrays.asList("get", "grab", "obtain", "acquire", "take", "pick up"));
    }

    static List<String> validLook(){
        return new ArrayList<>(Arrays.asList("look", "inspect", "examine", "view", "review", "search", "study", "canvass"));
    }

    static List<String> validQuit(){
        return new ArrayList<>(Arrays.asList("q", "quit", "exit", "leave"));
    }

    static List<String> validHelp(){
        return new ArrayList<>(Arrays.asList("commands", "command", "help", "aid", "assist", "assistance"));
    }

    static List<String> validRestart(){
        return new ArrayList<>(Arrays.asList("restart", "replay", "renew", "redo", "reset", "reboot"));
    }

    static List<String> validSolve(){
        return new ArrayList<>(Arrays.asList("solve", "answer", "resolve", "decipher", "decode", "guess"));
    }

    static List<String> validTalk(){
        return new ArrayList<>(Arrays.asList("talk", "speak", "communicate", "chat", "interact", "gossip"));
    }

    static List<String> validAudio(){
        return new ArrayList<>(Arrays.asList("stop", "play", "pause", "unpause"));
    }

    private static List<String> verbs(){
        return new ArrayList<>(Arrays.asList("go", "stop", "play","pause", "unpause", "pick up", "take", "get", "grab", "obtain", "acquire", "look", "talk", "move", "run", "walk", "solve", "quit", "command", "help", "aid", "assist", "assistance", "commands", "restart", "q", "exit", "leave", "answer", "resolve", "decipher", "decode", "guess", "inspect", "examine", "view", "review", "search", "study", "canvass", "speak", "communicate", "chat", "interact", "gossip", "replay", "renew", "redo", "reset", "reboot"));
    }

    static List<String> textParser(String input) {
        verbs();
        List<String> userInput = new ArrayList<>(Arrays.asList(input.split(" ")));
        filter(userInput);

        if (userInput.size() == 3 && verbs().contains(userInput.get(0) + " " + userInput.get(1))){
            List<String> resultArr = new ArrayList<>();
            resultArr.add(userInput.get(0) + " " + userInput.get(1));
            resultArr.add(userInput.get(2));
            return resultArr;
        }

        if(!(verbs().contains(userInput.get(0)))) {
            System.out.println("That is an invalid input!");
        }
        return userInput;
    }
}