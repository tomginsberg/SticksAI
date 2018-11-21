package game;

import exceptions.GameFinishedException;
import exceptions.IllegalMoveException;
import players.SticksPlayer;

import java.util.concurrent.ThreadLocalRandom;

public class AutoGame extends Game {

    private int state;

    public AutoGame(SticksPlayer opponent, int numSticks, boolean statusUpdates) {
        super(opponent, numSticks, statusUpdates);
        this.state = ThreadLocalRandom.current().nextInt(0, 2);
    }
    public boolean play(SticksPlayer player){
        while (this.inProgress()){
            try {
                switch (state) {
                    case 0:
                        this.playMove(player.getMove(this.getNumSticksLeft()));
                        state = 1;
                        break;
                    case 1:
                        this.playOpponentMove();
                        state = 0;
                        break;

                }
            }catch (IllegalMoveException | GameFinishedException e) {
                e.printStackTrace();
            }

        }
        player.endGame();
        this.endGame();
        return this.isWon();
    }
}
