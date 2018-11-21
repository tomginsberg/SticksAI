package players;

import exceptions.IllegalMoveException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class RealPlayer implements SticksPlayer {
    private Scanner sc;
    private int move;
    public RealPlayer(){
        sc = new Scanner(System.in);
    }
    @Override
    public int getMove(int sticksRemaining) {
        while (true) {
            System.out.printf("\nThere are %d stick's remaining, how many would you like to draw? (1-3): \n", sticksRemaining);
            try {
                move = Integer.parseInt(sc.nextLine());
                if (move > sticksRemaining || move < 1 || move > 3) {
                    throw new IllegalMoveException();
                }
                break;
            } catch (IllegalMoveException e) {
                System.out.println("Please enter a legal move\n");
                sc.reset();
            } catch (Exception e) {
                System.out.println("Please enter an integer\n");
                sc.reset();
            }
        }
        return move;
    }

    @Override
    public void endGame() {
        sc.close();
    }
}
