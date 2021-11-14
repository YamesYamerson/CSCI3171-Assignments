import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class NumberGame {
    BufferedReader input;
    PrintWriter output;
    String inputLine;

    public void run() throws IOException {
        boolean isPlaying;
        int randomNumber = -1;
        final int maxNum = 100;
        System.out.println("[CLIENT HAS INITIATED GUESS THE NUMBER GAME!]");
        output.println("Would you like to play the number game (Y/N)?");
        inputLine = input.readLine();
        if(inputLine.equalsIgnoreCase("Y")){
            System.out.println("[CLIENT REQUESTS TO BEGIN A GAME!]");
            isPlaying = true;
            System.out.println("[SERVER IS PICKING A RANDOM NUMBER!]");
            output.println("Enter A Number Between 0-100 (/endgame to quit)");
            randomNumber = (int) Math.floor(Math.random() * maxNum + 1);
            System.out.println("[RANDOM NUMBER IS: " + randomNumber + "]");
        }else if(inputLine.equalsIgnoreCase("N")){
            System.out.println("[GUESS THE NUMBER GAME ABORTING]");
            output.println("Quitting guess the number game");
            isPlaying = false;
        }else{
            System.out.println("[CLIENT PROVIDED FAULTY INPUT, GAME ABORTING!]");
            output.println("Input not recognized, game aborting!");
            isPlaying = false;
        }

        while(isPlaying) {
            System.out.println("[REQUESTING NUMBER FROM CLIENT!]");
            inputLine = input.readLine();
            if (inputLine.equalsIgnoreCase("/endgame")) {
                isPlaying = false;
            }
            if (isNumeric(inputLine)) {
                int currentGuess = Integer.parseInt(inputLine);
                if (randomNumber == currentGuess) {
                    System.out.println("[CLIENT HAS GUESSED THE CORRECT NUMBER, GAME TERMINATING!]");
                    output.println("You have guessed correctly! Game ending!");
                    isPlaying = false;
                } else if (currentGuess > randomNumber) {
                    System.out.println("[CLIENT GUESSED TOO HIGH!]");
                    output.println("Too high! Guess again! (/endgame to quit)");
                } else if (currentGuess < randomNumber) {
                    System.out.println("[CLIENT GUESSED TOO LOW!]");
                    output.println("Too low! Guess again! (/endgame to quit)");
                }
            }else if(inputLine.equalsIgnoreCase("/endgame")){
                System.out.println("[CLIENT HAS REQUESTED TO END GAME - TERMINATING...]");
                output.println("Quitting game, better luck next time!");
                break;

            }else{
                System.out.println("[CLIENT HAS PROVIDED INCORRECT INPUT]");
                output.println("You have not entered a valid number, try again! (/endgame to quit)");
            }
        }
}