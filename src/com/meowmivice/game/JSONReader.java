package com.meowmivice.game;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JSONReader {
    private List<Location> locations;
    private JSONParser jsonParser;
    private BufferedReader in;
    private FileReader locReader;
    private JSONArray locFile;
    private Location location;

    public JSONReader() throws IOException, ParseException {
        locations = new ArrayList<>();
        in = new BufferedReader(new InputStreamReader(System.in));
        jsonParser = new JSONParser();
        locReader = new FileReader("resources/Json/locationsmock.json");
        locFile = (JSONArray) jsonParser.parse(locReader);
    }

    public List<Location> locationParser() {
        for (Object o : locFile) {
            JSONObject obj = (JSONObject) o;
            String name = (String) obj.get("roomName");
            String description = (String) obj.get("roomDescription");
            JSONObject locDirections = (JSONObject) obj.get("roomDirections");

            if (((JSONObject) o).containsKey("roomClue")) {
                JSONObject clue = (JSONObject) obj.get()
            }

            location = new Location(name, description, locDirections);
            locations.add(location);

        }
        return locations;
    }
}