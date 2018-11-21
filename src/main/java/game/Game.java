package game;

import exceptions.GameFinishedException;
import exceptions.IllegalMoveException;
import players.SticksPlayer;

public class Game {
    private int gameSate;
    private int numSticks;
    private int sticksRemaining;
    private SticksPlayer opponent;
    private boolean statusUpdates;

    /**
     * @param opponent
     * @param numSticks
     */
    public Game(SticksPlayer opponent, int numSticks, boolean statusUpdates){
        gameSate = 0;
        this.numSticks = numSticks;
        this.sticksRemaining = numSticks;
        this.opponent = opponent;
        this.statusUpdates = statusUpdates;
    }

    public void endGame(){
        this.opponent.endGame();
    }

    /**
     * @param draw
     * @throws IllegalMoveException
     * @throws GameFinishedException
     */
    public void playMove(int draw) throws IllegalMoveException, GameFinishedException {
        if(inProgress()) {
            if (sticksRemaining < draw || draw > 3 || draw < 1) {
                System.out.println("Illegal Player Move");
                throw new IllegalMoveException();
            }
            sticksRemaining -= draw;
            if(statusUpdates){
                System.out.printf("Player drew %d sticks\n",draw);
            }
            if (sticksRemaining == 0) loseGame();

        }else {
            throw new GameFinishedException();
        }
    }

    /**
     * @throws IllegalMoveException
     * @throws GameFinishedException
     */
    public void playOpponentMove() throws IllegalMoveException, GameFinishedException {
        if(inProgress()) {
            int opponentMove = opponent.getMove(sticksRemaining);
            if (opponentMove > sticksRemaining || opponentMove > 3 || opponentMove < 1) {
                System.out.println("Illegal Opponent Move");
                throw new IllegalMoveException();
            }
            sticksRemaining -= opponentMove;
            if(statusUpdates){
                System.out.printf("Opponent drew %d sticks\n", opponentMove);
            }
            if (sticksRemaining == 0) winGame();

        }else {
            throw new GameFinishedException();
        }
    }

     private void loseGame(){
        gameSate = 1;
    }

    public int getNumSticksLeft(){
        return sticksRemaining;
    }

    private void winGame(){
        gameSate = 2;
    }

    public boolean isWon(){
        boolean finalStatus = (gameSate == 2);
        if(statusUpdates){
            if (finalStatus){
                System.out.println("\nThe player has won the game!");
            }
            else {
                System.out.println("\nThe opponent has won the game!");
            }
        }
        return finalStatus;
    }

    public boolean isLost(){
        return gameSate == 1;
    }

    public boolean inProgress() {
        return gameSate == 0;
    }
    public void newGame(){
        this.gameSate = 0;
        this.sticksRemaining = this.numSticks;
    }
}
