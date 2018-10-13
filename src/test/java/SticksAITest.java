import game.AutoGame;
import org.junit.Test;
import players.RandomPlayer;
import players.SticksAI;
import org.junit.Assert;

import java.util.stream.IntStream;

public class SticksAITest {

    @Test
    public void aiTest(){
        SticksAI sticksAI = new SticksAI(50);
        sticksAI.train(100, new RandomPlayer());
        SticksAI betterAI = new SticksAI(50);
        betterAI.train(5, sticksAI);
        AutoGame autoGame = new AutoGame(sticksAI, 50);

        int wins;
        int rounds = 100;

        wins = (int) IntStream.range(0, rounds).filter(i -> autoGame.play(betterAI)).count();
        Assert.assertTrue(wins>rounds * 0.9);
    }
}