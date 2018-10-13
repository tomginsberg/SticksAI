import org.junit.Test;
import players.RandomPlayer;
import players.SticksAI;

public class SticksAITest {

    @Test
    public void aiTest(){
        SticksAI sticksAI = new SticksAI(50);
        sticksAI.train(100, new RandomPlayer());
        SticksAI betterAI = new SticksAI(50);
        betterAI.train(100000, sticksAI);

        System.out.print(betterAI.toString());
    }
}