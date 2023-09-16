package model;

import model.persistence.Writable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// Represents a List of Exercises for a User's Workout
public class Workout implements Writable {
    private String name;
    private List<Exercise> workout;         // List of Exercises for Workout

    // MODIFIES: this
    // EFFECTS: constructs a workout with an empty list of exercises
    public Workout() {
        name = "My Workout";
        workout = new ArrayList<>();
    }

    // REQUIRES: requires a non-empty List of Exercises
    // MODIFIES: this
    // EFFECTS: adds exercises to the workout if the Exercise is selected and adds Event to EventLog
    public void addSelectedExercise(List<Exercise> exercises) {
        List<Exercise> edited = new ArrayList<>();
        for (Exercise exercise : exercises) {
            if (exercise.isSelected()) {
                edited.add(exercise);
            }
        }
        workout.addAll(edited);
        EventLog.getInstance().logEvent(new Event("The selected exercises in "
                + exercises.get(0).getExerciseDay()
                + " variation of exercises have been updated to the Workout."));
    }

    // MODIFIES: this
    // EFFECTS: adds an exercise to the workout
    public void addExercise(Exercise exercise) {
        workout.add(exercise);
    }

    // REQUIRES: int id > 0
    // EFFECTS: returns exercise according to exercise's id and adds Event to EventLog
    public Exercise getExercise(int id) throws IndexOutOfBoundsException {
        if (id > 0) {
            EventLog.getInstance().logEvent(new Event("Show Information for Exercise: "
                    + workout.get(id - 1).getName()));
            return workout.get(id - 1);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    // EFFECTS: return true if workout is empty otherwise false
    public boolean isEmpty() {
        return workout.isEmpty();
    }

    // EFFECTS: clears workout of exercises and adds event to EventLog based on the parameter change
    public void clear(Boolean change) {
        workout.clear();
        if (change) {
            EventLog.getInstance().logEvent(new Event("The Workout is reset to accommodate recent changes to"
                    + " the Workout"));
        } else {
            EventLog.getInstance().logEvent(new Event("The Workout is reset"));
        }
    }

    public List<Exercise> getExercises() {
        return workout;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("My Workout", name);
        json.put("exercises", exercisesToJson());
        return json;
    }

    // EFFECTS: returns things in this workout as a JSON array
    private JSONArray exercisesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Exercise e : workout) {
            jsonArray.put(e.toJson());
        }

        return jsonArray;
    }

    public String getName() {
        return name;
    }
}