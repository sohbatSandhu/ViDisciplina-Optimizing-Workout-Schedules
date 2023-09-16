package persistence;


import model.Exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {

    //EFFECTS: check if fields of Exercise are equal
    protected void checkExercise(String name, String muscles, String technique, String equipment, String image,
                                 Exercise exercise) {
        assertEquals(name, exercise.getName());
        assertEquals(muscles, exercise.getMuscles());
        assertEquals(technique, exercise.getTechnique());
        assertEquals(equipment, exercise.getEquipment());
        assertEquals(image, exercise.getImage());
    }
}
