package players;

import java.util.concurrent.ThreadLocalRandom;

public class RandomPlayer implements SticksPlayer {
    @Override
    public int getMove(int sticksRemaining) {
        if (sticksRemaining < 3) return ThreadLocalRandom.current()
                .nextInt(1, sticksRemaining + 1);
        else return ThreadLocalRandom.current().nextInt(1, 4);
    }

    @Override
    public void endGame() {

    }
}
