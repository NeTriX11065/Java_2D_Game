package persistence;

import model.GameFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import model.Treasure;
import org.json.*;

/**
 * Represents a reader that reads gameFile from JSON data stored in file
 * Reference: JsonSerialization demo
 */

public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads gameFile from file and returns it;
    // throws IOException if an error occurs reading data from file
    public GameFile read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseGameFile(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses gamefile from JSON object and returns it
    private GameFile parseGameFile(JSONObject jsonObject) {
        String name = jsonObject.getString("Name");
        GameFile gf = new GameFile(name);
        addPockets(gf, jsonObject);
        return gf;
    }

    // MODIFIES: gf
    // EFFECTS: parse fortune from JSON object and adds them to GameFile
    private void addPockets(GameFile gf, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Fortune");
        for (Object json : jsonArray) {
            JSONObject nextPocket = (JSONObject) json;
            addTreasure(gf, nextPocket);
        }
    }

    // MODIFIES: gf
    // EFFECTS: Parse message from JSON object and adds it to GameFile
    private void addTreasure(GameFile gf, JSONObject jsonObject) {
        String name = jsonObject.getString("Fortune");
        Treasure treasure = new Treasure(0, 0);
        treasure.addMsg(name);
        gf.addTreasure(treasure);
    }


}

