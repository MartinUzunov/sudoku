# Sudoku
The classic Sudoku game implemented in Java with the help of the JavaFX library. The application implements the Remote Method Invocation model. The user can use both the mouse and the keyboard for input, choose a difficulty level, take notes, undo and redo moves and request a solution to the puzzle from the server.
<br></br>
![Sudoku](https://github.com/MartinUzunov/Sudoku/blob/master/sudoku.gif)

## How to start the game
There are 2 options to run the game.

- Using your IDE (IntelliJ IDEA / Eclipse / Netbeans)
- Using Maven - passing main class to run on the command line
<br></br>

### Run the Examples in IntelliJ Idea(needs pre-downloaded javafx modules)

To run the game from within IntelliJ IDEA you must first create a new project in IntelliJ and set the root
directory to the directory into which you have cloned this Git repository. 
<br></br>
Second, you must download JavaFX and unzip the distribution to some directory.
<br></br>
Third, you must add all the JAR files found in the "lib" directory to your project's classpath.
<br></br>
Fourth, start the server by running the ServerMain class.

You start the client by executing the ClientMain class.
- A client cannot be started if the server is shut down.
- It is possible to start multiple instances of the client on a single machine.
<br></br>

## Quick User Guide
When the client is started, a Prompt appears prompting the user to enter a Username. Once it is entered and this is confirmed(by clicking OK), the main(and only) window opens. The window contains 3 sections:
- Header(top of the window) - On the left, you can see the current difficulty of the game. The possible difficulties are Easy, Normal, Hard, and Expert. The default difficulty is Normal.
On the right, you can see the elapsed time since the start of the game.
- Sudoku board (on the left of the window) - movement on the board is done by clicking the mouse on or using the arrows on the Numpad. Entering a number is done with the numbers from 1 to 9 (Numpad numbers also work). Deleting a digit is done with DELETE or BACKSPACE.
Numbers in black cannot be changed, as they are part of the required numbers needed to have a board solution.
- Control Panel (to the right of the window) - At the top of the control panel can be seen a button to start a new game. When the button is pressed, a menu comes up from which the difficulty of the new game can be selected. Below the new game button there are 5 buttons:
    + Notes button - when the button is pressed, the notes mode is switched. At the top right of the button, there is a colored circle that indicates the current mode(green - notes are on, red - notes are off).
    + Undo button - when the button is pressed, the board returns to the previous position (1 move back).
    + Redo button - when the button is pressed, the undo action is applied again.
    + Erase button - when the button is pressed, the number in the currently selected cell is erased.
    + Solve button - when the button is pressed, a request is sent to the server to solve the current board and once the server responds to the request, all cells are filled with the correct numbers and the game ends.

Below the 5 buttons, we have the Numpad which allows entering numbers using the mouse.
If all the cells on the board are filled in correctly, a window appears indicating that the game has been completed successfully.<br></br>
## Description of the program code
The program contains a separate Jar file (and project) that contains Graphical user interface elements(JavaFX). The functionality of the GUI is explained in detail in the user manual.
### The algorithm used to solve sudoku:
1) Go around the board cell by cell.
2) In each cell that is not empty, we check if we can place any
number between 1 and 9.
3) If the number can be placed, it is placed and the function is
is called recursively with the new board.
4) The function terminates when the board contains no empty cells or when there is a
cell in which no number can be inserted.

### The algorithm used to generate sudoku:

1) Generate a filled board.
We need to make sure that these numbers are placed on the board following the rules of Sudoku. For this purpose, we use a backtracking algorithm to solve a sudoku, which we apply to an empty grid. We add a random element to this solution algorithm to make sure that a new grid is generated each time we run it.
2) One by one remove N cells(depending on the difficulty).
Each time a value is removed, a sudoku solving algorithm is applied to see if the grid can still be solved and count the number of solutions it leads to.
3) If the resulting grid has only one solution(unique), we can proceed with the removal. Otherwise, we will have to put back into the grid the value we took away and choose a new cell to remove from.
4) At the end of the algorithm we get a sudoku board with an existing unique solution.
