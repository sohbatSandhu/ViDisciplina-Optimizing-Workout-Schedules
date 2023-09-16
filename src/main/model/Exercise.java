package model;


import model.persistence.Writable;
import org.json.JSONObject;

// Represents an Exercise having a name, muscles targeted, technique, equipment and selection for workout
// the exercise image and technique is used from the cite (https://training.fit/)
public class Exercise implements Writable {
    private String name;            // Name of the exercise
    private String muscles;         // Muscles targeted by the exercise
    private String equipment;       // Equipment required for the exercise
    private String technique;       // reference path for exercise technique description
    private String image;           // reference path for exercise image
    private boolean selected;       // Selected for Workout
    public static final String FORMAT = "| %-4s | %-10s | %-45s | %-20s | %-40s |";

    // REQUIRES: non-empty strings for each argument
    // MODIFIES: this
    // EFFECTS: constructs an exercise with name, muscles targeted and equipment required.
    //          reference paths for both technique and image and false for selected
    public Exercise(String name, String muscles, String technique, String equipment, String image) {
        this.name = name;
        this.muscles = muscles;
        this.technique = technique;
        this.equipment = equipment;
        this.image = image;
        selected = false;
    }

    // MODIFIES: this
    // EFFECTS: selects/unselects an exercise from WorkOut
    //          returns inverted boolean selected
    public void selectToggle() {
        selected = !selected;
    }

    // REQUIRES: int id > 0
    // EFFECTS: returns an individual exercise summary with exercise id
    //          with "True" if exercise is selected for workout
    //          else with "False"
    public String exerciseLineSummary(int id) {
        if (isSelected()) {
            return String.format(FORMAT, id, "True", getName(), getMuscles(), getEquipment());
        } else {
            return String.format(FORMAT, id, "False", getName(), getMuscles(), getEquipment());
        }
    }

    // EFFECTS: returns an individual exercise's full information with Name, Muscles Targeted, Equipment
    //          required and correct technique
    public String exerciseInfo() {
        return "Name: " + getName() + "\nMuscles Targeted: " + getMuscles()
                + "\nEquipment Required: " + getEquipment() + "\nTechnique File Location: "
                + getTechnique();
    }

    public void setSelected(Boolean b) {
        if (selected ^ b) {
            if (b) {
                EventLog.getInstance().logEvent(new Event("Exercise: " + name + " is selected"
                        + " for the Workout."));
            } else {
                EventLog.getInstance().logEvent(new Event("Exercise: " + name + " is unselected"
                        + " for the Workout."));
            }
        }
        selected = b;
    }

    // EFFECTS: returns the exercise day according to the muscle targeted in the exercise
    public String getExerciseDay() {
        if (muscles.contains("Chest")) {
            return "Monday";
        } else if (muscles.contains("Back")) {
            return "Tuesday";
        } else if (muscles.contains("Core")) {
            return "Wednesday";
        } else if (muscles.contains("Arm")) {
            return "Thursday";
        } else if (muscles.contains("Shoulder")) {
            return "Friday";
        } else {
            return "Saturday";
        }
    }

    public String getName() {
        return name;
    }

    public String getMuscles() {
        return muscles;
    }

    public String getTechnique() {
        return technique;
    }

    public String getEquipment() {
        return equipment;
    }

    public String getImage() {
        return image;
    }

    public Boolean isSelected() {
        return selected;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("muscles", muscles);
        json.put("technique", technique);
        json.put("equipment", equipment);
        json.put("image", image);
        json.put("selected", selected);
        return json;
    }
}