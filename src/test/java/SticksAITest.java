import org.junit.Test;

import static org.junit.Assert.*;

public class SticksAITest {

    @Test
    public void aiTest(){
        SticksAI sticksAI = new SticksAI(10);
        sticksAI.train(10000);
        System.out.print(sticksAI.toString());
    }
}