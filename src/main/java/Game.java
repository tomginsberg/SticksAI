public class Game {
    private int gameSate, numSticks;
    private SticksPlayer opponent;

    public Game(SticksPlayer opponent, int numSticks){
        gameSate = 0;
        this.opponent = opponent;
    }
    public void playMove(int draw) throws IllegalMoveException {
        if(numSticks < draw){
            throw new IllegalMoveException();
        }
        numSticks -= draw;
    }
    public void loseGame(){
        gameSate = 1;
    }

    public void winGame(){
        gameSate = 2;
    }

    public boolean isWon(){
        return gameSate == 2;
    }

    public boolean isLost(){
        return gameSate == 1;
    }

    public boolean inProgress() {return gameSate == 0;}
}
