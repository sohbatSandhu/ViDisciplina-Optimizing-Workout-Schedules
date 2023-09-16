package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static model.Exercise.FORMAT;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

// Unit tests for Exercise Class
public class TestExercise {
    private Exercise ex1;
    private Exercise ex2;
    private Exercise ex3;
    private Exercise ex4;
    private Exercise ex5;
    private Exercise ex6;


    @BeforeEach
    void runBefore() {
        ex1 = new Exercise("Barbell Front Squats", "Leg-Quads",
                "./data/exerciseTechniques/leg/frontSquat.txt",
                "Barbell/Dumbbells", "./data/exerciseImages/leg/frontSquat.jpeg");
        ex2 = new Exercise("Incline Chest Press Machine", "Chest-Upper",
                "./data/exerciseTechniques/chest/inclineChestPress.txt",
                "Chest Press Machine", "./data/exerciseImages/chest/inclineChestPress.jpeg");
        ex3 = new Exercise("Standing Barbell/Dumbbell Curls", "Arm-Biceps",
                "./data/exerciseTechniques/arm/bicepCurl.txt",
                "Barbell/Dumbbells", "./data/exerciseImages/arm/bicepCurl.jpeg");
        ex4 = new Exercise("Pull Ups", "Back",
                "./data/exerciseTechniques/back/pullUps.txt",
                "Pull Up Bar", "./data/exerciseImages/back/pullUps.jpeg");
        ex5 = new Exercise("Reverse Crunches", "Core",
                "./data/exerciseTechniques/core/revCrunches.txt",
                "None", "./data/exerciseImages/core/revCrunches.jpeg");
        ex6 = new Exercise("Dumbbell Shoulder Press", "Shoulder",
                "./data/exerciseTechniques/shoulder/shoulderPress.txt",
                "Dumbbells & Bench", "./data/exerciseImages/shoulder/shoulderPress.jpeg");
    }

    @Test
    void testConstructor() {
        assertEquals("Barbell Front Squats", ex1.getName());
        assertEquals("Leg-Quads", ex1.getMuscles());
        assertEquals("Barbell/Dumbbells", ex1.getEquipment());
        assertEquals("./data/exerciseTechniques/leg/frontSquat.txt", ex1.getTechnique());
        assertEquals("./data/exerciseImages/leg/frontSquat.jpeg", ex1.getImage());
        assertFalse(ex1.isSelected());
    }

    @Test
    void testSelectToggle() {
        assertFalse(ex1.isSelected());
        ex1.selectToggle();
        assertTrue(ex1.isSelected());
        ex1.selectToggle();
        assertFalse(ex1.isSelected());
    }

    @Test
    void testExerciseLineSummary() {
        ex1.setSelected(true);

        String lineSummary = String.format(FORMAT, 2, "True", ex1.getName(), ex1.getMuscles(),
                ex1.getEquipment());
        assertEquals(lineSummary, ex1.exerciseLineSummary(2));

        ex1.setSelected(true);
        ex1.selectToggle();
        ex1.setSelected(false);

        lineSummary = String.format(FORMAT, 2, "False", ex1.getName(), ex1.getMuscles(),
                ex1.getEquipment());
        assertEquals(lineSummary, ex1.exerciseLineSummary(2));

        lineSummary = String.format(FORMAT, 1, "False", ex2.getName(), ex2.getMuscles(),
                ex2.getEquipment());
        assertEquals(lineSummary, ex2.exerciseLineSummary(1));

        ex2.selectToggle();
        ex2.setSelected(false);

        lineSummary = String.format(FORMAT, 1, "False", ex2.getName(), ex2.getMuscles(),
                ex2.getEquipment());
        assertEquals(lineSummary, ex2.exerciseLineSummary(1));
    }

    @Test
    void testExerciseInfo() {
        String info = "Name: " + ex1.getName() + "\nMuscles Targeted: " + ex1.getMuscles()
                + "\nEquipment Required: " + ex1.getEquipment() + "\nTechnique File Location: "
                + ex1.getTechnique();
        assertEquals(info, ex1.exerciseInfo());
    }

    @Test
    void testExerciseDay() {
        assertEquals("Saturday", ex1.getExerciseDay());
        assertEquals("Monday", ex2.getExerciseDay());
        assertEquals("Thursday", ex3.getExerciseDay());
        assertEquals("Tuesday", ex4.getExerciseDay());
        assertEquals("Wednesday", ex5.getExerciseDay());
        assertEquals("Friday", ex6.getExerciseDay());
    }
}
