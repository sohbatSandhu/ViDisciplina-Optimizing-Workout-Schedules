package ui;

import model.Event;
import model.EventLog;
import model.Exercise;
import model.Workout;
import model.persistence.JsonReader;
import model.persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Scanner;


// Vi Disciplina application
public class ViApp {
    private static final String JSON_STORE = "./data/myWorkout.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private final String[] options = {"./data/exerciseOptions/chestExercises.json",
            "./data/exerciseOptions/backExercises.json", "./data/exerciseOptions/coreExercises.json",
            "./data/exerciseOptions/armExercises.json", "./data/exerciseOptions/shoulderExercises.json",
            "./data/exerciseOptions/legExercises.json"};
    private List<String> sourceExercise = Arrays.asList(options);

    private Workout myWorkout;
    private List<Workout> exerciseOptions;
    private Scanner input;
    private static final String FORMAT_WORKOUT = "| %-4s | %-9s | %-45s |%n";
    private static final String FORMAT_EXERCISE = "| %-4s | %-10s | %-45s | %-20s | %-40s |%n";

    // EFFECTS: runs the Vi Disciplina application
    public ViApp() {
        runVi();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runVi() {
        boolean keepGoing = true;
        String command = null;

        init();

        while (keepGoing) {
            displayWorkoutMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                savingOption();
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }

    //EFFECTS: prompts the user with the option to save the workout to the file
    private void savingOption() {
        System.out.println("Save Workout to file? (y/n)");
        String save = null;
        save = input.next();
        save = save.toLowerCase();

        if (save.equals("y")) {
            saveWorkout();
        } else if (!save.equals("n")) {
            System.out.println("Selection Invalid : Choose Again");
            savingOption();
        }
        printLog(EventLog.getInstance());
    }

    // MODIFIES: this
    // EFFECTS: initializes saved Exercises and constructs a workout
    private void init() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        myWorkout = new Workout();

        try {
            exerciseOptions = new ArrayList<>();
            initExerciseOptions();
        } catch (IOException e) {
            System.out.println("Unable to read from files: ./data/exerciseOptions");
        }

        input = new Scanner(System.in);
        input.useDelimiter("\n");

    }

    // EFFECTS: reads various data files in exerciseOptions and makes a List<List<Exercise>> as options
    private void initExerciseOptions() throws IOException {
        for (String file : sourceExercise) {
            JsonReader tempJsonReader = new JsonReader(file);
            Workout muscleEx = tempJsonReader.read(false);
            exerciseOptions.add(muscleEx);
        }
    }

    // EFFECTS: displays menu of options to user
    private void displayWorkoutMenu() {
        System.out.println("\n----------------------------MY WORKOUT------------------------------");
        printWorkout();
        System.out.println("\tq -> quit");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.startsWith("i")) {
            try {
                int id = Integer.parseInt(command.substring(2));
                if (id > 0 && id <= myWorkout.getExercises().size()) {
                    showInformation(id);
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println("Selection Option i-{ID}: Format Invalid");
            }

        } else if (command.equals("m")) {
            makeWorkout();
        } else if (command.equals("e")) {
            editWorkoutOptions();
        } else if (command.equals("r")) {
            resetWorkout();
        } else if (command.equals("l")) {
            loadWorkout();
            syncWorkoutWithOptions();
        } else {
            System.out.println("Selection Invalid : Choose Again");
        }
    }

    // MODIFIES: this
    // EFFECTS: resets myWorkout by clearing it and all exerciseOptions is updated
    private void resetWorkout() {
        for (Workout workout : exerciseOptions) {
            for (Exercise exercise : workout.getExercises()) {
                if (exercise.isSelected()) {
                    exercise.selectToggle();
                }
            }
        }
        myWorkout.clear(false);
    }

    // MODIFIES: this
    // EFFECTS: synchronizes exercises in exerciseOptions by changing isSelected() to true
    private void syncWorkoutWithOptions() {
        for (Exercise exercise : myWorkout.getExercises()) {
            String muscles = exercise.getMuscles();
            int target = -1;
            if (muscles.contains("Chest")) {
                target = 0;
            } else if (muscles.contains("Back")) {
                target = 1;
            } else if (muscles.contains("Core")) {
                target = 2;
            } else if (muscles.contains("Arm")) {
                target = 3;
            } else if (muscles.contains("Shoulder")) {
                target = 4;
            } else if (muscles.contains("Leg")) {
                target = 5;
            }
            if (target != -1) {
                syncExerciseInMuscleGroupOptions(target, exercise);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: synchronizes exercises in exerciseOptions by changing isSelected() to true
    private void syncExerciseInMuscleGroupOptions(int target, Exercise exercise) {
        List<Exercise> muscleGroup = exerciseOptions.get(target).getExercises();
        for (Exercise e : muscleGroup) {
            if (e.getName().equals(exercise.getName())) {
                e.selectToggle();
            }
        }
    }

    // REQUIRES: id > 0
    // MODIFIES: this
    // EFFECTS: processes user command
    private void showInformation(int id) {
        boolean goBack = false;  // force entry into loop
        String backCommand = null;
        System.out.println("\tEXERCISE INFORMATION");

        String info = myWorkout.getExercise(id).exerciseInfo();
        System.out.println(info);
        System.out.println("\nb -> Go to Workout");

        while (!goBack) {
            backCommand = input.next();
            backCommand = backCommand.toLowerCase();

            if (backCommand.equals("b")) {
                goBack = true;
            } else {
                System.out.println("Selection Invalid : Choose Again");
            }
        }
    }

    // EFFECTS: prints out the complete Workout
    private void printWorkout() {
        if (!myWorkout.isEmpty()) {
            int id = 1;
            System.out.format(FORMAT_WORKOUT, "ID", "DAY", "NAME");
            for (Exercise exercise : myWorkout.getExercises()) {
                printExerciseWithDay(id, exercise);
                id++;
            }
            System.out.println("--------------------------------------------------------------------");
            System.out.println("\t\t\t\tDAYS NOT MENTIONED ARE REST DAYS");
            System.out.println("\nChoose Workout Options:");
            System.out.println("\ti-{ID} -> Show Exercise Information for Exercise with ID (Example: i-12)");
            System.out.println("\te -> Edit Workout According to Day/Muscle Variation");
            System.out.println("\tr -> Reset Workout");
        } else {
            System.out.println("\n\t\t\t\t NO EXERCISES IN MY WORKOUT !!!");
            System.out.println("\nChoose Workout Options:");
            System.out.println("\tm -> Make New Workout");
            System.out.println("\tl -> Load Workout from File");
        }
    }

    // EFFECTS: prints out the exercise with id, name and the day according to the muscles targeted
    private void printExerciseWithDay(int id, Exercise exercise) {
        System.out.format(FORMAT_WORKOUT, id, exercise.getExerciseDay(), exercise.getName());
    }

    // EFFECTS: prints out the workout editing options and edits the workout
    private void editWorkoutOptions() {
        System.out.println("EDIT WORKOUT ACCORDING TO DAY/MUSCLE TARGETED:");
        System.out.println("\nChoose Edit Options:");
        System.out.println("\t1 -> Edit Monday/Chest Exercises");
        System.out.println("\t2 -> Edit Tuesday/Back Exercises");
        System.out.println("\t3 -> Edit Wednesday/Core Exercises");
        System.out.println("\t4 -> Edit Thursday/Arm Exercises");
        System.out.println("\t5 -> Edit Friday/Shoulder Exercises");
        System.out.println("\t6 -> Edit Saturday/Leg Exercises");
        editWorkout();
    }

    // MODIFIES: this
    // EFFECTS: allows the user to follow the process to edit workout
    private void editWorkout() {
        String edit = "";
        edit = input.next();
        boolean keepGoing = edit.equals("1") || edit.equals("2") || edit.equals("3") || edit.equals("4")
                || edit.equals("5") || edit.equals("6");
        if (keepGoing) {
            int target = Integer.parseInt(edit);
            editWorkoutByMuscle(exerciseOptions.get(target - 1).getExercises());
        } else {
            System.out.println("Invalid Selection: Choose Again");
            editWorkout();
        }
        myWorkout.clear(true);
        updateWorkout();
    }

    // MODIFIES: this
    // EFFECTS: prompts user to the process to chooses exercises to make a workout
    private void makeWorkout() {
        myWorkout = new Workout();
        for (Workout muscleEx : exerciseOptions) {
            List<Exercise> muscleExercises = muscleEx.getExercises();
            editWorkoutByMuscle(muscleExercises);
            myWorkout.addSelectedExercise(muscleExercises);
        }
    }

    // MODIFIES: this
    // EFFECTS: updates the myWorkout
    private void updateWorkout() {
        for (Workout muscleEx : exerciseOptions) {
            List<Exercise> muscleExercises = muscleEx.getExercises();
            myWorkout.addSelectedExercise(muscleExercises);
        }
    }

    // EFFECTS: prints out the Exercise of muscleEx and calls selecting exercises options
    private void editWorkoutByMuscle(List<Exercise> muscleEx) {
        int id = 1;
        System.out.println("\nCHOOSE EXERCISES:\n");
        System.out.format(FORMAT_EXERCISE, "ID", "SELECTED", "NAME", "MUSCLES TARGETED", "EQUIPMENT REQUIRED");
        for (Exercise ex : muscleEx) {
            String summary = ex.exerciseLineSummary(id);
            System.out.println(summary);
            id++;
        }
        selectionProcess(muscleEx);
    }

    // MODIFIES: this
    // EFFECTS: prompts user to select exercises to add into myWorkout
    private void selectionProcess(List<Exercise> muscleEx) {
        String choices = "";  // force entry into loop
        adviceOptions(muscleEx.get(0).getMuscles());
        System.out.println("\n\t(Un)Select exercise options using by inputting the Id in front of each exercise on\n"
                + "\tthe console. When finished input -1\n");

        int id;
        while (!choices.equals("-1")) {
            System.out.println("(Un)Select exercise with ID or finished: ");
            choices = input.next();

            if (!choices.equals("")) {
                id = Integer.parseInt(choices);
                if (id <= muscleEx.size() && id > 0) {
                    muscleEx.get(id - 1).selectToggle();
                    printEdit(id, muscleEx.get(id - 1).isSelected());
                } else if (id == -1) {
                    System.out.println("Editing Completed: NEXT");
                } else {
                    System.out.println("Selection Invalid : Choose Again\n");
                }
            } else {
                System.out.println("Selection Invalid : Choose Again\n");
            }
        }
    }

    // EFFECTS: prints out event whether Exercise has been added/removed from the workout
    private void printEdit(int id, Boolean selected) {
        if (selected) {
            System.out.println("Exercise with ID " + id + ": Added to the Workout\n");
        } else {
            System.out.println("Exercise with ID " + id + ": Removed from the Workout\n");
        }
    }

    // EFFECTS: prints out the exercise advice according to muscle targeted
    private void adviceOptions(String muscles) {
        System.out.println("-----------------------------------------------------------------------------------------");
        if (muscles.contains("Chest")) {
            System.out.println("\tThe Full Chest Muscles include Upper, Middle and Lower Chest Muscles");
            System.out.println("\tADVICE: Choose exercises for MONDAY appropriately such that you exercise\n"
                    + "\t\tthe individual muscles at least twice");
        } else if (muscles.contains("Back")) {
            System.out.println("\tADVICE: Choose at least four exercises for TUESDAY appropriately");
        } else if (muscles.contains("Core")) {
            System.out.println("\tADVICE: Choose at least four exercises for WEDNESDAY appropriately");
        } else if (muscles.contains("Arm")) {
            System.out.println("\tThe Arm Muscles include Biceps, Triceps and Forearm");
            System.out.println("\tADVICE: Choose exercises for THURSDAY appropriately such that you exercise the\n"
                    + "\t\tindividual muscles at least twice. The Forearms are used in every exercise");
        } else if (muscles.contains("Shoulder")) {
            System.out.println("\tADVICE: Choose at least four exercises for FRIDAY appropriately");
        } else if (muscles.contains("Leg")) {
            System.out.println("\tThe Full Leg Muscles include Quads, Hamstring and Calf Muscles");
            System.out.println("\tADVICE: Choose exercises for SATURDAY appropriately such that you exercise the\n"
                    + "\t\tindividual muscles at least twice other than calf muscles which can be exercised once");
        }
        System.out.println("-----------------------------------------------------------------------------------------");
    }

    // EFFECTS: saves the myWorkout to file
    private void saveWorkout() {
        try {
            jsonWriter.open();
            jsonWriter.write(myWorkout);
            jsonWriter.close();
            System.out.println("Saved Workout to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads myWorkout from file
    private void loadWorkout() {
        try {
            myWorkout = jsonReader.read(true);
            System.out.println("Loaded Workout from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: prints out the EventLog with each event having a date and description on the console
    private void printLog(EventLog log) {
        for (Event event : log) {
            System.out.println(event.toString() + "\n");
        }
    }

    // EFFECTS: runs console UI for the Vi Disciplina Application
    public static void main(String[] args) {
        new ViApp();
    }
}
