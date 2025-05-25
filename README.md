# java-minesweeper
This is my first attempt at creating the minesweeper game using java and also some OOP principles

I have applied OOP principles such as encapsaulation, inhertiance etc.

the following next headers will explain what I did.

# My Classes

## Cell.java

This class is going to be responisble in representing a single cell on the Minesweeper Board:

- Each cell can be a mine 
- Each cell can be empty
- Each cell can be flagged by the player

I have added private attributes that are needed for the core logic such as

```java
private int neighborMines;  // Stores amount of adjacent mines
private boolean isRevealed; // Stores boolean either revealed or not
private boolean isFlagged;  // Stores boolean either flagged or not
private boolean isMine;     // stores boolean if it is a mine or not
```
a constructor is added naturally and then I added getters and setters for it which are 

### Getters

```java
public void setMine() { this.isMine = true; }
public void toggleFlag() { this.isFlagged = !this.isFlagged; }
public void setNeighborMines(int count) { this.neighborMines = count; }
public void reveal() { this.isRevealed = true; }
```
### Setters
```java
public boolean isMine() { return isMine; }
public boolean isFlagged() { return isFlagged; }
public boolean isRevealed() { return isRevealed; }
public int getNeighborMines() { return neighborMines; }
```

## Difficulty.java

This is an enum which is basically is a dictionarry that has a list of constant values that can be choosed and using this enum I created 3 difficulties that the user can choose from so forthe following difficulties:

```java
    // Easy difficulty: 8x8 grid with 10 mines
    EASY(8, 8, 10),
    // Medium difficulty: 16x16 grid with 40 mines
    MEDIUM(16, 16, 40),
    // Hard difficulty: 24x24 grid with 99 mines
    HARD(24, 24, 99);
```

and then I added a Constructor to create a difficulty level with specified board dimensions and mine count.

### Getters
```java
public int getRows() { return rows; }
public int getCols() { return cols; }
public int getMines() { return mines; }
```
### Setters
`typically, enums don’t provide setters because their fields are meant to be immutable once the enum constants are created.`


## Board.java

This class is representing the actual board for the game it is responsible for 3

- creating a grid with empty cells
- storing the amount of adjacent mines in each cell
- placing mines in cells randomly 
- functions to check if something is in bounds
- checks safe rows which are first clicked cell so that you cant lose first cell

### Getters

```java
public int getRows() {return rows;}
public int getCols() {return cols;}
```

## GameController.java

This Class manages the state and logic of the Game, it handels player moves, game rules, and win and loos conditions

main parts are

### Constructor
when initializing the game controller we load the chosen difficutly by the player from the game class main and load it in the constructor

```java
  19   │     public GameController(Difficulty difficulty) {
  20   │         this.board = new Board(difficulty.getRows(), difficulty.getCol());
  21   │         this.gameOver = false;
  22   │         this.gameWon = false;
  23   │         this.totalMines = difficulty.getMines();
  24   │     }
```

it has the following attributes

```java
   6   │     private final Board board;
   7   │     private boolean gameOver;
   8   │     private boolean gameWon;
   9   │     private boolean firstMoveMade = false;
  10   │     private final int totalMines;
```

and methods like 

```java
revealCell(int row, int col);
toggleFlag(int row, int col);
revealAdjacentCells(int row, int col);
checkWinCondition();
revealAllMines();
```
### Getters
```java
public Board getBoard() {return board;}

public boolean isGameWon() {return gameWon;}

public boolean isGameOver() {return gameOver;}
```

## MinesweeperGUI.java

This is where it all comes together

Here I made 

- Logic for making the first cell always safe
- asking the player what difficulty they want
- showing the results
- restarting the game

as well as 

- loading the icons
- choosing a color scheme
- loading the last difficulty when restarting 


# I am still a beginner but I am getting the hang of it slowly

for any changes or simplifications. please do not heistate to make a fork and let me know about it

