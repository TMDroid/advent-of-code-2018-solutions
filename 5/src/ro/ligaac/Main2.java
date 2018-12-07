package ro.ligaac;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Main2 {

    public static void main(String[] args) {
        new Main2();
    }

    public Main2() {
        run();
    }

    private void run() {
        long start = System.currentTimeMillis();
        Main main = new Main();

        try (Stream<String> stream = Files.lines(Paths.get("input"))) {
            String originalPolymer = (String) stream.toArray()[0];

            Map<Character, Integer> eliminations = new HashMap<>();

            for (int a = 0; a < originalPolymer.length(); a++) {
                char now = originalPolymer.charAt(a);
                String polymer = originalPolymer;

                if (!eliminations.containsKey(Character.toLowerCase(now))) {
                    boolean reacted;
                    do {
                        reacted = false;

                        for (int i = 0; i < polymer.length() - 1; i++) {
                            char current = polymer.charAt(i);

                            if (Character.toLowerCase(now) == Character.toLowerCase(current)) {
                                polymer = removeCharAtIndex(polymer, i);
                                reacted = true;
                                i--;

                            }
                        }
                    } while (reacted);

                    polymer = main.run(polymer);

                    eliminations.putIfAbsent(Character.toLowerCase(now), polymer.length());
                }
            }
            Integer smallest = eliminations.values().stream().min(Comparator.comparing(Integer::intValue)).orElseThrow(NoSuchElementException::new);

            System.out.println(smallest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();

        System.out.println("It took " + ((end-start) / 1000.) + " seconds");
    }

    private String removeCharAtIndex(String str, int index) {
        StringBuilder sb = new StringBuilder(str);
        sb.deleteCharAt(index);

        return sb.toString();
    }
}
