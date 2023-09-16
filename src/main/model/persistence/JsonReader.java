package model.persistence;

import model.Event;
import model.EventLog;
import model.Exercise;
import model.Workout;
import org.json.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads workout from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workout from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Workout read(Boolean workoutRead) throws IOException {
        if (workoutRead) {
            EventLog.getInstance().logEvent(new Event("Loaded Workout from " + source));
        }
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseWorkout(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses workout from JSON object and returns it
    private Workout parseWorkout(JSONObject jsonObject) {
        Workout w = new Workout();
        addExercises(w, jsonObject);
        return w;
    }

    // MODIFIES: w
    // EFFECTS: parses thingies from JSON object and adds them to workout
    private void addExercises(Workout w, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("exercises");
        for (Object json : jsonArray) {
            JSONObject nextExercise = (JSONObject) json;
            addExercise(w, nextExercise);
        }
    }

    // MODIFIES: w
    // EFFECTS: parses Exercise from JSON object and adds it to Workout
    private void addExercise(Workout w, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String muscles = jsonObject.getString("muscles");
        String technique = jsonObject.getString("technique");
        String equipment = jsonObject.getString("equipment");
        String image = jsonObject.getString("image");
        Boolean selected = null;
        Exercise exercise = null;
        try {
            selected = jsonObject.getBoolean("selected");
            exercise = new Exercise(name, muscles, technique, equipment, image);
        } catch (JSONException e) {
            exercise = new Exercise(name, muscles, technique, equipment, image);
        }

        if (selected != null) {
            exercise.selectToggle();
        }
        w.addExercise(exercise);
    }
}
