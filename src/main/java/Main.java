import game.AutoGame;
import game.Game;
import players.AIPlayer;
import players.RandomPlayer;
import players.RealPlayer;
import players.SticksPlayer;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        int sticks = 20;
        SticksPlayer ai = new AIPlayer(sticks);
        ((AIPlayer) ai).train(10000, new RandomPlayer());
        SticksPlayer human = new RealPlayer();
        AutoGame game = new AutoGame(ai, sticks, true);
        game.play(human);
    }
}
