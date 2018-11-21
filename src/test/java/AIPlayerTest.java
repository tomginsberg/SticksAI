import game.AutoGame;
import org.junit.Test;
import players.RandomPlayer;
import players.AIPlayer;
import org.junit.Assert;

import java.util.stream.IntStream;

public class AIPlayerTest {

    @Test
    public void aiTest(){
        AIPlayer AIPlayer = new AIPlayer(50);
        AIPlayer.train(100, new RandomPlayer());
        AIPlayer betterAI = new AIPlayer(50);
        betterAI.train(5, AIPlayer);
        AutoGame autoGame = new AutoGame(AIPlayer, 50, false);

        int wins;
        int rounds = 100;

        wins = (int) IntStream.range(0, rounds).filter(i -> autoGame.play(betterAI)).count();
        Assert.assertTrue(wins>rounds * 0.9);
    }
}