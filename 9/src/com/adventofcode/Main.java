package com.adventofcode;

import javafx.util.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        new Main();
    }

    private Main() {
        run();
    }

    private void run() {
        long start = System.currentTimeMillis();
        try (Stream<String> stream = Files.lines(Paths.get("input"))) {
            stream.map(row -> {
                Scanner s = new Scanner(row);

                Integer numberOfPlayers = s.nextInt();
                s.skip(" players; last marble is worth ");
                Integer lastMarbleIndex = s.nextInt();

                List<Pair<Integer, Long>> playersScore = new ArrayList<Pair<Integer, Long>>() {{
                    for (int i = 1; i <= numberOfPlayers; i++) {
                        add(new Pair<Integer, Long>(i, 0L));
                    }
                }};
                List<Integer> marbleBoard = new ArrayList<>(lastMarbleIndex);
                marbleBoard.add(0);
                Integer currentSelectedMarble = 0;
                for (int i = 1; i <= lastMarbleIndex; i++) {
                    if(i % 10000 == 0) {
                        System.out.println("MW: " + i);
                    }

                    if (i % 23 == 0) {
                        Integer currentPlayer = (i % numberOfPlayers) - 1;
                        if(currentPlayer < 0) currentPlayer = numberOfPlayers -1;
                        Integer oldKey = playersScore.get(currentPlayer).getKey();
                        Long oldScore = playersScore.get(currentPlayer).getValue();

                        Long newScore = oldScore;
                        newScore += i;
                        currentSelectedMarble -= 7;
                        if (currentSelectedMarble < 0) {
                            currentSelectedMarble = marbleBoard.size() + currentSelectedMarble;
                        }

                        Integer minusSeven = marbleBoard.get(currentSelectedMarble);
                        newScore += minusSeven;
                        marbleBoard.remove((int) currentSelectedMarble);

                        if (currentSelectedMarble >= marbleBoard.size()) {
                            currentSelectedMarble = 0;
                        }

                        playersScore.set(currentPlayer, new Pair<>(oldKey, newScore));
                    } else {
                        Integer diff = marbleBoard.size() - currentSelectedMarble - 1;

                        if (diff == 0) {
                            currentSelectedMarble += 2;

                            if (currentSelectedMarble >= marbleBoard.size()) {
                                currentSelectedMarble = -1;
                            }
                        }

                        diff = marbleBoard.size() - currentSelectedMarble - 1;

                        if (diff > 1) {
                            currentSelectedMarble += 2;
                            if (currentSelectedMarble > marbleBoard.size() - 1) {
                                currentSelectedMarble -= marbleBoard.size();
                            }
                            marbleBoard.add(currentSelectedMarble, i);
                        } else {
                            marbleBoard.add(i);
                            currentSelectedMarble = marbleBoard.size() - 1;
                        }
                    }
                }

                return playersScore;
            }).map(pairs -> pairs.stream().map(Pair::getValue).max(Comparator.comparing(Long::longValue)).orElseThrow(NoSuchElementException::new))
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

        long stop = System.currentTimeMillis();

        System.out.println("It took " + (stop - start) / 1000. + " seconds");
    }
}
