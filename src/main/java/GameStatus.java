public class GameStatus {
    private int gameSate;
    public GameStatus(){
        gameSate = 0;
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
}
