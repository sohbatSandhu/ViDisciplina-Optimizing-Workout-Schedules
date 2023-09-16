package persistence;


import model.Exercise;
import model.Workout;
import model.persistence.JsonReader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Workout w = reader.read(false);
            fail("IOException expected");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    void testReaderEmptyWorkout() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyWorkout.json");
        try {
            Workout w = reader.read(false);
            assertEquals("My Workout", w.getName());
            assertEquals(0, w.getExercises().size());
            // expected
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkout() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralWorkout.json");
        try {
            Workout w = reader.read(true);
            assertEquals("My Workout", w.getName());
            List<Exercise> exercises = w.getExercises();
            assertEquals(4, exercises.size());
            checkExercise("Incline Chest Press Machine", "Chest-Upper", "./data/exerciseTechniques/chest/inclineChestPress.txt",
                    "Chest Press Machine", "./data/exerciseImages/chest/inclineChestPress.jpeg",
                    exercises.get(0));
            checkExercise("T-Bar Rows", "Back", "./data/exerciseTechniques/back/tBarRows.txt",
                    "T-Bar Row Machine & Weight Plates", "./data/exerciseImages/back/tBarRows.jpeg",
                    exercises.get(1));
            checkExercise("Dumbbell Shoulder Press", "Shoulder",
                    "./data/exerciseTechniques/shoulder/shoulderPress.txt", "Dumbbells & Bench",
                    "./data/exerciseImages/shoulder/shoulderPress.jpeg", exercises.get(2));
            checkExercise("Sumo Deadlifts", "Leg-Hamstring",
                    "./data/exerciseTechniques/leg/sumoDeadlifts.txt", "Barbell/Dumbbells",
                    "./data/exerciseImages/leg/sumoDeadlifts.jpeg", exercises.get(3));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}