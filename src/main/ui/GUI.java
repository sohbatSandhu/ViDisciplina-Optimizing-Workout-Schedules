package ui;

import model.Event;
import model.EventLog;
import model.Exercise;
import model.Workout;
import model.persistence.JsonReader;
import model.persistence.JsonWriter;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Vi Disciplina application's Graphical UI
public class GUI extends JFrame implements ActionListener {
    private static final String JSON_STORE = "./data/myWorkout.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private final String[] options = {"./data/exerciseOptions/chestExercises.json",
            "./data/exerciseOptions/backExercises.json", "./data/exerciseOptions/coreExercises.json",
            "./data/exerciseOptions/armExercises.json", "./data/exerciseOptions/shoulderExercises.json",
            "./data/exerciseOptions/legExercises.json"};
    private final List<String> sourceExercise = Arrays.asList(options);

    private Workout myWorkout;
    private List<Workout> exerciseOptions;
    private final String[] columnsWorkout = {"Day", "Name", "Muscles Targeted"};
    private String[][] workoutData;
    private int selectionIndex;
    private final String[] columnsExercise = {"Selected", "Name", "Muscles Targeted", "Equipment Required"};
    private Object[][] exerciseData;
    private int selectionMuscle = 0;

    private JPanel mainMenuPanel;
    private JPanel workoutPanel;
    private JPanel infoPanel;
    private JPanel editOptionsPanel;
    private JPanel editWorkoutPanel;
    private JPanel nextEditPanel;

    private JButton makeButton;
    private JButton loadButton;
    private JButton quitAppButton;

    private JButton showInfoButton;
    private JButton editButton;
    private JButton resetButton;

    private JButton editChestButton;
    private JButton editBackButton;
    private JButton editCoreButton;
    private JButton editArmButton;
    private JButton editShoulderButton;
    private JButton editLegButton;

    private JButton doneEditButton;
    private JButton nextEditingButton;
    private JButton goBackButton;

    //EFFECTS: Makes a new JFrame with different attributes
    public GUI() {
        super("Vi Disciplina");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setPreferredSize(new Dimension(600, 600));
        init();

        createEmptyWorkoutPanel();
        initEmptyWorkoutPanel();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(true);
        mainMenuPanel.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes saved Exercises and constructs a workout
    private void init() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        try {
            exerciseOptions = new ArrayList<>();
            initExerciseOptions();
        } catch (IOException e) {
            System.out.println("Unable to read from files: ./data/exerciseOptions");
        }
        EventLog.getInstance().clear();
        myWorkout = new Workout();
    }

    // EFFECTS: reads various data files in exerciseOptions and makes a List<List<Exercise>> as options
    private void initExerciseOptions() throws IOException {
        for (String file : sourceExercise) {
            JsonReader tempJsonReader = new JsonReader(file);
            Workout muscleEx = tempJsonReader.read(false);
            exerciseOptions.add(muscleEx);
        }
    }

    // MODIFIES: this
    // EFFECTS: Makes the main menu panel when workout is empty
    private void createEmptyWorkoutPanel() {
        mainMenuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 550, 5));
        mainMenuPanel.setBackground(new Color(74, 132, 159));

        JLabel labelHeader = new JLabel("Vi Disciplina");
        JLabel labelSub = new JLabel("IMPROVE");
        JLabel appIconImage = new JLabel();
        JLabel quote = new JLabel("NO EXERCISES CURRENTLY CHOSEN FOR WORKOUT");

        addLabel(labelHeader, 50, mainMenuPanel, Font.BOLD);
        addLabel(labelSub, 20, mainMenuPanel, Font.BOLD);
        addImageToLabel(appIconImage, mainMenuPanel);
        addLabel(quote, 16, mainMenuPanel, Font.ITALIC);

        initEmptyWorkoutMenuButtons();
        addButtonsEmptyWorkoutMenu(makeButton, loadButton, quitAppButton);
        addActionToButtonsEmptyWorkout();
    }

    // MODIFIES: this
    // EFFECTS: Creates the workout panel when the myWorkout is not empty
    private void createLoadedWorkoutPanel() {
        workoutPanel = new JPanel();
        workoutPanel.setBackground(new Color(74, 132, 159));

        JLabel labelHeader = new JLabel("My Workout");
        addLabel(labelHeader, 35, workoutPanel, Font.BOLD);

        createWorkoutTableData();
        createJTableWorkout();

        createLoadedWorkoutAdvisory();

        initLoadedWorkoutMenuButtons();
        addButtonsLoadedWorkoutMenu(showInfoButton, editButton, resetButton, quitAppButton);
        addActionToButtonsLoadedWorkout();
    }

    // MODIFIES: this
    // EFFECTS: creates the infoPanel for the selected exercise and displays all the information about the exercise
    private void createInformationPanel() {
        infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 550, 5));
        infoPanel.setBackground(new Color(74, 132, 159));

        JLabel labelHeader = new JLabel("Exercise Information");
        addLabel(labelHeader, 30, infoPanel, Font.BOLD);

        exerciseInformation();

        initInfoPanelMenuButtons();
        addButtonsInfoPanelMenu(goBackButton);
        addActionToButtonsInfoPanel();
    }

    // MODIFIES: this
    // EFFECTS: creates the edit options menu where we can choose to edit myWorkout
    //          by day or muscle targeted variations
    private void createEditOptionsPanel() {
        editOptionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 550, 10));
        editOptionsPanel.setBackground(new Color(74, 132, 159));

        JLabel labelHeader = new JLabel("Edit Workout");
        addLabel(labelHeader, 40, editOptionsPanel, Font.BOLD);
        JLabel labelSub = new JLabel("\nAccording To Day or Muscle Targeted");
        addLabel(labelSub, 30, editOptionsPanel, Font.ITALIC);

        initEditOptionsMenuButtons();
        addButtonsEditOptionsMenu(editChestButton, editBackButton, editCoreButton, editArmButton, editShoulderButton,
                editLegButton);
        addActionToButtonsEditOptions();
    }

    // MODIFIES: this
    // EFFECTS: Creates the edit workout panel where we can edit myWorkout
    //          according to the chosen day or muscle targeted variation
    private void createEditWorkoutByMusclePanel(int i) {
        editWorkoutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 550, 10));
        editWorkoutPanel.setBackground(new Color(74, 132, 159));

        JLabel labelHeader = new JLabel("Edit Workout");
        addLabel(labelHeader, 35, editWorkoutPanel, Font.BOLD);
        JLabel labelSub = new JLabel("\nChoose Exercises: According To Day or Muscle Targeted");
        addLabel(labelSub, 20, editWorkoutPanel, Font.ITALIC);

        createExercisesByMuscleTableData(i);
        createJTableExercises(editWorkoutPanel);

        createEditAdvisory(i, editWorkoutPanel);

        initEditWorkoutMenuButtons();
        addButtonsEditWorkoutMenu(doneEditButton);
        addActionToButtonsEditWorkout();
    }

    // MODIFIES: this
    // EFFECTS: Creates the edit workout panel where we can edit myWorkout
    //          according to the chosen day or muscle targeted variation
    private void createNextEditByMusclePanel(int i) {
        nextEditPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 550, 10));
        nextEditPanel.setBackground(new Color(74, 132, 159));

        JLabel labelHeader = new JLabel("Edit Workout");
        addLabel(labelHeader, 35, nextEditPanel, Font.BOLD);
        JLabel labelSub = new JLabel("\nChoose Exercises: According To Day or Muscle Targeted");
        addLabel(labelSub, 20, nextEditPanel, Font.ITALIC);

        createExercisesByMuscleTableData(i);
        createJTableExercises(nextEditPanel);

        createEditAdvisory(i, nextEditPanel);

        initNextEditMenuButtons();
        addButtonsNextEditMenu(nextEditingButton);
        addActionToButtonsNextEdit();
    }

    // EFFECTS: Initializes main menu buttons and gives them labels
    private void initEmptyWorkoutMenuButtons() {
        makeButton = new JButton("Make New Workout");
        loadButton = new JButton("Load Workout From File");
        quitAppButton = new JButton("Quit Application");
    }

    // EFFECTS: Initializes loaded workout menu buttons and gives them labels
    private void initLoadedWorkoutMenuButtons() {
        showInfoButton = new JButton("Show Selected Exercise Information");
        editButton = new JButton("Edit Workout According to Day/Muscle Variation");
        resetButton = new JButton("Reset Workout");
    }

    // EFFECTS: Initializes infoPanel menu buttons and gives them labels
    private void initInfoPanelMenuButtons() {
        goBackButton = new JButton("Go Back To Workout");
    }

    // EFFECTS: Initializes edit options menu buttons and gives them labels
    private void initEditOptionsMenuButtons() {
        editChestButton = new JButton("Edit Monday/Chest Exercises");
        editBackButton = new JButton("Edit Tuesday/Back Exercises");
        editCoreButton = new JButton("Edit Wednesday/Core Exercises");
        editArmButton = new JButton("Edit Thursday/Arm Exercises");
        editShoulderButton = new JButton("Edit Friday/Shoulder Exercises");
        editLegButton = new JButton("Edit Saturday/Leg Exercises");
    }

    // EFFECTS: Initializes edit workout menu buttons and gives them labels
    private void initEditWorkoutMenuButtons() {
        doneEditButton = new JButton("Done Editing");
    }

    // EFFECTS: Initializes edit workout menu buttons and gives them labels
    private void initNextEditMenuButtons() {
        nextEditingButton = new JButton("Done: Next Editing");
    }

    // EFFECTS: Calls the addButton method for each argument for mainMenuPanel
    private void addButtonsEmptyWorkoutMenu(JButton b1, JButton b2, JButton b3) {
        addButton(b1, mainMenuPanel);
        addButton(b2, mainMenuPanel);
        addButton(b3, mainMenuPanel);
    }

    // EFFECTS: Calls the addButton method for each argument for workoutPanel
    private void addButtonsLoadedWorkoutMenu(JButton b1, JButton b2, JButton b3, JButton b4) {
        addButton(b1, workoutPanel);
        addButton(b2, workoutPanel);
        addButton(b3, workoutPanel);
        addButton(b4, workoutPanel);
    }

    // EFFECTS: Calls the addButton method for each argument for infoPanel
    private void addButtonsInfoPanelMenu(JButton b) {
        addButton(b, infoPanel);
    }

    // EFFECTS: Calls the addButton method for each argument for editOptionsPanel
    private void addButtonsEditOptionsMenu(JButton b1, JButton b2, JButton b3, JButton b4, JButton b5, JButton b6) {
        addButton(b1, editOptionsPanel);
        addButton(b2, editOptionsPanel);
        addButton(b3, editOptionsPanel);
        addButton(b4, editOptionsPanel);
        addButton(b5, editOptionsPanel);
        addButton(b6, editOptionsPanel);
    }

    // EFFECTS: Calls the addButton method for each argument for editWorkoutPanel
    private void addButtonsEditWorkoutMenu(JButton b) {
        addButton(b, editWorkoutPanel);
    }

    // EFFECTS: Calls the addButton method for each argument for editWorkoutPanel
    private void addButtonsNextEditMenu(JButton b) {
        addButton(b, nextEditPanel);
    }


    // MODIFIES: this
    // EFFECTS: Sets each button in mainMenuPanel to their respective action
    private void addActionToButtonsEmptyWorkout() {
        makeButton.addActionListener(this);
        makeButton.setActionCommand("m");
        loadButton.addActionListener(this);
        loadButton.setActionCommand("l");
        quitAppButton.addActionListener(this);
        quitAppButton.setActionCommand("q");
    }

    // MODIFIES: this
    // EFFECTS: Sets each button in workoutPanel to their respective action
    private void addActionToButtonsLoadedWorkout() {
        showInfoButton.addActionListener(this);
        showInfoButton.setActionCommand("i");
        editButton.addActionListener(this);
        editButton.setActionCommand("e");
        resetButton.addActionListener(this);
        resetButton.setActionCommand("r");
    }

    // MODIFIES: this
    // EFFECTS: Sets each button in infoPanel to their respective action
    private void addActionToButtonsInfoPanel() {
        goBackButton.addActionListener(this);
        goBackButton.setActionCommand("b");
    }

    // MODIFIES: this
    // EFFECTS: Sets each button in editOptionsPanel to their respective action
    private void addActionToButtonsEditOptions() {
        editChestButton.addActionListener(this);
        editChestButton.setActionCommand("0");
        editBackButton.addActionListener(this);
        editBackButton.setActionCommand("1");
        editCoreButton.addActionListener(this);
        editCoreButton.setActionCommand("2");
        editArmButton.addActionListener(this);
        editArmButton.setActionCommand("3");
        editShoulderButton.addActionListener(this);
        editShoulderButton.setActionCommand("4");
        editLegButton.addActionListener(this);
        editLegButton.setActionCommand("5");
    }

    // MODIFIES: this
    // EFFECTS: Sets each button in editWorkoutPanel to their respective action
    private void addActionToButtonsEditWorkout() {
        doneEditButton.addActionListener(this);
        doneEditButton.setActionCommand("d");
    }

    // MODIFIES: this
    // EFFECTS: Sets each button in nextEditPanel to their respective action
    private void addActionToButtonsNextEdit() {
        nextEditingButton.addActionListener(this);
        nextEditingButton.setActionCommand("c");
    }

    //MODIFIES: this
    //EFFECTS: creates JTable for workout and adds it to workoutPanel
    private void createJTableWorkout() {
        JTable workoutTable = new JTable(workoutData, columnsWorkout);
        workoutTable.setPreferredScrollableViewportSize(new Dimension(580, 200));
        workoutTable.setFillsViewportHeight(true);
        workoutTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        workoutTable.setRowSelectionAllowed(true);
        workoutTable.setGridColor(new Color(28, 53, 45));
        workoutTable.setFont(new Font("Arial", Font.BOLD, 12));
        workoutTable.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

        ListSelectionModel rowSM = workoutTable.getSelectionModel();
        rowSM.addListSelectionListener(e -> {
            //Ignore extra messages.
            if (e.getValueIsAdjusting()) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                selectionIndex = lsm.getMinSelectionIndex();
            }
        });

        JScrollPane scrollPane = new JScrollPane(workoutTable);
        workoutPanel.add(scrollPane);
    }

    //MODIFIES: this
    //EFFECTS: creates JTable for day or muscle variation exercises and adds it to editWorkoutPanel
    private void createJTableExercises(JPanel panel) {
        JTable exerciseTable = new JTable(new ExerciseTableModel());
        exerciseTable.setPreferredScrollableViewportSize(new Dimension(580, 150));
        exerciseTable.setFillsViewportHeight(true);
        exerciseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        exerciseTable.setRowSelectionAllowed(true);
        exerciseTable.setGridColor(new Color(28, 53, 45));
        exerciseTable.setFont(new Font("Arial", Font.BOLD, 12));
        exerciseTable.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

        ListSelectionModel rowSM = exerciseTable.getSelectionModel();
        rowSM.addListSelectionListener(e -> {
            //Ignore extra messages.
            if (e.getValueIsAdjusting()) {
                return;
            }

            ListSelectionModel lsm = (ListSelectionModel) e.getSource();
            lsm.getMinSelectionIndex();
        });

        JScrollPane scrollPane = new JScrollPane(exerciseTable);
        panel.add(scrollPane);
    }


    // EFFECTS: creates a table for the selected exercises from myWorkout
    private void createWorkoutTableData() {
        if (!myWorkout.isEmpty()) {
            List<String[]> workoutTableData = new ArrayList<>();
            for (Exercise exercise : myWorkout.getExercises()) {
                String[] line = {exercise.getExerciseDay(), exercise.getName(), exercise.getMuscles()};
                workoutTableData.add(line);
            }
            workoutData = workoutTableData.toArray(new String[0][0]);
        }
    }

    // EFFECTS: creates a table for the exercises in exerciseOptions
    //          according to the day or muscle targeted variation
    private void createExercisesByMuscleTableData(int i) {
        List<Object[]> exerciseTableData = new ArrayList<>();
        for (Exercise exercise : exerciseOptions.get(i).getExercises()) {
            Object[] line = {exercise.isSelected(), exercise.getName(), exercise.getMuscles(), exercise.getEquipment()};
            exerciseTableData.add(line);
        }
        exerciseData = exerciseTableData.toArray(new Object[0][0]);
    }

    // EFFECTS: creates the information from the exercise parameter
    private void exerciseInformation() {
        Exercise exercise = myWorkout.getExercise(selectionIndex + 1);
        JLabel name = new JLabel("Name: " + exercise.getName());
        JLabel muscles = new JLabel("Muscles Targeted: " + exercise.getMuscles());
        JLabel equipment = new JLabel("Equipment Required: " + exercise.getEquipment());
        JLabel image = new JLabel();

        addLabel(name, 14, infoPanel, Font.ITALIC);
        addLabel(muscles, 14, infoPanel, Font.ITALIC);
        addLabel(equipment, 14, infoPanel, Font.ITALIC);
        ImageIcon imageIcon = new ImageIcon(
                new ImageIcon(exercise.getImage()).getImage().getScaledInstance(500,
                        240, Image.SCALE_DEFAULT));
        image.setIcon(imageIcon);
        infoPanel.add(image);
        addTechniqueTextArea(exercise);
    }

    // EFFECTS: creates the JTextArea for the exercise.getTechnique()
    private void addTechniqueTextArea(Exercise exercise) {
        JTextArea technique = new JTextArea();
        technique.setColumns(39);
        technique.setLineWrap(true);
        technique.setRows(9);
        technique.setWrapStyleWord(true);
        technique.setEditable(false);
        technique.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED), BorderFactory.createLoweredBevelBorder()));

        FileReader reader = null;
        try {
            reader = new FileReader(exercise.getTechnique());
            technique.read(reader, exercise.getTechnique());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        technique.setFont(new Font("Georgia", Font.PLAIN, 15));
        technique.setBackground(new Color(128, 186, 215, 255));
        technique.setOpaque(true);
        JScrollPane scrollPane = new JScrollPane(technique);
        infoPanel.add(scrollPane);
    }

    // MODIFIES: this
    // EFFECTS: adds button with appropriate font to appropriate panel
    private void addButton(JButton b, JPanel panel) {
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setBackground(new Color(80, 114, 151));
        b.setOpaque(true);
        panel.add(b);
    }

    // EFFECTS: Creates the welcome text label and adds it to the appropriate panel
    private void addLabel(JLabel label, int i, JPanel panel, int font) {
        label.setFont(new Font("Bebas Neue", font, i));
        panel.add(label);
    }

    // EFFECTS: Sets image to label with appropriate size adds it to the appropriate panel
    private void addImageToLabel(JLabel label, JPanel panel) {
        ImageIcon imageIcon = new ImageIcon(
                new ImageIcon("./data/main.jpeg").getImage().getScaledInstance(550,
                        330, Image.SCALE_DEFAULT));
        label.setIcon(imageIcon);
        panel.add(label);
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("m")) {
            processMakeWorkout();
        } else if (e.getActionCommand().equals("l")) {
            processLoadWorkout();
        } else if (e.getActionCommand().equals("i")) {
            processShowInformation();
        } else if (e.getActionCommand().equals("e")) {
            processEditWorkout();
        } else if (e.getActionCommand().equals("r")) {
            processResetWorkout();
        } else if (e.getActionCommand().equals("q")) {
            processSaveWorkout();
        } else if (e.getActionCommand().equals("d")) {
            processDoneEditing();
        } else if (e.getActionCommand().equals("c")) {
            processNextEditing();
        } else if (e.getActionCommand().equals("b")) {
            processGoBack();
        } else if (Integer.parseInt(e.getActionCommand()) < 6 && Integer.parseInt(e.getActionCommand()) >= 0) {
            processEditWorkoutByMuscle(Integer.parseInt(e.getActionCommand()));
        }
    }

    // MODIFIES: this
    // EFFECTS: removes the visibility of the previous nextEditPanel
    //          allows user to go through the process of selecting the exercises from each muscle targeted variations
    //          and creates a new myWorkout
    private void processMakeWorkout() {
        if (nextEditPanel != null) {
            nextEditPanel.setVisible(false);
        }
        createNextEditByMusclePanel(selectionMuscle);
        initNextEditMenuByMusclePanel();
    }

    // EFFECTS: creates infoPanel and initializes the panel
    private void processShowInformation() {
        createInformationPanel();
        initInfoPanel();
    }

    // MODIFIES: this
    // EFFECTS: loads workout from JSON_STORE, syncs workout with exerciseOptions, creates workoutPanel
    //          and initializes the panel
    private void processLoadWorkout() {
        loadWorkout();
        syncWorkoutWithOptions();
        createLoadedWorkoutPanel();
        initLoadedWorkoutPanel();
    }

    // MODIFIES: this
    // EFFECTS: creates editOptionsPanel and initializes the panel
    private void processEditWorkout() {
        createEditOptionsPanel();
        initEditOptionsPanel();
    }

    // MODIFIES: this
    // EFFECTS: resets myWorkout, recreates mainMenuPanel and initializes the panel
    private void processResetWorkout() {
        resetWorkout();
        try {
            exerciseOptions = new ArrayList<>();
            initExerciseOptions();
        } catch (IOException e) {
            System.out.println("Unable to read from files: ./data/exerciseOptions");
        }
        createEmptyWorkoutPanel();
        initEmptyWorkoutPanel();
    }

    // EFFECTS: makes dialog to give the user the options to save the state of the application to file
    private void processSaveWorkout() {
        createsSavingDialog();
    }

    // MODIFIES: this
    // EFFECTS: creates a editWorkoutPanel according to a specific muscle and
    //              if b == true then creates editWorkoutPanel for single muscle targeted editing
    //              else creates editWorkoutPanel for all muscle target selection
    private void processEditWorkoutByMuscle(int i) {
        createEditWorkoutByMusclePanel(i);
        initEditWorkoutByMusclePanel();
    }

    // MODIFIES: this
    // EFFECTS: syncs edited workout with exerciseOptions;
    //          and initializes the workoutPanel
    private void processDoneEditing() {
        syncChanges();
        updateWorkout();
        createLoadedWorkoutPanel();
        initLoadedWorkoutPanel();
    }

    // MODIFIES: this
    // EFFECTS: syncs edited workout with exerciseOptions; increments selectionMuscle by 1
    //          if selectionMuscle < 6 then recursive call  to processMakeWorkout()
    //          else resets selectionMuscle to 0, updates myWorkout and creates and initializes workoutPanel
    private void processNextEditing() {
        syncChanges();
        selectionMuscle++;
        if (selectionMuscle < 6) {
            processMakeWorkout();
        } else {
            selectionMuscle = 0;
            updateWorkout();
            if (!myWorkout.isEmpty()) {
                createLoadedWorkoutPanel();
                initLoadedWorkoutPanel();
            } else {
                createEmptyWorkoutPanel();
                initEmptyWorkoutPanel();
            }
        }
    }

    // EFFECTS: initializes workoutPanel
    private void processGoBack() {
        initLoadedWorkoutPanel();
    }

    // EFFECTS: Adds the main menu panel to the screen and removes visibility of other panels
    private void initEmptyWorkoutPanel() {
        add(mainMenuPanel);
        mainMenuPanel.setVisible(true);

        if (workoutPanel != null) {
            workoutPanel.setVisible(false);
        }
        if (infoPanel != null) {
            infoPanel.setVisible(false);
        }
        if (editOptionsPanel != null) {
            editOptionsPanel.setVisible(false);
        }
        if (editWorkoutPanel != null) {
            editWorkoutPanel.setVisible(false);
        }
        if (nextEditPanel != null) {
            nextEditPanel.setVisible(false);
        }
    }

    // EFFECTS: Adds the workout panel to the screen and removes visibility of other panels
    private void initLoadedWorkoutPanel() {
        add(workoutPanel);
        workoutPanel.setVisible(true);

        if (mainMenuPanel != null) {
            mainMenuPanel.setVisible(false);
        }
        if (infoPanel != null) {
            infoPanel.setVisible(false);
        }
        if (editOptionsPanel != null) {
            editOptionsPanel.setVisible(false);
        }
        if (editWorkoutPanel != null) {
            editWorkoutPanel.setVisible(false);
        }
        if (nextEditPanel != null) {
            nextEditPanel.setVisible(false);
        }
    }

    // EFFECTS: Adds the infoPanel to the screen and removes visibility of other panels
    private void initInfoPanel() {
        add(infoPanel);
        infoPanel.setVisible(true);

        if (mainMenuPanel != null) {
            mainMenuPanel.setVisible(false);
        }
        if (workoutPanel != null) {
            workoutPanel.setVisible(false);
        }
        if (editOptionsPanel != null) {
            editOptionsPanel.setVisible(false);
        }
        if (editWorkoutPanel != null) {
            editWorkoutPanel.setVisible(false);
        }
        if (nextEditPanel != null) {
            nextEditPanel.setVisible(false);
        }
    }

    // EFFECTS: Adds the editOptionsPanel to the screen and removes visibility of other panels
    private void initEditOptionsPanel() {
        add(editOptionsPanel);
        editOptionsPanel.setVisible(true);

        if (mainMenuPanel != null) {
            mainMenuPanel.setVisible(false);
        }
        if (workoutPanel != null) {
            workoutPanel.setVisible(false);
        }
        if (infoPanel != null) {
            infoPanel.setVisible(false);
        }
        if (editWorkoutPanel != null) {
            editWorkoutPanel.setVisible(false);
        }
        if (nextEditPanel != null) {
            nextEditPanel.setVisible(false);
        }
    }

    // EFFECTS: Adds the editWorkoutPanel to the screen and removes visibility of other panels
    private void initEditWorkoutByMusclePanel() {
        add(editWorkoutPanel);
        editWorkoutPanel.setVisible(true);

        if (mainMenuPanel != null) {
            mainMenuPanel.setVisible(false);
        }
        if (workoutPanel != null) {
            workoutPanel.setVisible(false);
        }
        if (infoPanel != null) {
            infoPanel.setVisible(false);
        }
        if (editOptionsPanel != null) {
            editOptionsPanel.setVisible(false);
        }
        if (nextEditPanel != null) {
            nextEditPanel.setVisible(false);
        }
    }

    // EFFECTS: Adds the nextEditPanel to the screen and removes visibility of other panels
    private void initNextEditMenuByMusclePanel() {
        add(nextEditPanel);
        nextEditPanel.setVisible(true);

        if (mainMenuPanel != null) {
            mainMenuPanel.setVisible(false);
        }
        if (workoutPanel != null) {
            workoutPanel.setVisible(false);
        }
        if (infoPanel != null) {
            infoPanel.setVisible(false);
        }
        if (editOptionsPanel != null) {
            editOptionsPanel.setVisible(false);
        }
        if (editWorkoutPanel != null) {
            editWorkoutPanel.setVisible(false);
        }
    }

    // MODIFIES: this
    // EFFECTS: creates a JTextArea advisory for workoutPanel
    private void createLoadedWorkoutAdvisory() {
        JTextArea adv = new JTextArea();
        adv.setColumns(38);
        adv.setLineWrap(true);
        adv.setRows(10);
        adv.setWrapStyleWord(true);
        adv.setEditable(false);
        adv.setAlignmentY(JTextArea.CENTER_ALIGNMENT);
        adv.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED), BorderFactory.createLoweredBevelBorder()));


        FileReader reader = null;
        try {
            reader = new FileReader("./data/advisory/workoutPanelAdv.txt");
            adv.read(reader, "./data/advisory/workoutPanelAdv.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        adv.setFont(new Font("Georgia", Font.PLAIN, 15));
        adv.setBackground(new Color(128, 186, 215, 255));
        adv.setOpaque(true);
        JScrollPane scrollPane = new JScrollPane(adv);
        workoutPanel.add(scrollPane);
    }

    // EFFECTS: calls exerciseAdvisory for different muscle targeted variation
    private void createEditAdvisory(int i, JPanel panel) {
        if (i == 0) {
            exerciseAdvisory("./data/advisory/chestAdv.txt", panel);
        } else if (i == 1) {
            exerciseAdvisory("./data/advisory/backAdv.txt", panel);
        } else if (i == 2) {
            exerciseAdvisory("./data/advisory/coreAdv.txt", panel);
        } else if (i == 3) {
            exerciseAdvisory("./data/advisory/armAdv.txt", panel);
        } else if (i == 4) {
            exerciseAdvisory("./data/advisory/shoulderAdv.txt", panel);
        } else {
            exerciseAdvisory("./data/advisory/legAdv.txt", panel);
        }
    }

    private void exerciseAdvisory(String s, JPanel panel) {
        JTextArea adv = new JTextArea();
        adv.setColumns(38);
        adv.setLineWrap(true);
        adv.setRows(6);
        adv.setWrapStyleWord(true);
        adv.setEditable(false);
        adv.setAlignmentY(JTextArea.CENTER_ALIGNMENT);
        adv.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.RAISED), BorderFactory.createLoweredBevelBorder()));

        FileReader reader = null;
        try {
            reader = new FileReader(s);
            adv.read(reader, s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        adv.setFont(new Font("Georgia", Font.BOLD, 15));
        adv.setBackground(new Color(128, 186, 215, 255));
        adv.setOpaque(true);
        panel.add(adv);
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
    // EFFECTS: loads myWorkout from file
    private void loadWorkout() {
        try {
            myWorkout = jsonReader.read(true);
            System.out.println("Loaded Workout from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: creates a save myWorkout dialog to give the user option to save myWorkout to the file
    private void createsSavingDialog() {
        final JOptionPane optionPane = new JOptionPane(
                "Save Workout Before Exiting Application?\n (Note: Can proceed only by choosing Yes/No)",
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION);

        final JDialog dialog = new JDialog(this, "Save Workout", true);
        dialog.setContentPane(optionPane);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        optionPane.addPropertyChangeListener(e -> {
            String prop = e.getPropertyName();

            if (dialog.isVisible() && (e.getSource() == optionPane) && (JOptionPane.VALUE_PROPERTY.equals(prop))) {
                dialog.setVisible(false);
            }
        });
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        if ((Integer) optionPane.getValue() == JOptionPane.YES_OPTION) {
            saveWorkout();
        }
        printLog(EventLog.getInstance());
        System.exit(0);
    }

    // EFFECTS: prints out the EventLog with each event having a date and description on the console
    private void printLog(EventLog log) {
        for (Event event : log) {
            System.out.println(event.toString() + "\n");
        }
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

    // EFFECTS: gets target muscle targeted for syncing changes with exerciseOptions
    private void syncChanges() {
        String muscles = (String) exerciseData[0][2];
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
            syncEditedExerciseWithExerciseOptions(target);
        }
    }

    // MODIFIES: this
    // EFFECTS: syncs changes in exerciseData with exerciseOptions
    private void syncEditedExerciseWithExerciseOptions(int target) {
        int i = 0;
        for (Exercise exercise : exerciseOptions.get(target).getExercises()) {
            if (exercise.getName().equals(exerciseData[i][1])) {
                exercise.setSelected((Boolean) exerciseData[i][0]);
                i++;
            }
        }
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

    // MODIFIES: this
    // EFFECTS: updates the myWorkout
    private void updateWorkout() {
        myWorkout.clear(true);
        for (Workout muscleEx : exerciseOptions) {
            List<Exercise> muscleExercises = muscleEx.getExercises();
            myWorkout.addSelectedExercise(muscleExercises);
        }
    }

    // creates a custom table for my exercise options according to exercise day or muscle variations
    // using TableSortDemo.java as template from java Ui swing tutorials
    // (https://docs.oracle.com/javase/tutorial/uiswing/examples/components/TableSortDemoProject)
    class ExerciseTableModel extends AbstractTableModel {

        public int getColumnCount() {
            return columnsExercise.length;
        }

        public int getRowCount() {
            return exerciseData.length;
        }

        public String getColumnName(int col) {
            return columnsExercise[col];
        }

        public Object getValueAt(int row, int col) {
            return exerciseData[row][col];
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a checkbox.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        // EFFECTS: checks whether the cell selected is editable or not
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            return col == 0;
        }

        // MODIFIES: this
        // EFFECTS: changes the value of edited cell and prints the change in exerciseData
        public void setValueAt(Object value, int row, int col) {
            exerciseData[row][col] = value;
        }
    }

    // EFFECTS: runs GUI for the Vi Disciplina Application
    public static void main(String[] args) {
        new GUI();
    }
}