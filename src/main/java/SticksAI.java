import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class SticksAI implements SticksPlayer {
    private Map<Integer, List<Integer>> decisionSet = new HashMap<>();
    private Map<Integer,Map<Integer,Double>> strategy = new HashMap<>();
    private int numSticks;

    public SticksAI(int numSticks){

        this.numSticks = numSticks;
        IntStream.rangeClosed(3, numSticks).forEach(i -> this.decisionSet.put(i, IntStream.range(1, 4).boxed().collect(Collectors.toList())));
        this.decisionSet.put(1, IntStream.range(1, 2).boxed().collect(Collectors.toList()));
        this.decisionSet.put(2, IntStream.range(1, 3).boxed().collect(Collectors.toList()));
        this.updateStrategy();
    }

    @Override
    public String toString(){
        return this.strategy.toString();
    }

    private void updateStrategy(){
        this.decisionSet.keySet().forEach(turnNumber -> this.strategy.put(turnNumber, SticksAI.probDist(this.decisionSet.get(turnNumber))));
    }
    private static Map<Integer,Double> probDist(List<Integer> moves){
        Map<Integer,Double> probDist = IntStream.range(1, 4).boxed().collect(Collectors.toMap(i -> i, i -> 0.0, (a, b) -> b));
        int sampleSize = moves.size();
        moves.forEach(i -> probDist.replace(i, probDist.get(i) + 1));
        probDist.keySet().forEach(i -> probDist.replace(i, probDist.get(i) / sampleSize));
        return Map.copyOf(probDist);
    }


    public void train(int rounds){
        /* TRAINING ALGORITHM
                - For simplicity the AI always plays first
                - The AI can make a choice of drawing 1, 2, or 3 sticks on each round
                - The AI starts off with a equal probability of choosing 1, 2, or 3 sticks
                - Every time the AI makes a move, it keeps track of how many sticks it draws, and how many sticks are currently in the pile before he draws
                - If the AI wins the game, he increases the probability of making each specific move for the given number of sticks in the pile
                - The increase in probability is directly proportional to how many rounds hes played in his lifetime
                - If the AI loses the game he decreases the probability of making each specific move for the given number of sticks in the pile
                - The decrease in probability is directly proportional to how many rounds hes played in his lifetime, although the probability
                for making a given move cannot be zero, unless that move is invalid (removing more sticks then left in the pile)
                -The AI trains against a random opponent, but it may be interesting to train it against another AI, that has either already been trained already,
                 or trains in parallel!
         */
        Map<Integer,Integer> decisions;
        List<Integer> possibleMoves;
        int randomNum, move, sticksRemaining;
        Game game = new Game(new RandomPlayer(), 10);

        for (int round = 0; round < rounds; round++){

            sticksRemaining = this.numSticks;
            decisions = new HashMap<>();

            while (sticksRemaining > 0){

                possibleMoves = decisionSet.get(sticksRemaining);
                randomNum = ThreadLocalRandom.current().nextInt(0, possibleMoves.size());
                move = possibleMoves.get(randomNum);
                decisions.put(sticksRemaining,move);
                possibleMoves.remove(randomNum);
                sticksRemaining -= move;

                if (sticksRemaining == 0) {
                    game.loseGame();
                    break;
                }else if(sticksRemaining == 2){
                    game.winGame();
                    break;
                } else {
                    sticksRemaining -= ThreadLocalRandom.current().nextInt(1, 4);
                    if (sticksRemaining == 0){
                        game.winGame();
                        break;
                    }
                }
            }
            if (game.isWon()){
                for(Integer stickNumber : decisions.keySet()){
                    int moveToAdd = decisions.get(stickNumber);
                    List<Integer> newChoices = new ArrayList<>();
                    IntStream.range(0, 2).forEach(i -> newChoices.add(moveToAdd));
                    this.decisionSet.get(stickNumber).addAll(List.copyOf(newChoices));
                }
            }else {
                for(Integer stickNumber : decisions.keySet()){
                    int badMove = decisions.get(stickNumber);
                    if(!decisionSet.get(stickNumber).contains(badMove)) {
                        decisionSet.get(stickNumber).add(badMove);
                    }
                }
            }
        }
        this.updateStrategy();
    }

    @Override
    public int getMove(int sticksRemaining){
        List<Integer> choices = this.decisionSet.get(sticksRemaining);
        int listSize = choices.size();
        return choices.get(ThreadLocalRandom.current().nextInt(0, listSize));
    }

}
