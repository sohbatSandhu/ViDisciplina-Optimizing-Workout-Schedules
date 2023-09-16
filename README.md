# _Vi Disciplina_

## Description

A system that allows a user to choose the _optimal_ exercises with descriptions to _maximise_ the
efficiency of strength training. _Vi Disciplina_ shows a list of the most effective resistance
training exercises with **exercise name**, **muscles targeted**, **correct technique**, and **equipment required**.
The application allows a user to make a _personalised_ **weekly workout schedule** according to the muscles targeted
from
the chosen exercises.

## Target audience

_Vi Disciplina_ java application is useful for anyone who would like to enhance the quality of their lives,
improve bone and body strength, improve mental health, improve individual health, or simply, enhance
body aesthetics. This application may interest individuals who do not know what or how should
they exercise, to maximise the benefits of their training.

## Why does this project interest me?

Since childhood, I have always been an individual who prefers to spend my time outdoors but from the end of
high school,
I have been focusing more on my academics and reducing the time for sports or any outdoor activities resulting
in increased lethargy, fat gain, introversion, procrastination, unhealthy body and mind, and general moodiness. I have
recently started
exercising and my lifestyle has significantly improved. My efficiency in academic work has increased, my energy levels,
improved my mood, and instilled a sense of discipline to decrease my earlier procrastination.

Furthermore, studies have shown that strength training provides a range of mental and physical health benefits.
Some of the most noteworthy benefits are:

- _Increased_ **Muscle Size and Strength**
- _Improves_ **Cardiovascular Health**
- _Increases_ **Bone Density**
- _Stabilized_ and _Protects_ **Joints**
- _Reduces_ **Body Fat**
- _Improved_ **Mental Well-being**
- _Improved_ **Sleep Quality**
- _Enhance_ **Brain Health**
- _Enhance_ **Body Aesthetics**

The predominant cause for reducing my quality of life after high school was the time pressure due to my studies,
therefore I wanted to create an application that allows anyone to choose exercises, creating a personalised workout
schedule to get the maximum benefits from the best exercises, in just a few minutes.

## User Stories

- As a user, I want to edit my workout.
- As a user, I want to select or unselect an exercise to my workout.
- As a user, I want to reset my workout at once.
- As a user, I want to be given the option to choose the exercises to edit my workout by day/muscle variations.
- As a user, I only want to edit a single Tuesday/back exercise to my workout using the edit option.
- As a user, I only want to edit a Thursday/arm exercise existing in my workout using the edit option.
- As a user, I want to check the information about an exercise from my workout using the show information options.
- As a user, when I select the quit option from the workout option menu, I want to be reminded to save my workout to
  file and have the option to do so or not.
- As a user, when I start the application, I want to be given the option to load my workout from file.
- As a user, I want to be able to save my workout to file (if I so choose)
- As a user, I want to be able to be able to load my workout from file (if I so choose)

## Instructions for Grader

- _Run GUI.main()_
- You can generate the action of adding/removing multiple Exercises (Xs) to the Workout (Y) by clicking the
  "Make New Workout" Button. The action makes you go through different panels to select exercises by exercise day or
  muscle variation. In each panel, it shows you a list of exercise from which you can select/unselect the exercises for
  the workout. When you click the "Done: Next Editing" button it completes the changes to the workout.
- You can generate the action above when you have a non-empty workout by clicking "Edit Workout" button, which then
  directs you to a panel with allows you to edit workout by exercise day or muscle variation. Choosing one of the
  buttons
  allows you to go to a panel, where you can again go through the process od selecting/unselecting the exercises.
- After having a non-empty workout you can generate the **first required action** related to adding Xs to a Y by
  clicking
  the "Show Information" button after selecting an exercise from the table. It shows you the information about the
  exercise
  in a new panel.
- After having a non-empty workout you can generate the **second required action** related to adding Xs to a Y by
  clicking
  the "Reset Workout" button. It removes all the exercises from the workout at once.
- You can locate my visual component(s):
    - After running the application, you can see an image when there is no exercises in the workout.
    - When you generate the _first required action_ you can see another image that gives the visual depiction of
      the exercise.
