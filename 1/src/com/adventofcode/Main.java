package com.adventofcode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
	    new Main();
    }

    public Main() {
        run();
    }

    private void run() {
        File file = new File("input");

        try (Stream<String> stream = Files.lines(Paths.get("input"))) {
            int[] ints = stream.mapToInt(Integer::valueOf).toArray();

            ArrayList<Integer> used = new ArrayList<>();
            boolean foundDuplicateTotal = false;

            int total = 0;
            int bigIterations = 0;
            while(!foundDuplicateTotal) {
                for(int i = 0; i < ints.length; i++) {
                    total += ints[i];

                    for(Integer u : used) {
                        if(u == total) {
                            System.out.println("Found the guy: " + u);
                            foundDuplicateTotal = true;
                            break;
                        }
                    }

                    if(foundDuplicateTotal) break;

                    used.add(total);
                }

                System.out.println(++bigIterations);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
