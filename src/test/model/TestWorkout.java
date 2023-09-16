package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for Workout Class
public class TestWorkout {
    private Exercise ex1;
    private Exercise ex2;
    private Exercise ex3;
    private Workout testWorkout;

    @BeforeEach
    void runBefore() {
        ex1 = new Exercise("Barbell Front Squats", "Leg-Quads",
                "./data/exerciseTechniques/leg/frontSquat.txt",
                "Barbell/Dumbbells", "./data/exerciseImages/leg/frontSquat.jpeg");
        ex2 = new Exercise("Incline Chest Press Machine", "Chest-Upper",
                "./data/exerciseTechniques/chest/inclineChestPress.txt",
                "Chest Press Machine", "./data/exerciseImages/chest/inclineChestPress.jpeg");
        ex3 = new Exercise("Overhead Barbell/Dumbbell Triceps Extensions", "Arm-Triceps",
                "./data/exerciseTechniques/arm/overheadExt.txt",
                "Barbell/Dumbbells & Bench", "./data/exerciseImages/arm/overheadExt.jpeg");
        testWorkout = new Workout();
    }

    @Test
    void testConstructor() {
        assertTrue(testWorkout.isEmpty());
        assertEquals("My Workout", testWorkout.getName());
    }

    @Test
    void testAddSelectedExercise() {
        ex1.selectToggle();
        ex3.selectToggle();
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(ex1);
        exercises.add(ex2);
        exercises.add(ex3);
        testWorkout.addSelectedExercise(exercises);

        assertEquals(ex1, testWorkout.getExercise(1));
        assertEquals(ex3, testWorkout.getExercise(2));
        assertEquals(2, testWorkout.getExercises().size());
        assertFalse(testWorkout.getExercises().contains(ex2));
    }

    @Test
    void testAddExercise() {
        ex1.selectToggle();
        ex3.selectToggle();
        testWorkout.addExercise(ex1);
        testWorkout.addExercise(ex3);

        assertEquals(ex1, testWorkout.getExercise(1));
        assertEquals(ex3, testWorkout.getExercise(2));
        assertEquals(2, testWorkout.getExercises().size());
    }

    @Test
    void testGetExerciseUsingId() {
        ex1.selectToggle();
        ex3.selectToggle();
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(ex1);
        exercises.add(ex2);
        exercises.add(ex3);
        testWorkout.addSelectedExercise(exercises);
        try {
            assertEquals(ex1, testWorkout.getExercise(1));
            assertEquals(ex3, testWorkout.getExercise(2));
            // expected
        } catch (IndexOutOfBoundsException e) {
            fail("IndexOutOfBoundsException caught");
        }
    }

    @Test
    void testGetExerciseUsingIdExceptionThrown() {
        ex1.selectToggle();
        ex3.selectToggle();
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(ex1);
        exercises.add(ex2);
        exercises.add(ex3);
        testWorkout.addSelectedExercise(exercises);
        try {
            Exercise testExercise = testWorkout.getExercise(0);
            assertEquals(ex1, testExercise);
            fail("Exception not thrown");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    void testClear() {
        ex1.selectToggle();
        ex3.selectToggle();
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(ex1);
        exercises.add(ex2);
        testWorkout.addSelectedExercise(exercises);
        testWorkout.clear(false);

        assertTrue(testWorkout.isEmpty());
        exercises.add(ex1);
        exercises.add(ex2);
        testWorkout.addSelectedExercise(exercises);
        testWorkout.clear(true);
        assertTrue(testWorkout.isEmpty());
    }

    @Test
    void testIsEmpty() {
        assertTrue(testWorkout.isEmpty());
        ex1.selectToggle();
        ex3.selectToggle();
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(ex1);
        exercises.add(ex2);
        testWorkout.addSelectedExercise(exercises);
        assertFalse(testWorkout.isEmpty());
    }
}