- You can save the state of my application by clicking the "Quit Application" button which generates a dialog that gives
  the user the option to save the state of the application.
- You can reload the state of my application by clicking the "Load Workout" button which shows you the
  workout loaded from the file.
- **You can only quit the application by clicking the "Quit Application" button.**

## Phase 4: Task 2

### Sample representative of Log Events

Mon Aug 07 18:24:22 PDT 2023
_Event log cleared._

Mon Aug 07 18:24:29 PDT 2023
_Exercise: Flat Chest Press Machine is selected for the Workout._

Mon Aug 07 18:24:33 PDT 2023
_Exercise: Romanian DeadLift is selected for the Workout._

Mon Aug 07 18:24:37 PDT 2023
_Exercise: Incline Dumbbell Curls is selected for the Workout._

Mon Aug 07 18:24:37 PDT 2023
_Exercise: Flat Close Grip Bench Press is selected for the Workout._

Mon Aug 07 18:24:39 PDT 2023
_The Workout is reset to accommodate recent changes to the Workout_

Mon Aug 07 18:24:39 PDT 2023
_The selected exercises in Monday variation of exercises have been updated to the Workout._

Mon Aug 07 18:24:39 PDT 2023
_The selected exercises in Tuesday variation of exercises have been updated to the Workout._

Mon Aug 07 18:24:39 PDT 2023
_The selected exercises in Wednesday variation of exercises have been updated to the Workout._

Mon Aug 07 18:24:39 PDT 2023
_The selected exercises in Thursday variation of exercises have been updated to the Workout._

Mon Aug 07 18:24:39 PDT 2023
_The selected exercises in Friday variation of exercises have been updated to the Workout._

Mon Aug 07 18:24:39 PDT 2023
_The selected exercises in Saturday variation of exercises have been updated to the Workout._

Mon Aug 07 18:24:58 PDT 2023
_Exercise: T-Bar Rows is selected for the Workout._

Mon Aug 07 18:24:58 PDT 2023
_The Workout is reset to accommodate recent changes to the Workout_

Mon Aug 07 18:24:58 PDT 2023
_The selected exercises in Monday variation of exercises have been updated to the Workout._

Mon Aug 07 18:24:58 PDT 2023
_The selected exercises in Tuesday variation of exercises have been updated to the Workout._

Mon Aug 07 18:24:58 PDT 2023
_The selected exercises in Wednesday variation of exercises have been updated to the Workout._

Mon Aug 07 18:24:58 PDT 2023
_The selected exercises in Thursday variation of exercises have been updated to the Workout._

Mon Aug 07 18:24:58 PDT 2023
_The selected exercises in Friday variation of exercises have been updated to the Workout._

Mon Aug 07 18:24:58 PDT 2023
_The selected exercises in Saturday variation of exercises have been updated to the Workout._

Mon Aug 07 18:25:07 PDT 2023
_The Workout is reset_

Mon Aug 07 18:25:09 PDT 2023
_Loaded Workout from ./data/myWorkout.json_

Mon Aug 07 18:25:12 PDT 2023
_Show Information for Exercise: Abdominal Twists_

Mon Aug 07 18:25:14 PDT 2023
_Show Information for Exercise: Flat Close Grip Bench Press_

Mon Aug 07 18:25:23 PDT 2023
_Saved Workout to ./data/myWorkout.json_

## Phase 4: Task 3

If I had more time on my project, I would have liked to use refactoring to extract an abstract superclass to increase
the cohesion between my console-based User Interface and my Graphical User Interface. Due to the similarity of the 
methods used in both the classes of the UI package, creating an abstract superclass would have helped to reduce the 
time spent on coding the Graphical User Interface and also reduce the complexity of the GUI class of my project for 
easier understanding.

I would have like to create classes to split the responsibilities of the GUI class as it has many JPanel and JButton 
fields and methods. It also includes methods that uses JTable and JList classes for the panels. It feels like I have 
unnecessarily cluttered my GUI class while decreasing the efficiency of the code.  