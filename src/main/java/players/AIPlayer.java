package players;

import exceptions.GameFinishedException;
import exceptions.IllegalMoveException;
import game.Game;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class AIPlayer implements SticksPlayer {
    private Map<Integer, List<Integer>> decisionSet = new HashMap<>();
    private Map<Integer,Map<Integer,Double>> strategy = new HashMap<>();
    private int numSticks;

    /**
     * @param numSticks
     */
    public AIPlayer(int numSticks){

        this.numSticks = numSticks;
        IntStream.rangeClosed(5, numSticks).forEach(i -> this.decisionSet.put(i, IntStream.range(1, 4).boxed().collect(Collectors.toList())));
        this.decisionSet.put(1, new ArrayList<>(Collections.singletonList(1)));
        this.decisionSet.put(2, new ArrayList<>(Collections.singletonList(1)));
        this.decisionSet.put(3, new ArrayList<>(Collections.singletonList(2)));
        this.decisionSet.put(4, new ArrayList<>(Collections.singletonList(3)));
        this.updateStrategy();
    }

    @Override
    public String toString(){
        return this.strategy.toString();
    }

    private void updateStrategy(){
        this.decisionSet.keySet().forEach(turnNumber -> this.strategy.put(turnNumber, AIPlayer.probDist(this.decisionSet.get(turnNumber))));
    }
    private static Map<Integer,Double> probDist(List<Integer> moves){
        Map<Integer,Double> probDist = IntStream.range(1, 4).boxed().collect(Collectors.toMap(i -> i, i -> 0.0, (a, b) -> b));
        int sampleSize = moves.size();
        moves.forEach(i -> probDist.replace(i, probDist.get(i) + 1));
        probDist.keySet().forEach(i -> probDist.replace(i, probDist.get(i) / sampleSize));
        return Map.copyOf(probDist);
    }


    /**
     * @param rounds
     * @param trainer
     */
    public void train(int rounds, SticksPlayer trainer){
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
        Map<Integer,Integer> decisions = new HashMap<>();
        List<Integer> possibleMoves;
        int randomNum, move;
        Game game = new Game(trainer, this.numSticks, false);

        for (int round = 0; round < rounds; round++){

            decisions.clear();
            int state = ThreadLocalRandom.current().nextInt(0, 2);

            while (game.inProgress()){
                switch (state){
                    case 0:
                        possibleMoves = decisionSet.get(game.getNumSticksLeft());
                        randomNum = ThreadLocalRandom.current().nextInt(0, possibleMoves.size());
                        move = possibleMoves.get(randomNum);
                        decisions.put(game.getNumSticksLeft(),move);
                        possibleMoves.remove(randomNum);

                        try {
                            game.playMove(move);
                        } catch (IllegalMoveException | GameFinishedException e) {
                            e.printStackTrace();
                        }
                        state = 1;
                        break;

                    case 1:

                        try {
                                game.playOpponentMove();
                        } catch (IllegalMoveException | GameFinishedException e) {
                            e.printStackTrace();
                        }
                        state = 0;
                        break;
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
            game.newGame();
        }
        this.updateStrategy();
    }


    @Override
    public int getMove(int sticksRemaining){
        List<Integer> choices = this.decisionSet.get(sticksRemaining);
        int listSize = choices.size();
        return choices.get(ThreadLocalRandom.current().nextInt(0, listSize));
    }

    //Called when a game is finished. Although here we don't want to do anything
    @Override
    public void endGame() {

    }

}
