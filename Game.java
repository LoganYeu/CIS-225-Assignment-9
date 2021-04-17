/**
 * Gives methods for playing and storing manipulating information on the dice game pig.
 *
 * @author (Logan Yeubanks)
 * @version (Version 1)
 */

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

public class Game {
    //List for storing the players involved with the game.
    ArrayList<Player> playerList;


    public Game() {
        playerList = new ArrayList<>();

    }

    /**
     * This is the logic of the game of pig. It plays the game and is the core of the program.
     */
    public void playPigGame() {
        Scanner userInput = new Scanner(System.in);

        /*Prints the welcome screen and then asks the user if they want to load in a save game.
          It will then load the save game in if asked to or start a new game by proceeding.*/
        printWelcomeScreen();
        System.out.println("Type yes to load a previous save game, otherwise press any character to ignore ");
        String menuAnswer = userInput.next();
        if(menuAnswer.equalsIgnoreCase("yes")) {
            loadGame(userInput);
        }
        else {
            System.out.println("Okay, starting a new game...");
            int playerCount = askHowManyPlayers(userInput);
            askPlayerNames(playerCount, userInput);
        }

        boolean running = true;

        //Main while loop that occurs as long as there is not a winner to the game.
        while(running) {
            final int ROLL_LEADS_TO_BUST = 1;

            System.out.println("Type yes to save the game, otherwise press any character to ignore ");
            String answer = userInput.next();

            //Saves the game if yes is entered.
            if(answer.equalsIgnoreCase("yes")) {
                saveGame(userInput);
            }

            //Iterates through each player. Essentially is their turn.
            for (Player currentPlayer : playerList) {
                System.out.println(currentPlayer.getName() + ", your current score is: " + currentPlayer.getScore());
                int turnScore = 0;

                boolean validAnswer = false;

                //Loop runs unless a 1 is rolled or a hold is indicated.
                while (!validAnswer) {

                    System.out.println("Would you like to hold or roll? ");

                    String playerDecision = userInput.next();
                    if (playerDecision.equalsIgnoreCase("roll")) {
                        //Rolls a dice using the rollDice method.
                        int roll = rollDice();
                        System.out.println("You rolled a " + roll);

                        if(roll == ROLL_LEADS_TO_BUST) {
                            //Makes turn score 0 if a 1 is rolled, indicating a bust.
                            turnScore = 0;
                            validAnswer = true;
                        }
                        else {
                            //Adds the roll to the score for the turn.
                            turnScore += roll;
                        }
                    System.out.println("The score for the current turn is " + turnScore);
                    }

                    else if (playerDecision.equalsIgnoreCase("hold")) {
                        //Breaks loop if the user enters hold.
                        validAnswer = true;
                    }

                    else {
                        //Error message if roll or hold is not entered.
                        System.out.println("The only valid game moves are roll and hold, please try again.");
                    }
                }
                currentPlayer.addToScore(turnScore);
                System.out.println(currentPlayer.getName() + ", your total score is " + currentPlayer.getScore());

                //Prints that someone won the game and sets running to false if a user reaches 100.
                final int WINNING_SCORE = 100;
                if (currentPlayer.getScore() >= WINNING_SCORE) {
                   System.out.println("You win!");
                   running = false;
                   break;
                }
            }
        }


    }

    /**
     * Prints the welcome screen which includes a welcome message and a link to the rules of pig.
     */
    public void printWelcomeScreen() {
        System.out.println("Welcome to the dice game pig!");
        System.out.println("Click here for the rules of how to play: https://en.wikipedia.org/wiki/Pig_%28dice_game%29");
    }

    /**
     * Asks and returns the number of players the user would like to play with in the game.
     *
     * @param userInput
     * @return playerResponse
     */
    public int askHowManyPlayers(Scanner userInput) {
        boolean validNumPlayers = false;
        int minNumberOfPlayers = 2;

        //Asks how many players are going to be in the game and then validates the response.
        while(!validNumPlayers) {
            try {
                System.out.println("How many players will there be? ");
                int playerResponse = userInput.nextInt();
                if(playerResponse < minNumberOfPlayers) {
                    System.out.println("There must be at least 2 players to play!");
                }
                else {
                    return playerResponse;
                }
            }
            catch (InputMismatchException e) {
                System.out.println("You must enter a integer.");
                userInput.next();
            }

        }
        return 0;
    }

    /**
     * Asks for the name of the players in the game and then adds them to a list of players.
     *
     * @param playerCount
     * @param userInput
     */
    public void askPlayerNames(int playerCount, Scanner userInput) {
        for (int i = 1; i <= playerCount; i++) {
            System.out.println("Enter a name for player " + i);
            String playerName = userInput.next();
            Player newPlayer = new Player(playerName);
            playerList.add(newPlayer);
        }
    }

    /**
     * Rolls a 6 sided dice and returns the result.
     *
     * @return num
     */
    private int rollDice() {
        Random rand = new Random();
        int max = 6;

        int num = rand.nextInt(max) + 1;
        return num;
    }

    /**
     * Creates a .txt file with the name the user specifies.
     *
     * @param userInput
     * @return fileName
     */
    private String createSaveFile(Scanner userInput) {
        System.out.println("What would you like to name the save for this game? ");
        String fileName = userInput.next();
        //Creates the file unless the file already exists.
        try {
            File myObj = new File(fileName + ".txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already existed, but was overridden.");
            }
            return fileName;
        }
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return "";
    }

    /**
     *Writes teh current data of the game to a string and then saves it to the specified file.
     *
     * @param userInput
     */
    private void saveGame(Scanner userInput) {
        String saveString = "";
        for (Player currentPlayer : playerList) {
            saveString += currentPlayer.getName() + " " + currentPlayer.getScore() + "\n";
        }
        String fileName = createSaveFile(userInput);
        try {
            FileWriter myWriter = new FileWriter(fileName + ".txt");
            myWriter.write(saveString);
            myWriter.close();
            System.out.println("Successfully saved the game!.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Loads a .txt file specified by the user.
     *
     * @param userInput
     */
    private void loadGame(Scanner userInput) {
        System.out.println("What is the name of the game you would like to load. ");
        String answer = userInput.next();
        try {
            File myObj = new File(answer + ".txt");
            Scanner myReader = new Scanner(myObj);
            playerList.clear();
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] playerData = data.split("\\s+");
                Player player = new Player(playerData[0]);
                player.setScore(Integer.parseInt(playerData[1]));
                playerList.add(player);
            }
            myReader.close();
            System.out.println("Game successfully loaded!");
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
