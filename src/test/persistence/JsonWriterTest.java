package persistence;

import model.Exercise;
import model.Workout;
import model.persistence.JsonReader;
import model.persistence.JsonWriter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Workout w = new Workout();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    void testWriterEmptyWorkout() {
        try {
            Workout w = new Workout();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyWorkout.json");
            writer.open();
            writer.write(w);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyWorkout.json");
            w = reader.read(false);
            assertEquals("My Workout", w.getName());
            assertEquals(0, w.getExercises().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkout() {
        try {
            Workout w = new Workout();
            w.addExercise(new Exercise("T-Bar Rows", "Back",
                    "./data/exerciseTechniques/back/tBarRows.txt",
                    "T-Bar Row Machine & Weight Plates",
                    "./data/exerciseImages/back/tBarRows.jpeg"));
            w.addExercise(new Exercise("Sumo Deadlifts", "Leg-Hamstring",
                    "./data/exerciseTechniques/leg/sumoDeadlifts.txt", "Barbell/Dumbbells",
                    "./data/exerciseImages/leg/sumoDeadlifts.jpeg"));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralWorkout.json");
            writer.open();
            writer.write(w);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralWorkout.json");
            w = reader.read(true);
            assertEquals("My Workout", w.getName());
            List<Exercise> exercises = w.getExercises();
            assertEquals(2, exercises.size());
            checkExercise("T-Bar Rows", "Back", "./data/exerciseTechniques/back/tBarRows.txt",
                    "T-Bar Row Machine & Weight Plates",
                    "./data/exerciseImages/back/tBarRows.jpeg", exercises.get(0));
            checkExercise("Sumo Deadlifts", "Leg-Hamstring",
                    "./data/exerciseTechniques/leg/sumoDeadlifts.txt", "Barbell/Dumbbells",
                    "./data/exerciseImages/leg/sumoDeadlifts.jpeg", exercises.get(1));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}

