public class Game {
    private int gameSate, numSticks, sticksRemaining, opponentMove;
    private SticksPlayer opponent;

    public Game(SticksPlayer opponent, int numSticks){
        gameSate = 0;
        this.numSticks = numSticks;
        this.sticksRemaining = numSticks;
        this.opponent = opponent;
    }
    public void playMove(int draw) throws IllegalMoveException, GameFinishedException {
        if(inProgress()) {
            if (sticksRemaining < draw) {
                System.out.println("Illegal Player Move");
                throw new IllegalMoveException();
            }
            sticksRemaining -= draw;
            if (sticksRemaining == 0) loseGame();
            opponentMove = opponent.getMove(sticksRemaining);
            if (opponentMove < sticksRemaining) {
                System.out.println("Illegal Opponent Move");
                throw new IllegalMoveException();
            }
            sticksRemaining -= opponentMove;
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
        return gameSate == 2;
    }

    public boolean isLost(){
        return gameSate == 1;
    }

    public boolean inProgress() {
        return gameSate == 0;
    }
}
